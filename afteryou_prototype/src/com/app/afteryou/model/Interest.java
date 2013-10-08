package com.app.afteryou.model;

import com.nbi.common.data.Place;

public class Interest {

	private static final String EMPTY = "";
	private Object dataSrc = null;
	private String description = EMPTY;
	private String time = EMPTY;
	private String placeName = EMPTY;
	private String Distance = EMPTY;
	private String imageUrl = EMPTY;
	private String thumbImageUrl = EMPTY;
	private String address = EMPTY;
	private Place place = null;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}
	
	public Object getDataSrc() {
		return dataSrc;
	}
	
	public void setDataSrc(Object dataSrc) {
		this.dataSrc = dataSrc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getDistance() {
		return Distance;
	}

	public void setDistance(String distance) {
		Distance = distance;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getThumbnailImageUrl() {
		return thumbImageUrl;
	}

	public void setThumbnailImageUrl(String thumbImageUrl) {
		this.thumbImageUrl = thumbImageUrl;
	}

}
