package com.beacon.afterui.sliding.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.beacon.afterui.R;
import com.beacon.afterui.sliding.SlidingMenu;
import com.beacon.afterui.sliding.SlidingMenu.OnCloseListener;
import com.beacon.afterui.sliding.SlidingMenu.OnClosedListener;
import com.beacon.afterui.sliding.SlidingMenu.OnOpenListener;
import com.beacon.afterui.sliding.SlidingMenu.OnOpenedListener;

public class CustomViewAbove extends ViewGroup {

	private static final String TAG = "CustomViewAbove";
	private static final boolean DEBUG = false;

	private static final boolean USE_CACHE = false;

	private static final int MAX_SETTLE_DURATION = 600; // ms
	private static final int MIN_DISTANCE_FOR_FLING = 25; // dips

	private static final Interpolator sInterpolator = new Interpolator() {
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t * t * t + 1.0f;
		}
	};

	private FrameLayout mMenuLeft;
	private FrameLayout mMenuRight;
	protected FrameLayout mContent;

	private int mCurItem;
	private Scroller mScroller;

	private int mShadowWidth;
	private Drawable mShadowDrawableLeft;
	private Drawable mShadowDrawableRight;

	private boolean mScrollingCacheEnabled;

	private boolean mScrolling;

	private boolean mIsBeingDragged;
	//	private boolean mIsUnableToDrag;
	private int mTouchSlop;
	private float mInitialMotionX;
	/**
	 * Position of the last motion event.
	 */
	private float mLastMotionX;
	private float mLastMotionY;
	/**
	 * ID of the active pointer. This is used to retain consistency during
	 * drags/flings if multiple pointers are used.
	 */
	protected int mActivePointerId = INVALID_POINTER;
	/**
	 * Sentinel value for no current active pointer.
	 * Used by {@link #mActivePointerId}.
	 */
	private static final int INVALID_POINTER = -1;

	/**
	 * Determines speed during touch scrolling
	 */
	protected VelocityTracker mVelocityTracker;
	private int mMinimumVelocity;
	protected int mMaximumVelocity;
	private int mFlingDistance;

	private boolean mLastTouchAllowed = false;
	private final int mSlidingMenuThreshold = 16;
	private CustomViewBehind mViewBehindLeft;
	private CustomViewBehind mViewBehindRight;
	private boolean mEnabled = true;

	private OnPageChangeListener mOnPageChangeListener;
	private OnPageChangeListener mInternalPageChangeListener;

	private OnCloseListener mCloseListener;
	private OnOpenListener mOpenListener;
	private OnClosedListener mClosedListener;
	private OnOpenedListener mOpenedListener;

	//	private int mScrollState = SCROLL_STATE_IDLE;

	/**
	 * Callback interface for responding to changing state of the selected page.
	 */
	public interface OnPageChangeListener {

		/**
		 * This method will be invoked when the current page is scrolled, either as part
		 * of a programmatically initiated smooth scroll or a user initiated touch scroll.
		 *
		 * @param position Position index of the first page currently being displayed.
		 *                 Page position+1 will be visible if positionOffset is nonzero.
		 * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
		 * @param positionOffsetPixels Value in pixels indicating the offset from position.
		 */
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

		/**
		 * This method will be invoked when a new page becomes selected. Animation is not
		 * necessarily complete.
		 *
		 * @param position Position index of the new selected page.
		 */
		public void onPageSelected(int position);

	}

	/**
	 * Simple implementation of the {@link OnPageChangeListener} interface with stub
	 * implementations of each method. Extend this if you do not intend to override
	 * every method of {@link OnPageChangeListener}.
	 */
	public static class SimpleOnPageChangeListener implements OnPageChangeListener {

		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// This space for rent
		}


		public void onPageSelected(int position) {
			// This space for rent
		}


		public void onPageScrollStateChanged(int state) {
			// This space for rent
		}

	}

	public CustomViewAbove(Context context) {
		this(context, null);
	}

	public CustomViewAbove(Context context, AttributeSet attrs) {
		this(context, attrs, true);
	}

	public CustomViewAbove(Context context, AttributeSet attrs, boolean isAbove) {
		super(context, attrs);
		initCustomViewAbove(isAbove);
	}

	void initCustomViewAbove() {
		initCustomViewAbove(false);
	}

	void initCustomViewAbove(boolean isAbove) {
		setWillNotDraw(false);
		setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
		setFocusable(true);
		final Context context = getContext();
		mScroller = new Scroller(context, sInterpolator);
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
		setInternalPageChangeListener(new SimpleOnPageChangeListener() {

			public void onPageSelected(int position) {
				if (mViewBehindLeft != null) {
					switch (position) {
					case 0:
						mViewBehindLeft.setChildrenEnabled(true);
						break;
					case 1:
						mViewBehindLeft.setChildrenEnabled(false);
						break;
					}
				}
				if (mViewBehindRight != null) {
					switch (position) {
					case 1:
						mViewBehindRight.setChildrenEnabled(false);
						break;
					case 2:
						mViewBehindRight.setChildrenEnabled(true);
						break;
					}
				}
			}

		});

		final float density = context.getResources().getDisplayMetrics().density;
		mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);

		mMenuLeft = new FrameLayout(getContext());
		super.addView(mMenuLeft);
		mContent = new FrameLayout(getContext());
		super.addView(mContent);
		mMenuRight = new FrameLayout(getContext());
		super.addView(mMenuRight);

		if (isAbove) {
			View v = new LinearLayout(getContext());
			v.setBackgroundResource(android.R.color.transparent);
			setMenu(v);
		}

	}

	/**
	 * Set the currently selected page. If the CustomViewPager has already been through its first
	 * layout there will be a smooth animated transition between the current item and the
	 * specified item.
	 *
	 * @param item Item index to select
	 */
	public void setCurrentItem(int item) {
		setCurrentItemInternal(item, true, false);
	}

	/**
	 * Set the currently selected page.
	 *
	 * @param item Item index to select
	 * @param smoothScroll True to smoothly scroll to the new item, false to transition immediately
	 */
	public void setCurrentItem(int item, boolean smoothScroll) {
		setCurrentItemInternal(item, smoothScroll, false);
	}

	public int getCurrentItem() {
		return mCurItem;
	}

	void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
		setCurrentItemInternal(item, smoothScroll, always, 0);
	}

	void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
		if (!always && mCurItem == item && mMenuLeft != null && mContent != null) {
			setScrollingCacheEnabled(false);
			return;
		}

		int start = mViewBehindLeft == null ? 1 : 0;
		int end = mViewBehindRight == null ? 1 : 2;
		// TODO count accordingly
		if (item < start) {
			item = start;
		} else if (item > end) {
			item = end;
		}

		final boolean dispatchSelected = mCurItem != item;
		mCurItem = item;
		final int destX = this.getDestScrollX(mCurItem);

		if (dispatchSelected && mOnPageChangeListener != null) {
			mOnPageChangeListener.onPageSelected(item);
		}
		if (dispatchSelected && mInternalPageChangeListener != null) {
			mInternalPageChangeListener.onPageSelected(item);
		}
		if (smoothScroll) {
			smoothScrollTo(destX, 0, velocity);
		} else {
			completeScroll();
			scrollTo(destX, 0);
		}
	}

	/**
	 * Set a listener that will be invoked whenever the page changes or is incrementally
	 * scrolled. See {@link OnPageChangeListener}.
	 *
	 * @param listener Listener to set
	 */
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mOnPageChangeListener = listener;
	}

	public void setOnOpenListener(OnOpenListener l) {
		mOpenListener = l;
	}

	public void setOnCloseListener(OnCloseListener l) {
		mCloseListener = l;
	}

	public void setOnOpenedListener(OnOpenedListener l) {
		mOpenedListener = l;
	}

	public void setOnClosedListener(OnClosedListener l) {
		mClosedListener = l;
	}

	/**
	 * Set a separate OnPageChangeListener for internal use by the support library.
	 *
	 * @param listener Listener to set
	 * @return The old listener that was set, if any.
	 */
	OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
		OnPageChangeListener oldListener = mInternalPageChangeListener;
		mInternalPageChangeListener = listener;
		return oldListener;
	}

	/**
	 * Set the margin between pages.
	 *
	 * @param shadowWidth Distance between adjacent pages in pixels
	 * @see #getShadowWidth()
	 * @see #setShadowDrawable(Drawable)
	 * @see #setShadowDrawable(int)
	 */
	public void setShadowWidth(int shadowWidth) {
		mShadowWidth = shadowWidth;
		invalidate();
	}

	/**
	 * Return the margin between pages.
	 *
	 * @return The size of the margin in pixels
	 */
	public int getShadowWidth() {
		return mShadowWidth;
	}

	/**
	 * Set a drawable that will be used to fill the margin between pages.
	 *
	 * @param d Drawable to display between pages
	 */
	public void setShadowDrawable(Drawable d, int side) {
		if (side == SlidingMenu.LEFT) {
			mShadowDrawableLeft = d;
		} else if (side == SlidingMenu.RIGHT) {
			mShadowDrawableRight = d;
		} else if (side == SlidingMenu.BOTH) {
			mShadowDrawableLeft = d;
			mShadowDrawableRight = d;
		}
		if (d != null) refreshDrawableState();
		invalidate();
	}

	/**
	 * Set a drawable that will be used to fill the margin between pages.
	 *
	 * @param resId Resource ID of a drawable to display between pages
	 */
	public void setShadowDrawable(int resId, int side) {
		setShadowDrawable(getContext().getResources().getDrawable(resId), side);
	}


	@Override
	protected boolean verifyDrawable(Drawable who) {
		return super.verifyDrawable(who) || who == mShadowDrawableLeft || who == mShadowDrawableRight;
	}


	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		final Drawable d = mShadowDrawableLeft;
		if (d != null && d.isStateful()) {
			d.setState(getDrawableState());
		}
	}

	// We want the duration of the page snap animation to be influenced by the distance that
	// the screen has to travel, however, we don't want this duration to be effected in a
	// purely linear fashion. Instead, we use this method to moderate the effect that the distance
	// of travel has on the overall snap duration.
	float distanceInfluenceForSnapDuration(float f) {
		f -= 0.5f; // center the values about 0.
		f *= 0.3f * Math.PI / 2.0f;
		return (float) FloatMath.sin(f);
	}

	public int getDestScrollX(int i) {
		switch (i) {
		case 0:
			return 0;
		case 1:
			return mContent.getLeft();
		case 2:
			return mContent.getLeft() + mViewBehindRight.getWidth();
		}
		throw new RuntimeException("This should never happen!");
	}

	public int getRightScrollBound() {
		if (mViewBehindRight != null) {
			return  mViewBehindRight.getWidth();
		} else {
			return mContent.getLeft();
		}
	}

	public int getLeftScrollBound() {
		if (mViewBehindLeft != null) {
			return 0;
		} else {
			return mContent.getLeft();
		}
	}

	public int getChildLeft(int i) {
		if (i <= 0) return 0;
		return getChildWidth(i-1) + getChildLeft(i-1);
	}

	public int getChildRight(int i) {
		return getChildLeft(i) + getChildWidth(i);
	}

	public boolean isRightOpen() {
		if (mViewBehindRight == null)
			return false;

		return getCurrentItem() == 2;
	}

	public boolean isLeftOpen() {
		if (mViewBehindLeft == null) {
			return false;
		}

		return getCurrentItem() == 0;
	}

	public int getCustomWidth() {
		int i = 1;
		i = isLeftOpen()? 0 : i;
		i = isRightOpen()? 2 : i;
		return getChildWidth(i);
	}

	public int getChildWidth(int i) {
		try {
			switch (i) {
			case 0:
				return mViewBehindLeft.getWidth();
			case 1:
				return mContent.getWidth();
			case 2:
				return mViewBehindRight.getWidth();
			}
		} catch (NullPointerException e) { }
		return 0;
	}

	public int getBehindWidth() {
		if (mViewBehindLeft != null) {
			return mViewBehindLeft.getWidth();
		} else if (mViewBehindRight != null) {
			return mViewBehindRight.getWidth();
		} else {
			return 0;
		}
	}

	public boolean isSlidingEnabled() {
		return mEnabled;
	}

	public void setSlidingEnabled(boolean b) {
		mEnabled = b;
	}

	/**
	 * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
	 *
	 * @param x the number of pixels to scroll by on the X axis
	 * @param y the number of pixels to scroll by on the Y axis
	 */
	void smoothScrollTo(int x, int y) {
		smoothScrollTo(x, y, 0);
	}

	/**
	 * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
	 *
	 * @param x the number of pixels to scroll by on the X axis
	 * @param y the number of pixels to scroll by on the Y axis
	 * @param velocity the velocity associated with a fling, if applicable. (0 otherwise)
	 */
	void smoothScrollTo(int x, int y, int velocity) {
		if (getChildCount() == 0) {
			// Nothing to do.
			setScrollingCacheEnabled(false);
			return;
		}
		int sx = getScrollX();
		int sy = getScrollY();
		int dx = x - sx;
		int dy = y - sy;
		if (dx == 0 && dy == 0) {
			completeScroll();
			if (isLeftOpen()) {
				if (mOpenedListener != null)
					mOpenedListener.onOpened(SlidingMenu.LEFT);
			} else if (isRightOpen()) {
				if (mOpenedListener != null)
					mOpenedListener.onOpened(SlidingMenu.RIGHT);
			} else {
				
				if (mClosedListener != null)
					mClosedListener.onClosed();
			}
			return;
		}

		setScrollingCacheEnabled(true);
		mScrolling = true;

		final int width = getCustomWidth();
		final int halfWidth = width / 2;
		final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
		final float distance = halfWidth + halfWidth *
				distanceInfluenceForSnapDuration(distanceRatio);

		int duration = 0;
		velocity = Math.abs(velocity);
		if (velocity > 0) {
			duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
		} else {
			final float pageDelta = (float) Math.abs(dx) / (width + mShadowWidth);
			duration = (int) ((pageDelta + 1) * 100);
			duration = MAX_SETTLE_DURATION;
		}
		duration = Math.min(duration, MAX_SETTLE_DURATION);

		mScroller.startScroll(sx, sy, dx, dy, duration);
		invalidate();
	}

	protected void setMenu(View v) {
		if (mMenuLeft.getChildCount() > 0) {
			mMenuLeft.removeAllViews();
		}
		mMenuLeft.addView(v);
	}

	public void setContent(View v) {
		if (mContent.getChildCount() > 0) {
			mContent.removeAllViews();
		}
		if (v != null)
			mContent.addView(v);
	}

	public void setViewBehindLeft(CustomViewBehind cvb) {
		mViewBehindLeft = cvb;
	}

	public void setViewBehindRight(CustomViewBehind cvb) {
		mViewBehindRight = cvb;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		setMeasuredDimension(width, height);

		final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
		final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
		mContent.measure(contentWidth, contentHeight);

		final int menuWidth = getChildMeasureSpec(widthMeasureSpec, 0, getBehindWidth());
		mMenuLeft.measure(menuWidth, contentHeight);
		mMenuRight.measure(menuWidth, contentHeight);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Make sure scroll position is set correctly.
		if (w != oldw) {
			// [ChrisJ] - This fixes the onConfiguration change for orientation issue..
			// maybe worth having a look why the recomputeScroll pos is screwing
			// up?
			completeScroll();
			scrollTo(getChildLeft(mCurItem), getScrollY());
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;

		int contentLeft = getChildLeft(1);
		mMenuLeft.layout(0, 0, width, height);
		mContent.layout(contentLeft, 0, contentLeft + width, height);
		mMenuRight.layout(contentLeft + width, 0, contentLeft + 2*width, height);
		this.setCurrentItemInternal(mCurItem, false, true);
	}


	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				if (DEBUG) Log.i(TAG, "computeScroll: still scrolling");
				int oldX = getScrollX();
				int oldY = getScrollY();
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();

				if (oldX != x || oldY != y) {
					scrollTo(x, y);
					pageScrolled(x);
				}

				// Keep on drawing until the animation has finished.
				invalidate();
				return;
			}
		}

		// Done with scroll, clean up state.
		completeScroll();
	}

	private void pageScrolled(int xpos) {
		final int widthWithMargin = getChildWidth(mCurItem) + mShadowWidth;
		final int position = xpos / widthWithMargin;
		final int offsetPixels = xpos % widthWithMargin;
		final float offset = (float) offsetPixels / widthWithMargin;

		onPageScrolled(position, offset, offsetPixels);
	}

	/**
	 * This method will be invoked when the current page is scrolled, either as part
	 * of a programmatically initiated smooth scroll or a user initiated touch scroll.
	 * If you override this method you must call through to the superclass implementation
	 * (e.g. super.onPageScrolled(position, offset, offsetPixels)) before onPageScrolled
	 * returns.
	 *
	 * @param position Position index of the first page currently being displayed.
	 *                 Page position+1 will be visible if positionOffset is nonzero.
	 * @param offset Value from [0, 1) indicating the offset from the page at position.
	 * @param offsetPixels Value in pixels indicating the offset from position.
	 */
	protected void onPageScrolled(int position, float offset, int offsetPixels) {
		if (mOnPageChangeListener != null) {
			mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
		}
		if (mInternalPageChangeListener != null) {
			mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
		}
	}

	private void completeScroll() {
		boolean needPopulate = mScrolling;
		if (needPopulate) {
			// Done with scroll, no longer want to cache view drawing.
			setScrollingCacheEnabled(false);
			mScroller.abortAnimation();
			int oldX = getScrollX();
			int oldY = getScrollY();
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			if (oldX != x || oldY != y) {
				scrollTo(x, y);
			}
			if (isLeftOpen()) {
				if (mOpenedListener != null)
					mOpenedListener.onOpened(SlidingMenu.LEFT);
			} else if (isRightOpen()) {
				if (mOpenedListener != null)
					mOpenedListener.onOpened(SlidingMenu.RIGHT);
			} else {
				if (mClosedListener != null)
					mClosedListener.onClosed();
			}
		}
		mScrolling = false;
	}

	protected int mTouchMode = SlidingMenu.TOUCHMODE_MARGIN;
	private int mTouchModeBehind = SlidingMenu.TOUCHMODE_MARGIN;

	public void setTouchMode(int i) {
		mTouchMode = i;
	}

	public int getTouchMode() {
		return mTouchMode;
	}

	public void setTouchModeBehind(int i) {
		mTouchModeBehind = i;
	}

	public int getTouchModeBehind() {
		return mTouchModeBehind;
	}

	private boolean thisTouchAllowed(MotionEvent ev) {
		int x = (int) (ev.getX() + mScrollX);
		if (isLeftOpen()) {
			switch (mTouchModeBehind) {
			case SlidingMenu.TOUCHMODE_FULLSCREEN:
				return true;
			case SlidingMenu.TOUCHMODE_MARGIN:
				return x >= getBehindWidth() && x <= getWidth();
			default:
				return false;
			}
		} else if (isRightOpen()) {
			switch (mTouchModeBehind) {
			case SlidingMenu.TOUCHMODE_FULLSCREEN:
				return true;
			case SlidingMenu.TOUCHMODE_MARGIN:
				return x <= mContent.getRight();
			default:
				return false;
			}
		} else {
			switch (mTouchMode) {
			case SlidingMenu.TOUCHMODE_FULLSCREEN:
				return true;
			case SlidingMenu.TOUCHMODE_MARGIN:
				int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
						mSlidingMenuThreshold, getResources().getDisplayMetrics());
				int left = mContent.getLeft();
				int right = mContent.getRight();
				boolean bool = false;
				if (mViewBehindLeft != null)
					bool |= (x >= left && x <= pixels + left);
				if (mViewBehindRight != null)
					bool |= (x <= right && x >= right - pixels);
				return bool;
			default:
				return false;
			}
		}
	}
	
	private boolean thisSlideAllowed(float dx) {
		boolean allowed = false;
		if (isLeftOpen()) {
			allowed = dx < 0;
		} else if (isRightOpen()) {
			allowed = dx > 0;
		} else {
			if (mViewBehindLeft != null)
				allowed |= dx > 0;
			if (mViewBehindRight != null)
				allowed |= dx < 0;
		}
		Log.v(TAG, "this slide allowed " + allowed);
		return allowed;
	}

	private boolean mIsUnableToDrag;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (!mEnabled) {
			return false;
		}

		final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;

		if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			mIsBeingDragged = false;
			mIsUnableToDrag = false;
			mActivePointerId = INVALID_POINTER;
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			return false;
		}

		if (action != MotionEvent.ACTION_DOWN) {
			if (mIsBeingDragged)
				return true;
			else if (mIsUnableToDrag)
				return false;
		}

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int activePointerId = mActivePointerId;
			if (activePointerId == INVALID_POINTER)
				break;

			final int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
			final float x = MotionEventCompat.getX(ev, pointerIndex);
			final float dx = x - mLastMotionX;
			final float xDiff = Math.abs(dx);
			final float y = MotionEventCompat.getY(ev, pointerIndex);
			final float yDiff = Math.abs(y - mLastMotionY);
			if (xDiff > mTouchSlop && xDiff > yDiff && thisSlideAllowed(dx)) {
				if (DEBUG) Log.v(TAG, "Starting drag! from onInterceptTouch");
				mIsBeingDragged = true;
				mLastMotionX = x;
				setScrollingCacheEnabled(true);
			} else if (yDiff > mTouchSlop) {
				mIsUnableToDrag = true;
			}
			break;

		case MotionEvent.ACTION_DOWN:
			mActivePointerId = ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK;
			mLastMotionX = mInitialMotionX = MotionEventCompat.getX(ev, mActivePointerId);
			mLastMotionY = MotionEventCompat.getY(ev, mActivePointerId);
			if (thisTouchAllowed(ev)) {
				mIsBeingDragged = false;
				mIsUnableToDrag = false;
				if ((isLeftOpen() || isRightOpen()) && mTouchModeBehind == SlidingMenu.TOUCHMODE_MARGIN)
					return true;
			} else {
				mIsUnableToDrag = true;
			}
			break;
		case MotionEventCompat.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			break;
		}

		if (!mIsBeingDragged) {
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}
			mVelocityTracker.addMovement(ev);
		}

		return mIsBeingDragged;
	}


	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!mEnabled) {
			return false;
		}

		if (!mIsBeingDragged && !mLastTouchAllowed && !thisTouchAllowed(ev)) {
			return false;
		}

		final int action = ev.getAction();

		if (action == MotionEvent.ACTION_UP ||
				action == MotionEvent.ACTION_CANCEL ||
				action == MotionEvent.ACTION_OUTSIDE) {
			mLastTouchAllowed = false;
		} else {
			mLastTouchAllowed = true;
		}

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		switch (action & MotionEventCompat.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
			completeScroll();

			// Remember where the motion event started
			mLastMotionX = mInitialMotionX = ev.getX();
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			break;
		case MotionEvent.ACTION_MOVE:
			if (!mIsBeingDragged) {
				final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
				final float x = MotionEventCompat.getX(ev, pointerIndex);
				final float xDiff = Math.abs(x - mLastMotionX);
				final float y = MotionEventCompat.getY(ev, pointerIndex);
				final float yDiff = Math.abs(y - mLastMotionY);
				if (DEBUG) Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);
				if (xDiff > mTouchSlop && xDiff > yDiff) {
					if (DEBUG) Log.v(TAG, "Starting drag! from onTouch");
					mIsBeingDragged = true;
					mLastMotionX = x;
					setScrollingCacheEnabled(true);
				}
			}
			if (mIsBeingDragged) {
				// Scroll to follow the motion event
				final int activePointerIndex = MotionEventCompat.findPointerIndex(
						ev, mActivePointerId);
				final float x = MotionEventCompat.getX(ev, activePointerIndex);
				final float deltaX = mLastMotionX - x;
				mLastMotionX = x;
				float oldScrollX = getScrollX();
				float scrollX = oldScrollX + deltaX;
				final float leftBound = getLeftScrollBound();
				final float rightBound = getRightScrollBound();
				if (scrollX < leftBound) {
					scrollX = leftBound;
				} else if (scrollX > rightBound) {
					scrollX = rightBound;
				}
				// Don't lose the rounded component
				mLastMotionX += scrollX - (int) scrollX;
				scrollTo((int) scrollX, getScrollY());
				pageScrolled((int) scrollX);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mIsBeingDragged) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(
						velocityTracker, mActivePointerId);
				final int widthWithMargin = getChildWidth(mCurItem);
				final int scrollX = getScrollX();
				final float pageOffset = (float) (scrollX % widthWithMargin) / widthWithMargin;
				final int activePointerIndex =
						MotionEventCompat.findPointerIndex(ev, mActivePointerId);
				final float x = MotionEventCompat.getX(ev, activePointerIndex);
				final int totalDelta = (int) (x - mInitialMotionX);
				int nextPage = determineTargetPage(mCurItem, pageOffset, initialVelocity,
						totalDelta);
				setCurrentItemInternal(nextPage, true, true, initialVelocity);

				mActivePointerId = INVALID_POINTER;
				endDrag();
			} else if (isLeftOpen()) {
				// close the menu
				setCurrentItem(1);
			} else if (isRightOpen()) {
				setCurrentItem(1);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			if (mIsBeingDragged) {
				setCurrentItemInternal(mCurItem, true, true);
				mActivePointerId = INVALID_POINTER;
				endDrag();
			}
			break;
		case MotionEventCompat.ACTION_POINTER_DOWN: {
			final int index = MotionEventCompat.getActionIndex(ev);
			final float x = MotionEventCompat.getX(ev, index);
			mLastMotionX = x;
			mActivePointerId = MotionEventCompat.getPointerId(ev, index);
			break;
		}
		case MotionEventCompat.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			mLastMotionX = MotionEventCompat.getX(ev,
					MotionEventCompat.findPointerIndex(ev, mActivePointerId));
			break;
		}
		if (mActivePointerId == INVALID_POINTER)
			mLastTouchAllowed = false;
		return true;
	}

	private float mScrollScaleLeft;
	private float mScrollScaleRight;

	public float getScrollScale(int side) {
		if (side == SlidingMenu.LEFT) {
			return mScrollScaleLeft;
		} else {
			return mScrollScaleRight;
		}
	}

	public void setScrollScale(float f, int side) {
		if (f < 0 && f > 1)
			throw new IllegalArgumentException("ScrollScale must be between 0 and 1");
		if (side == SlidingMenu.LEFT) {
			mScrollScaleLeft = f;
		} else if (side == SlidingMenu.RIGHT) {
			mScrollScaleRight = f;
		} else if (side == SlidingMenu.BOTH) {
			mScrollScaleLeft = f;
			mScrollScaleRight = f;
		}
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		mScrollX = x;
		// adjust visibility so we don't have any layout issues
		if (mViewBehindLeft != null) {
			if (x < getBehindWidth()) {
				if (mViewBehindLeft.getVisibility() != View.VISIBLE) {
					mViewBehindLeft.setVisibility(View.VISIBLE);
				}
			} else if (mViewBehindLeft.getVisibility() != View.INVISIBLE){
				mViewBehindLeft.setVisibility(View.INVISIBLE);
			}
		}
		if (mViewBehindRight != null) {
			if (x > mContent.getLeft()) {
				if (mViewBehindRight.getVisibility() != View.VISIBLE) {
					mViewBehindRight.setVisibility(View.VISIBLE);
				}
			} else if (mViewBehindRight.getVisibility() != View.INVISIBLE){
				mViewBehindRight.setVisibility(View.INVISIBLE);
			}
		}
		// scroll the behind views
		if (mViewBehindLeft != null && mEnabled) {
			mViewBehindLeft.scrollTo((int)(x*mScrollScaleLeft), y);
		}
		if (mViewBehindRight != null && mEnabled) {
			int x2 = (int) ((x - mContent.getLeft() - mViewBehindRight.getWidth()) * mScrollScaleRight);
			Log.v(TAG, "scrolling right to " + x2);
			mViewBehindRight.scrollTo(x2, y);
		}

		if (mShadowDrawableLeft != null || mSelectorDrawable != null)
			invalidate();
	}

	private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
		int targetPage;
		if (Math.abs(deltaX) > (float)mFlingDistance && Math.abs(velocity) > mMinimumVelocity) {
			targetPage = velocity > 0 ? currentPage - 1 : currentPage + 1;
		} else {
			targetPage = (int) ((deltaX > 0) ? 
					(currentPage + pageOffset - 0.5f) : (currentPage - pageOffset + 0.5f));
			if ((deltaX > 0 && targetPage > currentPage) || (deltaX < 0 && targetPage < currentPage))
				targetPage = currentPage;
		}
		return targetPage;
	}

	protected float getPercentOpenLeft() {
		return (getBehindWidth() - mScrollX) / mViewBehindLeft.getWidth();		
	}

	protected float getPercentOpenRight() {
		return (getWidth() - (mContent.getRight() - mScrollX)) / mViewBehindRight.getWidth();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		final int behindWidth = getBehindWidth();
		// Draw the margin drawable if needed
		if (mShadowWidth > 0) {
			if (mShadowDrawableLeft != null) {
				final int left = behindWidth - mShadowWidth;
				mShadowDrawableLeft.setBounds(left, 0, left + mShadowWidth, getHeight());
				mShadowDrawableLeft.draw(canvas);
			}
			if (mShadowDrawableRight != null) {
				mShadowDrawableRight.setBounds(mContent.getRight(), 0, mContent.getRight() + mShadowWidth, getHeight());
				mShadowDrawableRight.draw(canvas);
			}
		}

		if (mFadeEnabled)
			onDrawBehindFade(canvas);

		if (mSelectorEnabled)
			onDrawMenuSelector(canvas);
	}

	// variables for drawing
	private float mScrollX = 0.0f;
	// for the fade
	private boolean mFadeEnabled;
	private float mFadeDegree = 1.0f;
	private final Paint mBehindFadePaint = new Paint();
	// for the indicator
	private boolean mSelectorEnabled = true;
	private Bitmap mSelectorDrawable;
	private View mSelectedView;

	private void onDrawBehindFade(Canvas canvas) {
		int alpha;
		if (mViewBehindLeft != null) {
			alpha = (int) (mFadeDegree * 255 * Math.abs(1-getPercentOpenLeft()));
			if (alpha > 0) {
				mBehindFadePaint.setColor(Color.argb(alpha, 0, 0, 0));
				canvas.drawRect(0, 0, getBehindWidth(), getHeight(), mBehindFadePaint);
			}
		}
		if (mViewBehindRight != null) {
			alpha = (int) (mFadeDegree * 255 * Math.abs(1-getPercentOpenRight()));
			if (alpha > 0) {
				mBehindFadePaint.setColor(Color.argb(alpha, 0, 0, 0));
				canvas.drawRect(mContent.getRight(), 0, mContent.getRight() + mViewBehindRight.getWidth(),
						getHeight(), mBehindFadePaint);
			}
		}
	}

	private void onDrawMenuSelector(Canvas canvas) {
		if (mSelectorDrawable != null && mSelectedView != null) {
			String tag = (String) mSelectedView.getTag(R.id.selected_view);
			if (tag.equals(TAG+"SelectedView")) {
				int right = getChildLeft(1);
				int left = (int) (right - mSelectorDrawable.getWidth() * getPercentOpenLeft());

				canvas.save();
				canvas.clipRect(left, 0, right, getHeight());
				canvas.drawBitmap(mSelectorDrawable, left, getSelectedTop(), null);
				canvas.restore();
			}
		}
	}

	public void setBehindFadeEnabled(boolean b) {
		mFadeEnabled = b;
	}

	public void setBehindFadeDegree(float f) {
		if (f > 1.0f || f < 0.0f)
			throw new IllegalStateException("The BehindFadeDegree must be between 0.0f and 1.0f");
		mFadeDegree = f;
	}

	public void setSelectorEnabled(boolean b) {
		mSelectorEnabled = b;
	}

	public void setSelectedView(View v) {
		if (mSelectedView != null) {
			mSelectedView.setTag(R.id.selected_view, null);
			mSelectedView = null;
		}
		if (v.getParent() != null) {
			mSelectedView = v;
			mSelectedView.setTag(R.id.selected_view, TAG+"SelectedView");
			invalidate();
		}
	}

	private int getSelectedTop() {
		int y = mSelectedView.getTop();
		y += (mSelectedView.getHeight() - mSelectorDrawable.getHeight()) / 2;
		return y;
	}

	public void setSelectorDrawable(Bitmap b) {
		mSelectorDrawable = b;
		refreshDrawableState();
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		if (DEBUG) Log.v(TAG, "onSecondaryPointerUp called");
		final int pointerIndex = MotionEventCompat.getActionIndex(ev);
		final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
		if (pointerId == mActivePointerId) {
			// This was our active pointer going up. Choose a new
			// active pointer and adjust accordingly.
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
			mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
			if (mVelocityTracker != null) {
				mVelocityTracker.clear();
			}
		}
	}

	private void endDrag() {
		mIsBeingDragged = false;
		mIsUnableToDrag = false;
		mLastTouchAllowed = false;

		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	private void setScrollingCacheEnabled(boolean enabled) {
		if (mScrollingCacheEnabled != enabled) {
			mScrollingCacheEnabled = enabled;
			if (USE_CACHE) {
				final int size = getChildCount();
				for (int i = 0; i < size; ++i) {
					final View child = getChildAt(i);
					if (child.getVisibility() != GONE) {
						child.setDrawingCacheEnabled(enabled);
					}
				}
			}
		}
	}

	/**
	 * Tests scrollability within child views of v given a delta of dx.
	 *
	 * @param v View to test for horizontal scrollability
	 * @param checkV Whether the view v passed should itself be checked for scrollability (true),
	 *               or just its children (false).
	 * @param dx Delta scrolled in pixels
	 * @param x X coordinate of the active touch point
	 * @param y Y coordinate of the active touch point
	 * @return true if child views of v can be scrolled by delta of dx.
	 */
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ViewGroup) {
			final ViewGroup group = (ViewGroup) v;
			final int scrollX = v.getScrollX();
			final int scrollY = v.getScrollY();
			final int count = group.getChildCount();
			// Count backwards - let topmost views consume scroll distance first.
			for (int i = count - 1; i >= 0; i--) {
				final View child = group.getChildAt(i);
				if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() &&
						y + scrollY >= child.getTop() && y + scrollY < child.getBottom() &&
						canScroll(child, true, dx, x + scrollX - child.getLeft(),
								y + scrollY - child.getTop())) {
					return true;
				}
			}
		}

		return checkV && ViewCompat.canScrollHorizontally(v, -dx);
	}


	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// Let the focused view and/or our descendants get the key first
		return super.dispatchKeyEvent(event) || executeKeyEvent(event);
	}

	/**
	 * You can call this function yourself to have the scroll view perform
	 * scrolling from a key event, just as if the event had been dispatched to
	 * it by the view hierarchy.
	 *
	 * @param event The key event to execute.
	 * @return Return true if the event was handled, else false.
	 */
	public boolean executeKeyEvent(KeyEvent event) {
		boolean handled = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				handled = arrowScroll(FOCUS_LEFT);
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				handled = arrowScroll(FOCUS_RIGHT);
				break;
			case KeyEvent.KEYCODE_TAB:
				if (Build.VERSION.SDK_INT >= 11) {
					// The focus finder had a bug handling FOCUS_FORWARD and FOCUS_BACKWARD
					// before Android 3.0. Ignore the tab key on those devices.
					if (KeyEventCompat.hasNoModifiers(event)) {
						handled = arrowScroll(FOCUS_FORWARD);
					} else if (KeyEventCompat.hasModifiers(event, KeyEvent.META_SHIFT_ON)) {
						handled = arrowScroll(FOCUS_BACKWARD);
					}
				}
				break;
			}
		}
		return handled;
	}

	public boolean arrowScroll(int direction) {
		View currentFocused = findFocus();
		if (currentFocused == this) currentFocused = null;

		boolean handled = false;

		View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused,
				direction);
		if (nextFocused != null && nextFocused != currentFocused) {
			if (direction == View.FOCUS_LEFT) {
				// If there is nothing to the left, or this is causing us to
				// jump to the right, then what we really want to do is page left.
				if (currentFocused != null && nextFocused.getLeft() >= currentFocused.getLeft()) {
					handled = pageLeft();
				} else {
					handled = nextFocused.requestFocus();
				}
			} else if (direction == View.FOCUS_RIGHT) {
				// If there is nothing to the right, or this is causing us to
				// jump to the left, then what we really want to do is page right.
				if (currentFocused != null && nextFocused.getLeft() <= currentFocused.getLeft()) {
					handled = pageRight();
				} else {
					handled = nextFocused.requestFocus();
				}
			}
		} else if (direction == FOCUS_LEFT || direction == FOCUS_BACKWARD) {
			// Trying to move left and nothing there; try to page.
			handled = pageLeft();
		} else if (direction == FOCUS_RIGHT || direction == FOCUS_FORWARD) {
			// Trying to move right and nothing there; try to page.
			handled = pageRight();
		}
		if (handled) {
			playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
		}
		return handled;
	}

	boolean pageLeft() {
		if (mCurItem > 0) {
			setCurrentItem(mCurItem-1, true);
			return true;
		}
		return false;
	}

	boolean pageRight() {
		if (mCurItem < 1) {
			setCurrentItem(mCurItem+1, true);
			return true;
		}
		return false;
	}

}
