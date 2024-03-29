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
import android.os.Handler;
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
import com.beacon.afterui.application.AfterYouApplication;
import com.beacon.afterui.imageUtils.ImageCache;
import com.beacon.afterui.log.AfterUIlog;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.sliding.SlidingActivity;
import com.beacon.afterui.sliding.SlidingMenu;
import com.beacon.afterui.sliding.customViews.ListViewAdapter;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.views.CapturePictureActivity;
import com.beacon.afterui.views.MainActivity;
import com.beacon.afterui.views.PickFriendsActivity;

/**
 * For showing left sliding menu behind main view.
 * 
 * @author spoddar
 * 
 */
public class SlidingMenuFragment extends Fragment implements
		OnItemClickListener, OnItemLongClickListener, OnClickListener,
		OnLongClickListener {
	public static final String TAG = SlidingMenuFragment.class.toString();
	private static final int ANIMATION_DURATION = 300;
	private static final float ANIMATION_X_TRANSLATION = 70.0f;
	private static final int TRANSLATION_X_RIGHT = 0x00000001;
	private static final int TRANSLATION_X_LEFT = 0x00000002;
	private static final int TRANSLATION_Y_TOP = 0x00000004;
	private static final int ANIMATION_DELETE = 0x00000008;

	private ListView mListView;
	private ListViewAdapter mListAdapter;
	private boolean mIsDeleteMode = false;
	private CloseSlidingMenuAdapter mCloseSlidingMenuAdapter = new CloseSlidingMenuAdapter();

	private static final String IMAGE = "icon";
	private static final String TEXT = "text";
	private Typeface ITCAvantGardeStdBkFont;

	private Bitmap mUserThumbBitmap;

	private static final int SETTING = 0;
	private static final int IMPORT_FREIND = 1;
	private static final int HELP_CENTER = 3;
	private static final int TERMS_POLICY = 2;
	private static final int LOGOUT = 4;
	private String[] mDashBoardTxt;
	private String[] mHeadinItemTxt;
	private String[] mSubHeadinItemTxt;
	private int[] mImages;
	private static String mHeadingTxt;
	private String FROM_IMPORT_SIDEBAR = "from_import";
	private int mCloseSideBar = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mDashBoardTxt = getResources().getStringArray(R.array.dash_board_txt);
		int[] mImages = { R.drawable.setting_img, R.drawable.import_friends,
				R.drawable.terms_policy, R.drawable.help_center,
				R.drawable.log_out_img, 0 };
		List<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
		// mRootView = (ViewGroup) inflater.inflate(R.layout.sliding_menu,
		// null);

		// font myriadPro ITCAvantGardeStdBkFont
		ITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		View view = inflater.inflate(R.layout.sliding_menu, null);
		View viewText = inflater.inflate(R.layout.sliding_menu_item, null);
		mListView = (ListView) view.findViewById(R.id.sliding_menu_list);
		TextView dashText = (TextView) viewText
				.findViewById(R.id.dashboard_txt);
		EditText searchEditText = (EditText) view.findViewById(R.id.search_txt);
		searchEditText.setTypeface(ITCAvantGardeStdBkFont);

		TextView userNameTxt = (TextView) view.findViewById(R.id.user_name);
		userNameTxt.setTypeface(ITCAvantGardeStdBkFont);
		String name = PreferenceEngine.getInstance(getActivity())
				.getFirstName()
				+ " "
				+ PreferenceEngine.getInstance(getActivity()).getLastName();
		userNameTxt.setText(name);

		ImageView userImage = (ImageView) view.findViewById(R.id.user_image);
		ImageCache mProfileThumb = new ImageCache(getActivity(),
				CapturePictureActivity.PROFILE_PIC_THUMB);
		mUserThumbBitmap = mProfileThumb
				.getBitmapFromDiskCache(CapturePictureActivity.PROFILE_PIC_THUMB);
		if (mUserThumbBitmap != null) {
			userImage.setImageBitmap(mUserThumbBitmap);
		}

		String[] from = { IMAGE, TEXT };
		int[] to = { R.id.dashboard_img, R.id.dashboard_txt };
		dashText.setTypeface(ITCAvantGardeStdBkFont);
		Log.d(TAG, "Size Array : " + mDashBoardTxt.length);

		for (int i = 0; i < mDashBoardTxt.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(IMAGE, String.valueOf(mImages[i]));
			map.put(TEXT, mDashBoardTxt[i]);
			mList.add(map);
		}
		SimpleAdapter addapter = new SimpleAdapter(getActivity(), mList,
				R.layout.sliding_menu_item, from, to) {
			@Override
			public void setViewText(TextView v, String text) {
				v.setTypeface(ITCAvantGardeStdBkFont);
				v.setTextColor(getActivity().getResources().getColor(
						R.color.font_blue_color));
				v.setText(text);
			}
		};
		mListView.setAdapter(addapter);
		mListView.setOnItemClickListener(leftDrawerListner);
		return view;
	}

	public static void setHeadingText(String heading) {

		mHeadingTxt = heading;
	}

	public static String getHeading() {
		return mHeadingTxt;
	}

	private OnItemClickListener leftDrawerListner = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent intent = null;
			Bundle bundle = new Bundle();

			switch (position) {
			case SETTING:
				initSettingArray();
				mCloseSideBar = 1;

				Fragment setting_menu_fragment = new SettingMenuItem(mImages,
						mHeadinItemTxt);
				setHeadingText("setting");
				// FragmentHelper
				// .gotoFragment(getActivity(), SlidingMenuFragment.this,
				// setting_menu_fragment, bundle);
				FragmentHelper.replaceFragment(getActivity(),
						setting_menu_fragment, bundle);
				break;

			case IMPORT_FREIND:
				mCloseSideBar = 1;
				Fragment friendList = new FriendListFragment(getActivity(),
						FROM_IMPORT_SIDEBAR);
				setHeadingText("import");
				FragmentHelper.replaceFragment(getActivity(), friendList,
						bundle);
				// FragmentHelper.gotoFragment(getActivity(),
				// SlidingMenuFragment.this, friendList, bundle);
				break;

			case TERMS_POLICY:
				mCloseSideBar = 1;
				initTermsPoliciesArray();
				Fragment terms_policies = new SettingMenuItem(mImages,
						mHeadinItemTxt, mSubHeadinItemTxt);
				setHeadingText("Terms & Policies");
				// FragmentHelper.gotoFragment(getActivity(),
				// SlidingMenuFragment.this, terms_policies, bundle);
				FragmentHelper.replaceFragment(getActivity(), terms_policies,
						bundle);
				break;

			case HELP_CENTER:
				mCloseSideBar = 1;
				break;

			case LOGOUT:
				Fragment log_out_fragment = new LogOutFragment();
				FragmentHelper.gotoFragment(getActivity(), SlidingMenuFragment.this, log_out_fragment, bundle);
				break;
			}
			if (mCloseSideBar == 1) {
				MainActivity mainActivity = (MainActivity) getActivity();
				mainActivity.toggle(SlidingMenu.LEFT);
			}
		}
	};
	
	private void initSettingArray() {

		mHeadinItemTxt = getResources()
				.getStringArray(R.array.setting_item_txt);
		mImages = new int[] { R.drawable.profile_setting_img,
				R.drawable.privacy_setting_img, R.drawable.blocking_img,
				R.drawable.notifications_img };

	}

	private void initTermsPoliciesArray() {

		mHeadinItemTxt = getResources().getStringArray(
				R.array.term_policies_heading_item_txt);
		mSubHeadinItemTxt = getResources().getStringArray(
				R.array.term_policies_sub_heading_item_txt);
		mImages = new int[] { R.drawable.rights_respo_img,
				R.drawable.data_uses_policy_img,
				R.drawable.comunity_standards_img };

	}
	
	
	private void onClickPickFriends() {
		startPickFriendsActivity();
	}

	private void startPickFriendsActivity() {
		AfterYouApplication application = (AfterYouApplication) getActivity()
				.getApplication();
		if (application.getOpenSession() != null) {
			application.setSelectedUsers(null);

			Intent intent = new Intent(getActivity(), PickFriendsActivity.class);
			PickFriendsActivity.populateParameters(intent, null, true, true);
			startActivityForResult(intent, MainActivity.PICK_FRIENDS_ACTIVITY);
		} else {
			AfterUIlog.e(TAG, "Facebook not activated");
		}
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
				mListView.invalidateViews();
				final ViewTreeObserver observer = mListView
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
		return false;
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
