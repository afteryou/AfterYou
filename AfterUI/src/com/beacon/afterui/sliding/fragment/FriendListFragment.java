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
import android.graphics.drawable.Drawable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;

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
	private EditText mSearchBox;
	private Boolean isBacking = false;
	private static final int AFTER_YOU_BTN = 1;
	private static final int CONTACTS_BTN = 2;
	private static final int FACEBOOK_BTN = 3;
	private static final int TWITTER_BTN = 4;
	private static final int IMPORT_CONTACTS_BTN = 5;
	private static final int IMPORT_FACEBOOK_BTN = 6;
	private static final int IMPORT_TWITTER_BTN = 7;
	private static final int IMPORT_GOOGLE_BTN = 8;

	private static int mButtonId;
	private Typeface mITCAvantGardeStdBk;
	private String mWhichFragment;
	private String FROM_HOT_VOTE_FRAGMENT = "from_hot_vote_fragment";
	private String FROM_IMPORT_SIDEBAR = "from_import";

	private Drawable mContactsDrawable;
	private Drawable mfacebookDrawable;
	private Drawable mTwitterDrawable;
	private Drawable mGoogleDrawable;
	private static int mFlag;

	public FriendListFragment() {
	}

	public FriendListFragment(Context context, String from) {

		mContext = context;
		mITCAvantGardeStdBk = FontUtils.loadTypeFace(mContext,
				FontUtils.ITC_AVANT_GARDE_STD_BK);
		mWhichFragment = from;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View friendListView = inflater.inflate(R.layout.friend_list_screen,
				null);
		RelativeLayout searchLay = (RelativeLayout) friendListView
				.findViewById(R.id.voting_search_lay);

		TextView cancel_btn = (TextView) friendListView
				.findViewById(R.id.voting_cancel_btn);
		cancel_btn.setTypeface(mITCAvantGardeStdBk);

		TextView setting_txt = (TextView) friendListView
				.findViewById(R.id.voting_setting_txt);
		setting_txt.setTypeface(mITCAvantGardeStdBk);

		mSearchBox = (EditText) friendListView.findViewById(R.id.search_txt);
		mSearchBox.setTypeface(mITCAvantGardeStdBk);

		mAfterYouFrnd = (TextView) friendListView
				.findViewById(R.id.after_you_friends_btn);
		mAfterYouFrnd.setTypeface(mITCAvantGardeStdBk);

		mContacts = (TextView) friendListView.findViewById(R.id.contacts_btn);
		mContacts.setTypeface(mITCAvantGardeStdBk);

		mFacebookFrnd = (TextView) friendListView
				.findViewById(R.id.facebook_voting_btn);
		mFacebookFrnd.setTypeface(mITCAvantGardeStdBk);

		mTwitterFrnd = (TextView) friendListView.findViewById(R.id.twitter_btn);
		mTwitterFrnd.setTypeface(mITCAvantGardeStdBk);

		mFriendList = (ListView) friendListView.findViewById(R.id.freind_list);

		mSearchBtn = (Button) friendListView.findViewById(R.id.search_btn);

		TextView done_btn = (TextView) friendListView
				.findViewById(R.id.voting_done_btn);
		done_btn.setTypeface(mITCAvantGardeStdBk);

		if (mWhichFragment.equals(FROM_IMPORT_SIDEBAR)) {
			initTempImages();
			mAfterYouFrnd.setCompoundDrawables(null, mContactsDrawable, null,
					null);
			mAfterYouFrnd.setText("contacts");
			mContacts.setCompoundDrawables(null, mfacebookDrawable, null, null);
			mContacts.setText("facebook");
			mFacebookFrnd.setCompoundDrawables(null, mTwitterDrawable, null,
					null);
			mFacebookFrnd.setText("twitter");
			mTwitterFrnd
					.setCompoundDrawables(null, mGoogleDrawable, null, null);
			mTwitterFrnd.setText("google+");

			mSearchBox.setVisibility(View.GONE);
			mSearchBtn.setVisibility(View.GONE);
			done_btn.setVisibility(View.INVISIBLE);
			searchLay.setBackground(null);
			setting_txt.setText(SlidingMenuFragment.getHeading());

			cancel_btn.setVisibility(View.VISIBLE);
			setting_txt.setVisibility(View.VISIBLE);
			cancel_btn.setOnClickListener(this);

			mFlag = 1;

		} else {
			mFlag = 0;
		}

		mAfterYouFrnd.setOnClickListener(this);
		mContacts.setOnClickListener(this);
		mFacebookFrnd.setOnClickListener(this);
		mTwitterFrnd.setOnClickListener(this);
		mFriendList.setOnItemClickListener(this);
		mSearchBtn.setOnClickListener(this);
		done_btn.setOnClickListener(this);

		return friendListView;
	}

	private void initTempImages() {
		mContactsDrawable = mContext.getResources().getDrawable(
				R.drawable.contacts_voting_img);
		mContactsDrawable.setBounds(0, 0,
				mContactsDrawable.getIntrinsicWidth(),
				mContactsDrawable.getIntrinsicHeight());

		mfacebookDrawable = mContext.getResources().getDrawable(
				R.drawable.facebook_voting_img);
		mfacebookDrawable.setBounds(0, 0,
				mfacebookDrawable.getIntrinsicWidth(),
				mfacebookDrawable.getIntrinsicHeight());

		mTwitterDrawable = mContext.getResources().getDrawable(
				R.drawable.twitter_voting_img);
		mTwitterDrawable.setBounds(0, 0, mTwitterDrawable.getIntrinsicWidth(),
				mTwitterDrawable.getIntrinsicHeight());

		mGoogleDrawable = mContext.getResources().getDrawable(
				R.drawable.google_plus_btn);
		mGoogleDrawable.setBounds(0, 0, mGoogleDrawable.getIntrinsicWidth(),
				mGoogleDrawable.getIntrinsicHeight());

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		Fragment confirmFragment = new VoteConfirm(mContext);
		if (mButtonId == AFTER_YOU_BTN) {

			// FragmentHelper.gotoFragment(getActivity(),
			// FriendListFragment.this,
			// confirmFragment, bundle);
			mButtonId = 0;
			FragmentHelper.replaceFragment(getActivity(), confirmFragment,
					bundle);

		} else if (mButtonId == CONTACTS_BTN) {

			// FragmentHelper.gotoFragment(getActivity(),
			// FriendListFragment.this,
			// confirmFragment, bundle);
			mButtonId = 0;
			FragmentHelper.replaceFragment(getActivity(), confirmFragment,
					bundle);

		} else if (mButtonId == FACEBOOK_BTN) {

			// FragmentHelper.gotoFragment(getActivity(),
			// FriendListFragment.this,
			// confirmFragment, bundle);
			mButtonId = 0;
			FragmentHelper.replaceFragment(getActivity(), confirmFragment,
					bundle);

		} else if (mButtonId == TWITTER_BTN) {
			// FragmentHelper.gotoFragment(getActivity(),
			// FriendListFragment.this,
			// confirmFragment, bundle);
			mButtonId = 0;
			FragmentHelper.replaceFragment(getActivity(), confirmFragment,
					bundle);
		} else if (mButtonId == IMPORT_CONTACTS_BTN) {
			mButtonId = 0;
			Toast.makeText(mContext, "Import contacts", Toast.LENGTH_SHORT)
					.show();
		} else if (mButtonId == IMPORT_FACEBOOK_BTN) {
			mButtonId = 0;
			Toast.makeText(mContext, "Import facebook contacts",
					Toast.LENGTH_SHORT).show();
		} else if (mButtonId == IMPORT_TWITTER_BTN) {
			mButtonId = 0;
			Toast.makeText(mContext, "Import twitter contacts",
					Toast.LENGTH_SHORT).show();
		} else {
			mButtonId = 0;
			Toast.makeText(mContext, "Import google+ contacts",
					Toast.LENGTH_SHORT).show();
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

		if (mFlag == 1) {
			switch (v.getId()) {
			case R.id.after_you_friends_btn:

				Toast.makeText(mContext, "Call the contacts addapter",
						Toast.LENGTH_SHORT).show();
				mButtonId = 5;

				break;
			case R.id.contacts_btn:

				Toast.makeText(mContext, "call the facebook adapter",
						Toast.LENGTH_SHORT).show();
				mButtonId = 6;

				break;
			case R.id.facebook_voting_btn:

				Toast.makeText(mContext, "call the twitter adapter",
						Toast.LENGTH_SHORT).show();
				mButtonId = 7;

				break;
			case R.id.twitter_btn:
				Toast.makeText(mContext, "call the google+ adapter",
						Toast.LENGTH_SHORT).show();
				mButtonId = 8;

				break;
			case R.id.voting_cancel_btn:
				Toast.makeText(mContext, "Cancel btn is pressed",
						Toast.LENGTH_SHORT).show();
				onBack();
				break;
			}

		} else {
			switch (v.getId()) {
			case R.id.after_you_friends_btn:

				mFriendName = new String[] { "After you", "facebook",
						"Twitter", "Contacts" };
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
				mFriendList.setAdapter(new FriendListAdapter(
						getPhoneContacts(), mFriendImage));
				mButtonId = 2;

				break;
			case R.id.facebook_voting_btn:
				mFriendName = new String[] { "Salman", "Shahrukh", "Amir",
						"Saif" };
				mFriendImage = new int[] { R.drawable.sample_img,
						R.drawable.sample_img, R.drawable.sample_img,
						R.drawable.sample_img };
				mFriendList.setAdapter(new FriendListAdapter(mFriendName,
						mFriendImage));
				mButtonId = 3;

				break;
			case R.id.twitter_btn:
				mFriendName = new String[] { "Karina", "Katrina", "Asin",
						"Deepika" };
				mFriendImage = new int[] { R.drawable.sample_img,
						R.drawable.sample_img, R.drawable.sample_img,
						R.drawable.sample_img };
				mFriendList.setAdapter(new FriendListAdapter(mFriendName,
						mFriendImage));
				mButtonId = 4;
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
		// if (!isBacking) {
		// isBacking = true;
		// ((MainActivity) getActivity()).updateToMainScreenActionBar();
		// applyBackAnimation();
		// }
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
