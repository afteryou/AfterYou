package com.beacon.afterui.views.data;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;

public class NotificationVoteAddapter extends BaseAdapter implements
		OnClickListener {

	private LayoutInflater mInflater;
	private Context mContext;
	private Drawable mDeclineBtn;
	private Drawable mHotGreenBtn;
	private Drawable mNoVoteBtn;
	private Typeface mTypeFace;

	public NotificationVoteAddapter(Context context) {

		mInflater = LayoutInflater.from(context);
		mContext = context;
		initTempImages();
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
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();

			view = mInflater.inflate(R.layout.connects_notification_item, null);

			holder.hotBtn = (TextView) view.findViewById(R.id.conect_green_btn);
			holder.hotBtn.setTypeface(mTypeFace);

			holder.notHotBtn = (TextView) view.findViewById(R.id.vote_btn);
			holder.notHotBtn.setTypeface(mTypeFace);

			holder.noVoteBtn = (TextView) view.findViewById(R.id.decline_btn);
			holder.noVoteBtn.setTypeface(mTypeFace);

			holder.userImg = (ImageView) view.findViewById(R.id.user_img);

			holder.requestUserName = (TextView) view
					.findViewById(R.id.user_name_decline);
			holder.requestUserName.setTypeface(mTypeFace);

			holder.referalUserImg = (ImageView) view
					.findViewById(R.id.referal_user_img);

			holder.referalUserName = (TextView) view
					.findViewById(R.id.referal_user_name);
			holder.referalUserName.setTypeface(mTypeFace);

			holder.voteTxt = (TextView) view.findViewById(R.id.referral_txt);
			holder.voteTxt.setTypeface(mTypeFace);
			holder.voteTxt.setText("Needs your vote");

			holder.noVoteBtn.setCompoundDrawables(null, mNoVoteBtn, null, null);
			holder.hotBtn.setCompoundDrawables(null, mHotGreenBtn, null, null);
			holder.hotBtn.setText("hot");
			holder.notHotBtn
					.setCompoundDrawables(null, mDeclineBtn, null, null);
			holder.notHotBtn.setText("not hot");
			holder.notHotBtn.setTextColor(mContext.getResources().getColor(
					R.color.red_color));
			holder.noVoteBtn.setText("no vote");
			holder.noVoteBtn.setTextColor(mContext.getResources().getColor(
					R.color.grey));

			holder.hotBtn.setOnClickListener(this);
			holder.notHotBtn.setOnClickListener(this);
			holder.noVoteBtn.setOnClickListener(this);
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		return view;
	}

	private void initTempImages() {
		mDeclineBtn = mContext.getResources().getDrawable(
				R.drawable.decline_btn);
		mDeclineBtn.setBounds(0, 0, mDeclineBtn.getIntrinsicWidth(),
				mDeclineBtn.getIntrinsicHeight());

		mHotGreenBtn = mContext.getResources().getDrawable(
				R.drawable.hot_green_btn);
		mHotGreenBtn.setBounds(0, 0, mHotGreenBtn.getIntrinsicWidth(),
				mHotGreenBtn.getIntrinsicHeight());

		mNoVoteBtn = mContext.getResources()
				.getDrawable(R.drawable.no_vote_btn);
		mNoVoteBtn.setBounds(0, 0, mNoVoteBtn.getIntrinsicWidth(),
				mNoVoteBtn.getIntrinsicHeight());

	}

	private static class ViewHolder {
		private TextView requestUserName;
		private ImageView userImg;
		private TextView hotBtn;
		private TextView notHotBtn;
		private TextView noVoteBtn;
		private ImageView referalUserImg;
		private TextView referalUserName;
		private TextView voteTxt;
		private TextView hotTxt;
		private TextView notHotTxt;
		private TextView noVoteTxt;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.conect_green_btn:

			Toast.makeText(mContext, "Hot btn clicked ", Toast.LENGTH_SHORT)
					.show();

			break;

		case R.id.vote_btn:

			Toast.makeText(mContext, "Not hot btn clicked ", Toast.LENGTH_SHORT)
					.show();

			break;

		case R.id.decline_btn:

			Toast.makeText(mContext, "No vote btn clicked ", Toast.LENGTH_SHORT)
					.show();

			break;

		}

	}

}
