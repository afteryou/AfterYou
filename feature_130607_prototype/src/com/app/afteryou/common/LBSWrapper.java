/*--------------------------------------------------------------------------

    @file     LBSWrapper.java
    @date     01/14/2013

    LBSWrapper class definition.

    (C) Copyright 2010-2013 by TeleCommunication Systems, Inc.

    The files and materials contained herein are the property of
    TeleCommunication Systems, Inc. (TCS) and their licensors.
    All use is subject to a license agreement between you and TCS.
    Contact us at contactus@navbuilderinside.com if you did not
    receive a copy of the license agreement when downloading or
    otherwise receiving these files and materials.

---------------------------------------------------------------------------*/
package com.app.afteryou.common;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.nbi.common.NBIContext;
import com.nbi.common.NBIContextListener;
import com.nbi.common.NBIException;
import com.nbi.location.LocationProvider;

/**
 * The reason the class is needed due to the fact that when the 
 * MainMapView activity exits, the MainMapView.onDestroy() might not be called
 * immediately.  This causes problem if another MainMapView activity is started before
 * the previous instance's onDestroy(), which call LBSManager.onDestroy() for clean up,
 * has not been called.  This results in the application crashing.
 * 
 * To alleviate this problem, now LBSManager.init() and LBSManager.destroy() are now reference counted.
 * LBSWrapper implements the adapter design pattern that translates the 
 * LBSManager.init() to LBSWrapper constructor and other methods are forward to LBSManager's methods.
 */
public class LBSWrapper {
    private final static String TAG = "LBSWrapper";
    
    public static class LBSContextListener implements NBIContextListener {
        Context mApplicationContext;
        public LBSContextListener(Context context) {
            mApplicationContext = context.getApplicationContext();
        }
        //Shows toast with appropriate message in case on NBI error
        @Override
        public void onError(NBIException exception) {
            String displayStr = ErrorInformation.getErrorMessage(exception.getErrorCode());
            Toast t = Toast.makeText(mApplicationContext, displayStr, Toast.LENGTH_SHORT);
            t.setGravity(Gravity.BOTTOM, 0, 0);
            t.show();
        }
    }
    
    public LBSWrapper(Context context, String credential, String carrier, NBIContextListener listener) {
        Log.d(TAG, "init()");  
        LBSManager.init(context, credential, carrier, listener);
    }    
    
    public NBIContext getNBIContext() {
        return LBSManager.getNBIContext();
    }
    
    public LocationProvider getLocationProvider() {
        return LBSManager.getLocationProvider();
    }
    
    public void destroy() {
        Log.d(TAG, "destroy()");           
        LBSManager.destroy();
    }
}
