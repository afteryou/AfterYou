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

public class AfterYouNotificationBy extends Fragment implements OnClickListener {

	private Typeface mITCAvantGardeStdBkFont;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.notification_by_after_you, null);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView cancelBtn = (TextView) view
				.findViewById(R.id.cancel_btn_afteryou_notif);
		cancelBtn.setTypeface(mITCAvantGardeStdBkFont);

		TextView setting_txt_afteryou_notif = (TextView) view
				.findViewById(R.id.setting_txt_afteryou_notif);
		setting_txt_afteryou_notif.setTypeface(mITCAvantGardeStdBkFont);

		TextView privacy_info_afteryou_notif = (TextView) view
				.findViewById(R.id.privacy_info_afteryou_notif);
		privacy_info_afteryou_notif.setTypeface(mITCAvantGardeStdBkFont);

		TextView adjust_txt = (TextView) view.findViewById(R.id.adjust_txt);
		adjust_txt.setTypeface(mITCAvantGardeStdBkFont);

		TextView one_txt = (TextView) view.findViewById(R.id.one_txt);
		one_txt.setTypeface(mITCAvantGardeStdBkFont);

		TextView tab_img = (TextView) view.findViewById(R.id.tab_img);
		tab_img.setTypeface(mITCAvantGardeStdBkFont);

		TextView two_txt = (TextView) view.findViewById(R.id.two_txt);
		two_txt.setTypeface(mITCAvantGardeStdBkFont);

		TextView notification_txt = (TextView) view
				.findViewById(R.id.notification_txt);
		notification_txt.setTypeface(mITCAvantGardeStdBkFont);

		TextView third_text = (TextView) view.findViewById(R.id.third_text);
		third_text.setTypeface(mITCAvantGardeStdBkFont);

		cancelBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cancel_btn_afteryou_notif:

			getActivity().onBackPressed();

			break;
		}

	}

}
