package com.beacon.afterui.utils.customviews;

import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.Utilities;

public class CustomerDatePickDialog extends CustomDialog implements
        OnClickListener, OnDateChangedListener {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private DatePicker mDatePicker;
    private OnDateSetListener mCallBack;
    private java.text.DateFormat mTitleDateFormat;
    private int mInitialYear;
    private int mInitialMonth;
    private int mInitialDay;
    private Calendar titleCalendar;

    private void init(Context context,
            OnDateSetListener callBack, int year, int monthOfYear,
            int dayOfMonth) {
        mCallBack = callBack;
        mInitialYear = year;
        mInitialMonth = monthOfYear;
        mInitialDay = dayOfMonth;
        mTitleDateFormat = java.text.DateFormat
                .getDateInstance(java.text.DateFormat.FULL);
        setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.IDS_SET), this);
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.IDS_CANCEL),(OnClickListener) null);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_date_picker_dialog, null);
        setView(view);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mDatePicker.init(mInitialYear, mInitialMonth, mInitialDay, this);
        mDatePicker.setCalendarViewShown(false);
        titleCalendar = Calendar.getInstance();
        titleCalendar.set(Calendar.YEAR, year);
        titleCalendar.set(Calendar.MONTH, monthOfYear);
        titleCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setTitle(Utilities.formatDate(titleCalendar.getTime()));
    	
    }

    public CustomerDatePickDialog(IDialogImpl dlg, Context context, int theme,
            OnDateSetListener callBack, int year, int monthOfYear,
            int dayOfMonth) {
        super(dlg, context, theme);
        init(context, callBack,year, monthOfYear, dayOfMonth);
    }
    
    public CustomerDatePickDialog(IDialogImpl dlg, Context context,
            OnDateSetListener callBack, int year, int monthOfYear,
            int dayOfMonth) {
        super(dlg, context);
        init(context, callBack,year, monthOfYear, dayOfMonth);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (mCallBack != null) {
            mDatePicker.clearFocus();
            mCallBack.onDateSet(mDatePicker, mDatePicker.getYear(),
                    mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
        }
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        mInitialYear = year;
        mInitialMonth = monthOfYear;
        mInitialDay = dayOfMonth;
        mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
    }

    private void updateTitle(int year, int month, int day) {
        titleCalendar.set(Calendar.YEAR, year);
        titleCalendar.set(Calendar.MONTH, month);
        titleCalendar.set(Calendar.DAY_OF_MONTH, day);
        setTitle(mTitleDateFormat.format(titleCalendar.getTime()));
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(YEAR, mDatePicker.getYear());
        state.putInt(MONTH, mDatePicker.getMonth());
        state.putInt(DAY, mDatePicker.getDayOfMonth());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int year = savedInstanceState.getInt(YEAR);
        int month = savedInstanceState.getInt(MONTH);
        int day = savedInstanceState.getInt(DAY);
        mDatePicker.init(year, month, day, this);
        updateTitle(year, month, day);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        titleCalendar.set(Calendar.YEAR, year);
        titleCalendar.set(Calendar.MONTH, month);
        titleCalendar.set(Calendar.DAY_OF_MONTH, day);
        setTitle(Utilities.formatDate(titleCalendar.getTime()));
    }

}
