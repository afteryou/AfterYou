package com.beacon.afterui.views.data;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.imageUtils.ScaleImageView;
import com.beacon.afterui.imageUtils.ScaleImageView.ImageChangedListener;
import com.beacon.afterui.sliding.customViews.StaggeredGridView;
import com.beacon.afterui.utils.ImageUtils;
import com.beacon.afterui.views.data.InterestController.InterestClickListener;

public class InterestAdapter extends BaseAdapter {

	private Context mContext;
	private LinkedList<Interest> mInterests;

	private InterestClickListener clickListner;

	Typeface typeFaceRegular;
	Typeface typeFaceBold;
	Typeface typefaceBlack;
	Typeface typefaceItalic;

	public InterestAdapter(Context context, InterestClickListener clickListner) {
		this(context, new LinkedList<Interest>());
		this.clickListner = clickListner;
	}

	public InterestAdapter(Context ctx, LinkedList<? extends Interest> datas) {
		mContext = ctx;
		mInterests = new LinkedList<Interest>(datas);
		typeFaceRegular = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-Regular.otf");
		typeFaceBold = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-Semibold.otf");
		typefaceBlack = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-Light.otf");
		typefaceItalic = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-It.otf");
		// mImageFetcher.setExitTasksEarly(false);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Interest sp = mInterests.get(position);
		convertView = convertDataToView(sp, position, convertView, parent);
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
		if (sp == null) {
			return null;
		}
		LayoutInflater layoutInflator = LayoutInflater.from(mContext);
		View result = layoutInflator.inflate(R.layout.infos_list, null);
		ScaleImageView imageView = (ScaleImageView) result
				.findViewById(R.id.news_pic);
		TextView nameView = (TextView) result.findViewById(R.id.news_name);
		nameView.setTypeface(typeFaceBold);
		// TextView ageView = (TextView) result.findViewById(R.id.news_age);
		// ageView.setTypeface(typeFaceRegular);
		// TextView albumCount = (TextView) result
		// .findViewById(R.id.news_album_count);
		// albumCount.setTypeface(typeFaceRegular);
		TextView statusView = (TextView) result.findViewById(R.id.news_status);
		statusView.setTypeface(typefaceItalic);
		// TextView lastLoginView = (TextView) result
		// .findViewById(R.id.news_lastlogin);
		// lastLoginView.setTypeface(typefaceBlack);
		// TextView lastLoginTime = (TextView) result
		// .findViewById(R.id.news_lastlogin_time);
		// lastLoginTime.setTypeface(typefaceBlack);
		// TextView likeCountView = (TextView) result
		// .findViewById(R.id.news_like_text);
		// likeCountView.setTypeface(typeFaceRegular);
		// TextView commentCountView = (TextView) result
		// .findViewById(R.id.news_comment_count);
		// commentCountView.setTypeface(typeFaceRegular);

		// TextView add_req_text = (TextView) result
		// .findViewById(R.id.news_add_req_text);
		// add_req_text.setTypeface(typeFaceRegular);

		View divider = (View) result.findViewById(R.id.news_divider);
		// if (sp.getDescription() == null || sp.getDescription() == "") {
		// divider.setVisibility(View.GONE);
		// descView.setVisibility(View.GONE);
		// } else {
		divider.setVisibility(View.VISIBLE);
		// descView.setVisibility(View.VISIBLE);
		// descView.setText(sp.getDescription());
		// }
		if (holder != null && holder.imageView.getDrawable() != null) {
			imageView.setImageDrawable(holder.imageView.getDrawable());
		}
		nameView.setText(sp.getName());
		// ageView.setText(mContext.getResources().getString(R.string.IDS_AGE)
		// + sp.getAge());
		// albumCount.setText(mContext.getResources().getString(
		// R.string.IDS_OPEN_BRACE)
		// + sp.getAlbum_photo_count()
		// + mContext.getResources().getString(R.string.IDS_CLOSE_BRACE));
		statusView.setText(sp.getStatus());
		// lastLoginView.setText(sp.getLast_online());
		// lastLoginTime.setText(sp.getLast_online_time());
		// likeCountView.setText(sp.getProfile_likes()
		// + mContext.getResources().getString(R.string.IDS_LIKES));
		// commentCountView.setText(sp.getProfile_comments_count()
		// + mContext.getResources().getString(R.string.IDS_COMMENTS));
		return result;
	}

	private View convertDataToView(Interest sp, int position, View convertView,
			final ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(mContext);
			convertView = layoutInflator.inflate(R.layout.infos_list, null);
			holder = new ViewHolder();
			holder.imageView = (ScaleImageView) convertView
					.findViewById(R.id.news_pic);
			holder.nameView = (TextView) convertView
					.findViewById(R.id.news_name);
			holder.nameView.setTypeface(typeFaceBold);
			// holder.ageView = (TextView)
			// convertView.findViewById(R.id.news_age);
			// holder.ageView.setTypeface(typeFaceRegular);
			// holder.albumCount = (TextView) convertView
			// .findViewById(R.id.news_album_count);
			// holder.albumCount.setTypeface(typeFaceRegular);
			holder.statusView = (TextView) convertView
					.findViewById(R.id.news_status);
			holder.statusView.setTypeface(typefaceItalic);
			// holder.lastLoginView = (TextView) convertView
			// .findViewById(R.id.news_lastlogin);
			// holder.lastLoginView.setTypeface(typefaceBlack);
			// holder.lastLoginTime = (TextView) convertView
			// .findViewById(R.id.news_lastlogin_time);
			// holder.lastLoginTime.setTypeface(typefaceBlack);
			// holder.likeCountView = (TextView) convertView
			// .findViewById(R.id.news_like_text);
			// holder.likeCountView.setTypeface(typeFaceRegular);
			// holder.commentCountView = (TextView) convertView
			// .findViewById(R.id.news_comment_count);
			// holder.commentCountView.setTypeface(typeFaceRegular);
			holder.add_req_text = (ImageView) convertView
					.findViewById(R.id.vote_btn);
			// holder.add_req_text.setTypeface(typeFaceRegular);
			holder.checkDetails = (Button) convertView
					.findViewById(R.id.news_details);
			holder.checkDetails.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(mContext,
							"Clicked Button = " + ((Integer) v.getTag()),
							Toast.LENGTH_LONG).show();
					clickListner.onItemClick((Integer) v.getTag());
				}
			});
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		View divider = (View) convertView.findViewById(R.id.news_divider);
		// if (sp.getDescription() == null || sp.getDescription() == "") {
		// divider.setVisibility(View.GONE);
		// holder.descView.setVisibility(View.GONE);
		// } else {
		divider.setVisibility(View.VISIBLE);
		// holder.descView.setVisibility(View.VISIBLE);
		// holder.descView.setText(sp.getDescription());
		// }
		holder.nameView.setText(sp.getName());
		// holder.ageView.setText("Age : " + sp.getAge());
		// holder.albumCount.setText("(" + sp.getAlbum_photo_count() + ")");
		holder.statusView.setText(sp.getStatus());
		// holder.lastLoginView.setText(sp.getLast_online());
		// holder.lastLoginTime.setText(sp.getLast_online_time());
		// holder.likeCountView.setText(sp.getProfile_likes() + " likes");
		// holder.commentCountView.setText(sp.getProfile_comments_count()
		// + " comments");
		holder.checkDetails.setTag(new Integer(position));
		convertView.setTag(holder);
		if (sp.getDataSrc() != null) {
			if (sp.getDataSrc() instanceof Integer) {
				((ScaleImageView) holder.imageView)
						.setImageResource((Integer) sp.getDataSrc());// TODO
																		// FIXME
			} else if (sp.getDataSrc() instanceof String
					&& !sp.getDataSrc().equals(holder.dataSrc)) {
				holder.dataSrc = sp.getDataSrc();
				ImageUtils.getInstance(mContext).loadImage(sp.getDataSrc(),
						holder.imageView);
				((ScaleImageView) holder.imageView)
						.setImageChangedListener(new ImageChangedListener() {

							@Override
							public void changed() {
								if (parent instanceof StaggeredGridView) {
									((StaggeredGridView) parent)
											.rePopulateView();
								}
							}
						});
			}
		}
		return convertView;
	}

	public class ViewHolder {
		ImageView add_req_text;
		ScaleImageView imageView;
		TextView nameView;
		// TextView ageView;
		// TextView albumCount;
		TextView statusView;
		// TextView lastLoginView;
		// TextView lastLoginTime;
		// TextView likeCountView;
		// TextView commentCountView;
		Button checkDetails;
		Object dataSrc;
	}
}
