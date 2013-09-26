package com.beacon.afterui.views;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.ImageUtils;

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
	JSONObject profileURL = null;
	private Context ctx;
	private String accessToken = null;

	public static final int GET_URL = 0;
	public static final int DONE_URL = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setIsRootView(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture_picture);
		this.ctx = this;
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

		accessToken = ((AfterYouApplication)getApplication()).getOpenSession().getAccessToken();
		if (getIntent().hasExtra(AppConstants.FACEBOOK_USER)) {
			initView();
		}

	}

	private void initView() {

		HandlerThread urlThread = new HandlerThread("profile_url");
		urlThread.start();

		Looper mLoop = urlThread.getLooper();
		URLHandler urlHandle = new URLHandler(mLoop);

		urlHandle.sendEmptyMessage(GET_URL);
		
		ImageUtils.getInstance(ctx).loadImage("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash4/372183_100002526091955_998385602_q.jpg", mUserImage);

	}

	class URLHandler extends Handler {
		public URLHandler(Looper loop) {
			super(loop);
		}

		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case GET_URL:
				InputStream is = null;
				String result = "";
				String url = "https://graph.facebook.com/"+PreferenceEngine.getInstance(ctx).getProfileID()+"/picture?redirect=false?access_token="+accessToken;
				
				try {   
				    HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
				    HttpPost httppost = new HttpPost(url);
				    HttpResponse response = httpclient.execute(httppost);
				    HttpEntity entity = response.getEntity();
				    is = entity.getContent();

				    // Read response to string
				    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),1024);
				    StringBuilder sb = new StringBuilder();
				    String line = "";
				    while ((line = reader.readLine()) != null) {
				    sb.append(line + "\n");
				    }
				    is.close();
				    result = sb.toString(); 


				    if (result.isEmpty()) { 
				        result = "nothing"; 
				        Toast message = Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG);
				        message.show();
				    }
				 // Convert string to object

				    profileURL = new JSONObject(result);
				    sendEmptyMessage(DONE_URL);
				
			
				}
				catch(Exception ex)
				{
					
				}
				break;
			case DONE_URL:
//				if(profileURL.optBoolean("is_silhouette"))
//				{
					
//				}
				break;
		}
	}
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