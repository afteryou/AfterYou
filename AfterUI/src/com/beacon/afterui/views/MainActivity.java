package com.beacon.afterui.views;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.chat.ChatManager;
import com.beacon.afterui.sliding.SlidingMenu;
import com.beacon.afterui.sliding.customViews.ListPopupMenu;
import com.beacon.afterui.sliding.fragment.ChatMenuFragment;
import com.beacon.afterui.sliding.fragment.ContentFragment;
import com.beacon.afterui.sliding.fragment.FragmentHelper;
import com.beacon.afterui.sliding.fragment.ISearchFunction;
import com.beacon.afterui.sliding.fragment.SlidingMenuFragment;
import com.beacon.afterui.utils.DebugUtils;
import com.beacon.afterui.utils.WindowUtils;
import com.beacon.afterui.views.data.InterestController;

public class MainActivity extends BaseActivity implements OnQueryTextListener,
        OnClickListener {

    private static final boolean ACTION_BAR_NAV_MENU_ENABLED = false;
    private SearchView mSearchView;
    private ListPopupMenu mActionBarNavMenu;
    private TextView mActionBarNavMenuText;
    private Menu mMenu;

    // private NBAssetManager mAssertMAnager;

    private View mActionbarListIcon, mActionbarChatIcon;
    private SearchRecentSuggestions mSarchSuggestions;

    private TextView mActionbarTitleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setIsRootView(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_container);
        initActionBar();
        initLeftSlidMenu();
        initRightSlidMenu();
        FragmentHelper.onActivityCreate(this);
        FragmentHelper.initFragment(this, new ContentFragment(this));
        doInterestSearch();
        // this.getWindow().setSoftInputMode(
        // WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        bindService();
    }

    private void bindService() {
        // Bind to LocalService
        Intent intent = new Intent(this, ChatManager.class);
        bindService(intent, mServicConnection, Context.BIND_AUTO_CREATE);
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.list_icon);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (ACTION_BAR_NAV_MENU_ENABLED) {
            actionBar.setCustomView(R.layout.actionbar_custom_view);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            ViewGroup customView = (ViewGroup) actionBar.getCustomView();
            customView.findViewById(R.id.btn_actionbar_dropdown_menu)
                    .setOnClickListener(this);
            mActionBarNavMenuText = (TextView) customView
                    .findViewById(R.id.actionbar_menu_text);
            // mActionBarNavMenuText.setText(this.getResources().getStringArray(R.array.actionbar_nav_items)[0]);
        } else {
            actionBar.setCustomView(R.layout.main_activity_actionbar);
            View customView = actionBar.getCustomView();
            mActionbarListIcon = customView.findViewById(R.id.icon_list);
            mActionbarListIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle(SlidingMenu.LEFT);
                }
            });
            mActionbarChatIcon = customView.findViewById(R.id.icon_chat);
            mActionbarChatIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle(SlidingMenu.RIGHT);
                }
            });
            this.getSlidingMenu().setOnMenuStateOpenListener(
                    new SlidingMenu.OnOpenListener() {

                        @Override
                        public void onOpen() {
                            mActionbarListIcon.animate().translationX(-20.0f);

                        }
                    });

            this.getSlidingMenu().setOnMenuStateCloseListener(
                    new SlidingMenu.OnCloseListener() {

                        @Override
                        public void onClose() {
                            mActionbarListIcon.animate().translationX(0.0f);

                        }
                    });
            mActionbarTitleView = (TextView) customView
                    .findViewById(R.id.actionbar_title_text);
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
            mActionBarNavMenu = new ListPopupMenu(getApplicationContext(),
                    R.layout.actionbar_menu_item, R.id.item_text,
                    getResources().getStringArray(R.array.body_type_choices)); // TODO
            mActionBarNavMenu.getListView().setSelector(
                    R.drawable.actionbar_btn_bg_selected);

            mActionBarNavMenu.getListView().setOnItemClickListener(
                    new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent,
                                View view, int position, long id) {
                            String text = mActionBarNavMenu.getAdapter()
                                    .getItem(position).getText();
                            Toast.makeText(parent.getContext(),
                                    text + " Clicked", Toast.LENGTH_SHORT)
                                    .show();
                            mActionBarNavMenuText.setText(text);
                            mActionBarNavMenu.dismiss();
                        }
                    });
        }
        return mActionBarNavMenu;
    }

    private void initLeftSlidMenu() {
        FrameLayout left = new FrameLayout(this);
        left.setId("LEFT".hashCode());
        setBehindLeftContentView(left);
        setSlidingActionBarEnabled(false);
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        SlidingMenuFragment menuFragment = new SlidingMenuFragment();
        fragmentTransaction.replace("LEFT".hashCode(), menuFragment,
                SlidingMenuFragment.TAG);
        fragmentTransaction.commit();
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setShadowDrawable(R.drawable.shadow, SlidingMenu.LEFT);
        slidingMenu.setShadowWidth(30);
        setSlidingMenuOffset(true);
        slidingMenu.setFadeDegree(0.9f);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

    private void initRightSlidMenu() {
        FrameLayout right = new FrameLayout(this);
        right.setId("RIGHT".hashCode());
        setBehindRightContentView(right);
        setSlidingActionBarEnabled(false);
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();

        ChatMenuFragment chatFragment = new ChatMenuFragment();
        fragmentTransaction.replace("RIGHT".hashCode(), chatFragment,
                ChatMenuFragment.TAG);
        fragmentTransaction.commit();
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setShadowDrawable(R.drawable.shadow, SlidingMenu.RIGHT);
        slidingMenu.setShadowWidth(20);
        setSlidingMenuOffset(false);
        slidingMenu.setFadeDegree(0.6f);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

    }

    private void doInterestSearch() {
        Handler uiHandler = new Handler(getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = FragmentHelper.getCurruntFragment();
                if (fragment != null && fragment instanceof ISearchFunction) {
                    ((ISearchFunction) fragment).doSearch(
                            InterestController.REQUEST_TYPE_INIT, null);
                }

            }
        });

    }

    public void updateToMainScreenActionBar() {
        if (mMenu != null) {
            clearActionBar();
        }
    }

    public void updateToDetailsScreenActionBar(String title) {
        clearActionBar();
        createShareAction();
        getActionBar().setDisplayOptions(
                ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE
                        | ActionBar.DISPLAY_HOME_AS_UP);
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
        MenuItem actionItem = menu
                .findItem(R.id.menu_item_share_action_provider_action_bar);
        ShareActionProvider actionProvider = (ShareActionProvider) actionItem
                .getActionProvider();
        actionProvider
                .setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        actionProvider.setShareIntent(createShareIntent());

        // // Set file with share history to the provider and set the share
        // intent.
        // MenuItem overflowItem =
        // menu.findItem(R.id.menu_item_share_action_provider_overflow);
        // ShareActionProvider overflowProvider = (ShareActionProvider)
        // overflowItem.getActionProvider();
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
        SlidingMenu slidingMenu = getSlidingMenu();
        if (!slidingMenu.isActivated()) {
            super.onBackPressed();
        } else {
            if (!FragmentHelper.onBack(this)) {
                SlidingMenuFragment slidingMenuFragment = (SlidingMenuFragment) getFragmentManager()
                        .findFragmentByTag(SlidingMenuFragment.TAG);
                if (slidingMenuFragment.onBack()) {
                    return;
                }
                super.onBackPressed();
            }
        }
    }

    private void setSlidingMenuOffset(boolean left) {
        int orientation = this.getResources().getConfiguration().orientation;
        int offset = orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 3;
        if (left) {
            getSlidingMenu().setBehindOffset(
                    WindowUtils.getDisplayMetrics(this).widthPixels / offset
                            - 10, SlidingMenu.LEFT);
        } else {
            getSlidingMenu().setBehindOffset(
                    WindowUtils.getDisplayMetrics(this).widthPixels / offset
                            - 10, SlidingMenu.RIGHT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        fixSlidingMenuBugOnScreenRotation();
        super.onConfigurationChanged(newConfig);
    }

    private void fixSlidingMenuBugOnScreenRotation() {
        final SlidingMenu slidingMenu = this.getSlidingMenu();
        slidingMenu.getViewTreeObserver().addOnPreDrawListener(
                new OnPreDrawListener() {
                    private int flag = 1;

                    public boolean onPreDraw() {
                        if (flag == 8) {
                            slidingMenu.getViewTreeObserver()
                                    .removeOnPreDrawListener(this);
                        }
                        flag++;
                        slidingMenu.showAbove();
                        return false;
                    }
                });
        setSlidingMenuOffset(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            if (getSlidingMenu().isSlidingEnabled()) {
                toggle(SlidingMenu.LEFT);
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
        }
        mSarchSuggestions.saveRecentQuery(query, null);
        return true;
    }

    private void setActionbarTitle(String title) {
        mActionbarTitleView.setText(title);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        showAbove();
        // SingleSearchController.search(newText, null, new
        // SingleSearchCallBack(), -1);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slid_in_from_top,
                R.anim.slid_out_from_bottom);
        super.onActivityResult(requestCode, resultCode, data);
        getFragmentManager().findFragmentByTag(SlidingMenuFragment.TAG)
                .onActivityResult(requestCode, resultCode, data);
    }

    private ServiceConnection mServicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };

    @Override
    protected void onDestroy() {
        unbindService(mServicConnection);
    }

}
