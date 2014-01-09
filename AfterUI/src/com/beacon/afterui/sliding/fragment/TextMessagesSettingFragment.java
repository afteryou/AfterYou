package com.beacon.afterui.sliding.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;

public class TextMessagesSettingFragment extends Fragment implements
		FragmentLifecycle, OnClickListener {

	private Typeface ITCAvantGardeStdBkFont;

	public TextMessagesSettingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.text_msges_fragment, null);
		ITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
		cancel_btn.setTypeface(ITCAvantGardeStdBkFont);

		TextView text_msg = (TextView) view.findViewById(R.id.text_msg);
		text_msg.setTypeface(ITCAvantGardeStdBkFont);

		TextView registered_txt = (TextView) view
				.findViewById(R.id.registered_txt);
		registered_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView loction_txt = (TextView) view.findViewById(R.id.loction_txt);
		loction_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView remove_txt = (TextView) view.findViewById(R.id.remove_txt);
		remove_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView text_msg_txt = (TextView) view.findViewById(R.id.text_msg_txt);
		text_msg_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView text_msg_no = (TextView) view.findViewById(R.id.text_msg_no);
		text_msg_no.setTypeface(ITCAvantGardeStdBkFont);

		TextView notification_txt = (TextView) view
				.findViewById(R.id.notification_txt);
		notification_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView sub_txt = (TextView) view.findViewById(R.id.sub_txt);
		sub_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView on_off_txt = (TextView) view.findViewById(R.id.on_off_txt);
		on_off_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView edit_btn = (TextView) view.findViewById(R.id.edit_btn);
		edit_btn.setTypeface(ITCAvantGardeStdBkFont);

		TextView daily_text_limit = (TextView) view
				.findViewById(R.id.daily_text_limit);
		daily_text_limit.setTypeface(ITCAvantGardeStdBkFont);

		TextView text_limit_txt = (TextView) view
				.findViewById(R.id.text_limit_txt);
		text_limit_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView messages_txt = (TextView) view.findViewById(R.id.messages_txt);
		messages_txt.setTypeface(ITCAvantGardeStdBkFont);

		cancel_btn.setOnClickListener(this);
		messages_txt.setOnClickListener(this);
		daily_text_limit.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.messages_txt:
			Fragment msg_setting_fragment = new MessageSettingFragment();
			FragmentHelper.replaceFragment(getActivity(), msg_setting_fragment);

			break;

		case R.id.daily_text_limit:
			Fragment text_limit_fragment = new DailyTextLimitFragment();
			FragmentHelper.replaceFragment(getActivity(), text_limit_fragment);
			break;

		case R.id.cancel_btn:
			getActivity().onBackPressed();
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
		return false;
	}

}
