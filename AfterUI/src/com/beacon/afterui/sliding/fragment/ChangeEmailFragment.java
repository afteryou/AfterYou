package com.beacon.afterui.sliding.fragment;

import com.beacon.afterui.R;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.FontUtils;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeEmailFragment extends Fragment implements FragmentLifecycle,
		OnClickListener {

	private Typeface ITCAvantGardeStdBkFont;
	private EditText mPrimaryEmail;
	private EditText mAfterYouEmail;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.change_email_fragment, null);

		ITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
		cancel_btn.setTypeface(ITCAvantGardeStdBkFont);
		cancel_btn.setOnClickListener(this);

		TextView done_btn = (TextView) view.findViewById(R.id.done_btn);
		done_btn.setTypeface(ITCAvantGardeStdBkFont);
		done_btn.setOnClickListener(this);

		TextView setting_txt = (TextView) view.findViewById(R.id.setting_txt);
		setting_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView head_txt = (TextView) view.findViewById(R.id.head_txt);
		head_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView after_you_email = (TextView) view
				.findViewById(R.id.after_you_email);
		after_you_email.setTypeface(ITCAvantGardeStdBkFont);

		TextView change_name_info = (TextView) view
				.findViewById(R.id.change_name_info);
		change_name_info.setTypeface(ITCAvantGardeStdBkFont);

		mPrimaryEmail = (EditText) view.findViewById(R.id.sub_txt);
		mPrimaryEmail.setTypeface(ITCAvantGardeStdBkFont);

		mPrimaryEmail.setText(PreferenceEngine.getInstance(getActivity())
				.getUserEmail());

		mAfterYouEmail = (EditText) view.findViewById(R.id.after_you_email_txt);
		mAfterYouEmail.setTypeface(ITCAvantGardeStdBkFont);

		mAfterYouEmail.setText(PreferenceEngine.getInstance(getActivity())
				.getFirstName() + "@afteryou.com");

		return view;
	}

	private void saveData() {

		PreferenceEngine.getInstance(getActivity()).saveUserEmail(
				mPrimaryEmail.getText().toString());
		mPrimaryEmail.setText(PreferenceEngine.getInstance(getActivity())
				.getUserEmail());

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.done_btn:

			if (mPrimaryEmail.length() == 0) {
				mPrimaryEmail.setText(PreferenceEngine.getInstance(
						getActivity()).getUserEmail());
				getActivity().onBackPressed();
			} else {
				saveData();
				mPrimaryEmail.clearFocus();
				getActivity().onBackPressed();
			}

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
