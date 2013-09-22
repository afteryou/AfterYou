package com.beacon.afterui.views;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
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
	private DatePicker mDatePicker;
	private int mLength;

	private EditText mFirstnameText;
	private EditText mLastNameText;
	private EditText mEmailText;
	private EditText mBirthDayText;
	private EditText mPasswordText;
	private EditText mConfirmText;

	private String mDateOfBirthTxt;

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
					CapturePictureActivity.class);
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
			dateDialog();
			break;
		}

		if (intent == null) {
			return;
		}
		try {
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in,
					R.anim.slide_out);
		} catch (ActivityNotFoundException e) {

			Log.e(TAG, " Activity not found : " + e.getMessage());
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		switch (v.getId()) {

		case R.id.birthday_edit_text:
			if (hasFocus) {
				dateDialog();
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

	private void dateDialog() {
		final Dialog dateDialog = new Dialog(this);
		dateDialog.setContentView(R.layout.date_picker);
		dateDialog.setTitle("Select date");
		mDatePicker = (DatePicker) dateDialog.findViewById(R.id.dob_picker);
		Button doneBtn = (Button) dateDialog
				.findViewById(R.id.done_btn_dob_dialog);

		doneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final int day = mDatePicker.getDayOfMonth();
				final int month = mDatePicker.getMonth() + 1;
				final int year = mDatePicker.getYear();

				mDateOfBirthTxt = day + "/" + month + "/" + year;
				mBirthDayText.setText(mDateOfBirthTxt);
				dateDialog.dismiss();
				mBirthDayText.clearFocus();

			}
		});
		dateDialog.show();
	}
}
