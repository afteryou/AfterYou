package com.beacon.afterui.views;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.provider.AfterYouMetadata.AuthTable;
import com.beacon.afterui.provider.PreferenceEngine;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;

public class LandingActivity extends BaseActivity implements OnClickListener {

	/** TAG */
	private static final String TAG = LandingActivity.class.getSimpleName();

	private static ImageView sLoginButton;
	private static ImageView sSignUpButton;

	private static final int SPLASH_END = 1;

	private static final int SPALSH_VISIBLE_TIME = 3000;

	private SplashHandler mSplashHandler;

	private static View sFbContainer;
	
	private Context ctx;

	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.landing_screen);
		
		this.ctx = this;

		sLoginButton = (ImageView) findViewById(R.id.login_btn);
		sSignUpButton = (ImageView) findViewById(R.id.signup_btn);
		sFbContainer = findViewById(R.id.fb_container);

		sLoginButton.setOnClickListener(this);
		sSignUpButton.setOnClickListener(this);
		sFbContainer.setOnClickListener(this);

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this)
						.setCallback(statusCallback));
			}
		}

		mSplashHandler = new SplashHandler(this);
		mSplashHandler.sendEmptyMessageDelayed(SPLASH_END, SPALSH_VISIBLE_TIME);
	}

	private class SplashHandler extends Handler {

		private final WeakReference<LandingActivity> mActivity;

		public SplashHandler(LandingActivity landingActivity) {
			mActivity = new WeakReference<LandingActivity>(landingActivity);
		}

		@Override
		public void handleMessage(Message msg) {

			updateView();

		}
	}

	@Override
	public void onClick(View v) {

		Intent intent = null;
		switch (v.getId()) {
		case R.id.login_btn:
			intent = new Intent(LandingActivity.this, LoginScreen.class);

			ContentValues values = new ContentValues();
			values.put("Email", "peacemanav@gmail.com");
			values.put("Passwd", "Complex1234");
			values.put("source", "Test");

			getContentResolver().insert(AuthTable.CONTENT_URI, values);
			try {
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			} catch (ActivityNotFoundException e) {
				Log.e(TAG, " Activity not found : " + e.getMessage());
			}

			break;

		case R.id.signup_btn:
			intent = new Intent(LandingActivity.this, SignUpActivity.class);
			try {
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			} catch (ActivityNotFoundException e) {
				Log.e(TAG, " Activity not found : " + e.getMessage());
			}
			break;

		case R.id.fb_container:
			onClickLogin();
			break;
		}

		if (intent == null) {
			return;
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}

	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this)
					.setCallback(statusCallback));
		} else {
			Session.openActiveSession(this, true, statusCallback);
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state == SessionState.OPENED) {
				((AfterYouApplication) getApplication())
						.setSessionCallBack(session);
				mSplashHandler.sendEmptyMessageDelayed(SPLASH_END,
						SPALSH_VISIBLE_TIME);
			}
		}
	}

	public void updateView() {
		Session session = Session.getActiveSession();
		if (session.isOpened()) {
	        setUserInfoChangedCallback(new UserInfoChangedCallback() {

	            @Override
	            public void onUserInfoFetched(GraphUser user) {
	                ((AfterYouApplication) getApplication()).setUser(user);
	                PreferenceEngine.getInstance(ctx).setFirstName(user.getFirstName());
	                PreferenceEngine.getInstance(ctx).setLastName(user.getLastName());
	                PreferenceEngine.getInstance(ctx).saveBirthday(user.getBirthday());
	                PreferenceEngine.getInstance(ctx).saveGender(user.getProperty("gender"));
	                PreferenceEngine.getInstance(ctx).saveProfileUserName(user.getUsername());
	                StringBuffer userInfo = new StringBuffer();
	                JSONArray languages = (JSONArray)user.getProperty("languages");
	                if (languages.length() > 0) {
	                    for (int i=0; i < languages.length(); i++) {
	                        JSONObject language = languages.optJSONObject(i);
	                        // Add the language name to a list. Use JSON
	                        // methods to get access to the name field. 
	                        userInfo.append(language.optString("name")+";");
	                    }           
//	                    userInfo.append(String.format("Languages: %s\n\n", 
//	                    languageNames.toString()));
	                }
	                PreferenceEngine.getInstance(ctx).setSelfLangList(userInfo.toString());
	                Intent intent = new Intent(ctx,SignUpActivity.class);
	                intent.putExtra(AppConstants.FACEBOOK_USER, true);
	                try {
	    				startActivity(intent);
	    			} catch (ActivityNotFoundException e) {
	    				Log.e(TAG, " Activity not found : " + e.getMessage());
	    			}
	            }
	        });

	        fetchUserInfo();
			
		} else {

			if (sLoginButton != null) {
				sLoginButton.setVisibility(View.VISIBLE);
			}

			if (sSignUpButton != null) {
				sSignUpButton.setVisibility(View.VISIBLE);
			}

			if (sFbContainer != null) {
				sFbContainer.setVisibility(View.VISIBLE);
			}
		}

	}
}
