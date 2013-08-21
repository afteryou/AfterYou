package com.navbuilder.app.hurricane;

import java.lang.reflect.Field;

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

import com.navbuilder.app.hurricane.controller.GPSController;
import com.navbuilder.app.hurricane.log.Nimlog;
import com.navbuilder.app.hurricane.preference.PreferenceEngine;
import com.navbuilder.app.hurricane.slidingmenu.SlidingActivity;
import com.navbuilder.app.hurricane.utils.AppUtils;
import com.navbuilder.app.hurricane.utils.DebugUtils;

public class BaseActivity extends SlidingActivity {

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
		Nimlog.i(TAG, "BaseActivity.onCreate done: mViewType=" + mViewType);
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
		com.nbi.location.Location location=GPSController.getInstance(this).getLastestLocation();
		if(location!=null){
			Nimlog.d(this, "[BaseActivity][exitApp]save last location, lat:"+location.getLatitude()+",lon:"+location.getLongitude());
			PreferenceEngine.getInstance().saveLastLocation(location.getLatitude(), location.getLongitude());
		}
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
	
	protected void checkGPSSetting(){
		if(!GPSController.getInstance(this).isGPSEnable()){
			super.showDialog(DIALOG_GPS_SETTING);
		}else{
			onGPSCheckFinish();
			AppUtils.setFirstTimeStart(false);
		}
	}
	
	protected void onGPSCheckFinish(){
		
	}

	private static final int DIALOG_GPS_SETTING=1001;
	private static final int DIALOG_NO_AVAILABLE_LAST_KNOWN_LOCATION=1002;
	
	@Override
	protected Dialog onCreateDialog(int id) {
		
		switch(id){
			case DIALOG_GPS_SETTING:
				
				return new AlertDialog.Builder(this)
                .setMessage(R.string.IDS_TO_OBTAIN_YOUR_CURRENT_POSITION)
                .setPositiveButton(R.string.IDS_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    	startActivityForResult(new Intent(
								"android.settings.LOCATION_SOURCE_SETTINGS"),
								-1);
                    }
                })
                .setOnCancelListener(new OnCancelListener(){
					@Override
					public void onCancel(DialogInterface dialog) {
						if(!AppUtils.isFirstTimeStart()&&PreferenceEngine.getInstance().getLastLocation()!=null){
                    		//TODO: use last known location, this logic will be handle in search part
                    		removeDialog(DIALOG_GPS_SETTING);
                    		onGPSCheckFinish();
                    	}else{
                    		//last known location is not available
                    		removeDialog(DIALOG_GPS_SETTING);
                    		showDialog(DIALOG_NO_AVAILABLE_LAST_KNOWN_LOCATION);
                    	}
                    	
                    	AppUtils.setFirstTimeStart(false);
						
					}})
                .setNegativeButton(R.string.IDS_CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    	if(!AppUtils.isFirstTimeStart()&&PreferenceEngine.getInstance().getLastLocation()!=null){
                    		//TODO: use last known location, this logic will be handle in search part
                    		removeDialog(DIALOG_GPS_SETTING);
                    		onGPSCheckFinish();
                    	}else{
                    		//last known location is not available
                    		removeDialog(DIALOG_GPS_SETTING);
                    		showDialog(DIALOG_NO_AVAILABLE_LAST_KNOWN_LOCATION);
                    	}
                    	
                    	AppUtils.setFirstTimeStart(false);
                    	
                    }
                })
                .create();
			
			case DIALOG_NO_AVAILABLE_LAST_KNOWN_LOCATION:
				
				return new AlertDialog.Builder(this)
                .setMessage(R.string.IDS_THERE_IS_NO_AVAILABLE_KNOWN_LOCATION)
                .setPositiveButton(R.string.IDS_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	removeDialog(DIALOG_NO_AVAILABLE_LAST_KNOWN_LOCATION);
                    	exitApp();
                    }
                })
                .setOnCancelListener(new OnCancelListener(){
					@Override
					public void onCancel(DialogInterface dialog) {
						removeDialog(DIALOG_NO_AVAILABLE_LAST_KNOWN_LOCATION);
                    	exitApp();
					}})
                .create();
				
		}
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
