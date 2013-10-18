package com.beacon.afterui.chat;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.proxy.ProxyInfo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

/**
 * @author sushil
 * 
 */
public class ChatManager extends Service {

    /** TAG */
    private static final String TAG = ChatManager.class.getSimpleName();

    private HandlerThread mDeamonThread;

    private Handler mHandler;

    private final Binder mBinder = new ChatManagerImpl();

    /** Login listener. */
    private LoginListener mLoginListener;

    private static ConnectionConfiguration sConnectionConfigurationn;

    private static XMPPConnection sXmppConnection;

    private static final String HOST = "76.74.223.195";

    private static final int PORT = 5222;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate:ChatManager");

        sConnectionConfigurationn = new ConnectionConfiguration(HOST, PORT,
                ProxyInfo.forDefaultProxy());

        mDeamonThread = new HandlerThread("chat_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mDeamonThread.start();
        mHandler = new Handler(mDeamonThread.getLooper());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class ChatManagerImpl extends Binder {

        public ChatManager getService() {
            return ChatManager.this;
        }
    }

    synchronized private XMPPConnection initXmpp() {
        if (sXmppConnection == null) {
            sXmppConnection = new XMPPConnection(sConnectionConfigurationn);
        }

        return sXmppConnection;
    }

    /**
     * Method for logging into chat.
     * 
     * This is an asynchronous method. Callback will be delivered using handler.
     * 
     * @param userName
     * @param password
     * @param loginListener
     * @param handler
     */
    synchronized public void login(final String userName,
            final String password, final LoginListener loginListener,
            final Handler handler) {

        new Thread(new Runnable() {

            @Override
            public void run() {

                if (userName == null || password == null) {
                    Log.e(TAG, "username or password is NULL!");
                    reportLoginStatus(loginListener, handler,
                            ChatConstants.USERNAME_PASSWORD_NULL);
                    return;
                }

                XMPPConnection xmppConnection = initXmpp();

                if (xmppConnection == null) {
                    Log.e(TAG, "XMPP connection is NULL!");
                    reportLoginStatus(loginListener, handler,
                            ChatConstants.XMPP_NULL);
                    return;
                }

                try {
                    xmppConnection.connect();
                } catch (XMPPException e) {
                    reportLoginStatus(loginListener, handler,
                            ChatConstants.XMPP_CONNECTION_FAILED);
                    return;
                }

                if (xmppConnection.isConnected()) {
                    Log.i(TAG, "Login done!");
                    try {
                        xmppConnection.login(userName, password);
                    } catch (XMPPException e) {
                        reportLoginStatus(loginListener, handler,
                                ChatConstants.LOGIN_FAILED);
                        return;
                    }
                    reportLoginStatus(loginListener, handler,
                            ChatConstants.LOGIN_SUCCESS);
                } else {
                    Log.e(TAG, "XMPP is not connected!");
                    reportLoginStatus(loginListener, handler,
                            ChatConstants.XMPP_NOT_CONNECTED);
                }
            }
        }).start();
    }

    private void reportLoginStatus(final LoginListener loginListener,
            final Handler handler, final int statusCode) {
        if (loginListener == null || handler == null) {
            Log.e(TAG, "login listener or handle is NULL");
            return;
        }
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (ChatConstants.LOGIN_SUCCESS == statusCode) {
                    loginListener.onLoginSuccess();
                } else {
                    loginListener.onLoginFailed(statusCode);
                }
            }
        });
    }

    synchronized public void updateRosterInDb(
            final RosterListener rosterListener, final Handler handler) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                if (sXmppConnection == null) {
                    throw new IllegalStateException(
                            "xmpp connection is null, please login!");
                }

                if (!sXmppConnection.isAuthenticated()) {
                    throw new IllegalStateException("User is not logged in!");
                }

                Roster roster = sXmppConnection.getRoster();

                if (roster == null) {
                    Log.e(TAG, "Roster is NULL");
                    reportRosterStatus(rosterListener, handler);
                    return;
                }

                Collection<RosterEntry> rosterList = roster.getEntries();
                for (RosterEntry rosterObject : rosterList) {
                    Log.d(TAG, "Name : " + rosterObject.getName() + " "
                            + rosterObject.getUser());
                    
                }
            }
        }).start();
    }

    private void reportRosterStatus(final RosterListener rosterListener,
            final Handler handler) {
        if (rosterListener == null || handler == null) {
            Log.e(TAG, "roster listener or handler is NULL!");
            return;
        }

        handler.post(new Runnable() {

            @Override
            public void run() {
                rosterListener.onRosterFailed();
            }
        });
    }

    @Override
    public void onDestroy() {

        if (mDeamonThread != null) {
            mDeamonThread.quit();
        }
        Log.d(TAG, "ChatManager service is getting destroyed!");
        super.onDestroy();
    }

}
