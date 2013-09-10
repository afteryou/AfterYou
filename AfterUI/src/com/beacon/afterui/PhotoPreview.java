package com.beacon.afterui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PhotoPreview extends Activity {

	private ImageView mPhotoPreviewBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_preview);
		mPhotoPreviewBtn = (ImageView) findViewById(R.id.submitbtn);
		mPhotoPreviewBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(PhotoPreview.this,
						ImageSettingActivity.class);
				startActivity(intent);

			}
		});
	}

}