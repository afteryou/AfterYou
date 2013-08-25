package com.app.afteryou;

import java.util.HashMap;
import java.util.Map;

import com.app.afteryou.scui.SCUI_Controller;
import com.app.afteryou.scui.SCUI_ScreenManager;

public class ControllerManager {
	private static ControllerManager mInstance;
	private Map<Class<? extends SCUI_Controller>, SCUI_Controller> mControllersMap = new HashMap<Class<? extends SCUI_Controller>, SCUI_Controller>();

	private ControllerManager() {
		registerSCUIListeners();
	}

	public static ControllerManager getInstance() {
		if (mInstance == null) {
			mInstance = new ControllerManager();
		}
		return mInstance;
	}

	@SuppressWarnings("unchecked")
	public <T extends SCUI_Controller> T getSCUIController(Class<? extends SCUI_Controller> type) {
		return (T) mControllersMap.get(type);
	}

	private void registerSCUIListeners() {
		SCUI_ScreenManager.setListener(ScreenManager.getInstance());
		mControllersMap.put(ScreenManager.class, ScreenManager.getInstance());
	}

}
