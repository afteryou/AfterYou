package com.beacon.afterui.views;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.network.NetworkManager;
import com.beacon.afterui.network.ParsingConstants;
import com.beacon.afterui.network.NetworkManager.SignInRequestListener;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.AnalyticsUtils;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomProgressDialog;
import com.beacon.afterui.utils.customviews.DialogHelper;
import com.beacon.afterui.utils.customviews.ErrorDialog;
import com.google.analytics.tracking.android.EasyTracker;

public class LoginScreen extends BaseActivity implements OnClickListener,
		OnFocusChangeListener, SignInRequestListener {

	/** TAG */
	private static final String TAG = LoginScreen.class.getSimpleName();

	private TextView mLoginButton;

	private ImageView mCrossEmailBtn;

	private ImageView mCrossPasswordBtn;

	private EditText mEmailText;
	private EditText mPasswordText;

	private CustomProgressDialog mWaitProgress;

	private static final boolean isTest = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getAction() == AppConstants.NOTIFICATION_SENT) {
			PreferenceEngine.getInstance(this).setFromNotification(true);
			PreferenceEngine.getInstance(this).setNotifySenderName(
					getIntent().getExtras().getString(AppConstants.SENDER));
		} else {
			PreferenceEngine.getInstance(this).setFromNotification(false);
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_screen);
		setBehindLeftContentView(R.layout.login_screen);
		setBehindRightContentView(R.layout.login_screen);

		mLoginButton = (TextView) findViewById(R.id.sign_in_btn_login_screen);
		mCrossEmailBtn = (ImageView) findViewById(R.id.cross_btn_email_login_screen);
		mCrossPasswordBtn = (ImageView) findViewById(R.id.cross_btn_password_login_screen);
		mEmailText = (EditText) findViewById(R.id.email_text);
		mPasswordText = (EditText) findViewById(R.id.password_text);

		Typeface typeFace = FontUtils.loadTypeFace(getApplicationContext(),
				FontUtils.ITC_AVANT_GARDE_STD_BK);

		mEmailText.setTypeface(typeFace);
		mPasswordText.setTypeface(typeFace);
		mLoginButton.setTypeface(typeFace);

		if (isTest) {
			// TODO remove this, it is for testing
			mEmailText.setText("peace_manav@def.com");
			mPasswordText.setText("peace");
		}

		mCrossEmailBtn.setOnClickListener(this);
		mCrossPasswordBtn.setOnClickListener(this);
		mLoginButton.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		EasyTracker easyTracker = EasyTracker.getInstance(this);
		easyTracker.activityStart(this);

		AnalyticsUtils.logScreenEvent(this, "Login Screen");
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	public void onClick(View v) {

		Intent intent = null;

		switch (v.getId()) {
		case R.id.sign_in_btn_login_screen:
			handleSignInButton();
			AnalyticsUtils.logButtonPressEvent(this, "sign in button", -1);
			break;

		case R.id.cross_btn_email_login_screen:
			mEmailText.setText("");
			break;

		case R.id.cross_btn_password_login_screen:
			mPasswordText.setText("");
			break;

		}
		if (intent == null) {
			return;
		}
	}

	private void handleSignInButton() {

		// check user name not NULL.
		if (mEmailText.getText().length() <= 0
				|| mPasswordText.getText().length() <= 0) {

			// Show dialog and return
			showErrorDialog(R.string.username_password_empty_desc);
			return;
		}

		if (!Utilities.isValidEmail(mEmailText.getText())) {
			// Show dialog and return
			showErrorDialog(R.string.invalid_email);
			return;
		}

		AnalyticsUtils.logButtonPressEvent(this, "login-"
				+ mEmailText.getText().toString(), -1);

		Map<String, String> data = new HashMap<String, String>();
		data.put(ParsingConstants.EMAIL, mEmailText.getText().toString());
		data.put(ParsingConstants.PASSWORD, mPasswordText.getText().toString());
		showProgressDialog();
		NetworkManager.signIn(data, this, new Handler());
	}

	private void showErrorDialog(int stringResId) {
		showErrorDialog(getResources().getString(stringResId));
	}

	private void showErrorDialog(final String message) {
		ErrorDialog errDialog = new ErrorDialog(new AfterYouDialogImpl(this),
				this, R.style.Theme_CustomDialog,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}, message);
		errDialog.show();
	}

	private void showProgressDialog() {
		mWaitProgress = DialogHelper.createProgessDialog(this, null);
		mWaitProgress.setMessage(this.getString(R.string.progress_sign_in));
		mWaitProgress
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
					}
				});
		mWaitProgress.show();
	}

	protected void removeDialog() {

		if (null != this && null != mWaitProgress && mWaitProgress.isShowing()) {
			mWaitProgress.dismiss();
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {

		case R.id.email_text:
			if (hasFocus) {
				mEmailText.setSelection(0);
				mEmailText.setHint("");
				mEmailText.setCursorVisible(true);
			}
			break;
		case R.id.password_text:
			if (hasFocus) {
				mPasswordText.setSelection(0);
				mPasswordText.setHint("");
				mPasswordText.setCursorVisible(true);
			}
			break;
		}
	}

	@Override
	public void onSignIn(JSONObject json) {

		removeDialog();
		if (json == null) {
			// show error and return.
			showErrorDialog(R.string.err_sign_in);
			return;
		}

		if (json.has(ParsingConstants.ERROR)) {
			// show error and return.
			try {
				showErrorDialog(json.getString(ParsingConstants.ERROR));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return;
		}

		PreferenceEngine prefEngine = PreferenceEngine
				.getInstance(LoginScreen.this);

		prefEngine.setUsername(mEmailText.getText().toString());
		prefEngine.setPassword(mPasswordText.getText().toString());
		prefEngine.setUserSignedUpStatus(true);
        try{
        	prefEngine.setUserID(json.getString(ParsingConstants.ID));
        }
        catch(JSONException ex)
        {
        	
        }
		Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(LoginScreen.this,
				CapturePictureActivity.class);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Log.e(TAG, " Activity not found : " + e.getMessage());
		}
	}

	@Override
	public void onFailure(int errorCode) {
		removeDialog();
		showErrorDialog(R.string.err_sign_in);
	}
}
