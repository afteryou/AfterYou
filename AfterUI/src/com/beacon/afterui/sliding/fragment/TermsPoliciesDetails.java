package com.beacon.afterui.sliding.fragment;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.views.MainActivity;
import com.beacon.afterui.views.data.TermsPoliciesAdapter;
import com.beacon.afterui.views.data.TermsPoliciesDetailsAdapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TermsPoliciesDetails extends Fragment implements
		FragmentLifecycle, OnClickListener, OnItemClickListener {

	private TextView mCancelBtn;
	private Typeface mITCAvantGardeStdBkFont;
	private ListView mTermsListView;
	private String[] mDetailsText;
	private boolean isBacking;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.setting_fragment, null);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);
		TextView clickItem = (TextView) view.findViewById(R.id.setting_txt);
		clickItem.setTypeface(mITCAvantGardeStdBkFont);
		clickItem.setText("Terms & Policies");

		mDetailsText = getResources().getStringArray(
				R.array.term_policies_detail_txt);

		mTermsListView = (ListView) view.findViewById(R.id.setting_menu_list);
		mCancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
		mCancelBtn.setTypeface(mITCAvantGardeStdBkFont);

		mTermsListView.setAdapter(new TermsPoliciesDetailsAdapter(
				getActivity(), mDetailsText, ""));
		mTermsListView.setOnItemClickListener(this);
		mCancelBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

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
