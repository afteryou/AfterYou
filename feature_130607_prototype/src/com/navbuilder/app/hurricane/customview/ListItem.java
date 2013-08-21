package com.navbuilder.app.hurricane.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItem {
	private static final int INVALID_ID = -1;
	private Context mContext;
	private LayoutInflater mInflater;
	private int mTextViewId = INVALID_ID;
	private View mContentView;
	private int mLayoutId = INVALID_ID;
	private String text;

	public ListItem(Context ctx) {
		mContext = ctx;
	}

	public ListItem(Context ctx, int layoutId) {
		this(ctx, layoutId, INVALID_ID, null);
	}

	public ListItem(Context ctx, int layoutId, int textViewId, int textRes) {
		this(ctx, layoutId, textViewId, ctx.getResources().getString(textRes));
	}

	public ListItem(Context ctx, int layoutId, int textViewId, String text) {
		this(ctx, layoutId, textViewId, text, INVALID_ID, null);
	}

	public ListItem(Context ctx, int layoutId, int textViewId, String text, int imageViewId, int iconRes) {
		this(ctx, layoutId, textViewId, text, imageViewId, BitmapFactory.decodeResource(ctx.getResources(), iconRes));
	}

	public ListItem(Context ctx, int layoutId, int textViewId, String text, int imageViewId, Bitmap icon) {
		mContext = ctx;
		mLayoutId = layoutId;
		mTextViewId = textViewId;
		setText(textViewId, text);
		setBitmap(imageViewId, icon);
	}

	public Context getContext() {
		return mContext;
	}

	public LayoutInflater getLayoutInflater() {
		if (mInflater == null) {
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		return mInflater;
	}

	protected View onCreateView() {
		View v = null;
		if (mLayoutId > 0) {
			v = getLayoutInflater().inflate(mLayoutId, null);
		}
		return v;
	}

	/**
	 * 
	 * @return <link>View</link> created by onCreateView()
	 */
	public View getView() {
		if (mContentView == null) {
			mContentView = onCreateView();
		}
		return mContentView;
	}

	public View getView(View convertView, ViewGroup parent) {
		if(convertView != null){
			mContentView = convertView;
		}
		if (mContentView == null) {
			mContentView = onCreateView();
		}
		return mContentView;
	}

	public View getView(boolean createNew) {
		if (createNew) {
			mContentView = onCreateView();
		} else {
			getView();
		}
		return mContentView;
	}

	public void setContentView(View v) {
		mContentView = v;
	}

	public void setContentView(int resId) {
		mLayoutId = resId;
	}

	public void setTextViewId(int resId) {
		mTextViewId = resId;
	}

	public void setText(int textViewId, String text) {
		if (textViewId == INVALID_ID) {
			return;
		}
		TextView tv = (TextView) getView().findViewById(textViewId);
		mTextViewId = textViewId;
		tv.setText(text);
	}

	public void setText(int textViewId, int textId) {
		setText(textViewId, mContext.getResources().getString(textId));
	}

	public String getText(int textViewId) {
		if (textViewId == INVALID_ID) {
			return "";
		}
		TextView tv = (TextView) this.getView().findViewById(textViewId);
		return tv.getText().toString();
	}

	public String getText() {
		return getText(mTextViewId);
	}

	public void setBitmap(int imageViewId, int resId) {
		setBitmap(imageViewId, BitmapFactory.decodeResource(getContext().getResources(), resId));
	}

	public void setBitmap(int imageViewId, Bitmap bitmap) {
		if (imageViewId == INVALID_ID) {
			return;
		}
		ImageView iv = (ImageView) getView().findViewById(imageViewId);
		iv.setImageBitmap(bitmap);
	}
}
