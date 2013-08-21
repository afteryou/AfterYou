package com.app.afteryou.search;

import android.util.Log;

import com.nbi.common.NBIException;
import com.nbi.common.NBIRequest;
import com.nbi.search.singlesearch.SingleSearchInformation;
import com.nbi.search.singlesearch.SingleSearchListener;
import com.nbi.search.singlesearch.SingleSearchRequest;

public class SingleSearchListenerImpl implements SingleSearchListener {
    private final static String TAG = "SingleSearchListenerImpl";
    
    @Override
    public void onRequestCancelled(NBIRequest arg0) {
        Log.i(TAG, "onRequestCancelled");
    }
    
    @Override
    public void onRequestComplete(NBIRequest arg0) {
        Log.i(TAG, "onRequestComplete");
    }
    
    @Override
    public void onRequestError(NBIException arg0, NBIRequest arg1) {
        Log.i(TAG, "onRequestError: code=" + arg0.getErrorCode() + ", " + arg0.getMessage());
    }
    
    @Override
    public void onRequestProgress(int arg0, NBIRequest arg1) {
        Log.i(TAG, "onRequestProgress");
    }
    
    @Override
    public void onRequestStart(NBIRequest arg0) {
        Log.i(TAG, "onRequestStart");
    }
    
    @Override
    public void onRequestTimeOut(NBIRequest arg0) {
        Log.i(TAG, "onRequestTimeOut");
    }
    
    @Override
    public void onSingleSearch(SingleSearchInformation arg0, SingleSearchRequest arg1) {
        Log.i(TAG, "onSingleSearch: ResultCount: " + arg0.getResultCount());
    }
    
}
