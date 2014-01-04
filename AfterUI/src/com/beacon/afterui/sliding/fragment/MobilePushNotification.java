package com.beacon.afterui.sliding.fragment;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.utils.SetHead;
import com.beacon.afterui.views.data.FuturePostAdapter;

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

public class MobilePushNotification extends Fragment implements
		FragmentLifecycle, OnClickListener, OnItemClickListener {
	private Typeface mITCAvantGardeStdBkFont;
	private ListView mListView;
	private TextView mCancelBtn;
	private String[] mDetailsText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.setting_fragment, null);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView clickItem = (TextView) view.findViewById(R.id.setting_txt);
		clickItem.setTypeface(mITCAvantGardeStdBkFont);
		clickItem.setText(getResources().getString(R.string.mobile_push_txt));

		mListView = (ListView) view.findViewById(R.id.setting_menu_list);
		mCancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
		mCancelBtn.setTypeface(mITCAvantGardeStdBkFont);

		mDetailsText = getResources().getStringArray(R.array.mobile_push);
		mListView.setAdapter(new FuturePostAdapter(getActivity(), mDetailsText,
				""));
		mListView.setOnItemClickListener(this);
		mCancelBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onClick(View view) {

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
