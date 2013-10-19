package com.beacon.afterui.provider;

import android.content.ContentProvider;
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
import android.text.TextUtils;
import android.util.Log;

import com.beacon.afterui.provider.AfterYouMetadata.RosterTable;

public class AfterYouContentProvider extends ContentProvider {

    /** TAG */
    private static final String TAG = AfterYouContentProvider.class
            .getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "after_you.db";

    private DatabaseHelper mDatabaseHelper;

    private SQLiteDatabase mDb;

    private static final boolean DEBUG = true;

    private static UriMatcher sUriMatcher;

    private static final int ROSTER = 0;
    private static final int ROSTER_ID = 1;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(AfterYouMetadata.AUTHORITY, RosterTable.TABLE_NAME,
                ROSTER);
        sUriMatcher.addURI(AfterYouMetadata.AUTHORITY, RosterTable.TABLE_NAME
                + "/#", ROSTER_ID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case ROSTER:
            return RosterTable.CONTENT_TYPE;

        case ROSTER_ID:
            return RosterTable.CONTENT_ITEM_TYPE;

        default:
            throw new IllegalArgumentException("Unknown URI");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String tableName = null;
        switch (sUriMatcher.match(uri)) {
        case ROSTER:
        case ROSTER_ID:
            tableName = RosterTable.TABLE_NAME;
            break;

        default:
            throw new IllegalArgumentException("URL not known!");
        }

        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException(
                    "Write op not supported for this URI!");
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long id = db.insert(tableName, null, values);

        if (id >= 0) {
            Uri insertUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(insertUri, null);
            return insertUri;
        }

        throw new IllegalStateException("Could not insert!");
    }

    @Override
    public boolean onCreate() {

        mDatabaseHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null,
                DATABASE_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

        String tableName = null;
        switch (sUriMatcher.match(uri)) {
        case ROSTER:
        case ROSTER_ID:
            tableName = RosterTable.TABLE_NAME;
            break;

        default:
            throw new IllegalArgumentException("URL not known!");
        }

        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException(
                    "Read op not supported for this URI!");
        }

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        return db.query(tableName, projection, selection, selectionArgs, null,
                null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {

        String tableName = null;
        switch (sUriMatcher.match(uri)) {
        case ROSTER:
        case ROSTER_ID:
            tableName = RosterTable.TABLE_NAME;
            break;

        default:
            throw new IllegalArgumentException("URL not known!");
        }

        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException(
                    "Write op not supported for this URI!");
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowsAffected = db.update(tableName, values, selection,
                selectionArgs);

        if (rowsAffected > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return rowsAffected;
        }

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
                db.execSQL(getQueryForRosterTable());
            } catch (SQLiteException e) {
                Log.e(TAG, "DatabaseHelper:onCreate: " + e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private static String getQueryForRosterTable() {
        StringBuilder query = new StringBuilder();

        query.append("CREATE TABLE IF NOT EXISTS " + RosterTable.TABLE_NAME
                + " (");
        query.append(RosterTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        query.append(RosterTable.NAME + " TEXT, ");
        query.append(RosterTable.USER_NAME + " TEXT, ");
        query.append(RosterTable.STATUS + " TEXT,");
        query.append(RosterTable.STATUS_TEXT + " TEXT,");
        query.append(RosterTable.SUBSCRIPTION_TYPE + " TEXT,");
        query.append(RosterTable.AVATAR + " TEXT);");

        if (DEBUG) {
            Log.d(TAG, "getQueryForRosterTable() : " + query);
        }

        return query.toString();
    }
}
