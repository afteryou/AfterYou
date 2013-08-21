package com.navbuilder.app.hurricane.home;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.navbuider.app.hurricane.uiflow.home.HomeController;
import com.navbuilder.app.hurricane.BaseActivity;
import com.navbuilder.app.hurricane.HurricaneAppUIController;
import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.map.MapManager;
import com.navbuilder.app.hurricane.preference.PreferenceEngine;
import com.navbuilder.app.hurricane.slidingmenu.SlidingMenu;
import com.navbuilder.app.hurricane.slidingmenu.SlidingMenu.MenuState;
import com.navbuilder.app.hurricane.slidingmenu.SlidingMenu.OnSlidingStateChangedListener;
import com.navbuilder.app.hurricane.startup.IStartupActivity;
import com.navbuilder.app.hurricane.startup.StartupController;
import com.navbuilder.app.hurricane.utils.DebugUtils;
import com.navbuilder.app.hurricane.utils.Nimlog;
import com.navbuilder.app.hurricane.utils.WindowUtils;
import com.nbi.common.data.MapLocation;
import com.nbi.search.ui.Card;
import com.nbi.search.ui.Search;
import com.nbi.search.ui.view.ListPopupMenu;
import com.nbi.search.ui.widget.ContentFragment;
import com.nbi.search.ui.widget.InterestSelectorFragment;

public class HomeActivity extends BaseActivity implements OnClickListener, IStartupActivity {

	private SearchView				mSearchView;
	private ListPopupMenu			mActionBarNavMenu;
	private TextView				mActionBarNavMenuText;
	private Menu					mMenu;
	private View					mActionbarListIcon;
	private TextView				mActionbarTitleView;
	private MapManager				mMapManager;
	private MapFragment				mMapFragment;
	private MenuItem				mActionbarBookmarkMenu;
	private OnSlidingStateChangedListener mOnSlidingStateChangedListener = new OnSlidingStateChangedListener() {
		@Override
		public void onMenuStateChanged(MenuState state) {
			switch (state) {
				case OPEN:
					float transX = getResources().getDimension(R.dimen.actionbar_list_icon_anim_x);
					HomeUIController.getInstance().getHomeController().updateSlidingMenuState(true);
					mActionbarListIcon.animate().translationX(-transX);
					break;
				case CLOSE:
					HomeUIController.getInstance().getHomeController().updateSlidingMenuState(false);
					mActionbarListIcon.animate().translationX(0.0f);
					break;
			}
			DebugUtils.recordAnimationFPS(HomeActivity.this, "(Interest Selector)Side Slide", 500);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragment_container);
		initActionBar();
		initSlidingMenu();
		initContentFragment();
		StartupController.getInstance(this).doStart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		overridePendingTransition(R.anim.slid_in_from_top, R.anim.slid_out_from_bottom);
		StartupController.getInstance(this).continueTask();
	}

	@Override
	public void onTaskStart() {
		Nimlog.d(this, "[MainActivity][onTaskStart]task start");

	}

	@Override
	public void onTaskEnd() {
		Nimlog.d(this, "[MainActivity][onTaskEnd]task end");
		PreferenceEngine.setFirstTimeStart(false);
		Search.start();
		// initMapKit();
	}

	@Override
	public void onTaskError() {
		Nimlog.d(this, "[MainActivity][onTaskError]task error, app will exit");
		HurricaneAppUIController.getInstance().exitApp();
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.list_icon);
		actionBar.setDisplayHomeAsUpEnabled(true);

		
		actionBar.setCustomView(R.layout.main_activity_actionbar);
		View customView = actionBar.getCustomView();
		mActionbarListIcon = customView.findViewById(R.id.icon_list);
		customView.findViewById(R.id.home).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HomeUIController.getInstance().getHomeController().handleMenuItem(HomeController.MENU_ITEM_HOME);
			}
		});
		getSlidingMenu().setOnMenuStateListener(mOnSlidingStateChangedListener);
		mActionbarTitleView = (TextView) customView.findViewById(R.id.actionbar_title_text);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setTitle(getString(R.string.app_name));
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_actionbar_dropdown_menu:
				ListPopupMenu popup = getActionBarNavMenu();
				if (!popup.isShowing()) {
					popup.showAsDropDown(v);
				}
				break;
		}
	}

	private ListPopupMenu getActionBarNavMenu() {
		if (mActionBarNavMenu == null) {
			mActionBarNavMenu = new ListPopupMenu(getApplicationContext(), R.layout.actionbar_menu_item, R.id.item_text,
					getResources().getStringArray(R.array.actionbar_nav_items));
			mActionBarNavMenu.getListView().setSelector(R.drawable.actionbar_btn_bg_selected);

			mActionBarNavMenu.getListView().setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String text = mActionBarNavMenu.getAdapter().getItem(position).getText();
					Toast.makeText(parent.getContext(), text + " Clicked", Toast.LENGTH_SHORT).show();
					mActionBarNavMenuText.setText(text);
					mActionBarNavMenu.dismiss();
				}
			});
		}
		return mActionBarNavMenu;
	}

	private void initSlidingMenu() {
		setBehindContentView(R.layout.sliding_menu_container);
		setSlidingActionBarEnabled(false);
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		InterestSelectorFragment menuFragment = new InterestSelectorFragment();

		fragmentTransaction.replace(R.id.sliding_menu_container, menuFragment, InterestSelectorFragment.TAG);
		fragmentTransaction.commit();
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setShadowWidth((int)getResources().getDimension(R.dimen.sliding_menu_shadow_width));
		setSlidingMenuOffset();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	private void initContentFragment() {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.content_detail, new ContentFragment(), ContentFragment.class.toString());
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		updateToMainScreenActionBar(menu);
		return true;
	}

	public void updateToMainScreenActionBar(String title) {
		if (mMenu != null) {
			clearActionBar();
			updateToMainScreenActionBar(mMenu);
			mActionbarTitleView.setText(title);
		}
	}

	private void updateToMainScreenActionBar(Menu menu) {
		mMenu = menu;
		initActionBar();
		getMenuInflater().inflate(R.menu.actionbar, menu);
		mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		mSearchView.setOnSearchClickListener(new OnClickListener() {
			public void onClick(View v) {
				showAbove();
			}
		});
		// mSearchView.setOnQueryTextListener(this);
		// Enable voice search.
		SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
		mSearchView.setSearchableInfo(searchableInfo);
		mSearchView.setFocusable(false);
		mSearchView.setFocusableInTouchMode(false);

		getActionBar().setIcon(R.drawable.list_icon);
		getSlidingMenu().setSlidingEnabled(true);
	}

	public void updateToDetailsScreenActionBar(Card card) {
		clearActionBar();
		createDitailsActionbarItems();
		// getActionBar().setTitle(title);
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
		getActionBar().setIcon(R.drawable.actionbar_icon);
		getActionBar().setTitle(card.getTitle());
		mActionbarBookmarkMenu.setIcon(card.isBookmark() ? R.drawable.actionbar_bookmarked : R.drawable.actionbar_notbookmarked);
		getSlidingMenu().setSlidingEnabled(false);
	}

	private void clearActionBar() {
		mMenu.clear();
		DebugUtils.addDebugMenuItems(mMenu);
		getActionBar().setCustomView(null);
	}

	private void createDitailsActionbarItems() {
		Menu menu = mMenu;
		// Inflate your menu.
		getMenuInflater().inflate(R.menu.actionbar_details_view, menu);
		mActionbarBookmarkMenu = menu.findItem(R.id.action_bookmark);
		// Set file with share history to the provider and set the share intent.
		MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
		actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		actionProvider.setShareIntent(createShareIntent());

		// // Set file with share history to the provider and set the share intent.
		// MenuItem overflowItem = menu.findItem(R.id.menu_item_share_action_provider_overflow);
		// ShareActionProvider overflowProvider = (ShareActionProvider) overflowItem.getActionProvider();
		// overflowProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// // Note that you can set/change the intent any time,
		// // say when the user has selected an image.
		// overflowProvider.setShareIntent(createShareIntent());
	}

	/**
	 * Creates a sharing {@link Intent}.
	 * 
	 * @return The sharing intent.
	 */
	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, "TEST TEXT");
		// shareIntent.setType("image/*");
		// Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
		// shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		return shareIntent;
	}

	public void onBackPressed() {
		if (mMapFragment != null && mMapFragment.isActivated()) {
			FrameLayout contentDetailView = (FrameLayout) findViewById(R.id.content_detail);
			// MarginLayoutParams lp = (MarginLayoutParams) contentDetailView.getLayoutParams();
			// lp.topMargin = 0;
			contentDetailView.animate().translationY(0);
			mMapFragment.onBack();
		}
		HomeUIController.getInstance().getHomeController().handleBackAction();
	}

	private void setSlidingMenuOffset() {
		int orientation = this.getResources().getConfiguration().orientation;
		int offset = orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 3;
		getSlidingMenu().setBehindOffset(WindowUtils.getDisplayMetrics(this).widthPixels / offset - 10);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				HomeUIController.getInstance().getHomeController().handleMenuItem(HomeController.MENU_ITEM_HOME);
				return true;
			case R.id.action_search:
				toast("Search");
				HomeUIController.getInstance().getHomeController().handleMenuItem(HomeController.MENU_ITEM_SEARCH);
				return true;
			case R.id.action_settings:
				toast("Settings");
				HomeUIController.getInstance().getHomeController().handleMenuItem(HomeController.MENU_ITEM_SETTINGS);
				return true;
			case R.id.action_account_manager:
				toast("Account Manager");
				HomeUIController.getInstance().getHomeController().handleMenuItem(HomeController.MENU_ITEM_ACCOUNT_MANAGER);
				return true;
			case R.id.action_about:
				toast("About");
				HomeUIController.getInstance().getHomeController().handleMenuItem(HomeController.MENU_ITEM_ABOUT);
				return true;
			case R.id.action_bookmark:
				HomeUIController.getInstance().getHomeController().handleMenuItem(HomeController.MENU_ITEM_BOOKMARK);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		overridePendingTransition(R.anim.slid_in_from_top, R.anim.slid_out_from_bottom);
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void showMapView(MapLocation mapLocation) {
		mMapManager = MapManager.getInstance();
		mMapManager.setPinLocation(mapLocation);
		if (mMapFragment == null) {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			mMapFragment = new MapFragment();
			fragmentTransaction.replace(R.id.map_container, mMapFragment, ContentFragment.class.toString());
			fragmentTransaction.commit();
		} else {
			mMapFragment.showMapCenter(false);
		}
	}
	
}
