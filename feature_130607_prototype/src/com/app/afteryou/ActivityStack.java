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

package com.app.afteryou;

import java.util.ArrayList;

import android.app.Activity;

@SuppressWarnings("serial")
public class ActivityStack<T extends BaseActivity> extends ArrayList<BaseActivity> {
    /**
     * @param baseActivity
     * @return
     */
    public synchronized boolean push(T baseActivity) {
        return add(baseActivity);
    }

    public synchronized Activity getActivityByType(int viewType) {
        BaseActivity ret = null;
        for (BaseActivity activity: this) {
//            if (activity.getViewType() == viewType) {
//                ret = activity;
//                break;
//            }
        }

        return ret;
    }

    public synchronized void removeAllElements() {
        while (!isEmpty()) {
            get(0).finish();
        }
    }
}