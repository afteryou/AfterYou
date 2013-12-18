package com.beacon.afterui.sliding.fragment;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
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
	private TextView mAfterYouFrnd;
	private TextView mContacts;
	private TextView mFacebookFrnd;
	private TextView mTwitterFrnd;
	private Button mSearchBtn;
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
		View friendListView = inflater.inflate(R.layout.friend_list_screen,
				null);
		Typeface typeFaceBK = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/ITCAvantGardeStd-Bk.otf");
		mAfterYouFrnd = (TextView) friendListView
				.findViewById(R.id.after_you_friends_btn);
		mAfterYouFrnd.setTypeface(typeFaceBK);

		mContacts = (TextView) friendListView.findViewById(R.id.contacts_btn);
		mContacts.setTypeface(typeFaceBK);

		mFacebookFrnd = (TextView) friendListView
				.findViewById(R.id.facebook_voting_btn);
		mFacebookFrnd.setTypeface(typeFaceBK);

		mTwitterFrnd = (TextView) friendListView.findViewById(R.id.twitter_btn);
		mTwitterFrnd.setTypeface(typeFaceBK);

		mFriendList = (ListView) friendListView.findViewById(R.id.freind_list);

		mSearchBtn = (Button) friendListView.findViewById(R.id.search_btn);

		TextView done_btn = (TextView) friendListView
				.findViewById(R.id.voting_done_btn);
		done_btn.setTypeface(typeFaceBK);

		mAfterYouFrnd.setOnClickListener(this);
		mContacts.setOnClickListener(this);
		mFacebookFrnd.setOnClickListener(this);
		mTwitterFrnd.setOnClickListener(this);
		mFriendList.setOnItemClickListener(this);
		mSearchBtn.setOnClickListener(this);
		done_btn.setOnClickListener(this);

		return friendListView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		Fragment confirmFragment = new VoteConfirm(mContext);
		if (mButtonId == AFTER_YOU_BTN) {

			FragmentHelper.gotoFragment(getActivity(), FriendListFragment.this,
					confirmFragment, bundle);

		} else if (mButtonId == CONTACTS_BTN) {

			FragmentHelper.gotoFragment(getActivity(), FriendListFragment.this,
					confirmFragment, bundle);

		} else if (mButtonId == FACEBOOK_BTN) {

			FragmentHelper.gotoFragment(getActivity(), FriendListFragment.this,
					confirmFragment, bundle);

		} else {
			FragmentHelper.gotoFragment(getActivity(), FriendListFragment.this,
					confirmFragment, bundle);
		}

	}

	class PhoneContactInfo {
		String phoneContactNumber, phoneContactName;
		int phoneContactID;

		public PhoneContactInfo(int id, String name, String number) {
			this.phoneContactNumber = number;
			this.phoneContactID = id;
			this.phoneContactName = name;
		}

		public int getPhoneContactID() {
			return phoneContactID;
		}

		public String getPhoneContactNumber() {
			return phoneContactNumber;
		}

		public String getPhoneContactName() {
			return phoneContactName;
		}
	}

	private ArrayList<PhoneContactInfo> getPhoneContacts() {
		PhoneContactInfo phoneContactInfo = null;
		ArrayList<PhoneContactInfo> contacts = new ArrayList<PhoneContactInfo>();
		Cursor cursor = getActivity().getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
						ContactsContract.CommonDataKinds.Phone._ID }, null,
				null,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			int contactID = cursor
					.getInt(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
			String contactName = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String contactNumber = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			phoneContactInfo = new PhoneContactInfo(contactID, contactName,
					contactNumber);
			contacts.add(phoneContactInfo);
			cursor.moveToNext();
		}
		return contacts;
	}

	@Override
	public void onClick(View v) {
		mFriendName = null;
		mFriendImage = null;
		switch (v.getId()) {
		case R.id.after_you_friends_btn:

			mFriendName = new String[] { "After you", "facebook", "Twitter",
					"Contacts" };
			mFriendImage = new int[] { R.drawable.sample_img,
					R.drawable.sample_img, R.drawable.sample_img,
					R.drawable.sample_img };
			mFriendList.setAdapter(new FriendListAdapter(mFriendName,
					mFriendImage));
			mButtonId = 1;
			break;
		case R.id.contacts_btn:
			// mFriendName = getPhoneContacts();
			mFriendImage = new int[] { R.drawable.sample_img,
					R.drawable.sample_img, R.drawable.sample_img,
					R.drawable.sample_img };
			mFriendList.setAdapter(new FriendListAdapter(getPhoneContacts(),
					mFriendImage));
			mButtonId = 2;

			break;
		case R.id.facebook_voting_btn:
			mFriendName = new String[] { "Salman", "Shahrukh", "Amir", "Saif" };
			mFriendImage = new int[] { R.drawable.sample_img,
					R.drawable.sample_img, R.drawable.sample_img,
					R.drawable.sample_img };
			mFriendList.setAdapter(new FriendListAdapter(mFriendName,
					mFriendImage));
			mButtonId = 3;

			break;
		case R.id.twitter_btn:
			mFriendName = new String[] { "Karina", "Katrina", "Asin", "Deepika" };
			mFriendImage = new int[] { R.drawable.sample_img,
					R.drawable.sample_img, R.drawable.sample_img,
					R.drawable.sample_img };
			mFriendList.setAdapter(new FriendListAdapter(mFriendName,
					mFriendImage));
			break;
		case R.id.search_btn:
			Toast.makeText(mContext, "Search btn is pressed",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.voting_done_btn:
			onBack();
			break;

		}

	}

	private class FriendListAdapter extends BaseAdapter {
		private String[] friendName;
		private int[] image;
		private ArrayList<PhoneContactInfo> contactInfo;

		public FriendListAdapter(String[] list, int[] userImage) {
			this.friendName = list;
			image = userImage;
		}

		public FriendListAdapter(ArrayList<PhoneContactInfo> contactInfo,
				int[] userImage) {
			this.contactInfo = contactInfo;
			image = userImage;
		}

		@Override
		public int getCount() {
			if (contactInfo != null) {
				return contactInfo.size();
			}
			return friendName.length;
		}

		@Override
		public Object getItem(int position) {
			if (contactInfo != null) {
				return contactInfo.get(position);
			}
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
				Typeface typeFaceBK = Typeface.createFromAsset(getActivity()
						.getAssets(), "fonts/ITCAvantGardeStd-Bk.otf");
				holder = new ViewHolder();
				view = getActivity().getLayoutInflater().inflate(
						R.layout.sliding_menu_item, null);
				holder.userImage = (ImageView) view
						.findViewById(R.id.dashboard_img);
				holder.userName = (TextView) view
						.findViewById(R.id.dashboard_txt);
				holder.userName.setTypeface(typeFaceBK);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			if (contactInfo != null) {
				holder.userName.setText(contactInfo.get(position)
						.getPhoneContactName());
			} else {
				holder.userName.setText(friendName[position]);
			}
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
