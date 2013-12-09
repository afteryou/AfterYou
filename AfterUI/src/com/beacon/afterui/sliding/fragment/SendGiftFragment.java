package com.beacon.afterui.sliding.fragment;

import com.beacon.afterui.R;
import com.beacon.afterui.views.MainActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SendGiftFragment extends Fragment implements FragmentLifecycle,
        ISearchFunction, OnClickListener {

    /** TAG */
    private static final String TAG = SendGiftFragment.class.getSimpleName();

    private Button mDoneButton;
    private Button mPost;
    
    private boolean isBacking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.send_gift_screen, null, false);

        mDoneButton = (Button) view.findViewById(R.id.send_gift_done_button);
        mPost = (Button) view.findViewById(R.id.gifts_buy_post_button);

        mDoneButton.setOnClickListener(this);
        mPost.setOnClickListener(this);

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

    private void showToast(final String toastMessage) {
        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }
    
    private void finishFragment() {
        if(!isBacking) {
            isBacking = true;
            ((MainActivity)getActivity()).updateToMainScreenActionBar();
            applyBackAnimation();
        }
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
        case R.id.send_gift_done_button:
            showToast("Gift is sent!");
            finishFragment();
            break;

        case R.id.gifts_buy_post_button:
            break;
        }
    }
}
