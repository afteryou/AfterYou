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
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.views.MainActivity;

public class VoteConfirm extends Fragment implements FragmentLifecycle,
		OnClickListener {

	private Context mContext;
	private boolean isBacking;

	public VoteConfirm() {
	}

	public VoteConfirm(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.voting_008, null);
		ImageView vote_confirm_btn = (ImageView) view
				.findViewById(R.id.vote_confirm_btn);
		ImageView msg_confirm_btn = (ImageView) view
				.findViewById(R.id.msg_confirm_btn);

		TextView voting_done_btn = (TextView) view
				.findViewById(R.id.voting_done_btn);

		vote_confirm_btn.setOnClickListener(this);
		msg_confirm_btn.setOnClickListener(this);
		voting_done_btn.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {

		Bundle bundle = new Bundle();

		switch (v.getId()) {
		case R.id.vote_confirm_btn:

			Toast.makeText(mContext, "Vote Confirm btn is pressed",
					Toast.LENGTH_SHORT).show();

			break;
		case R.id.msg_confirm_btn:

			Toast.makeText(mContext, "Send message to voter btn is pressed",
					Toast.LENGTH_SHORT).show();
			Fragment sendMsg = new SendMessageScreenFragment(mContext);
			FragmentHelper.gotoFragment(getActivity(), VoteConfirm.this,
					sendMsg, bundle);

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
