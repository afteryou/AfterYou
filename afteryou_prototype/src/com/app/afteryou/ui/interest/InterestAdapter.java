package com.app.afteryou.ui.interest;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.afteryou.model.Interest;
import com.app.afteryou.ui.staggered.StaggeredGridView;
import com.app.afteryou.ui.view.ScaleImageView;
import com.app.afteryou.ui.view.ScaleImageView.ImageChangedListener;
import com.app.afteryou.utils.ImageUtils;
import com.app.afteryou.R;

public class InterestAdapter extends BaseAdapter {

	private Context mContext;
	private LinkedList<Interest> mInterests;

	public InterestAdapter(Context context) {
		this(context, new LinkedList<Interest>());
	}

	public InterestAdapter(Context ctx, LinkedList<? extends Interest> datas) {
		mContext = ctx;
		mInterests = new LinkedList<Interest>(datas);
		// mImageFetcher.setExitTasksEarly(false);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Interest sp = mInterests.get(position);
		convertView = convertDataToView(sp, convertView, parent);
		return convertView;
	}

	@Override
	public int getCount() {
		return mInterests.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mInterests.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return mInterests.get(arg0).hashCode();
	}

	public void addItemLast(List<Interest> datas) {
		mInterests.addAll(datas);
	}

	public void addItemTop(List<Interest> datas) {
		mInterests.clear();
		mInterests.addAll(datas);
	}
	
	public View cloneView(int position, ViewHolder holder) {
		Interest sp = (Interest) getItem(position);
		if(sp == null) {
			return null;
		}
		LayoutInflater layoutInflator = LayoutInflater.from(mContext);
		View result = layoutInflator.inflate(R.layout.infos_list, null);
		ScaleImageView imageView = (ScaleImageView) result
				.findViewById(R.id.news_pic);
		TextView descView = (TextView) result.findViewById(R.id.news_desc);
		TextView nameView = (TextView) result.findViewById(R.id.news_name);
		TextView disView = (TextView) result.findViewById(R.id.news_distance);

		View divider = (View) result.findViewById(R.id.news_divider);
		if (sp.getDescription() == null || sp.getDescription() == "") {
			divider.setVisibility(View.GONE);
			descView.setVisibility(View.GONE);
		} else {
			divider.setVisibility(View.VISIBLE);
			descView.setVisibility(View.VISIBLE);
			descView.setText(sp.getDescription());
		}
		if (holder != null && holder.imageView.getDrawable() != null) {
			imageView.setImageDrawable(holder.imageView.getDrawable());
		}
		nameView.setText(sp.getPlaceName());
		disView.setText(sp.getDistance());
		return result;
	}

	private View convertDataToView(Interest sp, View convertView, final ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(mContext);
			convertView = layoutInflator.inflate(R.layout.infos_list, null);
			holder = new ViewHolder();
			holder.imageView = (ScaleImageView) convertView
					.findViewById(R.id.news_pic);
			holder.descView = (TextView) convertView
					.findViewById(R.id.news_desc);
			holder.nameView = (TextView) convertView
					.findViewById(R.id.news_name);
			holder.disView = (TextView) convertView
					.findViewById(R.id.news_distance);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		View divider = (View) convertView.findViewById(R.id.news_divider);
		if (sp.getDescription() == null || sp.getDescription() == "") {
			divider.setVisibility(View.GONE);
			holder.descView.setVisibility(View.GONE);
		} else {
			divider.setVisibility(View.VISIBLE);
			holder.descView.setVisibility(View.VISIBLE);
			holder.descView.setText(sp.getDescription());
		}
		holder.nameView.setText(sp.getPlaceName());
		holder.disView.setText(sp.getDistance());
		convertView.setTag(holder);
		if(sp.getDataSrc() != null){
			if(sp.getDataSrc() instanceof Integer) {
				((ScaleImageView)holder.imageView).setImageResource((Integer) sp.getDataSrc());//TODO FIXME
			} else if (sp.getDataSrc() instanceof String 
					&& !sp.getDataSrc().equals(holder.dataSrc)) {
				holder.dataSrc = sp.getDataSrc();
				ImageUtils.getInstance(mContext).loadImage(sp.getDataSrc(), holder.imageView);
				((ScaleImageView)holder.imageView).setImageChangedListener(new ImageChangedListener() {

					@Override
					public void changed() {
						if(parent instanceof StaggeredGridView) {
							((StaggeredGridView)parent).rePopulateView();
						}
					}
				});
			}
		}
		return convertView;
	}

	public class ViewHolder {
		ScaleImageView imageView;
		TextView descView;
		TextView nameView;
		TextView disView;
		Object dataSrc;
	}
}
