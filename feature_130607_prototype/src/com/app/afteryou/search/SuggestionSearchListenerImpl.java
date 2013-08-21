package com.app.afteryou.search;

import android.util.Log;

import com.nbi.common.NBIException;
import com.nbi.common.NBIRequest;
import com.nbi.search.singlesearch.SuggestionSearchInformation;
import com.nbi.search.singlesearch.SuggestionSearchListener;
import com.nbi.search.singlesearch.SuggestionSearchRequest;

public class SuggestionSearchListenerImpl implements SuggestionSearchListener {
    private final static String TAG = "SuggestionSearchListenerImpl";

    @Override
    public void onRequestCancelled(NBIRequest request) {
        Log.i(TAG, "onRequestCancelled");
    }
    
    @Override
    public void onRequestError(NBIException exception, NBIRequest request) {
        Log.i(TAG, "onRequestError");
    }
    
    @Override
    public void onRequestProgress(int percentage, NBIRequest request) {
        Log.i(TAG, "onRequestProgress");
    }
    
    @Override
    public void onRequestStart(NBIRequest request) {
        Log.i(TAG, "onRequestStart");
    }
    
    @Override
    public void onRequestTimeOut(NBIRequest request) {
        Log.i(TAG, "onRequestTimeOut");
    }
    
    @Override
    public void onRequestComplete(NBIRequest request) {
        Log.i(TAG, "onRequestComplete");
    }
    
    @Override
    public void onSuggestionSearch(SuggestionSearchInformation information, SuggestionSearchRequest request) {
        Log.i(TAG, "onSuggestionSearch");
    }
    
}
