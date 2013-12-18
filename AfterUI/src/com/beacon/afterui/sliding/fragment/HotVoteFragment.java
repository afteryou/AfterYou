package com.beacon.afterui.sliding.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.views.MainActivity;

public class HotVoteFragment extends Fragment implements OnClickListener,
		FragmentLifecycle {

	private Context mContext;
	private boolean isBacking = false;
	private TextView mVoteBtn;
	private TextView mHotBtn;

	public HotVoteFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public HotVoteFragment(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View hotVoteView = inflater.inflate(R.layout.voting_001, null);
		mVoteBtn = (TextView) hotVoteView.findViewById(R.id.vote_btn);
		mHotBtn = (TextView) hotVoteView.findViewById(R.id.hot_btn);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/ITCAvantGardeStd-Demi.otf");
		mVoteBtn.setTypeface(typeFace);
		mHotBtn.setTypeface(typeFace);
		mVoteBtn.setOnClickListener(this);
		mHotBtn.setOnClickListener(this);
		return hotVoteView;
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

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.vote_btn:
			Fragment detail = new FriendListFragment(mContext);
			FragmentHelper.gotoFragment(getActivity(), HotVoteFragment.this,
					detail, bundle);
			break;
		case R.id.hot_btn:
			Fragment notification = new NotificationFragment(mContext);
			FragmentHelper.gotoFragment(getActivity(), HotVoteFragment.this,
					notification, bundle);
			break;
		}

	}

}
