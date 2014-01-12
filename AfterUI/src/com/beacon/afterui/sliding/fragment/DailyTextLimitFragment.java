package com.beacon.afterui.sliding.fragment;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.utils.SetHead;
import com.beacon.afterui.views.data.MessageSettingAdapter;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DailyTextLimitFragment extends Fragment implements
		FragmentLifecycle, OnClickListener, OnItemClickListener {

	private Typeface ITCAvantGardeStdBkFont;
	private String[] mDailyText;
	private ListView mTextLimitList;

	public DailyTextLimitFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.daily_text_limit_setting_fragment, null);
		ITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
		cancel_btn.setTypeface(ITCAvantGardeStdBkFont);

		TextView text_msg = (TextView) view.findViewById(R.id.text_msg);
		text_msg.setTypeface(ITCAvantGardeStdBkFont);

		mDailyText = getResources().getStringArray(R.array.text_limit);

		mTextLimitList = (ListView) view
				.findViewById(R.id.daily_text_limit_list);
		mTextLimitList.setAdapter(new MessageSettingAdapter(getActivity(),
				mDailyText, ""));

		cancel_btn.setOnClickListener(this);
		mTextLimitList.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

}
