package com.beacon.afterui.views.data;

import com.beacon.afterui.R;
import com.beacon.afterui.sliding.fragment.BlockingFragment;
import com.beacon.afterui.sliding.fragment.PrivacySettingFragment;
import com.beacon.afterui.sliding.fragment.SlidingMenuFragment;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.utils.SetHead;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TermsPoliciesDetailsAdapter extends BaseAdapter {

	private String[] mTermsDetails;
	private Context mContext;
	private Typeface mITCAvantGardeStdBkFont;
	private LayoutInflater mLayoutInflater;
	private String mClassName;

	public TermsPoliciesDetailsAdapter(Context context, String[] termsDetails,
			String class_name) {
		mContext = context;
		mTermsDetails = termsDetails;
		mLayoutInflater = LayoutInflater.from(mContext);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(mContext,
				FontUtils.ITC_AVANT_GARDE_STD_BK);
		mClassName = class_name;

	}

	@Override
	public int getCount() {
		return mTermsDetails.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder;

		if (view == null) {

			holder = new ViewHolder();
			view = mLayoutInflater.inflate(
					R.layout.terms_policies_details_item, null);
			holder.detailTxt = (TextView) view
					.findViewById(R.id.terms_detail_list_item);
			holder.detailTxt.setTypeface(mITCAvantGardeStdBkFont);
			if (mClassName.equals(SetHead.getConditionHead())) {
				holder.detailTxt.setTextSize(19.0f);
			}

			view.setTag(holder);

		} else {

			holder = (ViewHolder) view.getTag();
		}
		holder.detailTxt.setText(mTermsDetails[position]);
		return view;
	}

	private class ViewHolder {

		private TextView detailTxt;

	}

}
