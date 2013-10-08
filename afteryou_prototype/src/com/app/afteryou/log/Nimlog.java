
package com.app.afteryou.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.app.afteryou.constants.AppConstants;

import android.content.Context;
import android.util.Log;

public final class Nimlog {

	private static final String LOG_TAG = "NIM-";
	public static void printStackTrace(Throwable e) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		if (e != null) {
			e.printStackTrace();
		}
	}
	public static void v(Object obj, String msg) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		Log.println(Log.VERBOSE, strClassName, msg);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.VERBOSE, strClassName, msg);
		}
	}

	public static void v(Object obj, String msg, Throwable tr) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		String message;
		StringBuffer sb = new StringBuffer(msg);
		sb.append('\n').append(getStackTraceString(tr));
		message = sb.toString();

		Log.println(Log.VERBOSE, strClassName, message);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.VERBOSE, strClassName, message);
		}
	}

	public static void d(Object obj, String msg) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		Log.println(Log.DEBUG, strClassName, msg);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.DEBUG, strClassName, msg);
		}
	}

	public static void d(Object obj, String msg, Throwable tr) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		String message;
		StringBuffer sb = new StringBuffer(msg);
		sb.append('\n').append(getStackTraceString(tr));
		message = sb.toString();

		Log.println(Log.DEBUG, strClassName, message);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.DEBUG, strClassName, message);
		}
	}

	public static void i(Object obj, String msg) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		Log.println(Log.INFO, strClassName, msg);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.INFO, strClassName, msg);
		}
	}

	public static void i(Object obj, String msg, Throwable tr) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		String message;
		StringBuffer sb = new StringBuffer(msg);
		sb.append('\n').append(getStackTraceString(tr));
		message = sb.toString();

		Log.println(Log.INFO, strClassName, message);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.INFO, strClassName, message);
		}
	}

	public static void w(Object obj, String msg) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		Log.println(Log.WARN, strClassName, msg);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.WARN, strClassName, msg);
		}
	}

	public static void w(Object obj, String msg, Throwable tr) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		String message;
		StringBuffer sb = new StringBuffer(msg);
		sb.append('\n').append(getStackTraceString(tr));
		message = sb.toString();

		Log.println(Log.WARN, strClassName, message);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.WARN, strClassName, message);
		}
	}

	public static void w(Object obj, Throwable tr) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		String message = getStackTraceString(tr);

		Log.println(Log.WARN, strClassName, message);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.WARN, strClassName, message);
		}
	}

	public static void e(Object obj, String msg, Throwable tr, Context context) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		String message;
		StringBuffer sb = new StringBuffer(msg);
		sb.append('\n').append(getStackTraceString(tr));
		message = sb.toString();

		Log.e(strClassName, msg, tr);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.ERROR, strClassName, message);
		}
		if (context != null) {
			showToast(context, strClassName + '\n' + message);
		}
	}

	public static void e(Object obj, String msg) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		Log.println(Log.ERROR, strClassName, msg);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.ERROR, strClassName, msg);
		}
	}

	public static void e(Object obj, String msg, Throwable tr) {
		if(!AppConstants.isDebugMode()){
			return;
		}
		String strClassName = getClassName(obj);
		
		if(!AppConstants.isDebugMode()) {
			return;
		}

		String message;
		StringBuffer sb = new StringBuffer(msg);
		sb.append('\n').append(getStackTraceString(tr));
		message = sb.toString();

		Log.e(strClassName, msg, tr);

		if (logToFile != null && enableLog) {
			logToFile.writeLogToFile(Log.ERROR, strClassName, message);
		}
	}

	public static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		return sw.toString();
	}

	public static boolean isLoggable(String tag, int level) {
		return Log.isLoggable(LOG_TAG + tag, level);
	}

	public static int println(int priority, String tag, String msg) {
		if(!AppConstants.isDebugMode()){
			return 0;
		}
		return Log.println(priority, LOG_TAG + tag, msg);
	}

	public static void setLogEnable(boolean bool) {
		enableLog = bool;
	}

	private static LogToFile logToFile;
	private static boolean enableLog = false;
//	private static Handler handler = new Handler();
	static {
		if (enableLog && AppConstants.isDebugMode()) {
			try {
				logToFile = new LogToFile();
			} catch (RuntimeException e) {
				Log.e("NIM-NimLog", e.toString());
			} catch (IOException e) {
				Log.e("NIM-Nimlog", e.toString());
			}	
		}		
	}

	private Nimlog() {

	}

	private static String getClassName(Object obj) {
		String strName = "";
		if(obj instanceof String){
			strName = (String)obj;
		}else{
			strName = obj.getClass().getName();
			int index = strName.lastIndexOf('.');
			if (index != -1) {
				strName = strName.substring(index + 1);
			}
		}
		return LOG_TAG + strName;
	}

	private static void showToast(final Context context, final String str) {
		//TODO, disable it first because some issues.
//		handler.post(new Runnable() {
//			public void run() {
//				Toast.makeText(context, str, Toast.LENGTH_LONG).show();
//			}
//		});
	}


}

class LogToFile {
	public static final String SD_CARD = "/sdcard";
	public static final String FILE1 = "/sdcard/demo.nimlog";
	public static final String FILE2 = "/sdcard/demo2.nimlog";
	public static final String FILE3 = "/sdcard/demo3.nimlog";
	public static final int MAX_FILE_SIZE = 2;// M bytes
	private FileWriter fileWriter;

	public LogToFile() throws RuntimeException, IOException {
		File sdcard = new File(SD_CARD);
		File file1 = new File(FILE1);
		File file2 = new File(FILE2);
		File file3 = new File(FILE3);

		if (!sdcard.exists()) {
			throw new RuntimeException("SD card not exists!");
		} else {
			if (!file1.exists()) {
				try {
					file1.createNewFile();
				} catch (IOException e) {
					throw e;
				}
			} else {
				long fileSize = (file1.length() >>> 20);// convert to M bytes
				if (fileSize > MAX_FILE_SIZE) {
					if (!file2.exists()) {
						file1.renameTo(file2);
						file1 = new File(FILE1);
						try {
							file1.createNewFile();
						} catch (IOException e) {
							throw e;
						}
					} else {
						file2.renameTo(file3);
						file2 = new File(FILE2);
						file1.renameTo(file2);
						file1 = new File(FILE1);
						try {
							file1.createNewFile();
						} catch (IOException e) {
							throw e;
						}
					}
				}
			}
			fileWriter = new FileWriter(file1, true);
		}
	}

	// we use one space to separate elements
	public void writeLogToFile(int priority, String tag, String message) {

		Date date = new Date();
		SimpleDateFormat simpleDateFormate = new SimpleDateFormat(
				"yyyy:MM:dd kk:mm:ss.SSS");
		String strLog = simpleDateFormate.format(date);

		StringBuffer sb = new StringBuffer(strLog);
		sb.append(' ');
		sb.append(strPriority[priority]);
		sb.append(' ');
		sb.append(tag);
		sb.append(' ');
		sb.append(message);
		sb.append('\n');
		strLog = sb.toString();

		try {
			fileWriter.write(strLog);
			fileWriter.flush();
		} catch (IOException e) {
			Log.e("LogToFile", "", e);
		}
	}

	private static final String strPriority[];
	static {
		strPriority = new String[8];
		strPriority[0] = "";
		strPriority[1] = "";
		strPriority[2] = "verbose";
		strPriority[3] = "debug";
		strPriority[4] = "info";
		strPriority[5] = "warn";
		strPriority[6] = "error";
		strPriority[7] = "ASSERT";
	}
}
