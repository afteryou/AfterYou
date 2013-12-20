package com.beacon.afterui.sliding.fragment;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.views.MainActivity;
import com.beacon.afterui.views.data.MessageAdapter;

public class SendMessageScreenFragment extends Fragment implements
		FragmentLifecycle, OnClickListener {

	private Context mContext;
	private TextView mPostBtn;
	private EditText mMessageEditText;
	private ListView mMessageList;
	private boolean isBacking;
	private MessageAdapter mMessageAdapter;
	private ArrayList<String> mMessage = new ArrayList<String>();

	private Typeface mITCAvantGardeStdBkFont;

	public SendMessageScreenFragment() {
	}

	public SendMessageScreenFragment(Context context) {

		mContext = context;
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(mContext,
				FontUtils.ITC_AVANT_GARDE_STD_BK);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.send_message_screen, null);

		TextView voting_done_btn = (TextView) view
				.findViewById(R.id.voting_done_btn);
		voting_done_btn.setTypeface(mITCAvantGardeStdBkFont);

		mPostBtn = (TextView) view.findViewById(R.id.post_btn);
		mPostBtn.setTypeface(mITCAvantGardeStdBkFont);

		mMessageEditText = (EditText) view.findViewById(R.id.write_msg_txt);
		mMessageEditText.setTypeface(mITCAvantGardeStdBkFont);

		mMessageList = (ListView) view.findViewById(R.id.chat_msg_list);
		voting_done_btn.setOnClickListener(this);
		mPostBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.post_btn:
			final String text = mMessageEditText.getText().toString().trim();
			if (text.length() > 0) {
				mMessageEditText.setText("");
				mMessage.add(text);
				mMessageAdapter = new MessageAdapter(mContext, mMessage);
				mMessageList.setAdapter(mMessageAdapter);
				mMessageAdapter.notifyDataSetChanged();
			}
			break;

		case R.id.voting_done_btn:
			onBack();
			break;
		}
	}

	@Override
	public void onFragmentPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFragmentResume() {
		// TODO Auto-generated method stub

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
