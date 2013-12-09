package com.beacon.afterui.views.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;

public class NotificationReferralsAddapter extends BaseAdapter implements
		OnClickListener {

	private LayoutInflater mInflater;

	private static final int VIEW_VOTES = 0;

	private static final int VIEW_REFERRAL = 1;

	private static final int VIEW_TYPE_COUNT = 2;

	private Context mContext;

	private static final int REFERRAL_BTN = 2;

	private int mButtonId;

	public NotificationReferralsAddapter(Context context, final int buttonId) {

		mInflater = LayoutInflater.from(context);
		mContext = context;
		mButtonId = buttonId;

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
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {

		if (position == 3) {
			return VIEW_VOTES;
		}
		return VIEW_REFERRAL;

	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder;
		int type = getItemViewType(position);
		if (view == null) {
			holder = new ViewHolder();
			switch (type) {
			case VIEW_REFERRAL:
				view = mInflater.inflate(R.layout.connects_notification_item,
						null);
				holder.connectBtn = (ImageView) view
						.findViewById(R.id.conect_green_btn);

				holder.voteBtn = (ImageView) view.findViewById(R.id.vote_btn);

				holder.userImg = (ImageView) view.findViewById(R.id.user_img);

				holder.declineBtn = (ImageView) view
						.findViewById(R.id.decline_btn);
				holder.requestUserName = (TextView) view
						.findViewById(R.id.user_name_decline);

				holder.referalUserImg = (ImageView) view
						.findViewById(R.id.referal_user_img);
				holder.referalUserName = (TextView) view
						.findViewById(R.id.referal_user_name);
				holder.referalTxt = (TextView) view
						.findViewById(R.id.referral_txt);

				holder.connectBtn.setOnClickListener(this);
				holder.voteBtn.setOnClickListener(this);
				holder.declineBtn.setOnClickListener(this);
				break;
			case VIEW_VOTES:

				view = mInflater.inflate(R.layout.view_votes_lay, null);

				if (REFERRAL_BTN == mButtonId) {
					LinearLayout lay = (LinearLayout) view
							.findViewById(R.id.referal_lay);
					lay.setVisibility(View.VISIBLE);
				}
				break;
			}
			view.setTag(holder);
		}

		holder = (ViewHolder) view.getTag();

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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.conect_green_btn:

			Toast.makeText(mContext, "Connect btn clicked ", Toast.LENGTH_SHORT)
					.show();

			break;

		case R.id.vote_btn:

			Toast.makeText(mContext, "Vote btn clicked ", Toast.LENGTH_SHORT)
					.show();

			break;

		case R.id.decline_btn:

			Toast.makeText(mContext, "Decline btn clicked ", Toast.LENGTH_SHORT)
					.show();

			break;

		}

	}

}
