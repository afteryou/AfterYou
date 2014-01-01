package com.beacon.afterui.sliding.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.views.MainActivity;
import com.beacon.afterui.views.data.SettingMenuAdapter;

public class PrivacySettingFragment extends Fragment implements
		FragmentLifecycle, OnClickListener, OnItemClickListener {

	private boolean isBacking;
	private ListView mListView;
	private TextView mCancelBtn;
	private Typeface mITCAvantGardeStdBkFont;
	private String[] mDetailsText;
	private int mImages[];
	private static final int WHO_SEE_STUFF = 0;
	private static final int WHO_CONTACT_ME = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.setting_fragment, null);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);
		TextView clickItem = (TextView) view.findViewById(R.id.setting_txt);
		clickItem.setTypeface(mITCAvantGardeStdBkFont);
		clickItem.setText(getResources().getString(R.string.privacy_setting));

		mListView = (ListView) view.findViewById(R.id.setting_menu_list);
		mCancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
		mCancelBtn.setTypeface(mITCAvantGardeStdBkFont);
		mDetailsText = getResources().getStringArray(
				R.array.privacy_setting_txt);
		mImages = new int[] { R.drawable.stuff_img, R.drawable.contact_img };
		mListView.setAdapter(new SettingMenuAdapter(getActivity(),
				mDetailsText, mImages));

		mListView.setOnItemClickListener(this);
		mCancelBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		switch (position) {
		case WHO_SEE_STUFF:
			Fragment privacy_setting_menu_fragment = new StuffSettingFragment();
			FragmentHelper.gotoFragment(getActivity(),
					PrivacySettingFragment.this, privacy_setting_menu_fragment,
					bundle);
			break;
		case WHO_CONTACT_ME:

			break;

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
