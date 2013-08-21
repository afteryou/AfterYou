/*
 * (C) Copyright 2013 by TeleCommunication Systems, Inc.
 *
 * The information contained herein is confidential, proprietary
 * to TeleCommunication Systems, Inc., and considered a trade secret
 * as defined in section 499C of the penal code of the State of
 * California. Use of this information by anyone other than
 * authorized employees of TeleCommunication Systems is granted only
 * under a written non-disclosure agreement, expressly prescribing
 * the scope and manner of such use.
 *
 */

package com.navbuilder.app.hurricane;

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
