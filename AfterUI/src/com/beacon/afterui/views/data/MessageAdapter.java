package com.beacon.afterui.views.data;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beacon.afterui.R;

public class MessageAdapter extends BaseAdapter {

	private static final int VIEW_TYPE_LEFT = 0;

	private static final int VIEW_TYPE_RIGHT = 1;

	private static final int VIEW_TYPE_COUNT = 2;

	private LayoutInflater mInflater;

	private Context mContext;
	private int i = 1;

	private ArrayList<String> mMessage = new ArrayList<String>();

	public MessageAdapter(Context context, ArrayList<String> msg) {
		mMessage.clear();
		mMessage = msg;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mMessage.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	// @Override
	// public int getItemViewType(int position) {
	//
	// if (position > 4) {
	// return VIEW_TYPE_RIGHT;
	// } else {
	// return VIEW_TYPE_LEFT;
	// }
	// }
	//
	// @Override
	// public int getViewTypeCount() {
	// return VIEW_TYPE_COUNT;
	// }

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();

			// int type = getItemViewType(position);

			// switch (type) {
			// case VIEW_TYPE_RIGHT:
			// view =
			// mInflater.inflate(R.layout.chat_screen_left_text_item,
			// null);

			// break;
			// case VIEW_TYPE_LEFT:
			view = mInflater
					.inflate(R.layout.chat_screen_right_text_item, null);
			holder.userMessageTxt = (TextView) view.findViewById(R.id.user_msg);

			// break;
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.userMessageTxt.setText(mMessage.get(position));

		return view;
	}

	private static class ViewHolder {

		TextView userMessageTxt;

	}
}
