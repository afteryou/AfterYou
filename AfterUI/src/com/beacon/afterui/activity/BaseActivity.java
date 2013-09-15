package com.beacon.afterui.activity;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.beacon.afterui.R;
import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.log.AfterUIlog;
import com.beacon.afterui.utils.DebugUtils;
import com.beacon.afterui.utils.FacebookGraphUserInfo;
import com.beacon.afterui.views.LandingActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;


public class BaseActivity extends Activity {

	protected static final String TAG = "BaseActivity";

	public static final String VIEW_TYPE = "_view_type";

	public static final String VIEW_ROOT = "_view_root";
	
	private static final int MENU_LOG_OUT = 1001;
	
	private static final int MENU_DEBUG_FACEBOOK = 1002;

	protected int mViewType;

	protected boolean mIsRootView;

	protected ScreenManager mScreenManager;

	private ControllerManager mControllerManager;
	
    private Session userInfoSession = null; // the Session used to fetch the current user info

	private UserInfoChangedCallback userInfoChangedCallback;

	private GraphUser user = null;

	
	public void setIsRootView(boolean isRootView) {
		this.mIsRootView = isRootView;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mViewType = bundle.getInt(VIEW_TYPE);
			mIsRootView = bundle.getBoolean(VIEW_ROOT, false);
		}

		mControllerManager = ControllerManager.getInstance();
		mScreenManager = mControllerManager.getSCUIController(ScreenManager.class);
		pruneStack(mIsRootView);
		mScreenManager.setCurrentContext(this);
		AfterUIlog.i(TAG, "BaseActivity.onCreate done: mViewType=" + mViewType);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mScreenManager.setCurrentContext(this);
	}

	@Override
	public void finish() {
		if (mScreenManager != null) {
			mScreenManager.getActivityStack().remove(this);
		}

		super.finish();
	}

	@Override
	public void onBackPressed() {
		if (mIsRootView) {
			exitApp();
			return;
		}
		finish();
	}

	public void exitApp(){
		// Calling destroy causes crash. On Thunderbolt application hungs
		// when crashing.
		// As a workaround we just kill the process.
		// ControllerManager cm = ControllerManager.getInstance();
		// cm.destroy();
		// android.os.Process.killProcess(android.os.Process.myPid());
		ActivityManager activityMgr = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		activityMgr.killBackgroundProcesses(getPackageName());
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}
	
	public int getViewType() {
		return mViewType;
	}

	private void pruneStack(boolean root) {
		ActivityStack<BaseActivity> actStack = mScreenManager.getActivityStack();
		if (root) {
			actStack.removeAllElements();
		}

		actStack.push(this);
	}

	private void hackToShowActionBarOverflow() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}
	}
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		return null;
	}


	@SuppressWarnings("unchecked")
	public <T> T findViewByIdAutoCast(int id) {
		return (T) this.findViewById(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		DebugUtils.addDebugMenuItems(menu);
		Session session = Session.getActiveSession();
        if (session.isOpened()) {
        	menu.add(Menu.NONE,MENU_LOG_OUT,1,R.string.Log_Out);
        	if(user != null)
        	{
        		menu.add(Menu.NONE,MENU_DEBUG_FACEBOOK,2,"Check Facebook User");
        	}
        }
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		DebugUtils.onDebugOptionsItemSelected(this, item);
		switch (item.getItemId()) {
		case MENU_LOG_OUT:
			onClickLogout();
			break;
		case MENU_DEBUG_FACEBOOK:
			Intent intent = new Intent(this, FacebookGraphUserInfo.class);
	        try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				AfterUIlog.e(TAG, " Activity not found : " + e.getMessage());
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public ControllerManager getControllerManager() {
		return mControllerManager;
	}
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }
    
    protected void fetchUserInfo() {
            final Session currentSession = ((AfterYouApplication)getApplication()).getOpenSession();
            user  = ((AfterYouApplication)getApplication()).getUser();
            if (currentSession != null) {
                if (currentSession != userInfoSession) {
                    Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser me,  Response response) {
                                user = me;
                                if (userInfoChangedCallback != null) {
                                    userInfoChangedCallback.onUserInfoFetched(user);
                                }
                            if (response.getError() != null) {
                        		AfterUIlog.i(TAG, "FetchUser Info Exception=" + response.getError().getException());
                            }
                            invalidateOptionsMenu();
                        }
                    });
                    Request.executeBatchAsync(request);
                    userInfoSession = currentSession;
                }
            } else {
                user = null;
                if (userInfoChangedCallback != null) {
                    userInfoChangedCallback.onUserInfoFetched(user);
                }
            }
    }
    
    /**
     * Specifies a callback interface that will be called when the button's notion of the current
     * user changes (if the fetch_user_info attribute is true for this control).
     */
    public interface UserInfoChangedCallback {
        /**
         * Called when the current user changes.
         * @param user  the current user, or null if there is no user
         */
        void onUserInfoFetched(GraphUser user);
    }
    
    /**
     * Gets the callback interface that will be called when the current user changes.
     * @return the callback interface
     */
    public UserInfoChangedCallback getUserInfoChangedCallback() {
        return userInfoChangedCallback;
    }

    /**
     * Sets the callback interface that will be called when the current user changes.
     *
     * @param userInfoChangedCallback   the callback interface
     */
    public void setUserInfoChangedCallback(UserInfoChangedCallback userInfoChangedCallback) {
        this.userInfoChangedCallback = userInfoChangedCallback;
    }


    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
        finish();
        Intent intent = new Intent(this, LandingActivity.class);
        try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			AfterUIlog.e(TAG, " Activity not found : " + e.getMessage());
		}
    }
    

}
