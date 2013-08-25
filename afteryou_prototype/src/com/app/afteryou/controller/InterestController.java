package com.app.afteryou.controller;

import android.content.Context;

import com.app.afteryou.controller.CacheManager.ResultCacheInfo;
import com.app.afteryou.controller.SingleSearchController.SingleSearchCallBack;
import com.app.afteryou.controller.SingleSearchController.SingleSearchResult;
import com.app.afteryou.fragment.ISearchFunction.SearchParams;
import com.nbi.common.NBIException;
import com.nbi.common.NBIRequest;
import com.nbi.search.singlesearch.SingleSearchInformation;

public class InterestController {

	private static InterestController mInstance;
	private CacheManager mCacheManager;

	private Context mContext;

	public static final int REQUEST_TYPE_INIT = 0;
	public static final int REQUEST_TYPE_APPEND = 1;
	public static final int REQUEST_TYPE_REFRESH = 2;
	public static final int REQUEST_TYPE_RELATED = 3;
	public static final int REQUEST_TYPE_CATEGORY = 4;
	public static final int REQUEST_TYPE_BRAND = 5;

	public static final int STATE_IDLE = 0;
	public static final int STATE_RUNNING = 1;
	private int mState = STATE_IDLE;
	
	public static final int RESULT_LIMIT = 50;

	private InterestCallBack mCallback;

	private InterestController(Context ctx) {
		mContext = ctx;
		mCacheManager = new CacheManager();
	}

	public static InterestController getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new InterestController(ctx);
		}
		return mInstance;
	}

	public CacheManager getCacheManager() {
		return mCacheManager;
	}

	public void loadInterests(final int type, InterestCallBack callback, Object... data) {
		if(isRunning()) {
				resetState();
			//return;
		}
		if(callback != null) {
			mCallback = callback;
		}
		switch (type) {
		case REQUEST_TYPE_INIT:
		case REQUEST_TYPE_REFRESH:
		case REQUEST_TYPE_CATEGORY:
		case REQUEST_TYPE_BRAND:
			setState(STATE_RUNNING);
			SearchParams params = null;
			if(data != null && data.length>0
					&& data[0] instanceof SearchParams) {
				params = (SearchParams) data[0];
			} else {
				params = new SearchParams();
			}

			SingleSearchController.search(params.query, params.categories, params.brands, new SingleSearchCallBack() {
				public void onSingleSearchResults(SingleSearchResult result) {
					int key = mCacheManager.saveCache(CacheManager.CacheType.TYPE_GENERAL, result, CacheManager.CacheOperation.OPERATION_CREATE);
					if(mCallback != null) {
						mCallback.onResult(type, key);
					}
					resetState();
				}
				public void onSingleSearchError(NBIException exception, NBIRequest request) {
					resetState();
				}
			});
			break;
			
		case REQUEST_TYPE_APPEND:
			setState(STATE_RUNNING);
			int key = CacheManager.NULL_KEY;
			if(data != null && data.length >0 
					&& data[0] instanceof Integer) {
					key = (Integer) data[0];
			}
			if (key == CacheManager.NULL_KEY) {
				resetState();
				return;
			}
			ResultCacheInfo cache = mCacheManager.readCache(key);
			if(cache.getResultCacheSize() < RESULT_LIMIT) {
				SingleSearchController.searchNext((SingleSearchInformation) cache.getSearchInfo(), cache.getSearchRequest(), new SingleSearchCallBack() {
					public void onSingleSearchResults(SingleSearchResult result) {
						int key = mCacheManager.saveCache(CacheManager.CacheType.TYPE_GENERAL, result, CacheManager.CacheOperation.OPERATION_APPEND);
						if(mCallback != null) {
							mCallback.onResult(type, key);
						}
						resetState();
					}
					public void onSingleSearchError(NBIException exception, NBIRequest request) {
						resetState();
					}
				});
			} else {
				resetState();
			}
			break;
		case REQUEST_TYPE_RELATED:
			setState(STATE_RUNNING);
			if(data != null && data.length>0
					&& data[0] instanceof SearchParams) {
				params = (SearchParams) data[0];
			} else {
				params = new SearchParams();
			}
			SingleSearchController.searchRelated(params.query, params.categories, params.brands, new SingleSearchCallBack() {
				public void onSingleSearchResults(SingleSearchResult result) {
					int key = mCacheManager.saveCache(CacheManager.CacheType.TYPE_MORELIKE, result, CacheManager.CacheOperation.OPERATION_CREATE);
					if(mCallback != null) {
						mCallback.onResult(type, key);
					}
					resetState();
				}
				
				public void onSingleSearchError(NBIException exception, NBIRequest request) {
					resetState();
				}
			});

			break;
		default:
			throw new IllegalArgumentException("Unsupported type!");
		}
	}
	
	private void setState(int state) {
		mState = state;
	}

	private void resetState() {
		mState = STATE_IDLE;
		mCallback = null;
	}

	private boolean isRunning() {
		return mState == STATE_RUNNING;
	}

	public interface InterestCallBack {
		public void onResult(int type, Object result);
	}
	
}
