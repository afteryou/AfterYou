package com.navbuilder.app.hurricane.fragment;

public interface FragmentLifecycle {

	public void onFragmentPause();
	
	public void onFragmentResume();
	
	public boolean onBack();
	
}
