package com.beacon.afterui.sliding.fragment;

import com.beacon.afterui.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class GiftListFragment extends Fragment implements FragmentLifecycle,
        ISearchFunction {

    private GridView mGridView;

    public GiftListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gifts_list_screen, null, false);
        mGridView = (GridView) view.findViewById(R.id.gifts_list_grid_view);
        mGridView.setAdapter(new GiftsAdapter());
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

}
