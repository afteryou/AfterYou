package com.app.afteryou.fragment;

public interface FragmentLifecycle {

	public void onFragmentPause();
	
	public void onFragmentResume();
	
	public boolean onBack();
	
}
