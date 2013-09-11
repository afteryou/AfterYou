package com.beacon.afterui.views;

import android.os.Bundle;
import android.widget.ImageView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;

public class SignUpActivity extends BaseActivity {

	private ImageView mSubmitBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up_screen);
//		mSubmitBtn = (ImageView) findViewById(R.id.submitbtn);
//		mSubmitBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//				Intent intent = new Intent(SignUpActivity.this,
//						CapturePicture.class);
//				startActivity(intent);
//
//			}
//		});
	}

}
