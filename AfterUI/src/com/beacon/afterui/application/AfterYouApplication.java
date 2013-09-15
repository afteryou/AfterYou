package com.beacon.afterui.application;

import com.facebook.Session;
import com.facebook.model.GraphUser;

import android.app.Application;

public class AfterYouApplication extends Application {

	private static Application instance;
	private Session session;
    private GraphUser user = null;


	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	public static Application getInstance() {
		return instance;
	}
	
    public Session getOpenSession() {
        Session openSession = getSession();
        if (openSession != null && openSession.isOpened()) {
            return openSession;
        }
        return null;
    }
    
    private Session getSession() {
        return (session == null) ? Session.getActiveSession() : session;
    }
    
    public void setSessionCallBack(Session session)
    {
    	this.session = session;
    }

	public GraphUser getUser() {
		return user;
	}

	public void setUser(GraphUser user) {
		this.user = user;
	}

}
