package com.beacon.afterui.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

public final class FileUtils {

	public static final String UTF8 = "UTF-8";

	public static final String ENTER = "\n";

	public static File getSDCardDirectory() {
		return Environment.getExternalStorageDirectory();
	}

	public static void makeDir(String dir) {
		File absDir = new File(dir);
		if (!absDir.exists()) {
			absDir.mkdirs();
		}
	}

	public static String readFile(String file, String encoding) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			return EncodingUtils.getString(buffer, encoding);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(fin);
		}
	}

	public static void saveDataToFile(File fileToWrite, String data) {
		FileOutputStream fOut = null;
		OutputStreamWriter myOutWriter = null;
		data = EncodingUtils.getString(data.getBytes(), UTF8);
		try {
			if (!fileToWrite.exists()) {
				fileToWrite.createNewFile();
			}

			fOut = new FileOutputStream(fileToWrite);
			myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(data);
			myOutWriter.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(fOut);
			close(myOutWriter);
		}
	}

	public static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
			}
		}
	}
}
