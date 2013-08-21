package com.navbuilder.app.hurricane.home;

import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.navbuider.app.hurricane.uiflow.home.HomeController;
import com.navbuilder.app.hurricane.BaseActivity;
import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.utils.DebugUtils;
import com.nbi.search.ui.widget.AddInterestFragment;

public class AddInterestActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.add_interest_activity);
		overridePendingTransition(R.anim.slid_in_from_bottom, R.anim.slid_out_from_top);
		
		DebugUtils.recordAnimationFPS(this, "(AddInterests)Bottom Slid", 500);
		
		FragmentTransaction t = getFragmentManager().beginTransaction();
		t.replace(R.id.mainContainer, new AddInterestFragment());
		t.commit();

		getActionBar().setTitle(R.string.IDS_ADD_INTEREST);
		getActionBar().setIcon(R.drawable.actionbar_icon);
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void onPause() {
		super.onPause();
		DebugUtils.recordAnimationFPS(this, "(AddInterests)Bottom Slid", 500);
	}

	@Override
	public void onBackPressed() {
		HomeUIController.getInstance().getHomeController().handleBackAction();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			HomeUIController.getInstance().getHomeController().handleMenuItem(HomeController.MENU_ITEM_HOME);
		}
		return super.onOptionsItemSelected(item);
	}	
}
