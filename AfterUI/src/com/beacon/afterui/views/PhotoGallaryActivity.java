package com.beacon.afterui.views;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aviary.android.feather.FeatherActivity;
import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.utils.ImageInfoUtils;
import com.beacon.afterui.views.gallery.ImageCache.ImageCacheParams;
import com.beacon.afterui.views.gallery.ImageFetcher;

public class PhotoGallaryActivity extends BaseActivity implements
        OnClickListener, android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private GridView mPhotoGridView;
    private String mId;
    private String mName;

    private TextView mCancelButton;
    private TextView mDoneBtn;

    private static final int PHOTO_LIST_LOADER_ID = 0;

    private PhotoGridAdapter mPhotoGridAdapter;

    private ImageFetcher mImageFetcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photo_gallary);
        setBehindLeftContentView(R.layout.photo_gallary);
        setBehindRightContentView(R.layout.photo_gallary);
        mId = getIntent().getStringExtra(PhotoAlbumActivity.ID);
        mName = getIntent().getStringExtra(PhotoAlbumActivity.NAME);

        // font myriadPro semibold
        Typeface itcAvaStdBk = Typeface.createFromAsset(getAssets(),
                "fonts/ITCAvantGardeStd-Bk.otf");

        mPhotoGridView = (GridView) findViewById(R.id.photo_gallary_layout);
        mPhotoGridView.setOnItemClickListener(mPhotoGridListener);

        mCancelButton = (TextView) findViewById(R.id.cancel_btn_photo_album);
        mCancelButton.setOnClickListener(this);
        mCancelButton.setTypeface(itcAvaStdBk);

        mDoneBtn = (TextView) findViewById(R.id.done_btn_photo_album);
        mDoneBtn.setTypeface(itcAvaStdBk);


        ImageCacheParams cacheParams = new com.beacon.afterui.views.gallery.ImageCache.ImageCacheParams();
        cacheParams.setMemCacheSizePercent(0.2f);
        com.beacon.afterui.views.gallery.ImageCache imageCache = new com.beacon.afterui.views.gallery.ImageCache(
                cacheParams);
        mImageFetcher = new ImageFetcher(this, 50, imageCache);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);

        mPhotoGridAdapter = new PhotoGridAdapter(this, null, true);
        mPhotoGridView.setAdapter(mPhotoGridAdapter);
        getLoaderManager().initLoader(PHOTO_LIST_LOADER_ID, null, this);
    }


    private OnItemClickListener mPhotoGridListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {

            Cursor cursor = mPhotoGridAdapter.getCursor();
            if (cursor == null || cursor.getCount() < position) {
                return;
            }

            final String bucketId = cursor.getString(cursor
                    .getColumnIndex(ImageColumns._ID));
            final String imagePath = ImageInfoUtils.getPhotoPath(
                    PhotoGallaryActivity.this, String.valueOf(bucketId));
            Intent newIntent = new Intent(PhotoGallaryActivity.this,
                    FeatherActivity.class);
            newIntent.setData(Uri.parse(imagePath));
            startActivityForResult(newIntent, 1);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
        case RESULT_OK:
            setResult(RESULT_OK, data);
            finish();
            break;
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

    private class PhotoGridAdapter extends CursorAdapter {

        public PhotoGridAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            final String bucketId = cursor.getString(cursor
                    .getColumnIndex(ImageColumns._ID));
            mImageFetcher.loadImage(bucketId, (ImageView) view);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            ImageView photoImage = new ImageView(PhotoGallaryActivity.this);
            photoImage.setScaleType(ImageView.ScaleType.FIT_XY);
            photoImage.setBackgroundResource(R.drawable.image_border);
            // photoImage.setPadding(1, 1, 1, 1);
            return photoImage;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
        case PHOTO_LIST_LOADER_ID:
            final String BUCKET_ORDER_BY = "datetaken DESC";
            final String WHERE = MediaStore.Images.Media.BUCKET_ID + "=?";
            final String WHERE_ARGS[] = { mId };

            final String[] PROJECTION = { MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            return new CursorLoader(this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION,
                    WHERE, WHERE_ARGS, BUCKET_ORDER_BY);

        default:
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mPhotoGridAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }
}
