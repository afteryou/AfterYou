package com.navbuilder.app.hurricane.ui.staggered;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Support pulling up to load more.
 */
public class CustomGridView extends StaggeredGridView {

	private float mLastY = -1; 

	private IDataListener mDataListener;

	private boolean mEnablePullLoad = true;

	private int mCapacity = 50;

	public CustomGridView(Context context) {
		super(context);
		init(context);
	}

	public CustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
	}

	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
	}

	private void startLoadMore() {
		if (mDataListener != null) {
			mDataListener.onLoadMore();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(this.isEnabled() 
				&& getAdapter() != null) {
			if (mLastY == -1) {
				mLastY = ev.getRawY();
			}
			
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				if (getLastVisiblePosition() == getAdapter().getCount() - 1 
						&& deltaY < 0) {
					if(mEnablePullLoad 
							&& getAdapter().getCount() < mCapacity) {
						startLoadMore();
					}
				}
				break;
			default:
				mLastY = -1;
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	public void setCapacity(int capacity) {
		mCapacity = capacity;
	}
	
	public void setDataListener(IDataListener l) {
		mDataListener = l;
	}

	public interface IDataListener {
		public void onLoadMore();
	}

}
