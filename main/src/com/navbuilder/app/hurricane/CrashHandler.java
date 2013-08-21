package com.navbuilder.app.hurricane;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;

import org.apache.http.impl.cookie.DateUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.navbuilder.app.hurricane.utils.FileUtils;
import com.navbuilder.app.hurricane.utils.Nimlog;

public class CrashHandler implements UncaughtExceptionHandler {
	public static final String TAG = CrashHandler.class.toString();
	private static CrashHandler instance = new CrashHandler();
	private Context mContext;
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private String mDeviceCrashInfo = "";
	private static final String VERSION_NAME = "versionName:";
	private static final String VERSION_CODE = "versionCode:";
	public static final String CRASH_LOG_EXTENSION = ".txt";
	public static final String CRASH_LOG_DIR = FileUtils.getSDCardDirectory() + "/Hurricane/CrashLog/";
	public static final String CRASH_LOG_DATE_PATTERN = "yyyy-MM-dd-HH-mm-sss";

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		if (instance == null)
			instance = new CrashHandler();
		return instance;
	}

	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		handleException(ex);

		try {
			// sleep for showing toast.
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Nimlog.e(TAG, "Error : ", e);
		}
		if (mDefaultHandler != null) {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
			//mDefaultHandler.uncaughtException(thread, ex);
		}
	}

	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return false;
		}
		FileUtils.makeDir(CRASH_LOG_DIR);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				String cause = ex.getCause() == null ? "" : ex.getCause().getLocalizedMessage();
				Toast.makeText(mContext, "Error: " + ex.getLocalizedMessage() + " " + cause, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		collectCrashDeviceInfo(mContext);
		saveCrashInfoToFile(ex);
		return true;
	}

	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo = mDeviceCrashInfo + VERSION_NAME + pi.versionName == null ? "not set"
						: pi.versionName + FileUtils.ENTER;
				mDeviceCrashInfo = mDeviceCrashInfo + VERSION_CODE + pi.versionCode + FileUtils.ENTER;
			}
		} catch (NameNotFoundException e) {
			Nimlog.e(TAG, "Error while collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo = mDeviceCrashInfo + field.getName() + ": " + field.get(null) + FileUtils.ENTER;
			} catch (Exception e) {
				Nimlog.e(TAG, "Error while collect crash info", e);
			}
		}
	}

	private String saveCrashInfoToFile(Throwable ex) {
		PrintWriter printWriter = null;
		try {
			Writer stackTrace = new StringWriter();
			printWriter = new PrintWriter(stackTrace);
			ex.printStackTrace(printWriter);

			Throwable cause = ex.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}
			String result = stackTrace.toString();
			String timestamp = DateUtils.formatDate(new Date(), CRASH_LOG_DATE_PATTERN);
			String fileName = timestamp + CRASH_LOG_EXTENSION;
			FileUtils.saveDataToFile(new File(CRASH_LOG_DIR + fileName), timestamp + FileUtils.ENTER + mDeviceCrashInfo
					+ result);
			return fileName;
		} catch (Exception e) {
			Nimlog.e(TAG, "an error occured while writing report file...", e);
		} finally {
			FileUtils.close(printWriter);
		}
		return null;
	}

}