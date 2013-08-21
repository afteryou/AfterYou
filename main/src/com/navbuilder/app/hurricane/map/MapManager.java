package com.navbuilder.app.hurricane.map;

import android.content.Context;

import com.navbuilder.app.hurricane.constants.AppConstants;
import com.navbuilder.nb.data.Location;
import com.navbuilder.sc.ControllerManager;
import com.navbuilder.sc.map.MapController;
import com.nbi.common.data.MapLocation;

public class MapManager {
	private static MapManager mInstance;
	private MapLocation centerLocation;
	private MapLocation pinLocation;
	private int  mapAnimationType = AppConstants.Map.ANIMATION_TYPE_STOP;
	private float  mapAnimationTotalDistance = 0;
	
	
	
	public MapLocation getPinLocation() {
		return pinLocation;
	}


	public void setPinLocation(MapLocation pinLocation) {
		this.pinLocation = pinLocation;
	}


	public int getMapAnimationType() {
		return mapAnimationType;
	}


	public void setMapAnimationType(int mapAnimationType) {
		this.mapAnimationType = mapAnimationType;
	}


	public float getMapAnimationTotalDistance() {
		return mapAnimationTotalDistance;
	}


	public void setMapAnimationTotalDistance(float mapAnimationTotalDistance) {
		this.mapAnimationTotalDistance = mapAnimationTotalDistance;
	}


	public static MapManager getInstance() {
		if (mInstance == null) {
			mInstance = new MapManager();
		}
		return mInstance;
	}
	
	public void updateMapViewPosition(MapLocation mapLocation){
		if(mapLocation == null){
			return;
		}
		centerLocation = new MapLocation();
		centerLocation.setLatitude(mapLocation.getLatitude());
		centerLocation.setLongitude(mapLocation.getLongitude());
		
		MapController mMapController = ControllerManager.getInstance().getMapController();
		mMapController.setPosition((float) centerLocation.getLatitude(),(float)centerLocation.getLongitude());
	}
	
	public void panMap(float latitude, float longitude) {
		centerLocation.setLatitude(centerLocation.getLatitude() + latitude);
		centerLocation.setLongitude(centerLocation.getLongitude() + longitude);
		updateMapViewPosition(centerLocation);
	}
	

	public void showPin(Context ctx) {
		Location l1 = new Location();
		l1.setLatLon(pinLocation.getLatitude(),pinLocation.getLongitude());
		
		com.navbuilder.nb.data.Place p1 = new com.navbuilder.nb.data.Place("",l1);
		MapController mMapController = ControllerManager.getInstance()
				.getMapController();
        mMapController.showPin(p1);
		
	}
	
	public void hidePins() {
		MapController mMapController = ControllerManager.getInstance()
				.getMapController();
        mMapController.showPins(false);
		
	}

}
