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

	public static boolean isDebugMode() {
		return DEBUGMODE.trim().equals("true");
	}
	
}
