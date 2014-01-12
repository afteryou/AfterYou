package com.beacon.afterui.views.data;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.utils.SetHead;

public class MessageSettingAdapter extends BaseAdapter {

	private Context mContext;
	private String[] mMsgSetting;
	private String mClassName;
	private LayoutInflater mLayoutInflater;
	private Typeface ITCAvantGardeStdBkFont;
	private RadioButton mCurrentlyCheckedRB;
	private boolean userSelected = false;

	public MessageSettingAdapter(Context context, String[] msg, String className) {
		mClassName = className;
		mContext = context;
		mMsgSetting = msg;
		mLayoutInflater = LayoutInflater.from(mContext);
		ITCAvantGardeStdBkFont = FontUtils.loadTypeFace(mContext,
				FontUtils.ITC_AVANT_GARDE_STD_BK);
	}

	@Override
	public int getCount() {
		return mMsgSetting.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.msg_setting_item, null);
			holder.msg_limit_txt = (TextView) view
					.findViewById(R.id.msg_limit_txt);
			holder.radioBtn = (RadioButton) view
					.findViewById(R.id.set_limit_txt);
			holder.msg_limit_txt.setTypeface(ITCAvantGardeStdBkFont);
			if (mClassName.equals(SetHead.getConditionHead())) {
				holder.msg_limit_txt.setTextSize(14.0f);
			}

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (position == getCount() - 1 && userSelected == false) {
			holder.radioBtn.setChecked(true);
			mCurrentlyCheckedRB = holder.radioBtn;
		} else {
			holder.radioBtn.setChecked(false);
		}

		// holder.radioBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// if (mCurrentlyCheckedRB != null) {
		// if (mCurrentlyCheckedRB == null)
		// mCurrentlyCheckedRB = (RadioButton) v;
		// mCurrentlyCheckedRB.setChecked(true);
		// }
		//
		// if (mCurrentlyCheckedRB == v)
		// return;
		//
		// mCurrentlyCheckedRB.setChecked(false);
		// ((RadioButton) v).setChecked(true);
		// mCurrentlyCheckedRB = (RadioButton) v;
		// }
		// });

		holder.msg_limit_txt.setText(mMsgSetting[position]);
		return view;
	}

	private class ViewHolder {

		private TextView msg_limit_txt;
		private RadioButton radioBtn;

	}

}
