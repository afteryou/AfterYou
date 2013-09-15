package com.beacon.afterui.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.beacon.afterui.framework.rest.RESTfulContentProvider;
import com.beacon.afterui.provider.AfterYouMetadata.AuthDetails;
import com.beacon.afterui.provider.AfterYouMetadata.AuthTable;

public class AfterYouContentProvider extends RESTfulContentProvider {

    /** TAG */
    private static final String TAG = AfterYouContentProvider.class
            .getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "after_you.db";

    private DatabaseHelper mDatabaseHelper;

    private SQLiteDatabase mDb;

    private static final boolean DEBUG = true;

    private static UriMatcher sUriMatcher;

    private static final int AUTH = 0;
    private static final int AUTH_ID = 1;

    private static final int AUTH_DETAILS = 2;
    private static final int AUTH_DETAILS_ID = 3;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(AfterYouMetadata.AUTHORITY, AuthTable.TABLE_NAME,
                AUTH);
        sUriMatcher.addURI(AfterYouMetadata.AUTHORITY, AuthTable.TABLE_NAME
                + "/#", AUTH_ID);

        sUriMatcher.addURI(AfterYouMetadata.AUTHORITY, AuthDetails.TABLE_NAME,
                AUTH_DETAILS);
        sUriMatcher.addURI(AfterYouMetadata.AUTHORITY, AuthTable.TABLE_NAME
                + "/#", AUTH_DETAILS_ID);

    }

    @Override
    public boolean onCreate() {

        mDatabaseHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null,
                DATABASE_VERSION);
        mDb = mDatabaseHelper.getWritableDatabase();

        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues cv, SQLiteDatabase db) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SQLiteDatabase getDatabase() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
        case AUTH:
            return AuthTable.CONTENT_TYPE;

        case AUTH_ID:
            return AuthTable.CONTENT_ITEM_TYPE;

        case AUTH_DETAILS:
            return AuthDetails.CONTENT_TYPE;

        case AUTH_DETAILS_ID:
            return AuthDetails.CONTENT_ITEM_TYPE;

        default:
            throw new IllegalArgumentException("Unknown URI");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String tableName = null;

        switch (sUriMatcher.match(uri)) {
        case AUTH:
            tableName = AuthTable.TABLE_NAME;
            break;

        default:
            throw new IllegalArgumentException("Unknown URI");
        }

        long id = mDb.insert(tableName, null, values);

        if (id > 0) {
            Uri insertUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(insertUri, null);
            return insertUri;
        }

        throw new IllegalStateException("could not insert "
                + "content values: " + values);

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name,
                CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(getQueryForAuthTable());
                db.execSQL(getQueryForAuthDetailsTable());
            } catch (SQLiteException e) {
                Log.e(TAG, "DatabaseHelper:onCreate: " + e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private static String getQueryForAuthTable() {
        StringBuilder query = new StringBuilder();

        query.append("CREATE TABLE IF NOT EXISTS " + AuthTable.TABLE_NAME
                + " (");
        query.append(AuthTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        query.append(AuthTable.AUTH_SOURCE + " TEXT, ");
        query.append(AuthTable.AUTH_ID + " TEXT, ");
        query.append(AuthTable.STATUS + " INTEGER,");
        query.append(AuthTable.STATUS_MESSAGE + " TEXT);");

        if (DEBUG) {
            Log.d(TAG, "getQueryForAuthTable() : " + query);
        }

        return query.toString();
    }

    private static String getQueryForAuthDetailsTable() {
        StringBuilder query = new StringBuilder();

        query.append("CREATE TABLE IF NOT EXISTS " + AuthDetails.TABLE_NAME
                + " (");
        query.append(AuthDetails._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        query.append(AuthDetails.AUTH_ID + " INTEGER NOT NULL, ");
        query.append(AuthDetails.MIME_TYPE + " TEXT, ");
        query.append(AuthDetails.DATA + " TEXT);");

        if (DEBUG) {
            Log.d(TAG, "getQueryForAuthDetailsTable() : " + query);
        }

        return query.toString();
    }
}
