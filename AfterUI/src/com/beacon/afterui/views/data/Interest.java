package com.beacon.afterui.views.data;


public class Interest {

	private static final String EMPTY = "";
	private Object dataSrc = null;
	private String imageUrl = EMPTY;
	private String thumbImageUrl = EMPTY;
	private String name = EMPTY;
	private String age = EMPTY;
	private String status = EMPTY;
	private String last_online = EMPTY;
	private String last_online_time = EMPTY;
	private String profile_likes = EMPTY;
	private String profile_comments_count = EMPTY;
	private String album_photo_count = EMPTY;
	
	
	public Object getDataSrc() {
		return dataSrc;
	}
	
	public void setDataSrc(Object dataSrc) {
		this.dataSrc = dataSrc;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLast_online() {
		return last_online;
	}

	public void setLast_online(String last_online) {
		this.last_online = last_online;
	}

	public String getLast_online_time() {
		return last_online_time;
	}

	public void setLast_online_time(String last_online_time) {
		this.last_online_time = last_online_time;
	}

	public String getProfile_likes() {
		return profile_likes;
	}

	public void setProfile_likes(String profile_likes) {
		this.profile_likes = profile_likes;
	}

	public String getProfile_comments_count() {
		return profile_comments_count;
	}

	public void setProfile_comments_count(String profile_comments_count) {
		this.profile_comments_count = profile_comments_count;
	}

	public String getAlbum_photo_count() {
		return album_photo_count;
	}

	public void setAlbum_photo_count(String album_photo_count) {
		this.album_photo_count = album_photo_count;
	}

}
