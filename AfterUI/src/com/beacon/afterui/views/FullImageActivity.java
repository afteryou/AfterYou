package com.beacon.afterui.views;

import com.beacon.afterui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImageActivity extends Activity {
	private static final String POSITION = "position";
	private ImageView mFullSizeImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image_activity);
		mFullSizeImage = (ImageView) findViewById(R.id.photo_of_user_full_image);

	}

}
