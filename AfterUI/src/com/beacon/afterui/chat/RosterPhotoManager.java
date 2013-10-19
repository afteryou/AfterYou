package com.beacon.afterui.chat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class RosterPhotoManager {

    /** TAG */
    private static final String TAG = RosterPhotoManager.class.getSimpleName();

    private static RosterPhotoManager sRosterPhotoManager;

    private HashMap<String, Bitmap> mPhotoMap;

    private RosterPhotoManager() {
        mPhotoMap = new HashMap<String, Bitmap>();
    }

    public static RosterPhotoManager getPhotoManager() {
        synchronized (RosterPhotoManager.class) {
            if (sRosterPhotoManager == null) {
                sRosterPhotoManager = new RosterPhotoManager();
            }

            return sRosterPhotoManager;
        }
    }

    public Bitmap getPhoto(final Context context, final String key) {

        if (context == null) {
            Log.e(TAG, "getPhoto() : Context is NULL!");
            return null;
        }

        if (key == null) {
            Log.e(TAG, "getPhoto() : key is NULL!");
            return null;
        }

        if (mPhotoMap.containsKey(key)) {
            Log.d(TAG, "Photo for " + key + " returning from cache!");
            return mPhotoMap.get(key);
        }

        FileInputStream fis = null;
        try {
            fis = context.openFileInput(key);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            if (bitmap != null) {
                mPhotoMap.put(key, bitmap);
            }
            return bitmap;
        } catch (FileNotFoundException e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }
    
    public synchronized void resetPhotoFor( final String key ) {
        if (mPhotoMap.containsKey(key)) {
            mPhotoMap.remove(key);
        }
    }
}
