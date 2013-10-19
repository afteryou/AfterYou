package com.beacon.afterui.chat;

import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class ChatUtils {

    /** TAG */
    private static final String TAG = ChatUtils.class.getSimpleName();

    public static boolean savePhoto(final Context context, final String user,
            final byte[] data) {

        FileOutputStream outputStream = null;
        
        try {
            outputStream = context.openFileOutput(user, Context.MODE_PRIVATE);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (IOException e) {
            Log.e(TAG, "Exception while storing data! : " + e.getMessage());
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
