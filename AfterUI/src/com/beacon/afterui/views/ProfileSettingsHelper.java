package com.beacon.afterui.views;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.custom.view.wheel.OnWheelChangedListener;
import com.beacon.afterui.custom.view.wheel.WheelView;
import com.beacon.afterui.custom.view.wheel.adapter.ArrayWheelAdapter;
import com.beacon.afterui.custom.view.wheel.adapter.NumericWheelAdapter;
import com.beacon.afterui.utils.Utilities;

public class ProfileSettingsHelper {

    // For date.

    private final static String months[] = new String[] { "January",
            "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December" };

    private static Calendar sCalendar;

    public static void initForBirthDate(final View view, final TextView textView) {

        if (view == null) {
            return;
        }

        final WheelView month = (WheelView) view.findViewById(R.id.month);
        final WheelView year = (WheelView) view.findViewById(R.id.year);
        final WheelView day = (WheelView) view.findViewById(R.id.day);

        month.setViewAdapter(new DateArrayAdapter(view.getContext(), months));

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        year.setViewAdapter(new DateNumericAdapter(view.getContext(),
                curYear - 100, curYear - 15));
        year.setCurrentItem(80);

        updateDays(year, month, day, textView);

        OnWheelChangedListener dateListener = new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day, textView);
            }
        };

        OnWheelChangedListener dayListener = new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                sCalendar.set(Calendar.DAY_OF_MONTH, day.getCurrentItem() + 1);
                // if (textView != null) {
                // String date = Utilities.getDateByCalendar(sCalendar);
                // textView.setText(date);
                // }
            }
        };

        day.addChangingListener(dayListener);
        month.addChangingListener(dateListener);
        year.addChangingListener(dateListener);
    }

    private static void updateDays(WheelView year, WheelView month,
            WheelView day, TextView textView) {
        sCalendar = Calendar.getInstance();
        sCalendar.set(Calendar.YEAR, (sCalendar.get(Calendar.YEAR) - 100)
                + year.getCurrentItem());
        sCalendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = sCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(year.getContext(), 1, maxDays));
        sCalendar.set(Calendar.DAY_OF_MONTH, day.getCurrentItem() + 1);

        // if (textView != null) {
        // String date = Utilities.getDateByCalendar(sCalendar);
        // textView.setText(date);
        // }#cccbca
    }

    public static Calendar getBirthDate() {
        return sCalendar;
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    public static class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;

        // Index of item to be highlighted
        int currentValue;

        private Typeface typeFaceRegular;
        
        private Context mContext;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue) {
            super(context, minValue, maxValue);
            this.currentValue = 0;
            mContext = context;
            typeFaceRegular = Typeface.createFromAsset(context.getAssets(),
                    "fonts/MyriadPro-Regular.otf");
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(typeFaceRegular);
            view.setTextColor(mContext.getResources().getColor(
                    R.color.brown_background));
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    public static class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        private Typeface typeFaceRegular;

        private Context mContext;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items) {
            super(context, items);
            mContext = context;
            this.currentValue = 0;
            setTextSize(16);
            typeFaceRegular = Typeface.createFromAsset(context.getAssets(),
                    "fonts/MyriadPro-Regular.otf");
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(typeFaceRegular);
            view.setTextColor(mContext.getResources().getColor(
                    R.color.brown_background));
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
}
