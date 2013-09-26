package com.beacon.afterui.constants;


public final class AppConstants {
	public static final String APP_PREFERENCE = "com.beacon.afterui.application";

	public static final String DEBUGMODE = "true";
	public static final String LOG_TAG = "AfterYou";

	public static final String PREF_KEY_IS_FTT = "is_ftt";

	public static final String BIRTH_DAY = "Birthdate";
	public static final String SELF_RELIGION = "Self_Rlg";
	public static final String SELF_RELIGION_INT = "Self_Rlg_Int";
	public static final String HAVE_CHILDREN = "hv_chld";
	public static final String SELF_RELATION = "Self_Rltn";
	public static final String SELF_RELATION_INT = "Self_Rltn_Int";
	public static final String WANT_CHILDREN = "wnt_chld";
	public static final String SELF_HEIGHT = "self_height";
	public static final String SELF_HEIGHT_INT ="self_height_int";
	public static final String SELF_BODY = "self_body";
	public static final String SELF_BODY_INT ="self_body_int";
	public static final String SELF_COMM = "self_comm";
	public static final String SELF_COMM_INT ="self_comm_int";
	public static final String SELF_DIET = "self_diet";
	public static final String SELF_DIET_INT ="self_diet_int";
	public static final String SELF_LANG ="self_lang";

	public static final String FACEBOOK_USER = "FACEBOOK_USER";

	public static final String SELF_FIRST_NAME = "SELF_FIRST_NAME";

	public static final String SELF_LAST_NAME = "SELF_LAST_NAME";

	public static final String SELF_GENDER = "self_gender";
	public static final String SELF_FB_ID = "SELF_FB_ID";

	public static boolean isDebugMode() {
		return DEBUGMODE.trim().equals("true");
	}
	
}
