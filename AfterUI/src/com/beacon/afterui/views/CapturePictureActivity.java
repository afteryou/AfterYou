package com.beacon.afterui.views;

import android.os.Bundle;
import android.widget.ImageView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;

public class CapturePictureActivity extends BaseActivity {

	private ImageView mSubmitBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setIsRootView(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture_picture);
//		mSubmitBtn = (ImageView) findViewById(R.id.submitbtn);
//		mSubmitBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//				Intent intent = new Intent(CapturePictureActivity.this,
//						PhotoPreview.class);
//				startActivity(intent);
//
//			}
//		});
	}
	
	


}