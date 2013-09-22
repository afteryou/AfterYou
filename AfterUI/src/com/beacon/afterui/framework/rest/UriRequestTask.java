package com.beacon.afterui.framework.rest;

import org.apache.http.client.methods.HttpUriRequest;

import android.content.ContentValues;
import android.content.Context;

public abstract class UriRequestTask implements Runnable {

    /** TAG */
    private static final String TAG = UriRequestTask.class.getSimpleName();

    protected HttpUriRequest mRequest;
    protected ResponseHandler mHandler;

    protected Context mAppContext;

    protected RESTfulContentProvider mSiteProvider;
    protected String mRequestTag;

    protected ContentValues mContentValues;

    public UriRequestTask(Context appContext,
            RESTfulContentProvider siteProvider, ContentValues values,
            String queryText) {
        mAppContext = appContext;
        mSiteProvider = siteProvider;
        mContentValues = values;
        mRequestTag = queryText;
    }

    public UriRequestTask(HttpUriRequest request, ResponseHandler handler,
            Context appContext) {
        this(null, null, request, handler, appContext);
    }

    public UriRequestTask(String requestTag,
            RESTfulContentProvider siteProvider, HttpUriRequest request,
            ResponseHandler handler, Context appContext) {
        mRequestTag = requestTag;
        mSiteProvider = siteProvider;
        mRequest = request;
        mHandler = handler;
        mAppContext = appContext;
    }
}
