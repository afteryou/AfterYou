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
import com.beacon.afterui.utils.SetHead;
import com.beacon.afterui.views.data.MessageSettingAdapter;

public class MessageSettingFragment extends Fragment implements
		FragmentLifecycle, OnClickListener, OnItemClickListener {

	private Typeface ITCAvantGardeStdBkFont;
	private ListView mSetMsgList;
	private String[] mMsgCondition;
	private static final String CLASS_NAME = "MessageSettingFragment";

	public MessageSettingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.messages_setting_fragment, null);
		ITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
		cancel_btn.setTypeface(ITCAvantGardeStdBkFont);

		TextView text_msg = (TextView) view.findViewById(R.id.text_msg);
		text_msg.setTypeface(ITCAvantGardeStdBkFont);

		TextView msg_setting_info = (TextView) view
				.findViewById(R.id.msg_setting_info);
		msg_setting_info.setTypeface(ITCAvantGardeStdBkFont);
		mMsgCondition = getResources().getStringArray(R.array.msg_array);

		mSetMsgList = (ListView) view.findViewById(R.id.msg_setting_list);
		SetHead.setConditionHead(CLASS_NAME);
		mSetMsgList.setAdapter(new MessageSettingAdapter(getActivity(),
				mMsgCondition, CLASS_NAME));
		mSetMsgList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		cancel_btn.setOnClickListener(this);
		mSetMsgList.setOnItemClickListener(this);

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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

}
