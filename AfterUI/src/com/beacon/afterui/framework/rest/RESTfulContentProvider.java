package com.beacon.afterui.framework.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
     * Source will indicate, for whom the request is placed. Values will hold
     * the post parameters.
     * 
     * @param source
     * @param uri
     * @param values
     */
    public void asyncInsertRequest(String source, Uri uri, ContentValues values) {

    }
    
    public void requestComplete(String mQueryText) {
        
    }
}
