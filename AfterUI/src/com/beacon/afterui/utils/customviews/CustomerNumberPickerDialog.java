package com.beacon.afterui.utils.customviews;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import com.beacon.afterui.R;

public class CustomerNumberPickerDialog extends CustomDialog implements
        OnClickListener, OnValueChangeListener {

    private NumberPicker mNumberPicker;
    private OnValueChangeListener mCallBack;
    private int oldValue, newValue;
    String NUMBER_KEY ="picker_value";

    private void init(Context context,
    		OnValueChangeListener callBack,String[] displayedValues, int currentVlue) {
        mCallBack = callBack;
        setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.IDS_SET), this);
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.IDS_CANCEL),(OnClickListener) null);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_number_picker_dialog, null);
        setView(view);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        mNumberPicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				oldValue = oldVal;
				newValue = newVal;
				
			}
		});
        if(displayedValues != null)
        {
        	mNumberPicker.setMaxValue(displayedValues.length -1);
        	mNumberPicker.setMinValue(0);
        	mNumberPicker.setDisplayedValues(displayedValues);
        	mNumberPicker.setWrapSelectorWheel(false);
        	mNumberPicker.setValue(currentVlue);
        	mNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        }
        setTitle(context.getString(R.string.height_label));
    	
    }

    public CustomerNumberPickerDialog(IDialogImpl dlg, Context context, int theme,
            OnValueChangeListener callBack, String[] displayedValues, int currentVlue) {
        super(dlg, context, theme);
        init(context, callBack,displayedValues,currentVlue);
    }
    
    public CustomerNumberPickerDialog(IDialogImpl dlg, Context context,
            OnValueChangeListener callBack) {
        super(dlg, context);
        init(context, callBack,null, 0);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (mCallBack != null) {
            mNumberPicker.clearFocus();
            mCallBack.onValueChange(mNumberPicker, oldValue, newValue);
        }
    }



    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(NUMBER_KEY, mNumberPicker.getValue());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int new_value = savedInstanceState.getInt(NUMBER_KEY);
        mNumberPicker.setValue(new_value);
    }


	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		this.oldValue = oldVal;
		this.newValue = newVal;
		
	}

}
