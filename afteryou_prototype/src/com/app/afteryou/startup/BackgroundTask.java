
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
