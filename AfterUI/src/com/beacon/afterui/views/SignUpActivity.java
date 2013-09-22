package com.beacon.afterui.views;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomerDatePickDialog;

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
	private int mLength;

	private EditText mFirstnameText;
	private EditText mLastNameText;
	private EditText mEmailText;
	private static EditText mBirthDayText;
	private EditText mPasswordText;
	private EditText mConfirmText;
	private Calendar mCalendar;

	private static String mDateOfBirthTxt;

	private TextView mLeftImage;

	private TextView mRightImage;

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

		Intent intent = null;
		switch (v.getId()) {
		case R.id.sign_in_btn_signup_screen:
			intent = new Intent(SignUpActivity.this,
					ProfileSettingsActivity.class);
			break;

		case R.id.cross_btn_firstname:
			break;

		case R.id.cross_btn_lastname:
			break;

		case R.id.cross_btn_email:
			break;

		case R.id.cross_btn_birth_day:
			mBirthDayText.setText("");
			break;

		case R.id.cross_btn_password:
			break;

		case R.id.cross_btn_confirm_password:
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
			break;
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
			PreferenceEngine.getInstance(getApplicationContext()).saveBirthday(date);
		}
	};


}
