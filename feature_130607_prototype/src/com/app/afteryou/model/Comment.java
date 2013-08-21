package com.app.afteryou.model;

public class Comment {

	private Object dataSrc = null;
	private float rate = 0f;
	private String time = "";
	private String placeId = "";
	private String userName = "";
	private String comment = "";
	
	public Object getDataSrc() {
		return dataSrc;
	}
	public void setDataSrc(Object dataSrc) {
		this.dataSrc = dataSrc;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
