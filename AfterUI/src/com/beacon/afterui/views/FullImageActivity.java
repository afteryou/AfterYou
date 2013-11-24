package com.beacon.afterui.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

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
	private Button mDoneBtn;
	private Button mCancelBtn;
	private ImageButton mImageRotateBtn;
	private ImageButton mImageCropBtn;
	private ImageButton mImageEffectBtn;
	private ImageButton mImageRedEyeBtn;
	
	private Bitmap enhancedBitmap;
	private long mId;
	
	Uri uri = null;
	
	int width = -1,height = -1;

	private ImageButton gray_btn, sepia_btn, sharp_btn, color_btn,
			contrast_btn, emboss_btn;
	
	Bitmap actualBitmap,actualGrayBitmap,actualSepiaBitmap,actualSharpBitmap,actualColorBitmap,actualContrastBitmap,actualEmbossBitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.full_image_activity);
		setBehindLeftContentView(R.layout.full_image_activity);
		setBehindRightContentView(R.layout.full_image_activity);
		mFullSizeImage = (ImageView) findViewById(R.id.photo_of_user_full_image);
		mDoneBtn = (Button) findViewById(R.id.done_btn_full_image_activity);
		mCancelBtn = (Button) findViewById(R.id.cancel_btn_full_image_activity);

		mImageCropBtn = (ImageButton) findViewById(R.id.image_crop_btn);
		mImageRotateBtn = (ImageButton) findViewById(R.id.image_rotate_btn);
		mImageEffectBtn = (ImageButton) findViewById(R.id.image_effect_btn);
		mImageRedEyeBtn = (ImageButton) findViewById(R.id.image_red_eye_btn);

		mImageCropBtn.setOnClickListener(this);
		mImageRotateBtn.setOnClickListener(this);
		mImageEffectBtn.setOnClickListener(this);
		mImageRedEyeBtn.setOnClickListener(this);

		Button doneTxt = (Button) findViewById(R.id.done_btn_full_image_activity);
		Typeface typeFaceSemiBold = Typeface.createFromAsset(getAssets(),
				"fonts/MyriadPro-Semibold.otf");
		doneTxt.setTypeface(typeFaceSemiBold);

		mId = getIntent().getLongExtra(ID, -1);
		mImagePath = ImageInfoUtils.getPhotoPath(FullImageActivity.this,
				String.valueOf(mId));
		uri = Uri.parse(mImagePath);
		mFullSizeImage.setImageURI(uri);
		mDoneBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);

		getImagePreviews(uri);
		

	}

	private void getImageEffect(Uri uri) {
		if(uri != null && width > -1 && height > -1)
		{
			new PhotoEnhancer(uri, width, height).execute(new Void[]{});
			new ActualGrayPhotoEffects(uri, width, height)
			.execute(new Uri[] { uri });
			new ActualSepiaPhotoEffects(uri, width, height)
			.execute(new Uri[] { uri });
			new ActualSharpPhotoEffects(uri, width, height)
			.execute(new Uri[] { uri });
			new ActualColorPhotoEffects(uri, width, height)
			.execute(new Uri[] { uri });
			new ActualContrastPhotoEffects(uri, width, height)
			.execute(new Uri[] { uri });
			new ActualEmbossPhotoEffects(uri, width, height)
			.execute(new Uri[] { uri });
			
		}
	}

	private void getImagePreviews(Uri uri) {
		gray_btn = (ImageButton) findViewById(R.id.gray_button);
		gray_btn.setOnClickListener(this);
		sepia_btn = (ImageButton) findViewById(R.id.sepia_button);
		sepia_btn.setOnClickListener(this);
		sharp_btn = (ImageButton) findViewById(R.id.sharp_button);
		sharp_btn.setOnClickListener(this);
		color_btn = (ImageButton) findViewById(R.id.color_button);
		color_btn.setOnClickListener(this);
		contrast_btn = (ImageButton) findViewById(R.id.contrast_button);
		contrast_btn.setOnClickListener(this);
		emboss_btn = (ImageButton) findViewById(R.id.emboss_button);
		emboss_btn.setOnClickListener(this);

		new PhotoEffects(uri, gray_btn.getLayoutParams().width, gray_btn.getLayoutParams().height)
				.execute(new Uri[] { uri });
		
	}
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		if(hasFocus)
		{
			width=mFullSizeImage.getWidth();
			height=mFullSizeImage.getHeight();
			getImageEffect(uri);
		}
	}
	
	private int calculateInSampleSize(Options options, int reqWidth,int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int sampleSize = 1;
		
		if(height > reqHeight || width > reqWidth)
		{
			final int halfHeight = height/2;
			final int halfWidth = width/2;
			
			while((halfHeight/sampleSize) > reqHeight && (halfWidth/sampleSize) > reqWidth)
			{
				sampleSize *= 2;
			}
		}
		
		return sampleSize;
	}
	
	class ActualGrayPhotoEffects extends AsyncTask<Uri, Integer, Void> {
		int width, height;
		Uri uri;
		Bitmap testBitmap,grayBitmap;

		public ActualGrayPhotoEffects(Uri uri, int width, int height) {
			this.width = width;
			this.height = height;
			this.uri = uri;

		}

		@Override
		public void onPreExecute() {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(uri.getPath(), options);
			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;
			testBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
		}

		@Override
		protected Void doInBackground(Uri... params) {

			grayBitmap = ImageFilter.doGreyscale(testBitmap);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			actualGrayBitmap = grayBitmap;
		}

	}
	
	class ActualSepiaPhotoEffects extends AsyncTask<Uri, Integer, Void> {
		int width, height;
		Uri uri;
		Bitmap testBitmap,sepiaBitmap;

		public ActualSepiaPhotoEffects(Uri uri, int width, int height) {
			this.width = width;
			this.height = height;
			this.uri = uri;

		}

		@Override
		public void onPreExecute() {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(uri.getPath(), options);
			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;
			testBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
		}

		@Override
		protected Void doInBackground(Uri... params) {

			sepiaBitmap = ImageFilter.createSepiaToningEffect(testBitmap, 100,
					1, 1, 1.5);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			actualSepiaBitmap = sepiaBitmap;
			
		}

	}
	
	class ActualSharpPhotoEffects extends AsyncTask<Uri, Integer, Void> {
		int width, height;
		Uri uri;
		Bitmap testBitmap,sharpBitmap;

		public ActualSharpPhotoEffects(Uri uri, int width, int height) {
			this.width = width;
			this.height = height;
			this.uri = uri;

		}

		@Override
		public void onPreExecute() {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(uri.getPath(), options);
			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;
			testBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
		}

		@Override
		protected Void doInBackground(Uri... params) {

			sharpBitmap = ImageFilter.sharpen(testBitmap, 100);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			actualSharpBitmap = sharpBitmap;
		}

	}
	class ActualColorPhotoEffects extends AsyncTask<Uri, Integer, Void> {
		int width, height;
		Uri uri;
		Bitmap testBitmap,colorBitmap;

		public ActualColorPhotoEffects(Uri uri, int width, int height) {
			this.width = width;
			this.height = height;
			this.uri = uri;

		}

		@Override
		public void onPreExecute() {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(uri.getPath(), options);
			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;
			testBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
		}

		@Override
		protected Void doInBackground(Uri... params) {

			colorBitmap = ImageFilter.decreaseColorDepth(testBitmap, 64);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			actualColorBitmap = colorBitmap;
		}

	}
	
	
	class ActualContrastPhotoEffects extends AsyncTask<Uri, Integer, Void> {
		int width, height;
		Uri uri;
		Bitmap testBitmap,contrastBitmap;

		public ActualContrastPhotoEffects(Uri uri, int width, int height) {
			this.width = width;
			this.height = height;
			this.uri = uri;

		}

		@Override
		public void onPreExecute() {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(uri.getPath(), options);
			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;
			testBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
		}

		@Override
		protected Void doInBackground(Uri... params) {

			contrastBitmap = ImageFilter.createContrast(testBitmap, 100);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			actualContrastBitmap = contrastBitmap;
		}

	}
	
	
	class ActualEmbossPhotoEffects extends AsyncTask<Uri, Integer, Void> {
		int width, height;
		Uri uri;
		Bitmap testBitmap,embossBitmap;

		public ActualEmbossPhotoEffects(Uri uri, int width, int height) {
			this.width = width;
			this.height = height;
			this.uri = uri;

		}

		@Override
		public void onPreExecute() {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(uri.getPath(), options);
			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;
			testBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
		}

		@Override
		protected Void doInBackground(Uri... params) {

			embossBitmap = ImageFilter.emboss(testBitmap);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			actualEmbossBitmap = embossBitmap;
		}

	}
	
	
	class PhotoEnhancer extends AsyncTask<Void, Integer, Void>
	{
		
		Uri uri;
		int width,height;
		Bitmap testBitmap;
		
		public PhotoEnhancer(Uri uri,int width, int height)
		{
			this.uri = uri;
			this.width = width;
			this.height = height;
		}
		
		@Override
		protected void onPreExecute()
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(uri.getPath(), options);
			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;
			testBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
		}

		@Override
		protected Void doInBackground(Void... params) {
			testBitmap = ImageInfoUtils.updateHSV(testBitmap);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			enhancedBitmap = testBitmap;
		}
		
	}

	class PhotoEffects extends AsyncTask<Uri, Integer, Void> {
		int width, height;
		Uri uri;
		Bitmap testBitmap,grayBitmap,sepiaBitmap,sharpBitmap,colorBitmap,contrastBitmap,embossBitmap;

		public PhotoEffects(Uri uri, int width, int height) {
			this.width = width;
			this.height = height;
			this.uri = uri;

		}

		@Override
		public void onPreExecute() {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(uri.getPath(), options);
			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;
			testBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
		}

		@Override
		protected Void doInBackground(Uri... params) {

			grayBitmap = ImageFilter.doGreyscale(testBitmap);
			sepiaBitmap = ImageFilter.createSepiaToningEffect(testBitmap, 100,
					1, 1, 1.5);
			sharpBitmap = ImageFilter.sharpen(testBitmap, 100);
			colorBitmap = ImageFilter.decreaseColorDepth(testBitmap, 64);
			contrastBitmap = ImageFilter.createContrast(testBitmap, 100);
			embossBitmap = ImageFilter.emboss(testBitmap);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			gray_btn.setImageBitmap(grayBitmap);
			sepia_btn.setImageBitmap(sepiaBitmap);
			sharp_btn.setImageBitmap(sharpBitmap);
			color_btn.setImageBitmap(colorBitmap);
			contrast_btn.setImageBitmap(contrastBitmap);
			emboss_btn.setImageBitmap(embossBitmap);
		}

	}
	
	private void setVisibilityEffectsBtn(int visibility)
	{
		gray_btn.setVisibility(visibility);
		sepia_btn.setVisibility(visibility);
		sharp_btn.setVisibility(visibility);
		color_btn.setVisibility(visibility);
		contrast_btn.setVisibility(visibility);
		emboss_btn.setVisibility(visibility);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.image_rotate_btn:
			Bitmap bit = ImageInfoUtils.rotateToPortrait(((BitmapDrawable) mFullSizeImage.getDrawable()).getBitmap(),
	                ExifInterface.ORIENTATION_ROTATE_90);
			mFullSizeImage.setImageBitmap(bit);
			break;
			
		case R.id.image_effect_btn:
			if(enhancedBitmap != null)
			{
				mFullSizeImage.setImageBitmap(enhancedBitmap);
			}
			else{
				
			}
			break;
			
		case R.id.image_red_eye_btn:
			if(gray_btn.getVisibility() == View.VISIBLE)
			{
				setVisibilityEffectsBtn(View.GONE);
			}
			else{
				setVisibilityEffectsBtn(View.VISIBLE);
			}
			break;
			
		case R.id.gray_button:
			if(actualGrayBitmap != null)
			{
				mFullSizeImage.setImageBitmap(actualGrayBitmap);
			}
			else{
				
			}
			break;
			
		case R.id.sharp_button:
			if(actualSharpBitmap != null)
			{
				mFullSizeImage.setImageBitmap(actualSharpBitmap);
			}
			else{
				
			}
			break;
			
		case R.id.sepia_button:
			if(actualSepiaBitmap != null)
			{
				mFullSizeImage.setImageBitmap(actualSepiaBitmap);
			}
			else{
				
			}
			break;
			
		case R.id.color_button:
			if(actualColorBitmap != null)
			{
				mFullSizeImage.setImageBitmap(actualColorBitmap);
			}
			else{
				
			}
			break;
			
		case R.id.contrast_button:
			if(actualContrastBitmap != null)
			{
				mFullSizeImage.setImageBitmap(actualContrastBitmap);
			}
			else{
				
			}
			break;
			
		case R.id.emboss_button:
			if(actualEmbossBitmap != null)
			{
				mFullSizeImage.setImageBitmap(actualEmbossBitmap);
			}
			else{
				
			}
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
