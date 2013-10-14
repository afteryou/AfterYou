package com.beacon.afterui.views;

import java.lang.ref.WeakReference;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.constants.AppConstants;
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
	private static final int START_SPINNER = 2;
	private static final int STOP_SPINNER = 3;

	private static final int SPALSH_VISIBLE_TIME = 3000;

	private SplashHandler mSplashHandler;

	private static View sFbContainer;

	private Context ctx;

	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	private ProgressBar mCustomProgress;

	private TextView progrssText;

	private RelativeLayout user_inter;

	private TextView userwarn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.landing_screen);
		setBehindLeftContentView(R.layout.landing_screen);
		setBehindRightContentView(R.layout.landing_screen);
		this.ctx = this;

		user_inter = (RelativeLayout) findViewById(R.id.user_but_id);
		sLoginButton = (ImageView) findViewById(R.id.login_btn);
		sSignUpButton = (ImageView) findViewById(R.id.signup_btn);
		sFbContainer = findViewById(R.id.fb_container);
		userwarn = (TextView) findViewById(R.id.user_fb_warn);

		sLoginButton.setOnClickListener(this);
		sSignUpButton.setOnClickListener(this);
		sFbContainer.setOnClickListener(this);

		TextView copyRightTextView = (TextView) findViewById(R.id.copy_right_text);
		TextView loginTxt = (TextView) findViewById(R.id.login_txt);
		TextView signupTxt = (TextView) findViewById(R.id.signup_txt);
		TextView signupFbTxt = (TextView) findViewById(R.id.sign_up_using_fb_txt);
		TextView userFbWarn = (TextView) findViewById(R.id.user_fb_warn);

		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/MyriadPro-Regular.otf");

		copyRightTextView.setTypeface(typeFace);
		loginTxt.setTypeface(typeFace);
		signupTxt.setTypeface(typeFace);
		signupFbTxt.setTypeface(typeFace);
		userFbWarn.setTypeface(typeFace);

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

			switch (msg.what) {
			case SPLASH_END:
				updateView();
				break;
			case START_SPINNER:
				startSpinner(true);
				break;

			case STOP_SPINNER:
				startSpinner(false);
				Intent intent = new Intent(ctx, SignUpActivity.class);
				intent.putExtra(AppConstants.FACEBOOK_USER, true);
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					Log.e(TAG, " Activity not found : " + e.getMessage());
				}
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {

		Intent intent = null;
		switch (v.getId()) {
		case R.id.login_btn:
			// ContentValues values = new ContentValues();
			// values.put("Email", "peacemanav@gmail.com");
			// values.put("Passwd", "Complex1234");
			// values.put("source", "Test");
			//
			// getContentResolver().insert(AuthTable.CONTENT_URI, values);
			intent = new Intent(LandingActivity.this, LoginScreen.class);
			break;

		case R.id.signup_btn:
			intent = new Intent(LandingActivity.this, SignUpActivity.class);
			break;

		case R.id.fb_container:
			onClickLogin();
			break;
		}

		if (intent == null) {
			return;
		}

		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Log.e(TAG, " Activity not found : " + e.getMessage());
		}
	}

	public void startSpinner(boolean start) {
		if (start) {
			if (mCustomProgress == null) {
				mCustomProgress = (ProgressBar) findViewById(R.id.progress_bar);
				mCustomProgress.setIndeterminate(true);
				mCustomProgress.setIndeterminateDrawable(getResources()
						.getDrawable(R.drawable.progress_spinner));
			}
			if (progrssText == null) {
				progrssText = (TextView) findViewById(R.id.progress_text);
				progrssText.setText(getResources().getString(
						R.string.IDS_AUTHENTICATING));
			}
			progrssText.setVisibility(View.VISIBLE);
			mCustomProgress.setVisibility(View.VISIBLE);
		} else {
			progrssText.setVisibility(View.GONE);
			mCustomProgress.setVisibility(View.GONE);
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
					if (user != null) {
						if (user.getFirstName() != null) {
							PreferenceEngine.getInstance(ctx).setFirstName(
									user.getFirstName());
						}
						if (user.getLastName() != null) {
							PreferenceEngine.getInstance(ctx).setLastName(
									user.getLastName());
						}
						if (user.getBirthday() != null) {
							PreferenceEngine.getInstance(ctx).saveBirthday(
									user.getBirthday());
						}
						PreferenceEngine.getInstance(ctx).saveUserEmail(
								user.getProperty("email"));
						PreferenceEngine.getInstance(ctx).saveGender(
								user.getProperty("gender"));
						PreferenceEngine.getInstance(ctx).saveProfileUserName(
								user.getUsername());
						StringBuffer userInfo = new StringBuffer();
						JSONArray languages = (JSONArray) user
								.getProperty("languages");
						if (languages.length() > 0) {
							for (int i = 0; i < languages.length(); i++) {
								JSONObject language = languages
										.optJSONObject(i);
								// Add the language name to a list. Use JSON
								// methods to get access to the name field.
								userInfo.append(language.optString("name")
										+ ";");
							}
							// userInfo.append(String.format("Languages: %s\n\n",
							// languageNames.toString()));
						}
						if (userInfo.length() > 0) {
							PreferenceEngine.getInstance(ctx).setSelfLangList(
									userInfo.toString());
							mSplashHandler.sendEmptyMessageDelayed(
									STOP_SPINNER, SPALSH_VISIBLE_TIME);
						}
					}

				}
			});

			mSplashHandler.sendEmptyMessageDelayed(START_SPINNER,
					SPALSH_VISIBLE_TIME);
			fetchUserInfo();

		} else {

			if (user_inter != null) {
				user_inter.setVisibility(View.VISIBLE);
			}
			if (userwarn != null) {
				userwarn.setVisibility(View.VISIBLE);
			}

		}

	}
}
