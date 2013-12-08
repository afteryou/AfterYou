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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.views.MainActivity;
import com.beacon.afterui.views.data.ViewVotesAddapter;

public class ViewVotesFragment extends Fragment implements FragmentLifecycle,
		OnClickListener {

	private Context mContext;
	private boolean isBacking = false;
	private static final int REFERRAL_BTN = 2;
	private int mButtonId;
	private ProgressBar mProgressNot;
	private ProgressBar mProgressHot;
	private ProgressBar mProgressNo;
	private int mNotValue = 300;
	private int mHotValue = 200;
	private int mNoValue = 500;

	public ViewVotesFragment(Context context) {

		mContext = context;

	}

	public ViewVotesFragment(Context context, final int buttonId) {

		mContext = context;
		mButtonId = buttonId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.voting_006, null);
		ListView mViewVotesList = (ListView) view
				.findViewById(R.id.view_votes_list);
		TextView votedNotTxt = (TextView) view
				.findViewById(R.id.voted_not_num_count);
		TextView votedHotTxt = (TextView) view
				.findViewById(R.id.voted_hot_num_count);
		TextView votedNoTxt = (TextView) view
				.findViewById(R.id.voted_no_num_count);
		TextView done_btn = (TextView) view.findViewById(R.id.voting_done_btn);
		ImageView connectBtn = (ImageView) view.findViewById(R.id.conect_btn);
		mProgressNot = (ProgressBar) view.findViewById(R.id.progress_not);
		mProgressHot = (ProgressBar) view.findViewById(R.id.progress_hot);
		mProgressNo = (ProgressBar) view.findViewById(R.id.progress_no);

		if (mNotValue > mHotValue && mNotValue > mNoValue) {
			mProgressNot.setMax(mNotValue);
			mProgressHot.setMax(mNotValue);
			mProgressNo.setMax(mNotValue);

		} else if (mHotValue > mNotValue && mHotValue > mNoValue) {
			mProgressNot.setMax(mHotValue);
			mProgressHot.setMax(mHotValue);
			mProgressNo.setMax(mHotValue);

		} else {
			mProgressNot.setMax(mNoValue);
			mProgressHot.setMax(mNoValue);
			mProgressNo.setMax(mNoValue);

		}

		mProgressNot.setProgress(mNotValue);
		mProgressHot.setProgress(mHotValue);
		mProgressNo.setProgress(mNoValue);
		if (REFERRAL_BTN == mButtonId) {
			LinearLayout connect_btn_lay = (LinearLayout) view
					.findViewById(R.id.connect_btn_lay);
			TextView voting_congrates_txt = (TextView) view
					.findViewById(R.id.voting_congrates_txt);
			TextView slogan_congrates_txt = (TextView) view
					.findViewById(R.id.slogan_congrates_txt);
			voting_congrates_txt.setText("It seems to be a tie");
			slogan_congrates_txt.setText("But u can still accept if you wish");
			connect_btn_lay.setVisibility(View.VISIBLE);
		}
		String votedNot = "(" + 2 + ")";
		votedNotTxt.setText(votedNot);
		String votedHot = "(" + 5 + ")";
		votedHotTxt.setText(votedHot);

		String votedNo = "(" + 1 + ")";
		votedNoTxt.setText(votedNo);
		mViewVotesList.setAdapter(new ViewVotesAddapter(mContext));

		connectBtn.setOnClickListener(this);
		done_btn.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.conect_btn:
			Toast.makeText(mContext, "Connect Btn is Pressed",
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.voting_done_btn:
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
