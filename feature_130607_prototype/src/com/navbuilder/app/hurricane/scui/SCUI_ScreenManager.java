/*
 * (C) Copyright 2013 by TeleCommunication Systems, Inc.
 *
 * The information contained herein is confidential, proprietary
 * to TeleCommunication Systems, Inc., and considered a trade secret
 * as defined in section 499C of the penal code of the State of
 * California. Use of this information by anyone other than
 * authorized employees of TeleCommunication Systems is granted only
 * under a written non-disclosure agreement, expressly prescribing
 * the scope and manner of such use.
 *
 */

package com.navbuilder.app.hurricane.scui;

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
