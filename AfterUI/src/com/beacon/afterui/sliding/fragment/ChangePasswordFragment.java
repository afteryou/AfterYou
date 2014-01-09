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

public class ChangePasswordFragment extends Fragment implements
		OnClickListener, FragmentLifecycle {

	private Typeface ITCAvantGardeStdBkFont;

	private EditText mCurrentPassword;
	private EditText mNewPassword;
	private EditText mRetypePassword;
	private int mFlag = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.change_password_fragment, null);
		ITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
		cancel_btn.setTypeface(ITCAvantGardeStdBkFont);

		TextView done_btn = (TextView) view.findViewById(R.id.done_btn);
		done_btn.setTypeface(ITCAvantGardeStdBkFont);

		TextView setting_txt = (TextView) view.findViewById(R.id.setting_txt);
		setting_txt.setTypeface(ITCAvantGardeStdBkFont);

		mCurrentPassword = (EditText) view
				.findViewById(R.id.current_password_box);
		mCurrentPassword.setTypeface(ITCAvantGardeStdBkFont);
		mCurrentPassword.setText(PreferenceEngine.getInstance(getActivity())
				.getPassword());
		mCurrentPassword.setKeyListener(null);

		TextView change_password_btn = (TextView) view
				.findViewById(R.id.change_password_btn);
		change_password_btn.setTypeface(ITCAvantGardeStdBkFont);

		mNewPassword = (EditText) view.findViewById(R.id.new_password_box);
		mNewPassword.setTypeface(ITCAvantGardeStdBkFont);

		mRetypePassword = (EditText) view.findViewById(R.id.retype_pass);
		mRetypePassword.setTypeface(ITCAvantGardeStdBkFont);

		TextView current_password_txt = (TextView) view
				.findViewById(R.id.current_password_txt);
		current_password_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView new_password_txt = (TextView) view
				.findViewById(R.id.new_password_txt);
		new_password_txt.setTypeface(ITCAvantGardeStdBkFont);

		TextView retype_txt = (TextView) view.findViewById(R.id.retype_txt);
		retype_txt.setTypeface(ITCAvantGardeStdBkFont);

		done_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);
		change_password_btn.setOnClickListener(this);

		return view;
	}

	private void saveData() {

		PreferenceEngine.getInstance(getActivity()).setPassword(
				mRetypePassword.getText().toString());
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.change_password_btn:

			if (!(mNewPassword.length() == 0)
					&& mNewPassword.getText().toString()
							.equals(mRetypePassword.getText().toString())) {

				saveData();
				mCurrentPassword.setText(mRetypePassword.getText().toString());

				// getActivity().onBackPressed();

			} else {
				Toast.makeText(
						getActivity(),
						"password length greter than 6 character or password not match",
						Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.cancel_btn:

			getActivity().onBackPressed();

			break;

		case R.id.done_btn:
			// if (mFlag == 1) {
			// getActivity().onBackPressed();
			// } else {
			// Toast.makeText(getActivity(), "Please save data",
			// Toast.LENGTH_SHORT).show();
			// }
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
