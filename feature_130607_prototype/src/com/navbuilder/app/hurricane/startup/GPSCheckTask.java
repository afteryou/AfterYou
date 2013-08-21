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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Handler;

import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.controller.GPSController;
import com.navbuilder.app.hurricane.log.Nimlog;
import com.navbuilder.app.hurricane.preference.PreferenceEngine;
import com.navbuilder.app.hurricane.utils.AppUtils;
import com.navbuilder.nb.NBException;

class GPSCheckTask extends ForegroundTask {
	private Context context;
	private Handler uiHandler;
	
	private Dialog gpsSettingDialog;
	private Dialog exitAppDialog;
	
	private static final int DIALOG_GPS_SETTING=1001;
	private static final int DIALOG_NO_AVAILABLE_LAST_KNOWN_LOCATION=1002;

	GPSCheckTask(ITaskManager taskManager) {
		super(new ITaskCondition() {
			@Override
			public boolean isTaskExecute() {
				return true;
			}
		}, taskManager);
		this.context = taskManager.getContext();
		uiHandler = new Handler(context.getMainLooper());
	}
	
	@Override
	public void execute() {
		if(getCondition()){
			Nimlog.i(this, "executeEvent  GPSCheckTask");
			checkGpsSetting();
		}else{
			getTaskManager().onTaskComplete(this);
		}
	   
	}

	private void checkGpsSetting() {
		Nimlog.i(this, "checkGpsSetting start >>");
        
		boolean gpsEnable=GPSController.getInstance(context).isGPSEnable();
		
        if (gpsEnable){
            Nimlog.i(this,"Location services available");
            getTaskManager().onTaskComplete(this);
        }
        else{
			uiHandler.post(new Runnable() {
				@Override
				public void run() {
					showUIComponent(DIALOG_GPS_SETTING, null);
				}
			});
        }
	}

	@Override
	public void onActivityResume() {
		if(exitAppDialog!=null&&exitAppDialog.isShowing()){
			return;
		}
		
		if(gpsSettingDialog != null){
			gpsSettingDialog.dismiss();
			checkGpsSetting();
		}
	}

	@Override
	public void showUIComponent(int componentId, NBException exception) {
		
		switch(componentId){
			case DIALOG_GPS_SETTING:
				
				if (gpsSettingDialog == null || !gpsSettingDialog.isShowing()) {
					uiHandler.post(new Runnable() {
						@Override
						public void run() {
							
							AlertDialog.Builder builder=new AlertDialog.Builder(context)
			                .setMessage(R.string.IDS_TO_OBTAIN_YOUR_CURRENT_POSITION)
			                .setPositiveButton(R.string.IDS_OK, new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int whichButton) {
			                    	dialog.dismiss();
									if (context != null) {
										context.startActivity(new Intent(
											"android.settings.LOCATION_SOURCE_SETTINGS"));
									} else {
										getTaskManager().onTaskComplete(GPSCheckTask.this);
									}
			                    }
			                })
			                .setOnCancelListener(new OnCancelListener(){
								@Override
								public void onCancel(DialogInterface dialog) {
									
									dialog.dismiss();
			                    	if(!AppUtils.isFirstTimeStart()&&PreferenceEngine.getInstance().getLastLocation()!=null){
			                    		//TODO: use last known location
			                    		getTaskManager().onTaskComplete(GPSCheckTask.this);
			                    	}else{
			                    		//last known location is not available
			                    		showUIComponent(DIALOG_NO_AVAILABLE_LAST_KNOWN_LOCATION,null);
			                    	}
								}})
			                .setNegativeButton(R.string.IDS_CANCEL, new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int whichButton) {

			                    	dialog.dismiss();
			                    	if(!AppUtils.isFirstTimeStart()&&PreferenceEngine.getInstance().getLastLocation()!=null){
			                    		//TODO: use last known location
			                    		getTaskManager().onTaskComplete(GPSCheckTask.this);
			                    	}else{
			                    		//last known location is not available
			                    		showUIComponent(DIALOG_NO_AVAILABLE_LAST_KNOWN_LOCATION,null);
			                    	}
			                    	
			                    }
			                });
							
							gpsSettingDialog = builder.create();
							gpsSettingDialog.show();
						}
					});
				}
				
				break;
			case DIALOG_NO_AVAILABLE_LAST_KNOWN_LOCATION:
				
				if (exitAppDialog == null || !exitAppDialog.isShowing()) {
					uiHandler.post(new Runnable() {
						@Override
						public void run() {
							
							AlertDialog.Builder builder=new AlertDialog.Builder(context)
			                .setMessage(R.string.IDS_THERE_IS_NO_AVAILABLE_KNOWN_LOCATION)
			                .setOnCancelListener(new OnCancelListener(){
								@Override
								public void onCancel(DialogInterface dialog) {
									dialog.dismiss();
			                    	getTaskManager().onTaskBreak(GPSCheckTask.this);
								}})
			                .setPositiveButton(R.string.IDS_OK, new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int whichButton) {
			                    	dialog.dismiss();
			                    	getTaskManager().onTaskBreak(GPSCheckTask.this);
			                    }
			                });
							
							exitAppDialog=builder.create();
			            	exitAppDialog.show();
							
						}
					});
				}
				
				break;
		}
		
	}
}
