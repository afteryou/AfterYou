package com.app.afteryou.search;

import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
	   /**
     * This is the provider authority identifier.  The same string must appear in your
     * Manifest file, and any time you instantiate a 
     * {@link android.provider.SearchRecentSuggestions} helper class. 
     */
    public final static String AUTHORITY = "com.navbuilder.app.hurricane.search.SearchSuggestionsProvider";
    /**
     * These flags determine the operating mode of the suggestions provider.  This value should 
     * not change from run to run, because when it does change, your suggestions database may 
     * be wiped.
     */
    public final static int MODE = DATABASE_MODE_QUERIES;
    
    /**
     * The main job of the constructor is to call {@link #setupSuggestions(String, int)} with the
     * appropriate configuration values.
     */
    public SearchSuggestionsProvider() {
        super();
        setupSuggestions(AUTHORITY, MODE);
    }

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return super.query(uri, projection, selection, selectionArgs, sortOrder);
	}
    
    
}
