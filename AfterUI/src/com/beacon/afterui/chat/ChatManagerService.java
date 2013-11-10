package com.beacon.afterui.chat;

import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smackx.packet.VCard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import com.beacon.afterui.R;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.provider.AfterYouMetadata.MessageTable;
import com.beacon.afterui.provider.AfterYouMetadata.RosterTable;
import com.beacon.afterui.provider.PreferenceEngine;

/**
 * @author sushil
 * 
 */
public class ChatManagerService extends Service implements
        org.jivesoftware.smack.RosterListener, ChatManagerListener,
        MessageListener, PacketListener {

    /** TAG */
    private static final String TAG = ChatManagerService.class.getSimpleName();

    private HandlerThread mDeamonThread;

    private HandlerThread mSendMessageThread;

    private DeamonHandler mHandler;

    private SendMessagehandler mSendMessageHandler;

    private final Binder mBinder = new ChatManagerImpl();

    private static ConnectionConfiguration sConnectionConfigurationn;

    private static XMPPConnection sXmppConnection;

    private static final String HOST = "76.74.223.195";

    private static final int PORT = 5222;

    private static final int UPDATE_STATUS = 1;

    private static final int PROCESS_INCOMING_MESSAGE = 2;

    private static final int PROCESS_OUTGOING_MESSAGE = 3;

    private ChatManager mChatManager;

    private boolean isChatSessioOpen = false;

    private String mCurrentChatUser;

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

        mSendMessageThread = new HandlerThread("send_message",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mSendMessageThread.start();
        mSendMessageHandler = new SendMessagehandler(
                mSendMessageThread.getLooper());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class ChatManagerImpl extends Binder {

        public ChatManagerService getService() {
            return ChatManagerService.this;
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
                    PreferenceEngine.getInstance(getApplicationContext())
                            .setChatUserName(userName + "@" + HOST);
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
                    PreferenceEngine.getInstance(getApplicationContext())
                            .setChatUserName(userName + "@" + HOST);
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
                    mChatManager = sXmppConnection.getChatManager();
                    // mChatManager.addOutgoingMessageInterceptor(packetInterceptor,
                    // filter)

                    PacketFilter filter = new MessageTypeFilter(
                            org.jivesoftware.smack.packet.Message.Type.chat);
                    sXmppConnection.addPacketListener(ChatManagerService.this,
                            filter);
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
                    reportRosterFailedStatus(rosterListener, handler);
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
                roster.addRosterListener(ChatManagerService.this);
                reportRosterSuccessStatus(rosterListener, handler);
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

    private void reportRosterFailedStatus(final RosterListener rosterListener,
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
    
    private void reportRosterSuccessStatus(final RosterListener rosterListener,
            final Handler handler) {
        if (rosterListener == null || handler == null) {
            Log.e(TAG, "roster listener or handler is NULL!");
            return;
        }

        handler.post(new Runnable() {

            @Override
            public void run() {
                rosterListener.onRosterDownloaded();
            }
        });
    }

    @Override
    public void onDestroy() {

        if (mDeamonThread != null) {
            mDeamonThread.quit();
        }

        if (mSendMessageThread != null) {
            mSendMessageThread.quit();
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

            case PROCESS_INCOMING_MESSAGE:
                if (msg.obj != null) {
                    org.jivesoftware.smack.packet.Message message = (org.jivesoftware.smack.packet.Message) msg.obj;
                    processIncomingMessages(message);
                }
                break;

            case PROCESS_OUTGOING_MESSAGE:
                if (msg.obj != null) {
                    OutgoingMessage message = (OutgoingMessage) msg.obj;
                    processOutGoingMessage(message);
                }
                break;

            default:
                Log.d(TAG, "Not a valid ID!");
            }
        }
    }

    private class SendMessagehandler extends Handler {
        public SendMessagehandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Get the latest unsent, oldest message from DB and send it across.

            ContentResolver resolver = getContentResolver();
            final String selection = MessageTable.STATUS + "="
                    + MessageTable.MESSAGE_SENDING + " OR "
                    + MessageTable.STATUS + "=" + MessageTable.MESSAGE_FAILED;
            final String[] selectionArgs = null;
            final String[] projection = { MessageTable._ID,
                    MessageTable.RECEIVER, MessageTable.MESSAGE };

            final Cursor cursor = resolver.query(MessageTable.CONTENT_URI,
                    projection, selection, selectionArgs, MessageTable.TIME
                            + " ASC");

            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            Chat chat = null;
            String oldReceiver = "";
            do {

                final int messageId = cursor.getInt(cursor
                        .getColumnIndex(MessageTable._ID));
                final String receiver = cursor.getString(cursor
                        .getColumnIndex(MessageTable.RECEIVER));
                final String message = cursor.getString(cursor
                        .getColumnIndex(MessageTable.MESSAGE));

                if (chat == null) {
                    chat = mChatManager.createChat(receiver, null);
                } else if (!receiver.equals(oldReceiver)) {
                    chat = mChatManager.createChat(receiver, null);
                }
                oldReceiver = receiver;

                int messageStatus;
                try {
                    chat.sendMessage(message);
                    messageStatus = MessageTable.MESSAGE_SUCCESS;
                } catch (XMPPException e) {
                    Log.e(TAG,
                            "Error while sending the message : "
                                    + e.getMessage());
                    messageStatus = MessageTable.MESSAGE_FAILED;
                }

                final ContentValues values = new ContentValues();
                values.put(MessageTable.STATUS, String.valueOf(messageStatus));
                final String where = MessageTable._ID + "=?";
                final String[] selectionArguments = { String.valueOf(messageId) };

                // Update the status in DB.
                resolver.update(MessageTable.CONTENT_URI, values, where,
                        selectionArguments);

            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    private MessageListener mMessageListener = new MessageListener() {

        @Override
        public void processMessage(Chat chat,
                org.jivesoftware.smack.packet.Message message) {
            if (true) {
                Log.d(TAG, "--From : " + message.getFrom());
                Log.d(TAG, "--To : " + message.getTo());
                Log.d(TAG, "--body    : " + message.getBody());
            }
            Message msg = mHandler.obtainMessage();
            msg.obj = message;
            msg.what = PROCESS_INCOMING_MESSAGE;
            mHandler.sendMessage(msg);
        }
    };

    public void openChatSession(final String currentChatUser) {
        isChatSessioOpen = true;
        if (currentChatUser == null) {
            throw new IllegalArgumentException("Chat user can not be NULL!");
        }
        mCurrentChatUser = currentChatUser;
    }

    public void closeChatSession() {
        isChatSessioOpen = false;
        mCurrentChatUser = null;
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
        values.put(MessageTable.TIME, System.currentTimeMillis());

        int readStatus;
        if (mCurrentChatUser == null || !mCurrentChatUser.equals(from)) {
            readStatus = MessageTable.MESSAGE_UNREAD;
        } else {
            readStatus = MessageTable.MESSAGE_READ;
        }
        values.put(MessageTable.READ_STATUS, readStatus);
        values.put(MessageTable.STATUS, MessageTable.MESSAGE_SUCCESS);

        final ContentResolver resolver = getContentResolver();
        resolver.insert(MessageTable.CONTENT_URI, values);
        
        PendingIntent pdSent = PendingIntent.getBroadcast(this, 0, new Intent(AppConstants.NOTIFICATION_SENT).putExtra(AppConstants.SENDER, from), 0);
        
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification noti = new Notification.Builder(this).setAutoCancel(true).setContentTitle(getString(R.string.IDS_NOTIFY_STRING) + from).setContentText(message.getBody()).setSmallIcon(R.drawable.logo).setContentIntent(pdSent).build(); 
        nm.notify(AppConstants.NOTIFICATION_ID, AppConstants.NOTIFICATION_INT, noti);
    }

    private void processOutGoingMessage(final OutgoingMessage outgoingMessage) {

        if (outgoingMessage == null) {
            return;
        }

        final ContentValues values = new ContentValues();

        values.put(MessageTable.MESSAGE, outgoingMessage.message);

        values.put(MessageTable.SENDER,
                PreferenceEngine.getInstance(getApplicationContext())
                        .getChatUserName());
        values.put(MessageTable.RECEIVER, outgoingMessage.to);
        values.put(MessageTable.TIME, System.currentTimeMillis());
        values.put(MessageTable.READ_STATUS, MessageTable.MESSAGE_READ);
        values.put(MessageTable.STATUS, MessageTable.MESSAGE_SENDING);

        final ContentResolver resolver = getContentResolver();
        resolver.insert(MessageTable.CONTENT_URI, values);

        mSendMessageHandler.sendEmptyMessage(1);
    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        Log.d(TAG, "chatCreated() : " + createdLocally);
        if (!createdLocally) {
            chat.addMessageListener(ChatManagerService.this);
        }
    }

    @Override
    public void processMessage(Chat chat,
            org.jivesoftware.smack.packet.Message message) {
        if (true) {
            Log.d(TAG, "From : " + message.getFrom());
            Log.d(TAG, "To : " + message.getTo());
            Log.d(TAG, "body    : " + message.getBody());
        }
        Message msg = mHandler.obtainMessage();
        msg.obj = message;
        msg.what = PROCESS_INCOMING_MESSAGE;
        msg.arg1 = MessageTable.MESSAGE_UNREAD;
        mHandler.sendMessage(msg);
    }

    private class OutgoingMessage {
        public String message;
        public String to;
    }

    public void sendMessage(final String message, final String to) {

        OutgoingMessage outgoingMessage = new OutgoingMessage();
        outgoingMessage.message = message;
        outgoingMessage.to = to;

        Message msg = mHandler.obtainMessage();
        msg.what = PROCESS_OUTGOING_MESSAGE;
        msg.obj = outgoingMessage;
        mHandler.sendMessage(msg);
    }

    @Override
    public void processPacket(Packet packet) {
        if (packet instanceof org.jivesoftware.smack.packet.Message) {
            Message msg = mHandler.obtainMessage();
            msg.obj = packet;
            msg.what = PROCESS_INCOMING_MESSAGE;
            msg.arg1 = MessageTable.MESSAGE_UNREAD;
            mHandler.sendMessage(msg);
        }
    }
}
