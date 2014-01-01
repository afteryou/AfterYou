package com.beacon.afterui.sliding.fragment;

import android.app.Fragment;
import android.content.Context;
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
import com.beacon.afterui.views.data.CommentAdapter;

public class ReviewPostsFragment extends Fragment implements FragmentLifecycle,
		OnClickListener, OnItemClickListener {

	private Typeface mITCAvantGardeStdBkFont;
	private String mMonth = "Novemeber"; // use variable for month
	private int mDate = 7; // use variable for date

	public ReviewPostsFragment() {
	}

	public ReviewPostsFragment(Context Context) {

		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity(),
				FontUtils.ITC_AVANT_GARDE_STD_BK);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.review_lay, null);

		TextView cancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
		cancelBtn.setTypeface(mITCAvantGardeStdBkFont);
		cancelBtn.setOnClickListener(this);

		TextView setting_txt = (TextView) view.findViewById(R.id.setting_txt);
		setting_txt.setText(getResources().getString(R.string.review_post));
		setting_txt.setTypeface(mITCAvantGardeStdBkFont);

		TextView activity_log_txt = (TextView) view
				.findViewById(R.id.activity_log_txt);
		activity_log_txt.setTypeface(mITCAvantGardeStdBkFont);

		TextView activity_log_sub_txt = (TextView) view
				.findViewById(R.id.activity_log_sub_txt);
		activity_log_sub_txt.setTypeface(mITCAvantGardeStdBkFont);

		TextView filter_txt = (TextView) view.findViewById(R.id.filter_txt);
		filter_txt.setTypeface(mITCAvantGardeStdBkFont);
		filter_txt.setOnClickListener(this);

		TextView date_txt = (TextView) view.findViewById(R.id.date_txt);
		date_txt.setTypeface(mITCAvantGardeStdBkFont);
		date_txt.setText(mMonth + " " + mDate);

		ListView commentList = (ListView) view.findViewById(R.id.review_list);
		commentList.setAdapter(new CommentAdapter(getActivity()));
		commentList.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onClick(View view) {
		Bundle bundle = new Bundle();
		switch (view.getId()) {
		case R.id.filter_txt:
			Fragment filter_setting_fragment = new FilterSettingFragment();
			FragmentHelper.gotoFragment(getActivity(),
					ReviewPostsFragment.this, filter_setting_fragment, bundle);

			break;
		case R.id.cancel_btn:

			onBack();

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
