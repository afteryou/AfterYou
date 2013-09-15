package com.beacon.afterui.views;

import com.beacon.afterui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.application.AfterYouApplication;
import com.facebook.model.GraphUser;
public class CapturePictureActivity extends BaseActivity {

    private ImageButton mSubmitBtn;
    private ImageButton mEditBtn;
    private ImageButton mCancelBtn;
    private ImageButton mDoneBtn;
    private ImageButton mCropBarBtn;
    private ImageButton mChooseFromLiabraryBtn;
    private ImageView mCropImgSqaureGray;
    private ImageView mCropImgSqaureLine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        mEditBtn.setOnClickListener(mEditListener);
        mChooseFromLiabraryBtn.setOnClickListener(mChooseFromLiabraryListener);
        mSubmitBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

            }
        });
		setUserInfoChangedCallback(new UserInfoChangedCallback() {
			
			@Override
			public void onUserInfoFetched(GraphUser user) {
				((AfterYouApplication)getApplication()).setUser(user);					
				
			}
		});
		
		fetchUserInfo();
	
    }

    OnClickListener mEditListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {

            mCancelBtn.setVisibility(View.VISIBLE);
            mDoneBtn.setVisibility(View.VISIBLE);
            mCropBarBtn.setVisibility(View.VISIBLE);
            mCropImgSqaureGray.setVisibility(View.VISIBLE);
            mCropImgSqaureLine.setVisibility(View.VISIBLE);

        }
    };
    OnClickListener mChooseFromLiabraryListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 Intent intent = new Intent(CapturePictureActivity.this,
	                    PhotoAlbum.class);
	            startActivity(intent);
			
		}
	};
	

//		(new FacebookUserInfo(this))
//				.execute(new FacebookGraphUserInfo());


}