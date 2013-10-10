package com.beacon.afterui.views.data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beacon.afterui.log.AfterUIlog;
import com.beacon.afterui.utils.FileUtils;

import android.content.Context;


public class SearchInformation
{
	private Context mContext;
	
	private List<SearchResults> interests;
	
	public static final String SEARCH_NAME = "name";
	public static final String SEARCH_AGE = "age";
	public static final String SEARCH_STATUS = "status";
	public static final String SEARCH_LAST_ONLINE = "last-online-date";
	public static final String SEARCH_LAST_TIME = "last-online-time";
	public static final String SEARCH_PROFILE_PHOTO = "profile-photo";
	public static final String SEARCH_PROFILE_LIKES = "profile-number-likes";
	public static final String SEARCH_PROFILE_COMMENTS_COUNT = "profile-number-comments";
	public static final String SEARCH_PHOTO_COUNT = "photo-count";
	
	public static final String SEARCH_TYPE_INTEREST = "interests";
	
	public SearchInformation(Context mContext){
		this.mContext = mContext;
		interests = new ArrayList<SearchInformation.SearchResults>();
	}
	
	public void startSearch(ISearchListener callback)
	{
		interests = getSearchResultsFromAssets();
		callback.onSingleSearch(this);
	}
	
	public SearchResults getSearchResult(int index)
	{
		if(interests == null || index >interests.size()-1)
		{
			return null;
		}
		return interests.get(index);
		
	}
	
	public int getResultCount()
	{
		return interests.size();
	}
	
	private List<SearchResults> getSearchResultsFromAssets()
	{
		JSONObject search_result;
		try {
			search_result = new JSONObject(getJsonDataFromAsset("interest_search.json"));
			JSONArray search_array = search_result.getJSONArray(SEARCH_TYPE_INTEREST);
			List<SearchResults> results = new ArrayList<SearchInformation.SearchResults>();
			for(int i = 0; i < search_array.length(); i++)
			{
				JSONObject object = search_array.getJSONObject(i);
				SearchResults searchResult = new SearchResults();
				searchResult.setName(object.getString(SEARCH_NAME));
				searchResult.setAge(object.getString(SEARCH_AGE));
				searchResult.setStatus(object.getString(SEARCH_STATUS));
				searchResult.setLast_online(object.getString(SEARCH_LAST_ONLINE));
				searchResult.setLast_online_time(object.getString(SEARCH_LAST_TIME));
				searchResult.setImageUrl(object.getString(SEARCH_PROFILE_PHOTO));
				searchResult.setThumbnailImageUrl(object.getString(SEARCH_PROFILE_PHOTO));
				searchResult.setProfile_likes(object.getString(SEARCH_PROFILE_LIKES));
				searchResult.setProfile_comments_count(object.getString(SEARCH_PROFILE_COMMENTS_COUNT));
				searchResult.setAlbum_photo_count(object.getString(SEARCH_PHOTO_COUNT));
				results.add(searchResult);
			}
			return results;
		} catch (JSONException e) {
			AfterUIlog.printStackTrace(e);
			return null;
		}
		
	}
	
	private String getJsonDataFromAsset(String assetName) {
		InputStream is = null;
		InputStreamReader reader = null;
		try {
			is = mContext.getAssets().open(assetName);
			reader = new InputStreamReader(is);
			char[] data = new char[is.available()];
			reader.read(data);
			return String.valueOf(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			FileUtils.close(is);
			FileUtils.close(reader);
		}
	}
	
	public class SearchResults
	{
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
	
	public interface ISearchListener{
		public abstract void onSingleSearch(SearchInformation info);
	}
	
	
}
