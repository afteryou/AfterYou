
package com.beacon.afterui.activity;

import android.os.Handler;
import android.os.Looper;

public class MainLoopPosting {

    static MainLoopPosting mSelf;

    Handler mHandler;

    public static MainLoopPosting getInstance() {
        if (mSelf == null) {
            mSelf = new MainLoopPosting();
        }
        return mSelf;
    }

    protected MainLoopPosting() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void post(Runnable r) {
        mHandler.post(r);
    }

}
