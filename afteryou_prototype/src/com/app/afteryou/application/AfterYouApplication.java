package com.app.afteryou.application;

import android.app.Application;

public class AfterYouApplication extends Application {

	private static Application instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	public static Application getInstance() {
		return instance;
	}

}
