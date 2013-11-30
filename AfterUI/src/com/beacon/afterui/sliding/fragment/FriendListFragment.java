package com.beacon.afterui.sliding.fragment;

import android.R.color;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.views.MainActivity;

public class FriendListFragment extends Fragment implements OnClickListener,
		FragmentLifecycle {
	private String[] mFriendName;
	private int[] mFriendImage;
	private ImageView mAfterYouFrnd;
	private ImageView mContacts;
	private ImageView mFacebookFrnd;
	private ImageView mTwitterFrnd;
	private ListView mFriendList;
	private Context mContext;
	private Boolean isBacking = false;

	public FriendListFragment() {
	}

	public FriendListFragment(Context context) {

		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View friendListView = inflater.inflate(R.layout.voting_002, null);
		mAfterYouFrnd = (ImageView) friendListView
				.findViewById(R.id.after_you_friends_btn);
		mContacts = (ImageView) friendListView.findViewById(R.id.contacts_btn);
		mFacebookFrnd = (ImageView) friendListView
				.findViewById(R.id.facebook_voting_btn);
		mTwitterFrnd = (ImageView) friendListView
				.findViewById(R.id.twitter_btn);
		mFriendList = (ListView) friendListView.findViewById(R.id.freind_list);

		mAfterYouFrnd.setOnClickListener(this);
		mContacts.setOnClickListener(this);
		mFacebookFrnd.setOnClickListener(this);
		mTwitterFrnd.setOnClickListener(this);

		return friendListView;
	}

	@Override
	public void onClick(View v) {
		mFriendName = null;
		mFriendImage = null;
		switch (v.getId()) {
		case R.id.after_you_friends_btn:

			mFriendName = new String[] { "After you", "facebook", "Twitter",
					"Contacts" };
			mFriendImage = new int[] { R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder };
			mFriendList.setAdapter(new FriendListAdapter(mFriendName,
					mFriendImage));
			break;
		case R.id.contacts_btn:
			mFriendName = new String[] { "Rajiv", "Sonia", "Priyanka", "Rahul" };
			mFriendImage = new int[] { R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder };
			mFriendList.setAdapter(new FriendListAdapter(mFriendName,
					mFriendImage));

			break;
		case R.id.facebook_voting_btn:
			mFriendName = new String[] { "Salman", "Shahrukh", "Amir", "Saif" };
			mFriendImage = new int[] { R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder };
			mFriendList.setAdapter(new FriendListAdapter(mFriendName,
					mFriendImage));

			break;
		case R.id.twitter_btn:
			mFriendName = new String[] { "Karina", "Katrina", "Asin", "Deepika" };
			mFriendImage = new int[] { R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder };
			mFriendList.setAdapter(new FriendListAdapter(mFriendName,
					mFriendImage));

			break;

		}

	}

	private class FriendListAdapter extends BaseAdapter {
		private String[] friendName;
		private int[] image;

		public FriendListAdapter(String[] list, int[] userImage) {
			this.friendName = list;
			image = userImage;
		}

		@Override
		public int getCount() {
			return friendName.length;
		}

		@Override
		public Object getItem(int position) {
			return friendName[position];
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder holder;
			RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if (view == null) {
				holder = new ViewHolder();
				view = getActivity().getLayoutInflater().inflate(
						R.layout.sliding_menu_item, null);
				holder.userImage = (ImageView) view
						.findViewById(R.id.dashboard_img);
				holder.userName = (TextView) view
						.findViewById(R.id.dashboard_txt);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.userName.setText(friendName[position]);
			holder.userName.setTextColor(mContext.getResources().getColor(
					R.color.brown_background));
			holder.userName.setTextSize(16f);
			llp.setMargins(15, 0, 0, 0);
			holder.userImage.setLayoutParams(llp);
			holder.userImage.setImageResource(image[position]);

			return view;
		}
	}

	private class ViewHolder {

		private ImageView userImage;
		private TextView userName;

	}

	@Override
	public void onFragmentPause() {

	}

	@Override
	public void onFragmentResume() {

	}

	private void applyBackAnimation() {
		AnimatorSet fideOutMap = new AnimatorSet();
		fideOutMap.setDuration(250);
		fideOutMap.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator anim) {
				FragmentHelper.popFragment(getActivity());
				isBacking = false;
			}
		});

		fideOutMap.start();
	}

	@Override
	public boolean onBack() {
		if (!isBacking) {
			isBacking = true;
			((MainActivity) getActivity()).updateToMainScreenActionBar();
			applyBackAnimation();
		}
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

}
