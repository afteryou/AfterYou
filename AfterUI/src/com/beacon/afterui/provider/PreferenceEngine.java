package com.beacon.afterui.provider;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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

    public String getMatchReligion() {
        return getDefaultSharePreference().getString(
                AppConstants.MATCH_RELIGION, null);
    }

    public int getSelfReligionInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.SELF_RELIGION_INT, 0);
    }

    public int getMatchReligionInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.MATCH_RELIGION_INT, 0);
    }

    public void setSelfReligion(String rlg) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_RELIGION, rlg);
        editor.commit();
    }

    public void setMatchReligion(String rlg) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.MATCH_RELIGION, rlg);
        editor.commit();
    }

    public void setSelfReligionInt(int rlg) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.SELF_RELIGION_INT, rlg);
        editor.commit();
    }

    public void setMatchReligionInt(int rlg) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.MATCH_RELIGION_INT, rlg);
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

    public String getMatchRelation() {
        return getDefaultSharePreference().getString(
                AppConstants.MATCH_RELATION, null);
    }

    public int getMatchRelationInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.MATCH_RELATION_INT, 0);
    }

    public void setMatchRelation(String rltn) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.MATCH_RELATION, rltn);
        editor.commit();
    }

    public void setMatchRelationInt(int rltn) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.MATCH_RELATION_INT, rltn);
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

    public void saveMatchHaveChildren(boolean hvChld) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putBoolean(AppConstants.MATCH_HAVE_CHILDREN, hvChld);
        editor.commit();
    }

    public boolean getMatchHaveChildren() {
        return getDefaultSharePreference().getBoolean(
                AppConstants.MATCH_HAVE_CHILDREN, false);
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

    public void saveMatchWantChildren(boolean wntChld) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putBoolean(AppConstants.MATCH_WANT_CHILDREN, wntChld);
        editor.commit();
    }

    public boolean getMatchWantChildren() {
        return getDefaultSharePreference().getBoolean(
                AppConstants.MATCH_WANT_CHILDREN, false);
    }

    // get self height foot int
    public int getSelfHeightFootInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.SELF_HEIGHT_FOOT_INT, 0);
    }

    // get match height foot int
    public int getMatchHeightFootInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.MATCH_HEIGHT_FOOT_INT, 0);
    }

    // get self height inches int
    public int getSelfHeightInchesInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.SELF_HEIGHT_INCHES_INT, 0);
    }

    // get match height inches int
    public int getMatchHeightInchesInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.MATCH_HEIGHT_INCHES_INT, 0);
    }

    // set self height foot string
    public void setSelfHeightFoot(String hgt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_HEIGHT_FOOT, hgt);
        editor.commit();
    }

    // set match height foot string
    public void setMatchHeightFoot(String hgt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.MATCH_HEIGHT_FOOT, hgt);
        editor.commit();
    }

    // set match height inches string
    public void setSelfHeightInches(String hgt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_HEIGHT_INCHES, hgt);
        editor.commit();
    }

    // set match height inches string
    public void setMatchHeightInches(String hgt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.MATCH_HEIGHT_INCHES, hgt);
        editor.commit();
    }

    // set self height foot int
    public void setSelfHeightFootInt(int hgt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.SELF_HEIGHT_FOOT_INT, hgt);
        editor.commit();
    }

    // set match height foot int
    public void setMatchHeightFootInt(int hgt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.MATCH_HEIGHT_FOOT_INT, hgt);
        editor.commit();
    }

    // set self height inches int
    public void setSelfHeightInchesInt(int hgt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.SELF_HEIGHT_INCHES_INT, hgt);
        editor.commit();
    }

    // set match height inches int
    public void setMatchHeightInchesInt(int hgt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.MATCH_HEIGHT_INCHES_INT, hgt);
        editor.commit();
    }

    public String getSelfHeightFoot() {
        return getDefaultSharePreference().getString(
                AppConstants.SELF_HEIGHT_FOOT, null);
    }

    public String getMatchHeightFoot() {
        return getDefaultSharePreference().getString(
                AppConstants.MATCH_HEIGHT_FOOT, null);
    }

    public String getSelfHeightInches() {
        return getDefaultSharePreference().getString(
                AppConstants.SELF_HEIGHT_INCHES, null);
    }

    public String getMatchHeightInches() {
        return getDefaultSharePreference().getString(
                AppConstants.MATCH_HEIGHT_INCHES, null);
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

    public String getMatchBodyType() {
        return getDefaultSharePreference().getString(AppConstants.MATCH_BODY,
                null);
    }

    public int getMatchBodyTypeInt() {
        return getDefaultSharePreference().getInt(AppConstants.MATCH_BODY_INT,
                0);
    }

    public void setMatchBodyType(String bdyType) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.MATCH_BODY, bdyType);
        editor.commit();
    }

    public void setMatchBodyTypeInt(int bdyInt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.MATCH_BODY_INT, bdyInt);
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

    public String getMatchCommunity() {
        return getDefaultSharePreference().getString(AppConstants.MATCH_COMM,
                null);
    }

    public int getMatchCommunityInt() {
        return getDefaultSharePreference().getInt(AppConstants.MATCH_COMM_INT,
                0);
    }

    public void setMatchCommunity(String bdyType) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.MATCH_COMM, bdyType);
        editor.commit();
    }

    public void setMatchCommunityInt(int bdyInt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.MATCH_COMM_INT, bdyInt);
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

    public String getMatchDiet() {
        return getDefaultSharePreference().getString(AppConstants.MATCH_DIET,
                null);
    }

    public int getMatchDietInt() {
        return getDefaultSharePreference().getInt(AppConstants.MATCH_DIET_INT,
                0);
    }

    public void setMatchDiet(String bdyType) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.MATCH_DIET, bdyType);
        editor.commit();
    }

    public void setMatchDietInt(int bdyInt) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.MATCH_DIET_INT, bdyInt);
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
                    if (referenceList[j].equalsIgnoreCase(saveList[i])) {
                        selectedLang[j] = true;
                        break;
                    }
                }
            }
        }

        return selectedLang;
    }

    public void setMatchLangList(String matchLang) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.MATCH_LANG, matchLang);
        editor.commit();
    }

    public String getMatchLangList() {
        return getDefaultSharePreference().getString(AppConstants.MATCH_LANG,
                null);
    }

    public boolean[] getMatchLangBoolean(String[] referenceList) {
        boolean[] selectedLang = new boolean[referenceList.length];
        String[] saveList;
        if (getMatchLangList() != null) {
            saveList = getMatchLangList().split(";");

            for (int i = 0; i < saveList.length; i++) {
                for (int j = 0; j < referenceList.length; j++) {
                    if (referenceList[j].equalsIgnoreCase(saveList[i])) {
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

    public String getFirstName() {
        return getDefaultSharePreference().getString(
                AppConstants.SELF_FIRST_NAME, null);
    }

    public void setLastName(String lastName) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_LAST_NAME, lastName);
        editor.commit();
    }

    public void setUsername(final String userName) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.USER_NAME, userName);
        editor.commit();
    }

    public void setPassword(final String password) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.PASSWORD, password);
        editor.commit();
    }

    public String getUserName() {
        return getDefaultSharePreference().getString(AppConstants.USER_NAME,
                null);
    }

    public String getPassword() {
        return getDefaultSharePreference().getString(AppConstants.PASSWORD,
                null);
    }

    public void setLastLoggedIn(final String lastLoggedIn) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.LAST_LOGGED_IN, lastLoggedIn);
        editor.commit();
    }

    public String getLastLoggedIn() {
        return getDefaultSharePreference().getString(
                AppConstants.LAST_LOGGED_IN, null);
    }

    public String getLastName() {
        return getDefaultSharePreference().getString(
                AppConstants.SELF_LAST_NAME, null);
    }

    public void saveGender(Object gender) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_GENDER, (String) gender);
        editor.commit();

    }

    public String getGender() {
        return getDefaultSharePreference().getString(AppConstants.SELF_GENDER,
                null);
    }

    public void saveProfileUserName(String id) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_FB_ID, id);
        editor.commit();
    }

    public String getProfileID() {
        return getDefaultSharePreference().getString(AppConstants.SELF_FB_ID,
                null);
    }

    public void setSelfDrinkingInt(int mCurrentSlfDriking) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.SELF_DRINKING_INT, mCurrentSlfDriking);
        editor.commit();

    }

    public void setSelfDrinking(String selectedSlfDrinking) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_DRINKING, selectedSlfDrinking);
        editor.commit();

    }

    public void setMatchDrinkingInt(int mCurrentSlfDriking) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.MATCH_DRINKING_INT, mCurrentSlfDriking);
        editor.commit();

    }

    public void setMatchDrinking(String selectedSlfDrinking) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.MATCH_DRINKING, selectedSlfDrinking);
        editor.commit();

    }

    public void setSelfEducationInt(int mCurrentSlfEducation) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.SELF_EDUCATION_INT, mCurrentSlfEducation);
        editor.commit();

    }

    public void setSelfEducation(String selectedSlfEducation) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_EDUCATION, selectedSlfEducation);
        editor.commit();

    }

    public void setSelfSalaryInt(int mCurrentSlfSalary) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.SELF_SALARY_INT, mCurrentSlfSalary);
        editor.commit();

    }

    public void setSelfSalary(String selectedSlfSalary) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_SALARY, selectedSlfSalary);
        editor.commit();

    }

    public void setWantAgeInt(int mCurrentWntAge) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.WANT_AGE_INT, mCurrentWntAge);
        editor.commit();

    }

    public void setWantAge(String selectedWntAge) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.WANT_AGE, selectedWntAge);
        editor.commit();

    }

    public int getWantAgeInt() {
        return getDefaultSharePreference().getInt(AppConstants.WANT_AGE_INT, 0);
    }

    public String getWantAge() {
        return getDefaultSharePreference().getString(AppConstants.WANT_AGE,
                null);
    }

    public int getSelfSmokingInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.SELF_SMOKING_INT, 0);
    }

    public String getSelfSmoking() {
        return getDefaultSharePreference().getString(AppConstants.SELF_SMOKING,
                null);
    }

    public void setSelfSmokingInt(int mCurrentSlfSmoking) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.SELF_SMOKING_INT, mCurrentSlfSmoking);
        editor.commit();

    }

    public void setSelfSmoking(String selectedSlfSmoking) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_SMOKING, selectedSlfSmoking);
        editor.commit();

    }

    public int getMatchSmokingInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.MATCH_SMOKING_INT, 0);
    }

    public String getMatchSmoking() {
        return getDefaultSharePreference().getString(
                AppConstants.MATCH_SMOKING, null);
    }

    public void setMatchSmokingInt(int mCurrentSlfSmoking) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putInt(AppConstants.MATCH_SMOKING_INT, mCurrentSlfSmoking);
        editor.commit();

    }

    public void setMatchSmoking(String selectedSlfSmoking) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.MATCH_SMOKING, selectedSlfSmoking);
        editor.commit();

    }

    public String getSelfSalary() {
        return getDefaultSharePreference().getString(AppConstants.SELF_SALARY,
                null);
    }

    public int getSelfEducationInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.SELF_EDUCATION_INT, 0);
    }

    public String getSelfEducation() {
        return getDefaultSharePreference().getString(
                AppConstants.SELF_EDUCATION, null);
    }

    public int getSelfDrinkInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.SELF_DRINKING_INT, 0);
    }

    public String getSelfDrinking() {
        return getDefaultSharePreference().getString(
                AppConstants.SELF_DRINKING, null);
    }

    public int getMatchDrinkInt() {
        return getDefaultSharePreference().getInt(
                AppConstants.MATCH_DRINKING_INT, 0);
    }

    public String getMatchDrinking() {
        return getDefaultSharePreference().getString(
                AppConstants.MATCH_DRINKING, null);
    }

    public int getSelfSalaryInt() {
        return getDefaultSharePreference().getInt(AppConstants.SELF_SALARY_INT,
                0);
    }

    public void saveUserEmail(Object email) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SELF_EMAIL, (String) email);
        editor.commit();

    }

    public String getUserEmail() {
        return getDefaultSharePreference().getString(AppConstants.SELF_EMAIL,
                null);
    }

    public void setChatUserName(final String userName) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.CHAT_USER_NAME, userName);
        editor.commit();
    }

    public String getChatUserName() {
        return getDefaultSharePreference().getString(
                AppConstants.CHAT_USER_NAME, null);
    }

    public void setFTT(boolean isFTT) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putBoolean(AppConstants.IS_FTT, isFTT);
        editor.commit();

    }

    public boolean isFTT() {
        return getDefaultSharePreference()
                .getBoolean(AppConstants.IS_FTT, true);

    }

    public void setFromNotification(boolean notify) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putBoolean(AppConstants.NOTIFICATION_ID, notify);
        editor.commit();

    }

    public boolean isFromNotification() {
        return getDefaultSharePreference().getBoolean(
                AppConstants.NOTIFICATION_ID, false);
    }

    public void setUnReadMessageList(Set<String> messSet) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putStringSet(AppConstants.UNREAD_MSGS, messSet);
        editor.commit();

    }

    public String[] getUnReadMessageList() {
        String[] array = new String[getDefaultSharePreference().getStringSet(
                AppConstants.UNREAD_MSGS, null).size()];
        getDefaultSharePreference()
                .getStringSet(AppConstants.UNREAD_MSGS, null).toArray(array);
        return array;

    }

    public void setNotifySenderName(String sender) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putString(AppConstants.SENDER, sender);
        editor.commit();

    }

    public String getSenderName() {
        return getDefaultSharePreference().getString(AppConstants.SENDER, null);
    }

    public void setUserSignedUpStatus(final boolean status) {
        Editor editor = getDefaultSharePreference().edit();
        editor.putBoolean(AppConstants.SIGNED_UP, status);
        editor.commit();

    }

    public boolean isUserSignedUp() {
        return getDefaultSharePreference().getBoolean(AppConstants.SIGNED_UP,
                false);
    }
    
    public boolean isUserFromFacebook()
    {
        return getDefaultSharePreference().getBoolean(AppConstants.FACEBOOK_USER,
                false);
    }
    
    public void userFromFacebook(boolean userFacebook)
    {
    	Editor editor = getDefaultSharePreference().edit();
        editor.putBoolean(AppConstants.FACEBOOK_USER, userFacebook);
        editor.commit();
    }
}
