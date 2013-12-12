package com.beacon.afterui.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.utils.AnalyticsUtils;

public class SignInSignUpScreen extends BaseActivity implements OnClickListener {

	private TextView mSingInBtn;
	private TextView mSingUpBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sign_in_sign_up_screen);
		setBehindLeftContentView(R.layout.sign_in_sign_up_screen);
		setBehindRightContentView(R.layout.sign_in_sign_up_screen);
		mSingInBtn = (TextView) findViewById(R.id.sign_in);
		mSingUpBtn = (TextView) findViewById(R.id.sign_up);

		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ITCAvantGardeStd-BkCn.otf");
		mSingInBtn.setTypeface(typeFace);
		mSingUpBtn.setTypeface(typeFace);

		mSingInBtn.setOnClickListener(this);
		mSingUpBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		Intent intent = null;
		switch (v.getId()) {
		case R.id.sign_in:

			intent = new Intent(SignInSignUpScreen.this, LoginScreen.class);
			AnalyticsUtils.logButtonPressEvent(this, "login button", -1);
			break;

		case R.id.sign_up:

			intent = new Intent(SignInSignUpScreen.this, SignUpActivity.class);
			AnalyticsUtils.logButtonPressEvent(this, "signup button", -1);

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
