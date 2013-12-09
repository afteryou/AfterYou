package com.beacon.afterui.sliding.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.beacon.afterui.R;
import com.beacon.afterui.constants.CommonConstants;

public class GiftDetailsFragment extends Fragment implements FragmentLifecycle,
        ISearchFunction, OnClickListener {

    /** TAG */
    private static final String TAG = GiftDetailsFragment.class.getSimpleName();

    private Button mDoneButton;
    private Button mCancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gift_details_screen, null, false);

        mDoneButton = (Button) view.findViewById(R.id.gift_details_done_button);
        mCancelButton = (Button) view
                .findViewById(R.id.gift_details_cancel_button);

        mDoneButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void doSearch(int type, SearchParams params) {

    }

    @Override
    public void onFragmentPause() {

    }

    @Override
    public void onFragmentResume() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onBack() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.gift_details_done_button:
            Fragment detail = new GiftsBuyFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(CommonConstants.BundleKey.GIFT_ID, 1);
            FragmentHelper.gotoFragment(getActivity(),
                    GiftDetailsFragment.this, detail, bundle);
            break;

        case R.id.gift_details_cancel_button:
            break;
        }
    }

}
