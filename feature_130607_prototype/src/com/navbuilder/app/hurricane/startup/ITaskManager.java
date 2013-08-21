/*
 * (C) Copyright 2012 by TeleCommunication Systems, Inc.
 *
 * The information contained herein is confidential, proprietary
 * to TeleCommunication Systems, Inc., and considered a trade secret
 * as defined in section 499C of the penal code of the State of
 * California. Use of this information by anyone other than
 * authorized employees of TeleCommunication Systems is granted only
 * under a written non-disclosure agreement, expressly prescribing
 * the scope and manner of such use.
 *
 */

package com.navbuilder.app.hurricane.startup;

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
