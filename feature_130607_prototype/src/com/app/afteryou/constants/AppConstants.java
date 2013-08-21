package com.app.afteryou.constants;

public class AppConstants {
	public static final String APP_PREFERENCE = "com.hurricane.preference.application";
	public static final String PREF_KEY_GPS_FILE = "gps_file_name";
	public static final String PREF_KEY_SERVER_TOKEN = "server_token";
	public static final String PREF_KEY_IS_FTT = "is_ftt";
	public static final String PREF_KEY_LAST_LOCATION = "user_last_location";

	public static final String TOKEN = "DEV4";
	public static final int LAST_KNOWN_GPS_TIMEOUT = 1000 * 60;
	
	public static final String DEBUGMODE = "true";
	public static final String LOG_TAG = "Hurricane";

	public static boolean isDebugMode() {
		return DEBUGMODE.trim().equals("true");
	}
}
