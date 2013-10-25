package com.beacon.afterui.chat;

import com.beacon.afterui.R;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.provider.AfterYouMetadata.MessageTable;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MessageListAdapter extends CursorAdapter {

    /** TAG */
    private static final String TAG = MessageListAdapter.class.getSimpleName();

    private RosterPhotoManager mRosterPhotoManager;

    private LayoutInflater mLayoutInflator;

    private static final int VIEW_TYPE_LEFT = 0;

    private static final int VIEW_TYPE_RIGHT = 1;

    private static final int VIEW_TYPE_COUNT = 2;

    private String mLoggedInUser;

    public MessageListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mLayoutInflator = LayoutInflater.from(context);
        mRosterPhotoManager = RosterPhotoManager.getPhotoManager();
        mLoggedInUser = PreferenceEngine.getInstance(context).getChatUserName();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final String user = cursor.getString(cursor
                .getColumnIndex(MessageTable.SENDER));
        TextView messageTextView;
        if (user.equals(mLoggedInUser)) {
            messageTextView = (TextView) view.findViewById(R.id.user_msg);
        } else {
            messageTextView = (TextView) view.findViewById(R.id.friend_msg);
        }

        final String message = cursor.getString(cursor
                .getColumnIndex(MessageTable.MESSAGE));

        messageTextView.setText(message.trim());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = null;

        final String user = cursor.getString(cursor
                .getColumnIndex(MessageTable.SENDER));
        if (user.equals(mLoggedInUser)) {
            view = mLayoutInflator.inflate(
                    R.layout.chat_screen_right_text_item, null);
        } else {
            view = mLayoutInflator.inflate(R.layout.chat_screen_left_text_item,
                    null);
        }

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = getCursor();

        if (cursor != null) {
            cursor.moveToPosition(position);
            final String user = cursor.getString(cursor
                    .getColumnIndex(MessageTable.SENDER));
            if (user.equals(mLoggedInUser)) {
                return VIEW_TYPE_RIGHT;
            } else {
                return VIEW_TYPE_LEFT;
            }
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
