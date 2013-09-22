package com.beacon.afterui.utils;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.beacon.afterui.application.CrashHandler;
import com.beacon.afterui.constants.AppConstants;

public final class DebugUtils {
	public static final String CRASH_LOG_RECEIVERS = "sarnab.poddar@gmail.com, peacemanav@gmail.com, sachindasaramjimane@gmail.com";

	public static final int MENU_SEND_CRASH_LOG = 0x00000001;
	public static final int MENU_CLEAR_CRASH_LOG = 0x00000003;
	public static final int MENU_VIEW_CRASH_LOG = 0x00000004;
	public static final int MENU_SEND_DDMS_LOG = 0x00000006;
	

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
				buffer.append(FileUtils.readFile(CrashHandler.CRASH_LOG_DIR + file, FileUtils.UTF8)).append(
						FileUtils.ENTER);
			}
		}
		EMailUtils.send(activity, CRASH_LOG_RECEIVERS, CRASH_LOG_RECEIVERS,
				"Crash Log " + Utilities.formatDate(new Date(), CrashHandler.CRASH_LOG_DATE_PATTERN),
				buffer.toString(), files);
	}

	public static void addDebugMenuItems(Menu menu) {
		if (AppConstants.isDebugMode()) {
			menu = menu.addSubMenu(Menu.NONE, 0, 1000, "Debug Tools >");
			menu.addSubMenu(Menu.NONE, MENU_SEND_CRASH_LOG, 1, "Send Crash Log");
			menu.addSubMenu(Menu.NONE, MENU_CLEAR_CRASH_LOG, 2, "Clear Crash Log");
			menu.addSubMenu(Menu.NONE, MENU_VIEW_CRASH_LOG, 3, "View last Crash Log");
			menu.addSubMenu(Menu.NONE, MENU_SEND_DDMS_LOG, 6, "Send DDMS Log");
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
		case MENU_VIEW_CRASH_LOG:
			viewLastCrashLog(activity);
			break;
		case MENU_SEND_DDMS_LOG:
			DDMSLogUtils du = new DDMSLogUtils(activity);
			du.collect();
			String date = Utilities.formatDate(new Date());
			du.sendLog(CRASH_LOG_RECEIVERS, "ddms log " + date, date);
		}
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
