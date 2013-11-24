package com.beacon.afterui.views.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

import com.beacon.afterui.R;
import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.utils.ImageUtils;
import com.beacon.afterui.views.data.SearchInformation.ISearchListener;
import com.beacon.afterui.views.data.SearchInformation.SearchResults;
import com.beacon.afterui.views.data.SingleSearchController.SingleSearchResult.ResultType;

public class SingleSearchController {

	private static final String TAG = "SingleSearchController";
	private static Context mContext = AfterYouApplication.getInstance()
			.getApplicationContext();

	public static void search(final String query,
			final SingleSearchCallBack callback) {
		search(query, null, null, callback);
	}

	public static void search(final String query, String[] categories,
			String[] brands, final SingleSearchCallBack callback) {
		SearchInformation searchInfo = new SearchInformation(mContext);
		searchInfo.startSearch(new MySingleSearchListener(callback));
	}

	public static void searchRelated(final String query,
			final SingleSearchCallBack callback) {
	}

	public static void searchRelated(final String query, String[] categories,
			String[] brands, final SingleSearchCallBack callback) {
	}

	private static class MySingleSearchListener implements ISearchListener {
		private SingleSearchCallBack callBack;

		MySingleSearchListener(SingleSearchCallBack callBack) {
			this.callBack = callBack;
		}

		public void onSingleSearch(SearchInformation info) {
			SingleSearchResult result = new SingleSearchResult();
			result.setData(ResultType.SINGLE_SEARCH_INFO, info);
			List<SearchResults> searchList = new ArrayList<SearchResults>();
			List<Interest> interestList = new ArrayList<Interest>();
			for (int i = 0; i < info.getResultCount(); i++) {
				SearchResults searchResult = info.getSearchResult(i);
				searchList.add(searchResult);
				Interest interest = new Interest();
				interest.setName(searchResult.getName());
				interest.setAge(searchResult.getAge());
				interest.setStatus(searchResult.getStatus());
				interest.setProfile_comments_count(searchResult
						.getProfile_comments_count());
				interest.setProfile_likes(searchResult.getProfile_likes());
				interest.setAlbum_photo_count(searchResult
						.getAlbum_photo_count());
				interest.setLast_online(searchResult.getLast_online());
				interest.setLast_online_time(searchResult.getLast_online_time());
				interest.setThumbnailImageUrl(searchResult
						.getThumbnailImageUrl());
				interest.setImageUrl(searchResult.getImageUrl());
				if (searchResult.getThumbnailImageUrl() != null) {
					interest.setDataSrc(searchResult.getThumbnailImageUrl());
				} else {
					interest.setDataSrc(R.drawable.empty_photo);
				}
				interestList.add(interest);
			}

			result.setData(ResultType.SEARCH_LIST, searchList);
			result.setData(ResultType.INTEREST_LIST, interestList);
			callBack.onSingleSearchResults(result);
			prefetchImages(interestList);
		}

	}

	private static void prefetchImages(List<Interest> interests) {
		if (interests != null && !interests.isEmpty()) {
			ImageWorkerTask task = new ImageWorkerTask();
			task.execute(interests);
		}
	}

	private static class ImageWorkerTask extends
			AsyncTask<List<Interest>, Void, Void> {

		@Override
		protected Void doInBackground(List<Interest>... params) {
			List<Interest> interests = params[0];
			for (Interest interest : interests) {
				Object src = interest.getDataSrc();
				if (src != null) {
					ImageUtils.getInstance(mContext).loadImage(src);
				}
			}
			return null;
		}
	}

	static List<String> dataUrl = new ArrayList<String>();

	static {
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/4513269/1/stock-photo-4513269-tasty-italian-pizza.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/5769129/1/stock-photo-5769129-pizza.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/15711280/1/stock-photo-15711280-pepperoni-pizza.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/1241597/1/stock-photo-1241597-deluxe-pizza-03.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/17378726/1/stock-photo-17378726-brioches-e-cappuccino.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/17583538/1/stock-photo-17583538-hot-coffee.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/18400296/1/stock-photo-18400296-coffee-in-opened-disposable-cup.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/19002287/1/stock-photo-19002287-coffee-cup-beans-cinnamon-anise-and-sweets.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/7194218/1/stock-photo-7194218-hotel.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/18042428/1/stock-photo-18042428-hotel-reception-bell.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/16400772/1/stock-photo-16400772-las-vegas-hotel-room.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/18355431/1/stock-photo-18355431-caribbean-beach-vacation-resort-hotel-room.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/15017286/1/stock-photo-15017286-restaurant-dinner-place-setting.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/18206915/1/stock-photo-18206915-dinner-plate-setting.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/12587743/1/stock-photo-12587743-orange-drink-in-a-bar.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/14967178/1/stock-photo-14967178-glass-of-dark-beer.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/16515039/1/stock-photo-16515039-tasty-hamburger-and-french-fries.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/12723147/1/stock-photo-12723147-classic-burger-with-french-fries.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/6100380/1/stock-photo-6100380-extra-large-submarine-sandwich-with-ham-and-cheese.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/11359586/1/stock-photo-11359586-hamburger.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/6700261/1/stock-photo-6700261-fuel-pumps.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/93116/1/stock-photo-93116-empty-tank.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/17020332/1/stock-photo-17020332-airplane-take-off.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/5487656/1/stock-photo-5487656-entering-the-car-wash.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/5478693/1/stock-photo-5478693-vehicle-in-the-car-wash.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/21996017/1/stock-photo-21996017-rx-medicine-prescription-bottles.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/17010061/1/stock-photo-17010061-united-states-post-office.jpg");
		dataUrl.add("http://i.istockimg.com/file_thumbview_approve/11684144/1/stock-photo-11684144-elegant-shopping-mall.jpg");
	}

	public static class SingleSearchResult {
		public static enum ResultType {
			INTEREST_LIST, SEARCH_LIST, SINGLE_SEARCH_INFO, SINGLE_SEARCH_REQUEST, SUGGESTION_SEARCH_INFO, SUGGESTION_SEARCH_REQUEST, SUGGESTION_MATCH_LIST
		}

		private Map<ResultType, Object> mSearchResultMap = new HashMap<ResultType, Object>();

		public void setData(ResultType type, Object data) {
			mSearchResultMap.put(type, data);
		}

		@SuppressWarnings("unchecked")
		public <T> T getData(ResultType type) {
			return (T) mSearchResultMap.get(type);
		}
	}

	public static class SingleSearchCallBack {
		public void onSingleSearchResults(SingleSearchResult result) {
		}

	}
}
