package com.beacon.afterui.views;

import java.security.spec.MGF1ParameterSpec;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.ImageInfoUtils;
import com.beacon.afterui.utils.Photo;

public class PhotoGallaryActivity extends Activity {

	private GridView mPhotoGridView;
	private static final String POSITION = "position";
	private String mId;
	private String mName;
	
	private List<Photo> mPhotoList;
	
	private static final int LOADING_IMAGES = 1;

	private static final int LOADING_IMAGES_COMPLETED = 2;
	
	private ProgressDialog mProgresDialog;

	private Handler mAlbumHandler;
	
	private HandlerThread mHandlerThread;
	
	private ImageAdapter mImageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_gallary);

		mId = getIntent().getStringExtra(PhotoAlbum.ID);
		mName = getIntent().getStringExtra(PhotoAlbum.NAME);

		mPhotoGridView = (GridView) findViewById(R.id.photo_gallary_layout);
		mPhotoGridView.setAdapter(new ImageAdapter(this));
		mPhotoGridView.setOnItemClickListener(mPhotoGriedListener);
		
//		mPhotoList = ImageInfoUtils.getPhotoList(this, mId);
//
//		Log.d( "test", "Total Image : " + mPhotoList.size());
		mHandlerThread = new HandlerThread("album_loader");
		mHandlerThread.start();
		mAlbumHandler = new Handler(mHandlerThread.getLooper());

		mHandler.sendEmptyMessage(LOADING_IMAGES);
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case LOADING_IMAGES:
				mAlbumHandler.post(mLoadImages);
				mProgresDialog = new ProgressDialog(PhotoGallaryActivity.this);
				mProgresDialog.setTitle("Loading albums..");
				mProgresDialog.show();
				break;

			case LOADING_IMAGES_COMPLETED:
				mImageAdapter = new ImageAdapter(PhotoGallaryActivity.this);
				mImageAdapter.setPhotoList(mPhotoList);
				mPhotoGridView.setAdapter(mImageAdapter);
				mImageAdapter.notifyDataSetChanged();
				mProgresDialog.dismiss();
				break;
			}
		};
	};
	
	private Runnable mLoadImages = new Runnable() {

		@Override
		public void run() {
			mPhotoList = ImageInfoUtils.getPhotoList(PhotoGallaryActivity.this, mId);
			mHandler.sendEmptyMessage(LOADING_IMAGES_COMPLETED);
		}
	};

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
	
	@Override
	protected void onDestroy() {

		if (mHandlerThread != null && mHandlerThread.isAlive()) {
			mHandlerThread.quit();
		}

		if (mProgresDialog != null && mProgresDialog.isShowing()) {
			mProgresDialog.dismiss();
		}

		super.onDestroy();
	}
}
