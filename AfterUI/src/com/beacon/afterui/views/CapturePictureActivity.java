package com.beacon.afterui.views;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.application.CrashHandler;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.imageUtils.ImageResizer;
import com.beacon.afterui.log.AfterUIlog;
import com.beacon.afterui.utils.ImageInfoUtils;
import com.beacon.afterui.utils.ImageUtils;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomProgressDialog;
import com.beacon.afterui.utils.customviews.DialogHelper;
import com.beacon.afterui.utils.customviews.ErrorDialog;

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
	private ImageButton mImageRotateBtn;
	private ImageButton mImageCropBtn;
	private ImageButton mImageEffectBtn;
	private ImageButton mImageRedEyeBtn;
	private static final String PATH = "path";
	private static final String ID = "id";
	private ImageView mUserImage;
	private JSONObject profileURL = null;
	private Context ctx;
	Bitmap editedBitmap;

	private RelativeLayout mImageEditLayout;
	private static final String FLAG = "ok";

	private Uri mImageUri;

	private String accessToken = null;
	private HandlerThread urlThread;
	private UIHandler handler;

	public static final int GET_URL = 0;
	public static final int DONE_URL = 1;
	public static final int UPDATE_IMAGE = 2;
	public static final int START_GETTING_IMAGE = 3;
	public static final int START_EDITING_IMAGE = 4;
	public static final int DONE_GETTING_IMAGE = 5;
	public static final int START_PROCESS_IMAGE = 6;
	public static final int ERROR_FILTERING_IMAGE = 7;

	private HandlerThread mDeamonThread;
	private Handler mDeamonHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setIsRootView(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture_picture);
		this.ctx = this;
		mSubmitBtn = (ImageButton) findViewById(R.id.submit_btn);
		mEditBtn = (ImageButton) findViewById(R.id.edit_btn);
		mChooseFromLiabraryBtn = (ImageButton) findViewById(R.id.choose_from_library_btn);

		mCancelBtn = (ImageButton) findViewById(R.id.cancel_btn_capture_picture);
		mDoneBtn = (ImageButton) findViewById(R.id.done_btn_capture_picture);

		TextView cancelTxt = (TextView) findViewById(R.id.cancel_txt);
		TextView takePhotoTxt = (TextView) findViewById(R.id.take_photo_txt);
		TextView doneTxt = (TextView) findViewById(R.id.done_txt);
		TextView chooseFromFbTxt = (TextView) findViewById(R.id.choose_from_fb_txt);
		TextView chooseFromLibraryTxt = (TextView) findViewById(R.id.choose_from_library_txt);
		TextView editTxt = (TextView) findViewById(R.id.edit_txt);
		TextView submitTxt = (TextView) findViewById(R.id.submit_txt);

		// font myriadPro regular
		Typeface typeFaceRegular = Typeface.createFromAsset(getAssets(),
				"fonts/MyriadPro-Regular.otf");
		chooseFromFbTxt.setTypeface(typeFaceRegular);
		chooseFromLibraryTxt.setTypeface(typeFaceRegular);
		editTxt.setTypeface(typeFaceRegular);
		submitTxt.setTypeface(typeFaceRegular);

		// font myriadPro semibold
		Typeface typeFaceSemiBold = Typeface.createFromAsset(getAssets(),
				"fonts/MyriadPro-Semibold.otf");
		cancelTxt.setTypeface(typeFaceSemiBold);
		takePhotoTxt.setTypeface(typeFaceSemiBold);
		doneTxt.setTypeface(typeFaceSemiBold);

		mImageCropBtn = (ImageButton) findViewById(R.id.image_crop_btn);
		mImageRotateBtn = (ImageButton) findViewById(R.id.image_rotate_btn);
		mImageEffectBtn = (ImageButton) findViewById(R.id.image_effect_btn);
		mImageRedEyeBtn = (ImageButton) findViewById(R.id.image_red_eye_btn);
		mImageEditLayout = (RelativeLayout) findViewById(R.id.image_edit_layout);

		mCropImgSqaureGray = (ImageView) findViewById(R.id.crop_image_sqaure);
		mCropImgSqaureLine = (ImageView) findViewById(R.id.crop_image_sqaure_white);
		mUserImage = (ImageView) findViewById(R.id.user_image);
		mUserImage.setImageDrawable(getResources().getDrawable(
				R.drawable.capture_photo_bg));
		mUserImage.setRotation(0);

		mEditBtn.setEnabled(true);
		mEditBtn.setOnClickListener(this);
		mChooseFromLiabraryBtn.setOnClickListener(this);
		mSubmitBtn.setOnClickListener(this);
		mImageCropBtn.setOnClickListener(this);
		mImageRotateBtn.setOnClickListener(this);
		mImageEffectBtn.setOnClickListener(this);
		mImageRedEyeBtn.setOnClickListener(this);

		mCancelBtn.setOnClickListener(this);
		mDoneBtn.setOnClickListener(this);

		initDeamonThread();

		handler = new UIHandler(getMainLooper());
		if (getIntent().hasExtra(AppConstants.FACEBOOK_USER)) {
			accessToken = ((AfterYouApplication) getApplication())
					.getOpenSession().getAccessToken();

			initView();
		}
	}

	private void initView() {

		urlThread = new HandlerThread("profile_url");
		urlThread.start();

		Looper mLoop = urlThread.getLooper();
		URLHandler uriHandler = new URLHandler(mLoop);
		handler.sendEmptyMessage(START_GETTING_IMAGE);
		uriHandler.sendEmptyMessage(GET_URL);
	}

	/**
	 * Should be used for backend processing.
	 */
	private void initDeamonThread() {
		mDeamonThread = new HandlerThread("deamon");
		mDeamonThread.start();

		mDeamonHandler = new Handler(mDeamonThread.getLooper());
	}

	class UIHandler extends Handler {
		private CustomProgressDialog wait_progress;

		public UIHandler(Looper loop) {
			super(loop);
		}

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERROR_FILTERING_IMAGE:
				showErrorDialog(R.string.IDS_ERROR_FILTER);
				break;
			case START_GETTING_IMAGE:
				wait_progress = DialogHelper.createProgessDialog(ctx, null);
				wait_progress.setMessage(ctx
						.getString(R.string.IDS_GETTING_IMAGE_FROM_FACEBOOK));
				wait_progress
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								removeDialog();
							}
						});
				wait_progress.show();
				break;

			case START_PROCESS_IMAGE:
				wait_progress = DialogHelper.createProgessDialog(ctx, null);
				wait_progress.setMessage(ctx
						.getString(R.string.IDS_APPLYING_FILTER));
				wait_progress
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								removeDialog();
							}
						});
				wait_progress.show();
				break;
			case DONE_GETTING_IMAGE:
				removeDialog();
				if (editedBitmap != null) {
					mUserImage.setImageBitmap(editedBitmap);
					AfterUIlog.d("test", " mUrlHandler : " + mDeamonHandler);
					mDeamonHandler.post(mSaveImage);
				}
				break;
			case UPDATE_IMAGE:

				setImage();
				removeDialog();
				break;
			}
		}

		protected void removeDialog() {

			if (null != ctx && null != wait_progress
					&& wait_progress.isShowing()) {

				wait_progress.dismiss();

			}

		}
	}

	private void showErrorDialog(int stringResId) {
		ErrorDialog errDialog = new ErrorDialog(new AfterYouDialogImpl(this),
				this, R.style.Theme_CustomDialog,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}, getResources().getString(stringResId));
		errDialog.show();
	}

	private class URLHandler extends Handler {
		public URLHandler(Looper loop) {
			super(loop);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_URL:
				InputStream is = null;
				String result = "";
				String url = "https://graph.facebook.com/me/picture?redirect=false&width="
						+ mUserImage.getDrawable().getIntrinsicWidth()
						+ "&height="
						+ mUserImage.getDrawable().getIntrinsicHeight()
						+ "&access_token=" + accessToken;

				try {
					HttpClient httpclient = new DefaultHttpClient(); // for port
																		// 80
																		// requests!
					HttpGet httpget = new HttpGet(url);
					HttpResponse response = httpclient.execute(httpget);
					HttpEntity entity = response.getEntity();
					is = entity.getContent();

					// Read response to string
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();
					String line = "";
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();
					result = sb.toString();

					if (result.isEmpty()) {
						result = "nothing";
						Toast message = Toast.makeText(getBaseContext(),
								result, Toast.LENGTH_LONG);
						message.show();
					}
					// Convert string to object

					profileURL = new JSONObject(result);
					sendEmptyMessage(DONE_URL);

				} catch (Exception ex) {

				}
				break;
			case DONE_URL:
				if (!profileURL.optBoolean("is_silhouette")) {
					handler.sendEmptyMessage(UPDATE_IMAGE);
					urlThread.quit();
				}
				break;
			}
		}
	}

	public void setImage() {
		try {
			ImageUtils.getInstance(ctx).loadImage(
					profileURL.getJSONObject("data").getString("url"),
					mUserImage);
			mEditBtn.setEnabled(true);
		} catch (JSONException ex) {
			CrashHandler.getInstance().collectCrashDeviceInfo(this);
		}

	}

	private void rotateImage() {

		Bitmap bitmap = ((BitmapDrawable) mUserImage.getDrawable()).getBitmap();

		Bitmap rotatedBitmap = ImageInfoUtils.rotateToPortrait(bitmap,
				ExifInterface.ORIENTATION_ROTATE_90);

		// We don't need to release old bitmap, because image view does it on
		// our behalf.
		if (rotatedBitmap != null) {
			mUserImage.setImageBitmap(rotatedBitmap);
			AfterUIlog.d("test", " mUrlHandler : " + mDeamonHandler);
			mDeamonHandler.post(mSaveImage);
		}
	}

	private Runnable mSaveImage = new Runnable() {

		@Override
		public void run() {
			// store to media store.
			ImageInfoUtils.saveToMediaStore(CapturePictureActivity.this,
					((BitmapDrawable) mUserImage.getDrawable()).getBitmap());
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data == null) {
			AfterUIlog.d(TAG, "User cancelled the operation!");
			return;
		}

		switch (resultCode) {
		case RESULT_OK:
			String result = data.getStringExtra(PATH);
			mImageUri = Uri.parse(result);
			mUserImage.setImageURI(mImageUri);
			mUserImage.setScaleType(ScaleType.CENTER_CROP);
			String flag = data.getStringExtra(FLAG);
			if (flag.equals("ok")) {
				mEditBtn.setEnabled(true);

			}
			break;
		}
	}

	@Override
	public void onClick(View v) {

		Intent intent = null;
		switch (v.getId()) {

		case R.id.cancel_btn_capture_picture:
			finish();
			break;

		case R.id.choose_from_library_btn:

			intent = new Intent(CapturePictureActivity.this,
					PhotoAlbumActivity.class);
			mImageEditLayout.setVisibility(View.INVISIBLE);
			mCropImgSqaureGray.setVisibility(View.GONE);
			mCropImgSqaureLine.setVisibility(View.GONE);
			break;

		case R.id.submit_btn:
			Intent i = new Intent(this, MainActivity.class);
			 startActivity(i);
			break;

		case R.id.edit_btn:

//			 Intent i = new Intent(this, MainActivity.class);
//			 startActivity(i);
			mImageEditLayout.setVisibility(View.VISIBLE);

			break;

		case R.id.crop_image_sqaure:

			break;

		case R.id.crop_image_sqaure_white:

			break;

		case R.id.image_crop_btn:

			mCropImgSqaureGray.setVisibility(View.VISIBLE);
			mCropImgSqaureLine.setVisibility(View.VISIBLE);

			break;

		case R.id.image_rotate_btn:
			rotateImage();
			break;

		case R.id.image_effect_btn:

			processImage();
			break;

		case R.id.image_red_eye_btn:

			break;

		}

		if (intent == null) {
			return;
		}
		try {
			startActivityForResult(intent, 1);
		} catch (ActivityNotFoundException e) {
			AfterUIlog.e(TAG, " Activity not found : " + e.getMessage());
		}
	}

	private void processImage() {
		HandlerThread imageEditor = new HandlerThread("image_editor");
		imageEditor.start();
		ImageHandler img = new ImageHandler(imageEditor.getLooper());
		img.sendEmptyMessage(START_EDITING_IMAGE);

	}

	class ImageHandler extends Handler {

		public ImageHandler(Looper loop) {
			super(loop);
		}

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_EDITING_IMAGE:
				handler.sendEmptyMessage(START_PROCESS_IMAGE);
				if (mImageUri == null) {
					Drawable d = mUserImage.getDrawable();
					Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),
							d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmap);
					d.draw(canvas);
					editedBitmap = ImageInfoUtils.updateHSV(bitmap);
					handler.sendEmptyMessage(ERROR_FILTERING_IMAGE);
				} else {
					Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFile(
							mImageUri.getPath(), 400, 400);
					if (bitmap != null) {
						editedBitmap = ImageInfoUtils.updateHSV(bitmap);
						handler.sendEmptyMessage(DONE_GETTING_IMAGE);
					} else {
						handler.sendEmptyMessage(ERROR_FILTERING_IMAGE);
					}
				}

				break;
			}
		}
	}

	@Override
	protected void onDestroy() {

		if (urlThread != null && urlThread.isAlive()) {
			urlThread.quit();
		}

		if (mDeamonThread != null && mDeamonThread.isAlive()) {
			mDeamonThread.quit();
		}

		super.onDestroy();
	}

}
