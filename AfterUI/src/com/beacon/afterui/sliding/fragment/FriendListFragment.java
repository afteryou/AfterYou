package com.beacon.afterui.sliding.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.views.MainActivity;

public class FriendListFragment extends Fragment implements OnClickListener,
		FragmentLifecycle, OnItemClickListener {
	private String[] mFriendName;
	private int[] mFriendImage;
	private ImageView mAfterYouFrnd;
	private ImageView mContacts;
	private ImageView mFacebookFrnd;
	private ImageView mTwitterFrnd;
	private ListView mFriendList;
	private Context mContext;
	private Boolean isBacking = false;
	private static final int AFTER_YOU_BTN = 1;
	private static final int CONTACTS_BTN = 2;
	private static final int FACEBOOK_BTN = 3;
	private static int mButtonId;

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
		TextView done_btn = (TextView) friendListView
				.findViewById(R.id.voting_done_btn);
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
		mFriendList.setOnItemClickListener(this);
		done_btn.setOnClickListener(this);

		return friendListView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		Fragment confirmFragment = new VoteConfirm(mContext);
		if (mButtonId == AFTER_YOU_BTN) {

			Toast.makeText(mContext, "Item clicked " + position,
					Toast.LENGTH_SHORT).show();

			FragmentHelper.gotoFragment(getActivity(), FriendListFragment.this,
					confirmFragment, bundle);

		} else if (mButtonId == CONTACTS_BTN) {

			Toast.makeText(mContext, "Item clicked " + position,
					Toast.LENGTH_SHORT).show();

			FragmentHelper.gotoFragment(getActivity(), FriendListFragment.this,
					confirmFragment, bundle);

		} else if (mButtonId == FACEBOOK_BTN) {

			Toast.makeText(mContext, "Item clicked " + position,
					Toast.LENGTH_SHORT).show();

			FragmentHelper.gotoFragment(getActivity(), FriendListFragment.this,
					confirmFragment, bundle);

		} else {
			Toast.makeText(mContext, "Item clicked " + position,
					Toast.LENGTH_SHORT).show();
			FragmentHelper.gotoFragment(getActivity(), FriendListFragment.this,
					confirmFragment, bundle);
		}

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
			mButtonId = 1;
			break;
		case R.id.contacts_btn:
			mFriendName = new String[] { "Rajiv", "Sonia", "Priyanka", "Rahul" };
			mFriendImage = new int[] { R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder };
			mFriendList.setAdapter(new FriendListAdapter(mFriendName,
					mFriendImage));
			mButtonId = 2;

			break;
		case R.id.facebook_voting_btn:
			mFriendName = new String[] { "Salman", "Shahrukh", "Amir", "Saif" };
			mFriendImage = new int[] { R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder,
					R.drawable.chat_person_placeholder };
			mFriendList.setAdapter(new FriendListAdapter(mFriendName,
					mFriendImage));
			mButtonId = 3;

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
		case R.id.voting_done_btn:
			onBack();
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
