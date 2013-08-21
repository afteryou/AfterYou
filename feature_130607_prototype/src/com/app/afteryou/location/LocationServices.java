/*
 * (C) Copyright 2012 by TeleCommunication Systems, Inc.
 *
 * The information contained herein is confidential, proprietary
 * to TeleCommunication Systems, Inc., and considered a trade secret
 * as defined in section 499C of the penal code of the State of
 * California. Use of this information by anyone other than
 * authorized employees of TeleCommunication Systems is granted only
 * under a written non-disclosure agreement, expressly prescribing
 * the scope and manner of such use.
 *
 */

package com.app.afteryou.location;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.app.afteryou.log.Nimlog;
import com.navbuilder.nb.location.NBLocationException;
import com.navbuilder.pal.location.NBLocation;
import com.nbi.location.Location;
import com.nbi.location.LocationException;
import com.nbi.location.LocationListener;
import com.nbi.location.LocationProvider;

public class LocationServices
{
	LocationProvider provider;
	
	Map<LocationListener, LocationListener> requests = new HashMap<LocationListener,LocationListener>();
	Map<LocationListener, Timer> timers = new HashMap<LocationListener, Timer>();
	
	public static final int DEFAULT_TIMEOUT = 2 * 60 * 1000;
	public static final int NAVIGATION_TRACKING_ACCURACY = 100;
	public static final int NON_NAVIGATION_TRACKING_ACCURACY = 250;
	public static final int NARROW_TO_ACCURATE_ACCURACY = 250;
	public static final int CELLID_ACCURACY_THRESHOLD = 1000;
	
	public void setProvider(LocationProvider provider)
	{
		this.provider = provider;
	}
	
	public void cancelLocationRequest(final LocationListener listener)
	{
		LocationListener internalListener = requests.get(listener);
		try
		{
			Nimlog.i(this, "Cancel request... " + listener);
			if (internalListener == null)
			{
				Nimlog.e(this, "Unable to retrieve internal listener");
				return;
			}
			requests.remove(listener);
			provider.cancelGetLocation(internalListener);
		}
		catch (LocationException e)
		{
			Nimlog.e(this, "Exception in cancelRequest, NBI code = " + e.getErrorCode());
			listener.onLocationError(e.getErrorCode());
		}
		finally
		{
			Timer canceledTimer = timers.get(internalListener);
			if (canceledTimer != null) { canceledTimer.cancel(); canceledTimer.purge(); timers.remove(internalListener); }
		}
	}
	
	public void startOneFix(final LocationListener listener, int mode) {
		try	{
			Nimlog.i(this, "Starting startOneFix with mode " + mode);
			provider.getOneFix(listener, mode);
			requests.put(listener, listener);
		} catch (LocationException e) {
			Nimlog.e(this, "Exception in startOneFix, NBI code = " + e.getErrorCode());
			listener.onLocationError(e.getErrorCode());
		}
	}
	
	public void startNavigationTracking(final LocationListener listener, boolean initialNetwork)
	{
		startNavigationTracking(listener, NAVIGATION_TRACKING_ACCURACY, initialNetwork);
	}
	
	public void startNavigationTracking(final LocationListener listener, final int accuracy, final boolean initialNetwork) {
		
		LocationListener internalListener=new LocationListener(){

			boolean hasReceivedGPS = !initialNetwork;
			
			@Override
			public void locationUpdated(Location location) {
				if ((location.getLocationType() == NBLocation.FIX_TYPE_GPS && location.getAccuracy() <= accuracy) || !hasReceivedGPS) {
					hasReceivedGPS = hasReceivedGPS || location.getLocationType() == NBLocation.FIX_TYPE_GPS;
					listener.locationUpdated(location);
				} else {
					if ((location.getLocationType() == NBLocation.FIX_TYPE_GPS && location.getAccuracy() > accuracy)) {
//						QALogger.logGpsFixFiltered(Utilities.convertToPosition(location));
					}
				}
			}

			@Override
			public void providerStateChanged(int newState) {
				listener.providerStateChanged(newState);
			}

			@Override
			public void onLocationError(int errorCode) {
				listener.onLocationError(errorCode);
			}};
		
		try	{
			Nimlog.i(this, "Starting startNavigationTracking with: " + internalListener);
			provider.startReceivingFixes(internalListener);
			requests.put(listener, internalListener);
		} catch (LocationException e) {
			Nimlog.e(this, "Exception in startNavigationTracking, NBI code = " + e.getErrorCode());
			listener.onLocationError(e.getErrorCode());
		}
	}
	
	public void startNonNavigationTracking(final LocationListener listener, boolean initialCellID) {
		startNonNavigationTracking(listener, NON_NAVIGATION_TRACKING_ACCURACY, initialCellID);
	}
	
	public void startNonNavigationTracking(final LocationListener listener, int accuracy, final boolean allowInitialCellID)	{
		LocationListener internalListener=new LocationListener(){

			boolean gotCellID = !allowInitialCellID;
			
			@Override
			public void locationUpdated(Location location) {
				if (gotCellID &&
						location.getLocationType() == NBLocation.FIX_TYPE_NETWORK &&
						location.getAccuracy() > CELLID_ACCURACY_THRESHOLD) {
						Nimlog.i(this, "got uninterested CellID, discarding");
						return;
					}
					
				gotCellID = true;
				listener.locationUpdated(location);
			}

			@Override
			public void providerStateChanged(int newState) {
				listener.providerStateChanged(newState);
			}

			@Override
			public void onLocationError(int errorCode) {
				listener.onLocationError(errorCode);
				
			}};
		
		
		try
		{
			Nimlog.i(this, "Starting startNonNavigationTracking with: " + internalListener);
			provider.startReceivingFixes(internalListener);
			requests.put(listener, internalListener);
		}
		catch (LocationException e)
		{
			Nimlog.e(this, "Exception in startNonNavigationTracking, NBI code = " + e.getErrorCode());
			listener.onLocationError(e.getErrorCode());
		}
	}
	
	public void singleshotWithinAccuracy(LocationListener listener, int accuracy, long timeoutDuration, boolean errorOnTimeout)
	{
		startFixStream(listener, accuracy, timeoutDuration, errorOnTimeout, false);
	}
	
	public void narrowToAccurate(final ILocationStreamListener listener)
	{
		narrowToAccurate(listener, NARROW_TO_ACCURATE_ACCURACY, DEFAULT_TIMEOUT);
	}
	
	public void narrowToAccurate(ILocationStreamListener listener, int accuracy, long timeoutDuration)
	{
		startFixStream(listener, accuracy, timeoutDuration, false, true);
	}
	
	private void startFixStream(LocationListener listener, int accuracy,
			long timeout, boolean errorOnTimeout, boolean intermediateFixes)
	{
		final TimedListener internalListener = new TimedListener(listener, accuracy, errorOnTimeout, intermediateFixes);
		try
		{
			Nimlog.i(this, "Starting fix stream with: " + internalListener);
			provider.startReceivingFixes(internalListener);
			requests.put(listener, internalListener);
			
			if (timeout > 0) {
				Timer timeoutTimer = new Timer("LOCATION_SERVICES_TIMEOUT_TIMER");
				TimerTask timeoutTask = new TimerTask()
				{
					@Override
					public void run()
					{
						internalListener.timeout();
					}	
				};
				timers.put(internalListener, timeoutTimer);
				timeoutTimer.schedule(timeoutTask, timeout);
			}
		}
		catch (LocationException e)
		{
			Nimlog.e(this, "Exception in startFixStream, NBI code = " + e.getErrorCode());
			listener.onLocationError(e.getErrorCode());
		}
	}
	
	private class TimedListener implements LocationListener {
		private Location bestLocation = null;
		private int targetAccuracy;
		private boolean intermediateFixes;
		private boolean errorOnTimeout;
		private LocationListener listener;
		
		public TimedListener(LocationListener listener, int accuracy,
				boolean errorOnTimeout, boolean intermediateFixes) {
			this.listener = listener;
			this.targetAccuracy = accuracy;
			this.intermediateFixes = intermediateFixes;
			this.errorOnTimeout = errorOnTimeout;
		}
		
		public void timeout() {
			Nimlog.i(this, "Timeout on " + this);
			
			if (listener instanceof ILocationStreamListener) {
				((ILocationStreamListener)listener).onComplete();
			}
			if (!intermediateFixes) {
				if (bestLocation != null && !errorOnTimeout) {
					listener.locationUpdated(bestLocation);
				}
				else {
					listener.onLocationError(NBLocationException.NBI_ERROR_NO_LOCATION_AVAILABLE);
				}
			}
			
			cancelLocationRequest(listener);
		}
		
		@Override
		public void locationUpdated(Location location) {
			if (location == null ||
				location.getAccuracy() == 0 ||
				(location.getLatitude() == 0.0 && location.getLongitude() == 0.0)) {
//				QALogger.logGpsFixFiltered(Utilities.convertToPosition(location));
				Nimlog.e(this, "Received invalid location");
				return;
			}
			if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
				bestLocation = location;
				if (intermediateFixes) {
					listener.locationUpdated(location);
				}
			}
			if (location.getAccuracy() < targetAccuracy) {
				Nimlog.i(this, "Location within " + targetAccuracy +
						"m found (" + location.getAccuracy() + "m)");
				if (!intermediateFixes) {
					listener.locationUpdated(location);
				}
 				Nimlog.i(this, "finished: " + this);
				cancelLocationRequest(listener);
				if (listener instanceof ILocationStreamListener) {
					((ILocationStreamListener)listener).onComplete();
				}
			}
		}

		@Override
		public void onLocationError(int errorCode) {
			cancelLocationRequest(listener);
			listener.onLocationError(errorCode);
		}
		
		@Override
		public void providerStateChanged(int state)	{
			listener.providerStateChanged(state);
		}
	}
}
