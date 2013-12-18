package com.beacon.afterui.views.data;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;

public class NotificationConnectsAddapter extends BaseAdapter implements
		OnClickListener {
	private int flag = 0;

	private Context mContext;

	private LayoutInflater mInflater;
	private static final int VIEW_VOTES = 0;

	private static final int VIEW_CONNECTS = 1;

	private static final int VIEW_TYPE_COUNT = 2;

	private Typeface mTypeFace;

	private TextView mViewVotesUserName;

	public NotificationConnectsAddapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mTypeFace = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/ITCAvantGardeStd-Bk.otf");
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
	public int getItemViewType(int position) {

		if (position == 4) {
			return VIEW_VOTES;
		}
		return VIEW_CONNECTS;

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
			case VIEW_CONNECTS:
				view = mInflater.inflate(R.layout.connects_notification_item,
						null);
				holder.connectBtn = (TextView) view
						.findViewById(R.id.conect_green_btn);
				holder.connectBtn.setTypeface(mTypeFace);

				holder.voteBtn = (TextView) view.findViewById(R.id.vote_btn);
				holder.voteBtn.setTypeface(mTypeFace);

				holder.declineBtn = (TextView) view
						.findViewById(R.id.decline_btn);
				holder.declineBtn.setTypeface(mTypeFace);

				holder.userImg = (ImageView) view.findViewById(R.id.user_img);

				holder.requestUserName = (TextView) view
						.findViewById(R.id.user_name_decline);
				holder.requestUserName.setTypeface(mTypeFace);

				holder.referalUserImg = (ImageView) view
						.findViewById(R.id.referal_user_img);

				holder.referalUserName = (TextView) view
						.findViewById(R.id.referal_user_name);
				holder.referalUserName.setTypeface(mTypeFace);

				holder.referalTxt = (TextView) view
						.findViewById(R.id.referral_txt);
				holder.referalTxt.setTypeface(mTypeFace);

				holder.referalUserImg.setVisibility(View.GONE);
				holder.referalUserName.setVisibility(View.GONE);
				holder.referalTxt.setVisibility(View.GONE);

				holder.connectBtn.setOnClickListener(this);
				holder.voteBtn.setOnClickListener(this);
				holder.declineBtn.setOnClickListener(this);

				break;
			case VIEW_VOTES:
				view = mInflater.inflate(R.layout.view_votes_lay, null);
				holder.viewVotesUserName = (TextView) view
						.findViewById(R.id.user_name_view_votes);
				holder.viewVotesUserName.setTypeface(mTypeFace);

				holder.view_votes_txt = (TextView) view
						.findViewById(R.id.view_votes_txt);
				holder.view_votes_txt.setTypeface(mTypeFace);

				holder.voting_in_progress_txt = (TextView) view
						.findViewById(R.id.voting_in_progress_txt);
				holder.voting_in_progress_txt.setTypeface(mTypeFace);

				holder.time_txt = (TextView) view.findViewById(R.id.time_txt);
				holder.time_txt.setTypeface(mTypeFace);
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
		private TextView connectBtn;
		private TextView voteBtn;
		private TextView declineBtn;
		private ImageView referalUserImg;
		private TextView referalUserName;
		private TextView referalTxt;
		private TextView view_votes_txt;
		private TextView voting_in_progress_txt;
		private TextView time_txt;
		private TextView viewVotesUserName;
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
