package com.beacon.afterui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageSettingActivity extends Activity {

	private ImageView mSubmitBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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