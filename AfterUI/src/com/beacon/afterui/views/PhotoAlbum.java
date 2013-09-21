package com.beacon.afterui.views;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;

public class PhotoAlbum extends BaseActivity implements OnClickListener {

	private ImageView mCancelBtn;
	private ImageView mDoneBtn;
	private static final int SELECT_PICTURE = 1;
	private String filemanagerstring;
	private String selectedImagePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_album);
		mCancelBtn = (ImageView) findViewById(R.id.cancel_btn_photo_album);
		mDoneBtn = (ImageView) findViewById(R.id.done_btn_photo_album);

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				SELECT_PICTURE);
		mCancelBtn.setOnClickListener(this);
		mDoneBtn.setOnClickListener(this);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();

				// OI FILE Manager
				filemanagerstring = selectedImageUri.getPath();

				// MEDIA GALLERY
				selectedImagePath = getPath(selectedImageUri);
				Log.d("Images", filemanagerstring);
				Log.d("Images", selectedImagePath);

				// DEBUG PURPOSE - you can delete this if you want
				if (selectedImagePath != null)
					System.out.println(selectedImagePath);
				else
					System.out.println("selectedImagePath is null");
				if (filemanagerstring != null)
					System.out.println(filemanagerstring);
				else
					System.out.println("filemanagerstring is null");

				// NOW WE HAVE OUR WANTED STRING
				if (selectedImagePath != null)
					System.out
							.println("selectedImagePath is the right one for you!");
				else
					System.out
							.println("filemanagerstring is the right one for you!");
			}
		}
	}

	// UPDATED!
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

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

}