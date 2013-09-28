package com.beacon.afterui.views;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomerDatePickDialog;
import com.beacon.afterui.utils.customviews.ErrorDialog;

public class SignUpActivity extends Activity implements OnClickListener,
        OnFocusChangeListener {

    /** TAG */
    private static final String TAG = SignUpActivity.class.getSimpleName();

    private ImageButton mSignInBtn;
    private ImageView mBirthDayTextImg;
    private ImageView mPasswordValidationTxt;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        mSignInBtn = (ImageButton) findViewById(R.id.sign_in_btn_signup_screen);
        mCrossFirstNameBtn = (ImageView) findViewById(R.id.cross_btn_firstname);
        mCrossLastNameBtn = (ImageView) findViewById(R.id.cross_btn_lastname);
        mCrossEmailBtn = (ImageView) findViewById(R.id.cross_btn_email);
        mCrossDateOfBirthBtn = (ImageView) findViewById(R.id.cross_btn_birth_day);
        mCrossPasswordBtn = (ImageView) findViewById(R.id.cross_btn_password);
        mCrossConfirmPasswordBtn = (ImageView) findViewById(R.id.cross_btn_confirm_password);

        mFirstnameText = (EditText) findViewById(R.id.first_name_edit_text);
        mLastNameText = (EditText) findViewById(R.id.last_name_edit_text);
        mEmailText = (EditText) findViewById(R.id.email_edit_text);
        mBirthDayText = (EditText) findViewById(R.id.birthday_edit_text);
        mPasswordText = (EditText) findViewById(R.id.password_edit_text);
        mConfirmText = (EditText) findViewById(R.id.confirm_edit_text);

        mBirthDayTextImg = (ImageView) findViewById(R.id.date_of_birth_txt);
        mPasswordValidationTxt = (ImageView) findViewById(R.id.password_validation_txt);

        mLeftImage = (TextView) findViewById(R.id.left_image);
        mRightImage = (TextView) findViewById(R.id.right_image);
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
            if (PreferenceEngine.getInstance(this).getGender()
                    .equalsIgnoreCase("male")) {
                mLeftImage.setBackgroundResource(R.drawable.switch_btn);
                mRightImage.setBackgroundDrawable(null);
            } else {
                mRightImage.setBackgroundResource(R.drawable.switch_btn);
                mLeftImage.setBackgroundDrawable(null);
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

        Intent intent = null;
        switch (v.getId()) {
        case R.id.sign_in_btn_signup_screen:
            intent = new Intent(SignUpActivity.this,
                    ProfileSettingsActivity.class);
            if (isFromFacebook) {
                intent.putExtra(AppConstants.FACEBOOK_USER, true);
            }
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
            mLeftImage.setBackgroundResource(R.drawable.switch_btn);
            mRightImage.setBackgroundDrawable(null);
            break;

        case R.id.right_image:
            mRightImage.setBackgroundResource(R.drawable.switch_btn);
            mLeftImage.setBackgroundDrawable(null);
            break;

        case R.id.birthday_edit_text:
            mBirthDayText.setText("");
            break;
        }

        if (!validateData()) {
            return;
        }

        if (intent == null) {
            return;
        }
        try {
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, " Activity not found : " + e.getMessage());
        }
    }

    private boolean validateData() {
        boolean result = true;

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

        if (!comparePassword(mPasswordText.getText().toString(), mConfirmText.getText().toString())) {
            showErrorDialog(R.string.password_not_match);
            return false;
        }

        return result;
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

        case R.id.birthday_edit_text:
            if (hasFocus) {
                getDatePickDialog().show();
            } else {
                if (mBirthDayText.length() == 0) {
                    mBirthDayTextImg.setVisibility(View.VISIBLE);
                }

            }
            break;

        case R.id.password_edit_text:
            if (hasFocus) {
                mPasswordValidationTxt.setVisibility(View.GONE);
            } else {
                if (mPasswordText.length() == 0) {
                    mPasswordValidationTxt.setVisibility(View.VISIBLE);
                }

            }
            break;
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
            mDateOfBirthTxt = dayOfMonth + "/" + monthOfYear + "/" + year;
            mBirthDayText.setText(mDateOfBirthTxt);
            mBirthDayText.clearFocus();
            mCalendar.set(year, monthOfYear, dayOfMonth);
            String date = Utilities.getDateByCalendar(mCalendar);
            PreferenceEngine.getInstance(getApplicationContext()).saveBirthday(
                    date);
        }
    };

}
