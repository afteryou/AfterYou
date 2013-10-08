package com.app.afteryou.map;

import com.app.afteryou.constants.CommonConstants;
import com.app.afteryou.fragment.FragmentHelper;
import com.app.afteryou.fragment.FragmentLifecycle;
import com.app.afteryou.R;
//import com.navbuilder.nbui.Map3DView;
//import com.navbuilder.nbui.MapViewContext;
//import com.navbuilder.sc.ControllerManager;
//import com.navbuilder.sc.map.MapController;
//import com.tcs.jcc.testharness.BubbleView;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MapFragment extends Fragment implements FragmentLifecycle{
	private LinearLayout mapviewParent;
//	private Map3DView mMap3DView;
	private int mCacheKey;
	private int mSelectedPostion = -1;
	private MapManager mapManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mapManager = MapManager.getInstance();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map_fullscreen, null);
		mapviewParent = (LinearLayout) view.findViewById(R.id.map_parent);
		
		
		if(getArguments() != null) {
			mCacheKey = getArguments().getInt(CommonConstants.BundleKey.CACHE_KEY);
			mSelectedPostion = getArguments().getInt(CommonConstants.BundleKey.SELECTED_POSITION);
			mapManager.updateCacheKey(mCacheKey);
			mapManager.updateSelectedPostion(mSelectedPostion);
		}
		
	return view;	
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		clearMapView();
		showMapView();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		clearMapView();
		showMapView();
		mapManager.updateMapViewPosition(getActivity());
		mapManager.showPin(getActivity());
	}
	
	@Override
	public void onFragmentPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFragmentResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onBack() {
		System.out.println("[MapFragment][onBack]");
		clearMapView();
		FragmentHelper.popFragment(getActivity());
		return true;
	}

	private void showMapView() {
		
//		MapController mMapController = ControllerManager.getInstance()
//				.getMapController();
//		mMapController.getMapViewContext().showMap3DView(
//				new MapViewContext(getActivity()) {
//
//					@Override
//					public void setView(final Map3DView view) {
//						 BubbleView.getBubble(this.getAppContext()).onStart();
//						mMap3DView = view;
////						mapviewParent.removeAllViews();
//						mapviewParent.addView(mMap3DView);
//						mMap3DView.setKeepScreenOn(true);
//						
//					}
//
//				});

	}
	
	private void clearMapView(){
		if(mapviewParent != null){
			mapviewParent.removeAllViews();
		}
//		ControllerManager.getInstance().getMapController().removeMapView();
	}
	
}
