package com.beacon.afterui.views;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

public class PhotoAlbum extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private ImageView mCancelBtn;
	private ImageView mDoneBtn;
	private static final int SELECT_PICTURE = 1;
	private String filemanagerstring;
	private String selectedImagePath;

	private static final int LOADING_ALBUMS = 1;

	private static final int LOADING_ALBUMS_COMPLETED = 2;

	private HandlerThread mHandlerThread;

	private static List<Album> sAlbumList;

	private ProgressDialog mProgresDialog;

	private Handler mAlbumHandler;

	private ListView mListView;

	public static final String ID = "id";
	public static final String NAME = "name";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_album);
		mCancelBtn = (ImageView) findViewById(R.id.cancel_btn_photo_album);
		mDoneBtn = (ImageView) findViewById(R.id.done_btn_photo_album);
		mListView = (ListView) findViewById(R.id.album_list);
		mListView.setOnItemClickListener(this);
		mCancelBtn.setOnClickListener(this);
		mDoneBtn.setOnClickListener(this);
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
				mProgresDialog = new ProgressDialog(PhotoAlbum.this);
				mProgresDialog.setTitle("Loading albums..");
				mProgresDialog.show();
				break;

			case LOADING_ALBUMS_COMPLETED:
				mProgresDialog.dismiss();
				mListView.setAdapter(new AlbumAdapter());
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.done_btn_photo_album:

			Intent intent = new Intent(PhotoAlbum.this,
					PhotoGallaryActivity.class);
			startActivity(intent);
			break;

		case R.id.cancel_btn_photo_album:

			break;

		}
	}

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

	private Runnable mLoadAlbums = new Runnable() {

		@Override
		public void run() {
			sAlbumList = ImageInfoUtils.getAlbumList(PhotoAlbum.this);
			mHandler.sendEmptyMessage(LOADING_ALBUMS_COMPLETED);
		}
	};

	private class AlbumAdapter extends BaseAdapter {

		LayoutInflater inflator;

		public AlbumAdapter() {
			inflator = PhotoAlbum.this.getLayoutInflater();
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
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {

			if (view == null) {
				view = inflator.inflate(R.layout.albumlayout, null, false);
			}

			Album album = (Album) getItem(position);

			TextView albumName = (TextView) view.findViewById(R.id.album_name);
			albumName.setText(album.name);

			TextView albumCount = (TextView) view.findViewById(R.id.number_txt);
			albumCount.setText("" + album.count);

			ImageView coverImage = (ImageView) view
					.findViewById(R.id.photo_of_user);
			coverImage.setImageBitmap(album.thumb);

			return view;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {

		Album album = sAlbumList.get(position);

		Intent intent = new Intent(PhotoAlbum.this, PhotoGallaryActivity.class);
		intent.putExtra(ID, album.id);
		intent.putExtra(NAME, album.name);
		startActivity(intent);

	}
}