package com.navbuilder.app.hurricane;

import android.app.Application;

import com.navbuider.app.hurricane.uiflow.HurricaneAppController;

public class HurricaneApplication extends Application {

	private static Application instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		HurricaneAppController.getInstance().launchAppCommon(HurricaneAppUIController.getInstance());
	}

	public static Application getInstance() {
		return instance;
	}

}
