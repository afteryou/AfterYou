package com.beacon.afterui.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.imageUtils.ImageFilter;
import com.beacon.afterui.utils.ImageInfoUtils;

public class FullImageActivity extends BaseActivity implements OnClickListener {
	private static final String ID = "id";
	private static final String PATH = "path";
	private static final String FLAG = "ok";
	private ImageView mFullSizeImage;
	private String mImagePath;
	private ImageButton mDoneBtn;
	private ImageButton mCancelBtn;
    private ImageButton mImageRotateBtn;
    private ImageButton mImageCropBtn;
    private ImageButton mImageEffectBtn;
    private ImageButton mImageRedEyeBtn;
	private long mId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.full_image_activity);
		setBehindLeftContentView(R.layout.full_image_activity);
		setBehindRightContentView(R.layout.full_image_activity);
		mFullSizeImage = (ImageView) findViewById(R.id.photo_of_user_full_image);
		mDoneBtn = (ImageButton) findViewById(R.id.done_btn_full_image_activity);
		mCancelBtn = (ImageButton) findViewById(R.id.cancel_btn_full_image_activity);

		TextView cancelTxt = (TextView) findViewById(R.id.cancel_txt);
		TextView chooseFromLiTxt = (TextView) findViewById(R.id.choose_from_library_txt);
        mImageCropBtn = (ImageButton) findViewById(R.id.image_crop_btn);
        mImageRotateBtn = (ImageButton) findViewById(R.id.image_rotate_btn);
        mImageEffectBtn = (ImageButton) findViewById(R.id.image_effect_btn);
        mImageRedEyeBtn = (ImageButton) findViewById(R.id.image_red_eye_btn);
        
        mImageCropBtn.setOnClickListener(this);
        mImageRotateBtn.setOnClickListener(this);
        mImageEffectBtn.setOnClickListener(this);
        mImageRedEyeBtn.setOnClickListener(this);
        
		TextView doneTxt = (TextView) findViewById(R.id.done_txt);
		Typeface typeFaceSemiBold = Typeface.createFromAsset(getAssets(),
				"fonts/MyriadPro-Semibold.otf");
		cancelTxt.setTypeface(typeFaceSemiBold);
		chooseFromLiTxt.setTypeface(typeFaceSemiBold);
		doneTxt.setTypeface(typeFaceSemiBold);

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
		
		case R.id.image_rotate_btn:
			Bitmap bit = ImageFilter.rotate(((BitmapDrawable)mFullSizeImage.getDrawable()).getBitmap(), 45);
			mFullSizeImage.setImageBitmap(bit);
			break;

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
