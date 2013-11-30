package com.beacon.afterui.views.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;

public class NotificationConnectsAddapter extends BaseAdapter {

	private LayoutInflater mInflater;

	public NotificationConnectsAddapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 5;
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

			view = mInflater.inflate(R.layout.connects_notification_item, null);
			holder.connectBtn = (ImageView) view
					.findViewById(R.id.conect_green_btn);

			holder.voteBtn = (ImageView) view.findViewById(R.id.vote_btn);

			holder.userImg = (ImageView) view.findViewById(R.id.user_img);

			holder.declineBtn = (ImageView) view.findViewById(R.id.decline_btn);
			holder.requestUserName = (TextView) view
					.findViewById(R.id.user_name_decline);

			holder.referalUserImg = (ImageView) view
					.findViewById(R.id.referal_user_img);
			holder.referalUserName = (TextView) view
					.findViewById(R.id.referal_user_name);
			holder.referalTxt = (TextView) view.findViewById(R.id.referral_txt);

			holder.referalUserImg.setVisibility(View.GONE);
			holder.referalUserName.setVisibility(View.GONE);
			holder.referalTxt.setVisibility(View.GONE);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		return view;
	}

	private static class ViewHolder {
		private TextView requestUserName;
		private ImageView userImg;
		private ImageView connectBtn;
		private ImageView voteBtn;
		private ImageView declineBtn;
		private ImageView referalUserImg;
		private TextView referalUserName;
		private TextView referalTxt;
	}

}
