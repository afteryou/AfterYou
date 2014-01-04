package com.beacon.afterui.views.data;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;

public class ReceiveNotificationForAdapter extends BaseAdapter {

	private String[] mPrivacyPermission;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private Typeface mITCAvantGardeStdBkFont;

	public ReceiveNotificationForAdapter(Context context,
			String[] privacyPermission) {
		mContext = context;
		mPrivacyPermission = privacyPermission;
		mLayoutInflater = LayoutInflater.from(mContext);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(mContext,
				FontUtils.ITC_AVANT_GARDE_STD_BK);

	}

	@Override
	public int getCount() {
		return mPrivacyPermission.length;
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
			view = mLayoutInflater.inflate(
					R.layout.receive_notification_for_adapter, null);
			holder.permittedUserTxt = (TextView) view
					.findViewById(R.id.receive_notification_for);
			holder.permittedUserTxt.setTypeface(mITCAvantGardeStdBkFont);
			holder.permissionCheck = (CheckBox) view
					.findViewById(R.id.check_notification);

			view.setTag(holder);

		} else {

			holder = (ViewHolder) view.getTag();
		}
		holder.permittedUserTxt.setText(mPrivacyPermission[position]);
		return view;
	}

	private class ViewHolder {

		private TextView permittedUserTxt;
		private CheckBox permissionCheck;

	}

}
