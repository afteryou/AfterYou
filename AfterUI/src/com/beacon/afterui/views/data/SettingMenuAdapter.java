package com.beacon.afterui.views.data;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;

public class SettingMenuAdapter extends BaseAdapter {

	private String[] mSettingItemTxt;
	private int[] mImages;
	private Context mContext;
	private Typeface mITCAvantGardeStdBkFont;

	public SettingMenuAdapter(Context context, String[] text, int[] images) {

		mContext = context;
		mSettingItemTxt = text;
		mImages = images;
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(mContext,
				FontUtils.ITC_AVANT_GARDE_STD_BK);
	}

	@Override
	public int getCount() {
		return mSettingItemTxt.length;
	}

	@Override
	public Object getItem(int position) {
		return mSettingItemTxt[position];
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.setting_menu_item, null);
			holder.txtTitle = (TextView) view
					.findViewById(R.id.setting_item_txt);
			holder.txtTitle.setTypeface(mITCAvantGardeStdBkFont);
			holder.imageView = (ImageView) view.findViewById(R.id.setting_img);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.txtTitle.setText(mSettingItemTxt[position]);
		holder.imageView.setImageResource(mImages[position]);
		return view;
	}

	private class ViewHolder {
		private ImageView imageView;
		private TextView txtTitle;
	}

}
