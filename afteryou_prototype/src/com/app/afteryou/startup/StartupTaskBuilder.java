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

package com.app.afteryou.startup;

import com.app.afteryou.log.Nimlog;
import com.navbuilder.nb.NBGlobalListener;

public class StartupTaskBuilder {
	
	public static void buildTaskList(boolean isFtt, ITaskManager taskManager) {
		
		if(!isFtt){
			Nimlog.i("StartupTaskBuilder", "==== STARTUP_NORMAL ====");
			taskManager.addTask(new GPSCheckTask(taskManager));
		}else{
			Nimlog.i("StartupTaskBuilder", "==== STARTUP_FTT ====");	
			taskManager.addTask(new GPSCheckTask(taskManager));
		}
		
	}
	
}
