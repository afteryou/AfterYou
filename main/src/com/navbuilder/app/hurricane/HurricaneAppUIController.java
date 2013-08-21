package com.navbuilder.app.hurricane;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.navbuider.app.hurricane.uiflow.HurricaneAppController;
import com.navbuider.app.hurricane.uiflow.HurricaneAppUIListener;
import com.navbuilder.app.hurricane.common.Credentials;
import com.navbuilder.app.hurricane.common.LBSManager;
import com.navbuilder.app.hurricane.constants.AppConstants;
import com.navbuilder.app.hurricane.debug.ContextManager;
import com.navbuilder.app.hurricane.home.HomeActivity;
import com.navbuilder.app.hurricane.home.HomeUIController;
import com.navbuilder.app.hurricane.preference.PreferenceEngine;
import com.navbuilder.app.hurricane.qalog.LBSQALogUploadListener;
import com.navbuilder.app.hurricane.qalog.LBSQALoggerManager;
import com.navbuilder.app.hurricane.utils.Nimlog;
import com.nbi.location.Location;

public class HurricaneAppUIController implements HurricaneAppUIListener {

	private static HurricaneAppUIController instance;

	private HurricaneAppController hurricaneAppController;

	private HurricaneAppUIController() {
		hurricaneAppController = HurricaneAppController.getInstance();
	}

	public synchronized static HurricaneAppUIController getInstance() {
		if (instance == null) {
			instance = new HurricaneAppUIController();
		}
		return instance;
	}

	@Override
	public void handlePurning() {
	}

	@Override
	public void showHomeScreen() {
		while(!(ActivityStack.getInstance().getTop() instanceof HomeActivity)){
			ActivityStack.getInstance().pop().finish();
		}
	}
	
	@Override
	public void initKits() {
		Context appContext = HurricaneApplication.getInstance().getApplicationContext();
		LBSManager.init(appContext, Credentials.instance().getValue(), ContextManager.getSavedCarrier(appContext), null);
	}

	@Override
	public Location getCachedLocation() {

		android.location.Location loc = PreferenceEngine.getInstance().getLastLocation();

		Location location = null;

		if (loc != null) {
			location = new Location();
			location.setLatitude(loc.getLatitude());
			location.setLongitude(loc.getLongitude());
		}

		return location;
	}

	@Override
	public void saveCachedLocation(double lat,double lon) {
		PreferenceEngine.getInstance().saveLastLocation(lat,lon);
	}
	
	@Override
	public void startRegister() {
		hurricaneAppController.registerHome(HomeUIController.getInstance());
	}

	@Override
	public void exitApp() {
		if (AppConstants.isDebugMode()) {
			showQALogUploadDialog(ActivityStack.getInstance().getTop());
		} else {
			killProcess();
		}
	}

	private void showQALogUploadDialog(final Activity activity){
		if(activity == null){
			return;
		}
		new AlertDialog.Builder(activity)
		.setMessage("QA log will be cleared. Do you want to upload QA log?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	
            	LBSQALogUploadListener qalogUploadListenner = new LBSQALogUploadListener(activity);
            	qalogUploadListenner.setCompleteDialogButtonListenner(new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						killProcess();
					}
				});
            	LBSQALoggerManager.persistLogging(qalogUploadListenner);
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	killProcess();
            }
        })
        .create()
        .show();	
	}
	
	private void killProcess() {
		ActivityStack.getInstance().clearAndFinishAllActivity();
		com.nbi.location.Location location = HurricaneAppController.getInstance().getLastestLocation();
		if (location != null) {
			Nimlog.d(
					this,
					"[BaseActivity][exitApp]save last location, lat:" + location.getLatitude() + ",lon:"
							+ location.getLongitude());
			HurricaneAppController.getInstance().saveCachedLocation(location.getLatitude(), location.getLongitude());
		}
		// Calling destroy causes crash. On Thunderbolt application hungs
		// when crashing.
		// As a workaround we just kill the process.
		// ControllerManager cm = ControllerManager.getInstance();
		// cm.destroy();
		// android.os.Process.killProcess(android.os.Process.myPid());
		ActivityManager activityMgr = (ActivityManager) HurricaneApplication.getInstance().getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		activityMgr.killBackgroundProcesses(HurricaneApplication.getInstance().getApplicationContext().getPackageName());
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}

}
