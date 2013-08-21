package com.navbuilder.app.hurricane.utils;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.impl.cookie.DateUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Toast;

import com.navbuilder.app.hurricane.CrashHandler;
import com.navbuilder.app.hurricane.HurricaneApplication;
import com.navbuilder.app.hurricane.constants.AppConstants;
import com.navbuilder.app.hurricane.debug.ContextManager;
import com.navbuilder.app.hurricane.debug.GPSChooseActivity;
import com.navbuilder.app.hurricane.qalog.LBSQALogUploadListener;
import com.navbuilder.app.hurricane.qalog.LBSQALoggerManager;
import com.navbuilder.location.LocationWrapper;
import com.nbi.search.ui.Search;
import com.nbi.search.ui.controller.SearchKitManager;
import com.nbi.search.ui.utils.FileUtils;

public final class DebugUtils {
	private static final String CRASH_LOG_RECEIVERS = "qwang@telecomsys.com, wangqi.bj@gmail.com, zhu@telecomsys.com, grli@telecomsys.com";
	private static final String ANMATION_PERFORMANCE_LOG_FILE = FileUtils.getSDCardDirectory()
			+ "/Hurricane/animation_performance_log.txt";

	private static final int MENU_SEND_CRASH_LOG = 0x00000001;
	private static final int MENU_CHANGE_GPS_FILE = 0x00000002;
	private static final int MENU_CLEAR_CRASH_LOG = 0x00000004;
	private static final int MENU_VIEW_CRASH_LOG = 0x00000008;
	private static final int MENU_CHANGE_SERVER = 0x00000016;
	private static final int MENU_RECORD_ANIMATION_FPS = 0x00000032;
	private static final int MENU_GET_SINGLE_FIX = 0x00000007;
	private static final int MENU_UPLOAD_QA_LOG = 0x00000064;

	private static final Context appCtx = HurricaneApplication.getInstance().getApplicationContext();
	private static boolean isAnimationFPSRecordEnabled = getAnimationFPSRecordingStateFromPref();
	private static final String PREF_KEY_ANIMATION_FPS_RECORD_STATE = "anmation-fps-recording-state";
	private static final String PREF_FILE_FOR_DEBUG = "com.hurricane.debug.preference";
	

	private static final String MENU_TITLE_LOG_FPS = "Log Animation FPS";
	private static final String MENU_TITLE_STOP_LOG_FPS = "Stop Logging Animation FPS";

	public static void sendCrashLogByEmail(Activity activity) {
		if (!checkCrashLog(activity)) {
			return;
		}
		StringBuffer buffer = new StringBuffer();
		String[] files = new File(CrashHandler.CRASH_LOG_DIR).list();
		if (files == null) {
			return;
		}
		for (String file : files) {
			if (file.endsWith(CrashHandler.CRASH_LOG_EXTENSION)) {
				buffer.append(FileUtils.readFile(CrashHandler.CRASH_LOG_DIR + file, FileUtils.UTF8)).append(FileUtils.ENTER);
			}
		}
		EMailUtils.send(activity, CRASH_LOG_RECEIVERS, CRASH_LOG_RECEIVERS,
				"Crash Log " + DateUtils.formatDate(new Date(), CrashHandler.CRASH_LOG_DATE_PATTERN), buffer.toString(), files);
	}

	public static void addDebugMenuItems(Menu menu) {
		if (AppConstants.isDebugMode()) {
			menu = menu.addSubMenu(Menu.NONE, 0, 1000, "Debug Tools >");
			menu.addSubMenu(Menu.NONE, MENU_SEND_CRASH_LOG, 1, "Send Crash Log");
			menu.addSubMenu(Menu.NONE, MENU_CLEAR_CRASH_LOG, 2, "Clear Crash Log");
			menu.addSubMenu(Menu.NONE, MENU_VIEW_CRASH_LOG, 3, "View last Crash Log");
			menu.addSubMenu(Menu.NONE, MENU_RECORD_ANIMATION_FPS, 4, isAnimationFPSRecordEnabled ? MENU_TITLE_STOP_LOG_FPS
					: MENU_TITLE_LOG_FPS);
			menu.addSubMenu(Menu.NONE, MENU_CHANGE_GPS_FILE, 5, "Change GPS File");
			menu.addSubMenu(Menu.NONE, MENU_GET_SINGLE_FIX, 6, "Get One fix");
			menu.addSubMenu(Menu.NONE, MENU_CHANGE_SERVER, 7, "Switch Server & Carrier");
			menu.addSubMenu(Menu.NONE, MENU_UPLOAD_QA_LOG, 8, "Upload QA Log");
		}
	}

	public static void onDebugOptionsItemSelected(Activity activity, MenuItem item) {
		if (!AppConstants.isDebugMode()) {
			return;
		}
		switch (item.getItemId()) {
		case MENU_SEND_CRASH_LOG:
			DebugUtils.sendCrashLogByEmail(activity);
			break;
		case MENU_CLEAR_CRASH_LOG:
			clearCrashLog(activity);
			break;
		case MENU_CHANGE_GPS_FILE:
			activity.startActivity(new Intent(activity, GPSChooseActivity.class));
			break;
		case MENU_VIEW_CRASH_LOG:
			viewLastCrashLog(activity);
			break;
		case MENU_CHANGE_SERVER:
			activity.startActivity(new Intent(activity, ContextManager.class));
			break;
		case MENU_GET_SINGLE_FIX:
			getSingleFix(activity);
			break;
		case MENU_RECORD_ANIMATION_FPS:
			toggleAnimationFPSRecording(item);
			break;
		case MENU_UPLOAD_QA_LOG:
			LBSQALoggerManager.persistLogging(new LBSQALogUploadListener(activity));
			break;
		}
	}

	public static void getSingleFix(final Activity act) {
		
		LocationWrapper.getInstance().requestSingleFix(new com.nbi.location.LocationListener(){

			@Override
			public void locationUpdated(com.nbi.location.Location location) {
				Toast.makeText(act,
						"lat:" + location.getLatitude() + ",lon:"
								+ location.getLongitude() + ",type:"
								+ location.getLocationType() + ",accuracy:"
								+ location.getAccuracy(), Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onLocationError(int errorCode) {
				Toast.makeText(act, "get one fix error:"+errorCode, Toast.LENGTH_LONG).show();
			}

			@Override
			public void providerStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}}, LocationWrapper.FAST_MODE);
	}

	public static void viewLastCrashLog(Context ctx) {
		if (!checkCrashLog(ctx)) {
			return;
		}
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Uri uri1 = Uri.parse(string);
		// intent.setDataAndType(uri1, "text/plain");

		String[] files = new File(CrashHandler.CRASH_LOG_DIR).list();
		Uri uri = Uri.fromFile(new File(CrashHandler.CRASH_LOG_DIR + files[files.length - 1]));
		intent.setDataAndType(uri, "text/plain");
		ctx.startActivity(Intent.createChooser(intent, "Choose Application..."));
	}

	public static void clearCrashLog(final Activity activity) {
		if (!checkCrashLog(activity)) {
			return;
		}
		AlertDialog.Builder builder = new Builder(activity);
		builder.setMessage("The change cannot be undone!");
		builder.setTitle("Clear Crash Log");
		builder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				deleteAllCrashLog(activity);
			}
		});

		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	public static void deleteAllCrashLog(Activity activity) {
		String[] files = new File(CrashHandler.CRASH_LOG_DIR).list();
		for (String file : files) {
			new File(CrashHandler.CRASH_LOG_DIR + file).delete();
		}
		Toast.makeText(activity, "All logs are deleted.", Toast.LENGTH_LONG).show();
	}

	private static void toggleAnimationFPSRecording(MenuItem item) {
		if (isAnimationFPSRecordEnabled) {
			item.setTitle(MENU_TITLE_LOG_FPS);
			Search.setDebugModeEnabled(false);
			isAnimationFPSRecordEnabled = false;
		} else {
			item.setTitle(MENU_TITLE_STOP_LOG_FPS);
			Toast.makeText(appCtx, "Log to: " + ANMATION_PERFORMANCE_LOG_FILE, Toast.LENGTH_LONG).show();
			Search.setDebugModeEnabled(true);
			isAnimationFPSRecordEnabled = true;
		}
		saveAnimationFPSRecordingStateToPref(isAnimationFPSRecordEnabled);
	}

	private static void saveAnimationFPSRecordingStateToPref(boolean enabled) {
		SharedPreferences pref = appCtx.getSharedPreferences(PREF_FILE_FOR_DEBUG, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putBoolean(PREF_KEY_ANIMATION_FPS_RECORD_STATE, enabled);
		editor.commit();
	}

	private static boolean getAnimationFPSRecordingStateFromPref() {
		SharedPreferences pref = appCtx.getSharedPreferences(PREF_FILE_FOR_DEBUG, Context.MODE_PRIVATE);
		boolean isEnabled = pref.getBoolean(PREF_KEY_ANIMATION_FPS_RECORD_STATE, false);
		Search.setDebugModeEnabled(isEnabled);
		return isEnabled;
	}

	public static void recordAnimationFPS(final Activity activity, final String animationName, final long duration) {
		if (!SearchKitManager.isDebugModeEnabled()) {
			return;
		}
		final View view = activity.getWindow().getDecorView();
		recordAnimationFPS(view, animationName, duration);
	}

	public static void recordAnimationFPS(final View view, final String animationName, final long duration) {
		if (!SearchKitManager.isDebugModeEnabled()) {
			return;
		}
		view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			private float frame = 0f;
			private boolean isTimerStarted = false;

			private void startTimerIfNeeded() {
				if (isTimerStarted) {
					return;
				}
				final OnPreDrawListener self = this;
				final Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					public void run() {
						timer.cancel();
						view.getViewTreeObserver().removeOnPreDrawListener(self);
						final float fps = frame / ((float) duration / 1000f);
						String info = " " + animationName + ": fps=" + fps;
						String logInfo = DateUtils.formatDate(new Date()) + ": " + animationName + " frame=" + (int) frame
								+ " duration=" + duration + " fps=" + fps;
						Log.i("Test", logInfo);
						Looper.prepare();
						Toast.makeText(view.getContext(), info, Toast.LENGTH_LONG).show();
						FileUtils.saveDataToFile(new File(ANMATION_PERFORMANCE_LOG_FILE), logInfo + "\n", true);
						Looper.loop();
					}
				};
				isTimerStarted = true;
				timer.schedule(task, duration);
			}

			public boolean onPreDraw() {
				startTimerIfNeeded();
				++frame;
				view.invalidate(0, 0, 1, 1);
				return true;
			}
		});
	}

	private static boolean checkCrashLog(Context ctx) {
		if (!AppConstants.isDebugMode()) {
			return false;
		}
		String[] files = new File(CrashHandler.CRASH_LOG_DIR).list();
		if (files == null || files.length == 0) {
			Toast.makeText(ctx, "No Logs found.", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

}
