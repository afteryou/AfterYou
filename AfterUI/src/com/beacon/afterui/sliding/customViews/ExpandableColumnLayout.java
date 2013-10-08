package com.beacon.afterui.sliding.customViews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class ExpandableColumnLayout extends LinearLayout implements OnClickListener {

	public static final int INVALID_NUMBER = -1;

	private Context mContext;
	private List<LinearLayout> mColumnLayouts;
	private LinearLayout mColumnContainer;
	private boolean isInitializing = true;

	private int mHorizontalMargin = 0;
	private int mVerticalMargin = 0;
	private ListViewAdapter mListViewAdapter;
	private OnItemClickListener mOnItemClickListener;

	private Animation mItemAnimation;

	private View mExpanderLayout;

	private int mItemShowNumber = INVALID_NUMBER;

	private boolean mIsCollapse = false;

	private View mExpandIndicator;

	private View mCollapseIndicator;

	private int mExpandHeight = 0;

	public ExpandableColumnLayout(Context context) {
		this(context, null);
	}

	public ExpandableColumnLayout(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.style.Widget_AbsListView);
	}

	public ExpandableColumnLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, android.R.style.Widget_AbsListView);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mContext = context;
		mColumnContainer = new LinearLayout(mContext);
		setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		mColumnContainer.setLayoutParams(params);
		mColumnContainer.setOrientation(LinearLayout.HORIZONTAL);

		addView(mColumnContainer);
		setColumnNum(1);
		isInitializing = false;
	}

	public void setColumnNum(int num) {
		if (num < 1) {
			throw new IllegalArgumentException("Column number must greater than 0");
		}
		mColumnContainer.removeAllViews();
		mColumnLayouts = Collections.synchronizedList(new ArrayList<LinearLayout>(num));
		for (int i = 0; i < num; i++) {
			LinearLayout columnLayout = new LinearLayout(mContext);
			columnLayout.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			p.weight = 1;
			p.leftMargin = i == 0 ? 0 : mHorizontalMargin;
			mColumnLayouts.add(columnLayout);
			columnLayout.setLayoutParams(p);
			mColumnContainer.addView(columnLayout, i);
		}
		if (mListViewAdapter != null) {
			for (ListItem item : mListViewAdapter.getAll()) {
				addView(item.getView(true));
			}
		}
		requestLayout();
		postInvalidate();
	}

	public void setExpanderLayout(int resId, final int expandIndicatorResId, final int collapseIndicatorResId) {
		setExpanderLayout(resId, expandIndicatorResId, collapseIndicatorResId, new OnClickListener() {
			public void onClick(View v) {
				if (mIsCollapse) {
					expand();
				} else {
					collapse();
				}
				resetExpanderIndicatorsState();
				requestLayout();
				invalidate();
			}
		});
	}

	private void setExpanderLayout(int resId, int expandIndicatorResId, int collapseIndicatorResId, OnClickListener l) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View expander = inflater.inflate(resId, null);
		mExpandIndicator = expander.findViewById(expandIndicatorResId);
		mCollapseIndicator = expander.findViewById(collapseIndicatorResId);
		mExpanderLayout = expander;
		mExpanderLayout.setOnClickListener(l);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		mExpanderLayout.setLayoutParams(params);
		mExpanderLayout.setVisibility(mIsCollapse ? View.VISIBLE : View.GONE);
		resetExpanderIndicatorsState();
		addViewToMainView(mExpanderLayout);
		setCollapseState(mItemShowNumber);
	}

	private void resetExpanderIndicatorsState() {
		mExpandIndicator.setVisibility(mIsCollapse ? View.VISIBLE : View.GONE);
		mCollapseIndicator.setVisibility(mIsCollapse ? View.GONE : View.VISIBLE);
	}

	private void collapse() {
		Animation animation = new ExpandAnimation(mExpandHeight, this.getCollapseHeight());
		animation.setDuration(400);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {

				for (int i = 0; i < mListViewAdapter.getCount(); i++) {
					if (i >= mItemShowNumber) {
						View v = mListViewAdapter.getItem(i).getView();
						v.setVisibility(View.GONE);
						v.clearAnimation();
					}
				}
			}

			public void onAnimationRepeat(Animation animation) {
			}
		});
		startAnimation(animation);
		mIsCollapse = true;
	}

	private void expand() {
		List<ListItem> items = mListViewAdapter.getAll();
		for (int i = 0; i < items.size(); i++) {
			View v = items.get(i).getView();
			v.setVisibility(View.VISIBLE);
		}
		requestLayout();
		invalidate();
		Animation animation = new ExpandAnimation(getBottom(), mExpandHeight);
		animation.setDuration(400);
		mColumnContainer.startAnimation(animation);
		mIsCollapse = false;
	}

	private int getCollapseHeight() {
		int result = 0;
		List<View> views = getAllViewsInClomns();
		for (int i = 0; i < mItemShowNumber; i++) {
			result += (views.get(i).getMeasuredHeight() + mVerticalMargin);
		}
		return result / (mColumnLayouts.size()) + mExpanderLayout.getMeasuredHeight();
	}

	public void setAdapter(ListViewAdapter adapter) {
		if (adapter == null) {
			return;
		}
		mListViewAdapter = adapter;
		clearAllItems();
		for (ListItem item : mListViewAdapter.getAll()) {
			View v = item.getView();
			v.setOnClickListener(this);
			addView(v);
		}
		requestLayout();
		postInvalidate();

		mListViewAdapter.registerDataSetObserver(new DataSetObserver() {
			public void onChanged() {
				List<View> views = getAllViewsInClomns();
				for (ListItem item : mListViewAdapter.getAll()) {
					if (!views.contains(item.getView())) {
						View v = item.getView();
						v.setOnClickListener(ExpandableColumnLayout.this);
						addView(v);
					}
				}
				List<View> adapterViews = getAllViewsInAdapter();
				for (View v : getAllViewsInClomns()) {
					if (!adapterViews.contains(v)) {
						for (LinearLayout l : mColumnLayouts) {
							l.removeView(v);
						}
					}
				}
				requestLayout();
				postInvalidate();
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		for (View child : getAllViewsInClomns()) {
			child.startAnimation(getItemAnimation());
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	public ListViewAdapter getAdapter() {
		return mListViewAdapter;
	}

	private List<View> getAllViewsInAdapter() {
		List<ListItem> items = mListViewAdapter.getAll();
		List<View> result = new ArrayList<View>(items.size());
		for (ListItem item : items) {
			result.add(item.getView());
		}
		return result;
	}

	private void clearAllItems() {
		for (LinearLayout l : mColumnLayouts) {
			l.removeAllViews();
		}
	}

	private int getPositionForView(View v) {
		List<View> allItems = getAllViewsInAdapter();
		for (int i = 0; i < allItems.size(); i++) {
			if (v.equals(allItems.get(i))) {
				return i;
			}
		}
		return INVALID_NUMBER;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}

	private void addViewToMainView(View v) {
		isInitializing = true;
		addView(mExpanderLayout);
		isInitializing = false;
	}

	@Override
	public void addView(View child) {
		addView(child, getChildCount());
	}

	@Override
	public void addView(View child, int index) {
		if (isInitializing)
			super.addView(child, index);
		else
			addViewToColumn(child);
	}

	@Override
	public void addView(View child, int width, int height) {
		if (isInitializing)
			super.addView(child, width, height);
		else
			addViewToColumn(child, width, height);
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		if (isInitializing)
			super.addView(child, params);
		else
			addViewToColumn(child, params);
	}

	@Override
	public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
		if (isInitializing)
			super.addView(child, index, params);
		else
			addViewToColumn(child, params);
	}

	private void addViewToColumn(View child) {
		addViewToColumn(child, null);
	}

	private void addViewToColumn(View child, int width, int height) {
		addViewToColumn(child, new LinearLayout.LayoutParams(width, height));
	}

	private void addViewToColumn(View child, android.view.ViewGroup.LayoutParams params) {
		addViewToColumn(child, getWorkingColumn(), params);
		// mWorkingColumn = (mWorkingColumn + 1) % mColumnLayouts.size();
	}

	private int getWorkingColumn() {
		return getAllViewsInClomns().size() % mColumnLayouts.size();
	}

	private void addViewToColumn(View child, int columnIndex, android.view.ViewGroup.LayoutParams params) {
		LinearLayout.LayoutParams newParams = null;
		if (params == null) {
			newParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		} else {
			newParams = new LinearLayout.LayoutParams(params);
		}
		LinearLayout column = mColumnLayouts.get(columnIndex);
		if (column.getChildCount() > 0) {
			newParams.topMargin = mVerticalMargin;
		}
		column.addView(child, newParams);
	}

	private Animation getItemAnimation() {
		if (mItemAnimation != null) {
			AnimationSet set = new AnimationSet(true);
			set.addAnimation(mItemAnimation);
			set.setDuration(getAnimationDuration(600, 90));
			return mItemAnimation;
		}
		return getDefaultItemAnimation();

	}

	private int getAnimationDuration(int min, int random) {
		return (int) (Math.random() * random) + min;
	}

	public void setItemAnimation(Animation animation) {
		mItemAnimation = animation;
	}

	private Animation getDefaultItemAnimation() {
		AnimationSet set = new AnimationSet(true);
		Animation animation;
		// Animation animation = new AlphaAnimation(0.0f, 1.0f);
		// animation.setDuration(300);
		// set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		animation.setDuration(getAnimationDuration(500, 90));
		set.addAnimation(animation);
		return set;
	}

	public void setCollapseState(int itemShowNumber) {
		if (itemShowNumber < 0 || itemShowNumber >= mListViewAdapter.getCount()) {
			return;
		}
		mItemShowNumber = itemShowNumber;
		getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			private boolean isFTT = true;

			public boolean onPreDraw() {
				if (isFTT) {
					isFTT = false;
					mExpandHeight = getMeasuredHeight();
					for (int i = 0; i < mListViewAdapter.getCount(); i++) {
						if (i >= mItemShowNumber) {
							View v = mListViewAdapter.getItem(i).getView();
							v.setVisibility(View.GONE);
							v.clearAnimation();
						}
					}
				}
				return true;
			}
		});

		if (mExpanderLayout != null) {
			mExpanderLayout.setVisibility(View.VISIBLE);
		}

		mIsCollapse = true;
	}

	public View getExpanderLayout() {
		return mExpanderLayout;
	}

	public int getChildCountInColumns() {
		int count = 0;
		for (ViewGroup column : mColumnLayouts) {
			count += column.getChildCount();
		}
		return count;
	}

	public View getChildAtColumns(int index) {
		if (index < 0 || index >= getChildCountInColumns()) {
			return null;
		}
		if (index == 0) {
			return mColumnLayouts.get(0).getChildAt(0);
		}
		int column = index % mColumnLayouts.size();
		int realIndex = index / mColumnLayouts.size();
		return mColumnLayouts.get(column).getChildAt(realIndex);
	}

	public View getChildAtColumn(int column, int index) {
		if (index < 0 || index >= getChildCountInColumns() || column < 0 || column >= mColumnLayouts.size()) {
			return null;
		}
		return mColumnLayouts.get(column).getChildAt(index);
	}

	public void setHorizontalMargin(int margin) {
		mHorizontalMargin = margin;
		for (int i = 0; i < mColumnLayouts.size(); i++) {
			LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) mColumnLayouts.get(i).getLayoutParams();
			p.setMargins(i == 0 ? 0 : mHorizontalMargin, 0, 0, 0);
		}
	}

	public void setVerticalMargin(int margin) {
		mVerticalMargin = margin;
	}

	public int getHorizontalMargin() {
		return mHorizontalMargin;
	}

	public int getVerticalMargin() {
		return mVerticalMargin;
	}

	public List<View> getAllViewsAtClomn(int clomnIndex) {
		List<View> result = new ArrayList<View>();
		ViewGroup vg = mColumnLayouts.get(clomnIndex);
		for (int i = 0; i < vg.getChildCount(); i++) {
			result.add(vg.getChildAt(i));
		}
		return result;
	}

	public List<View> getAllViewsInClomns() {
		List<View> result = new ArrayList<View>();
		for (int i = 0; i < mColumnLayouts.size(); i++) {
			result.addAll(getAllViewsAtClomn(i));
		}
		return result;
	}

	@Override
	public void onClick(View v) {
		if (mOnItemClickListener == null) {
			return;
		}
		int position = this.getPositionForView(v);
		if (position == INVALID_NUMBER) {
			return;
		}
		mOnItemClickListener.onItemClick(this, v, position, position);
	}

	/**
	 * Interface definition for a callback to be invoked when an item in this
	 * AdapterView has been clicked.
	 */
	public interface OnItemClickListener {

		/**
		 * Callback method to be invoked when an item in this AdapterView has
		 * been clicked.
		 * <p>
		 * Implementers can call getItemAtPosition(position) if they need to
		 * access the data associated with the selected item.
		 * 
		 * @param parent
		 *            The ColumnLayout where the click happened.
		 * @param view
		 *            The view within the AdapterView that was clicked (this
		 *            will be a view provided by the adapter)
		 * @param position
		 *            The position of the view in the adapter.
		 * @param id
		 *            The row id of the item that was clicked.
		 */
		void onItemClick(ExpandableColumnLayout parent, View view, int position, long id);
	}

	private class ExpandAnimation extends Animation {
		private final int mStartHeight;
		private final int mDeltaHeight;

		public ExpandAnimation(int startHeight, int endHeight) {
			mStartHeight = startHeight;
			mDeltaHeight = endHeight - startHeight;
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			android.view.ViewGroup.LayoutParams lp = getLayoutParams();
			lp.height = (int) (mStartHeight + mDeltaHeight * interpolatedTime);
			setLayoutParams(lp);
		}

		@Override
		public boolean willChangeBounds() {
			return true;
		}
	}

}
