package com.beacon.afterui.sliding.customViews;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

/**
 * Support pulling up to load more.
 */
public class CustomGridView extends GridView {

	private float mLastY = -1, mx, my , curX, curY; 
	boolean click = false;

	private IDataListener mDataListener;

	private boolean mEnablePullLoad = true;

	private int mCapacity = 50;
	
	/**
	 * Rectangle used for hit testing children
	 */
	private Rect mTouchFrame;

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
				click = true;
                mx = ev.getX();
                my = ev.getY();                     
				break;
			case MotionEvent.ACTION_MOVE:
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				curX = ev.getX();
				curY = ev.getY();
				if(click && (mx-curX <5) && (my-curY <5))
				{
					click = true;
				}
				else{
					click = false;
				}
				if (getLastVisiblePosition() == getAdapter().getCount() - 1 
						&& deltaY < 0) {
					if(mEnablePullLoad 
							&& getAdapter().getCount() < mCapacity) {
						startLoadMore();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if(click)
				{
//					int position = pointToPosition(mx, my);
//					if (position>=0 && position<=getCount()) {
//						final View view = getChildAt(position);
//						// If there is no view, something bad happened (the view
//						// scrolled off the
//						// screen, etc.) and we should cancel the click
//						if (view != null) {
//							performItemClick(view, position,
//									getItemIdAtPosition(position));
//						}
//                    }
				}
				break;
			default:
				mLastY = -1;
				break;
			}
		}
		return super.onTouchEvent(ev);
	}
	
	public int pointToPosition(float x, float y) {
		Rect frame = mTouchFrame;
		if (frame == null) {
			mTouchFrame = new Rect();
			frame = mTouchFrame;
		}

		final int count = getChildCount();
		for (int i = count - 1; i >= 0; i--) {
			final View child = getChildAt(i);
			if (child.getVisibility() == View.VISIBLE) {
				child.getHitRect(frame);
				if (frame.contains((int)x, (int)y)) {
					return i;
				}
			}
		}
		return INVALID_POSITION;
	}
	
	@Override
	public View getChildAt(int position)
	{
		final int count = getChildCount();
		if(position > count-1)
		{
			position = getLastVisiblePosition() - position;
			
		}
		return super.getChildAt(position);
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
