
package com.app.afteryou.startup;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;

import com.app.afteryou.log.Nimlog;
import com.navbuilder.nb.NBException;



public abstract class ForegroundTask extends AbstractTask{
	
	private ITaskManager taskMgr;
	
	
	ForegroundTask(ITaskCondition condition, ITaskManager taskManager) {
		super(condition);
		taskMgr = taskManager;
	}
	
	protected final ITaskManager getTaskManager(){
		return taskMgr;
	}
	/**
	 * Please run it in UI thread.
	 * @param componentId  internal id using for UI 
	 * @param exception   null in ordinary
	 */
	public abstract void showUIComponent(int componentId,NBException exception);
	
	
//	void showErrorDialog(final Activity act, final NBException exception){
//		Handler uiHandler = new Handler(act.getMainLooper());
//		uiHandler.post(new Runnable(){
//			@Override
//			public void run() {
//				Dialog errorDialog =  ThemeManager.getInstance().newErrorDialog(act,
//                    new OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        	if(AppBuildConfig.isDebugMode() && !PreferenceEngine.getInstance(act.getApplicationContext()).isFirstBoot()
//                        			 && !AppBuildConfig.APP_TOKEN.equalsIgnoreCase("prod"))
//                        		new DebugSwitchServerTask(act, getTaskManager(), true).showUIComponent(0, exception);
//                        	else
//                        	getTaskManager().onTaskBreak(ForegroundTask.this);
//                        }
//					}, exception == null ? act.getString(R.string.IDS_COULD_NOT_PROCESS_YOUR_REQUEST)
//							: ErrorController.getNBExceptionErrStringId(exception));
//				SafeShowDialog.show(errorDialog);
//			}
//        });
//	}
	
	@Override
	public void cancel(){
		Nimlog.i(this, "Cancel Task:" + this.getClass().getSimpleName());
		getTaskManager().onTaskBreak(this);
	}
}
