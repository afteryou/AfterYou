
package com.app.afteryou.controller;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.app.afteryou.log.Nimlog;
import com.app.afteryou.preference.PreferenceEngine;
import com.app.afteryou.startup.IStartupActivity;
import com.app.afteryou.startup.ITaskManager;
import com.app.afteryou.startup.StartupTask;
import com.app.afteryou.startup.StartupTaskBuilder;
import com.app.afteryou.utils.AppUtils;
import com.navbuilder.nb.NBGlobalListener;

public class StartupController implements ITaskManager{
	private LinkedList<StartupTask> tasks;
	private StartupTask runningTask;
	private boolean isStartComplete = false;
	private Context context;
	private IStartupActivity actionActivity;
	
	private static StartupController instance;
	
	private StartupController(Context con){
		this.context=con;
		tasks = new LinkedList<StartupTask>();
	}
	
	public static StartupController getInstance(Context context){
		if(instance==null){
			instance=new StartupController(context);
		}
		return instance;
	}
	
	public void doStart(IStartupActivity actionActivity) {
		
		if(actionActivity!=null){
			Nimlog.i(this, "doStart" );
			this.actionActivity=actionActivity;
			
			Handler uiHandler = new Handler(context.getMainLooper());
			uiHandler.post(new Runnable() {
				@Override
				public void run() {
					buildTasks();
					executeTask();
				}
			});
		}
	}
	
	private void buildTasks() {
		StartupTaskBuilder.buildTaskList(AppUtils.isFirstTimeStart(), this);
	}

	private void reset() {
		tasks.clear();
		runningTask = null;
		isStartComplete = false;
	}

	
	@Override
	public void addTask(StartupTask task) {
		tasks.add(task);		
	}

	@Override
	public void continueTask() {
		if (runningTask != null) {
			runningTask.onActivityResume();
		}
	}

	@Override
	public void executeTask() {
		actionActivity.onTaskStart();
		executeNextTask();		
	}

	private void executeNextTask() {
		if (tasks.isEmpty()) {
			isStartComplete = true;
			runningTask = null;
			//TODO: startup complete
			actionActivity.onTaskEnd();
			Nimlog.i(this, "startup complete" );
			return;
		}
		
		runningTask = tasks.poll();
		if (runningTask != null) {
			Nimlog.i("startup", runningTask + " is executing...");
			try {
				runningTask.execute();
			} catch(NullPointerException e) {
				Nimlog.w("startup", "Exception: ");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onTaskBreak(StartupTask task) {
		actionActivity.onTaskError();
		reset();
	}

	@Override
	public void onTaskComplete(StartupTask task) {
		if (runningTask == task) {
			executeNextTask();
		}else{
			Nimlog.e(this, "Running task and finished task did not match");
		}		
	}

	public void cancelTask(){
		if (runningTask != null) {
			runningTask.cancel();
		}
	}

	@Override
	public Context getContext() {
		return context;
	}
	
}
