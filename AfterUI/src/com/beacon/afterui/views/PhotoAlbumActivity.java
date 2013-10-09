package com.beacon.afterui.views;

import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.utils.Album;
import com.beacon.afterui.utils.ImageInfoUtils;
import com.beacon.afterui.utils.customviews.CustomProgressDialog;
import com.beacon.afterui.utils.customviews.DialogHelper;

public class PhotoAlbumActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	/** TAG */
	private static final String TAG = PhotoAlbumActivity.class.getSimpleName();

	private ImageView mCancelBtn;

	private static final int LOADING_ALBUMS = 1;

	private static final int LOADING_ALBUMS_COMPLETED = 2;

	private static final int NO_IMAGE = 3;

	private HandlerThread mHandlerThread;

	private static List<Album> sAlbumList;

	private Handler mAlbumHandler;

	private ListView mListView;

	private TextView mNoImageGallary;

	public static final String ID = "id";
	public static final String NAME = "name";
	private CustomProgressDialog wait_progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_album);
		mCancelBtn = (ImageView) findViewById(R.id.cancel_btn_photo_album);
		mListView = (ListView) findViewById(R.id.album_list);
		mListView.setOnItemClickListener(this);
		mNoImageGallary = (TextView) findViewById(R.id.no_images);
		TextView cancelTxt = (TextView) findViewById(R.id.cancel_txt);
		TextView chooseFromLiTxt = (TextView) findViewById(R.id.choose_from_library_txt);

		// font myriadPro semibold
		Typeface typeFaceSemiBold = Typeface.createFromAsset(getAssets(),
				"fonts/MyriadPro-Semibold.otf");
		cancelTxt.setTypeface(typeFaceSemiBold);
		chooseFromLiTxt.setTypeface(typeFaceSemiBold);

		mCancelBtn.setOnClickListener(this);
		mHandlerThread = new HandlerThread("album_loader");
		mHandlerThread.start();
		mAlbumHandler = new Handler(mHandlerThread.getLooper());

		mHandler.sendEmptyMessage(LOADING_ALBUMS);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case LOADING_ALBUMS:
				mAlbumHandler.post(mLoadAlbums);
				wait_progress = DialogHelper.createProgessDialog(
						PhotoAlbumActivity.this, null);
				wait_progress.setMessage(PhotoAlbumActivity.this
						.getString(R.string.IDS_GETTING_ALBUM));
				wait_progress
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								removeDialog();
							}
						});
				wait_progress.show();
				break;

			case LOADING_ALBUMS_COMPLETED:
				removeDialog();
				mListView.setAdapter(new AlbumAdapter());
				break;

			case NO_IMAGE:
				removeDialog();
				mNoImageGallary.setVisibility(View.VISIBLE);
				break;
			}
		};
	};

	protected void removeDialog() {

		if (null != PhotoAlbumActivity.this && null != wait_progress
				&& wait_progress.isShowing()) {

			wait_progress.dismiss();

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

	@Override
	protected void onDestroy() {

		if (mHandlerThread != null && mHandlerThread.isAlive()) {
			mHandlerThread.quit();
		}

		Log.d("test", "OnDestory of PhotoAlbumActivity");
		super.onDestroy();
	}

	private Runnable mLoadAlbums = new Runnable() {

		@Override
		public void run() {
			sAlbumList = ImageInfoUtils.getAlbumList(PhotoAlbumActivity.this);
			if (sAlbumList.size() > 0) {
				mHandler.sendEmptyMessage(LOADING_ALBUMS_COMPLETED);
			} else {
				mHandler.sendEmptyMessage(NO_IMAGE);
			}
		}
	};

	private class AlbumAdapter extends BaseAdapter {

		LayoutInflater inflator;

		public AlbumAdapter() {
			inflator = PhotoAlbumActivity.this.getLayoutInflater();
		}

		@Override
		public int getCount() {
			return sAlbumList.size();
		}

		@Override
		public Object getItem(int position) {
			return sAlbumList.get(position);
		}

		@Override
		public long getItemId(int id) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {

			// font myriadPro regular
			Typeface typeFaceRegular = Typeface.createFromAsset(getAssets(),
					"fonts/MyriadPro-Regular.otf");
			Album album = (Album) getItem(position);

			if (view == null) {
				view = inflator.inflate(R.layout.albumlayout, null, false);

				if (position == 0) {
					Bitmap bitmap = ImageInfoUtils
							.roundCornered(album.thumb, 4);
					album.thumb.recycle();
					album.thumb = bitmap;
				}
			}

			TextView albumName = (TextView) view.findViewById(R.id.album_name);
			albumName.setText(album.name);
			albumName.setTypeface(typeFaceRegular);

			TextView albumCount = (TextView) view.findViewById(R.id.number_txt);
			albumCount.setText(String.valueOf(album.count));
			albumCount.setTypeface(typeFaceRegular);

			ImageView coverImage = (ImageView) view
					.findViewById(R.id.photo_of_user);
			coverImage.setImageBitmap(album.thumb);

			return view;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case RESULT_OK:
			setResult(resultCode, data);
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {

		Album album = sAlbumList.get(position);

		Intent intent = new Intent(PhotoAlbumActivity.this,
				PhotoGallaryActivity.class);
		intent.putExtra(ID, album.id);
		intent.putExtra(NAME, album.name);
		startActivityForResult(intent, 1);
	}
}