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


public abstract class BackgroundTask extends AbstractTask{

	private ITaskManager taskMgr;
	BackgroundTask(ITaskCondition condition, ITaskManager taskManager) {
		super(condition);
		taskMgr = taskManager;
	}

	protected final ITaskManager getTaskManager(){
		return taskMgr;
	}
	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

}
