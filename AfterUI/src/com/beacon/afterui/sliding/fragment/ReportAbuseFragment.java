package com.beacon.afterui.sliding.fragment;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ReportAbuseFragment extends Fragment implements FragmentLifecycle,
		OnClickListener, OnItemClickListener, OnFocusChangeListener {

	private Typeface mITCAvantGardeStdBkFont;
	private EditText mYourNameBox;
	private EditText mEmailBox;
	private EditText mAbusersProfileName;
	private EditText mWriteMsg;

	private ImageView mNameCrossBtn;
	private ImageView mEmailCrossBtn;
	private ImageView mAbusersCrossBtn;
	private ImageView mWriteMsgCrossBtn;

	public ReportAbuseFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.report_abuse_lay, null);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView cancel_btn_report_abuse = (TextView) view
				.findViewById(R.id.cancel_btn_report_abuse);
		cancel_btn_report_abuse.setTypeface(mITCAvantGardeStdBkFont);
		cancel_btn_report_abuse.setOnClickListener(this);

		TextView setting_txt_report_abuse = (TextView) view
				.findViewById(R.id.setting_txt_report_abuse);
		setting_txt_report_abuse.setTypeface(mITCAvantGardeStdBkFont);

		TextView privacy_info_report_abuse = (TextView) view
				.findViewById(R.id.privacy_info_report_abuse);
		privacy_info_report_abuse.setTypeface(mITCAvantGardeStdBkFont);

		mYourNameBox = (EditText) view.findViewById(R.id.name_box);
		mYourNameBox.setTypeface(mITCAvantGardeStdBkFont);
		mYourNameBox.setOnFocusChangeListener(this);

		mEmailBox = (EditText) view.findViewById(R.id.email_box);
		mEmailBox.setTypeface(mITCAvantGardeStdBkFont);
		mEmailBox.setOnFocusChangeListener(this);

		mAbusersProfileName = (EditText) view
				.findViewById(R.id.abusers_proile_name);
		mAbusersProfileName.setTypeface(mITCAvantGardeStdBkFont);
		mAbusersProfileName.setOnFocusChangeListener(this);

		mWriteMsg = (EditText) view.findViewById(R.id.write_comment);
		mWriteMsg.setTypeface(mITCAvantGardeStdBkFont);
		mWriteMsg.setOnFocusChangeListener(this);

		mNameCrossBtn = (ImageView) view.findViewById(R.id.name_cross_btn);
		mNameCrossBtn.setOnClickListener(this);

		mEmailCrossBtn = (ImageView) view.findViewById(R.id.email_cross_btn);
		mEmailCrossBtn.setOnClickListener(this);

		mAbusersCrossBtn = (ImageView) view
				.findViewById(R.id.abusers_cross_btn);
		mAbusersCrossBtn.setOnClickListener(this);

		mWriteMsgCrossBtn = (ImageView) view
				.findViewById(R.id.write_comment_cross_btn);
		mWriteMsgCrossBtn.setOnClickListener(this);

		TextView report_abuse_btn = (TextView) view
				.findViewById(R.id.report_abuse_btn);
		report_abuse_btn.setTypeface(mITCAvantGardeStdBkFont);
		report_abuse_btn.setOnClickListener(this);

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.report_abuse_btn:

			Toast.makeText(getActivity(), "Report abuse btn pressed",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.name_cross_btn:

			mYourNameBox.setText("");
			mYourNameBox.clearFocus();
			mNameCrossBtn.setVisibility(View.GONE);

			break;
		case R.id.email_cross_btn:

			mEmailBox.setText("");
			mEmailBox.clearFocus();
			mEmailCrossBtn.setVisibility(View.GONE);

			break;
		case R.id.abusers_cross_btn:

			mAbusersProfileName.setText("");
			mAbusersProfileName.clearFocus();
			mAbusersCrossBtn.setVisibility(View.GONE);

			break;
		case R.id.write_comment_cross_btn:

			mWriteMsg.setText("");
			mWriteMsg.clearFocus();
			mWriteMsgCrossBtn.setVisibility(View.GONE);

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

	@Override
	public void onFocusChange(View view, boolean hasFocus) {

		switch (view.getId()) {
		case R.id.name_box:
			if (!(mYourNameBox.length() == 0)) {

				mNameCrossBtn.setVisibility(View.VISIBLE);

			}

			break;

		case R.id.email_box:

			if (!(mEmailBox.length() == 0)) {

				mEmailCrossBtn.setVisibility(View.VISIBLE);

			}

			break;

		case R.id.abusers_proile_name:
			if (!(mAbusersProfileName.length() == 0)) {

				mAbusersCrossBtn.setVisibility(View.VISIBLE);

			}

			break;

		case R.id.write_comment:
			if (!(mWriteMsg.length() == 0)) {

				mWriteMsgCrossBtn.setVisibility(View.VISIBLE);

			}

			break;

		}

	}

}
