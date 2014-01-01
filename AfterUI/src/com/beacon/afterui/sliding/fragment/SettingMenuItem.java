package com.beacon.afterui.sliding.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.views.MainActivity;
import com.beacon.afterui.views.ProfileSettingsActivity;
import com.beacon.afterui.views.data.SettingMenuAdapter;
import com.beacon.afterui.views.data.TermsPoliciesAdapter;

/**
 * For showing left sliding menu behind main view.
 * 
 * @author spoddar
 * 
 */
public class SettingMenuItem extends Fragment implements FragmentLifecycle,
		OnClickListener, OnItemClickListener {
	public static final String TAG = SettingMenuItem.class.toString();

	private ListView mListView;

	private Typeface mITCAvantGardeStdBkFont;

	private static final int PROFILE_SETTING = 0;
	private static final int PRIVACY_SETTING = 1;
	private static final int BLOCKING = 2;
	private static final int NOTIFICATION = 3;
	private static final String SETTING = "setting";
	private static final String TERM_POLICIES = "Terms & Policies";
	private static String CHECKED_ITEM;
	private int[] mImages;
	private String[] mSettingItemTxt;
	private String[] mSubHeadingItemTxt;
	private TextView mCancelBtn;

	private static final int STAT_RIGHTS = 0;
	private static final int DATA_USE_POLICY = 1;
	private static final int COMMUNITY_STANDARDS = 2;

	private boolean isBacking = false;

	public SettingMenuItem() {
	}

	public SettingMenuItem(int[] Images, String[] SettingItemTxt) {

		mImages = Images;
		mSettingItemTxt = SettingItemTxt;

	}

	public SettingMenuItem(int[] Images, String[] SettingItemTxt,
			String[] subHeadingItemTxt) {

		mImages = Images;
		mSettingItemTxt = SettingItemTxt;
		mSubHeadingItemTxt = subHeadingItemTxt;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.setting_fragment, null);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);
		TextView clickItem = (TextView) view.findViewById(R.id.setting_txt);
		clickItem.setTypeface(mITCAvantGardeStdBkFont);
		clickItem.setText(SlidingMenuFragment.getHeading());

		mListView = (ListView) view.findViewById(R.id.setting_menu_list);
		mCancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
		mCancelBtn.setTypeface(mITCAvantGardeStdBkFont);

		if (SlidingMenuFragment.getHeading().equals(SETTING)) {
			mListView.setAdapter(new SettingMenuAdapter(getActivity(),
					mSettingItemTxt, mImages));
		} else if (SlidingMenuFragment.getHeading().equals(TERM_POLICIES)) {
			mListView.setAdapter(new TermsPoliciesAdapter(getActivity(),
					mImages, mSettingItemTxt, mSubHeadingItemTxt));
		}

		mListView.setOnItemClickListener(this);
		mCancelBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		Bundle bundle = new Bundle();
		if (SlidingMenuFragment.getHeading().equals(SETTING)) {

			switch (position) {

			case PROFILE_SETTING:
				intent = new Intent(getActivity().getApplicationContext(),
						ProfileSettingsActivity.class);
				startActivity(intent);
				break;

			case PRIVACY_SETTING:
				Fragment privacy_setting = new PrivacySettingFragment();
				FragmentHelper.gotoFragment(getActivity(),
						SettingMenuItem.this, privacy_setting, bundle);
				break;

			case BLOCKING:
				Fragment blocking_setting = new BlockingFragment();
				FragmentHelper.gotoFragment(getActivity(),
						SettingMenuItem.this, blocking_setting, bundle);
				break;

			case NOTIFICATION:
				Fragment notification = new NotificationFragment(getActivity());
				FragmentHelper.gotoFragment(getActivity(),
						SettingMenuItem.this, notification, bundle);
				break;

			}

		} else if (SlidingMenuFragment.getHeading().equals(TERM_POLICIES)) {

			switch (position) {
			case STAT_RIGHTS:
				Toast.makeText(getActivity(),
						"Statement of rights and responsibilities",
						Toast.LENGTH_SHORT).show();
				Fragment terms_detail_fragment = new TermsPoliciesDetails();
				FragmentHelper.gotoFragment(getActivity(),
						SettingMenuItem.this, terms_detail_fragment, bundle);
				break;
			case DATA_USE_POLICY:

				Toast.makeText(getActivity(), "Data use policy",
						Toast.LENGTH_SHORT).show();

				break;
			case COMMUNITY_STANDARDS:

				Toast.makeText(getActivity(), "Community standards",
						Toast.LENGTH_SHORT).show();

				break;

			}
		}

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.cancel_btn:
			onBack();
			break;
		}

	}

	@Override
	public void onFragmentPause() {

	}

	@Override
	public void onFragmentResume() {

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
