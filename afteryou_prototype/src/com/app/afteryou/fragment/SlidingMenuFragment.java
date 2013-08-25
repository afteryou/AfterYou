package com.app.afteryou.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.app.afteryou.BaseActivity;
import com.app.afteryou.activity.AddCategoryActivity;
import com.app.afteryou.controller.CategoryController;
import com.app.afteryou.customview.ListItem;
import com.app.afteryou.customview.ListViewAdapter;
import com.app.afteryou.model.Category;
import com.app.afteryou.slidingmenu.SlidingActivity;
import com.app.afteryou.R;

/**
 * For showing sliding menu behind main view.
 * 
 * @author qwang
 * 
 */
public class SlidingMenuFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener, OnClickListener, OnLongClickListener {
	public static final String TAG = SlidingMenuFragment.class.toString();
	private static final int ANIMATION_DURATION = 300;
	private static final float ANIMATION_X_TRANSLATION = 70.0f;
	private static final int TRANSLATION_X_RIGHT = 0x00000001;
	private static final int TRANSLATION_X_LEFT = 0x00000002;
	private static final int TRANSLATION_Y_TOP = 0x00000004;
	private static final int ANIMATION_DELETE = 0x00000008;

	private ListView mListView;
	private boolean mAllCategoriesAttached = false;
	private ViewGroup mRootView;
	private Set<Category> mAttachedCategories = new HashSet<Category>();
	private ListViewAdapter mListAdapter;
	private boolean mIsDeleteMode = false;
	private CloseSlidingMenuAdapter mCloseSlidingMenuAdapter = new CloseSlidingMenuAdapter();
	private CategoryController mCategoryController;
	private View mListFooterView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCategoryController = CategoryController.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = (ViewGroup) inflater.inflate(R.layout.sliding_menu, null);
		initListView();
		restoreUserAttached();
		View btnAll = mRootView.findViewById(R.id.btn_all_category);
		btnAll.setOnClickListener(this);
		btnAll.setOnLongClickListener(this);
		mRootView.findViewById(R.id.btn_add_cagegory).setOnClickListener(this);
		return mRootView;
	}

	private void initListView() {
		mListView = (ListView) mRootView.findViewById(R.id.sliding_menu_list);
		mListAdapter = new ListViewAdapter(getActivity(), R.layout.sliding_menu_item, R.id.slid_menu_item_text,
				mCategoryController.getUserCategoriesFromPref());
		mListFooterView = getActivity().getLayoutInflater().inflate(R.layout.sliding_menu_add_categories_item, null);
		mListView.addFooterView(mListFooterView);
		mListView.setAdapter(mListAdapter);
		mListView.setDivider(null);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		mListView.setSelector(R.color.sliding_menu_bg);
	}

	private boolean isRuntimePostJellyBean() {
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
		if (position >= mListAdapter.getCount()) {
			return;
		}
		final Category item = (Category) mListAdapter.getItem(position);
		if (mIsDeleteMode) {
			showDeleteDialog(position, item);
			return;
		}

		boolean alreadyAttached = mAttachedCategories.contains(item);

		if (mAllCategoriesAttached) {
			/*
			 * When all is selected and the user selects an interest, the
			 * unselected interests should disconnect
			 */
			List<View> views = mListAdapter.getAllViews();
			views.remove(position);
			animateListItems(TRANSLATION_X_LEFT, views);
			mAttachedCategories.clear();
			mAttachedCategories.add((Category) mListAdapter.getItem(position));
			mAllCategoriesAttached = false;
			if (mOnMenuClickListener != null) {
				mOnMenuClickListener.onMenuClick(item);
			}
		} else if (alreadyAttached) {
			// mAttachedCategories.remove(item);
			// animateListItem(TRANSLATION_X_LEFT, view);
			// mAllCategoriesAttached = false;
			return;
		} else {
			Iterator<Category> iterator = mAttachedCategories.iterator();
			animateListItem(TRANSLATION_X_RIGHT, view);
			while (iterator.hasNext()) {
				animateListItem(TRANSLATION_X_LEFT, iterator.next().getView());
			}
			mAttachedCategories.clear();
			mAttachedCategories.add(item);

			if (mOnMenuClickListener != null) {
				mOnMenuClickListener.onMenuClick(item);
			}
		}
		checkIfAllCategoriesAttached();
		saveCategoriesToPreference();
	}

	private void showDeleteDialog(final int position, final Category item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Do you want to delete the selected category (" + item.getText() + ")?");
		builder.setTitle("Delete");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				deleteListItem(position, item);
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void deleteListItem(final int position, final Category item) {
		final View view = mListAdapter.getItem(position).getView();
		view.setPivotX(0.0f);
		view.setPivotY(0.0f);
		if (isRuntimePostJellyBean()) {
			mListView.invalidateViews();
			setHasTransientState(view, true);
			AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
				public void onAnimationEnd(Animator animation) {
					deleteCategory(position, item);
				}
			};
			view.animate().setDuration(ANIMATION_DURATION).setListener(listener).translationX(-view.getWidth());
		} else {
			ListItemAnimation anim = new ListItemAnimation(ANIMATION_DELETE, view);
			anim.setAnimationListener(new AnimationAdapter() {
				public void onAnimationEnd(Animation animation) {
					deleteCategory(position, item);
				}
			});
			anim.start();
		}
	}

	private void setHasTransientState(View view, boolean b) {
		if (isRuntimePostJellyBean()) {
			view.setHasTransientState(b);
		} else {
			ViewCompat.setHasTransientState(view, b);
		}
	}

	private void deleteCategory(int position, Category item) {
		mAttachedCategories.remove(item);
		mListAdapter.remove(position);
		mListView.invalidateViews();

		setHasTransientState(item.getView(), false);
		saveCategoriesToPreference();
		checkIfAllCategoriesAttached();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		mIsDeleteMode = !mIsDeleteMode;
		switchDeleteMode(mIsDeleteMode);
		return true;
	}

	private void switchDeleteMode(boolean delMode) {
		if (!delMode) {
			for (int i = 0; i < mListAdapter.getCount(); i++) {
				ViewGroup vg = (ViewGroup) mListAdapter.getItem(i).getView();
				vg.findViewById(R.id.btn_delete_slid_menu_item).setVisibility(View.GONE);
			}
		} else {
			for (int i = 0; i < mListAdapter.getCount(); i++) {
				ViewGroup vg = (ViewGroup) mListAdapter.getItem(i).getView();
				vg.findViewById(R.id.btn_delete_slid_menu_item).setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_all_category:
			if (mAllCategoriesAttached) {
				// detachAllListItem();
			} else {
				attachAllCategories();
			}
			break;
		case R.id.btn_add_cagegory:
			startAddCategoryActivity();
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_all_category:
			mIsDeleteMode = !mIsDeleteMode;
			switchDeleteMode(mIsDeleteMode);
			return true;
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null || !data.hasExtra(AddCategoryActivity.SELECTED_CATEGORY)) {
			return;
		}
		String newCategoryTitle = data.getStringExtra(AddCategoryActivity.SELECTED_CATEGORY).trim();
		if (isCategoryExists(newCategoryTitle)) {
			return;
		}
		Category c = new Category(getActivity().getApplicationContext());
		c.setName(newCategoryTitle);
		c.setCode(data.getStringExtra(AddCategoryActivity.CATEGORY_CODE));
		c.setType(data.getIntExtra(AddCategoryActivity.CATEGORY_TYPE, -1));
		c.setIcon("");
		mListAdapter.add(c);

		saveCategoriesToPreference();
		if (mAllCategoriesAttached) {
			mAllCategoriesAttached = false;
			animateButtonAll();
		}
		if (mIsDeleteMode) {
			switchDeleteMode(false);
		}
		mListAdapter.getItem(mListAdapter.getCount() - 1).getView().setTranslationX(ANIMATION_X_TRANSLATION);
		Iterator<Category> iterator = mAttachedCategories.iterator();
		while (iterator.hasNext()) {
			iterator.next().getView().setTranslationX(0.0f);
		}
		mAttachedCategories.clear();
		mAttachedCategories.add(c);
		if (mOnMenuClickListener != null) {
			mOnMenuClickListener.onMenuClick(c);
		}
		((BaseActivity) this.getActivity()).showAbove();
	}

	private void restoreUserAttached() {
		List<Category> attached = mCategoryController.getUserCategoriesFromPref();
		for (int i = 0; i < mListAdapter.getCount(); i++) {
			Category category = (Category) mListAdapter.getItem(i);
			for (Category c : attached) {
				if (c.getName().trim().equals(category.getName())) {
					mAttachedCategories.add(category);
					View view = mListAdapter.getView(i, null, null);
					view.setTranslationX(ANIMATION_X_TRANSLATION);
				}
			}
		}
		mAllCategoriesAttached = true;
		animateButtonAll();
		checkIfAllCategoriesAttached();
		mListView.invalidateViews();
	}

	private void checkIfAllCategoriesAttached() {
		if (mAttachedCategories.size() == mListAdapter.getCount()) {
			mAllCategoriesAttached = true;
		} else {
			mAllCategoriesAttached = false;
		}
		animateButtonAll();
	}

	private void saveCategoriesToPreference() {
		mCategoryController.saveUserCategoriesToPref(mListAdapter.getAll());
		mCategoryController.saveUserAttachedCategoriesToPref(new ArrayList<Category>(mAttachedCategories));
	}

	private void attachAllCategories() {
		mAllCategoriesAttached = true;
		List<View> views = new ArrayList<View>();
		for (int i = 0; i < mListAdapter.getCount(); i++) {
			if (!mAttachedCategories.contains(mListAdapter.getItem(i))) {
				views.add(mListAdapter.getView(i, null, null));
			}
			mAttachedCategories.add((Category) mListAdapter.getItem(i));
		}
		animateListItems(TRANSLATION_X_RIGHT, views, mCloseSlidingMenuAdapter);
		animateButtonAll();
		saveCategoriesToPreference();
		searchingAllCallback();
	}

	private void detachAllListItem() {
		mAllCategoriesAttached = false;
		List<View> views = new ArrayList<View>();
		for (int i = 0; i < mListAdapter.getCount(); i++) {
			views.add(mListAdapter.getView(i, null, null));
		}
		mAttachedCategories.clear();
		animateListItems(TRANSLATION_X_LEFT, views);
		saveCategoriesToPreference();
		animateButtonAll();
	}

	private void animateListItems(int type, List<View> views) {
		animateListItems(type, views, mCloseSlidingMenuAdapter);
	}

	private void animateListItems(final int type, final List<View> views, final AnimatorListenerAdapter adapter) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				mListView.invalidateViews();
				final ViewTreeObserver observer = mListView.getViewTreeObserver();
				for (View v : views) {
					setHasTransientState(v, true);
				}
				observer.addOnPreDrawListener(new OnPreDrawListener() {

					public boolean onPreDraw() {
						observer.removeOnPreDrawListener(this);
						for (View view : views) {
							translationItem(type, view, adapter);
							setHasTransientState(view, false);
						}
						return true;
					}
				});
			}
		};
		getActivity().runOnUiThread(runnable);
	}

	private void animateListItem(int type, View item) {
		List<View> views = new ArrayList<View>();
		views.add(item);
		animateListItems(type, views);
	}

	private void translationItem(int type, View target, final AnimatorListenerAdapter adapter) {
		target.setPivotX(0.0f);
		target.setPivotY(0.0f);
		if (isRuntimePostJellyBean()) {
			ViewPropertyAnimator animator = target.animate();
			animator.setDuration(ANIMATION_DURATION);
			animator.setListener(adapter);
			switch (type) {
			case TRANSLATION_X_RIGHT:
				animator.translationX(ANIMATION_X_TRANSLATION);
				break;
			case TRANSLATION_Y_TOP:
				break;
			case TRANSLATION_X_LEFT:
				animator.translationX(0.0f);
				break;
			}
		} else {
			ListItemAnimation anim = new ListItemAnimation(type, target);
			anim.setAnimationListener(new AnimationAdapter() {
				public void onAnimationEnd(Animation animation) {
					adapter.onAnimationEnd(null);
				}
			});
			anim.start();
		}
	}

	private void animateButtonAll() {
		final View allBtn = mRootView.findViewById(R.id.btn_all_category);
		allBtn.setPivotX(0.0f);
		allBtn.setPivotY(0.0f);
		setHasTransientState(allBtn, true);
		ViewPropertyAnimator anim = allBtn.animate();
		anim.setDuration(ANIMATION_DURATION);
		if (mAllCategoriesAttached) {
			anim.scaleX(1.20f);
		} else {
			anim.scaleX(1.0f);
		}
		setHasTransientState(allBtn, false);
	}

	private void searchingAllCallback() {
		List<Category> list = new ArrayList<Category>();
		for (ListItem item : mListAdapter.getAll()) {
			if (item instanceof Category) {
				list.add((Category) item);
			}
		}
		if (mOnMenuClickListener != null) {
			mOnMenuClickListener.onMutilMenuClick(list);
		}
	}

	private void startAddCategoryActivity() {
		Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
		getActivity().startActivityForResult(intent, AddCategoryActivity.class.hashCode());
	}

	private boolean isCategoryExists(String category) {
		for (int i = 0; i < mListAdapter.getCount(); i++) {
			String c = mListAdapter.getItem(i).getText();
			if (c.trim().equals(category)) {
				return true;
			}
		}
		return false;
	}

	private class CloseSlidingMenuAdapter extends AnimatorListenerAdapter {
		public void onAnimationEnd(Animator animation) {
			Activity activity = getActivity();
			if (activity != null) {
				mIsDeleteMode = false;
				switchDeleteMode(false);
				((SlidingActivity) activity).getSlidingMenu().showAbove();
			}
		}
	}

	public boolean onBack() {
		if (mAllCategoriesAttached) {
			return false;
		}
		attachAllCategories();
		this.getActivity().getActionBar().setTitle("All My Interests");
		return true;
	}

	private class ListItemAnimation extends Animation {
		private int mTransType;
		private View mTarget;

		public ListItemAnimation(int type, View v) {
			mTransType = type;
			mTarget = v;
			Rect targetRect = new Rect();
			v.getGlobalVisibleRect(targetRect);
			Rect listRect = new Rect();
			mListView.getGlobalVisibleRect(listRect);
			if (!listRect.contains(targetRect)) {
				end();
			} else {
				v.setAnimation(this);
			}
			setDuration(ANIMATION_DURATION);
		}

		private void end() {
			switch (mTransType) {
			case TRANSLATION_X_RIGHT:
				mTarget.setTranslationX(ANIMATION_X_TRANSLATION);
				break;
			case TRANSLATION_X_LEFT:
				mTarget.setTranslationX(0.0f);
				break;
			case ANIMATION_DELETE:
				mTarget.setTranslationX(-mTarget.getWidth());
				break;
			case TRANSLATION_Y_TOP:
				mTarget.setTranslationY(0.0f);
			}
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			switch (mTransType) {
			case TRANSLATION_X_RIGHT:
				mTarget.setTranslationX(ANIMATION_X_TRANSLATION * interpolatedTime);
				break;
			case TRANSLATION_X_LEFT:
				mTarget.setTranslationX(ANIMATION_X_TRANSLATION - ANIMATION_X_TRANSLATION * interpolatedTime);
				break;
			case ANIMATION_DELETE:
				mTarget.setTranslationX(-mTarget.getWidth() * interpolatedTime);
				break;
			case TRANSLATION_Y_TOP:
				mTarget.setTranslationY(mTarget.getHeight() - mTarget.getHeight() * interpolatedTime);
			}
		}

		@Override
		public void start() {
			super.start();
			mTarget.requestFocusFromTouch();
		}

		@Override
		public boolean willChangeBounds() {
			return true;
		}
	}

	private class AnimationAdapter implements AnimationListener {
		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	private OnMenuClickListener mOnMenuClickListener;

	public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
		mOnMenuClickListener = onMenuClickListener;
	}

	public static interface OnMenuClickListener {
		public void onMenuClick(Category category);

		public void onMutilMenuClick(List<Category> categories);
	}
}
