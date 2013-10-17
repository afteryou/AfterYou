package com.beacon.afterui.chat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

/**
 * @author sushil
 * 
 */
public class ChatManager extends Service {

    /** TAG */
    private static final String TAG = ChatManager.class.getSimpleName();

    private HandlerThread mDeamonThread;

    private Handler mHandler;

    private final Binder mBinder = new ChatManagerImpl();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate:ChatManager");
        mDeamonThread = new HandlerThread("chat_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mDeamonThread.start();
        mHandler = new Handler(mDeamonThread.getLooper());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class ChatManagerImpl extends Binder {

        public ChatManager getService() {
            return ChatManager.this;
        }

    }

    @Override
    public void onDestroy() {

        if (mDeamonThread != null) {
            mDeamonThread.quit();
        }
        Log.d(TAG, "ChatManager service is getting destroyed!");
        super.onDestroy();
    }

}
