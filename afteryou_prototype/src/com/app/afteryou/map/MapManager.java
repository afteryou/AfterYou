package com.app.afteryou.map;

import android.content.Context;
import android.util.SparseArray;

import com.app.afteryou.controller.CacheManager;
import com.app.afteryou.controller.InterestController;
import com.app.afteryou.model.Interest;
import com.navbuilder.nb.data.Location;
//import com.navbuilder.sc.ControllerManager;
//import com.navbuilder.sc.map.MapController;
import com.nbi.common.data.MapLocation;
import com.nbi.common.data.Place;


public class MapManager {
	private static MapManager mInstance;
	private int mCacheKey;
	private int mSelectedPostion = -1;
	
	public static MapManager getInstance() {
		if (mInstance == null) {
			mInstance = new MapManager();
		}
		return mInstance;
	}
	
	public void updateCacheKey(int cacheKey){
		mCacheKey = cacheKey;
	}
	
	public void updateSelectedPostion(int selectedPostion){
		mSelectedPostion = selectedPostion;
	}
	
	public void updateMapViewPosition(Context ctx){

		MapLocation location = getCurrentLocation(ctx);
		if(location == null){
			return;
		}
//		MapController mMapController = ControllerManager.getInstance()
//				.getMapController();
//		mMapController.setPosition((float) location.getLatitude(),(float)location.getLongitude());
	}

	public void showPin(Context ctx) {
		MapLocation location = getCurrentLocation(ctx);
		if(location == null){
			return;
		}
		Location l1 = new Location();
		l1.setLatLon(location.getLatitude(),location.getLongitude());
		
		com.navbuilder.nb.data.Place p1 = new com.navbuilder.nb.data.Place("test1",l1);
//		MapController mMapController = ControllerManager.getInstance()
//				.getMapController();
//        mMapController.showPin(p1);
//		mMapController.showPins(true);
		
	}
	
	public void hidePins() {
//		MapController mMapController = ControllerManager.getInstance()
//				.getMapController();
//        mMapController.showPins(false);
		
	}
	
	private MapLocation getCurrentLocation(Context ctx){
		if(mCacheKey == CacheManager.NO_RESULT_KEY) {
			return null;
		}
		SparseArray datas = InterestController.getInstance(ctx).getCacheManager().readCache(mCacheKey).getResultData();
		Interest data = (Interest) datas.get(mSelectedPostion);
		Place place = data.getPlace();
		MapLocation location = place.getLocation();
		return location;
	}
	
	

}
