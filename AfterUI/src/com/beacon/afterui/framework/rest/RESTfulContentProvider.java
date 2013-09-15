package com.beacon.afterui.framework.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import org.apache.http.client.methods.HttpGet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Encapsulates functions for asynchronous RESTful requests so that subclass
 * content providers can use them for initiating request while still using
 * custom methods for interpreting REST based content such as, RSS, ATOM, JSON,
 * etc.
 */
public abstract class RESTfulContentProvider extends ContentProvider {

    /** TAG */
    private static final String TAG = RESTfulContentProvider.class
            .getSimpleName();

    private Map<String, UriRequestTask> mRequestsInProgress = new HashMap<String, UriRequestTask>();

    protected static final String APP_AUTH = "after_you";

    public RESTfulContentProvider() {

    }

    public abstract Uri insert(Uri uri, ContentValues cv, SQLiteDatabase db);

    /**
     * Allows the subclass to define the database used by a response handler.
     * 
     * @return database passed to response handler.
     */
    public abstract SQLiteDatabase getDatabase();

    public static String encode(String gDataQuery) {
        try {
            return URLEncoder.encode(gDataQuery, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "could not decode UTF-8," + " this should not happen");
        }
        return null;
    }

    /**
     * Abstract method that allows a subclass to define the type of handler that
     * should be used to parse the response of a given request.
     *
     * @param requestTag
     *            unique tag identifying this request.
     * @return The response handler created by a subclass used to parse the
     *         request response.
     */
    protected abstract ResponseHandler newResponseHandler(String requestTag);

    private UriRequestTask newQueryTask(String requestTag, String url,
            ContentValues values) {
        UriRequestTask requestTask = null;

        final HttpGet get = new HttpGet(url);
        ResponseHandler handler = newResponseHandler(requestTag);

        if (requestTag.equals(APP_AUTH)) {
            requestTask = new AfterYouAuthRequestTask(getContext(), this,
                    values, requestTag);
        }

        // requestTask = new UriRequestTask(getContext(), requestTag);

        if (requestTask == null) {
            Log.d(TAG, "newQueryTask() : request task is NULL!");
            return null;
        }

        mRequestsInProgress.put(requestTag, requestTask);
        return requestTask;
    }

    private UriRequestTask getRequestTask(String queryText) {
        return mRequestsInProgress.get(queryText);
    }

    /**
     * Source will indicate, for whom the request is placed. Values will hold
     * the post parameters.
     * 
     * @param source
     * @param uri
     * @param values
     */
    public void asyncInsertRequest(String source, Uri uri, ContentValues values) {
        synchronized (mRequestsInProgress) {
            UriRequestTask requestTask = getRequestTask(source);

            if (requestTask == null) {
                requestTask = newQueryTask(source, uri.toString(), values);
                Thread thread = new Thread(requestTask);
                // allows other requests to run in parallel.
                thread.start();
            }
        }
    }

    public void requestComplete(String mQueryText) {

    }
}
