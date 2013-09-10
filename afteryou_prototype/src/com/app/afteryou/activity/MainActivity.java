package com.app.afteryou.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.app.afteryou.BaseActivity;
import com.app.afteryou.controller.CategoryController;
import com.app.afteryou.controller.GPSController;
import com.app.afteryou.controller.InterestController;
import com.app.afteryou.controller.StartupController;
import com.app.afteryou.customview.ListPopupMenu;
import com.app.afteryou.fragment.ContentFragment;
import com.app.afteryou.fragment.FragmentHelper;
import com.app.afteryou.fragment.ISearchFunction;
import com.app.afteryou.fragment.SlidingMenuFragment;
import com.app.afteryou.fragment.ISearchFunction.SearchParams;
import com.app.afteryou.fragment.SlidingMenuFragment.OnMenuClickListener;
import com.app.afteryou.log.Nimlog;
import com.app.afteryou.model.Category;
import com.app.afteryou.search.SearchSuggestionsProvider;
import com.app.afteryou.slidingmenu.SlidingMenu;
import com.app.afteryou.slidingmenu.SlidingMenu.MenuState;
import com.app.afteryou.slidingmenu.SlidingMenu.OnMenuStateListener;
import com.app.afteryou.startup.IStartupActivity;
import com.app.afteryou.utils.AppUtils;
import com.app.afteryou.utils.DebugUtils;
import com.app.afteryou.utils.WindowUtils;
import com.app.afteryou.R;
//import com.navbuilder.pal.android.ndk.GPSLocationManager;
//import com.navbuilder.pal.android.ndk.PalRadio;
//import com.navbuilder.sc.ControllerManager;
//import com.navbuilder.sc.ControllerManagerConfig;
import com.nbi.location.LocationListener;
//import com.tcs.jcc.common.NBAssetManager;

public class MainActivity extends BaseActivity implements OnQueryTextListener, OnClickListener,IStartupActivity {

	private static final boolean ACTION_BAR_NAV_MENU_ENABLED = false;
	private SearchView mSearchView;
	private ListPopupMenu mActionBarNavMenu;
	private TextView mActionBarNavMenuText;
	private Menu mMenu;

//	private NBAssetManager mAssertMAnager;

	private View mActionbarListIcon;
	private SearchRecentSuggestions mSarchSuggestions;

	private boolean mapConfigInitialize = false;
	private TextView mActionbarTitleView;
	private boolean isAppInitFinished = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		setIsRootView(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragment_container);
		initActionBar();
		initSlidMenu();
		FragmentHelper.onActivityCreate(this);
		FragmentHelper.initFragment(this, new ContentFragment());

		mSarchSuggestions = new SearchRecentSuggestions(this, SearchSuggestionsProvider.AUTHORITY,
				SearchSuggestionsProvider.MODE);
		StartupController.getInstance(this).doStart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StartupController.getInstance(this).continueTask();
	}
	
	
	@Override
	public void onTaskStart() {
		Nimlog.d(this, "[MainActivity][onTaskStart]task start");

	}

	@Override
	public void onTaskEnd() {
		Nimlog.d(this, "[MainActivity][onTaskEnd]task end");
		AppUtils.setFirstTimeStart(false);
		isAppInitFinished = true;
		doInterestSearch(CategoryController.getInstance().getUserCategoriesFromPref());
	}

	@Override
	public void onTaskError() {
		Nimlog.d(this, "[MainActivity][onTaskError]task error, app will exit");
		exitApp();
	}


	private class SetupTask extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MainActivity.this, "", "Initializing... Please wait...", true);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();

			if (result == Boolean.TRUE) {
				showUI();
			} else {
				AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
				dialog.setTitle("Error");
				dialog.setMessage("Initialization failed!");
				dialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						MainActivity.this.finish();
					}
				});
				dialog.show();
			}
		}
	}

	private void showUI() {
		initControllerManager();

//		ControllerManager cm = ControllerManager.getInstance();
//		cm.getScreenManager().setCurrentContext(this);
//		cm.showUI();
		// We don't need Launcher anymore. Let's finish it
		// finish();
	}

	private boolean initControllerManager() {
		if (!mapConfigInitialize) {

//			String workFolder = mAssertMAnager.getAssetFolder();
//
//			ControllerManagerConfig config = new ControllerManagerConfig();
//			config.setTpsfile(workFolder + "/appconfig/tesla.tpl");
//			config.setLanguage("en");
//			config.setCountry("US");
//			config.setUid("80b053fccead422934b9a3140b72983c450f8832");
//			config.setMapCacheDirectory(workFolder + "/Map");
//			config.setVoiceCacheDirectory(workFolder + "/Voice");
//			config.setQalogFilename(workFolder + "/qalog");
//			config.setCarrierName("Verizon");
//			config.setWorkFolder(workFolder);
//			config.setMapkitCachePath(workFolder + "/mapkit");
//			config.setResourceFolderPath(workFolder);
//			mapConfigInitialize = true;
//			return ControllerManager.initialize(config);
		}
		return true;
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.list_icon);
		actionBar.setDisplayHomeAsUpEnabled(true);

		if (ACTION_BAR_NAV_MENU_ENABLED) {
			actionBar.setCustomView(R.layout.actionbar_custom_view);
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);
			ViewGroup customView = (ViewGroup) actionBar.getCustomView();
			customView.findViewById(R.id.btn_actionbar_dropdown_menu).setOnClickListener(this);
			mActionBarNavMenuText = (TextView) customView.findViewById(R.id.actionbar_menu_text);
			mActionBarNavMenuText.setText(this.getResources().getStringArray(R.array.actionbar_nav_items)[0]);
		} else {
			actionBar.setCustomView(R.layout.main_activity_actionbar);
			View customView = actionBar.getCustomView();
			mActionbarListIcon = customView.findViewById(R.id.icon_list);
			customView.findViewById(R.id.btn_home).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					toggle();
				}
			});
			this.getSlidingMenu().setOnMenuStateListener(new OnMenuStateListener() {

				@Override
				public void onMenuStateChanged(MenuState state) {
					switch (state) {
					case OPEN:
						mActionbarListIcon.animate().translationX(-20.0f);
						break;
					case CLOSE:
						mActionbarListIcon.animate().translationX(0.0f);
						break;
					}
				}
			});
			mActionbarTitleView = (TextView)customView.findViewById(R.id.actionbar_title_text);
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			actionBar.setTitle(getString(R.string.app_name));
		}
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
			mActionBarNavMenu = new ListPopupMenu(getApplicationContext(), R.layout.actionbar_menu_item,
					R.id.item_text, getResources().getStringArray(R.array.actionbar_nav_items));
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

	private void initSlidMenu() {
		setBehindContentView(R.layout.sliding_menu_container);
		setSlidingActionBarEnabled(false);
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		SlidingMenuFragment menuFragment = new SlidingMenuFragment();
		menuFragment.setOnMenuClickListener(new OnMenuClickListener() {

			@Override
			public void onMutilMenuClick(List<Category> categories) {
				if(!isAppInitFinished){
					return;
				}
				setActionbarTitle("All My Interests");
				doInterestSearch(categories);
			}

			@Override
			public void onMenuClick(Category category) {
				if(!isAppInitFinished){
					return;
				}
				setActionbarTitle(category.getName());
				Fragment fragment = FragmentHelper.getCurruntFragment();
				if (fragment != null && fragment instanceof ISearchFunction) {
					int searchType = category.getType() == Category.TYPE_BRAND ? InterestController.REQUEST_TYPE_BRAND
							: InterestController.REQUEST_TYPE_CATEGORY;
					SearchParams params = new SearchParams();
					if (searchType == InterestController.REQUEST_TYPE_BRAND) {
						params.brands = new String[] {category.getName()};
					} else {
						params.categories = new String[] {category.getCode()};
					}
					((ISearchFunction) fragment).doSearch(searchType, params);
				}
			}
		});
		fragmentTransaction.replace(R.id.sliding_menu_container, menuFragment, SlidingMenuFragment.TAG);
		// fragmentTransaction.replace(R.id.content_detail, new
		// DetailFragment(), DetailFragment.class.toString());
		fragmentTransaction.commit();
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setShadowWidth(30);
		setSlidingMenuOffset();
		slidingMenu.setFadeDegree(0.9f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

//	@Override
//	protected void onGPSCheckFinish() {
//		isAppInitFinished = true;
//		doInterestSearch(CategoryController.getInstance().getUserCategoriesFromPref());
//	}

	private void doInterestSearch(List<Category> categories) {
		Fragment fragment = FragmentHelper.getCurruntFragment();
		if (fragment != null && fragment instanceof ISearchFunction) {
			List<String> brands = new ArrayList<String>();
			List<String> interests = new ArrayList<String>();
			for (Category c : categories) {
				if (c.getType() == Category.TYPE_BRAND) {
					brands.add(c.getName());
				} else {
					interests.add(c.getCode());
				}
			}
			SearchParams params = new SearchParams();
			if(!interests.isEmpty()) {
				params.categories = new String[]{};
				interests.toArray(params.categories);
			}
			if(!brands.isEmpty()) {
				params.brands = new String[]{};
				brands.toArray(params.brands);
			}
			((ISearchFunction) fragment).doSearch(InterestController.REQUEST_TYPE_INIT, params);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		updateToMainScreenActionBar(menu);
		return true;
	}

	public void updateToMainScreenActionBar() {
		if (mMenu != null) {
			clearActionBar();
			updateToMainScreenActionBar(mMenu);
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
		mSearchView.setOnQueryTextListener(this);
		// Enable voice search.
		SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
		mSearchView.setSearchableInfo(searchableInfo);
		mSearchView.setFocusable(false);
		mSearchView.setFocusableInTouchMode(false);

		getActionBar().setIcon(R.drawable.list_icon);
		getSlidingMenu().setSlidingEnabled(true);
	}

	public void updateToDetailsScreenActionBar(String title) {
		clearActionBar();
		createShareAction();
		// getActionBar().setTitle(title);
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
		getActionBar().setIcon(R.drawable.actionbar_icon);
		getActionBar().setTitle(title);
		getSlidingMenu().setSlidingEnabled(false);
	}

	private void clearActionBar() {
		mMenu.clear();
		DebugUtils.addDebugMenuItems(mMenu);
		getActionBar().setCustomView(null);
	}

	private void createShareAction() {
		Menu menu = mMenu;
		// Inflate your menu.
		getMenuInflater().inflate(R.menu.actionbar_details_view, menu);

		// Set file with share history to the provider and set the share intent.
		MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
		actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		actionProvider.setShareIntent(createShareIntent());

//		// Set file with share history to the provider and set the share intent.
//		MenuItem overflowItem = menu.findItem(R.id.menu_item_share_action_provider_overflow);
//		ShareActionProvider overflowProvider = (ShareActionProvider) overflowItem.getActionProvider();
//		overflowProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
//		// Note that you can set/change the intent any time,
//		// say when the user has selected an image.
//		overflowProvider.setShareIntent(createShareIntent());
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
		SlidingMenu slidingMenu = getSlidingMenu();
		if (slidingMenu.isActivated()) {
			super.onBackPressed();
		} else {
			if (!FragmentHelper.onBack(this)) {
				SlidingMenuFragment slidingMenuFragment = (SlidingMenuFragment) getFragmentManager().findFragmentByTag(
						SlidingMenuFragment.TAG);
				if (slidingMenuFragment.onBack()) {
					return;
				}
				super.onBackPressed();
			}
		}
	}

	private void setSlidingMenuOffset() {
		int orientation = this.getResources().getConfiguration().orientation;
		int offset = orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 3;
		getSlidingMenu().setBehindOffset(WindowUtils.getDisplayMetrics(this).widthPixels / offset - 10);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		fixSlidingMenuBugOnScreenRotation();
		super.onConfigurationChanged(newConfig);
	}

	private void fixSlidingMenuBugOnScreenRotation() {
		final SlidingMenu slidingMenu = this.getSlidingMenu();
		slidingMenu.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			private int flag = 1;
			public boolean onPreDraw() {
				if(flag == 8){
					slidingMenu.getViewTreeObserver().removeOnPreDrawListener(this);
				}
				flag ++;
				slidingMenu.showAbove(false, true);
				return false;
			}
		});
		setSlidingMenuOffset();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (getSlidingMenu().isSlidingEnabled()) {
				toggle();
			} else {
				onBackPressed();
			}
			return true;
		case R.id.action_search:
			toast("Search");
			return true;
		case R.id.action_settings:
			toast("Settings");
			return true;
		case R.id.action_account_manager:
			toast("Account Manager");
			return true;
		case R.id.action_about:
			toast("About");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
		showAbove();
		Fragment fragment = FragmentHelper.getCurruntFragment();
		if (fragment != null && fragment instanceof ISearchFunction) {
			SearchParams params = new SearchParams();
			params.query = query;
			((ISearchFunction) fragment).doSearch(InterestController.REQUEST_TYPE_INIT, params);
		}
		mSarchSuggestions.saveRecentQuery(query, null);
		return true;
	}
	
	private void setActionbarTitle(String title){
		mActionbarTitleView.setText(title);
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		showAbove();
		//SingleSearchController.search(newText, null, new SingleSearchCallBack(), -1);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		overridePendingTransition(R.anim.slid_in_from_top, R.anim.slid_out_from_bottom);
		super.onActivityResult(requestCode, resultCode, data);
		getFragmentManager().findFragmentByTag(SlidingMenuFragment.TAG).onActivityResult(requestCode, resultCode, data);
	}

}
