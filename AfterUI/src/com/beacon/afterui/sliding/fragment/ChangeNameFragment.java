package com.beacon.afterui.sliding.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.FontUtils;

public class ChangeNameFragment extends Fragment implements FragmentLifecycle,
		OnClickListener {

	private Typeface ITCAvantGardeStdBkFont;

	private EditText mFirstName;
	private EditText mLastName;
	private String mSavedPassword;
	private EditText mComparePassword;
	private int mFlag = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.change_name_fragment, null);
		ITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
		cancel_btn.setTypeface(ITCAvantGardeStdBkFont);

		TextView done_btn = (TextView) view.findViewById(R.id.done_btn);
		done_btn.setTypeface(ITCAvantGardeStdBkFont);

		TextView setting_txt = (TextView) view.findViewById(R.id.setting_txt);
		setting_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView change_name_info = (TextView) view
				.findViewById(R.id.change_name_info);
		change_name_info.setTypeface(ITCAvantGardeStdBkFont);

		TextView first_name = (TextView) view.findViewById(R.id.first_name);
		first_name.setTypeface(ITCAvantGardeStdBkFont);

		TextView last_name = (TextView) view.findViewById(R.id.last_name);
		last_name.setTypeface(ITCAvantGardeStdBkFont);

		TextView save_msg_txt = (TextView) view.findViewById(R.id.save_msg_txt);
		save_msg_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView save_btn = (TextView) view.findViewById(R.id.save_btn);
		save_btn.setTypeface(ITCAvantGardeStdBkFont);

		EditText password_box = (EditText) view.findViewById(R.id.password_box);
		password_box.setTypeface(ITCAvantGardeStdBkFont);

		mFirstName = (EditText) view.findViewById(R.id.first_name_sub_txt);
		mFirstName.setTypeface(ITCAvantGardeStdBkFont);

		mFirstName.setText(PreferenceEngine.getInstance(getActivity())
				.getFirstName());

		mLastName = (EditText) view.findViewById(R.id.last_name_sub_txt);
		mLastName.setTypeface(ITCAvantGardeStdBkFont);

		mLastName.setText(PreferenceEngine.getInstance(getActivity())
				.getLastName());

		mComparePassword = (EditText) view.findViewById(R.id.password_box);

		mSavedPassword = PreferenceEngine.getInstance(getActivity())
				.getPassword();

		done_btn.setOnClickListener(this);
		save_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);

		return view;
	}

	private void saveData() {
		PreferenceEngine.getInstance(getActivity()).setFirstName(
				mFirstName.getText().toString());
		PreferenceEngine.getInstance(getActivity()).setLastName(
				mLastName.getText().toString());
		mFirstName.setText(PreferenceEngine.getInstance(getActivity())
				.getFirstName());
		mLastName.setText(PreferenceEngine.getInstance(getActivity())
				.getLastName());
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.save_btn:
			if (mSavedPassword.equals(mComparePassword.getText().toString())) {

				if (mFirstName.length() == 0 || mLastName.length() == 0) {

					Toast.makeText(getActivity(), "Enter name",
							Toast.LENGTH_SHORT).show();

				} else {
					saveData();
					mFlag = 1;
				}

			} else {

				Toast.makeText(getActivity(), "Password not match",
						Toast.LENGTH_SHORT).show();

			}

			break;

		case R.id.cancel_btn:

			getActivity().onBackPressed();

			break;

		case R.id.done_btn:
			if (mFlag == 1) {
				getActivity().onBackPressed();
			} else {
				Toast.makeText(getActivity(), "Please save data",
						Toast.LENGTH_SHORT).show();
			}

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
