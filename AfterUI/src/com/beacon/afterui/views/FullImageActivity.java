package com.beacon.afterui.views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.ImageInfoUtils;

public class FullImageActivity extends Activity implements OnClickListener {
	private static final String ID = "id";
	private static final String PATH = "path";
	private static final String FLAG = "ok";
	private ImageView mFullSizeImage;
	private String mImagePath;
	private ImageButton mDoneBtn;
	private ImageButton mCancelBtn;
	private long mId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image_activity);
		mFullSizeImage = (ImageView) findViewById(R.id.photo_of_user_full_image);
		mDoneBtn = (ImageButton) findViewById(R.id.done_btn_full_image_activity);
		mCancelBtn = (ImageButton) findViewById(R.id.cancel_btn_full_image_activity);
		mId = getIntent().getLongExtra(ID, -1);
		mImagePath = ImageInfoUtils.getPhotoPath(FullImageActivity.this,
				String.valueOf(mId));
		Uri uri = Uri.parse(mImagePath);
		mFullSizeImage.setImageURI(uri);
		mDoneBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.done_btn_full_image_activity:
			Intent intent = new Intent();
			intent.putExtra(PATH, mImagePath);
			intent.putExtra(FLAG, "ok");
			intent.putExtra(ID, mId);
			setResult(RESULT_OK, intent);
			finish();
			break;

		case R.id.cancel_btn_full_image_activity:
			setResult(RESULT_CANCELED);
			finish();
			break;

		}

	}

}
