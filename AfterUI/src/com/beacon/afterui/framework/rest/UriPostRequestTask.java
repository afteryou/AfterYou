package com.beacon.afterui.framework.rest;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Provides a runnable that uses an HttpClient to asynchronously load a given
 * URI. After the network content is loaded, the task delegates handling of the
 * request to a ResponseHandler specialized to handle the given content.
 */
public class UriPostRequestTask extends UriRequestTask {

    /** TAG */
    private static final String TAG = UriPostRequestTask.class.getSimpleName();

    private int mRawResponse = -1;

    // private int mRawResponse = R.raw.map_src;

    public UriPostRequestTask(HttpUriRequest request, ResponseHandler handler,
            Context appContext) {
        super(request, handler, appContext);
    }

    public UriPostRequestTask(String requestTag,
            RESTfulContentProvider siteProvider, HttpUriRequest request,
            ResponseHandler handler, Context appContext) {
        super(requestTag, siteProvider, request, handler, appContext);
    }

    public void setRawResponse(int rawResponse) {
        mRawResponse = rawResponse;
    }

    /**
     * Carries out the request on the complete URI as indicated by the protocol,
     * host, and port contained in the configuration, and the URI supplied to
     * the constructor.
     */
    public void run() {
        HttpResponse response;

        try {
            response = execute(mRequest);
            mHandler.handleResponse(response, getUri());
        } catch (IOException e) {
            Log.w(TAG, "exception processing asynch request", e);
        } finally {
            if (mSiteProvider != null) {
                mSiteProvider.requestComplete(mRequestTag);
            }
        }
    }

    private HttpResponse execute(HttpUriRequest mRequest) throws IOException {
        // if (mRawResponse >= 0) {
        // return new RawResponse(mAppContext, mRawResponse);
        // } else {
        HttpClient client = new DefaultHttpClient();
        return client.execute(mRequest);
        // }
    }

    public Uri getUri() {
        return Uri.parse(mRequest.getURI().toString());
    }
}
