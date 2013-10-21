package com.beacon.afterui.chat;

import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smackx.packet.VCard;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.beacon.afterui.provider.AfterYouMetadata.MessageTable;
import com.beacon.afterui.provider.AfterYouMetadata.RosterTable;

/**
 * @author sushil
 * 
 */
public class ChatManager extends Service implements
        org.jivesoftware.smack.RosterListener, ChatManagerListener,
        MessageListener {

    /** TAG */
    private static final String TAG = ChatManager.class.getSimpleName();

    private HandlerThread mDeamonThread;

    private DeamonHandler mHandler;

    private final Binder mBinder = new ChatManagerImpl();

    private static ConnectionConfiguration sConnectionConfigurationn;

    private static XMPPConnection sXmppConnection;

    private static final String HOST = "76.74.223.195";

    private static final int PORT = 5222;

    private static final int UPDATE_STATUS = 1;

    private static final int PROCESS_MESSAGE = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate:ChatManager");

        sConnectionConfigurationn = new ConnectionConfiguration(HOST, PORT,
                ProxyInfo.forDefaultProxy());

        mDeamonThread = new HandlerThread("chat_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mDeamonThread.start();
        mHandler = new DeamonHandler(mDeamonThread.getLooper());
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

                if (xmppConnection.isAuthenticated()) {
                    reportLoginStatus(loginListener, handler,
                            ChatConstants.LOGIN_SUCCESS);
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

                    // Also init chat manager.
                    sXmppConnection.getChatManager().addChatListener(
                            ChatManager.this);
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

                final boolean DEBUG = false;

                for (RosterEntry rosterObject : rosterList) {

                    final ContentValues values = new ContentValues();

                    if (DEBUG) {
                        Log.d(TAG, "Name   : " + rosterObject.getName());
                        Log.d(TAG, "Jid    : " + rosterObject.getUser());
                        Log.d(TAG, "Type   : " + rosterObject.getType());
                    }
                    values.put(RosterTable.NAME, rosterObject.getName());
                    values.put(RosterTable.USER_NAME, rosterObject.getUser());
                    values.put(RosterTable.SUBSCRIPTION_TYPE, rosterObject
                            .getType().toString());
                    String user = rosterObject.getUser();

                    Presence presence = roster.getPresence(user);

                    if (DEBUG) {
                        Log.d(TAG, "USer : " + user);
                        Log.d(TAG, "Status : " + presence.getStatus());
                        Log.d(TAG, "Type   : " + presence.getType().toString());
                        if (presence.getMode() != null) {
                            Log.d(TAG, "Mode : "
                                    + presence.getMode().toString());
                        } else {
                            Log.d(TAG, "Mode IS NULL!");
                        }
                    }

                    if (presence.getType() != null) {

                        if (presence.getType().toString()
                                .equalsIgnoreCase(ChatConstants.AVAILABLE)) {

                            if (presence.getMode() == null) {
                                values.put(RosterTable.STATUS, presence
                                        .getType().toString());
                            } else {
                                values.put(RosterTable.STATUS, presence
                                        .getMode().toString());
                            }
                        }
                    }
                    values.put(RosterTable.STATUS_TEXT, presence.getStatus());

                    VCard vcard = new VCard();

                    try {
                        vcard.load(sXmppConnection, user);
                        byte[] photo = vcard.getAvatar();

                        if (photo != null && photo.length > 0) {
                            boolean saved = ChatUtils.savePhoto(
                                    getApplicationContext(), user, photo);
                            if (saved) {
                                RosterPhotoManager.getPhotoManager()
                                        .resetPhotoFor(user);
                                values.put(RosterTable.AVATAR, user);
                            }
                        }

                        ContentResolver resolver = getContentResolver();
                        if (isUserPresent(user)) {
                            // Update
                            final String selection = RosterTable.USER_NAME
                                    + "=?";
                            final String[] selectionArgs = { user };

                            resolver.update(RosterTable.CONTENT_URI, values,
                                    selection, selectionArgs);

                        } else {
                            // Insert
                            resolver.insert(RosterTable.CONTENT_URI, values);
                        }
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "Roster updated to DB!");
                roster.addRosterListener(ChatManager.this);
            }
        }).start();
    }

    private boolean isUserPresent(final String user) {

        final String selection = RosterTable.USER_NAME + "=?";
        final String[] selectionArgs = { user };
        final String[] projection = null/* { RosterTable._ID } */;

        final ContentResolver resolver = getContentResolver();
        final Cursor cursor = resolver.query(RosterTable.CONTENT_URI,
                projection, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }

        return false;
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

    @Override
    public void entriesAdded(Collection<String> addresses) {
        Log.d(TAG, "entriesAdded()");
    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {
        Log.d(TAG, "entriesUpdated()");
    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {
        Log.d(TAG, "entriesDeleted()");
    }

    @Override
    public void presenceChanged(Presence presence) {
        Log.d(TAG, "presenceChanged()");
        Message msg = mHandler.obtainMessage();
        msg.obj = presence;
        msg.what = UPDATE_STATUS;
        mHandler.sendMessage(msg);
    }

    private class DeamonHandler extends Handler {
        public DeamonHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
            case UPDATE_STATUS:
                if (msg.obj != null) {
                    Presence presence = (Presence) msg.obj;
                    updatePresence(presence);
                }
                break;

            case PROCESS_MESSAGE:
                if (msg.obj != null) {
                    org.jivesoftware.smack.packet.Message message = (org.jivesoftware.smack.packet.Message) msg.obj;
                    processIncomingMessages(message);
                }
                break;

            default:
                Log.d(TAG, "Not a valid ID!");
            }
        }
    }

    private String getUserName(final String userName) {
        String from = userName;
        int index = from.indexOf("/");

        if (index > 0) {
            from = from.substring(0, index);
        }

        return from;
    }

    private void updatePresence(final Presence presence) {
        if (presence == null) {
            return;
        }

        String from = getUserName(presence.getFrom());
        final ContentValues values = new ContentValues();

        if (presence.getType() != null) {

            if (presence.getType().toString()
                    .equalsIgnoreCase(ChatConstants.AVAILABLE)) {

                if (presence.getMode() == null) {
                    values.put(RosterTable.STATUS, presence.getType()
                            .toString());
                } else {
                    values.put(RosterTable.STATUS, presence.getMode()
                            .toString());
                }
            } else if (presence.getType().toString()
                    .equalsIgnoreCase(ChatConstants.UN_AVAILABLE)) {
                values.put(RosterTable.STATUS, presence.getType().toString());
            }
        }
        values.put(RosterTable.STATUS_TEXT, presence.getStatus());

        // Update photo.
        VCard vcard = new VCard();

        try {
            vcard.load(sXmppConnection, from);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        byte[] photo = vcard.getAvatar();

        if (photo != null && photo.length > 0) {
            ChatUtils.savePhoto(getApplicationContext(), from, photo);
            RosterPhotoManager.getPhotoManager().resetPhotoFor(from);
        }

        final ContentResolver resolver = getContentResolver();
        final String selection = RosterTable.USER_NAME + "=?";
        final String[] selectionArgs = { from };

        resolver.update(RosterTable.CONTENT_URI, values, selection,
                selectionArgs);
    }

    private void processIncomingMessages(
            final org.jivesoftware.smack.packet.Message message) {
        if (message == null) {
            return;
        }

        final ContentValues values = new ContentValues();

        values.put(MessageTable.MESSAGE, message.getBody());

        final String from = getUserName(message.getFrom());
        values.put(MessageTable.SENDER, from);
        final String to = getUserName(message.getTo());
        values.put(MessageTable.RECEIVER, to);
        values.put(MessageTable.READ_STATUS, MessageTable.MESSAGE_UNREAD);
        values.put(MessageTable.STATUS, MessageTable.MESSAGE_SUCCESS);

        final ContentResolver resolver = getContentResolver();
        resolver.insert(MessageTable.CONTENT_URI, values);
    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        Log.d(TAG, "chatCreated() : " + createdLocally);
        if (!createdLocally) {
            chat.addMessageListener(ChatManager.this);
        }
    }

    @Override
    public void processMessage(Chat chat,
            org.jivesoftware.smack.packet.Message message) {
        Log.d(TAG, "From : " + message.getFrom());
        Log.d(TAG, "To : " + message.getTo());
        Log.d(TAG, "body    : " + message.getBody());
        Message msg = mHandler.obtainMessage();
        msg.obj = message;
        msg.what = PROCESS_MESSAGE;
        mHandler.sendMessage(msg);
    }
}
