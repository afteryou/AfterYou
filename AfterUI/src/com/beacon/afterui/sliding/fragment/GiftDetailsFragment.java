package com.beacon.afterui.sliding.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.constants.CommonConstants;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.views.MainActivity;

public class GiftDetailsFragment extends Fragment implements FragmentLifecycle,
		ISearchFunction, OnClickListener {

	/** TAG */
	private static final String TAG = GiftDetailsFragment.class.getSimpleName();

	private Button mDoneButton;
	private Button mCancelButton;

	private boolean isBacking;

	private View mSendGiftPopup;
	private TextView mSendGiftTextView;
	private TextView mSendMessageWithGiftTextView;

	private TextView mGiftName;
	private TextView mGiftPoints;
	private TextView mDateText;

	private View mBuyPointsPopUp;
	private TextView mBuyPointsButton;
	private Typeface mTypeFace;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.gift_details_screen, null, false);
//		mTypeFace = Typeface.createFromAsset(getActivity().getAssets(),
//				"fonts/ITCAvantGardeStd-Bk.otf");
		
		mTypeFace = FontUtils.loadTypeFace(getActivity()
                .getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		mGiftName = (TextView) view.findViewById(R.id.gift_name);
		mGiftName.setTypeface(mTypeFace);

		mGiftPoints = (TextView) view.findViewById(R.id.number_of_points);
		mGiftPoints.setTypeface(mTypeFace);

		mDateText = (TextView) view.findViewById(R.id.date_txt);
		mDateText.setTypeface(mTypeFace);

		mDoneButton = (Button) view.findViewById(R.id.gift_details_done_button);
		mCancelButton = (Button) view
				.findViewById(R.id.gift_details_cancel_button);

		mDoneButton.setTypeface(mTypeFace);
		mCancelButton.setTypeface(mTypeFace);

		mSendGiftPopup = view.findViewById(R.id.send_gift_pop_up);
		mSendGiftTextView = (TextView) view.findViewById(R.id.send_gift);
		mSendMessageWithGiftTextView = (TextView) view
				.findViewById(R.id.send_message_with_gift);
		mSendMessageWithGiftTextView.setTypeface(mTypeFace);
		mSendGiftTextView.setTypeface(mTypeFace);

		mSendGiftTextView.setOnClickListener(this);
		mSendMessageWithGiftTextView.setOnClickListener(this);

		mBuyPointsPopUp = view.findViewById(R.id.buy_points_pop_up);
		mBuyPointsButton = (TextView) view.findViewById(R.id.buy_points);
		mBuyPointsButton.setOnClickListener(this);
		mBuyPointsButton.setTypeface(mTypeFace);

		TextView not_enough_points = (TextView) view
				.findViewById(R.id.not_enough_points);
		not_enough_points.setTypeface(mTypeFace);

		mDoneButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mSendGiftPopup.setVisibility(View.GONE);
		mBuyPointsPopUp.setVisibility(View.GONE);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gift_details_done_button:
			mSendGiftPopup.setVisibility(View.VISIBLE);
			break;

		case R.id.gift_details_cancel_button:
			onBack();
			break;

		case R.id.send_gift:
			mSendGiftPopup.setVisibility(View.GONE);
			mBuyPointsPopUp.setVisibility(View.VISIBLE);
			break;

		case R.id.send_message_with_gift:
			mSendGiftPopup.setVisibility(View.GONE);
			mBuyPointsPopUp.setVisibility(View.VISIBLE);
			break;

		case R.id.buy_points:
			launchBuyGiftFragment();
			break;
		}
	}

	private void launchBuyGiftFragment() {
		Fragment detail = new GiftsBuyFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(CommonConstants.BundleKey.GIFT_ID, 1);
		FragmentHelper.gotoFragment(getActivity(), GiftDetailsFragment.this,
				detail, bundle);
	}
}
