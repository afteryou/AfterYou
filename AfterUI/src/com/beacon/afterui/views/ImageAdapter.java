package com.beacon.afterui.views;

import com.beacon.afterui.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;

	public Integer[] mImageId = { R.drawable.user_image,
			R.drawable.user_image, R.drawable.user_image,
			R.drawable.user_image, R.drawable.user_image,
			R.drawable.user_image, R.drawable.user_image,
			R.drawable.user_image, R.drawable.user_image,
			R.drawable.user_image, R.drawable.user_image,
			R.drawable.user_image, R.drawable.user_image,
			R.drawable.user_image, R.drawable.user_image,
			R.drawable.user_image, R.drawable.user_image };

	public ImageAdapter(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		return mImageId.length;
	}

	@Override
	public Object getItem(int position) {
		return mImageId[position];
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView photoImage = new ImageView(mContext);
		photoImage.setImageResource(mImageId[position]);
		photoImage.setLayoutParams(new GridView.LayoutParams(85, 85));
		photoImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
		photoImage.setPadding(8, 8, 8, 8);
		return photoImage;
	}

}
