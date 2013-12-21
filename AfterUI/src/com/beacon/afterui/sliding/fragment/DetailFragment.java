package com.beacon.afterui.sliding.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.constants.CommonConstants;
import com.beacon.afterui.provider.CacheManager;
import com.beacon.afterui.sliding.customViews.CustomGridView;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.utils.ImageUtils;
import com.beacon.afterui.utils.WindowUtils;
import com.beacon.afterui.views.MainActivity;
import com.beacon.afterui.views.data.Interest;
import com.beacon.afterui.views.data.InterestController;

public class DetailFragment extends Fragment implements FragmentLifecycle,
		OnClickListener {
	private CustomGridView mMoreView = null;
	private ViewGroup mDetailView = null;

	private int mCacheKey;

	private int mSelectedPostion = -1;

	private boolean isBacking = false;

	private Context mContext;
	private int mNumNotification = 25;
	private int mNumHot = 25;
	private int mNumComment = 3;

	private Typeface mMyriadProRegularFont;
	Typeface typeFaceBold;
	Typeface typefaceBlack;
	Typeface typefaceItalic;

	private TextView mVoteBtn;
	private TextView mConnectBtn;
	private TextView mRefferBtn;
	private TextView mChatBtn;

	private TextView mPhotosBtn;
	private TextView mGiftDetailBtn;
	private TextView mVideoDetailBtn;
	private TextView mCommentBtn;

	private TextView mNotificationTxt;
	private TextView mEditBtn;
	private TextView mNumHotTxt;
	private TextView mHomeLocationTxt;
	private TextView mFromLocation;
	private TextView mUserName;

	private ListView mCommentList;

	private LayoutInflater mInflater;

	private Typeface mITCAvantGardeStdBk;

	public DetailFragment(Context mContext) {
		this.mContext = mContext;
		mMyriadProRegularFont = FontUtils.loadTypeFace(this.mContext,
				"MyriadPro-Regular.otf");
		typeFaceBold = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-Semibold.otf");
		typefaceBlack = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-Light.otf");
		typefaceItalic = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-It.otf");

		mITCAvantGardeStdBk = FontUtils.loadTypeFace(this.mContext,
				FontUtils.ITC_AVANT_GARDE_STD_BK);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.detail_box, null);
		mDetailView = (ViewGroup) view.findViewById(R.id.detail_content);

		mCommentList = (ListView) mDetailView.findViewById(R.id.comment_list);
		mCommentList.setAdapter(new CommentAdapter());
		Helper.getListViewSize(mCommentList);

		mPhotosBtn = (TextView) view.findViewById(R.id.photos_btn);
		mPhotosBtn.setTypeface(mITCAvantGardeStdBk);

		mGiftDetailBtn = (TextView) view.findViewById(R.id.gift_btn);
		mGiftDetailBtn.setTypeface(mITCAvantGardeStdBk);

		mVideoDetailBtn = (TextView) view.findViewById(R.id.video_btn);
		mVideoDetailBtn.setTypeface(mITCAvantGardeStdBk);

		mCommentBtn = (TextView) view.findViewById(R.id.comment_btn);
		mCommentBtn.setTypeface(mITCAvantGardeStdBk);

		mVoteBtn = (TextView) view.findViewById(R.id.vote_btn_detail);
		mVoteBtn.setTypeface(mITCAvantGardeStdBk);

		mConnectBtn = (TextView) view.findViewById(R.id.connect_btn_detail);
		mVoteBtn.setTypeface(mITCAvantGardeStdBk);

		mRefferBtn = (TextView) view.findViewById(R.id.referr_btn_detail);
		mConnectBtn.setTypeface(mITCAvantGardeStdBk);

		mChatBtn = (TextView) view.findViewById(R.id.chat_btn_detail);
		mChatBtn.setTypeface(mITCAvantGardeStdBk);

		mNotificationTxt = (TextView) view.findViewById(R.id.notification_txt);
		mNotificationTxt.setText("Notifications " + "(" + mNumNotification
				+ ")");
		mNotificationTxt.setTypeface(mITCAvantGardeStdBk);

		// edit btn on img
		mEditBtn = (TextView) view.findViewById(R.id.edit_btn);
		mEditBtn.setTypeface(mITCAvantGardeStdBk);
		mEditBtn.setOnClickListener(this);

		// num hot
		mNumHotTxt = (TextView) view.findViewById(R.id.num_hot);
		mNumHotTxt.setTypeface(mITCAvantGardeStdBk);
		mNumHotTxt.setText("Hot " + "(" + mNumHot + ")");

		TextView last_update_txt = (TextView) view
				.findViewById(R.id.last_update_txt);
		last_update_txt.setTypeface(mITCAvantGardeStdBk);

		TextView lastUpdateStatus = (TextView) view
				.findViewById(R.id.last_update_status);
		lastUpdateStatus.setTypeface(mITCAvantGardeStdBk);

		TextView home_txt = (TextView) view.findViewById(R.id.home_txt);
		home_txt.setTypeface(mITCAvantGardeStdBk);

		mHomeLocationTxt = (TextView) view.findViewById(R.id.home_location);
		mHomeLocationTxt.setTypeface(mITCAvantGardeStdBk);

		TextView from_txt = (TextView) view.findViewById(R.id.from_txt);
		from_txt.setTypeface(mITCAvantGardeStdBk);

		mFromLocation = (TextView) view.findViewById(R.id.from_location);
		mFromLocation.setTypeface(mITCAvantGardeStdBk);

		TextView edit_txt = (TextView) view.findViewById(R.id.edit_txt);
		edit_txt.setTypeface(mITCAvantGardeStdBk);

		TextView sponser_txt = (TextView) view.findViewById(R.id.sponser_txt);
		sponser_txt.setTypeface(mITCAvantGardeStdBk);

		mUserName = (TextView) view.findViewById(R.id.user_name);
		mUserName.setTypeface(mITCAvantGardeStdBk);

		TextView and_txt = (TextView) view.findViewById(R.id.and_txt);
		and_txt.setTypeface(mITCAvantGardeStdBk);

		TextView other_txt = (TextView) view.findViewById(R.id.other_txt);
		other_txt.setTypeface(mITCAvantGardeStdBk);

		TextView think_txt = (TextView) view.findViewById(R.id.think_txt);
		think_txt.setTypeface(mITCAvantGardeStdBk);

		TextView comments_txt = (TextView) view.findViewById(R.id.comments_txt);
		comments_txt.setTypeface(mITCAvantGardeStdBk);
		comments_txt.setText("Comments " + "(" + mNumComment + ")");

		TextView my_story_label = (TextView) view
				.findViewById(R.id.my_story_label);
		my_story_label.setTypeface(mITCAvantGardeStdBk);

		TextView my_story_text = (TextView) view
				.findViewById(R.id.my_story_text);
		my_story_text.setTypeface(mMyriadProRegularFont);

		TextView my_match_label = (TextView) view
				.findViewById(R.id.my_match_label);
		my_match_label.setTypeface(mITCAvantGardeStdBk);

		TextView my_match_text = (TextView) view
				.findViewById(R.id.my_match_text);
		my_match_text.setTypeface(mMyriadProRegularFont);

		mPhotosBtn.setOnClickListener(this);
		mGiftDetailBtn.setOnClickListener(this);
		mVideoDetailBtn.setOnClickListener(this);
		mCommentBtn.setOnClickListener(this);

		mVoteBtn.setOnClickListener(this);
		mConnectBtn.setOnClickListener(this);
		mRefferBtn.setOnClickListener(this);
		mChatBtn.setOnClickListener(this);

		if (getArguments() != null) {
			mCacheKey = getArguments().getInt(
					CommonConstants.BundleKey.CACHE_KEY);
			mSelectedPostion = getArguments().getInt(
					CommonConstants.BundleKey.SELECTED_POSITION);
		}
		return view;
	}

	@Override
	public void onClick(View v) {

		Bundle bundle = new Bundle();

		switch (v.getId()) {
		case R.id.photos_btn:

			break;
		case R.id.gift_btn:

			Fragment giftListFragment = new GiftListFragment();
			FragmentHelper.gotoFragment(getActivity(), DetailFragment.this,
					giftListFragment, bundle);
			break;
		case R.id.video_btn:

			break;
		case R.id.comment_btn:

			break;

		case R.id.vote_btn:

			break;
		case R.id.connect_btn_detail:

			break;
		case R.id.referr_btn_detail:

			break;
		case R.id.chat_btn_detail:

			break;
		case R.id.edit_txt:

			break;

		}

	}

	private void updateDetailView() {
		if (mCacheKey == CacheManager.NO_RESULT_KEY) {
			return;
		}
		SparseArray datas = InterestController.getInstance(getActivity())
				.getCacheManager().readCache(mCacheKey).getResultData();
		Interest data = (Interest) datas.get(mSelectedPostion);

		ImageView iv = (ImageView) mDetailView.findViewById(R.id.detail_pic);
		if (data.getDataSrc() instanceof String) {
			ImageUtils.getInstance(getActivity()).loadImage(data.getDataSrc(),
					iv);
		} else if (data.getDataSrc() instanceof Integer) {
			ImageUtils.getInstance(getActivity()).resizeImage(
					data.getDataSrc(), iv);
		}
		// Detail view

		TextView nameView = (TextView) mDetailView
				.findViewById(R.id.detail_name);
		nameView.setTypeface(mITCAvantGardeStdBk);

		TextView ageView = (TextView) mDetailView.findViewById(R.id.detail_age);
		ageView.setTypeface(mITCAvantGardeStdBk);

		// TextView albumCount = (TextView) mDetailView
		// .findViewById(R.id.detail_album_count);
		// albumCount.setTypeface(mITCAvantGardeStdBk);
		TextView statusView = (TextView) mDetailView
				.findViewById(R.id.detail_status);
		statusView.setTypeface(mITCAvantGardeStdBk);
		// TextView lastLoginView = (TextView) mDetailView
		// .findViewById(R.id.detail_lastlogin);
		// lastLoginView.setTypeface(mITCAvantGardeStdBk);
		// TextView lastLoginTime = (TextView) mDetailView
		// .findViewById(R.id.detail_lastlogin_time);
		// lastLoginTime.setTypeface(mITCAvantGardeStdBk);
		// TextView likeCountView = (TextView) mDetailView
		// .findViewById(R.id.detail_like_text);
		// likeCountView.setTypeface(mITCAvantGardeStdBk);
		// TextView commentCountView = (TextView) mDetailView
		// .findViewById(R.id.detail_comment_count);
		// commentCountView.setTypeface(mITCAvantGardeStdBk);
		// TextView add_req_text = (TextView) mDetailView
		// .findViewById(R.id.detail_chat_req_text);
		// add_req_text.setTypeface(mITCAvantGardeStdBk);
		nameView.setText(data.getName());
		ageView.setText(mContext.getResources().getString(R.string.IDS_AGE)
				+ data.getAge());
		// albumCount.setText(mContext.getResources().getString(
		// R.string.IDS_OPEN_BRACE)
		// + data.getAlbum_photo_count()
		// + mContext.getResources().getString(R.string.IDS_CLOSE_BRACE));
		statusView.setText(data.getStatus());
		// lastLoginView.setText(data.getLast_online());
		// lastLoginTime.setText(data.getLast_online_time());
		// likeCountView.setText(data.getProfile_likes()
		// + mContext.getResources().getString(R.string.IDS_LIKES));
		// commentCountView.setText(data.getProfile_comments_count()
		// + mContext.getResources().getString(R.string.IDS_COMMENTS));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		// displayViews();
		updateDetailView();
		fixDetailViewY(null);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onFragmentPause() {

	}

	@Override
	public void onFragmentResume() {
		System.out.println("[onFragmentResume]");
	}

	@Override
	public boolean onBack() {
		if (!isBacking) {
			isBacking = true;
			((MainActivity) getActivity()).updateToMainScreenActionBar();
			applyBackAnimation();
		}
		return true;
	}

	private void applyBackAnimation() {
		AnimatorSet fideOutMap = new AnimatorSet();
		fideOutMap.setDuration(250);
		fideOutMap.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator anim) {
				FragmentHelper.popFragment(getActivity());
				isBacking = false;
			}
		});

		fideOutMap.start();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		fixDetailViewY(newConfig);
	}

	private void fixDetailViewY(Configuration config) {
		mDetailView.setVisibility(View.VISIBLE);
		MarginLayoutParams lp = (MarginLayoutParams) mDetailView
				.getLayoutParams();
	}

	private int getWindowWidth(Configuration config) {
		Rect window = WindowUtils.getRootViewVisibleDisplayFrame(getActivity());
		int height = window.height();
		int width = window.width();
		if (config == null) {
			return width;
		}
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			return height > width ? width : height;
		} else {
			return height > width ? height : width;
		}
	}

	private int getWindowHeight(Configuration config) {
		Rect window = WindowUtils.getRootViewVisibleDisplayFrame(getActivity());
		int height = window.height();
		int width = window.width();
		if (config == null) {
			return height;
		}
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			return height < width ? width : height;
		} else {
			return height < width ? height : width;
		}
	}

	private class CommentAdapter extends BaseAdapter {

		public CommentAdapter() {

			mInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {

			ViewHolder holder;

			if (view == null) {
				holder = new ViewHolder();
				view = mInflater.inflate(R.layout.comments_item_detail, null);

				holder.commentTxt = (TextView) view
						.findViewById(R.id.comment_txt);
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

}
