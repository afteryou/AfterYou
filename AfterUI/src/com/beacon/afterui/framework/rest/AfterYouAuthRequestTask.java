package com.beacon.afterui.framework.rest;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

public class AfterYouAuthRequestTask extends UriRequestTask {

    /** TAG */
    private static final String TAG = AfterYouAuthRequestTask.class
            .getSimpleName();

    public AfterYouAuthRequestTask(Context appContext,
            RESTfulContentProvider siteProvider, ContentValues values,
            String queryText) {
        super(appContext, siteProvider, values, queryText);
    }

    @Override
    public void run() {

        
        Log.d(TAG, "AfterYouAuthRequestTask running successfully");
        mSiteProvider.requestComplete(mRequestTag);
    }

}
