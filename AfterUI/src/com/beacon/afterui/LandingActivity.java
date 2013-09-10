package com.beacon.afterui;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class LandingActivity extends Activity implements OnClickListener {

    /** TAG */
    private static final String TAG = LandingActivity.class.getSimpleName();

    private static ImageView sLoginButton;
    private static ImageView sSignUpButton;

    private static final int SPLASH_END = 1;

    private static final int SPALSH_VISIBLE_TIME = 3000;

    private SplashHandler mSplashHandler;

    private static View sFbContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_screen);

        sLoginButton = (ImageView) findViewById(R.id.login_btn);
        sSignUpButton = (ImageView) findViewById(R.id.signup_btn);
        sFbContainer = findViewById(R.id.fb_container);

        sLoginButton.setOnClickListener(this);
        sSignUpButton.setOnClickListener(this);

        mSplashHandler = new SplashHandler(this);
        mSplashHandler.sendEmptyMessageDelayed(SPLASH_END, SPALSH_VISIBLE_TIME);
    }

    private static final class SplashHandler extends Handler {

        private final WeakReference<LandingActivity> mActivity;

        public SplashHandler(LandingActivity landingActivity) {
            mActivity = new WeakReference<LandingActivity>(landingActivity);
        }

        @Override
        public void handleMessage(Message msg) {
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

    @Override
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()) {
        case R.id.login_btn:
            intent = new Intent(LandingActivity.this, LoginScreen.class);
            break;

        case R.id.signup_btn:
            intent = new Intent(LandingActivity.this, SignUpActivity.class);
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

}