package com.beacon.afterui.views;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.network.NetworkManager;
import com.beacon.afterui.network.ParsingConstants;
import com.beacon.afterui.network.NetworkManager.SignUpRequestListener;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomProgressDialog;
import com.beacon.afterui.utils.customviews.CustomerDatePickDialog;
import com.beacon.afterui.utils.customviews.DialogHelper;
import com.beacon.afterui.utils.customviews.ErrorDialog;

public class SignUpActivity extends BaseActivity implements OnClickListener,
        OnFocusChangeListener, SignUpRequestListener {

    /** TAG */
    private static final String TAG = SignUpActivity.class.getSimpleName();

    private TextView mSignInBtn;
    private ImageView mCrossFirstNameBtn;
    private ImageView mCrossLastNameBtn;
    private ImageView mCrossEmailBtn;
    private ImageView mCrossDateOfBirthBtn;
    private ImageView mCrossPasswordBtn;
    private ImageView mCrossConfirmPasswordBtn;

    private EditText mFirstnameText;
    private EditText mLastNameText;
    private EditText mEmailText;
    private static EditText mBirthDayText;
    private EditText mPasswordText;
    private EditText mConfirmText;
    private Calendar mCalendar;

    private static String mDateOfBirthTxt;

    private static final int AUTO_SIGN_UP_END = 1;

    private static final int AUTO_SIGN_UP_VISIBLE_TIME = 3000;

    private TextView mLeftImage;

    private TextView mRightImage;

    private SignUpHandler mSplashHandler;

    private boolean isFromFacebook;

    private static final boolean isTest = false;

    private static final int MALE = 1;
    private static final int FEMALE = 2;

    private int mSelectedGender = MALE;

    private CustomProgressDialog mWaitProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up_screen);
        setBehindLeftContentView(R.layout.sign_up_screen);
        setBehindRightContentView(R.layout.sign_up_screen);
        Typeface typeFaceRegular = Typeface.createFromAsset(getAssets(),
                "fonts/ITCAvantGardeStd-Md.otf");

        mSignInBtn = (TextView) findViewById(R.id.sign_in_btn_signup_screen);
        mCrossFirstNameBtn = (ImageView) findViewById(R.id.cross_btn_firstname);
        mCrossLastNameBtn = (ImageView) findViewById(R.id.cross_btn_lastname);
        mCrossEmailBtn = (ImageView) findViewById(R.id.cross_btn_email);
        mCrossDateOfBirthBtn = (ImageView) findViewById(R.id.cross_btn_birth_day);
        mCrossPasswordBtn = (ImageView) findViewById(R.id.cross_btn_password);
        mCrossConfirmPasswordBtn = (ImageView) findViewById(R.id.cross_btn_confirm_password);

        mFirstnameText = (EditText) findViewById(R.id.first_name_edit_text);
        mFirstnameText.setTypeface(typeFaceRegular);
        mLastNameText = (EditText) findViewById(R.id.last_name_edit_text);
        mLastNameText.setTypeface(typeFaceRegular);
        mEmailText = (EditText) findViewById(R.id.email_edit_text);
        mEmailText.setTypeface(typeFaceRegular);
        mBirthDayText = (EditText) findViewById(R.id.birthday_edit_text);
        mBirthDayText.setTypeface(typeFaceRegular);
        mPasswordText = (EditText) findViewById(R.id.password_edit_text);
        mPasswordText.setTypeface(typeFaceRegular);
        mConfirmText = (EditText) findViewById(R.id.confirm_edit_text);
        mConfirmText.setTypeface(typeFaceRegular);

        mLeftImage = (TextView) findViewById(R.id.left_image);
        mRightImage = (TextView) findViewById(R.id.right_image);

        mLeftImage.setTypeface(typeFaceRegular);
        mRightImage.setTypeface(typeFaceRegular);
        mSignInBtn.setTypeface(typeFaceRegular);

        if (isTest) {
            mFirstnameText.setText("abc");
            mLastNameText.setText("xyz");
            mEmailText.setText("peace_manav@gmail.in");
            mPasswordText.setText("peacea");
            mConfirmText.setText("peacea");
            mBirthDayText.setText("1234");
        }
        mLeftImage.setOnClickListener(this);
        mRightImage.setOnClickListener(this);

        mCrossFirstNameBtn.setOnClickListener(this);
        mCrossLastNameBtn.setOnClickListener(this);
        mCrossEmailBtn.setOnClickListener(this);
        mCrossDateOfBirthBtn.setOnClickListener(this);
        mCrossPasswordBtn.setOnClickListener(this);
        mCrossConfirmPasswordBtn.setOnClickListener(this);
        mSignInBtn.setOnClickListener(this);

        mBirthDayText.setOnFocusChangeListener(this);
        mPasswordText.setOnFocusChangeListener(this);
        mCalendar = Calendar.getInstance();

        if (getIntent().hasExtra(AppConstants.FACEBOOK_USER)) {
            isFromFacebook = true;
            mFirstnameText.setText(PreferenceEngine.getInstance(this)
                    .getFirstName());
            mLastNameText.setText(PreferenceEngine.getInstance(this)
                    .getLastName());
            mEmailText.setText(PreferenceEngine.getInstance(this)
                    .getUserEmail());
            if (PreferenceEngine.getInstance(this).getGender()
                    .equalsIgnoreCase("male")) {
                mLeftImage.setBackgroundResource(R.color.white);
                mRightImage.setBackgroundResource(R.drawable.switch_btn_normal);
            } else {
                mRightImage.setBackgroundResource(R.color.white);
                mLeftImage.setBackgroundResource(R.drawable.switch_btn_normal);
            }
            mEmailText.setVisibility(View.GONE);
            mBirthDayText.setText(PreferenceEngine.getInstance(this)
                    .getBirthday());
            mPasswordText.setVisibility(View.GONE);
            mConfirmText.setVisibility(View.GONE);
            mCrossEmailBtn.setVisibility(View.GONE);
            mCrossPasswordBtn.setVisibility(View.GONE);
            mCrossConfirmPasswordBtn.setVisibility(View.GONE);
            mSplashHandler = new SignUpHandler(this);
            mSplashHandler.sendEmptyMessageDelayed(AUTO_SIGN_UP_END,
                    AUTO_SIGN_UP_VISIBLE_TIME);
        }
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private class SignUpHandler extends Handler {

        private final WeakReference<SignUpActivity> mActivity;

        public SignUpHandler(SignUpActivity signUpActivity) {
            mActivity = new WeakReference<SignUpActivity>(signUpActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            mSignInBtn.performClick();
        }
    }

    private static final boolean comparePassword(String password,
            String confirmPassword) {

        if (password.equals(confirmPassword)) {
            return true;
        }

        return false;
    }

    private static final boolean checkPasswordLength(final String password) {
        final String PASSWORD_PATTERN = "(?=.{6,10})";
        final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        final Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.sign_in_btn_signup_screen:
            if (!validateData()) {
                return;
            }

            sendSignUpRequest();
            break;

        case R.id.cross_btn_firstname:
            mFirstnameText.setText("");
            break;

        case R.id.cross_btn_lastname:
            mLastNameText.setText("");
            break;

        case R.id.cross_btn_email:
            mEmailText.setText("");
            break;

        case R.id.cross_btn_birth_day:
            mBirthDayText.setText("");
            break;

        case R.id.cross_btn_password:
            mPasswordText.setText("");
            break;

        case R.id.cross_btn_confirm_password:
            mConfirmText.setText("");
            break;

        case R.id.left_image:
            mLeftImage.setBackgroundResource(R.drawable.switch_btn_pressed);
            mRightImage.setBackgroundResource(R.drawable.switch_btn_normal);
            mSelectedGender = MALE;
            break;

        case R.id.right_image:
            mRightImage.setBackgroundResource(R.drawable.switch_btn_pressed);
            mLeftImage.setBackgroundResource(R.drawable.switch_btn_normal);
            mSelectedGender = FEMALE;
            break;

        case R.id.birthday_edit_text:
            getDatePickDialog().show();
            mBirthDayText.clearFocus();
            break;

        }
    }

    private void sendSignUpRequest() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("fname", mFirstnameText.getText().toString());
        data.put("lname", mLastNameText.getText().toString());
        data.put("email", mEmailText.getText().toString());
        data.put("dob", mBirthDayText.getText().toString());
        data.put("password", mConfirmText.getText().toString());
        data.put("gender", String.valueOf(mSelectedGender));

        showProgressDialog();
        NetworkManager.signUp(data, this, new Handler());
        saveData();
    }

    private void saveData() {
        PreferenceEngine prefEngine = PreferenceEngine
                .getInstance(SignUpActivity.this);

        prefEngine.setFirstName(mFirstnameText.getText().toString());
        prefEngine.setLastName(mLastNameText.getText().toString());

        prefEngine.setUsername(mEmailText.getText().toString());
        prefEngine.setPassword(mPasswordText.getText().toString());
    }

    private boolean validateData() {

        if (isFromFacebook) {
            return true;
        }

        // check first name.
        if (mFirstnameText.getText().length() <= 0) {
            showErrorDialog(R.string.first_name_empty);
            return false;
        }

        // check last name.
        if (mLastNameText.getText().length() <= 0) {
            showErrorDialog(R.string.last_name_empty);
            return false;
        }

        // check last name.
        if (mEmailText.getText().length() <= 0) {
            showErrorDialog(R.string.email_empty);
            return false;
        }

        // email address validity.
        if (!Utilities.isValidEmail(mEmailText.getText())) {
            showErrorDialog(R.string.invalid_email);
            return false;
        }

        if (mBirthDayText.getText().length() <= 0) {
            showErrorDialog(R.string.birth_date_empty);
            return false;
        }

        if (mPasswordText.getText().length() <= 0) {
            showErrorDialog(R.string.password_empty);
            return false;
        }

        if (mPasswordText.getText().length() < 6) {
            showErrorDialog(R.string.password_length);
            return false;
        }

        if (mConfirmText.getText().length() <= 0) {
            showErrorDialog(R.string.confirm_password_empty);
            return false;
        }

        if (!comparePassword(mPasswordText.getText().toString(), mConfirmText
                .getText().toString())) {
            showErrorDialog(R.string.password_not_match);
            return false;
        }

        return true;
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
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {

        case R.id.first_name_edit_text:
            if (hasFocus) {
                mFirstnameText.setSelection(0);
                mFirstnameText.setHint("");
                mFirstnameText.setCursorVisible(true);
            }
            break;

        case R.id.birthday_edit_text:
            if (hasFocus) {
                getDatePickDialog().show();
                mBirthDayText.clearFocus();
            }
            break;
        //
        // case R.id.last_name_edit_text:
        // if (hasFocus) {
        // mLastNameText.setSelection(0);
        // mLastNameText.setHint("");
        // mLastNameText.setCursorVisible(true);
        // }
        // break;
        //
        // case R.id.email_edit_text:
        // if (hasFocus) {
        // mEmailText.setSelection(0);
        // mEmailText.setHint("");
        // mEmailText.setCursorVisible(true);
        // }
        //
        // break;
        //
        // case R.id.confirm_edit_text:
        // if (hasFocus) {
        // mConfirmText.setSelection(0);
        // mConfirmText.setHint("");
        // mConfirmText.setCursorVisible(true);
        // }
        //
        // break;

        }
    }

    private Dialog getDatePickDialog() {

        return new CustomerDatePickDialog(new AfterYouDialogImpl(this), this,
                R.style.Theme_CustomDialog, mDateSetListener,
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DATE));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int userAge = currentYear - year;
            if (userAge >= 16) {
                mDateOfBirthTxt = monthOfYear + " / " + dayOfMonth + " / "
                        + year;
                mBirthDayText.setText(mDateOfBirthTxt);
                // mBirthDayText.clearFocus();
                // mCalendar.set(year, monthOfYear, dayOfMonth);
                String date = Utilities.getDateByCalendar(mCalendar);
                PreferenceEngine.getInstance(getApplicationContext())
                        .saveBirthday(date);
            } else {
                showErrorDialog(R.string.date_of_less_than_sixteen);
                mBirthDayText.clearFocus();
            }
        }
    };

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

    @Override
    public void onSignUp(JSONObject json) {

        removeDialog();
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

//        saveData();
        Intent intent = new Intent(SignUpActivity.this,
                ProfileSettingsActivity.class);
        if (isFromFacebook) {
            intent.putExtra(AppConstants.FACEBOOK_USER, true);
        }

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, " Activity not found : " + e.getMessage());
        }
    }

    @Override
    public void onFailure(int errorCode) {
        removeDialog();
        Log.e(TAG, "onFailure() : " + errorCode);
        showErrorDialog(R.string.err_sign_up);
    }
}
