package com.beacon.afterui.sliding.fragment;

public interface FragmentLifecycle {

	public void onFragmentPause();
	
	public void onFragmentResume();
	
	public boolean onBack();
	
}
