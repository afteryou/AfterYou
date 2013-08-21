package com.navbuilder.app.hurricane.home;

import android.content.Intent;

import com.navbuider.app.hurricane.uiflow.home.HomeController;
import com.navbuider.app.hurricane.uiflow.home.HomeUIListener;
import com.navbuilder.app.hurricane.ActivityStack;
import com.nbi.common.data.MapLocation;
import com.nbi.search.ui.Card;

public class HomeUIController implements HomeUIListener {

	private static HomeUIController	instance;
	private HomeController			mHomeController;

	public synchronized static HomeUIController getInstance() {
		if (instance == null) {
			instance = new HomeUIController();
		}
		return instance;
	}

	@Override
	public void init(HomeController controller) {
		mHomeController = controller;
	}

	@Override
	public void showAddInterestScreen() {
		getHomeActivity().startActivity(new Intent(getHomeActivity(), AddInterestActivity.class));
	}

	@Override
	public void updateDetailActionBar(Card card) {
		getHomeActivity().updateToDetailsScreenActionBar(card);
	}

	@Override
	public void updateHomeActionBar(String title) {
		HomeActivity homeActivity = getHomeActivity();
		homeActivity.updateToMainScreenActionBar(title);
		homeActivity.showAbove();
	}

	@Override
	public void showErrorDialog(String arg0) {

	}

	public HomeController getHomeController() {
		return this.mHomeController;
	}

	public void showMapView(MapLocation mapLocation) {
		getHomeActivity().showMapView(mapLocation);
	}

	private HomeActivity getHomeActivity() {
		return ActivityStack.getInstance().getHomeActivity();
	}

	@Override
	public void toggleSlidingMenu() {
		getHomeActivity().toggle();
	}
}
