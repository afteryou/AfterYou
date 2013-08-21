package com.navbuilder.app.hurricane.constants;

public class AppConstants {
	public static final String APP_PREFERENCE = "com.hurricane.preference.application";
	public static final String PREF_KEY_GPS_FILE = "gps_file_name";
	public static final String PREF_KEY_SERVER_TOKEN = "server_token";
	public static final String PREF_KEY_IS_FTT = "is_ftt";
	public static final String PREF_KEY_LAST_LOCATION = "user_last_location";

	public static final String TOKEN = "QA4";
	
	public static final String DEBUGMODE = "true";

	public static boolean isDebugMode() {
		return DEBUGMODE.trim().equals("true");
	}
	
	public static class Map { 
		public final static int ANIMATION_TYPE_STOP = 0;
		public final static int ANIMATION_TYPE_UP = 1;
		public final static int ANIMATION_TYPE_DOWN = 2;
		public final static float ANIMATION_DISTANCE = 0.003f;
		public final static float ANIMATION_STEP_LENGTH = 0.00006f;
		public final static float DETAIL_MAP_DEFAULT_TILT = 500f;
		public final static int DETAIL_MAP_DEFAULT_DELAY = 2000;
	}
}
