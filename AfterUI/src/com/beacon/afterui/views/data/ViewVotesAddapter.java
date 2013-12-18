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

public class ViewVotesAddapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;

	public ViewVotesAddapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mContext = context;
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
			Typeface typeFaceBK = Typeface.createFromAsset(
					mContext.getAssets(), "fonts/ITCAvantGardeStd-Bk.otf");

			viewHolder.mUserName = (TextView) view
					.findViewById(R.id.user_name_txt);
			viewHolder.mUserName.setTypeface(typeFaceBK);

			viewHolder.mTimeTxt = (TextView) view.findViewById(R.id.time_txt);
			viewHolder.mTimeTxt.setTypeface(typeFaceBK);

			viewHolder.mSloganTxt = (TextView) view
					.findViewById(R.id.dashboard_txt);
			viewHolder.mSloganTxt.setTypeface(typeFaceBK);

			view.setTag(viewHolder);
		}else
		{
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
