package com.beacon.afterui.sliding.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;

public class ChatScreenFragment extends BaseActivity implements OnClickListener {

	private static final String TAG = ChatScreenFragment.class.getSimpleName();
	private Button mPostBtn;
	private EditText mMessageEditText;
	private ListView mMessageList;
	private ChatMessageAdapter mChatMessageAdpter;
	private String[] mUserMsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_screen);
		setBehindLeftContentView(R.layout.chat_screen);
        setBehindRightContentView(R.layout.chat_screen);
		mPostBtn = (Button) findViewById(R.id.post_btn);
		mMessageEditText = (EditText) findViewById(R.id.write_msg_txt);
		mMessageList = (ListView) findViewById(R.id.chat_msg_list);
		mChatMessageAdpter = new ChatMessageAdapter();
		for (int i = 1; i <= 50; i++) {
			mChatMessageAdpter.addLeftMsgText("Friend Message");

			if (i % 2 == 0) {
				mChatMessageAdpter.addRightMsgText("User Message");
			}

		}
		mMessageList.setAdapter(mChatMessageAdpter);

		mPostBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.post_btn:
			// getMessage();

			break;

		}

	}

	// private void getMessage() {
	// String msg = mMessageEditText.getText().toString();
	//
	// if (mMessageEditText.length() > 0) {
	//
	// mChatMessageAdpter = new ChatMessage(msg);
	// mMessageList.setAdapter(mChatMessageAdpter);
	// mChatMessageAdpter.notifyDataSetChanged();
	// } else {
	// Log.d(TAG, "Write message");
	// }
	//
	// }

	private class ChatMessageAdapter extends BaseAdapter {

		private ArrayList<String> leftText = new ArrayList<String>();
		private ArrayList<Integer> rightText = new ArrayList<Integer>();
		private static final int LEFT_LAYOUT = 0;
		private static final int RIGHT_LAYOUT = 1;
		private static final int NO_OF_LAYOUT = RIGHT_LAYOUT + 1;

		private void addLeftMsgText(String msg) {
			leftText.add(msg);
			notifyDataSetChanged();

		}

		private void addRightMsgText(String msg) {
			leftText.add(msg);
			rightText.add(leftText.size() - 1);
			notifyDataSetChanged();

		}

		@Override
		public int getItemViewType(int position) {
			return rightText.contains(position) ? RIGHT_LAYOUT : LEFT_LAYOUT;
		}

		@Override
		public int getViewTypeCount() {
			return NO_OF_LAYOUT;
		}

		@Override
		public int getCount() {
			return leftText.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder viewholder = null;
			Log.d(TAG, "View :" + view);
			int layoutType = getItemViewType(position);
			if (view == null) {
				viewholder = new ViewHolder();
				switch (layoutType) {

				case LEFT_LAYOUT:
					view = getLayoutInflater().inflate(
							R.layout.chat_screen_left_text_item, null);
					viewholder.text = (TextView) view
							.findViewById(R.id.friend_msg);
					break;
				case RIGHT_LAYOUT:
					view = getLayoutInflater().inflate(
							R.layout.chat_screen_right_text_item, null);
					viewholder.text = (TextView) view
							.findViewById(R.id.user_msg);

					break;

				}
				view.setTag(viewholder);

				Log.d(TAG, "View :" + view);

			} else {
				viewholder = (ViewHolder) view.getTag();
			}

			viewholder.text.setText(leftText.get(position));

			return view;
		}
	}

	private static class ViewHolder {
		TextView text;
	}

}
