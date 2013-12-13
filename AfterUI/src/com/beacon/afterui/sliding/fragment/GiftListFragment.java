package com.beacon.afterui.sliding.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.constants.CommonConstants;
import com.beacon.afterui.views.MainActivity;

public class GiftListFragment extends Fragment implements FragmentLifecycle,
		ISearchFunction, OnItemClickListener, OnClickListener {

	private static final String TAG = GiftListFragment.class.getSimpleName();

	private GridView mGridView;

	private boolean isBacking;

	private TextView mBasicTextView;
	private TextView mDeluxeTextView;
	private TextView mPremiumTextView;
	private TextView mLuxaryTextView;

	private Button mCancelBtn;

	private Drawable mNormalImage;
	private Drawable mSelectedImage;

	private int mSelectedId;
	private Typeface mTypeFace;

	private GiftsAdapter mGiftAdapter;

	private Drawable mBasic;
	private Drawable mDeluxe;
	private Drawable mPremium;
	private Drawable mLuxary;

	public GiftListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.gifts_list_screen, null, false);

		mTypeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/ITCAvantGardeStd-Bk.otf");

		mGridView = (GridView) view.findViewById(R.id.gifts_list_grid_view);
		mGiftAdapter = new GiftsAdapter();
		mGridView.setAdapter(mGiftAdapter);
		mGridView.setOnItemClickListener(this);

		mBasicTextView = (TextView) view.findViewById(R.id.basic);
		mDeluxeTextView = (TextView) view.findViewById(R.id.deluxe);
		mPremiumTextView = (TextView) view.findViewById(R.id.premimum);
		mLuxaryTextView = (TextView) view.findViewById(R.id.luxary);

		mBasicTextView.setTypeface(mTypeFace);
		mDeluxeTextView.setTypeface(mTypeFace);
		mPremiumTextView.setTypeface(mTypeFace);
		mLuxaryTextView.setTypeface(mTypeFace);

		mBasicTextView.setOnClickListener(this);
		mDeluxeTextView.setOnClickListener(this);
		mPremiumTextView.setOnClickListener(this);
		mLuxaryTextView.setOnClickListener(this);

		mCancelBtn = (Button) view
				.findViewById(R.id.gifts_list_buy_cancel_button);
		TextView gl_points_acc_text = (TextView) view
				.findViewById(R.id.gl_points_acc_text);

		mCancelBtn.setTypeface(mTypeFace);
		gl_points_acc_text.setTypeface(mTypeFace);
		mCancelBtn.setOnClickListener(this);

		mNormalImage = getResources().getDrawable(R.drawable.gift_icon);
		mNormalImage.setBounds(0, 0, mNormalImage.getIntrinsicWidth(),
				mNormalImage.getIntrinsicHeight());

		mSelectedImage = getResources().getDrawable(
				R.drawable.gift_selected_icon);
		mSelectedImage.setBounds(0, 0, mSelectedImage.getIntrinsicWidth(),
				mSelectedImage.getIntrinsicHeight());

		initTempImages();
		return view;
	}

	private void initTempImages() {
		mBasic = getResources().getDrawable(R.drawable.chocolate_gift);
		mBasic.setBounds(0, 0, mBasic.getIntrinsicWidth(),
				mBasic.getIntrinsicHeight());

		mDeluxe = getResources().getDrawable(R.drawable.sandle_gift);
		mDeluxe.setBounds(0, 0, mDeluxe.getIntrinsicWidth(),
				mDeluxe.getIntrinsicHeight());

		mPremium = getResources().getDrawable(R.drawable.teddy_gift);
		mPremium.setBounds(0, 0, mPremium.getIntrinsicWidth(),
				mPremium.getIntrinsicHeight());

		mLuxary = getResources().getDrawable(R.drawable.test_gift);
		mLuxary.setBounds(0, 0, mLuxary.getIntrinsicWidth(),
				mLuxary.getIntrinsicHeight());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSelectedId = R.id.basic;
		setSelectedImage(R.id.basic);
	}

	@Override
	public void doSearch(int type, SearchParams params) {

	}

	@Override
	public void onFragmentPause() {

	}

	@Override
	public void onFragmentResume() {

	}

	private class GiftsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 15;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.gift_list_item, null, false);
			}

			TextView giftItem = (TextView) convertView
					.findViewById(R.id.gift_list_temp);
			giftItem.setTypeface(mTypeFace);

			switch (mSelectedId) {
			case R.id.basic:
				giftItem.setCompoundDrawablesWithIntrinsicBounds(null, mBasic,
						null, null);
				break;

			case R.id.deluxe:
				giftItem.setCompoundDrawablesWithIntrinsicBounds(null, mDeluxe,
						null, null);
				break;

			case R.id.premimum:
				giftItem.setCompoundDrawablesWithIntrinsicBounds(null,
						mPremium, null, null);
				break;

			case R.id.luxary:
				giftItem.setCompoundDrawablesWithIntrinsicBounds(null, mLuxary,
						null, null);
			}

			return convertView;
		}
	}

	@Override
	public boolean onBack() {
		if (!isBacking) {
			isBacking = true;
			((MainActivity) getActivity()).updateToMainScreenActionBar();
			applyBackAnimation();
		}
		return true;
	}

	private void applyBackAnimation() {
		AnimatorSet fideOutMap = new AnimatorSet();
		fideOutMap.setDuration(250);
		fideOutMap.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator anim) {
				FragmentHelper.popFragment(getActivity());
				isBacking = false;
			}
		});

		fideOutMap.start();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Log.d(TAG, "Clicked on " + position);

		Fragment detail = new GiftDetailsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(CommonConstants.BundleKey.GIFT_ID, 1);
		FragmentHelper.gotoFragment(getActivity(), GiftListFragment.this,
				detail, bundle);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.basic:
			break;
		case R.id.deluxe:
			break;
		case R.id.premimum:
			break;
		case R.id.luxary:
			break;
		case R.id.gifts_list_buy_cancel_button:
			onBack();
			break;
		}
		setSelectedImage(v.getId());
		mGiftAdapter.notifyDataSetChanged();
	}

	private void setSelectedImage(final int selectedId) {

		switch (selectedId) {
		case R.id.basic:
			mSelectedId = R.id.basic;
			mBasicTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mSelectedImage, null, null);
			mDeluxeTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			mPremiumTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			mLuxaryTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			break;

		case R.id.deluxe:
			mSelectedId = R.id.deluxe;
			mBasicTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			mDeluxeTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mSelectedImage, null, null);
			mPremiumTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			mLuxaryTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			break;

		case R.id.premimum:
			mSelectedId = R.id.premimum;
			mBasicTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			mDeluxeTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			mPremiumTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mSelectedImage, null, null);
			mLuxaryTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			break;

		case R.id.luxary:
			mSelectedId = R.id.luxary;
			mBasicTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			mDeluxeTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			mPremiumTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mNormalImage, null, null);
			mLuxaryTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mSelectedImage, null, null);
		}
	}
}
