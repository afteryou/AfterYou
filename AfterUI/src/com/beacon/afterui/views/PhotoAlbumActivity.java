package com.beacon.afterui.views;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.utils.customviews.CustomProgressDialog;
import com.beacon.afterui.views.gallery.ImageCache.ImageCacheParams;
import com.beacon.afterui.views.gallery.ImageFetcher;

public class PhotoAlbumActivity extends BaseActivity implements
        OnClickListener, OnItemClickListener,
        android.app.LoaderManager.LoaderCallbacks<Cursor> {

    /** TAG */
    private static final String TAG = PhotoAlbumActivity.class.getSimpleName();

    private TextView mCancelBtn;
    private TextView mDoneBtn;

    private ListView mListView;

    private TextView mNoImageGallary;

    public static final String ID = "id";
    public static final String NAME = "name";
    private CustomProgressDialog mWaitProgress;

    private static final int PHOTO_ALBUM_LOADER_ID = 0;

    private AlbumCursorAdapter mAlbumCursorAdapter;

    private ImageFetcher mImageFetcher;

    private Cursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photo_album);
        setBehindLeftContentView(R.layout.photo_album);
        setBehindRightContentView(R.layout.photo_album);
        mCancelBtn = (TextView) findViewById(R.id.cancel_btn_photo_album);
        mDoneBtn = (TextView) findViewById(R.id.done_btn_photo_album);
        mListView = (ListView) findViewById(R.id.album_list);
        mListView.setOnItemClickListener(this);
        mListView.setDivider(getResources().getDrawable(
                R.drawable.divider_listview));
        mNoImageGallary = (TextView) findViewById(R.id.no_images);
        // TextView chooseFromLiTxt = (TextView)
        // findViewById(R.id.choose_from_library_txt);

        // font myriadPro semibold
        Typeface itcAvaStdBk = Typeface.createFromAsset(getAssets(),
                "fonts/ITCAvantGardeStd-Bk.otf");
        mCancelBtn.setTypeface(itcAvaStdBk);
        mDoneBtn.setTypeface(itcAvaStdBk);
        // chooseFromLiTxt.setTypeface(typeFaceSemiBold);

        mCancelBtn.setOnClickListener(this);

        ImageCacheParams cacheParams = new com.beacon.afterui.views.gallery.ImageCache.ImageCacheParams();
        cacheParams.setMemCacheSizePercent(0.2f);
        com.beacon.afterui.views.gallery.ImageCache imageCache = new com.beacon.afterui.views.gallery.ImageCache(
                cacheParams);
        Log.d("ImageCache", imageCache.toString()
                + "imagecashe object is created");

        mImageFetcher = new ImageFetcher(this, 50, imageCache);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);

        mAlbumCursorAdapter = new AlbumCursorAdapter(this, null, true);
        mListView.setAdapter(mAlbumCursorAdapter);
        getLoaderManager().initLoader(PHOTO_ALBUM_LOADER_ID, null, this);

        // mHandlerThread = new HandlerThread("album_loader");
        // mHandlerThread.start();
        // mAlbumHandler = new Handler(mHandlerThread.getLooper());
        //
        // mHandler.sendEmptyMessage(LOADING_ALBUMS);
    }

    protected void removeDialog() {
        if (null != PhotoAlbumActivity.this && null != mWaitProgress
                && mWaitProgress.isShowing()) {

            mWaitProgress.dismiss();

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.cancel_btn_photo_album:
            setResult(RESULT_CANCELED);
            finish();
            break;
        }
    }

    private class AlbumCursorAdapter extends CursorAdapter {

        private LayoutInflater mInflator;
        private Typeface mFont;

        public AlbumCursorAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
            mInflator = getLayoutInflater();
            mFont = FontUtils.loadTypeFace(getApplicationContext(),
                    FontUtils.MYRIAD_PRO_REGULAR);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            final String bucketDisplayName = cursor.getString(cursor
                    .getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME));
            TextView albumName = (TextView) view.findViewById(R.id.album_name);
            albumName.setText(bucketDisplayName);

            final String bucketPhotoCount = cursor.getString(cursor
                    .getColumnIndex("count"));
            TextView albumCount = (TextView) view.findViewById(R.id.number_txt);
            albumCount.setText("(" + bucketPhotoCount + ")");

            ImageView coverImage = (ImageView) view
                    .findViewById(R.id.photo_of_user);
            final String bucketId = cursor.getString(cursor
                    .getColumnIndex(ImageColumns._ID));
            mImageFetcher.loadImage(bucketId, coverImage);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

            View view = mInflator.inflate(R.layout.albumlayout, null, false);

            TextView albumName = (TextView) view.findViewById(R.id.album_name);
            albumName.setTypeface(mFont);
            TextView albumCount = (TextView) view.findViewById(R.id.number_txt);
            albumCount.setTypeface(mFont);
            return view;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
        case RESULT_OK:
            setResult(resultCode, data);
            finish();
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
            long id) {

        if (mCursor == null || mCursor.getCount() < position) {
            return;
        }

        Intent intent = new Intent(PhotoAlbumActivity.this,
                PhotoGallaryActivity.class);

        final String bucketId = mCursor.getString(mCursor
                .getColumnIndex(ImageColumns.BUCKET_ID));
        final String bucketName = mCursor.getString(mCursor
                .getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME));
        intent.putExtra(ID, bucketId);
        intent.putExtra(NAME, bucketName);
        startActivityForResult(intent, 1);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
        case PHOTO_ALBUM_LOADER_ID:
            final String[] PROJECTION_BUCKET = { ImageColumns.BUCKET_ID,
                    ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.DATE_TAKEN,
                    ImageColumns.DATA, "count(*) AS count", ImageColumns._ID };
            final String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
            final String BUCKET_ORDER_BY = "MAX(datetaken) DESC";
            return new CursorLoader(this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

        default:
            return null;
        }

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader,
            Cursor cursor) {
        mAlbumCursorAdapter.changeCursor(cursor);
        mCursor = cursor;

        if (cursor.getCount() <= 0) {
            mNoImageGallary.setVisibility(View.VISIBLE);
            findViewById(R.id.line_below_list_view).setVisibility(View.GONE);
        } else {
            mNoImageGallary.setVisibility(View.GONE);
            findViewById(R.id.line_below_list_view).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }
}