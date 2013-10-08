
package com.app.afteryou.startup;

import android.app.Activity;
import android.content.Context;

public interface ITaskManager {
	public void onTaskComplete(StartupTask task);

	public void onTaskBreak(StartupTask task);
	
	public void addTask(StartupTask task);

	public void executeTask();

	// this method should invoke when application is switched back, take
	// example, suppose user open native location setting page and make some
	// change, when he close the screen and come back to our application, this
	// method should be invoked to continue with startup.
	public void continueTask();
	public Context getContext();
}
