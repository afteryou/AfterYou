package com.beacon.afterui.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.views.LoginScreen;

public class NotificationReciever extends BroadcastReceiver {

	public static final String NOTIFICATION_DETAIL = "NOTIFICATION";

	Context contextRecd = null;

	@Override
	public void onReceive(Context arg0, Intent intent) {
		System.out.println("Here in Notification SARNAB");

		Bundle extras = intent.getExtras();
		contextRecd = arg0;
//		if (extras != null) {
			Intent notifyIntent = new Intent(contextRecd, LoginScreen.class);
			notifyIntent.setAction(AppConstants.NOTIFICATION_SENT);
			notifyIntent.putExtra(AppConstants.SENDER, intent.getExtras().getString(AppConstants.SENDER));
			notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			contextRecd.startActivity(notifyIntent);
//		}
	}

}
