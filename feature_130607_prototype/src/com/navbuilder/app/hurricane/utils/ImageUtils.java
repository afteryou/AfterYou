package com.navbuilder.app.hurricane.utils;

import android.content.Context;
import android.widget.ImageView;

import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.ui.view.imagetool.ImageCache;
import com.navbuilder.app.hurricane.ui.view.imagetool.ImageFetcher;
import com.navbuilder.app.hurricane.ui.view.imagetool.ImageResizer;

public class ImageUtils {
	private static ImageUtils mInstance;
	private Context mContext;
	
	private ImageFetcher mImageFetcher;
	private ImageResizer mImageResizer;
	public final static String CACHENAME = "interests";
	public static ImageUtils getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new ImageUtils(ctx);
		}
		return mInstance;
	}
	private ImageUtils(Context ctx) {
		mContext = ctx;
		mImageFetcher = new ImageFetcher(mContext, 240);  //FIXME need define the width and height in resource file
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mImageFetcher.setImageCache(new ImageCache(mContext, CACHENAME));
		
		mImageResizer = new ImageResizer(mContext, 240);
		mImageResizer.setLoadingImage(R.drawable.empty_photo);
	}
	
	public void resizeImage(Object data, ImageView imageView) {
		mImageResizer.loadImage(data, imageView);
	}
	
	public void loadImage(Object data, ImageView imageView) {
		mImageFetcher.loadImage(data, imageView);
	}
	
	public void loadImage(Object data) {
		mImageFetcher.loadImage(data);
	}
}
