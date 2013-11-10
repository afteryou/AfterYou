package com.beacon.afterui.utils;

import android.app.Activity;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

public class AnalyticsUtils {

    public static void logScreenEvent(final Activity activity,
            final String screenName) {

        if (activity == null || screenName == null) {
            return;
        }

        EasyTracker easyTracker = EasyTracker.getInstance(activity);

        easyTracker.set(Fields.SCREEN_NAME, screenName);

        easyTracker.send(MapBuilder.createAppView().build());
    }

    public static void logButtonPressEvent(final Activity activity,
            final String eventLabel, final long eventValue) {
        if (activity == null || eventLabel == null) {
            return;
        }

        EasyTracker easyTracker = EasyTracker.getInstance(activity);
        easyTracker.send(MapBuilder.createEvent(
                AnalyticsConstants.CAT_UI_ACTION, // Event category
                AnalyticsConstants.CAT_BUTTON_PRESS, // Event action
                eventLabel, // Event label
                eventValue) // Event value
                .build());
    }

}
