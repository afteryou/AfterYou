/*
 * (C) Copyright 2013 by TeleCommunication Systems, Inc.
 * 
 * The information contained herein is confidential, proprietary
 * to TeleCommunication Systems, Inc., and considered a trade secret
 * as defined in section 499C of the penal code of the State of
 * California. Use of this information by anyone other than
 * authorized employees of TeleCommunication Systems is granted only
 * under a written non-disclosure agreement, expressly prescribing
 * the scope and manner of such use.
 */

package com.navbuilder.app.hurricane;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

import com.navbuider.app.hurricane.uiflow.HurricaneAppController;
import com.navbuider.app.hurricane.uiflow.HurricaneAppController.Screen;
import com.navbuilder.app.hurricane.home.HomeActivity;
import com.navbuilder.app.hurricane.utils.ReferenceListener;
import com.navbuilder.app.hurricane.utils.WeakArrayList;
import com.navbuilder.app.hurricane.home.AddInterestActivity;

public class ActivityStack extends WeakArrayList<Activity> {

	private static ActivityStack instance;
	private boolean isOnPush = false;

	private static final Map<Class<? extends Activity>, Screen> activityScreenMapping = new HashMap<Class<? extends Activity>, Screen>();
	
	static{
		activityScreenMapping.put(HomeActivity.class, Screen.homeScreen);
		activityScreenMapping.put(AddInterestActivity.class, Screen.addInterestScreen);
	}

	public static synchronized ActivityStack getInstance() {
		if (instance == null) {
			instance = new ActivityStack();
		}
		return instance;
	}

	private ActivityStack() {
		super();
		this.setReferenceListener(new ReferenceListener() {
			@Override
			public void onReferenceReleased(int releaseNum) {
				fireStackChanged();
			}
		});
	}

	private void fireStackChanged() {
		if (size() > 0) {
			HurricaneAppController.getInstance().updateScreenStack(size(), activityScreenMapping.get(getTop().getClass()));
		}else{
			HurricaneAppController.getInstance().updateScreenStack(0, null);
		}
	}

	/**
	 * @param Activity
	 */
	public synchronized boolean push(Activity activity) {
		try {
			if (this.contains(activity)) {
				return false;
			}
			isOnPush = true;
			add(0, activity);
			fireStackChanged();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public synchronized Activity getTop() {
		if (this.isEmpty()) {
			return null;
		}
		return this.get(0);
	}

	public synchronized Activity pop() {
		if (this.isEmpty()) {
			return null;
		}
		Activity obj = this.get(0);
		this.remove(obj);
		fireStackChanged();
		return obj;
	}

	@Override
	public void add(int index, Activity element) {
		// only allow push()
		if (isOnPush) {
			super.add(index, element);
			isOnPush = false;
		}
	}

	@Override
	public boolean addAll(int location, Collection<? extends Activity> collection) {
		// disable addAll()
		return false;
	}

	public synchronized void clearAndFinishAllActivity() {
		for (Activity a : this) {
			a.finish();
		}
		this.clear();
	}

	public HomeActivity getHomeActivity() {
		for (Activity activity : this) {
			if (activity instanceof HomeActivity) {
				return (HomeActivity) activity;
			}
		}
		return null;
	}
}