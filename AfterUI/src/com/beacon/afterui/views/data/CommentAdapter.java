package com.beacon.afterui.views.data;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;

public class CommentAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private Typeface mMyriadProRegularFont;
	private Typeface mITCAvantGardeStdBk;

	public CommentAdapter(Context context) {

		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mMyriadProRegularFont = FontUtils.loadTypeFace(this.mContext,
				"MyriadPro-Regular.otf");
		mITCAvantGardeStdBk = FontUtils.loadTypeFace(this.mContext,
				FontUtils.ITC_AVANT_GARDE_STD_BK);
	}

	@Override
	public int getCount() {
		return 3;
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
			view = mInflater.inflate(R.layout.comments_item_detail, null);

			holder.commentTxt = (TextView) view.findViewById(R.id.comment_txt);
			holder.commentTxt.setTypeface(mMyriadProRegularFont);

			holder.commentUserImg = (ImageView) view
					.findViewById(R.id.comment_user_img);

			holder.commentUserName = (TextView) view
					.findViewById(R.id.comment_user_name);
			holder.commentUserName.setTypeface(mITCAvantGardeStdBk);

			holder.timeTxt = (TextView) view
					.findViewById(R.id.comment_time_txt);
			holder.timeTxt.setTypeface(mMyriadProRegularFont);

			view.setTag(holder);

		} else {

			holder = (ViewHolder) view.getTag();
		}

		return view;
	}

	private class ViewHolder {

		private ImageView commentUserImg;
		private TextView commentUserName;
		private TextView commentTxt;
		private TextView timeTxt;

	}

}