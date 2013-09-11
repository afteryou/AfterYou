package com.beacon.afterui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.constants.AppConstants;


public class AppUtils {

	public static boolean isFirstTimeStart() {
		Context ctx = AfterYouApplication.getInstance().getApplicationContext();
		SharedPreferences p = ctx.getSharedPreferences(AppConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
		return p.getBoolean(AppConstants.PREF_KEY_IS_FTT, true);
	}

	public static void setFirstTimeStart(final boolean isFTT) {
		final Context ctx = AfterYouApplication.getInstance().getApplicationContext();
		new Thread(new Runnable() {
			public void run() {
				SharedPreferences p = ctx.getSharedPreferences(AppConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
				Editor editor = p.edit();
				editor.putBoolean(AppConstants.PREF_KEY_IS_FTT, isFTT);
				editor.commit();
			}
		}).start();
	}
}
