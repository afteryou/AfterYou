package com.navbuilder.app.hurricane.fragment;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.activity.MainActivity;
import com.navbuilder.app.hurricane.constants.CommonConstants;
import com.navbuilder.app.hurricane.controller.CacheManager;
import com.navbuilder.app.hurricane.controller.CategoryController;
import com.navbuilder.app.hurricane.controller.InterestController;
import com.navbuilder.app.hurricane.controller.InterestController.InterestCallBack;
import com.navbuilder.app.hurricane.map.MapFragment;
import com.navbuilder.app.hurricane.map.MapManager;
import com.navbuilder.app.hurricane.model.Category;
import com.navbuilder.app.hurricane.model.Interest;
import com.navbuilder.app.hurricane.ui.interest.InterestAdapter;
import com.navbuilder.app.hurricane.ui.staggered.CustomGridView;
import com.navbuilder.app.hurricane.ui.staggered.CustomGridView.IDataListener;
import com.navbuilder.app.hurricane.utils.ImageUtils;
import com.navbuilder.app.hurricane.utils.WindowUtils;
//import com.navbuilder.nbui.Map3DView;
//import com.navbuilder.nbui.MapViewContext;
//import com.navbuilder.sc.ControllerManager;
//import com.navbuilder.sc.map.MapController;
import com.nbi.common.data.MapLocation;
import com.nbi.common.data.Place;

public class DetailFragment extends Fragment implements FragmentLifecycle, ISearchFunction {
	private CustomGridView mMoreView = null;
	private ViewGroup mDetailMore = null;
	private ViewGroup mDetailView = null;
//	private Map3DView mMap3DView;
	private LinearLayout mapviewParent;
	private LinearLayout mapviewBackground;
//	private LinearLayout mapDetailView ;
	private View pinView = null;
	private InterestAdapter mMoreAdapter = null;
	private InterestController mController;
	private int mMoreKey;
	
	private int mCacheKey;
	
	private int mSelectedPostion = -1;
	
	private final static int CAPACITY = 50;
	private boolean isBacking = false;
	
	private InterestCallBack mCallBack;
	private boolean isMapClicked = false;
	private MapManager mapManager;
	
	public DetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mController = InterestController.getInstance(getActivity());
		mapManager = MapManager.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.detail_fragment, null);
		mapviewParent = (LinearLayout) view.findViewById(R.id.map_kit_view);
//		mapDetailView = (LinearLayout) view.findViewById(R.id.map_detail_view);
		mapviewBackground = (LinearLayout) view.findViewById(R.id.map_kit_bg);
		
		LinearLayout mapviewBackgroundNoEvent =  (LinearLayout) view.findViewById(R.id.map_kit_bg_no_event);
		mapviewBackgroundNoEvent.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
				
			}
		});
		
		
		mapviewBackground.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();

				if (action == MotionEvent.ACTION_UP){
					
					clearMapView();
//					ControllerManager.getInstance().getMapController().removeMapView();
					
					Bundle bundle = new Bundle();
	        	    bundle.putInt(CommonConstants.BundleKey.CACHE_KEY, mCacheKey);
	        	    bundle.putInt(CommonConstants.BundleKey.SELECTED_POSITION, mSelectedPostion);
					Fragment mapFragment = new MapFragment();
					FragmentHelper.gotoFragment(getActivity(), DetailFragment.this, mapFragment);
//					FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, WindowUtils.getScreenHeight(getActivity()));
//					mapviewParent.setLayoutParams(params);
//					showMapView();
//					mapDetailView.setVisibility(View.GONE);
					isMapClicked = true;
				}

				return true;
			}
		});
		showMapView();
		mapManager.hidePins();
		pinView = view.findViewById(R.id.detail_map_pin);
		mDetailView = (ViewGroup)view.findViewById(R.id.detail_content);
		mDetailMore = (ViewGroup)view.findViewById(R.id.detail_more);
		
		mMoreView = (CustomGridView) view.findViewById(R.id.interest_more);
		mMoreView.setPullLoadEnable(true);
		mMoreView.setCapacity(CAPACITY);
		mMoreView.setDataListener(new IDataListener() {
			@Override
			public void onLoadMore() {
				//mController.loadInterests(InterestController.REQUEST_TYPE_APPEND, mMoreKey);
			}
			
		});

		mMoreView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//applyAnimation();
			}
			
		});
		mCallBack = new InterestCallBack() {

			@Override
			public void onResult(int type, Object result) {
				mMoreKey = (Integer)result;
				if(mMoreKey == CacheManager.NO_RESULT_KEY) {
					return;
				}
				SparseArray datas = InterestController.getInstance(getActivity()).getCacheManager().readCache(mMoreKey).getLastAppended();
				List<Interest> data = new ArrayList<Interest>();
				for(int i=0; i < datas.size(); i++) {
					data.add((Interest) datas.get(i));
				}

				switch (type) {
				case InterestController.REQUEST_TYPE_APPEND:
					mMoreAdapter.addItemLast(data);
					//mAdapter.notifyDataSetChanged();
					setGridViewHeightBasedOnChildren(mMoreView);
					break;
				case InterestController.REQUEST_TYPE_RELATED:
					mMoreAdapter.addItemTop(data);
					mMoreAdapter.notifyDataSetChanged();
					setGridViewHeightBasedOnChildren(mMoreView);
					break;
					default:
						throw new IllegalArgumentException("Unsupported type!");
				}
			}
		};
		
		if(getArguments() != null) {
			mCacheKey = getArguments().getInt(CommonConstants.BundleKey.CACHE_KEY);
			mSelectedPostion = getArguments().getInt(CommonConstants.BundleKey.SELECTED_POSITION);
			
			mapManager.updateCacheKey(mCacheKey);
			mapManager.updateSelectedPostion(mSelectedPostion);
		}
		mMoreAdapter = new InterestAdapter(getActivity());
		mMoreView.setAdapter(mMoreAdapter);
		return view;
	}

	private void loadMoreRelated() {
		List<Category> cates = CategoryController.getInstance().getUserAttachedCategoriesFromPref();
		if(cates.isEmpty()) {
			cates = CategoryController.getInstance().getUserCategoriesFromPref();
		}
		List<String> brands = new ArrayList<String>();
		List<String> interests = new ArrayList<String>();
		for (Category c : cates) {
			if (c.getType() == Category.TYPE_BRAND) {
				brands.add(c.getName());
			} else {
				interests.add(c.getCode());
			}
		}
		mController.loadInterests(InterestController.REQUEST_TYPE_RELATED, mCallBack, interests.toArray(), brands.toArray());
	}
	
	private static void setGridViewHeightBasedOnChildren(final CustomGridView listView) {  
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				ListAdapter listAdapter = listView.getAdapter();   
		        if (listAdapter == null) {  
		            return;  
		        }  
		        int height = listView.getRealHeight();
		        ViewGroup.LayoutParams params = listView.getLayoutParams();  
		        params.height = height; 
		        listView.setLayoutParams(params);  
			}}, 500);
    }  
	
	private void applyBackAnimation() {
        AnimatorSet fideOutMap = new AnimatorSet();
        fideOutMap.play(ObjectAnimator
				.ofFloat(mapviewBackground, 
						View.ALPHA, 1f, 0f))
				.with(ObjectAnimator.ofFloat(pinView, View.ALPHA, 1f, 0f))
				.with(ObjectAnimator.ofFloat(mDetailMore, View.ALPHA, 1f, 0f));
        fideOutMap.setDuration(250);
        fideOutMap.addListener(new AnimatorListenerAdapter() {
        	@Override
            public void onAnimationEnd(Animator anim) {
        		mapviewBackground.setVisibility(View.GONE);
        		pinView.setVisibility(View.GONE);
        		mDetailMore.setVisibility(View.GONE);
        		pinView.setAlpha(1f);
        		mDetailMore.setAlpha(1f);
        		FragmentHelper.popFragment(getActivity());
        		isBacking = false;
        	}
        });

        fideOutMap.start(); 
	}

	private void applyAnimation() {
		boolean isLandscape = WindowUtils.isLandscape(getActivity());

        AnimatorSet fideInMap = new AnimatorSet();
        fideInMap.play(ObjectAnimator
				.ofFloat(mapviewBackground, 
						View.ALPHA, 0f , 1f))
				.with(ObjectAnimator.ofFloat(mDetailMore, View.ALPHA, 0f, 1f));
        fideInMap.setDuration(500);
        fideInMap.addListener(new AnimatorListenerAdapter() {
        	@Override
            public void onAnimationStart(Animator anim) {
        		mapviewBackground.setVisibility(View.VISIBLE);
        	}
        });

        AnimatorSet dropPin = new AnimatorSet();
        long duration = 400L;
        pinView.setX((getWindowWidth(getResources().getConfiguration()) - 60) /2);
        float endY = getWindowHeight(getResources().getConfiguration()) * getResources().getInteger(R.integer.detail_pin_rateY) / 100;
        float deltaY = 25; //should set this value based on the size of pin image
        ObjectAnimator transY = ObjectAnimator.ofFloat(pinView, View.Y, -100, endY);
        transY.setInterpolator(new AccelerateInterpolator());
        transY.setDuration(duration/2);

        ValueAnimator squashAnim2 = ObjectAnimator.ofFloat(pinView, View.SCALE_X, 1f, 1.4f);
        squashAnim2.setInterpolator(new DecelerateInterpolator());
        ValueAnimator squashAnim2Back = ObjectAnimator.ofFloat(pinView, View.SCALE_X, 1.4f, 1f);
        squashAnim2Back.setInterpolator(new BounceInterpolator());
        AnimatorSet squash2 = new AnimatorSet();
        squash2.play(squashAnim2).before(squashAnim2Back);
        squash2.setDuration(duration/2);

        ValueAnimator stretchAnim1 = ObjectAnimator.ofFloat(pinView, View.Y, endY,
                endY + deltaY);
        stretchAnim1.setInterpolator(new DecelerateInterpolator());
        ValueAnimator stretchAnim1Back = ObjectAnimator.ofFloat(pinView, View.Y, endY + deltaY,
        		endY);
        stretchAnim1Back.setInterpolator(new BounceInterpolator());
        AnimatorSet stretch1 = new AnimatorSet();
        stretch1.play(stretchAnim1).before(stretchAnim1Back);
        stretch1.setDuration(duration/2);

        ValueAnimator stretchAnim2 = ObjectAnimator.ofFloat(pinView, View.SCALE_Y,
        		1f, 0.5f);
        stretchAnim2.setInterpolator(new DecelerateInterpolator());
        ValueAnimator stretchAnim2Back = ObjectAnimator.ofFloat(pinView, View.SCALE_Y,
        		0.5f, 1f);
        stretchAnim2Back.setInterpolator(new BounceInterpolator());
        AnimatorSet stretch2 = new AnimatorSet();
        stretch2.play(stretchAnim2).before(stretchAnim2Back);
        stretch2.setDuration(duration/2);
        
        AnimatorSet bounce = new AnimatorSet();
        bounce.play(stretch1).with(stretch2).with(squash2);
        dropPin.play(transY).before(bounce);
        dropPin.addListener(new AnimatorListenerAdapter() {
        	@Override
            public void onAnimationStart(Animator anim) {
        		pinView.setVisibility(View.VISIBLE);
        		pinView.setAlpha(1f);
        	}
        });
        
        AnimatorSet showDetail = new AnimatorSet();
        showDetail.play(fideInMap).before(dropPin);
        showDetail.start();
	}

	private void updateDetailView() {
		if(mCacheKey == CacheManager.NO_RESULT_KEY) {
			return;
		}
		SparseArray datas = InterestController.getInstance(getActivity()).getCacheManager().readCache(mCacheKey).getResultData();
		Interest data = (Interest) datas.get(mSelectedPostion);

		ImageView iv = (ImageView)mDetailView.findViewById(R.id.detail_graph);
		if(data.getDataSrc() instanceof String) {
			ImageUtils.getInstance(getActivity()).loadImage(data.getDataSrc(), iv);
		} else if(data.getDataSrc() instanceof Integer) {
			ImageUtils.getInstance(getActivity()).resizeImage(data.getDataSrc(), iv);
		}
		if(true/*check if need display rating*/
				&& mDetailView.findViewWithTag(1) == null) {
			ViewGroup container = (ViewGroup) mDetailView.findViewById(R.id.detail_middle);
			View reviews = this.getActivity().getLayoutInflater().inflate(R.layout.reviews_comp, null);
			reviews.setTag(1);
			container.addView(reviews);
			RatingBar rb = (RatingBar) reviews
				.findViewById(R.id.reviews_rating);
			rb.setRating(4.5f);
		}
		
		TextView tv = (TextView)mDetailView.findViewById(R.id.detail_address);
		tv.setText(data.getAddress());
		TextView tv3 = (TextView)mDetailView.findViewById(R.id.detail_title);
		tv3.setText(data.getPlaceName());
		TextView tv4 = (TextView)mDetailView.findViewById(R.id.detail_distance);
		tv4.setText(data.getDistance());
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
		mapManager.updateMapViewPosition(getActivity());
		applyAnimation();
		loadMoreRelated();
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
		if(isMapClicked){
			showMapView();
			mapManager.updateMapViewPosition(getActivity());
			isMapClicked = false;
			mapManager.hidePins();
		}
	}

	@Override
	public boolean onBack() {
		if(!isBacking) {
			isBacking = true;
			((MainActivity)getActivity()).updateToMainScreenActionBar();
			applyBackAnimation();
			clearMapView();
		}
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		pinView.setX((getWindowWidth(newConfig) - 60) /2);
		pinView.setY(getWindowHeight(newConfig) * getResources().getInteger(R.integer.detail_pin_rateY) / 100);
		fixDetailViewY(newConfig);
		setGridViewHeightBasedOnChildren(mMoreView);
		if(!isMapClicked){
			clearMapView();
			showMapView();
		}
		
	}
	
	private void fixDetailViewY(Configuration config) {
		mDetailView.setVisibility(View.VISIBLE);
		MarginLayoutParams lp = (MarginLayoutParams) mDetailView.getLayoutParams();
		lp.topMargin = (int) (getWindowHeight(config) * getResources().getInteger(R.integer.detail_view_rateY) / 100);
		lp.height = (int)(getWindowHeight(config) * getResources().getInteger(R.integer.detail_box_rateHeight) / 100);
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

	@Override
	public void doSearch(int type, SearchParams params) {
		InterestController.getInstance(getActivity()).loadInterests(type,
				mCallBack, params);
	}
	
	private void clearMapView(){
		if(mapviewParent != null){
			mapviewParent.removeAllViews();
		}
//		ControllerManager.getInstance().getMapController().removeMapView();
	}
	
	private void showMapView() {
		
//		MapController mMapController = ControllerManager.getInstance()
//				.getMapController();
//		mMapController.getMapViewContext().showMap3DView(
//				new MapViewContext(getActivity()) {
//
//					@Override
//					public void setView(final Map3DView view) {
//						// BubbleView.getBubble(this.getAppContext()).onStart();
//						mMap3DView = view;
////						mapviewParent.removeAllViews();
//						mapviewParent.addView(mMap3DView);
//						mMap3DView.setKeepScreenOn(true);
//					}
//
//				});

	}
	
}
