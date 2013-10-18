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
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.constants.CommonConstants;
import com.beacon.afterui.provider.CacheManager;
import com.beacon.afterui.sliding.customViews.CustomGridView;
import com.beacon.afterui.utils.ImageUtils;
import com.beacon.afterui.utils.WindowUtils;
import com.beacon.afterui.views.MainActivity;
import com.beacon.afterui.views.data.Interest;
import com.beacon.afterui.views.data.InterestController;

public class DetailFragment extends Fragment implements FragmentLifecycle{
	private CustomGridView mMoreView = null;
	private ViewGroup mDetailView = null;
	
	private int mCacheKey;
	
	private int mSelectedPostion = -1;
	
	private boolean isBacking = false;
	
	Context mContext;
	
	Typeface typeFaceRegular;
	Typeface typeFaceBold;
	Typeface typefaceBlack;
	Typeface typefaceItalic;
	
	
	public DetailFragment(Context mContext) {
		this.mContext = mContext;
		typeFaceRegular = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-Regular.otf");
		typeFaceBold = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-Semibold.otf");
		typefaceBlack = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-Light.otf");
		typefaceItalic = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/MyriadPro-It.otf");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.detail_box, null);
		mDetailView = (ViewGroup)view.findViewById(R.id.detail_content);
		if(getArguments() != null) {
			mCacheKey = getArguments().getInt(CommonConstants.BundleKey.CACHE_KEY);
			mSelectedPostion = getArguments().getInt(CommonConstants.BundleKey.SELECTED_POSITION);
		}
		return view;
	}

	
	private void updateDetailView() {
		if(mCacheKey == CacheManager.NO_RESULT_KEY) {
			return;
		}
		SparseArray datas = InterestController.getInstance(getActivity()).getCacheManager().readCache(mCacheKey).getResultData();
		Interest data = (Interest) datas.get(mSelectedPostion);

		ImageView iv = (ImageView) mDetailView.findViewById(R.id.detail_pic);
		if (data.getDataSrc() instanceof String) {
			ImageUtils.getInstance(getActivity()).loadImage(data.getDataSrc(),
					iv);
		} else if (data.getDataSrc() instanceof Integer) {
			ImageUtils.getInstance(getActivity()).resizeImage(
					data.getDataSrc(), iv);
		}
		TextView nameView = (TextView) mDetailView
				.findViewById(R.id.detail_name);
		nameView.setTypeface(typeFaceBold);
		TextView ageView = (TextView) mDetailView.findViewById(R.id.detail_age);
		ageView.setTypeface(typeFaceRegular);
		TextView albumCount = (TextView) mDetailView
				.findViewById(R.id.detail_album_count);
		albumCount.setTypeface(typeFaceRegular);
		TextView statusView = (TextView) mDetailView
				.findViewById(R.id.detail_status);
		statusView.setTypeface(typefaceItalic);
		TextView lastLoginView = (TextView) mDetailView
				.findViewById(R.id.detail_lastlogin);
		lastLoginView.setTypeface(typefaceBlack);
		TextView lastLoginTime = (TextView) mDetailView
				.findViewById(R.id.detail_lastlogin_time);
		lastLoginTime.setTypeface(typefaceBlack);
		TextView likeCountView = (TextView) mDetailView
				.findViewById(R.id.detail_like_text);
		likeCountView.setTypeface(typeFaceRegular);
		TextView commentCountView = (TextView) mDetailView
				.findViewById(R.id.detail_comment_count);
		commentCountView.setTypeface(typeFaceRegular);
		TextView add_req_text = (TextView) mDetailView
				.findViewById(R.id.detail_chat_req_text);
		add_req_text.setTypeface(typeFaceRegular);
		nameView.setText(data.getName());
		ageView.setText(mContext.getResources().getString(R.string.IDS_AGE) + data.getAge());
		albumCount.setText(mContext.getResources().getString(R.string.IDS_OPEN_BRACE) + data.getAlbum_photo_count() + mContext.getResources().getString(R.string.IDS_CLOSE_BRACE));
		statusView.setText(data.getStatus());
		lastLoginView.setText(data.getLast_online());
		lastLoginTime.setText(data.getLast_online_time());
		likeCountView.setText(data.getProfile_likes() + mContext.getResources().getString(R.string.IDS_LIKES));
		commentCountView.setText(data.getProfile_comments_count() +  mContext.getResources().getString(R.string.IDS_COMMENTS));
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
		//displayViews();
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
		if(!isBacking) {
			isBacking = true;
			((MainActivity)getActivity()).updateToMainScreenActionBar();
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
		MarginLayoutParams lp = (MarginLayoutParams) mDetailView.getLayoutParams();
	}
	
	private int getWindowWidth(Configuration config) {
		Rect window = WindowUtils.getRootViewVisibleDisplayFrame(getActivity());
		int height = window.height();
		int width = window.width();
		if(config == null) {
			return width;
		}
		if(config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			return height > width ? width : height;
		} else {
			return height > width ? height: width;
		}
	}
	
	private int getWindowHeight(Configuration config) {
		Rect window = WindowUtils.getRootViewVisibleDisplayFrame(getActivity());
		int height = window.height();
		int width = window.width();
		if(config == null) {
			return height;
		}
		if(config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			return height < width ? width : height;
		} else {
			return height < width ? height: width;
		}
	}

}
