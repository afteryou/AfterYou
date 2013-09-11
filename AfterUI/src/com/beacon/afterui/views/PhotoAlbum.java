package com.beacon.afterui.views;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;

public class PhotoAlbum extends BaseActivity {

	private ImageView mCancelBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
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