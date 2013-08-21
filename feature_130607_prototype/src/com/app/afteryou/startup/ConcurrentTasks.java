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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;

import com.app.afteryou.log.Nimlog;
import com.navbuilder.nb.NBException;

public final class ConcurrentTasks extends ForegroundTask implements ITaskManager{
	
	private List<StartupTask> tasks;
	private ITaskManager taskMgr;
	private Context context;
	private Handler uiHandler;
//	CustomProgressDialog progressDialog;
	private final int PROGRESS_DIALOG = 1;
	ConcurrentTasks(ITaskManager mgr) {
		super(new ITaskCondition(){
			@Override
			public boolean isTaskExecute() {
				return true;
			}
			
		}, mgr);
		taskMgr = mgr;
		this.context = mgr.getContext();
		uiHandler = new Handler(context.getMainLooper());
		tasks = new ArrayList<StartupTask>();
	}

	
	@Override
	public void execute() {
		Nimlog.i(ConcurrentTasks.this, "[execute]execute");
		executeTask();
	}
	
	@Override
	public void cancel() {
//		closeProgressDialog();
		List<StartupTask> cancelTasks = new ArrayList<StartupTask>(tasks);
		for(StartupTask task:cancelTasks){
			Nimlog.i(this, "cancel :: " + task);
			task.cancel();
		}
		getTaskManager().onTaskBreak(this);
	}


	@Override
	public void showUIComponent(int componentId, NBException exception) {
//		switch (componentId) {
//		case PROGRESS_DIALOG:
//	        uiHandler.post(new Runnable(){
//				@Override
//				public void run() {
//					progressDialog = DialogHelper.createProgessDialog(activity, null);
//					progressDialog.setMessage(activity.getString(R.string.IDS_DOWNLOADING_DATA)
//							+ activity.getString(R.string.IDS_ELLIPSIS));
//					progressDialog.setIndeterminate(true);
////					progressDialog.setCancelable(false);
//					progressDialog.setOnCancelListener(new OnCancelListener(){
//
//						@Override
//						public void onCancel(DialogInterface dialog) {
//							cancel();							
//						}});
//					SafeShowDialog.show(progressDialog);
//				}
//	        });	
//			break;
//
//		default:
//			break;
//		}
		
	}

	@Override
	public void onTaskComplete(StartupTask task) {
		Nimlog.i(ConcurrentTasks.this, "[onTaskComplete]onTaskComplete:"+task.getClass().toString());
		tasks.remove(task);
		checkTaskList(task);
		
	}


	@Override
	public void onTaskBreak(StartupTask task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTask(StartupTask task) {
		Nimlog.i(ConcurrentTasks.this, "[addTask]addTask:"+task.getClass().toString());
		tasks.add(task);
		
	}


	@Override
	public void executeTask() {
		Nimlog.i(ConcurrentTasks.this, "[executeTask]tasks.size:"+tasks.size());
		if(tasks!=null&&!tasks.isEmpty()){
			for(StartupTask task: tasks){
				Nimlog.i(ConcurrentTasks.this, "[executeTask]task:"+task.getClass().toString());
				task.execute();
			}
		}else{
			taskMgr.onTaskComplete(this);
		}
		
	}


	@Override
	public void continueTask() {
		// TODO Auto-generated method stub
		
	}
	
	private void checkTaskList(StartupTask task){
		Nimlog.i(ConcurrentTasks.this, "[checkTaskList]tasks size:"+tasks.size());
		if(tasks.size() == 0){
			taskMgr.onTaskComplete(this);
		}
	}


	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return context;
	}
}
