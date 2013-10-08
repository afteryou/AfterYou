package com.app.afteryou.controller;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.app.afteryou.controller.SingleSearchController.SingleSearchResult;
import com.app.afteryou.controller.SingleSearchController.SingleSearchResult.ResultType;
import com.app.afteryou.log.Nimlog;
import com.app.afteryou.model.Interest;
import com.nbi.search.singlesearch.SingleSearchInformation;
import com.nbi.search.singlesearch.SingleSearchRequest;

public class CacheManager {

	public static final int NO_RESULT_KEY = -999;
    public static final int NULL_KEY = -1;
    
    public static final class CacheType {
    	public static final byte TYPE_GENERAL = 1;
    	public static final byte TYPE_MORELIKE = 2;
    	//public static final byte TYPE_MOVIE = 2;
    }
    
    public static final class CacheOperation {
		public static final short OPERATION_CREATE = 1;
		public static final short OPERATION_APPEND = 0;
		public static final short OPERATION_APPEND_DETAILS = 2;
	}
    
    private Integer KEY_VALUE = new Integer(0);
    private SparseArray<ResultCacheInfo> cacheKey_cacheData_mapping;
    private SparseIntArray poiType_cachekey_mapping;
    
    public static class ResultCacheInfo {
    	private SparseArray<Object> mResultData;
    	private SparseArray<Object> mLastAppended;
    	private int mCurrentHighLight;
        private boolean hasMore = false;
        private int mPosition = -1;
        private byte cacheType;
        private SingleSearchInformation searchInfo;
        private SingleSearchRequest searchRequest;
        
        public SingleSearchInformation getSearchInfo() {
			return searchInfo;
		}

		public void setSearchInfo(SingleSearchInformation searchInfo) {
			this.searchInfo = searchInfo;
		}

		public SingleSearchRequest getSearchRequest() {
			return searchRequest;
		}

		public void setSearchRequest(SingleSearchRequest searchRequest) {
			this.searchRequest = searchRequest;
		}

		class ResultPage {
            private int firstItemIndex;
            private int lastItemIndex;
            private int premiumPOICount = 0;

            int getDisplaySize() {
                int size = lastItemIndex - firstItemIndex + 1 - premiumPOICount;

                return size;
            }
        }
        private ArrayList<ResultPage> pageList = new ArrayList<ResultPage>();
        private int pageIndex;
        
        ResultCacheInfo() {
            mResultData = new SparseArray<Object>();
            mLastAppended = new SparseArray<Object>();
        }

        ResultCacheInfo(SparseArray<Object> data) {
            mResultData = data;
            mLastAppended = data;
        }
        
        void addNewPage(int startIndex, int endIndex) {
            ResultPage page = new ResultPage();
            page.firstItemIndex = startIndex;
            page.lastItemIndex = endIndex;
            pageList.add(page);
        }

        void addNewPage(int startIndex, int endIndex, int premiumCount) {
            ResultPage page = new ResultPage();
            page.firstItemIndex = startIndex;
            page.lastItemIndex = endIndex;
            page.premiumPOICount = premiumCount;
            pageList.add(page);
        }
        
        public byte getCacheType() {
            return cacheType;
        }

        public void setCacheType(byte aCacheType) {
            this.cacheType = aCacheType;
        }
        
        public SparseArray<Object> getResultData() {
            return mResultData;
        }
        
        public void setResultData(SparseArray<Object> resultData) {
            mResultData = resultData;
        }
        
        public SparseArray<Object> getLastAppended() {
            return mLastAppended;
        }
        
        public void setLastAppended(SparseArray<Object> appended) {
        	mLastAppended = appended;
        }
        
        public int getCurrentHighLight() {
            return mCurrentHighLight;
        }
        
        public void setCurrentHighLight(int currentHighLight) {
            this.mCurrentHighLight = currentHighLight;
            locatePageIndex();
        }
        
        private void locatePageIndex() {
            pageIndex = 0;
            if (pageList.size() == 1) {
                return;
            }
            for (ResultPage page : pageList) {
                if (mCurrentHighLight >= page.firstItemIndex
                        && mCurrentHighLight <= page.lastItemIndex) {
                    break;
                }
                pageIndex++;
            }

        }
        
        public boolean hasMore() {
            return hasMore;
        }

        public void setHasMore(boolean more) {
            this.hasMore = more;
        }

        public boolean needRequestMore() {
            return hasMore && (pageIndex == pageList.size() - 1);
        }
        
        public int getCurrentPage() {
            return pageIndex;
        }
        
        public int getResultCacheSize() {
            if (mResultData != null) {
                return mResultData.size();
            } else {
                return -1;
            }
        }
        
        public Object getItem(int index) {
            if (mResultData != null) {
                return mResultData.get(index);
            } else {
                return null;
            }
        }
    }

    CacheManager() {
        cacheKey_cacheData_mapping = new SparseArray<ResultCacheInfo>();
        poiType_cachekey_mapping = new SparseIntArray();
    }
    
    public int saveCache(byte type, SingleSearchResult result, short operation) {
    	List<Interest> interests = result.getData(ResultType.INTEREST_LIST);
		SparseArray<Object> data = new SparseArray<Object>();
		for(int i=0; i < interests.size(); i++) {
			data.put(i, interests.get(i));
		}
    	
        int key;
        if (operation == CacheOperation.OPERATION_CREATE) {
            key = createNewCache(type, result);
        } else {
            key = appendToCache(type, result);
        }
        if (readCache(key) == null || readCache(key).getResultData() == null || readCache(
                        key).getResultData().size() <= 0) {
            key = NO_RESULT_KEY;
        }
        return key;
    }
    
    public synchronized ResultCacheInfo readCache(int key) {
        return cacheKey_cacheData_mapping.get(key);
    }
    
    public synchronized void clearAllCache() {
        cacheKey_cacheData_mapping.clear();
        poiType_cachekey_mapping.clear();
        synchronized (KEY_VALUE) {
            KEY_VALUE = new Integer(0);
        }
        Nimlog.i(this, "All caches has been clear.");
    }
    
    private synchronized int createNewCache(byte type, SingleSearchResult result) {
		SparseArray<Object> data = parseResultData(result);
        int key = generalNewKey();
        removePrevResult(type);
        ResultCacheInfo cache = new ResultCacheInfo(data);
        cache.addNewPage(0, data.size() - 1);
        cache.setSearchInfo((SingleSearchInformation) result.getData(ResultType.SINGLE_SEARCH_INFO));
        cache.setSearchRequest((SingleSearchRequest) result.getData(ResultType.SINGLE_SEARCH_REQUEST));
        cache.setCacheType(type);
        cacheKey_cacheData_mapping.put(key, cache);
        poiType_cachekey_mapping.put(type, key);
        //perfromQALog(type, NBQALogger.SEARCH_CACHE_OPERATION_CREATE, 0);

        Nimlog.i(this, "Create new cache for type " + type + " with new key " + key + " ,length "
                + cache.getResultData().size());
        return key;
    }
    
    private synchronized int appendToCache(byte type, SingleSearchResult result) {
        if (poiType_cachekey_mapping.indexOfKey(type) < 0) {
            return createNewCache(type, result);
        }
        int key = poiType_cachekey_mapping.get(type);
        ResultCacheInfo cache = cacheKey_cacheData_mapping.get(key);
        if (cache == null) {
            return createNewCache(type, result);
        } else {
            append(cache, result);
            Nimlog.i(this, "Result append to cache of type " + type + " key " + key
                    + " length " + cache.getResultData().size());
            return key;
        }
    }
    
    private SparseArray<Object> parseResultData(SingleSearchResult result) {
    	List<Interest> interests = result.getData(ResultType.INTEREST_LIST);
		SparseArray<Object> data = new SparseArray<Object>();
		for(int i=0; i < interests.size(); i++) {
			data.put(i, interests.get(i));
		}
		
		return data;
    }
    
    private void append(ResultCacheInfo cache, SingleSearchResult result) {
    	SparseArray<Object> data = parseResultData(result);
        int prevCount = cache.getResultData().size();
        int count = data.size();
        int index = 0;
        cache.setLastAppended(new SparseArray<Object>());
        for (int i = prevCount; i < prevCount + count; i++) {
        	cache.getLastAppended().put(index, data.get(index));
            cache.getResultData().put(i, data.get(index));
        	index++;
        }
        cache.addNewPage(prevCount, data.size() - 1);
        cache.setSearchInfo((SingleSearchInformation) result.getData(ResultType.SINGLE_SEARCH_INFO));
        cache.setSearchRequest((SingleSearchRequest) result.getData(ResultType.SINGLE_SEARCH_REQUEST));
    }
    
    private void removePrevResult(byte type) {
        try {
            if (poiType_cachekey_mapping.size() <= 0 || poiType_cachekey_mapping.indexOfKey(type) < 0) {
                return;
            }

            int key = poiType_cachekey_mapping.get(type);
            if (cacheKey_cacheData_mapping.get(key) == null) {
            	Nimlog.i(this, "No prev result to remove for type " + type);
            } else {
                //perfromQALog(type, NBQALogger.SEARCH_CACHE_OPERATION_CLEAR);
                cacheKey_cacheData_mapping.remove(key);
                poiType_cachekey_mapping.put(type, NULL_KEY);
                Nimlog.i(this, "Prev result has removed for type " + type);
            }
        } catch (Exception e) {
            Nimlog.printStackTrace(e);
        }
    }
    
    private int generalNewKey() {
        synchronized (KEY_VALUE) {
            KEY_VALUE++;
            return KEY_VALUE.intValue();
        }

    }
    
    public int getKeyFromType(byte type) {
        if (poiType_cachekey_mapping.indexOfKey(type) >= 0) {
            return poiType_cachekey_mapping.get(type);
        } else {
            return NO_RESULT_KEY;
        }
    }
    
    private int getResultSize(byte type) {
        int key = getKeyFromType(type);
        if (key == CacheManager.NO_RESULT_KEY) {
            return 0;
        }
        return readCache(key).getResultCacheSize();
    }
}
