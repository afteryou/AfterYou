package com.beacon.afterui.views.data;

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

public class TermsPoliciesAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInfLayoutInflater;
	private int[] mImages;
	private String[] mHeadingItemTxt;
	private String[] mSubHeadingItemTxt;
	private Typeface mITCAvantGardeStdBkFont;

	public TermsPoliciesAdapter(Context context, int[] images,
			String[] headingItemTxt, String[] subHeadingItemTxt) {

		mContext = context;
		mInfLayoutInflater = LayoutInflater.from(mContext);
		mImages = images;
		mHeadingItemTxt = headingItemTxt;
		mSubHeadingItemTxt = subHeadingItemTxt;
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(mContext,
				FontUtils.ITC_AVANT_GARDE_STD_BK);

	}

	@Override
	public int getCount() {
		return mImages.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = mInfLayoutInflater.inflate(R.layout.terms_policies_item,
					null);
			holder.headingTxt = (TextView) view.findViewById(R.id.head_txt);
			holder.subHeadingTxt = (TextView) view
					.findViewById(R.id.sub_head_txt);
			holder.headingTxt.setTypeface(mITCAvantGardeStdBkFont);
			holder.subHeadingTxt.setTypeface(mITCAvantGardeStdBkFont);
			holder.termImg = (ImageView) view.findViewById(R.id.terms_img);

			view.setTag(holder);
		} else {

			holder = (ViewHolder) view.getTag();
		}
		holder.termImg.setImageResource(mImages[position]);
		holder.headingTxt.setText(mHeadingItemTxt[position]);
		holder.subHeadingTxt.setText(mSubHeadingItemTxt[position]);

		return view;
	}

	private class ViewHolder {

		private ImageView termImg;
		private TextView headingTxt;
		private TextView subHeadingTxt;

	}
}
