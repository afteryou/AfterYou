package com.beacon.afterui.sliding.fragment;

import com.beacon.afterui.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GiftDetailsFragment extends Fragment implements FragmentLifecycle,
        ISearchFunction {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.gift_details_screen, null, false);
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

}
