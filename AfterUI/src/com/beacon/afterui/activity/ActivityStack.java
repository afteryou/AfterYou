
package com.beacon.afterui.activity;

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
        }

        return ret;
    }

    public synchronized void removeAllElements() {
        while (!isEmpty()) {
            get(0).finish();
        }
    }
}