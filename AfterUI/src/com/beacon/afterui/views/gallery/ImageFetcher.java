package com.beacon.afterui.views.gallery;

import java.lang.ref.WeakReference;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.widget.ImageView;

import com.beacon.afterui.BuildConfig;
import com.beacon.afterui.utils.ImageInfoUtils;

public class ImageFetcher {

    protected int mImageWidth;
    protected int mImageHeight;
    private static final int FADE_IN_TIME = 200;
    private static final String TAG = ImageFetcher.class.getName();
    private final Object mPauseWorkLock = new Object();
    protected boolean mPauseWork = false;

    protected Resources mResources;
    private Bitmap mLoadingBitmap;

    private boolean mExitTasksEarly = false;

    private Context mContext;
    private ImageCache mImageCache;

    public ImageFetcher(Context context, int imageSize, ImageCache img) {
        mContext = context;
        mResources = mContext.getResources();
        setImageSize(imageSize);
        mImageCache = img;
    }

    /**
     * Set the target image width and height.
     * 
     * @param width
     * @param height
     */
    public void setImageSize(int width, int height) {
        mImageWidth = width;
        mImageHeight = height;
    }

    /**
     * Set the target image size (width and height will be the same).
     * 
     * @param size
     */
    public void setImageSize(int size) {
        setImageSize(size, size);
    }

    public void setLoadingImage(int resId) {
        mLoadingBitmap = BitmapFactory.decodeResource(mResources, resId);
    }

    public void loadImage(String filePath, ImageView imageView) {

        if (filePath == null) {
            return;
        }

        BitmapDrawable value = null;

        if (mImageCache != null) {
            value = mImageCache.getBitmapFromMemCache(filePath);
        }

        if (value != null) {
            // Bitmap found in memory cache
            imageView.setImageDrawable(value);
        } else if (cancelPotentialWork(filePath, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources,
                    mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);

            // NOTE: This uses a custom version of AsyncTask that has been
            // pulled from the
            // framework and slightly modified. Refer to the docs at the top of
            // the class
            // for more info on what was changed.
            task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, filePath);
        }
    }

    /**
     * The actual AsyncTask that will asynchronously process the image.
     */
    private class BitmapWorkerTask extends
            AsyncTask<String, Void, BitmapDrawable> {
        private final String TAG = BitmapWorkerTask.class.getName();
        private String filePath;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        /**
         * Background processing.
         */
        @Override
        protected BitmapDrawable doInBackground(String... params) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "doInBackground - starting work");
            }

            filePath = params[0];

            Bitmap bitmap = null;
            BitmapDrawable drawable = null;

            // If the bitmap was not found in the cache and this task has not
            // been cancelled by
            // another thread and the ImageView that was originally bound to
            // this task is still
            // bound back to this task and our "exit early" flag is not set,
            // then call the main
            // process method (as implemented by a subclass)
            if (bitmap == null && !isCancelled()
                    && getAttachedImageView() != null && !mExitTasksEarly) {
                // bitmap = processBitmap(params[0]);
                bitmap = processBitmapFromGallery(params[0]);
            }

            // If the bitmap was processed and the image cache is available,
            // then add the processed
            // bitmap to the cache for future use. Note we don't check if the
            // task was cancelled
            // here, if it was, and the thread is still running, we may as well
            // add the processed
            // bitmap to our cache as it might be used again in the future
            if (bitmap != null) {
                if (Utils.hasHoneycomb()) {
                    // Running on Honeycomb or newer, so wrap in a standard
                    // BitmapDrawable
                    drawable = new BitmapDrawable(mResources, bitmap);
                } else {
                    // Running on Gingerbread or older, so wrap in a
                    // RecyclingBitmapDrawable
                    // which will recycle automagically
                    drawable = new RecyclingBitmapDrawable(mResources, bitmap);
                }

                if (mImageCache != null) {
                    mImageCache.addBitmapToCache(filePath, drawable);
                }

            }

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "doInBackground - finished work");
            }

            return drawable;
        }

        /**
         * Once the image is processed, associates it to the imageView
         */
        @Override
        protected void onPostExecute(BitmapDrawable value) {
            // if cancel was called on this task or the "exit early" flag is set
            // then we're done
            if (isCancelled() || mExitTasksEarly) {
                value = null;
            }

            final ImageView imageView = getAttachedImageView();
            if (value != null && imageView != null) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "onPostExecute - setting bitmap");
                }
                setImageDrawable(imageView, value);
            }
        }

        @Override
        protected void onCancelled(BitmapDrawable value) {
            super.onCancelled(value);
            synchronized (mPauseWorkLock) {
                mPauseWorkLock.notifyAll();
            }
        }

        /**
         * Returns the ImageView associated with this task as long as the
         * ImageView's task still points to this task as well. Returns null
         * otherwise.
         */
        private ImageView getAttachedImageView() {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }

            return null;
        }
    }

    /**
     * A custom Drawable that will be attached to the imageView while the work
     * is in progress. Contains a reference to the actual worker task, so that
     * it can be stopped if a new binding is required, and makes sure that only
     * the last started worker process can bind its result, independently of the
     * finish order.
     */
    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
                    bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     * Called when the processing is complete and the final drawable should be
     * set on the ImageView.
     * 
     * @param imageView
     * @param drawable
     */
    private void setImageDrawable(ImageView imageView, Drawable drawable) {
        // if (mFadeInBitmap) {
        // Transition drawable with a transparent drawable and the final
        // drawable
        final TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                new ColorDrawable(android.R.color.transparent), drawable });
        // Set background to loading bitmap
        imageView.setBackgroundDrawable(new BitmapDrawable(mResources,
                mLoadingBitmap));

        imageView.setImageDrawable(td);
        td.startTransition(FADE_IN_TIME);
        // } else {
        // imageView.setImageDrawable(drawable);
        // }
    }

    /**
     * @param imageView
     *            Any imageView
     * @return Retrieve the currently active work task (if any) associated with
     *         this imageView. null if there is no such task.
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public Bitmap processBitmapFromGallery(String bucketId) {

        Bitmap tempBitmap = Images.Thumbnails.getThumbnail(
                mContext.getContentResolver(), Long.valueOf(bucketId),
                MediaStore.Images.Thumbnails.MICRO_KIND, null);

        Bitmap bitmap = ImageInfoUtils.roundCornered(tempBitmap, 4);

        if (tempBitmap != null && !tempBitmap.isRecycled()) {
            tempBitmap.recycle();
        }

        return bitmap;
    }

    public Bitmap processBitmap(String imagePath) {

        long id = 0;

        String[] projection = new String[] { MediaStore.Images.ImageColumns._ID };
        String selection = MediaStore.Images.ImageColumns.DATA + "=?";

        String[] selectionArgs = new String[] { imagePath };
        Log.d("test", imagePath);

        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = null;
        cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                id = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns._ID));
            } else {
                Log.d(TAG, "Cursor is empty");
            }
        } else {
            Log.d(TAG, "Cursor is NULL  !");
        }

        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(resolver, id,
                MediaStore.Images.Thumbnails.MINI_KIND,
                (BitmapFactory.Options) null);
        cursor.close();

        return bitmap;
    }

    /**
     * Returns true if the current work has been canceled or if there was no
     * work in progress on this image view. Returns false if the work in
     * progress deals with the same data. The work is not stopped in that case.
     */
    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final Object bitmapData = bitmapWorkerTask.filePath;
            if (bitmapData == null || !bitmapData.equals(data)) {
                bitmapWorkerTask.cancel(true);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "cancelPotentialWork - cancelled work for "
                            + data);
                }
            } else {
                // The same work is already in progress.
                return false;
            }
        }
        return true;
    }
}
