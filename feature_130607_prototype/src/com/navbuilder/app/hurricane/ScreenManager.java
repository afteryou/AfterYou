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

package com.navbuilder.app.hurricane;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.navbuilder.app.hurricane.scui.SCUI_Controller;
import com.navbuilder.app.hurricane.scui.SCUI_ScreenManagerListener;

public class ScreenManager implements SCUI_ScreenManagerListener, SCUI_Controller {
	private static final String TAG = "ScreenManager";
	private static final ScreenManager mInstance = new ScreenManager();

	private UIView mTempUIView;

	private Context mContext;

	private ActivityStack<BaseActivity> mActivityStack;

	private ScreenManager() {
		mActivityStack = new ActivityStack<BaseActivity>();
	}

	public static ScreenManager getInstance() {
		return mInstance;
	}

	@Override
	public void pushView(int viewType, boolean animated, int transition) {
		Log.i(TAG, "pushView: viewType=" + viewType + "; mTempUIView=" + mTempUIView);
		if (mTempUIView != null && mTempUIView.getViewType() == viewType) {
			mTempUIView.show(mContext);
			mTempUIView = null;
		}
	}

	@Override
	public void pushViewAsRoot() {
		Log.i(TAG, "pushView: mTempUIView=" + mTempUIView);
		if (mTempUIView != null) {
			mTempUIView.showAsRoot(mContext);
			mTempUIView = null;
		}
	}

	@Override
	public void popToMainScreen() {
		Log.i(TAG, "popToMainScreen");
	}

	@Override
	public void popView(final int viewType, boolean animated, int transition) {
		Log.i(TAG, "popView: viewType=" + viewType);
		MainLoopPosting.getInstance().post(new Runnable() {
			@Override
			public void run() {
				Activity activity = mActivityStack.getActivityByType(viewType);
				if (activity != null) {
					activity.finish();
				}
			}
		});
	}

	public void setTempUIView(UIView uiView) {
		mTempUIView = uiView;
	}

	public void setCurrentContext(Context context) {
		mContext = context;
	}

	public ActivityStack<BaseActivity> getActivityStack() {
		return mActivityStack;
	}

	@Override
	public void getDeviceDate(int year, int month, int day, int hour, int minute, int second, int timeZone,
			boolean useDaySaving) {
		Calendar c = Calendar.getInstance();
		nativeSetIntField(year, c.get(Calendar.YEAR));
		nativeSetIntField(month, c.get(Calendar.MONTH) + 1);
		nativeSetIntField(day, c.get(Calendar.DAY_OF_MONTH));
		nativeSetIntField(hour, c.get(Calendar.HOUR_OF_DAY));
		nativeSetIntField(minute, c.get(Calendar.MINUTE));
		nativeSetIntField(second, c.get(Calendar.SECOND));
		nativeSetIntField(timeZone, (c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)) / (3600000));

		// nativeSetBoolField(useDaySaving, false); //???
	}

	private native void nativeSetIntField(int ptr, int value);

	private native void nativeSetBoolField(int ptr, boolean value);
}