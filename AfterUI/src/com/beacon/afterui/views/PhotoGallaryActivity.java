package com.beacon.afterui.views;

import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.aviary.android.feather.FeatherActivity;
import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.utils.ImageInfoUtils;
import com.beacon.afterui.utils.Photo;
import com.beacon.afterui.utils.customviews.CustomProgressDialog;
import com.beacon.afterui.utils.customviews.DialogHelper;

public class PhotoGallaryActivity extends BaseActivity implements
		OnClickListener {

	private GridView mPhotoGridView;
	private static final String ID = "id";
	private String mId;
	private String mName;

	private TextView mCancelButton;
	private TextView mDoneBtn;

	private List<Photo> mPhotoList;

	private static final int LOADING_IMAGES = 1;

	private static final int LOADING_IMAGES_COMPLETED = 2;

	private Handler mAlbumHandler;

	private HandlerThread mHandlerThread;

	private ImageAdapter mImageAdapter;
	private CustomProgressDialog wait_progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_gallary);
		setBehindLeftContentView(R.layout.photo_gallary);
		setBehindRightContentView(R.layout.photo_gallary);
		mId = getIntent().getStringExtra(PhotoAlbumActivity.ID);
		mName = getIntent().getStringExtra(PhotoAlbumActivity.NAME);

		// font myriadPro semibold
		Typeface itcAvaStdBk = Typeface.createFromAsset(getAssets(),
				"fonts/ITCAvantGardeStd-Bk.otf");

		mPhotoGridView = (GridView) findViewById(R.id.photo_gallary_layout);
		mPhotoGridView.setAdapter(new ImageAdapter(this));
		mPhotoGridView.setOnItemClickListener(mPhotoGriedListener);

		mCancelButton = (TextView) findViewById(R.id.cancel_btn_photo_album);
		mCancelButton.setOnClickListener(this);
		mCancelButton.setTypeface(itcAvaStdBk);

		mDoneBtn = (TextView) findViewById(R.id.done_btn_photo_album);
		mDoneBtn.setTypeface(itcAvaStdBk);

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
				wait_progress = DialogHelper.createProgessDialog(
						PhotoGallaryActivity.this, null);
				wait_progress.setMessage(PhotoGallaryActivity.this
						.getString(R.string.IDS_GETTING_PHOTOS_FROM_ALBUM));
				wait_progress
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								removeDialog();
							}
						});
				wait_progress.show();
				break;

			case LOADING_IMAGES_COMPLETED:
				mImageAdapter = new ImageAdapter(PhotoGallaryActivity.this);
				mImageAdapter.setPhotoList(mPhotoList);
				mPhotoGridView.setAdapter(mImageAdapter);
				mImageAdapter.notifyDataSetChanged();
				removeDialog();
				break;
			}
		};
	};

	protected void removeDialog() {

		if (null != PhotoGallaryActivity.this && null != wait_progress
				&& wait_progress.isShowing()) {

			wait_progress.dismiss();

		}

	}

	private Runnable mLoadImages = new Runnable() {

		@Override
		public void run() {
			mPhotoList = ImageInfoUtils.getPhotoList(PhotoGallaryActivity.this,
					mId);
			mHandler.sendEmptyMessage(LOADING_IMAGES_COMPLETED);
		}
	};

	OnItemClickListener mPhotoGriedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Photo photo = mPhotoList.get(position);

			// Intent intent = new Intent(getApplicationContext(),
			// FullImageActivity.class);
			// intent.putExtra(ID, photo.coverId);
			// startActivityForResult(intent, 1);

			String imagePath = ImageInfoUtils.getPhotoPath(
					PhotoGallaryActivity.this, String.valueOf(photo.coverId));
			Intent newIntent = new Intent(PhotoGallaryActivity.this,
					FeatherActivity.class);
			newIntent.setData(Uri.parse(imagePath));
			startActivityForResult(newIntent, 1);

		}
	};

	@Override
	protected void onDestroy() {

		if (mHandlerThread != null && mHandlerThread.isAlive()) {
			mHandlerThread.quit();
		}

		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK, data);
			finish();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_btn_photo_album:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}
}
