package com.navbuilder.app.hurricane.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.navbuilder.app.hurricane.HurricaneApplication;
import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.constants.AppConstants;
import com.navbuilder.app.hurricane.utils.Nimlog;

public class PreferenceEngine {

	private static PreferenceEngine instance;
	private Context appContext = HurricaneApplication.getInstance().getApplicationContext();

	public synchronized static PreferenceEngine getInstance() {

		if (instance == null) {
			instance = new PreferenceEngine();
		}
		return instance;

	}

	private PreferenceEngine() {
	}

	public void saveGpsFileName(String fileName) {
		SharedPreferences preference = getDefaultSharePreference();
		SharedPreferences.Editor editor = preference.edit();
		editor.putString(AppConstants.PREF_KEY_GPS_FILE, fileName);
		editor.commit();
	}

	public String getGpsFileName() {
		String s = getDefaultSharePreference().getString(AppConstants.PREF_KEY_GPS_FILE,
				appContext.getString(R.string.REAL_GPS));
		Nimlog.d("", "[PreferenceEngine][getGpsFileName]gps file: " + s);
		return s;
	}

	public void saveLastLocation(double latitude, double longitude) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.PREF_KEY_LAST_LOCATION, System.currentTimeMillis() + "," + latitude + ","
				+ longitude);
		editor.commit();
	}

	public android.location.Location getLastLocation() {
		String locString = getDefaultSharePreference().getString(AppConstants.PREF_KEY_LAST_LOCATION, null);
		if (locString == null) {
			 return null;
		}
		String[] locInfo = locString.split(",");
		android.location.Location location = new android.location.Location("Last Location");
		location.setTime(Long.valueOf(locInfo[0]));
		location.setLatitude(Double.valueOf(locInfo[1]));
		location.setLongitude(Double.valueOf(locInfo[2]));
		return location;
	}

	public void saveToken(String token) {
		SharedPreferences preference = getDefaultSharePreference();
		SharedPreferences.Editor editor = preference.edit();
		editor.putString(AppConstants.PREF_KEY_SERVER_TOKEN, token);
		editor.commit();
	}

	public String getToken() {
		String s = getDefaultSharePreference().getString(AppConstants.PREF_KEY_SERVER_TOKEN, AppConstants.TOKEN);
		Nimlog.d("", "[PreferenceEngine][getToken]: " + s);
		return s;
	}

	private SharedPreferences getDefaultSharePreference() {
		return appContext.getSharedPreferences(AppConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
	}
	
	public static boolean isFirstTimeStart() {
		Context ctx = HurricaneApplication.getInstance().getApplicationContext();
		SharedPreferences p = ctx.getSharedPreferences(AppConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
		return p.getBoolean(AppConstants.PREF_KEY_IS_FTT, true);
	}

	public static void setFirstTimeStart(final boolean isFTT) {
		Context ctx = HurricaneApplication.getInstance().getApplicationContext();
		SharedPreferences p = ctx.getSharedPreferences(AppConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
		Editor editor = p.edit();
		editor.putBoolean(AppConstants.PREF_KEY_IS_FTT, isFTT);
		editor.commit();
	}
}
