package com.beacon.afterui.views;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.network.NetworkConstants;
import com.beacon.afterui.network.NetworkManager;
import com.beacon.afterui.network.ParsingConstants;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.AnalyticsUtils;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomProgressDialog;
import com.beacon.afterui.utils.customviews.DialogHelper;
import com.beacon.afterui.utils.customviews.ErrorDialog;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.google.analytics.tracking.android.EasyTracker;
import com.beacon.afterui.network.NetworkManager.SignInRequestListener;
import com.beacon.afterui.network.NetworkManager.SignUpRequestListener;

public class LandingActivity extends BaseActivity implements OnClickListener,SignUpRequestListener,SignInRequestListener {

    /** TAG */
    private static final String TAG = LandingActivity.class.getSimpleName();

    private static final int SPLASH_END = 1;
    private static final int START_SPINNER = 2;
    private static final int STOP_SPINNER = 3;
    private static final int FACEBOOK_RECIEVED_SUCCESS = 4;
    private final String HASHMAP_BUNDLE = "UserData";

    private static final int SPALSH_VISIBLE_TIME = 3000;

    private SplashHandler mSplashHandler;

    private Button mFacebookLogin;
    private Button sAfterYouLoginBtn;
    private Button sAfterYouSignupBtn;

    private CustomProgressDialog mWaitProgress;
    private Context ctx;

    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    private ProgressBar mCustomProgress;

    private TextView progrssText;

    private RelativeLayout user_inter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getIntent().getAction() == AppConstants.NOTIFICATION_SENT) {
            PreferenceEngine.getInstance(this).setFromNotification(true);
            PreferenceEngine.getInstance(this).setNotifySenderName(
                    getIntent().getExtras().getString(AppConstants.SENDER));
        } else {
            PreferenceEngine.getInstance(this).setFromNotification(false);
        }
        setContentView(R.layout.landing_screen);
        setBehindLeftContentView(R.layout.landing_screen);
        setBehindRightContentView(R.layout.landing_screen);
        this.ctx = this;
        user_inter = (RelativeLayout) findViewById(R.id.user_but_id);
        sAfterYouLoginBtn = (Button) findViewById(R.id.afteryou_register);
        mFacebookLogin = (Button) findViewById(R.id.facebook_login_btn);
        sAfterYouSignupBtn = (Button) findViewById(R.id.after_you_login_btn);

        sAfterYouLoginBtn.setOnClickListener(this);
        mFacebookLogin.setOnClickListener(this);
        sAfterYouSignupBtn.setOnClickListener(this);

        Typeface typeFace = Typeface.createFromAsset(getAssets(),
                "fonts/ITCAvantGardeStd-BkCn.otf");

        sAfterYouLoginBtn.setTypeface(typeFace);
        mFacebookLogin.setTypeface(typeFace);
        sAfterYouSignupBtn.setTypeface(typeFace);

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
        mSplashHandler.sendEmptyMessage(SPLASH_END);
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
                PreferenceEngine prefEngine = PreferenceEngine
                        .getInstance(LandingActivity.this);
                if (!prefEngine.isUserSignedUp()) {
                    updateView();
                } else {
                	
                	if(prefEngine.isUserFromFacebook())
                	{
                	        prefEngine.setUsername(prefEngine.getUserEmail());
                	}
                    // Already signed up, go to main activity.
                    Intent i = new Intent(LandingActivity.this,
                            MainActivity.class);
                    startActivity(i);
                }
                break;
                
            case FACEBOOK_RECIEVED_SUCCESS:
            	
            	signUp((HashMap<String, String>)msg.getData().getSerializable(HASHMAP_BUNDLE));
            	break;
            case START_SPINNER:
                startSpinner(true);
                break;

            case STOP_SPINNER:
                startSpinner(false);
                Intent intent = new Intent(ctx, ProfileSettingsActivity.class);
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

        case R.id.afteryou_register:
        	PreferenceEngine.getInstance(ctx).userFromFacebook(false);
            intent = new Intent(LandingActivity.this, SignUpActivity.class);
            AnalyticsUtils.logButtonPressEvent(this, "signup button", -1);
            break;

        case R.id.after_you_login_btn:
            // ContentValues values = new ContentValues();
            // values.put("Email", "peacemanav@gmail.com");
            // values.put("Passwd", "Complex1234");
            // values.put("source", "Test");
            //
            // getContentResolver().insert(AuthTable.CONTENT_URI, values);
        	 PreferenceEngine.getInstance(ctx).userFromFacebook(false);
            intent = new Intent(LandingActivity.this, LoginScreen.class);
            AnalyticsUtils.logButtonPressEvent(this, "login button", -1);
            break;

        // case R.id.signup_btn:
        // intent = new Intent(LandingActivity.this, SignUpActivity.class);
        // AnalyticsUtils.logButtonPressEvent(this, "sign up button", -1);
        // break;

        case R.id.facebook_login_btn:
            if (PreferenceEngine.getInstance(ctx).isFTT()) {
                onClickLogin();
            }
            PreferenceEngine.getInstance(ctx).setFTT(false);
            AnalyticsUtils.logButtonPressEvent(this, "fb button", -1);
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

    public void signUp(HashMap<String, String> serializableMap) {
    	 NetworkManager.signUp( serializableMap, this, new Handler());
		
	}

	public void startSpinner(boolean start) {
        if (start) {
        	showProgressDialog();
        } else {
            if (progrssText != null && mCustomProgress != null) {
                progrssText.setVisibility(View.GONE);
                mCustomProgress.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);

        EasyTracker easyTracker = EasyTracker.getInstance(this);
        easyTracker.activityStart(this);
        AnalyticsUtils.logScreenEvent(this, "Splash Screen");
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
        EasyTracker.getInstance(this).activityStop(this);
    }

    private void onClickLogin() {
        mSplashHandler.sendEmptyMessageDelayed(START_SPINNER,
                SPALSH_VISIBLE_TIME);
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setPermissions(Arrays.asList("basic_info","user_birthday"))
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
                    	HashMap<String, String> data = new HashMap<String, String>();
                        if (user.getFirstName() != null) {
                            PreferenceEngine.getInstance(ctx).setFirstName(
                                    user.getFirstName());
                            data.put("fname", user.getFirstName());
                        }
                        if (user.getLastName() != null) {
                            PreferenceEngine.getInstance(ctx).setLastName(
                                    user.getLastName());
                            data.put("lname", user.getLastName());
                        }
                        if (user.getBirthday() != null) {
                            PreferenceEngine.getInstance(ctx).saveBirthday(
                                    user.getBirthday());
                            data.put("dob", user.getBirthday());
                        }
                        
                        if(user.getProperty("email") != null)
                        {
                        	PreferenceEngine.getInstance(ctx).saveUserEmail(
                                user.getProperty("email"));
                        }
                        else{
                        	PreferenceEngine.getInstance(ctx).saveUserEmail(
                                    user.getUsername()+"@facebook.com");
                        	data.put("email", user.getUsername()+"@facebook.com");
                        }
                        PreferenceEngine.getInstance(ctx).saveGender(
                                user.getProperty("gender"));
                        data.put("gender", user.getProperty("gender")+"");
                        
                        PreferenceEngine.getInstance(ctx).saveProfileUserName(
                                user.getUsername());
                        StringBuffer userInfo = new StringBuffer();
                        JSONArray languages = (JSONArray) user
                                .getProperty("languages");
                        if (languages != null && languages.length() > 0) {
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
                        // if (userInfo.length() > 0) {
                        PreferenceEngine.getInstance(ctx).setSelfLangList(
                                userInfo.toString());
                        
                        
                        data.put("password", Session.getActiveSession().getAccessToken());
                        data.put("isFB","true");
                        Bundle messageData = new Bundle();
                        messageData.putSerializable(HASHMAP_BUNDLE, data);
                        Message msg = new Message();
                        msg.what = FACEBOOK_RECIEVED_SUCCESS;
                        msg.setData(messageData);
                        mSplashHandler.sendMessage(msg);
                        // }
                    }

                }
            });

            mSplashHandler.sendEmptyMessage(START_SPINNER);
            fetchUserInfo();

        } else {

            if (user_inter != null) {
                user_inter.setVisibility(View.VISIBLE);
            }

        }

    }

    private void showProgressDialog() {
        mWaitProgress = DialogHelper.createProgessDialog(this, null);
        mWaitProgress.setMessage(this.getString(R.string.progress_sign_up));
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

    
    @Override
    public void onSignUp(JSONObject json) {

        Log.d(TAG, "onSignUp : ---> " + json);
        if (json == null) {
            // show some error and return.
            showErrorDialog(R.string.err_sign_up);
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

        Toast.makeText(this, "Signed Up", Toast.LENGTH_SHORT).show();
        PreferenceEngine prefEngine = PreferenceEngine
                .getInstance(LandingActivity.this);
        prefEngine.userFromFacebook(true);
        prefEngine.setUserSignedUpStatus(true);
        Intent intent = new Intent(LandingActivity.this,
                ProfileSettingsActivity.class);

        intent.putExtra(AppConstants.FACEBOOK_USER, true);
        

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, " Activity not found : " + e.getMessage());
        }
    }

    @Override
    public void onFailure(int errorCode) {
        
        if(errorCode == NetworkConstants.SignUpRequestConstants.EMAIL_ALREADY_TAKEN)
        {
        	//TODO: Create a way for password for facebook.
        	Map<String, String> data = new HashMap<String, String>();
            data.put(ParsingConstants.EMAIL, PreferenceEngine.getInstance(ctx).getUserEmail());
            data.put(ParsingConstants.PASSWORD, Session.getActiveSession().getAccessToken());
            NetworkManager.signIn(data, this, new Handler());
        }
        else{
        	removeDialog();
        	Log.e(TAG, "onFailure() : " + errorCode);
        	showErrorDialog(R.string.err_sign_up);
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
                .getInstance(LandingActivity.this);
        prefEngine.userFromFacebook(true);
        prefEngine.setUsername(prefEngine.getUserEmail());
        prefEngine.setUserSignedUpStatus(true);
        Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LandingActivity.this, CapturePictureActivity.class);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, " Activity not found : " + e.getMessage());
        }
		
	}
}
