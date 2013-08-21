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


abstract class AbstractTask implements StartupTask {
	
	private ITaskCondition condition;
	
	AbstractTask(ITaskCondition condition){
		this.condition = condition;
	}
	
	protected final boolean getCondition(){
		return condition.isTaskExecute();
	}
	
	public void onActivityResume(){		
	}
}
