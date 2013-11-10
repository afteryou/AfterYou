package com.beacon.afterui.views;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.AnalyticsUtils;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.ErrorDialog;
import com.google.analytics.tracking.android.EasyTracker;

public class LoginScreen extends BaseActivity implements OnClickListener,
        OnFocusChangeListener {

    /** TAG */
    private static final String TAG = LoginScreen.class.getSimpleName();

    private ImageButton mLoginButton;

    private ImageView mCrossEmailBtn;

    private ImageView mCrossPasswordBtn;

    private EditText mEmailText;
    private EditText mPasswordText;

    private static final boolean isTest = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getAction() == AppConstants.NOTIFICATION_SENT)
        {
        	PreferenceEngine.getInstance(this).setFromNotification(true);
        	PreferenceEngine.getInstance(this).setNotifySenderName(getIntent().getExtras().getString(AppConstants.SENDER));
        }
        else{
        	PreferenceEngine.getInstance(this).setFromNotification(false);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_screen);
        setBehindLeftContentView(R.layout.login_screen);
        setBehindRightContentView(R.layout.login_screen);
        mLoginButton = (ImageButton) findViewById(R.id.sign_in_btn_login_screen);
        mCrossEmailBtn = (ImageView) findViewById(R.id.cross_btn_email_login_screen);
        mCrossPasswordBtn = (ImageView) findViewById(R.id.cross_btn_password_login_screen);
        mEmailText = (EditText) findViewById(R.id.email_text);
        mPasswordText = (EditText) findViewById(R.id.password_text);

        Typeface typeFace = Typeface.createFromAsset(getAssets(),
                "fonts/MyriadPro-Regular.otf");

        TextView sign_up_fb = (TextView) findViewById(R.id.fb_sign_up_txt);
        TextView sign_in = (TextView) findViewById(R.id.sign_in_txt_login);

        sign_up_fb.setTypeface(typeFace);
        sign_in.setTypeface(typeFace);
        mEmailText.setTypeface(typeFace);
        mPasswordText.setTypeface(typeFace);

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

        switch (v.getId()) {
        case R.id.sign_in_btn_login_screen:
            handleSignInButton();
            break;

        case R.id.cross_btn_email_login_screen:
            mEmailText.setText("");
            break;

        case R.id.cross_btn_password_login_screen:
            mPasswordText.setText("");
            break;

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

        PreferenceEngine prefEngine = PreferenceEngine
                .getInstance(LoginScreen.this);

        prefEngine.setUsername(mEmailText.getText().toString());
        prefEngine.setPassword(mPasswordText.getText().toString());

        AnalyticsUtils.logButtonPressEvent(this, "login-"
                + mEmailText.getText().toString(), -1);

        Intent intent = new Intent(LoginScreen.this,
                CapturePictureActivity.class);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, " Activity not found : " + e.getMessage());
        }
    }

    private void showErrorDialog(int stringResId) {
        ErrorDialog errDialog = new ErrorDialog(new AfterYouDialogImpl(this),
                this, R.style.Theme_CustomDialog,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, getResources().getString(stringResId));
        errDialog.show();
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
}
