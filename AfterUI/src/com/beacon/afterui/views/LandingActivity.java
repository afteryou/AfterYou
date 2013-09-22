package com.beacon.afterui.views;

import java.lang.ref.WeakReference;

import android.content.ActivityNotFoundException;
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
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

public class LandingActivity extends BaseActivity implements OnClickListener {

    /** TAG */
    private static final String TAG = LandingActivity.class.getSimpleName();

    private static ImageView sLoginButton;
    private static ImageView sSignUpButton;

    private static final int SPLASH_END = 1;

    private static final int SPALSH_VISIBLE_TIME = 3000;

    private SplashHandler mSplashHandler;

    private static View sFbContainer;

    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_screen);

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
            intent = new Intent(LandingActivity.this, ProfileSettingsActivity.class);
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
			((AfterYouApplication)getApplication()).setSessionCallBack(session);
            mSplashHandler.sendEmptyMessageDelayed(SPLASH_END,
                    SPALSH_VISIBLE_TIME);
        }
    }

    public void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            Intent intent = new Intent(this, CapturePictureActivity.class);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, " Activity not found : " + e.getMessage());
            }
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
