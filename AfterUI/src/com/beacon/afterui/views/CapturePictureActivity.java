package com.beacon.afterui.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.application.AfterYouApplication;
import com.facebook.model.GraphUser;

public class CapturePictureActivity extends BaseActivity implements
        OnClickListener {

    private ImageButton mSubmitBtn;
    private ImageButton mEditBtn;
    private ImageButton mCancelBtn;
    private ImageButton mDoneBtn;
    private ImageButton mCropBarBtn;
    private ImageButton mChooseFromLiabraryBtn;
    private ImageView mCropImgSqaureGray;
    private ImageView mCropImgSqaureLine;
    private String PATH = "path";
    private ImageView mUserImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setIsRootView(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_picture);
        mSubmitBtn = (ImageButton) findViewById(R.id.submit_btn);
        mEditBtn = (ImageButton) findViewById(R.id.edit_crop_btn);
        mCancelBtn = (ImageButton) findViewById(R.id.cancel_btn_capture_picture);
        mDoneBtn = (ImageButton) findViewById(R.id.done_btn_capture_picture);
        mCropBarBtn = (ImageButton) findViewById(R.id.crop_bar_btn);
        mChooseFromLiabraryBtn = (ImageButton) findViewById(R.id.choose_from_library_btn);
        mCropImgSqaureGray = (ImageView) findViewById(R.id.crop_image_sqaure);
        mCropImgSqaureLine = (ImageView) findViewById(R.id.crop_image_sqaure_white);
        mUserImage = (ImageView) findViewById(R.id.user_image);

        mEditBtn.setOnClickListener(this);
        mChooseFromLiabraryBtn.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);

        setUserInfoChangedCallback(new UserInfoChangedCallback() {

            @Override
            public void onUserInfoFetched(GraphUser user) {
                ((AfterYouApplication) getApplication()).setUser(user);

            }
        });

        fetchUserInfo();

    }

    @Override
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()) {
        case R.id.choose_from_library_btn:

            intent = new Intent(CapturePictureActivity.this,
                    PhotoAlbumActivity.class);
            break;

        case R.id.submit_btn:

            break;

        case R.id.edit_crop_btn:

            mCancelBtn.setVisibility(View.VISIBLE);
            mDoneBtn.setVisibility(View.VISIBLE);
            mCropBarBtn.setVisibility(View.VISIBLE);
            mCropImgSqaureGray.setVisibility(View.VISIBLE);
            mCropImgSqaureLine.setVisibility(View.VISIBLE);
            break;

        case R.id.cancel_btn_capture_picture:

            break;
        case R.id.done_btn_capture_picture:

            break;

        case R.id.crop_bar_btn:

            break;

        case R.id.crop_image_sqaure:

            break;

        case R.id.crop_image_sqaure_white:

            break;

        }

        if (intent == null) {
            return;
        }
        try {
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, " Activity not found : " + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            Log.d(TAG, "User cancelled the operation!");
            return;
        }

        switch (resultCode) {
        case RESULT_OK:
            String result = data.getStringExtra(PATH);
            Uri uri = Uri.parse(result);
            mUserImage.setImageURI(uri);
            mUserImage.setScaleType(ScaleType.CENTER_CROP);
            break;
        }
    }

    // (new FacebookUserInfo(this))
    // .execute(new FacebookGraphUserInfo());

}