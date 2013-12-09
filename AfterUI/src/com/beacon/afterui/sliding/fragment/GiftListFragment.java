package com.beacon.afterui.sliding.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.beacon.afterui.R;
import com.beacon.afterui.constants.CommonConstants;

public class GiftListFragment extends Fragment implements FragmentLifecycle,
        ISearchFunction, OnItemClickListener {

    private static final String TAG = GiftListFragment.class.getSimpleName();

    private GridView mGridView;

    public GiftListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gifts_list_screen, null, false);
        mGridView = (GridView) view.findViewById(R.id.gifts_list_grid_view);
        mGridView.setAdapter(new GiftsAdapter());
        mGridView.setOnItemClickListener(this);
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

    }

    @Override
    public boolean onBack() {
        return false;
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

            return convertView;
        }
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
}
