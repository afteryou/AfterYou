package com.beacon.afterui.views;

import com.beacon.afterui.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class LoginScreen extends Activity implements OnClickListener {

	/** TAG */
	private static final String TAG = LoginScreen.class.getSimpleName();

	private ImageButton mLoginButton;

	private ImageView mCrossEmailBtn;

	private ImageView mCrossPasswordBtn;

	private EditText mEmailText;
	private EditText mPasswordText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		mLoginButton = (ImageButton) findViewById(R.id.sign_in_btn_login_screen);
		mCrossEmailBtn = (ImageView) findViewById(R.id.cross_btn_email_login_screen);
		mCrossPasswordBtn = (ImageView) findViewById(R.id.cross_btn_password_login_screen);
		mEmailText = (EditText) findViewById(R.id.email_text);
		mPasswordText = (EditText) findViewById(R.id.password_text);

		mCrossEmailBtn.setOnClickListener(this);
		mCrossPasswordBtn.setOnClickListener(this);
		mLoginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.sign_in_btn_login_screen:
			intent = new Intent(LoginScreen.this, ProfileSettingsActivity.class);
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
		try {
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in,
					R.anim.slide_out);
		} catch (ActivityNotFoundException e) {

			Log.e(TAG, " Activity not found : " + e.getMessage());
		}

	}

}
