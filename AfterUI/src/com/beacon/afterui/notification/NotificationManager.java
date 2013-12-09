package com.beacon.afterui.notification;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.beacon.afterui.notification.message.IMessage;

public class NotificationManager extends Service {
	private IMessage notifyMessage;

	private NotifyManagerImpl notification = new NotifyManagerImpl();

	class NotifyManagerImpl extends Binder {
		public NotificationManager getService() {
			return NotificationManager.this;
		}
	}

	public void notifyAndroidTaskbar() {

	}

	public IMessage getNotifyMessage() {
		return notifyMessage;
	}

	public void setNotifyMessage(IMessage notifyMessage) {
		this.notifyMessage = notifyMessage;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return notification;
	}

}