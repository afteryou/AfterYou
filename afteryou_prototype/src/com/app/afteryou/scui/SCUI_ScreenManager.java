
package com.app.afteryou.scui;

import android.util.Log;

public class SCUI_ScreenManager{

	private static SCUI_ScreenManagerListener mListener;

	private SCUI_ScreenManager() {
	}

	public static void setListener(SCUI_ScreenManagerListener listener) {
		mListener = listener;
	}

	public static void pushView(int type, boolean animated, int transition) {
		if (mListener != null) {
			mListener.pushView(type, animated, transition);
		}
	}

	public static void pushViewAsRoot() {
		Log.i("Htmantest", "[pushViewAsRoot]");
		if (mListener != null) {
			mListener.pushViewAsRoot();
		}
	}

	public static void popToMainScreen() {
		if (mListener != null) {
			mListener.popToMainScreen();
		}
	}

	public static void popView(int type, boolean animated, int transition) {
		if (mListener != null) {
			mListener.popView(type, animated, transition);
		}
	}

	public static void getDeviceDate(int year, int month, int day, int hour, int minute, int second, int timeZone,
			boolean useDaySaving) {
		if (mListener != null) {
			mListener.getDeviceDate(year, month, day, hour, minute, second, timeZone, useDaySaving);
		}
	}
}
