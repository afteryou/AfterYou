package com.beacon.afterui.notification.message;

import com.beacon.afterui.notification.message.data.ProfileInfo;

public interface IMessage {

	public boolean isReceived();

	public String senderName();

	public long getMessageID();

	public void setResult(int result);

	public int[] getResult();

	public String getMessage();

	public ProfileInfo getProfileInfo();
}