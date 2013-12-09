package com.beacon.afterui.notification.message.data;

public class ProfileInfo {
	private String name;
	private String profilePicURL;
	private long profileID;
	private String albumURL;
	private int countLikes;
	private int countComments;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfilePicURL() {
		return profilePicURL;
	}

	public void setProfilePicURL(String profilePicURL) {
		this.profilePicURL = profilePicURL;
	}

	public long getProfileID() {
		return profileID;
	}

	public void setProfileID(long profileID) {
		this.profileID = profileID;
	}

	public String getAlbumURL() {
		return albumURL;
	}

	public void setAlbumURL(String albumURL) {
		this.albumURL = albumURL;
	}

	public int getCountLikes() {
		return countLikes;
	}

	public void setCountLikes(int countLikes) {
		this.countLikes = countLikes;
	}

	public int getCountComments() {
		return countComments;
	}

	public void setCountComments(int countComments) {
		this.countComments = countComments;
	}

}