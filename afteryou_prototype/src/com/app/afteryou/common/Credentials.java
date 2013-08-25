
package com.app.afteryou.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.TreeMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.app.afteryou.application.AfterYouApplication;
import com.app.afteryou.preference.PreferenceEngine;

public class Credentials {

	private static final String TAG = "Credentials";

	private TreeMap<String, String> mCredentials;

	private static Credentials instance;

	// private SharedPreferences mPreferences;

	/**
	 * Always reads the credential name form shared preferences since the
	 * preferences can be modified by any activity with Credentials object.
	 */
	public String getName() {
		return PreferenceEngine.getInstance().getToken();
	}

	public void saveName(String name) {
		PreferenceEngine.getInstance().saveToken(name);
	}

	public String getValue() {
		String k = getName();
		String s = mCredentials.get(k);
		if (s == null) {
			s = ""; // make sure no crash
		}
		return s;
	}

	public int getCount() {
		return mCredentials.size();
	}

	public String[] getCrendentialNames() {
		String[] sa = {};
		return mCredentials.keySet().toArray(sa);
	}

	private Credentials(Context context) {
		try {
			mCredentials = new TreeMap<String, String>();
			readCredentials(context);
			saveCredentialName(getName());
		} catch (IOException ioe) {
			Log.e(TAG, ioe.getMessage());
			saveCredentialName(ioe.getMessage());
		}
	}

	public static synchronized Credentials instance(){
		if(instance == null){
			instance = new Credentials(AfterYouApplication.getInstance().getApplicationContext());
		}
		return instance;
	}

	private void readCredentials(Context context) throws IOException {
		AssetManager am = context.getResources().getAssets();
		InputStream is = am.open("credentials.properties");
		try {
			Properties p = new Properties();
			p.load(is);
			for (int i = 0; i < 30; i++) {
				String nameKey = "name" + i;
				String valueKey = "value" + i;
				String name = p.getProperty(nameKey);
				String value = p.getProperty(valueKey);
				if (name != null && value != null && !value.contains("@")) {
					mCredentials.put(name, value);
					System.out.print(name + ":" + value);
				}
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	private void saveCredentialName(String token) {
		PreferenceEngine.getInstance().saveToken(token);
	}

}
