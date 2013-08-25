
package com.app.afteryou.startup;

public interface StartupTask {

	public abstract void execute();

	public abstract void cancel();

	/**
	 * call it when activity resume
	 */
	public abstract void onActivityResume();

}