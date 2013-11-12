package com.beacon.afterui.application;

import java.util.Collection;
import java.util.List;

import com.beacon.afterui.chat.ChatManagerService;
import com.beacon.afterui.chat.ChatManagerService.ChatManagerImpl;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class AfterYouApplication extends Application {

    /** TAG */
    private static final String TAG = AfterYouApplication.class.getSimpleName();

    private static Application instance;
    private Session session;
    private GraphUser user = null;
    private Collection<GraphUser> selectedUsers;

    private ChatManagerService mChatManagerService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        bindService();
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

    public void setSessionCallBack(Session session) {
        this.session = session;
    }

    public GraphUser getUser() {
        return user;
    }

    public void setUser(GraphUser user) {
        this.user = user;
    }

    public void setSelectedUsers(List<GraphUser> selection) {
        this.selectedUsers = selection;

    }

    public Collection<GraphUser> getSelectedUsers() {
        // TODO Auto-generated method stub
        return selectedUsers;
    }

    private void bindService() {
        // Bind to LocalService
        Intent intent = new Intent(this, ChatManagerService.class);
        bindService(intent, mServicConnection, Context.BIND_AUTO_CREATE);
    }

    public ChatManagerService getChatManager() {
        return mChatManagerService;
    }

    private ServiceConnection mServicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChatManagerImpl impl = (ChatManagerImpl) service;
            mChatManagerService = impl.getService();

            Log.d(TAG, "ChatManagerService connected from Application!");
            // Log.d(TAG, "Service connected, login started!");
            // PreferenceEngine prefEngine = PreferenceEngine
            // .getInstance(AfterYouApplication.this);
            //
            // // Get username and password.
            // String userName = prefEngine.getUserName();
            // final String password = prefEngine.getPassword();
            //
            // final String lastLoggedIn = prefEngine.getLastLoggedIn();
            //
            // if (!userName.equals(lastLoggedIn)) {
            // // Clean DB.
            // ContentResolver resolver = getActivity().getContentResolver();
            // resolver.delete(RosterTable.CONTENT_URI, null, null);
            //
            // prefEngine.setLastLoggedIn(userName);
            // }
            //
            // int index = userName.indexOf("@");
            // userName = userName.substring(0, index);
            //
            // mChatManager.login(userName, password, ChatMenuFragment.this,
            // mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mChatManagerService = null;
        }
    };

}
