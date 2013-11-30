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
import android.widget.ListView;

import com.beacon.afterui.R;
import com.beacon.afterui.views.MainActivity;
import com.beacon.afterui.views.data.NotificationConnectsAddapter;
import com.beacon.afterui.views.data.NotificationMessagesAddapter;
import com.beacon.afterui.views.data.NotificationReferralsAddapter;
import com.beacon.afterui.views.data.NotificationVoteAddapter;

public class NotificationFragment extends Fragment implements
		FragmentLifecycle, OnClickListener {

	private Context mContext;
	private ListView mNotificationList;
	private ImageView mNotificationConnetsBtn;
	private ImageView mNotificationReferralsBtn;
	private ImageView mNotificationVoteBtn;
	private ImageView mNotificationMsgBtn;
	private boolean isBacking = false;

	public NotificationFragment() {
	}

	public NotificationFragment(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.voting_003, null);
		mNotificationConnetsBtn = (ImageView) view
				.findViewById(R.id.num_friends_btn);
		mNotificationReferralsBtn = (ImageView) view
				.findViewById(R.id.num_referals_btn);
		mNotificationVoteBtn = (ImageView) view.findViewById(R.id.num_vote_btn);
		mNotificationMsgBtn = (ImageView) view.findViewById(R.id.num_msg_btn);
		mNotificationList = (ListView) view
				.findViewById(R.id.notification_list);
		mNotificationConnetsBtn.setImageResource(R.drawable.connects_pressed);
		mNotificationList
				.setAdapter(new NotificationConnectsAddapter(mContext));

		mNotificationConnetsBtn.setOnClickListener(this);
		mNotificationReferralsBtn.setOnClickListener(this);
		mNotificationVoteBtn.setOnClickListener(this);
		mNotificationMsgBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.num_friends_btn:
			mNotificationConnetsBtn
					.setImageResource(R.drawable.connects_pressed);
			mNotificationReferralsBtn.setImageResource(R.drawable.referr_btn);
			mNotificationVoteBtn.setImageResource(R.drawable.vote_btn);
			mNotificationMsgBtn.setImageResource(R.drawable.msg_voting_btn);

			mNotificationList.setAdapter(new NotificationConnectsAddapter(
					mContext));

			break;
		case R.id.num_referals_btn:
			mNotificationReferralsBtn
					.setImageResource(R.drawable.referrel_pressed);
			mNotificationConnetsBtn.setImageResource(R.drawable.connect_btn);
			mNotificationVoteBtn.setImageResource(R.drawable.vote_btn);
			mNotificationMsgBtn.setImageResource(R.drawable.msg_voting_btn);

			mNotificationList.setAdapter(new NotificationReferralsAddapter(
					mContext));
			break;
		case R.id.num_vote_btn:
			mNotificationVoteBtn.setImageResource(R.drawable.votes_pressed);
			mNotificationConnetsBtn.setImageResource(R.drawable.connect_btn);
			mNotificationReferralsBtn.setImageResource(R.drawable.referr_btn);
			mNotificationMsgBtn.setImageResource(R.drawable.msg_voting_btn);

			mNotificationList
					.setAdapter(new NotificationVoteAddapter(mContext));
			break;
		case R.id.num_msg_btn:
			mNotificationMsgBtn.setImageResource(R.drawable.msg_pressed);
			mNotificationVoteBtn.setImageResource(R.drawable.vote_btn);
			mNotificationConnetsBtn.setImageResource(R.drawable.connect_btn);
			mNotificationReferralsBtn.setImageResource(R.drawable.referr_btn);

			mNotificationList.setAdapter(new NotificationMessagesAddapter(
					mContext));
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
