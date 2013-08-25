
package com.app.afteryou.startup;


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
