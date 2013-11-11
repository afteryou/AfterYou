package com.beacon.afterui.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class StartChatService extends BroadcastReceiver {

    /** TAG */
    private static final String TAG = StartChatService.class.getSimpleName();

    public static final int BOOT_COMPLETED = 1;

    public static final int NETWORK_CHANGE = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() : " + intent.getAction());

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            startChatManagerService(context, BOOT_COMPLETED);
        } else if (intent.getAction().equals(
                ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            Log.d(TAG, "No Connectivity : " + noConnectivity);
            if (!noConnectivity) {
                startChatManagerService(context, NETWORK_CHANGE);
            }
        }
    }

    private void startChatManagerService(final Context context, final int type) {
        Intent intent = new Intent(context, ChatManagerService.class);
        context.startService(intent);
        intent.putExtra("type", type);
    }
}
