package com.beacon.afterui.sliding.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.views.data.FuturePostAdapter;

public class FuturePostFragment extends Fragment implements FragmentLifecycle,
		OnClickListener, OnItemClickListener {

	private Typeface mITCAvantGardeStdBkFont;
	private ListView mListView;
	private TextView mCancelBtn;
	private String[] mDetailsText;

	private static final int PUBLIC = 0;
	private static final int FRIENDS = 1;
	private static final int PROSPECTS = 2;
	private static final int FAMILY = 3;
	private static final int ONLY_ME = 4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.setting_fragment, null);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);
		TextView clickItem = (TextView) view.findViewById(R.id.setting_txt);
		clickItem.setTypeface(mITCAvantGardeStdBkFont);
		clickItem.setText(getResources().getString(R.string.future_post));

		TextView privacy_info = (TextView) view.findViewById(R.id.privacy_info);
		privacy_info.setVisibility(View.VISIBLE);
		privacy_info.setTypeface(mITCAvantGardeStdBkFont);
		View privacy_info_divider = view
				.findViewById(R.id.privacy_info_divider);
		privacy_info_divider.setVisibility(View.VISIBLE);

		mListView = (ListView) view.findViewById(R.id.setting_menu_list);
		mCancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
		mCancelBtn.setTypeface(mITCAvantGardeStdBkFont);

		mDetailsText = getResources().getStringArray(R.array.who_can_see_txt);
		mListView.setAdapter(new FuturePostAdapter(getActivity(), mDetailsText,
				""));

		mListView.setOnItemClickListener(this);
		mCancelBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		switch (position) {
		case PUBLIC:

			break;
		case FRIENDS:

			break;
		case PROSPECTS:

			break;
		case FAMILY:

			break;
		case ONLY_ME:

			break;

		}

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
