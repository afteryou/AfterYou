package com.beacon.afterui.activity;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.beacon.afterui.R;
import com.beacon.afterui.log.AfterUIlog;
import com.beacon.afterui.utils.AppUtils;
import com.beacon.afterui.utils.DebugUtils;


public class BaseActivity extends Activity {

	private static final String TAG = "BaseActivity";

	public static final String VIEW_TYPE = "_view_type";

	public static final String VIEW_ROOT = "_view_root";

	protected int mViewType;

	protected boolean mIsRootView;

	protected ScreenManager mScreenManager;

	private ControllerManager mControllerManager;
	
	public void setIsRootView(boolean isRootView) {
		this.mIsRootView = isRootView;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mViewType = bundle.getInt(VIEW_TYPE);
			mIsRootView = bundle.getBoolean(VIEW_ROOT, false);
		}

		mControllerManager = ControllerManager.getInstance();
		mScreenManager = mControllerManager.getSCUIController(ScreenManager.class);
		pruneStack(mIsRootView);
		mScreenManager.setCurrentContext(this);
		AfterUIlog.i(TAG, "BaseActivity.onCreate done: mViewType=" + mViewType);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mScreenManager.setCurrentContext(this);
	}

	@Override
	public void finish() {
		if (mScreenManager != null) {
			mScreenManager.getActivityStack().remove(this);
		}

		super.finish();
	}

	@Override
	public void onBackPressed() {
		if (mIsRootView) {
			exitApp();
			return;
		}
		finish();
	}

	public void exitApp(){
		// Calling destroy causes crash. On Thunderbolt application hungs
		// when crashing.
		// As a workaround we just kill the process.
		// ControllerManager cm = ControllerManager.getInstance();
		// cm.destroy();
		// android.os.Process.killProcess(android.os.Process.myPid());
		ActivityManager activityMgr = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		activityMgr.killBackgroundProcesses(getPackageName());
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}
	
	public int getViewType() {
		return mViewType;
	}

	private void pruneStack(boolean root) {
		ActivityStack<BaseActivity> actStack = mScreenManager.getActivityStack();
		if (root) {
			actStack.removeAllElements();
		}

		actStack.push(this);
	}

	private void hackToShowActionBarOverflow() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}
	}
	

	
	@Override
	protected Dialog onCreateDialog(int id) {
		return null;
	}


	@SuppressWarnings("unchecked")
	public <T> T findViewByIdAutoCast(int id) {
		return (T) this.findViewById(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		DebugUtils.addDebugMenuItems(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		DebugUtils.onDebugOptionsItemSelected(this, item);
		return super.onOptionsItemSelected(item);
	}

	public ControllerManager getControllerManager() {
		return mControllerManager;
	}
}
