
package com.app.afteryou.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.afteryou.search.CouponsStorage;
import com.nbi.common.NBIContext;
import com.nbi.common.NBIContextListener;
import com.nbi.location.LocationConfig;
import com.nbi.location.LocationException;
import com.nbi.location.LocationProvider;

/* This class is required for NBI functionality to operate.
   The NBIContext object must be created before any NBI calls are made.
   The LocationProvider object must be created before any calls to LocationKit are made. */
public class LBSManager {
    private final static String TAG = "LBSManager";    
    private static int sReferenceCount = 0;
    private static NBIContext sNBIContext;
    private static String sCurrentCredential;
    private static LocationProvider sLocationProvider = null;
    public final static String PREFERENCE_KEY = "com.nbi.testapp.common.PreferenceCredential";
    public final static String PREFERENCE_VALUE_KEY = "com.nbi.testapp.common.CurrentCredential.Value";
    public final static String USE_OWN_NETWORK_LOCATION_KEY = "com.nbi.testapp.common.location.useOwnNetworkLocation";
    public final static String ALLOW_MOCK_LOCATION_KEY = "com.nbi.testapp.common.location.allowMockLocation";
    public final static String ALLOW_WIFI_PROBE_COLLECTION = "com.nbi.testapp.common.location.allowWifiProbeCollection";
	private static boolean mUseOwnNetworkLocation = true;
	private static boolean 	mAllowMockLocation = false;
	private static boolean 	mAllowWifiProbeCollection = true;
	private static Context mAppContext = null;


    public static boolean hasInitialized() {
      return sNBIContext != null;
    }

    //Initiates NBI context with defined API credentials, language code and listener object.
	public static void init(Context context, String credential, String carrier, NBIContextListener listener) {
	    if (sReferenceCount <= 0) {
            Log.d(TAG, "init()");   	        
	        sReferenceCount = 1;
	        mAppContext = context.getApplicationContext();
	        if (sNBIContext == null)
	            sNBIContext = new NBIContext(context, credential, null, null, carrier);
	        sCurrentCredential = credential;
	        sNBIContext.setContextErrorListener(listener);
	    } else {
	        sReferenceCount++;
	    }
    }

    //Destroys NBI context. Is called before finished main application activity.
    public static void destroy() {
        sReferenceCount--;
        if (sReferenceCount <= 0) {
            Log.d(TAG, "destroy()");            
            CouponsStorage.release();
            destroyLocationManager();
            if(sNBIContext != null)
            {
                sNBIContext.destroy();
                sNBIContext = null;
            }
        }
    }

    //Returns NBIContext object
    public static NBIContext getNBIContext() {
        return sNBIContext;
    }

    //Returns current credential string
    public static String getCurrentCredential() {
        return sCurrentCredential;
    }

    //Destroys location provider object to change location preference settings
    public static void destroyLocationManager() {
    	if(sLocationProvider != null)
    	{
    		sLocationProvider.onDestroy();
    	}
        sLocationProvider = null;
    }

    //Updates location preferences
    private static void updatePreferences(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);

        if(sharedPref != null){
            mUseOwnNetworkLocation = sharedPref.getBoolean(LBSManager.USE_OWN_NETWORK_LOCATION_KEY, mUseOwnNetworkLocation);
            mAllowMockLocation = sharedPref.getBoolean(LBSManager.ALLOW_MOCK_LOCATION_KEY, mAllowMockLocation);
            mAllowWifiProbeCollection = sharedPref.getBoolean(LBSManager.ALLOW_WIFI_PROBE_COLLECTION, mAllowWifiProbeCollection);
        }

    }

    //Returns LocationProvider object
    public static LocationProvider getLocationProvider() {
        try {
        	if(sLocationProvider == null)
        	{
	        	updatePreferences(mAppContext);
		        LocationConfig config = LocationConfig.createLocationConfig();
		        config.setUseOwnNetworkLocation(mUseOwnNetworkLocation);
		        config.setAllowMockLocation(mAllowMockLocation);
	            sLocationProvider = LocationProvider.getInstance(sNBIContext, config);
        	}
        } catch(LocationException e) {
            e.printStackTrace();
            sLocationProvider=null;
        }

        return sLocationProvider;
    }
}
