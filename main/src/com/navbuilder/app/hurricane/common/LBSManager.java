/*--------------------------------------------------------------------------

    @file     LBSManager.java
    @date     05/13/2010

    LBSManager class definition.

    (C) Copyright 2010-2011 by TeleCommunication Systems, Inc.

    The files and materials contained herein are the property of
    TeleCommunication Systems, Inc. (TCS) and their licensors.
    All use is subject to a license agreement between you and TCS.
    Contact us at contactus@navbuilderinside.com if you did not
    receive a copy of the license agreement when downloading or
    otherwise receiving these files and materials.

---------------------------------------------------------------------------*/

package com.navbuilder.app.hurricane.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.navbuider.app.hurricane.uiflow.HurricaneAppController;
import com.navbuilder.app.hurricane.preference.PreferenceEngine;
import com.navbuilder.location.LocationWrapper;
import com.navbuilder.pal.android.ndk.GPSLocationManager;
import com.navbuilder.pal.android.ndk.PalRadio;
import com.navbuilder.sc.ControllerManager;
import com.navbuilder.sc.ControllerManagerConfig;
import com.nbi.common.NBIContext;
import com.nbi.common.NBIContextListener;
import com.nbi.search.ui.Search;
import com.tcs.jcc.common.NBAssetManager;
import com.navbuilder.app.hurricane.R;

/* This class is required for NBI functionality to operate.
   The NBIContext object must be created before any NBI calls are made.
   The LocationProvider object must be created before any calls to LocationKit are made. */
public class LBSManager {
    private final static String TAG = "LBSManager";    
    private static NBIContext sNBIContext;
    private static String sCurrentCredential;
    public final static String PREFERENCE_KEY = "com.nbi.testapp.common.PreferenceCredential";
    public final static String PREFERENCE_VALUE_KEY = "com.nbi.testapp.common.CurrentCredential.Value";
    public final static String USE_OWN_NETWORK_LOCATION_KEY = "com.nbi.testapp.common.location.useOwnNetworkLocation";
    public final static String ALLOW_MOCK_LOCATION_KEY = "com.nbi.testapp.common.location.allowMockLocation";
    public final static String ALLOW_WIFI_PROBE_COLLECTION = "com.nbi.testapp.common.location.allowWifiProbeCollection";
	private static boolean mUseOwnNetworkLocation = true;
	private static boolean 	mAllowMockLocation = false;
	private static boolean 	mAllowWifiProbeCollection = true;
	private static Context mAppContext = null;
	private static NBAssetManager mAssertMAnager;
	private static boolean mapConfigInitialize = false;
	
	
    public static boolean hasInitialized() {
      return sNBIContext != null;
    }

    //Initiates NBI context with defined API credentials, language code and listener object.
	public static void init(Context context, String credential, String carrier, NBIContextListener listener) {
		
        Log.d(TAG, "init()");   
        if(mAppContext==null){
        	mAppContext = context.getApplicationContext();
        }
        if (sNBIContext == null){
            sNBIContext = new NBIContext(context, credential, null, null, carrier);
        }
        sCurrentCredential = credential;
        sNBIContext.setContextErrorListener(listener);
	    
	    LocationWrapper.getInstance().init(sNBIContext,context,PreferenceEngine.getInstance().getGpsFileName());
	    Search.init(sNBIContext, context);
	    initMapKit();
    }

	private static void initMapKit() {
		// Copy assets
		mAssertMAnager = new NBAssetManager(mAppContext);

		// Start initializing
		new SetupTask().execute();

		// TODO: need to do in other place
		// PalRadio.init(this.getApplicationContext());
		// GPSLocationManager.init(this.getApplicationContext());
		PalRadio.init(mAppContext);
		GPSLocationManager.init(mAppContext);
	}
	
	private static class SetupTask extends AsyncTask<Void, Void, Boolean> {
//		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
//			dialog = ProgressDialog.show(mAppContext, "", "Initializing... Please wait...", true);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// Copy files from Asserts to cache folder
			boolean ret = mAssertMAnager.setupAssets();

			// Load Native libraries
			ret = !ret ? ret : ControllerManager.staticInitialize(mAssertMAnager.getControllerManagerProperties());
			return Boolean.valueOf(ret);
		}

		@Override
		protected void onPostExecute(Boolean result) {
//			dialog.dismiss();

			if (result == Boolean.TRUE) {
				showUI();
			} else {
				AlertDialog dialog = new AlertDialog.Builder(mAppContext).create();
				dialog.setTitle(R.string.IDS_ERROR);
				dialog.setMessage(mAppContext.getString(R.string.IDS_INITIALIZATION_FAILED));
				dialog.setButton(Dialog.BUTTON_POSITIVE, mAppContext.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						//MainActivity.this.finish();
					}
				});
				dialog.show();
			}
		}
	}
	
	private static void showUI() {
		initControllerManager();

		ControllerManager cm = ControllerManager.getInstance();
		cm.getScreenManager().setCurrentContext(mAppContext);
		cm.showUI();
		// We don't need Launcher anymore. Let's finish it
		// finish();
	}
	
	private static boolean initControllerManager() {
		if (!mapConfigInitialize) {

			String workFolder = mAssertMAnager.getAssetFolder();

			ControllerManagerConfig config = new ControllerManagerConfig();
			config.setTpsfile(workFolder + "/appconfig/tesla.tpl");
			config.setLanguage("en");
			config.setCountry("US");
			config.setUid("80b053fccead422934b9a3140b72983c450f8832");
			config.setMapCacheDirectory(workFolder + "/Map");
			config.setVoiceCacheDirectory(workFolder + "/Voice");
			config.setQalogFilename(workFolder + "/qalog");
			config.setCarrierName("Verizon");
			config.setWorkFolder(workFolder);
			config.setMapkitCachePath(workFolder + "/mapkit");
			config.setResourceFolderPath(workFolder);
			mapConfigInitialize = true;
			return ControllerManager.initialize(config);
		}
		return true;
	}
	
    public static Context getAppContext() {
        return mAppContext;
    }

    //Destroys NBI context. Is called before finished main application activity.
    public static void destroy() {
    	Log.d(TAG, "destroy()");            
		if (sNBIContext != null) {
			sNBIContext.destroy();
			sNBIContext = null;
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

}
