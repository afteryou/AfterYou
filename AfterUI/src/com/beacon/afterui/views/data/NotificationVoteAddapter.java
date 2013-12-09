package com.beacon.afterui.views.data;

import android.content.Context;
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

	public NotificationVoteAddapter(Context context) {

		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();

			view = mInflater.inflate(R.layout.connects_notification_item, null);
			holder.hotBtn = (ImageView) view
					.findViewById(R.id.conect_green_btn);

			holder.notHotBtn = (ImageView) view.findViewById(R.id.vote_btn);

			holder.userImg = (ImageView) view.findViewById(R.id.user_img);

			holder.noVoteBtn = (ImageView) view.findViewById(R.id.decline_btn);
			holder.hotTxt = (TextView) view.findViewById(R.id.connect_txt);
			holder.notHotTxt = (TextView) view.findViewById(R.id.vote_txt);
			holder.noVoteTxt = (TextView) view.findViewById(R.id.decline_txt);

			holder.requestUserName = (TextView) view
					.findViewById(R.id.user_name_decline);

			holder.referalUserImg = (ImageView) view
					.findViewById(R.id.referal_user_img);
			holder.referalUserName = (TextView) view
					.findViewById(R.id.referal_user_name);
			holder.voteTxt = (TextView) view.findViewById(R.id.referral_txt);
			holder.voteTxt.setText("Needs your vote");
			holder.noVoteBtn.setImageResource(R.drawable.no_vote_btn);
			holder.hotBtn.setImageResource(R.drawable.hot_green_btn);
			holder.notHotBtn.setImageResource(R.drawable.decline_btn);
			holder.hotTxt.setText("hot");
			holder.notHotTxt.setText("not hot");
			holder.notHotTxt.setTextColor(mContext.getResources().getColor(
					R.color.red_color));
			holder.noVoteTxt.setText("no vote");
			holder.noVoteTxt.setTextColor(mContext.getResources().getColor(
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

	private static class ViewHolder {
		private TextView requestUserName;
		private ImageView userImg;
		private ImageView hotBtn;
		private ImageView notHotBtn;
		private ImageView noVoteBtn;
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
