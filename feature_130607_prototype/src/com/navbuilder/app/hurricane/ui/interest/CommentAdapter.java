package com.navbuilder.app.hurricane.ui.interest;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.model.Comment;

public class CommentAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Comment> mComments = new ArrayList<Comment>();

	public CommentAdapter(Context ctx) {
		this(ctx, new ArrayList<Comment>());
	}

	public CommentAdapter(Context ctx, List<? extends Comment> data) {
		mContext = ctx;
		mComments = new ArrayList<Comment>(data);
	}

	@Override
	public int getCount() {
		return mComments.size();
	}

	@Override
	public Object getItem(int position) {
		return mComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			Comment comment = mComments.get(position);
			LayoutInflater layoutInflator = LayoutInflater.from(mContext);
			convertView = layoutInflator.inflate(R.layout.comment_item, null);
			ImageView userIcon = (ImageView) convertView
					.findViewById(R.id.user_icon);
			userIcon.setBackgroundResource((Integer) comment.getDataSrc());
			TextView userName = (TextView) convertView
					.findViewById(R.id.user_name);
			userName.setText(comment.getUserName());
			RatingBar rb = (RatingBar) convertView
					.findViewById(R.id.ratingBar1);
			rb.setRating(comment.getRate());
			TextView content = (TextView) convertView
					.findViewById(R.id.comment_content);
			content.setText(comment.getComment());
		}
		return convertView;
	}

	public void loadData(List<Comment> datas, boolean isAppend) {
		if (isAppend) {
			mComments.addAll(datas);
		} else {
			if (!mComments.isEmpty()) {
				mComments.clear();
			}
			mComments.addAll(datas);
		}
	}
}
