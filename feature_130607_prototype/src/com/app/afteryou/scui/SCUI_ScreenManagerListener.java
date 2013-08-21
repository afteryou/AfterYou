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

package com.app.afteryou.scui;

public interface SCUI_ScreenManagerListener {
    public void pushView(int type, boolean animated, int transition);

    public void pushViewAsRoot();

    public void popToMainScreen();
    
    public void popView(int viewType, boolean animated, int transition);
    
    public void getDeviceDate(int year, int month, int day, int hour, int minute, int second, int timeZone, boolean useDaySaving);
}
