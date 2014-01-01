package com.beacon.afterui.sliding.fragment;

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

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.utils.SetHead;
import com.beacon.afterui.views.data.TermsPoliciesDetailsAdapter;

public class StuffSettingFragment extends Fragment implements
		FragmentLifecycle, OnClickListener, OnItemClickListener {

	private Typeface mITCAvantGardeStdBkFont;
	private ListView mListView;
	private TextView mCancelBtn;
	private String[] mDetailsText;
	private static final String HEADING_TXT = "who can see my stuff?";

	public static final String CLASS_NAME = "stuff_setting_fragment";

	private static final int WHO_SEE_FUTURE_POST = 0;
	private static final int WHO_REVIEW_POSTS = 1;

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
				R.array.privacy_setting_menu_txt);
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
		Bundle bundle = new Bundle();
		switch (position) {
		case WHO_SEE_FUTURE_POST:

			Fragment future_post_fragment = new FuturePostFragment();
			// FragmentHelper.replaceFragment(getActivity(),
			// future_post_fragment,
			// bundle);
//			FragmentHelper.gotoFragment(getActivity(),
//					StuffSettingFragment.this, future_post_fragment, bundle);
			FragmentHelper.replaceFragment(getActivity(), future_post_fragment, bundle);
			break;
		case WHO_REVIEW_POSTS:
			Fragment review_post_fragment = new ReviewPostsFragment(
					getActivity());
			// FragmentHelper.replaceFragment(getActivity(),
			// review_post_fragment,
			// bundle);
			FragmentHelper.gotoFragment(getActivity(),
					StuffSettingFragment.this, review_post_fragment, bundle);
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