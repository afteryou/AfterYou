package com.beacon.afterui.sliding.fragment;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.chat.ChatManagerService;
import com.beacon.afterui.chat.ChatManagerService.ChatManagerImpl;
import com.beacon.afterui.chat.MessageListAdapter;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.provider.AfterYouMetadata.MessageTable;
import com.beacon.afterui.provider.AfterYouMetadata.RosterTable;
import com.beacon.afterui.provider.PreferenceEngine;

public class ChatScreenFragment extends BaseActivity implements
        OnClickListener, LoaderCallbacks<Cursor> {

    private static final String TAG = ChatScreenFragment.class.getSimpleName();
    private Button mPostBtn;
    private EditText mMessageEditText;
    private ListView mMessageList;
    // private ChatMessageAdapter mChatMessageAdpter;
    private MessageListAdapter mMessageAdapter;
    private String[] mUserMsg;

    private ChatManagerService mChatManager;

    private String mReceiver;

    private long mRosterId;

    private View mCancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);
        setBehindLeftContentView(R.layout.chat_screen);
        setBehindRightContentView(R.layout.chat_screen);

        mRosterId = getIntent().getLongExtra(AppConstants.ROSTER_ID, -1);

        mPostBtn = (Button) findViewById(R.id.post_btn);
        mMessageEditText = (EditText) findViewById(R.id.write_msg_txt);
        mMessageList = (ListView) findViewById(R.id.chat_msg_list);
        mMessageList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mMessageList.setStackFromBottom(true);

        mCancelButton = findViewById(R.id.cancel_btn_chat_screen);
        mCancelButton.setOnClickListener(this);

        mMessageAdapter = new MessageListAdapter(this, null,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mMessageList.setAdapter(mMessageAdapter);

        mPostBtn.setOnClickListener(this);

        bindService();
        initData();

        getLoaderManager().initLoader(0, null, this);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void initData() {
        final ContentResolver resolver = getContentResolver();

        final String selection = RosterTable._ID + "=?";
        final String[] selectionArgs = { String.valueOf(mRosterId) };

        final Cursor cursor = resolver.query(RosterTable.CONTENT_URI, null,
                selection, selectionArgs, null);

        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }

        mReceiver = cursor.getString(cursor
                .getColumnIndex(RosterTable.USER_NAME));

        cursor.close();
    }

    private void bindService() {
        // Bind to LocalService
        Intent intent = new Intent(this, ChatManagerService.class);
        bindService(intent, mServicConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChatManagerImpl impl = (ChatManagerImpl) service;
            mChatManager = impl.getService();
            mChatManager.openChatSession();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mChatManager = null;
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.post_btn:
            final String text = mMessageEditText.getText().toString().trim();
            if (text.length() > 0) {
                mChatManager.sendMessage(text, mReceiver);
                mMessageEditText.setText("");
            }
            break;

        case R.id.cancel_btn_chat_screen:
            finish();
            break;
        }
    }

    @Override
    public void onDestroy() {
        mChatManager.closeChatSession();
        unbindService(mServicConnection);
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader()");
        final String selection = "(" + MessageTable.SENDER + "=? OR "
                + MessageTable.SENDER + "=?) AND (" + MessageTable.RECEIVER
                + "=? OR " + MessageTable.RECEIVER + "=?)";
        final String sender = PreferenceEngine.getInstance(
                getApplicationContext()).getChatUserName();
        Log.d(TAG, "Sender : " + sender + " Receiver : " + mReceiver);
        final String[] selectionArgs = { sender, mReceiver, mReceiver, sender };
        return new CursorLoader(this, MessageTable.CONTENT_URI, null,
                selection, selectionArgs, MessageTable.TIME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished() : "
                + (cursor == null ? "Cursor is NULL" : "Cursor Length : "
                        + cursor.getCount()));
        mMessageAdapter.swapCursor(cursor);
        mMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
        mMessageAdapter.swapCursor(null);
        mMessageAdapter.notifyDataSetChanged();
    }
}
