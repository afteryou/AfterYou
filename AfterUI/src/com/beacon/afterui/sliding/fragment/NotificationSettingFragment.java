package com.beacon.afterui.sliding.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.utils.SetHead;
import com.beacon.afterui.views.data.ReceiveNotificationForAdapter;
import com.beacon.afterui.views.data.TermsPoliciesDetailsAdapter;

public class NotificationSettingFragment extends Fragment implements
		FragmentLifecycle, OnClickListener, OnItemClickListener {

	private Typeface mITCAvantGardeStdBkFont;

	private ListView mReceiveNotificationByList;
	private ListView mReceiveNotificationForList;

	private static final int AFTER_YOU = 0;
	private static final int EMAIL = 1;
	private static final int MOBILE_PUSH = 2;
	private static final int TEXT_MESSGES = 3;

	private String[] mDetailsText;

	private static final String CLASS_NAME = "NotificationSettingFragment";

	public NotificationSettingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notification_setting, null);

		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView cancel_btn_notification_setting = (TextView) view
				.findViewById(R.id.cancel_btn_notification_setting);
		cancel_btn_notification_setting.setTypeface(mITCAvantGardeStdBkFont);
		cancel_btn_notification_setting.setOnClickListener(this);

		TextView notification_by = (TextView) view
				.findViewById(R.id.notification_by);
		notification_by.setTypeface(mITCAvantGardeStdBkFont);
		TextView notification_for = (TextView) view
				.findViewById(R.id.notification_for);
		notification_for.setTypeface(mITCAvantGardeStdBkFont);

		mReceiveNotificationByList = (ListView) view
				.findViewById(R.id.notification_by_list);
		mDetailsText = getResources().getStringArray(
				R.array.notification_by_array);
		mReceiveNotificationByList.setAdapter(new TermsPoliciesDetailsAdapter(
				getActivity(), mDetailsText, CLASS_NAME));

		SetHead.setConditionHead(CLASS_NAME);
		mReceiveNotificationByList.setOnItemClickListener(this);

		mReceiveNotificationForList = (ListView) view
				.findViewById(R.id.notification_for_list);
		mDetailsText = getResources().getStringArray(
				R.array.notification_for_array);
		mReceiveNotificationForList
				.setAdapter(new ReceiveNotificationForAdapter(getActivity(),
						mDetailsText));

		mReceiveNotificationForList.setOnItemClickListener(this);

		// Helper.getListViewSize(mReceiveNotificationByList);
		// Helper.getListViewSize(mReceiveNotificationForList);
		setListViewHeightBasedOnChildren(mReceiveNotificationByList);
		setListViewHeightBasedOnChildren(mReceiveNotificationForList);

		return view;
	}

	private static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		switch (parent.getId()) {
		case R.id.notification_by_list:

			switch (position) {
			case AFTER_YOU:
				Fragment after_you_noti = new AfterYouNotificationBy();
				FragmentHelper.replaceFragment(getActivity(), after_you_noti);

				break;
			case EMAIL:

				Fragment email_notification_fragment = new EmailNotificationFragment();
				FragmentHelper.replaceFragment(getActivity(),
						email_notification_fragment);
				break;
			case MOBILE_PUSH:
				Fragment mobile_notification_fragment = new MobilePushNotification();
				FragmentHelper.replaceFragment(getActivity(),
						mobile_notification_fragment);
				break;
			case TEXT_MESSGES:

				Fragment text_msg_fragment = new TextMessagesSettingFragment();
				FragmentHelper
						.replaceFragment(getActivity(), text_msg_fragment);

				break;

			}
			break;

		case R.id.notification_for_list:
			break;

		}

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.cancel_btn_notification_setting:
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
