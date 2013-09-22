package com.beacon.afterui.views;

import com.beacon.afterui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PhotoGallaryActivity extends Activity {

	private GridView mPhotoGridView;
	private static final String POSITION = "position";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_gallary);

		mPhotoGridView = (GridView) findViewById(R.id.photo_gallary_layout);
		mPhotoGridView.setAdapter(new ImageAdapter(this));
		mPhotoGridView.setOnItemClickListener(mPhotoGriedListener);

	}

	OnItemClickListener mPhotoGriedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent intent = new Intent(getApplicationContext(),
					FullImageActivity.class);
			intent.putExtra(POSITION, position);
			startActivity(intent);

		}
	};
}
