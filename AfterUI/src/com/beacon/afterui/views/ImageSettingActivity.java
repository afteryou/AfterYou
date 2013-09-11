package com.beacon.afterui.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;

public class ImageSettingActivity extends BaseActivity {

	private ImageView mSubmitBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_setting);
		mSubmitBtn = (ImageView) findViewById(R.id.submitbtn);
		mSubmitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(ImageSettingActivity.this, PhotoAlbum.class);
				startActivity(intent);

			}
		});
	}

}