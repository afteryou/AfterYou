
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
