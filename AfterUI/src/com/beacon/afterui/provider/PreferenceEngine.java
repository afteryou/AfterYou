package com.beacon.afterui.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.constants.AppConstants;

public class PreferenceEngine {

	private static PreferenceEngine instance;
	private Context appContext ;

	public synchronized static PreferenceEngine getInstance(Context ctx) {

		if (instance == null) {
			instance = new PreferenceEngine(ctx.getApplicationContext());
		}
		return instance;

	}

	private PreferenceEngine(Context context) {
		this.appContext = context;
	}

	public void saveBirthday(String date)
	{
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.BIRTH_DAY, date);
		editor.commit();
	}
	
	public String getBirthday()
	{
		return getDefaultSharePreference().getString(AppConstants.BIRTH_DAY, null);
	}
	
	public String getSelfReligion() {
		return getDefaultSharePreference().getString(AppConstants.SELF_RELIGION, null);
	}
	
	public int getSelfReligionInt() {
		return getDefaultSharePreference().getInt(AppConstants.SELF_RELIGION_INT, 0);
	}
	
	public void setSelfReligion(String rlg)
	{
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_RELIGION, rlg);
		editor.commit();
	}
	
	public void setSelfReligionInt(int rlg)
	{
		Editor editor = getDefaultSharePreference().edit();
		editor.putInt(AppConstants.SELF_RELIGION_INT, rlg);
		editor.commit();
	}
	
	public String getSelfRelation() {
		return getDefaultSharePreference().getString(AppConstants.SELF_RELATION, null);
	}
	
	public int getSelfRelationInt() {
		return getDefaultSharePreference().getInt(AppConstants.SELF_RELATION_INT, 0);
	}
	
	public void setSelfRelation(String rltn)
	{
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_RELATION, rltn);
		editor.commit();
	}
	
	public void setSelfRelationInt(int rltn)
	{
		Editor editor = getDefaultSharePreference().edit();
		editor.putInt(AppConstants.SELF_RELATION_INT, rltn);
		editor.commit();
	}
	
	public void saveHaveChildren(boolean hvChld)
	{
		Editor editor = getDefaultSharePreference().edit();
		editor.putBoolean(AppConstants.HAVE_CHILDREN, hvChld);
		editor.commit();
	}
	
	public boolean getHaveChildren()
	{
		return getDefaultSharePreference().getBoolean(AppConstants.HAVE_CHILDREN, false);
	}

//	public void saveLastLocation(double latitude, double longitude) {
//		Editor editor = getDefaultSharePreference().edit();
//		editor.putString(AppConstants.PREF_KEY_LAST_LOCATION, System.currentTimeMillis() + "," + latitude + ","
//				+ longitude);
//		editor.commit();
//	}

//	public android.location.Location getLastLocation() {
//		String locString = getDefaultSharePreference().getString(AppConstants.PREF_KEY_LAST_LOCATION, null);
//		if (locString == null) {
//			 return null;
//		}
//		String[] locInfo = locString.split(",");
//		android.location.Location location = new android.location.Location("Last Location");
//		location.setTime(Long.valueOf(locInfo[0]));
//		location.setLatitude(Double.valueOf(locInfo[1]));
//		location.setLongitude(Double.valueOf(locInfo[2]));
//		return location;
//	}


	private SharedPreferences getDefaultSharePreference() {
		return appContext.getSharedPreferences(AppConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
	}


}
