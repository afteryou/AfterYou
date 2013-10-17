package com.beacon.afterui.sliding.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.sliding.SlidingActivity;
import com.beacon.afterui.sliding.customViews.ListViewAdapter;

/**
 * For showing left sliding menu behind main view.
 * 
 * @author spoddar
 * 
 */
public class ChatMenuFragment extends Fragment implements OnItemClickListener,
		OnItemLongClickListener, OnClickListener, OnLongClickListener {
	public static final String TAG = ChatMenuFragment.class.toString();
	private static final int ANIMATION_DURATION = 300;
	private static final float ANIMATION_X_TRANSLATION = 70.0f;
	private static final int TRANSLATION_X_RIGHT = 0x00000001;
	private static final int TRANSLATION_X_LEFT = 0x00000002;
	private static final int TRANSLATION_Y_TOP = 0x00000004;
	private static final int ANIMATION_DELETE = 0x00000008;

	private ListView mFavoriteMatchesList;
	private ListViewAdapter mListAdapter;
	private boolean mIsDeleteMode = false;
	private CloseSlidingMenuAdapter mCloseSlidingMenuAdapter = new CloseSlidingMenuAdapter();

	private static final String IMAGE = "icon";
	private static final String TEXT = "text";
	private Typeface typeFaceSemiBold;

	private Bitmap mUserThumbBitmap;
	private TextView mChatUserName;
	private ImageView mChatUserStatus;
	private ImageView mChatUserImg;
	private TextView mChatUserTime;
	private EditText mSearchEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Sachin - This string text should be taken from the Strings.xml
		// String[] chatUserList = getResources().getStringArray(
		// R.array.dash_board_txt);
		String[] chatUserList = { "Sushil Kadu", "Sarnab Poddar",
				"Pranav Dalal", "Ronak Patel", "Sachin Mane",
				"Sachin Tendulkar", "Rahul Dravid", "Saurav Gangully" };
		List<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();

		// font myriadPro semibold
		typeFaceSemiBold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/MyriadPro-Semibold.otf");
		// font myriadPro regular
		Typeface typeFaceRegular = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/MyriadPro-Regular.otf");

		View view = inflater.inflate(R.layout.chat_view, null);

		mFavoriteMatchesList = (ListView) view
				.findViewById(R.id.favorite_match_list);
		mSearchEditText = (EditText) view.findViewById(R.id.search_txt_right);
		TextView create_group_chat_txt = (TextView) view
				.findViewById(R.id.create_group_chat_txt);
		TextView edit_txt = (TextView) view.findViewById(R.id.edit_txt);
		TextView favorite_txt = (TextView) view.findViewById(R.id.favorite_txt);
		TextView group_chat_txt = (TextView) view
				.findViewById(R.id.group_chat_txt);

		group_chat_txt.setTypeface(typeFaceSemiBold);
		create_group_chat_txt.setTypeface(typeFaceSemiBold);
		favorite_txt.setTypeface(typeFaceSemiBold);
		edit_txt.setTypeface(typeFaceSemiBold);
		mSearchEditText.setTypeface(typeFaceRegular);

		View viewItem = inflater.inflate(R.layout.chat_view_item, null);

		mChatUserName = (TextView) viewItem.findViewById(R.id.chat_user_name);
		mChatUserStatus = (ImageView) viewItem.findViewById(R.id.user_status);
		mChatUserImg = (ImageView) viewItem.findViewById(R.id.user_img);
		mChatUserTime = (TextView) viewItem.findViewById(R.id.user_chat_time);

		mChatUserName.setTypeface(typeFaceSemiBold);
		mChatUserTime.setTypeface(typeFaceRegular);

		// ImageView dashImage = (ImageView)
		// view.findViewById(R.id.dashboard_img);

		String[] from = { TEXT };
		int[] to = { R.id.chat_user_name };
		mChatUserName.setTypeface(typeFaceSemiBold);
		Log.d(TAG, "Size Array : " + chatUserList.length);

		for (int i = 0; i < chatUserList.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			// map.put(IMAGE, String.valueOf(mImages[i]));
			map.put(TEXT, chatUserList[i]);
			mList.add(map);
			Log.d(TAG, "Size i : " + i);
		}
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), mList,
				R.layout.chat_view_item, from, to) {
			@Override
			public void setViewText(TextView v, String text) {
				v.setTypeface(typeFaceSemiBold);
				v.setText(text);
			}
		};
		mFavoriteMatchesList.setAdapter(adapter);
		return view;
	}

	private boolean isRuntimePostJellyBean() {
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		if (position >= mListAdapter.getCount()) {
			return;
		}

		List<View> views = mListAdapter.getAllViews();
		views.remove(position);
		animateListItems(TRANSLATION_X_LEFT, views);
	}

	private void setHasTransientState(View view, boolean b) {
		if (isRuntimePostJellyBean()) {
			view.setHasTransientState(b);
		} else {
			ViewCompat.setHasTransientState(view, b);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		mIsDeleteMode = !mIsDeleteMode;
		switchDeleteMode(mIsDeleteMode);
		return true;
	}

	private void switchDeleteMode(boolean delMode) {
		if (!delMode) {
			for (int i = 0; i < mListAdapter.getCount(); i++) {
				ViewGroup vg = (ViewGroup) mListAdapter.getItem(i).getView();
			}
		} else {
			for (int i = 0; i < mListAdapter.getCount(); i++) {
				ViewGroup vg = (ViewGroup) mListAdapter.getItem(i).getView();
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
	}

	@Override
	public boolean onLongClick(View v) {
		int id = v.getId();
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		((BaseActivity) this.getActivity()).showAbove();
	}

	private void animateListItems(int type, List<View> views) {
		animateListItems(type, views, mCloseSlidingMenuAdapter);
	}

	private void animateListItems(final int type, final List<View> views,
			final AnimatorListenerAdapter adapter) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				mFavoriteMatchesList.invalidateViews();
				final ViewTreeObserver observer = mFavoriteMatchesList
						.getViewTreeObserver();
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

	private void translationItem(int type, View target,
			final AnimatorListenerAdapter adapter) {
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
		// attachAllCategories();
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
			mFavoriteMatchesList.getGlobalVisibleRect(listRect);
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
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			switch (mTransType) {
			case TRANSLATION_X_RIGHT:
				mTarget.setTranslationX(ANIMATION_X_TRANSLATION
						* interpolatedTime);
				break;
			case TRANSLATION_X_LEFT:
				mTarget.setTranslationX(ANIMATION_X_TRANSLATION
						- ANIMATION_X_TRANSLATION * interpolatedTime);
				break;
			case ANIMATION_DELETE:
				mTarget.setTranslationX(-mTarget.getWidth() * interpolatedTime);
				break;
			case TRANSLATION_Y_TOP:
				mTarget.setTranslationY(mTarget.getHeight()
						- mTarget.getHeight() * interpolatedTime);
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

	@Override
	public void onDestroy() {
		if (mUserThumbBitmap != null) {
			mUserThumbBitmap.recycle();
			mUserThumbBitmap = null;
		}
		super.onDestroy();
	}
}
