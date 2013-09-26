package com.beacon.afterui.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.constants.AppConstants;

public class PreferenceEngine {

	private static PreferenceEngine instance;
	private Context appContext;

	public synchronized static PreferenceEngine getInstance(Context ctx) {

		if (instance == null) {
			instance = new PreferenceEngine(ctx.getApplicationContext());
		}
		return instance;

	}

	private PreferenceEngine(Context context) {
		this.appContext = context;
	}

	public void saveBirthday(String date) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.BIRTH_DAY, date);
		editor.commit();
	}

	public String getBirthday() {
		return getDefaultSharePreference().getString(AppConstants.BIRTH_DAY,
				null);
	}

	public String getSelfReligion() {
		return getDefaultSharePreference().getString(
				AppConstants.SELF_RELIGION, null);
	}

	public int getSelfReligionInt() {
		return getDefaultSharePreference().getInt(
				AppConstants.SELF_RELIGION_INT, 0);
	}

	public void setSelfReligion(String rlg) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_RELIGION, rlg);
		editor.commit();
	}

	public void setSelfReligionInt(int rlg) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putInt(AppConstants.SELF_RELIGION_INT, rlg);
		editor.commit();
	}

	public String getSelfRelation() {
		return getDefaultSharePreference().getString(
				AppConstants.SELF_RELATION, null);
	}

	public int getSelfRelationInt() {
		return getDefaultSharePreference().getInt(
				AppConstants.SELF_RELATION_INT, 0);
	}

	public void setSelfRelation(String rltn) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_RELATION, rltn);
		editor.commit();
	}

	public void setSelfRelationInt(int rltn) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putInt(AppConstants.SELF_RELATION_INT, rltn);
		editor.commit();
	}

	public void saveHaveChildren(boolean hvChld) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putBoolean(AppConstants.HAVE_CHILDREN, hvChld);
		editor.commit();
	}

	public boolean getHaveChildren() {
		return getDefaultSharePreference().getBoolean(
				AppConstants.HAVE_CHILDREN, false);
	}

	// public void saveLastLocation(double latitude, double longitude) {
	// Editor editor = getDefaultSharePreference().edit();
	// editor.putString(AppConstants.PREF_KEY_LAST_LOCATION,
	// System.currentTimeMillis() + "," + latitude + ","
	// + longitude);
	// editor.commit();
	// }

	// public android.location.Location getLastLocation() {
	// String locString =
	// getDefaultSharePreference().getString(AppConstants.PREF_KEY_LAST_LOCATION,
	// null);
	// if (locString == null) {
	// return null;
	// }
	// String[] locInfo = locString.split(",");
	// android.location.Location location = new
	// android.location.Location("Last Location");
	// location.setTime(Long.valueOf(locInfo[0]));
	// location.setLatitude(Double.valueOf(locInfo[1]));
	// location.setLongitude(Double.valueOf(locInfo[2]));
	// return location;
	// }

	private SharedPreferences getDefaultSharePreference() {
		return appContext.getSharedPreferences(AppConstants.APP_PREFERENCE,
				Context.MODE_PRIVATE);
	}

	public void saveWantChildren(boolean wntChld) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putBoolean(AppConstants.WANT_CHILDREN, wntChld);
		editor.commit();
	}

	public boolean getWantChildren() {
		return getDefaultSharePreference().getBoolean(
				AppConstants.WANT_CHILDREN, false);
	}

	public String getSelfHeight() {
		return getDefaultSharePreference().getString(AppConstants.SELF_HEIGHT,
				null);
	}

	public int getSelfHeightInt() {
		return getDefaultSharePreference().getInt(AppConstants.SELF_HEIGHT_INT,
				0);
	}

	public void setSelfHeight(String hgt) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_HEIGHT, hgt);
		editor.commit();
	}

	public void setSelfHeightInt(int hgt) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putInt(AppConstants.SELF_HEIGHT_INT, hgt);
		editor.commit();
	}

	public String getSelfBodyType() {
		return getDefaultSharePreference().getString(AppConstants.SELF_BODY,
				null);
	}

	public int getSelfBodyTypeInt() {
		return getDefaultSharePreference()
				.getInt(AppConstants.SELF_BODY_INT, 0);
	}

	public void setSelfBodyType(String bdyType) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_BODY, bdyType);
		editor.commit();
	}

	public void setSelfBodyTypeInt(int bdyInt) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putInt(AppConstants.SELF_BODY_INT, bdyInt);
		editor.commit();
	}

	public String getSelfCommunity() {
		return getDefaultSharePreference().getString(AppConstants.SELF_COMM,
				null);
	}

	public int getSelfCommunityInt() {
		return getDefaultSharePreference()
				.getInt(AppConstants.SELF_COMM_INT, 0);
	}

	public void setSelfCommunity(String bdyType) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_COMM, bdyType);
		editor.commit();
	}

	public void setSelfCommunityInt(int bdyInt) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putInt(AppConstants.SELF_COMM_INT, bdyInt);
		editor.commit();
	}

	public String getSelfDiet() {
		return getDefaultSharePreference().getString(AppConstants.SELF_DIET,
				null);
	}

	public int getSelfDietInt() {
		return getDefaultSharePreference()
				.getInt(AppConstants.SELF_DIET_INT, 0);
	}

	public void setSelfDiet(String bdyType) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_DIET, bdyType);
		editor.commit();
	}

	public void setSelfDietInt(int bdyInt) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putInt(AppConstants.SELF_DIET_INT, bdyInt);
		editor.commit();
	}
	
	public void setSelfLangList(String selfLang) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_LANG, selfLang);
		editor.commit();
	}


	public String getSelfLangList() {
		return getDefaultSharePreference().getString(AppConstants.SELF_LANG,
				null);
	}

	public boolean[] getSelfLangBoolean(String[] referenceList) {
		boolean[] selectedLang = new boolean[referenceList.length];
		String[] saveList;
		if (getSelfLangList() != null) {
			saveList = getSelfLangList().split(";");

			for (int i = 0; i < saveList.length; i++) {
				for (int j = 0; j < referenceList.length; j++) {
					 if(referenceList[j].equalsIgnoreCase(saveList[i]))
					 {
						 selectedLang[j] = true;
						 break;
					 }
				}
			}
		}
		
		return selectedLang;
	}

	public void setFirstName(String firstName) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_FIRST_NAME, firstName);
		editor.commit();
		
	}
	
	public String getFirstName()
	{
		return getDefaultSharePreference().getString(AppConstants.SELF_FIRST_NAME,
				null);
	}
	
	public void setLastName(String lastName) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_LAST_NAME, lastName);
		editor.commit();
		
	}
	
	public String getLastName()
	{
		return getDefaultSharePreference().getString(AppConstants.SELF_LAST_NAME,
				null);
	}

	public void saveGender(Object gender) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_GENDER,(String)gender);
		editor.commit();
		
	}
	
	public String getGender()
	{
		return getDefaultSharePreference().getString(AppConstants.SELF_GENDER,
				null);
	}

	public void saveProfileUserName(String id) {
		Editor editor = getDefaultSharePreference().edit();
		editor.putString(AppConstants.SELF_FB_ID,id);
		editor.commit();
	}
	public String getProfileID()
	{
		return getDefaultSharePreference().getString(AppConstants.SELF_FB_ID,
				null);
	}
	
}
