package com.beacon.afterui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PhotoAlbum extends Activity {

	private ImageView mCancelBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_album);
		mCancelBtn = (ImageView) findViewById(R.id.cancelbtn);
		mCancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				finish();

			}
		});
	}

}