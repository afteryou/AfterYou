package com.beacon.afterui.views.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;

public class NotificationMessagesAddapter extends BaseAdapter {

	private LayoutInflater mInflater;

	public NotificationMessagesAddapter(Context context) {

		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 10;
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

			view = mInflater.inflate(R.layout.sliding_menu_item, null);
			holder.userMsgImg = (ImageView) view
					.findViewById(R.id.dashboard_img);
			holder.userMsgTxt = (TextView) view
					.findViewById(R.id.dashboard_txt);

			holder.userMsgTxt.setText("Hello Sir how are you?");
			holder.userMsgImg.setImageResource(R.drawable.sample_img);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		return view;
	}

	private static class ViewHolder {
		private TextView userMsgTxt;
		private ImageView userMsgImg;
	}

}
