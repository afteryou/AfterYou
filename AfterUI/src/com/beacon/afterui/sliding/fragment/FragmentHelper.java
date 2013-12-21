package com.beacon.afterui.sliding.fragment;

import java.util.Stack;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.beacon.afterui.R;

public class FragmentHelper {

    private static Stack<Fragment> fragmentStack = new Stack<Fragment>();

    public static void gotoFragment(Activity act, Fragment currFragment,
            Fragment targetFragment) {
        gotoFragment(act, currFragment, targetFragment, null, true);
    }

    public static void gotoFragment(Activity act, Fragment currFragment,
            Fragment targetFragment, Bundle datas) {
        gotoFragment(act, currFragment, targetFragment, datas, true);
    }

    public static void gotoFragment(Activity act, Fragment currFragment,
            Fragment targetFragment, Bundle datas, boolean keepInStack) {
        FragmentTransaction transaction = act.getFragmentManager()
                .beginTransaction();
        // transaction.setCustomAnimations(enter, exit);

        FragmentManager fragmentManager = act.getFragmentManager();
        Log.d("test", "goto : Total Fragment count : " + fragmentManager.getBackStackEntryCount());
        
        if (datas != null) {
            targetFragment.setArguments(datas);
        }

        transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_right_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_left_exit);
        
        transaction.add(act.findViewById(R.id.content_detail).getId(),
                targetFragment, null);
//        transaction.replace(act.findViewById(R.id.main_fragment_container).getId(), targetFragment);
        transaction.show(targetFragment);
        if (!keepInStack) {
            transaction.remove(currFragment);
            if (!fragmentStack.isEmpty())
                fragmentStack.pop();
        } else {
//            transaction.hide(currFragment);
            transaction.addToBackStack(null);
            
            if (fragmentStack.isEmpty())
                fragmentStack.push(currFragment);
        }
        fragmentStack.push(targetFragment);
        transaction.commit();

        ((FragmentLifecycle) targetFragment).onFragmentResume();
    }

    public static void initFragment(Activity act, Fragment fragment) {
        FragmentTransaction transaction = act.getFragmentManager()
                .beginTransaction();
        transaction.add(act.findViewById(R.id.content_detail).getId(),
                fragment, null);
        fragmentStack.push(fragment);
        transaction.show(fragment);
        transaction.commit();
    }

    public static void replaceFragment(Activity act, Fragment fragment) {
        replaceFragment(act, fragment, null);
    }

    public static void replaceFragment(Activity act, Fragment fragment,
            Bundle datas) {
        replaceFragment(act, fragment, datas, true);
    }

    public static void replaceFragment(Activity act, Fragment fragment,
            Bundle datas, boolean keepInStack) {
        FragmentTransaction transaction = act.getFragmentManager()
                .beginTransaction();
        // transaction.setCustomAnimations(enter, exit);
        if (datas != null) {
            fragment.setArguments(datas);
        }
        transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_right_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_left_exit);
        transaction.replace(act.findViewById(R.id.content_detail)
                .getId(), fragment);
        transaction.show(fragment);
        if (keepInStack)
            transaction.addToBackStack(null);

        Log.d("test", "replace : Total Fragment count : " + act.getFragmentManager().getBackStackEntryCount());
        transaction.commit();
    }

    public static boolean popFragment(Activity act) {
        if (fragmentStack.size() == 0) {
            return false;
        }
        FragmentTransaction transaction = act.getFragmentManager()
                .beginTransaction();
        // transaction.setCustomAnimations(enter, exit);

        Fragment current = fragmentStack.pop();
        transaction.remove(current);
        ((FragmentLifecycle) current).onFragmentPause();
        current = fragmentStack.peek();
        transaction.show(current);
        ((FragmentLifecycle) current).onFragmentResume();
        transaction.commit();
        return true;
    }

    public static Fragment getCurruntFragment() {
        if (fragmentStack.size() == 0) {
            return null;
        }
        return fragmentStack.peek();
    }

    public static void onActivityCreate(Activity act) {
        fragmentStack.clear();
    }

    public static boolean onBack(Activity act) {
        if (fragmentStack.size() == 0) {
            return false;
        }
        Fragment current = fragmentStack.peek();
        return ((FragmentLifecycle) current).onBack();
    }

}
