package com.beacon.afterui.sliding.fragment;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.constants.CommonConstants;
import com.beacon.afterui.log.AfterUIlog;
import com.beacon.afterui.provider.CacheManager;
import com.beacon.afterui.sliding.customViews.CustomGridView;
import com.beacon.afterui.sliding.customViews.CustomGridView.IDataListener;
import com.beacon.afterui.utils.ImageUtils;
import com.beacon.afterui.utils.WindowUtils;
import com.beacon.afterui.views.MainActivity;
import com.beacon.afterui.views.data.Interest;
import com.beacon.afterui.views.data.InterestAdapter;
import com.beacon.afterui.views.data.InterestController;
import com.beacon.afterui.views.data.InterestController.InterestCallBack;


public class ContentFragment extends Fragment implements FragmentLifecycle, ISearchFunction {
	
	private CustomGridView mAdapterView = null;
	private ViewGroup mDetailBox = null;
	private InterestAdapter mAdapter = null;
	private InterestController mController;
	private final static int CAPACITY = 50;
	
	private boolean inDetail = false;
	
	private InterestCallBack mCallBack;
	
	public class AnimState implements Parcelable {
		int centerLeft = 0;
        int centerTop = 0;
        float centerScaleX = 0.0f;
        float centerScaleY = 0.0f;
        float centerAngle = 0.0f;
		@Override
		public int describeContents() {
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(this.centerLeft);
			dest.writeInt(this.centerTop);
			dest.writeFloat(this.centerScaleX);
			dest.writeFloat(this.centerScaleY);
			dest.writeFloat(this.centerAngle);
		}
		
		public final Parcelable.Creator<AnimState> CREATOR = new Parcelable.Creator<AnimState>() {

			@Override
			public AnimState createFromParcel(Parcel source) {
				AnimState state = new AnimState();
				if(source != null) {
					state.centerLeft = source.readInt();
					state.centerTop = source.readInt();
					state.centerScaleX = source.readFloat();
					state.centerScaleY = source.readFloat();
					state.centerAngle = source.readFloat();
				}
				return state;
			}

			@Override
			public AnimState[] newArray(int size) {
				return new AnimState[size];
			}
		};
	}
	
	private AnimState mAnimState = new AnimState();
	private int mCacheKey = -1;
	
	public ContentFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mController = InterestController.getInstance(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment, null);
		
		mAdapterView = (CustomGridView) view.findViewById(R.id.list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setCapacity(CAPACITY);
		mAdapterView.setDataListener(new IDataListener() {
			@Override
			public void onLoadMore() {
				mController.loadInterests(InterestController.REQUEST_TYPE_APPEND, mCallBack, mCacheKey);
			}
		});

		mAdapterView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SparseArray datas = InterestController.getInstance(getActivity()).getCacheManager().readCache(mCacheKey).getResultData();
				Interest data = (Interest) datas.get(position);
//				((MainActivity)getActivity()).updateToDetailsScreenActionBar(data.getPlaceName());
				applyAnimation();
				inDetail = true;
			}
		});
		mAdapter = new InterestAdapter(getActivity());
		mAdapterView.setAdapter(mAdapter);
		mCallBack = new InterestCallBack() {

			@Override
			public void onResult(int type, Object result) {
				mCacheKey = (Integer)result;
				if(mCacheKey == CacheManager.NO_RESULT_KEY) {
					return;
				}
				SparseArray datas = InterestController.getInstance(getActivity()).getCacheManager().readCache(mCacheKey).getLastAppended();
				List<Interest> data = new ArrayList<Interest>();
				for(int i=0; i < datas.size(); i++) {
					data.add((Interest) datas.get(i));
				}
				
				switch (type) {
				case InterestController.REQUEST_TYPE_APPEND:
					mAdapter.addItemLast(data);
					//mAdapter.notifyDataSetChanged();
					break;
				case InterestController.REQUEST_TYPE_INIT:
				case InterestController.REQUEST_TYPE_REFRESH:
				case InterestController.REQUEST_TYPE_CATEGORY:
				case InterestController.REQUEST_TYPE_BRAND:
					mAdapter.addItemTop(data);
					mAdapter.notifyDataSetChanged();
					break;
					default:
						AfterUIlog.e("ContentFragment", "Unsupported type!");
				}
			}
		};
		return view;
	}
	
	private void applyBackAnimation() {
		final CustomGridView list = mAdapterView;
		InterestAdapter adapter = mAdapter;
		final View selected = list.getSelectedView();
		int position = list.getSelectedItemPosition();
		if(selected == null) {
			list.setAlpha(1f);
			list.setVisibility(View.VISIBLE);
			list.setEnabled(true);
			return;
		}
		final View temp = adapter.cloneView(position, (InterestAdapter.ViewHolder)selected.getTag());
		temp.setVisibility(View.GONE);

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(selected.getMeasuredWidth(), selected.getMeasuredHeight());
		params.leftMargin = selected.getLeft();
		params.topMargin = selected.getTop();
		temp.setLayoutParams(params);
		((ViewGroup)getView()).addView(temp);

		final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();
        
        boolean isGlobalCoordinate = getView().getGlobalVisibleRect(finalBounds, globalOffset);
        selected.getGlobalVisibleRect(finalBounds);
        if (isGlobalCoordinate) {
	        finalBounds.offset(-globalOffset.x, -globalOffset.y);
		}
        
        AnimatorSet fideOut = new AnimatorSet();
        fideOut.play(ObjectAnimator
				.ofFloat(list, 
						View.ALPHA, 0f, 1f));
        fideOut.setDuration(500);
        fideOut.addListener(new AnimatorListenerAdapter() {
        	@Override
            public void onAnimationEnd(Animator anim) {
        		list.setEnabled(true);
        	}
        });
        
        final View detailBox = mDetailBox;
        
        AnimatorSet set1 = new AnimatorSet();

        set1.play(ObjectAnimator
        		.ofFloat(detailBox, 
        				View.TRANSLATION_X, -mAnimState.centerLeft))
    				.with(ObjectAnimator
    						.ofFloat(detailBox, 
    								View.TRANSLATION_Y, -mAnimState.centerTop))
                    .with(ObjectAnimator
                            .ofFloat(detailBox, 
                                    View.SCALE_X, mAnimState.centerScaleX/2 + 0.5f))
                    .with(ObjectAnimator
                            .ofFloat(detailBox, 
                                    View.SCALE_Y, mAnimState.centerScaleY/2 + 0.5f))
                    .with(ObjectAnimator.ofFloat(detailBox, "rotationY", mAnimState.centerAngle));
        set1.setDuration(250);
		set1.setInterpolator(new AccelerateInterpolator());
		set1.addListener(new AnimatorListenerAdapter() {
        	 @Override
             public void onAnimationEnd(Animator anim) {
        		 detailBox.setVisibility(View.GONE);
        	 }
        });
        
		AnimatorSet set2 = new AnimatorSet();
		set2.play(ObjectAnimator
				.ofFloat(temp, 
						View.TRANSLATION_X, mAnimState.centerLeft, 0))
					.with(ObjectAnimator
							.ofFloat(temp, 
									View.TRANSLATION_Y, mAnimState.centerTop, 0))
                    .with(ObjectAnimator
                            .ofFloat(temp, 
                                    View.SCALE_X, 1f/(mAnimState.centerScaleX *2) + 0.5f, 1f))
                    .with(ObjectAnimator
                            .ofFloat(temp, 
                                    View.SCALE_Y, 1f/(mAnimState.centerScaleY *2) + 0.5f, 1f))
                    .with(ObjectAnimator.ofFloat(temp, "rotationY", -mAnimState.centerAngle, 0f));
        set2.setDuration(250);
        set2.setInterpolator(new DecelerateInterpolator());
        set2.addListener(new AnimatorListenerAdapter() {
        	@Override
            public void onAnimationStart(Animator anim) {
        		temp.setVisibility(View.VISIBLE);
        		temp.bringToFront();
        		
        	}
            @Override
            public void onAnimationEnd(Animator anim) {
            	temp.setVisibility(View.GONE);
       		 	((ViewGroup)getView()).removeView(temp);
       		 	selected.setVisibility(View.VISIBLE);
            }
        });

        AnimatorSet filpOver = new AnimatorSet();
        filpOver.playSequentially(set1, set2);
        
        AnimatorSet set = new AnimatorSet();
        set.playTogether(filpOver , fideOut);
        set.start(); 
	}

	private void applyAnimation() {
		final CustomGridView list = mAdapterView;
		final InterestAdapter adapter = mAdapter;
		final View sView = list.getSelectedView();
		final int position = list.getSelectedItemPosition();
		final View temp = adapter.cloneView(position, (InterestAdapter.ViewHolder)sView.getTag());
		temp.setVisibility(View.GONE);

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(sView.getMeasuredWidth(), sView.getMeasuredHeight());
		params.leftMargin = sView.getLeft();
		params.topMargin = sView.getTop();
		temp.setLayoutParams(params);
		
		Rect startBounds = new Rect();
		Rect finalBounds = new Rect();
        Point globalOffset = new Point();
        sView.getGlobalVisibleRect(startBounds);

		boolean isGlobalCoordinate = getView().getGlobalVisibleRect(finalBounds, globalOffset);
        
		if (isGlobalCoordinate) {
	        startBounds.offset(-globalOffset.x, -globalOffset.y);
	        finalBounds.offset(-globalOffset.x, -globalOffset.y);
		}
        
        finalBounds = offsetDetailView(finalBounds);
		
		((ViewGroup)getView()).addView(temp);
		initDetailBox();
		updateDetailView(position);

		float startHeightScale = (float) startBounds.height()/finalBounds.height();
        float startWidthScale = (float) startBounds.width()/finalBounds.width() ;

        AnimatorSet fideIn = new AnimatorSet();
        fideIn.play(ObjectAnimator
				.ofFloat(list, 
						View.ALPHA, 1f, 0f));
        fideIn.setDuration(500);
        fideIn.addListener(new AnimatorListenerAdapter() {
        	@Override
            public void onAnimationStart(Animator anim) {
        		list.setEnabled(false);
        	}
        });

        AnimatorSet set1 = new AnimatorSet();
        mAnimState.centerScaleX = startWidthScale;
        mAnimState.centerScaleY = startHeightScale;
        mAnimState.centerAngle = -90f;
        mAnimState.centerLeft = (int) ((finalBounds.centerX() - startBounds.centerX()) /2);
        mAnimState.centerTop = (int) ((finalBounds.centerY() - startBounds.centerY()) /2);

        set1.play(ObjectAnimator
        		.ofFloat(temp, 
        				View.TRANSLATION_X, mAnimState.centerLeft))
    				.with(ObjectAnimator
        					.ofFloat(temp, 
        							View.TRANSLATION_Y, mAnimState.centerTop))
                    .with(ObjectAnimator
                            .ofFloat(temp, 
                                    View.SCALE_X, 1f/(mAnimState.centerScaleX *2) + 0.5f))
                    .with(ObjectAnimator
                            .ofFloat(temp, 
                                    View.SCALE_Y, 1f/(mAnimState.centerScaleY * 2) + 0.5f))
                    .with(ObjectAnimator.ofFloat(temp, "rotationY", -mAnimState.centerAngle));
        set1.setDuration(250);
		set1.setInterpolator(new AccelerateInterpolator());
		set1.addListener(new AnimatorListenerAdapter() {
        	@Override
            public void onAnimationStart(Animator anim) {
        		sView.setVisibility(View.INVISIBLE);
        		temp.setVisibility(View.VISIBLE);
        		temp.bringToFront();
        	}
        	
        	 @Override
             public void onAnimationEnd(Animator anim) {
        		 temp.setVisibility(View.GONE);
        		 ((ViewGroup)getView()).removeView(temp);
        	 }
        });
		
		final View invisiableView = mDetailBox;
		AnimatorSet set2 = new AnimatorSet();
		set2.play(ObjectAnimator
				.ofFloat(invisiableView, 
						View.TRANSLATION_X, -mAnimState.centerLeft, 0))
					.with(ObjectAnimator
							.ofFloat(invisiableView, 
									View.TRANSLATION_Y, -mAnimState.centerTop, 0))
                    .with(ObjectAnimator
                            .ofFloat(invisiableView, 
                                    View.SCALE_X, mAnimState.centerScaleX/2 + 0.5f, 1f))
                    .with(ObjectAnimator
                            .ofFloat(invisiableView, 
                                    View.SCALE_Y, mAnimState.centerScaleY/2 + 0.5f, 1f))
                    .with(ObjectAnimator.ofFloat(invisiableView, "rotationY", mAnimState.centerAngle, 0f));

        set2.setDuration(250);
        set2.setInterpolator(new DecelerateInterpolator());
        
        set2.addListener(new AnimatorListenerAdapter() {
        	@Override
            public void onAnimationStart(Animator anim) {
        		invisiableView.setVisibility(View.VISIBLE);
        		invisiableView.setAlpha(1f);
        	}
        	@Override
        	public void onAnimationEnd(Animator anim) {
//        		Fragment detail = new DetailFragment();
//	       		Bundle bundle = new Bundle();
//	       		bundle.putInt(CommonConstants.BundleKey.CACHE_KEY, mCacheKey);
//	       		bundle.putInt(CommonConstants.BundleKey.SELECTED_POSITION, position);
//	       		FragmentHelper.gotoFragment(getActivity(), ContentFragment.this, detail, bundle);
        	}
        });
		
        AnimatorSet filpOver = new AnimatorSet();
        filpOver.playSequentially(set1, set2);
        
		AnimatorSet set = new AnimatorSet();
        set.playTogether(filpOver, fideIn);
        set.start(); 
	}

	private void updateDetailView(int position) {
//		if(mCacheKey == CacheManager.NO_RESULT_KEY) {
//			return;
//		}
//		SparseArray datas = InterestController.getInstance(getActivity()).getCacheManager().readCache(mCacheKey).getResultData();
//		Interest data = (Interest) datas.get(position);
//
//		ImageView iv = (ImageView)mDetailBox.findViewById(R.id.detail_graph);
//		if(data.getDataSrc() instanceof String) {
//			ImageUtils.getInstance(getActivity()).loadImage(data.getDataSrc(), iv);
//		} else if(data.getDataSrc() instanceof Integer) {
//			ImageUtils.getInstance(getActivity()).resizeImage(data.getDataSrc(), iv);
//		}
//		if(true/*check if need display rating*/
//				&& mDetailBox.findViewWithTag(1) == null) {
//			ViewGroup container = (ViewGroup) mDetailBox.findViewById(R.id.detail_middle);
//			View reviews = this.getActivity().getLayoutInflater().inflate(R.layout.reviews_comp, null);
//			reviews.setTag(1);
//			container.addView(reviews);
//			RatingBar rb = (RatingBar) reviews
//				.findViewById(R.id.reviews_rating);
//			rb.setRating(4.5f);
//		}
//		
//		TextView tv = (TextView)mDetailBox.findViewById(R.id.detail_address);
//		tv.setText(data.getAddress());
//
//		TextView tv3 = (TextView)mDetailBox.findViewById(R.id.detail_title);
//		tv3.setText(data.getPlaceName());
//		TextView tv4 = (TextView)mDetailBox.findViewById(R.id.detail_distance);
//		tv4.setText(data.getDistance());
	}
	
	private void initDetailBox() {
//		if(mDetailBox == null) {
//			mDetailBox = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.detail_box, null);
//			((ViewGroup)getView()).addView(mDetailBox);
//		}
//		Rect window = WindowUtils.getRootViewVisibleDisplayFrame(getActivity());
//		int windowH = window.height();
//		int height = (int) (windowH * getResources().getInteger(R.integer.detail_box_rateHeight) / 100);
//		int width = window.width() - WindowUtils.dip2px(getActivity(), 10);
//		int marginTop = (int) (windowH * getResources().getInteger(R.integer.detail_view_rateY) / 100);
//		int marginLeft = WindowUtils.dip2px(getActivity(), 5);
//		((MarginLayoutParams)mDetailBox.getLayoutParams()).topMargin = marginTop;
//		((MarginLayoutParams)mDetailBox.getLayoutParams()).leftMargin = marginLeft;
//		mDetailBox.getLayoutParams().height = height;
//		mDetailBox.getLayoutParams().width = width;
//		mDetailBox.setVisibility(View.GONE);
	}

	private Rect offsetDetailView(Rect rect) {
		Rect window = WindowUtils.getRootViewVisibleDisplayFrame(getActivity());
		int height = window.height();
		int top = rect.top +  (int) (height * getResources().getInteger(R.integer.detail_view_rateY) / 100);
		int bottom = top + (int) (height * getResources().getInteger(R.integer.detail_box_rateHeight) / 100);
		
		rect.set(rect.left + WindowUtils.dip2px(getActivity(), 5),
				top, 
				rect.right - WindowUtils.dip2px(getActivity(), 5), 
				bottom);
		return rect;
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
		if(inDetail) {
			applyBackAnimation();
			inDetail = false;
		}
	}

	@Override
	public boolean onBack() {
		return false;
	}

	@Override
	public void doSearch(int type, SearchParams params) {
		InterestController.getInstance(getActivity()).loadInterests(type,
				mCallBack, params);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initDetailBox();
	}
	
}
