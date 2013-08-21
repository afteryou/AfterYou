package com.navbuilder.app.hurricane.home;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.constants.AppConstants;
import com.navbuilder.app.hurricane.map.MapManager;
import com.navbuilder.app.hurricane.utils.DebugUtils;
import com.navbuilder.app.hurricane.utils.WindowUtils;
import com.navbuilder.nbui.Map3DView;
import com.navbuilder.nbui.MapViewContext;
import com.navbuilder.sc.ControllerManager;
import com.navbuilder.sc.map.MapController;
import com.nbi.common.data.MapLocation;
import com.tcs.jcc.testharness.BubbleView;

public class MapFragment extends Fragment{
	private LinearLayout mapviewParent;
	private Map3DView mMap3DView;
	private LinearLayout mapviewBackground;
	private boolean activated;
	private boolean isFullScreen;
	private MapManager mapManager;
	private Handler handler;
	public boolean mapInited = false;
	private LinearLayout detailFooter;
	private FrameLayout contentDetailView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mapManager = MapManager.getInstance();
		handler = new Handler(getActivity().getMainLooper());
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map_fullscreen, null);
		
		mapviewBackground = (LinearLayout) view.findViewById(R.id.map_kit_bg);
		detailFooter =   (LinearLayout)getActivity().findViewById(R.id.detail_footer);
		mapviewParent = (LinearLayout) view.findViewById(R.id.map_parent);
		contentDetailView = (FrameLayout) getActivity().findViewById(R.id.content_detail);
		mapviewBackground.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(! mapInited){
					return true;
				}
				final int action = event.getAction();

				if (action == MotionEvent.ACTION_DOWN){
					 
					if(!isFullScreen){
						detailFooter.setVisibility(View.VISIBLE);
						int footerHeight =  (int) getActivity().getResources().getDimension(R.dimen.detail_map_footer_hight);
						expandMapView(footerHeight);
						return true;
					}
					
				}
				return false;
			}
		});
		detailFooter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isFullScreen){
					MapManager.getInstance().setMapAnimationType(AppConstants.Map.ANIMATION_TYPE_UP);
					DebugUtils.recordAnimationFPS(getActivity(), "Details Slide In", 500);
					contentDetailView.animate().translationY(0);
					setFullScreen(false);
					detailFooter.setVisibility(View.GONE);
				}
				
			}
		});
		
		showMapView();
	return view;	
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		clearMapView();
		showMapView();
	}
	
	
	
	@Override
	public void onStop() {
		super.onStop();
		clearMapView();
		activated = false;
	}
	@Override
	public void onResume() {
		super.onResume();
		activated = true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		activated = false;
	}
	
	public boolean onBack() {
		detailFooter.setVisibility(View.GONE);
		hideMapView();
		activated = false;
		return true;
	}

	private void hideMapView() {
		isFullScreen = false;
		mapManager.hidePins();
	}

	private void showMapView() {
		MapController mMapController = ControllerManager.getInstance()
				.getMapController();
		mMapController.getMapViewContext().showMap3DView(
				new MapViewContext(getActivity()) {

					@Override
					public void setView(final Map3DView view) {
						BubbleView.getBubble(this.getAppContext()).onStart();
						mMap3DView = view;
						mapviewParent.addView(mMap3DView);
						mMap3DView.setKeepScreenOn(true);

						showMapCenter(true);
					}

				});

	}
	
	public void clearMapView(){
		if(mapviewParent != null){
			mapviewParent.removeAllViews();
		}
		mapManager.hidePins();
		ControllerManager.getInstance().getMapController().removeMapView();
		
	}

	public boolean isActivated() {
		return activated;
	}

	public boolean isFullScreen() {
		return isFullScreen;
	}

	public void setFullScreen(boolean isFullScreen) {
		this.isFullScreen = isFullScreen;
	}
	
	public void showMapCenter(boolean delay){
		activated = true;
		int delayTime;
		if(delay){
			delayTime = AppConstants.Map.DETAIL_MAP_DEFAULT_DELAY;
		}else{
			delayTime = 0;
		}
		mapManager.showPin(getActivity());
		handler.postDelayed(new Runnable() {
            @Override public void run() {
            	if(!activated){
            		return;
            	}
            	MapLocation panedMapLocation = new MapLocation();
            	
            	panedMapLocation.setLongitude(mapManager.getPinLocation().getLongitude());
            	
				if(isFullScreen){
					panedMapLocation.setLatitude(mapManager.getPinLocation().getLatitude());
				}else{
					panedMapLocation.setLatitude(mapManager.getPinLocation().getLatitude() - AppConstants.Map.ANIMATION_DISTANCE);
					mMap3DView.onTilt(AppConstants.Map.DETAIL_MAP_DEFAULT_TILT);//Tilt to don't show the sky
				}
				mapManager.updateMapViewPosition(panedMapLocation);
				mapInited = true;
            }
        },delayTime);
	}
	
	public void panMap(float latitude, float longitude){
		mapManager.panMap(latitude, longitude);
	}
	
	public void expandMapView(int footerHeight) {
		int offsetY = getWindowHeight(null) - WindowUtils.getActionBarHeight(getActivity())  -WindowUtils.getStatusBarHeight(getActivity()) - (getVisableWindowHeight(null) * getResources().getInteger(R.integer.detail_view_rateY) / 100 + footerHeight);
		MapManager.getInstance().setMapAnimationType(AppConstants.Map.ANIMATION_TYPE_DOWN);
		DebugUtils.recordAnimationFPS(getActivity(), "Details Slide Out", 500);
		contentDetailView.animate().translationY(offsetY);
		
		
		contentDetailView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				if(mapManager.getMapAnimationType() == AppConstants.Map.ANIMATION_TYPE_STOP ){
					return true;
				}
				if(mapManager.getMapAnimationTotalDistance() < AppConstants.Map.ANIMATION_DISTANCE){
					if(mapManager.getMapAnimationType() == AppConstants.Map.ANIMATION_TYPE_DOWN){
						panMap(AppConstants.Map.ANIMATION_STEP_LENGTH,0);
					}else{
						panMap(AppConstants.Map.ANIMATION_STEP_LENGTH*(-1),0);
					}
					
					mapManager.setMapAnimationTotalDistance(mapManager.getMapAnimationTotalDistance() + AppConstants.Map.ANIMATION_STEP_LENGTH);
					//this.onPreDraw();
				}else{
					mapManager.setMapAnimationTotalDistance(0);
					mapManager.setMapAnimationType(AppConstants.Map.ANIMATION_TYPE_STOP);
				}
				
				return true;
			}
		});
		

		contentDetailView.animate().setListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				mapManager.setMapAnimationTotalDistance(0);
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
		
					MapLocation pinLocation = mapManager.getPinLocation();
					
					if(isFullScreen){
						mapManager.updateMapViewPosition(pinLocation);
					}else{
						MapLocation panedMapLocation = new MapLocation();
		            	panedMapLocation.setLatitude(pinLocation.getLatitude() - AppConstants.Map.ANIMATION_DISTANCE);
		            	panedMapLocation.setLongitude(pinLocation.getLongitude());
						mapManager.updateMapViewPosition(panedMapLocation);
					}
				
				mapManager.setMapAnimationTotalDistance(0);
				mapManager.setMapAnimationType(AppConstants.Map.ANIMATION_TYPE_STOP);
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				mapManager.setMapAnimationTotalDistance(0);
				mapManager.setMapAnimationType(AppConstants.Map.ANIMATION_TYPE_STOP);
				
			}
		});
		
			setFullScreen(true);		
	}
	
	private int getWindowHeight(Configuration config){
		int height = WindowUtils.getScreenHeight(getActivity());
		int width = WindowUtils.getScreenWidth(getActivity());
		if(config == null) {
			return height;
		}
		if(config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			return height < width ? width : height;
		} else {
			return height < width ? height: width;
		}
	}
	
	private int getVisableWindowHeight(Configuration config) {
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
