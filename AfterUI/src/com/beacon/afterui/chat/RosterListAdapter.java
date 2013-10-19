package com.beacon.afterui.chat;

import com.beacon.afterui.R;
import com.beacon.afterui.provider.AfterYouMetadata.RosterTable;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RosterListAdapter extends CursorAdapter {

    /** TAG */
    private static final String TAG = RosterListAdapter.class.getSimpleName();

    private LayoutInflater mLayoutInflator;

    public RosterListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mLayoutInflator = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final String userName = cursor.getString(cursor
                .getColumnIndex(RosterTable.NAME));
        final TextView nameTextView = (TextView) view
                .findViewById(R.id.chat_user_name);
        nameTextView.setText(userName);

        final ImageView userStatusImageView = (ImageView) view
                .findViewById(R.id.user_status);

        final String userStatus = cursor.getString(cursor
                .getColumnIndex(RosterTable.STATUS));

        if (ChatConstants.UN_AVAILABLE.equalsIgnoreCase(userStatus)) {
            userStatusImageView.setImageResource(R.drawable.un_available);
        } else if (ChatConstants.AVAILABLE.equalsIgnoreCase(userStatus)
                || ChatConstants.CHAT.equalsIgnoreCase(userStatus)) {
            userStatusImageView.setImageResource(R.drawable.available);
        } else if (ChatConstants.AWAY.equalsIgnoreCase(userStatus)
                || ChatConstants.EXTENDED_AWAY.equalsIgnoreCase(userStatus)) {
            userStatusImageView.setImageResource(R.drawable.away);
        } else if (ChatConstants.DO_NOT_DISTURB.equalsIgnoreCase(userStatus)) {
            userStatusImageView.setImageResource(R.drawable.do_not_disturb);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflator.inflate(R.layout.chat_view_item, null,
                false);

        final TextView nameTextView = (TextView) view
                .findViewById(R.id.chat_user_name);
        final Typeface typeFaceSemiBold = Typeface.createFromAsset(
                context.getAssets(), "fonts/MyriadPro-Semibold.otf");
        nameTextView.setTypeface(typeFaceSemiBold);

        return view;
    }
}
