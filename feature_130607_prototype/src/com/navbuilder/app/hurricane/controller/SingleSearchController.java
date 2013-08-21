package com.navbuilder.app.hurricane.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.application.HurricaneApplication;
import com.navbuilder.app.hurricane.common.ContextManager;
import com.navbuilder.app.hurricane.common.Credentials;
import com.navbuilder.app.hurricane.controller.SingleSearchController.SingleSearchResult.ResultType;
import com.navbuilder.app.hurricane.log.Nimlog;
import com.navbuilder.app.hurricane.model.Interest;
import com.navbuilder.app.hurricane.preference.PreferenceEngine;
import com.navbuilder.app.hurricane.search.SingleSearchListenerImpl;
import com.navbuilder.app.hurricane.search.SuggestionSearchListenerImpl;
import com.navbuilder.app.hurricane.utils.AppUtils;
import com.navbuilder.app.hurricane.utils.ImageUtils;
import com.navbuilder.app.hurricane.utils.LocationUtils;
import com.navbuilder.nb.NBException;
import com.nbi.common.NBIContext;
import com.nbi.common.NBIException;
import com.nbi.common.NBIRequest;
import com.nbi.common.data.FuelPOI;
import com.nbi.common.data.POI;
import com.nbi.common.data.Place;
import com.nbi.common.data.SearchRegion;
import com.nbi.location.LocationListener;
import com.nbi.search.SearchRequest;
import com.nbi.search.singlesearch.SingleSearchInformation;
import com.nbi.search.singlesearch.SingleSearchRequest;
import com.nbi.search.singlesearch.SuggestMatch;
import com.nbi.search.singlesearch.SuggestionSearchInformation;
import com.nbi.search.singlesearch.SuggestionSearchRequest;

public class SingleSearchController {

	private static final String TAG = "SingleSearchController";
	private static Context mContext = HurricaneApplication.getInstance().getApplicationContext();
	private static Set<SingleSearchRequest> mSingleSearchRequestCache = new HashSet<SingleSearchRequest>();

	public static void search(final String query, final SingleSearchCallBack callback) {
		search(query, null, null, callback);
	}

	public static void search(final String query, String[] categories, String[] brands, final SingleSearchCallBack callback) {
		search(query, categories, brands, null, SearchRequest.POI_ENHANCED, callback, 50);
	}

	public static void searchRelated(final String query, final SingleSearchCallBack callback) {
		search(query, null, null, null, SearchRequest.POI_ENHANCED, callback, 10);
	}

	public static void searchRelated(final String query, String[] categories, String[] brands, final SingleSearchCallBack callback) {
		search(query, categories, brands, null, SearchRequest.POI_ENHANCED, callback, 10);
	}

	public static void search(final String query, final String[] categories, final String[] brands, SearchRegion region, final int requestType,
			final SingleSearchCallBack callBack, final int resultCount) {
		SingleSearchRequest singleSearchRequest = null;
		if (!GPSController.getInstance(mContext).isGPSEnable() && AppUtils.isFirstTimeStart()) {
			return;
		}
		if (!GPSController.getInstance(mContext).isGPSEnable() && region == null) {
			Location loc = PreferenceEngine.getInstance().getLastLocation();
			if(loc == null){
				
				return;
			}
			region = new SearchRegion(loc.getLatitude(), loc.getLongitude());
		}
		try {
			if (region != null) {
				singleSearchRequest = new SingleSearchRequest(getNBIContext(), query, region, categories, brands, resultCount, requestType,
						new MySingleSearchListener(callBack));
				cancelPrevSingleSearch();
				singleSearchRequest.startRequest();
				mSingleSearchRequestCache.add(singleSearchRequest);
				return;
			}

			GPSController.getInstance(mContext).requestSingleFix(new LocationListener() {
				public void locationUpdated(com.nbi.location.Location loc) {
					SearchRegion region = new SearchRegion(loc.getLatitude(), loc.getLongitude());
					SingleSearchRequest singleSearchRequest = new SingleSearchRequest(getNBIContext(), query, region, categories, brands,
							resultCount, SearchRequest.POI_ENHANCED, new MySingleSearchListener(callBack));
					cancelPrevSingleSearch();
					singleSearchRequest.startRequest();
					mSingleSearchRequestCache.add(singleSearchRequest);
				}

				@Override
				public void providerStateChanged(int newState) {
				}

				@Override
				public void onLocationError(int errorCode) {
					Nimlog.d(this, "[search][onLocationError]error code:" + errorCode);

				}
			}, GPSController.FAST_MODE);

		} catch (Exception e) {
			Log.e(TAG, "Search Error: ", e);
			callBack.onSingleSearchError(createNBIException(e), singleSearchRequest);
		}
	}

	public static SuggestionSearchRequest search(String query, SearchRegion region, final SingleSearchCallBack callBack, int resultCount) {
		SuggestionSearchRequest suggestionSearchRequest = null;
		if (region == null) {
			Location loc = PreferenceEngine.getInstance().getLastLocation();
			region = new SearchRegion(loc.getLatitude(), loc.getLongitude());
		}
		GPSController.getInstance(mContext).requestSingleFix(new LocationListener() {

			@Override
			public void locationUpdated(com.nbi.location.Location location) {
				
			}

			@Override
			public void providerStateChanged(int newState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationError(int errorCode) {
				Nimlog.d(this, "[search][onLocationError]error code:" + errorCode);

			}
		}, GPSController.FAST_MODE);
		try {
			suggestionSearchRequest = new SuggestionSearchRequest(getNBIContext(), query, region, resultCount, SearchRequest.POI_ENHANCED,
					new MySuggestionSearchListener(callBack));
			suggestionSearchRequest.startRequest();
		} catch (Exception e) {
			Log.e(TAG, "Suggestion Search Error: ", e);
			callBack.onSuggestionSearchError(createNBIException(e), suggestionSearchRequest);
		}
		return suggestionSearchRequest;
	}

	public static SingleSearchRequest searchNext(SingleSearchInformation info, SingleSearchRequest request, final SingleSearchCallBack callBack) {
		SingleSearchRequest singleSearchRequest = null;
		try {
			singleSearchRequest = new SingleSearchRequest(getNBIContext(), request, info, SingleSearchRequest.ITERATION_NEXT,
					new MySingleSearchListener(callBack));
			singleSearchRequest.startRequest();
		} catch (Exception e) {
			Log.e(TAG, "searchNext Error: ", e);
			callBack.onSingleSearchError(createNBIException(e), request);
		}
		return singleSearchRequest;
	}

	public static void searchPrev(SingleSearchInformation info, SingleSearchRequest request, final SingleSearchCallBack callBack) {
		SingleSearchRequest singleSearchRequest = null;
		try {
			singleSearchRequest = new SingleSearchRequest(getNBIContext(), request, info, SingleSearchRequest.ITERATION_PREV,
					new MySingleSearchListener(callBack));
			singleSearchRequest.startRequest();
		} catch (Exception e) {
			Log.e(TAG, "searchPrev Error: ", e);
			callBack.onSingleSearchError(createNBIException(e), request);
		}
	}

	private static void cancelPrevSingleSearch() {
		Iterator<SingleSearchRequest> requests = mSingleSearchRequestCache.iterator();
		while (requests.hasNext()) {
			requests.next().cancelRequest();
		}
		mSingleSearchRequestCache.clear();
	}

	private static NBIContext getNBIContext() {
		NBIContext nbiContext = new NBIContext(mContext, Credentials.instance().getValue(), null, null, ContextManager.getSavedCarrier(mContext));
		return nbiContext;
	}

	private static NBIException createNBIException(Exception e) {
		return new NBIException(new NBException(NBIException.INTERNAL_ERROR, e.getMessage()));
	}

	private static class MySingleSearchListener extends SingleSearchListenerImpl {
		private SingleSearchCallBack callBack;

		MySingleSearchListener(SingleSearchCallBack callBack) {
			this.callBack = callBack;
		}

		@Override
		public void onRequestCancelled(NBIRequest arg0) {
			super.onRequestCancelled(arg0);
		}

		@Override
		public void onRequestComplete(NBIRequest arg0) {
			super.onRequestComplete(arg0);
		}

		@Override
		public void onRequestProgress(int arg0, NBIRequest arg1) {
			super.onRequestProgress(arg0, arg1);
		}

		@Override
		public void onRequestStart(NBIRequest arg0) {
			super.onRequestStart(arg0);
		}

		@Override
		public void onRequestTimeOut(NBIRequest arg0) {
			super.onRequestTimeOut(arg0);
		}

		@Override
		public void onSingleSearch(SingleSearchInformation info, SingleSearchRequest request) {
			SingleSearchResult result = new SingleSearchResult();
			super.onSingleSearch(info, request);
			result.setData(ResultType.SINGLE_SEARCH_INFO, info);
			result.setData(ResultType.SINGLE_SEARCH_REQUEST, request);
			List<POI> poiList = new ArrayList<POI>();
			List<Interest> interestList = new ArrayList<Interest>();
			for (int i = 0; i < info.getResultCount(); i++) {
				POI poi = info.getPOI(i);
				poiList.add(poi);
				Interest interest = new Interest();
				interest.setAddress(LocationUtils.formatLocation(poi.getPlace().getLocation(), false));
				// interest.setDescription();
				interest.setPlaceName(poi.getPlace().getName());
				interest.setDistance(LocationUtils.formatDistance(poi.getDistance(), true, 100, true, false, false) + " "
						+ mContext.getResources().getString(R.string.interest_distance_away));
				interest.setThumbnailImageUrl(poi.getThumbnailImageUrl());
				interest.setImageUrl(poi.getImageUrl());
				if (poi.getThumbnailImageUrl() != null) {
					interest.setDataSrc(poi.getThumbnailImageUrl());
				} else {
					interest.setDataSrc(R.drawable.empty_photo);
				}
				Place place = new Place();
				if (poi instanceof FuelPOI) {
					place.copy(((FuelPOI) poi).getFuelPlace());
				} else {
					place.copy(poi.getPlace());
				}
				interest.setPlace(place);
				interestList.add(interest);
			}

			result.setData(ResultType.POI_LIST, poiList);
			result.setData(ResultType.INTEREST_LIST, interestList);
			callBack.onSingleSearchResults(result);
			prefetchImages(interestList);
		}

		@Override
		public void onRequestError(NBIException exception, NBIRequest request) {
			callBack.onSingleSearchError(exception, request);
			Toast.makeText(HurricaneApplication.getInstance(), exception.getErrorCode() + ": " + exception.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private static void prefetchImages(List<Interest> interests) {
		if(interests!= null && !interests.isEmpty()) {
			ImageWorkerTask task = new ImageWorkerTask();
			task.execute(interests);
		}
	}

	private static class ImageWorkerTask extends AsyncTask<List<Interest>, Void, Void> {

		@Override
		protected Void doInBackground(List<Interest>... params) {
			List<Interest> interests = params[0];
			for(Interest interest : interests) {
				Object src = interest.getDataSrc();
				if(src != null) {
					ImageUtils.getInstance(mContext).loadImage(src);
				}
			}
			return null;
		}
	}
	

	private static class MySuggestionSearchListener extends SuggestionSearchListenerImpl {
		private SingleSearchCallBack callBack;

		MySuggestionSearchListener(SingleSearchCallBack callBack) {
			this.callBack = callBack;
		}

		@Override
		public void onRequestCancelled(NBIRequest request) {
			super.onRequestCancelled(request);
		}

		@Override
		public void onRequestError(NBIException exception, NBIRequest request) {
			super.onRequestError(exception, request);
			callBack.onSuggestionSearchError(exception, request);
			Toast.makeText(HurricaneApplication.getInstance(), exception.getErrorCode() + ": " + exception.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onRequestProgress(int percentage, NBIRequest request) {
			super.onRequestProgress(percentage, request);
		}

		@Override
		public void onRequestStart(NBIRequest request) {
			super.onRequestStart(request);
		}

		@Override
		public void onRequestTimeOut(NBIRequest request) {
			super.onRequestTimeOut(request);
		}

		@Override
		public void onRequestComplete(NBIRequest request) {
			super.onRequestComplete(request);
		}

		@Override
		public void onSuggestionSearch(SuggestionSearchInformation info, SuggestionSearchRequest request) {
			super.onSuggestionSearch(info, request);
			SingleSearchResult result = new SingleSearchResult();
			result.setData(ResultType.SUGGESTION_SEARCH_INFO, info);
			result.setData(ResultType.SUGGESTION_SEARCH_REQUEST, request);
			int resultCount = info.getResultCount();
			List<SuggestMatch> matchList = new ArrayList<SuggestMatch>();
			List<Interest> interestList = new ArrayList<Interest>();
			SuggestMatch sMatch = null;
			for (int i = 0; i < resultCount; i++) {
				// int type = sMatch.getMatchType();
				sMatch = info.getSuggestMatch(i);
				matchList.add(sMatch);
			}

			result.setData(ResultType.SUGGESTION_MATCH_LIST, matchList);
			result.setData(ResultType.INTEREST_LIST, interestList);
			callBack.onSingleSearchResults(result);
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
			INTEREST_LIST, POI_LIST, SINGLE_SEARCH_INFO, SINGLE_SEARCH_REQUEST, SUGGESTION_SEARCH_INFO, SUGGESTION_SEARCH_REQUEST, SUGGESTION_MATCH_LIST
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

		public void onSingleSearchError(NBIException exception, NBIRequest request) {
		}

		public void onSuggestionSearchResults(SingleSearchResult result) {
		}

		public void onSuggestionSearchError(NBIException exception, NBIRequest request) {
		}
	}
}
