package com.beacon.afterui.views;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.beacon.afterui.R;

public class SignUpActivity extends Activity implements OnClickListener,
        OnFocusChangeListener {

    private ImageView mSignInBtn;
    private ImageView mBirthDayTextImg;
    private ImageView mPasswordValidationTxt;
    private ImageView mCrossFirstNameBtn;
    private ImageView mCrossLastNameBtn;
    private ImageView mCrossEmailBtn;
    private ImageView mCrossDateOfBirthBtn;
    private ImageView mCrossPasswordBtn;
    private ImageView mCrossConfirmPasswordBtn;
    private int mLength;

    private EditText mFirstnameText;
    private EditText mLastNameText;
    private EditText mEmailText;
    private EditText mBirthDayText;
    private EditText mPasswordText;
    private EditText mConfirmText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        mSignInBtn = (ImageView) findViewById(R.id.sign_in_btn);
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

        mCrossFirstNameBtn.setOnClickListener(this);
        mCrossLastNameBtn.setOnClickListener(this);
        mCrossEmailBtn.setOnClickListener(this);
        mCrossDateOfBirthBtn.setOnClickListener(this);
        mCrossPasswordBtn.setOnClickListener(this);
        mCrossConfirmPasswordBtn.setOnClickListener(this);

//        mBirthDayText.setOnFocusChangeListener(this);
//        mPasswordText.setOnFocusChangeListener(this);

    }

    private static final boolean comparePassword(String password,
            String confirmPassword) {
        boolean result = false;

        if (checkPasswordLength(password)) {

            if (password.equals(confirmPassword)) {

                result = true;
                Log.d("Password", "equal");
                Log.d("" + password, "" + confirmPassword);

            } else {
                result = false;
                Log.d("Password", "Not equal");
                Log.d("password", "" + confirmPassword + "" + password);
            }

        } else {
            result = false;
        }

        return result;
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
        case R.id.sign_in_btn:
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
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        final EditText editText = (EditText) v;
        if (hasFocus) {
            editText.setVisibility(View.GONE);
        } else {
            if (mBirthDayText.length() == 0) {
                editText.setVisibility(View.VISIBLE);
            }

        }
    }
}
