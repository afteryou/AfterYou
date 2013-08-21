package com.navbuilder.app.hurricane;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.navbuilder.app.hurricane.slidingmenu.SlidingActivity;
import com.navbuilder.app.hurricane.utils.DebugUtils;

public class BaseActivity extends SlidingActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityStack.getInstance().push(this);
	}

	@SuppressWarnings("unchecked")
	public <T> T findViewByIdAutoCast(int id) {
		return (T) this.findViewById(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		DebugUtils.addDebugMenuItems(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		DebugUtils.onDebugOptionsItemSelected(this, item);
		return super.onOptionsItemSelected(item);
	}
}
