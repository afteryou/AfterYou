package com.beacon.afterui.sliding;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.beacon.afterui.R;

public class SlidingActivityHelper {

	private Activity mActivity;

	private SlidingMenu mSlidingMenu;
	private View mViewAbove;
	private View mViewBehindLeft;
	private View mViewBehindRight;
	private boolean mBroadcasting = false;

	private boolean mOnPostCreateCalled = false;
	private boolean mEnableSlide = true;
	private boolean mUseCustomUp = false;

	public SlidingActivityHelper(Activity activity) {
		mActivity = activity;
	}

	public void onCreate(Bundle savedInstanceState) {
		mSlidingMenu = (SlidingMenu) LayoutInflater.from(mActivity).inflate(R.layout.slidingmenumain, null);
//		mActivity.getTheme().applyStyle(R.style.CustomUp, true);
	}

	public void onPostCreate(Bundle savedInstanceState) {
		if ((mViewBehindLeft == null && mViewBehindRight == null) || mViewAbove == null) {
			throw new IllegalStateException("Both setBehind[Left|Right]ContentView must be called " +
					"in onCreate in addition to setContentView.");
		}
		if (mViewBehindLeft == null)
			mSlidingMenu.setViewBehind(null, SlidingMenu.LEFT);
		if (mViewBehindRight == null)
			mSlidingMenu.setViewBehind(null, SlidingMenu.RIGHT);

		mOnPostCreateCalled = true;
		
		// get the window background
		TypedArray a = mActivity.getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowBackground});
		int background = a.getResourceId(0, 0);

		if (mEnableSlide) {
			// move everything into the SlidingMenu
			ViewGroup decor = (ViewGroup) mActivity.getWindow().getDecorView();
			ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
			// save ActionBar themes that have transparent assets
			decorChild.setBackgroundResource(background);
			decor.removeView(decorChild);
			mSlidingMenu.setContent(decorChild);
			decor.addView(mSlidingMenu);
		} else {
			// take the above view out of
			ViewGroup parent = (ViewGroup) mViewAbove.getParent();
			if (parent != null) {
				parent.removeView(mViewAbove);
			}
			// save people from having transparent backgrounds
			if (mViewAbove.getBackground() == null) {
				mViewAbove.setBackgroundResource(background);
			}
			mSlidingMenu.setContent(mViewAbove);
			parent.addView(mSlidingMenu, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	}

	public void setSlidingActionBarEnabled(boolean b) {
		if (mOnPostCreateCalled)
			throw new IllegalStateException("enableSlidingActionBar must be called in onCreate.");
		mEnableSlide = b;
	}

	public void useCustomUpIndicator() {
		if (mOnPostCreateCalled)
			throw new IllegalStateException("useCustomUpIndicator must be called in onCreate.");
		mUseCustomUp = true;
	}

	public View findViewById(int id) {
		View v;
		if (mSlidingMenu != null) {
			v = mSlidingMenu.findViewById(id);
			if (v != null)
				return v;
		}
		return null;
	}

	public void registerAboveContentView(View v, LayoutParams params) {
		if (!mBroadcasting)
			mViewAbove = v;
	}

	public void setContentView(View v) {
		mBroadcasting = true;
		mActivity.setContentView(v);
	}

	public void setBehindLeftContentView(View v) {
		mViewBehindLeft = v;
		mSlidingMenu.setViewBehind(mViewBehindLeft, SlidingMenu.LEFT);
	}
	
	public void setBehindRightContentView(View v) {
		mViewBehindRight = v;
		mSlidingMenu.setViewBehind(mViewBehindRight, SlidingMenu.RIGHT);
	}

	public SlidingMenu getSlidingMenu() {
		return mSlidingMenu;
	}

	public void toggle(int side) {
		if (mSlidingMenu.isBehindShowing()) {
			showAbove();
		} else {
			showBehind(side);
		}
	}

	public void showAbove() {
		mSlidingMenu.showAbove();
	}

	public void showBehind(int side) {
		mSlidingMenu.showBehind(side);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mSlidingMenu.isBehindShowing()) {
			showAbove();
			return true;
		}
		return false;
	}

}
