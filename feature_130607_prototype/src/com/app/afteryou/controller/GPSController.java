package com.app.afteryou.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;

import com.app.afteryou.common.Credentials;
import com.app.afteryou.constants.AppConstants;
import com.app.afteryou.location.ILocationStreamListener;
import com.app.afteryou.location.LocationServices;
import com.app.afteryou.log.Nimlog;
import com.app.afteryou.preference.PreferenceEngine;
import com.navbuilder.nb.location.NBLocationProvider;
import com.navbuilder.pal.location.NBLocation;
import com.nbi.common.NBIContext;
import com.nbi.location.LocationConfig;
import com.nbi.location.LocationException;
import com.nbi.location.LocationListener;
import com.nbi.location.LocationProvider;

public class GPSController {

	public static int FAST_MODE=LocationProvider.LOCATION_FIX_FAST;
	public static int NORMAL_MODE=LocationProvider.LOCATION_FIX_NORMAL;
	public static int ACCURATE_MODE=LocationProvider.LOCATION_FIX_ACCURATE;
	
	private static GPSController instance;
	private Context context;
	private HandlerThread looperThread;
	private Handler handler;
	private LocationProvider provider;
	private LocationServices services;

	private Map<LocationListener, GPSListener> listeners = new HashMap<LocationListener, GPSListener>();
	
	private long latestGPSTimeStamp;
	private com.nbi.location.Location latestGPSLocation;
	
	private String TAG = "GPSController";

	public static GPSController getInstance(Context context) {
		if (instance == null) {
			instance = new GPSController(context);
		}
		return instance;
	}

	private GPSController(Context context) {
		this.context = context.getApplicationContext();

		services = new LocationServices();
		initHandler();
		initProvider();
	}

	private void initHandler() {
		if (looperThread != null) {
			looperThread.quit();
		}

		looperThread = new HandlerThread("gpsThread");
		looperThread.start();
		handler = new Handler(looperThread.getLooper());

	}

	public void initProvider() {
		Nimlog.d(TAG, "[GPSController][initProvider]start");
		LocationConfig config = LocationConfig.createLocationConfig();
		if (!isRealGPS()) {
			Nimlog.d(TAG, "[GPSController][initProvider]use mock gps");
			config.setAllowMockLocation(true);
			String gpsfile = PreferenceEngine.getInstance().getGpsFileName();
			if (gpsfile != null && !gpsfile.equals("")) {
				config.setLocationFilename(gpsfile);
			}
		}else{
			config.setLocationFilename(NBLocationProvider.REAL_GPS);
		}

		try {
			String token=Credentials.instance().getValue();
			Nimlog.d(TAG, "token:"+token);
			provider = LocationProvider.getInstance(new NBIContext(context,token,null), config);
		} catch (Exception e) {
			provider = null;
			Nimlog.e(TAG, "[GPSController][initProvider]error in initialize location provider", e);
		}
	}
	
    private void setProvider(){
    	services.setProvider(provider);
    }
	
	public void requestSingleFix(LocationListener observer,int mode) {
		requestSingleFix(observer,mode, false);
	}

	public void requestSingleFix(final LocationListener observer,final int mode, final boolean allowCache) {

		Runnable r = new Runnable() {
			public void run() {
				if (allowCache && isLegalCacheFix()) {
					Nimlog.i(TAG, "Using cached fix: " + latestGPSLocation);
					observer.locationUpdated(latestGPSLocation);
					return;
				}
				
				setProvider();
				GPSListener listener = new SingleFixGPSListener(observer);
				services.startOneFix(listener, mode);
				listeners.put(observer, listener);
				
			}
		};

		postToGPSThread(r);
	}
	
    public void requestSingleFixWithinAccuracy(LocationListener observer,int accuracy) {
    	requestSingleFixWithinAccuracy(accuracy, true, 0, false, observer);
    }
    
    public void requestSingleFixWithinAccuracy(final int accuracy, final boolean allowCache,
    		final int timeout, final boolean errorOnTimeout, final LocationListener observer){
    	Runnable r = new Runnable()
		{
			public void run()
			{
		    	Nimlog.i(TAG, "LS-BEGIN: singleshotWithinAccuracy; " +
		    			 "Target accuracy = " +
		    			 accuracy +
		    			 ", Maximum wait time = " +
		    			 timeout +
		    			 "ms");
		    	if (allowCache && isLegalCacheFix()) {
					Nimlog.i(TAG, "Using cached fix: " + latestGPSLocation);
					observer.locationUpdated(latestGPSLocation);
					return;
				}

		    	setProvider();
		    	GPSListener listener = new SingleFixGPSListener(observer);
		    	services.singleshotWithinAccuracy(listener, accuracy, timeout, errorOnTimeout);
				listeners.put(observer, listener);
			}
		};
    	
       	postToGPSThread(r);
	}
    
    public void narrowToAccurate(LocationListener observer) {
    	narrowToAccurate(observer, true);
    }
    
	public void narrowToAccurate(final LocationListener observer, final boolean allowCache){
		
		Runnable r = new Runnable()
		{
			public void run()
			{
				int accuracy=250;
				int timeout=120000;
				Nimlog.i(TAG, "LS-BEGIN: narrowToAccurate process; " +
		    			 "Target accuracy = " +
		    			 accuracy +
		    			 ", Maximum wait time = " +
		    			 timeout +
		    			 "ms");
				if (allowCache && isLegalCacheFix()) {
					Nimlog.i(TAG, "Using cached fix: " + latestGPSLocation);
					observer.locationUpdated(latestGPSLocation);
					return;
				}
				setProvider();
				StreamGPSFixListener listener = new StreamGPSFixListener(observer);
				services.narrowToAccurate(listener, accuracy, timeout);
				listeners.put(observer, listener);
			}
		};
       	postToGPSThread(r);
	}
	
	public void startNonNavigationTracking(final boolean initialCellID, final LocationListener observer)
	{
		Runnable r = new Runnable()
		{
			public void run()
			{
				int timeout=30000;
				Nimlog.i(TAG, "LS-BEGIN: startNonNavigationTracking process; " +
		    			 "Target accuracy = " +
		    			 LocationServices.NON_NAVIGATION_TRACKING_ACCURACY +
		    			 ", Timeout = " +
		    			 timeout +
		    			 "ms");
				setProvider();
				GPSListener listener = new TimeoutGPSFixListener(observer);
				services.startNonNavigationTracking(listener, initialCellID);
				listeners.put(observer, listener);
			}
		};
		
       	postToGPSThread(r);
	}
	
	public void startNavigationTracking(final LocationListener observer, final boolean initialNetwork){
		startNavigationTracking(observer, initialNetwork, false);
	}
	
	public void startNavigationTracking(final LocationListener observer, final boolean initialNetwork, final boolean checkDuplication)
	{
		Runnable r = new Runnable()
		{
			public void run()
			{
				int timeout=30000;
				Nimlog.i(TAG, "LS-BEGIN: startNavigationTracking process; " +
		    			 "Target accuracy = " +
		    			 LocationServices.NAVIGATION_TRACKING_ACCURACY +
		    			 ", Timeout = " +
		    			 timeout +
		    			 "ms");
				if(checkDuplication && listeners.containsKey(observer)) {
					Nimlog.i(TAG, "Already tracking, don't start: " + observer);
					return;
				}
				setProvider();
				GPSListener listener = new TimeoutGPSFixListener(observer);
				services.startNavigationTracking(listener, initialNetwork);
				listeners.put(observer, listener);
			}
		};
		
       	postToGPSThread(r);
	}

    public void cancelLocationRequest(LocationListener key){
    	clearLocationRequest(key, true);
    }
	
	public void clearLocationRequest(final LocationListener key,final boolean needCallBack) {
		Runnable r = new Runnable() {
			public void run() {
				GPSListener listener = listeners.get(key);
				
				if(listener==null){
					Nimlog.e(TAG, "LS-CANCEL: No match for key " + key + " in " + listeners.keySet());
					return;
				}
				
				listener.shutdown();
				services.cancelLocationRequest(listener);
				listeners.remove(key);

	    		if(needCallBack){
	    			//TODO: need to define one constant for cancel 
	    			listener.onLocationError(-1);
	    		}
				
			}
		};
		
		postToGPSThread(r);
	}

	private void postToGPSThread(Runnable r) {
		// fix the bug that app failed to post the runnable to the queue.
		boolean isPostSuccess = handler.post(r);
		if (!isPostSuccess) {
			initHandler();
			handler.post(r);
		}
	}

	public synchronized void shutdown() {
		for (GPSListener listener : listeners.values()) {
			listener.shutdown();
			try {
				provider.cancelGetLocation(listener);
			} catch (LocationException e) {
				Nimlog.e(TAG, "[GPSController][shutdown]exception in cancel request", e);
			}
		}
		listeners.clear();
		provider = null;

		latestGPSLocation = null;
		latestGPSTimeStamp = 0;
	}

	public com.nbi.location.Location getLastestLocation() {
		return latestGPSLocation;
	}

	private synchronized void updateLatestLocation(com.nbi.location.Location loc) {
		latestGPSLocation = loc;
		latestGPSTimeStamp = System.currentTimeMillis();
	}

	private boolean isLegalCacheFix() {
		if (latestGPSLocation == null)
			return false;
		return System.currentTimeMillis() - latestGPSTimeStamp < AppConstants.LAST_KNOWN_GPS_TIMEOUT;
	}

	public boolean isRealGPS() {
		String str = PreferenceEngine.getInstance().getGpsFileName();
		if (str.endsWith(".gps")) {
			return false;
		}
		return true;
	}

	public boolean isGPSEnable(){
		
		if(!isRealGPS()){
			Nimlog.d(TAG, "[GPSController][isGPSEnable]use mock gps");
			return true;
		}
		
		LocationManager androidLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return androidLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	private abstract class GPSListener implements LocationListener {
		protected LocationListener observer;

		protected GPSListener(LocationListener observer) {
			this.observer = observer;
		}

		@Override
		public void locationUpdated(com.nbi.location.Location location) {
			Nimlog.d(TAG, "[GPSEngineListener][locationUpdated]start");
			if (location == null)
				return;

			Nimlog.i(TAG,"LS-RESULT: (" + location.getLatitude() + "," + location.getLongitude() + ") Accuracy = "
							+ location.getAccuracy() + " Heading = " + location.getHeading() + " Speed = "
							+ location.getHorizontalVelocity() + " Listener = " + observer);
			if (location.getAccuracy() == 0 || (location.getLatitude() == 0.0 && location.getLongitude() == 0.0))
				return;

//			// Log to QALog
//	        if (PreferenceEngine.getInstance(context).isQALogEnabled() && androidLoc.isNavigable()
//	        		&& !isRealGPS()) {
//	            QALogger.logGpsFix(Utilities.converToPosition(androidLoc));
//	        }
			
			observer.locationUpdated(location);
			updateLatestLocation(location);
		}

		@Override
		public void onLocationError(int errorCode) {
			Nimlog.d(TAG, "[GPSEngineListener][onLocationError]start");
			observer.onLocationError(errorCode);
		}

		@Override
		public void providerStateChanged(int state) {
			Nimlog.d(TAG, "[GPSEngineListener][providerStateChanged]start");
			observer.providerStateChanged(state);
		}

		public void shutdown() {
			// nothing to do
		}
	}

	private class SingleFixGPSListener extends GPSListener {
		
		public SingleFixGPSListener(LocationListener observer) {
			super(observer);
		}

		@Override
		public void locationUpdated(final com.nbi.location.Location location) {
			super.locationUpdated(location);
			clearLocationRequest(observer,false);
		}

		@Override
		public void onLocationError(int errorCode) {
			super.onLocationError(errorCode);
			clearLocationRequest(observer,false);
		}
	}
	
	
	private class StreamGPSFixListener extends GPSListener implements ILocationStreamListener {

		public StreamGPSFixListener(LocationListener observer) {
			super(observer);
		}

		@Override
		public void onComplete() {
			Nimlog.i(TAG, "fix stream complete");
			listeners.remove(observer);
			return;
		}
		
	}
	
	private class TimeoutGPSFixListener extends GPSListener {

		private Timer timeoutTimer;
		private TimerTask timerTask;
		
		public TimeoutGPSFixListener(LocationListener observer) {
			super(observer);
			startTimeoutTimer();
		}
		
		@Override
		public void locationUpdated(com.nbi.location.Location location) {
			
			if (location.getLocationType() == NBLocation.FIX_TYPE_GPS && 
				timeoutTimer != null) {
				startTimeoutTimer();
			}
			Nimlog.d(TAG, "[TrackingGPSListener][locationUpdated]tracking gps, listener:"+this);
			super.locationUpdated(location);
		}
		
		private synchronized void startTimeoutTimer(){
			if(timeoutTimer != null){
				timeoutTimer.cancel();
				timeoutTimer.purge();
				timeoutTimer = null;
				timerTask = null;
			}

			final int timeout=30000;
			timeoutTimer = new Timer("GPS_TIME_OUT");

			timerTask = new TimerTask(){
				@Override
				public void run(){
					Nimlog.d(TAG, "[TrackingGPSListener][startTimeoutTimer]No GPS received in " + timeout + "s; timing out (GPSObserver " + observer + ")");
					onLocationError(LocationException.NBI_ERROR_GPS_TIMEOUT);
				}
			};

			timeoutTimer.scheduleAtFixedRate(timerTask, timeout,timeout);
		}
		
		public synchronized void shutdown(){
			if (timeoutTimer != null){
				timeoutTimer.cancel();
				timeoutTimer.purge();
				timeoutTimer = null;
			}
		}
	}

}
