
package com.beacon.afterui.scui;

public interface SCUI_ScreenManagerListener {
    public void pushView(int type, boolean animated, int transition);

    public void pushViewAsRoot();

    public void popToMainScreen();
    
    public void popView(int viewType, boolean animated, int transition);
    
    public void getDeviceDate(int year, int month, int day, int hour, int minute, int second, int timeZone, boolean useDaySaving);
}
