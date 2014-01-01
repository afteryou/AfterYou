package com.beacon.afterui.sliding.fragment;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.utils.SetHead;
import com.beacon.afterui.views.data.SettingMenuAdapter;
import com.beacon.afterui.views.data.TermsPoliciesDetailsAdapter;

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

public class BlockingFragment extends Fragment implements FragmentLifecycle,
		OnClickListener, OnItemClickListener {

	private Typeface mITCAvantGardeStdBkFont;
	private ListView mListView;
	private TextView mCancelBtn;
	private String[] mDetailsText;
	private static final String HEADING_TXT = "blocking";

	public static final String CLASS_NAME = "blocking_fragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.setting_fragment, null);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);
		TextView clickItem = (TextView) view.findViewById(R.id.setting_txt);
		clickItem.setTypeface(mITCAvantGardeStdBkFont);
		clickItem.setText(HEADING_TXT);

		mListView = (ListView) view.findViewById(R.id.setting_menu_list);
		mCancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
		mCancelBtn.setTypeface(mITCAvantGardeStdBkFont);
		mDetailsText = getResources().getStringArray(
				R.array.blocking_setting_txt);
		mListView.setAdapter(new TermsPoliciesDetailsAdapter(getActivity(),
				mDetailsText, CLASS_NAME));
		SetHead.setConditionHead(CLASS_NAME);

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
