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

public class ViewVotesAddapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private Typeface mITCAvantGardeStdBkFont;

	public ViewVotesAddapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(mContext,
				FontUtils.ITC_AVANT_GARDE_STD_BK);
	}

	@Override
	public int getCount() {
		return 20;
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

		ViewHolder viewHolder;

		if (view == null) {
			viewHolder = new ViewHolder();

			view = mInflater.inflate(R.layout.voting_006_voted_hot, null);

			viewHolder.mUserName = (TextView) view
					.findViewById(R.id.user_name_txt);
			viewHolder.mUserName.setTypeface(mITCAvantGardeStdBkFont);

			viewHolder.mTimeTxt = (TextView) view.findViewById(R.id.time_txt);
			viewHolder.mTimeTxt.setTypeface(mITCAvantGardeStdBkFont);

			viewHolder.mSloganTxt = (TextView) view
					.findViewById(R.id.dashboard_txt);
			viewHolder.mSloganTxt.setTypeface(mITCAvantGardeStdBkFont);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		return view;
	}

	private static class ViewHolder {

		private TextView mUserName;
		private TextView mTimeTxt;
		private TextView mSloganTxt;
		private ImageView mStatusImg;

	}

}
