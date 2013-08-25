
package com.app.afteryou;

import android.content.Context;
import android.content.Intent;


public class UIView {
    private static final int STATE_IDLE    = 0;
    private static final int STATE_STARTED = 1;

    private int mState;

    protected int mViewType;
    protected Intent mIntent;

    public UIView (int viewType, Intent intent) {
        mViewType = viewType;
        mIntent = intent;

        mState = STATE_IDLE;
    }

    public int getViewType() {
        return mViewType;
    }

    public synchronized void show(Context context) {
        if (mState == STATE_IDLE && context != null) {
            context.startActivity(mIntent);
            mState = STATE_STARTED;            
        }
    }

    public synchronized void showAsRoot(Context context) {
        if (mState == STATE_IDLE && context != null) {
//            mIntent.putExtra(BaseActivity.VIEW_ROOT, true);
            context.startActivity(mIntent);
            mState = STATE_STARTED;
        }
    }

    @Override public String toString() {
        return UIView.class.toString() + "; viewType=" + mViewType;
    }
}
