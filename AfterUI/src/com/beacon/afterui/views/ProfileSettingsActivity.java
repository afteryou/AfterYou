package com.beacon.afterui.views;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomDialog;
import com.beacon.afterui.utils.customviews.CustomerDatePickDialog;


public class ProfileSettingsActivity extends BaseActivity{
	
	
	private Calendar mCalendar;
	
	private Context ctx;
	
	private CharSequence[] religion;

	private Dialog mReligion;	
	private int mCurrentRlg = 0;
	private int mPreviousRlg =0;



	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_setup);
		ctx = this;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		mCalendar = Calendar.getInstance();
		initView();
		if(religion == null)
		{
			religion = getResources().getStringArray(R.array.religion_choices);
		}
	}

	private void initView() {
		
		String birthday = PreferenceEngine.getInstance(this).getBirthday();
		TextView b_day = (TextView)findViewById(R.id.birthday_edit_text);
		b_day.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDatePickDialog().show();
			}
		});
		if(birthday != null)
		{
			b_day.setText(birthday);
		}
		String religion_txt = PreferenceEngine.getInstance(this).getSelfReligion();
		TextView rlg = (TextView)findViewById(R.id.religion_edit_text);
		rlg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getSortDialog().show();
				
			}
		});
		if(religion_txt != null)
		{
			rlg.setText(religion_txt);
			mCurrentRlg = PreferenceEngine.getInstance(this).getSelfReligionInt();
		}
	}
	
	private Dialog getSortDialog(){
		
		 CustomDialog.Builder builder = new CustomDialog.Builder(this,
					new AfterYouDialogImpl.AfterYouBuilderImpl(this));
		
		mReligion = builder.setTitle(this.getString(R.string.religion_label))
        .setSingleChoiceItems(religion, mCurrentRlg, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	mCurrentRlg = whichButton;    
            }
        })
        .setPositiveButton(this.getString(R.string.IDS_OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {       	
            	
            	mPreviousRlg = mCurrentRlg;
            	changeSelfReligion(mPreviousRlg,mCurrentRlg);
            }
        })
        .setNegativeButton(this.getString(R.string.IDS_CANCEL), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	mCurrentRlg = mPreviousRlg;
               
            }
        })
       .create();
		mReligion.setOnCancelListener(new OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentRlg = mPreviousRlg;
			}
		});
	
	return mReligion;
}
	
	protected void changeSelfReligion(int mPreviousRlg2, int mCurrentRlg2) {
		String selectedReligion = religion[mCurrentRlg2].toString();
		PreferenceEngine.getInstance(ctx).setSelfReligion(selectedReligion);
		PreferenceEngine.getInstance(ctx).setSelfReligionInt(mCurrentRlg2);
		initView();
	}

	private Dialog getDatePickDialog() {
		return new CustomerDatePickDialog(new AfterYouDialogImpl(this), this,
				R.style.Theme_CustomDialog, mDateSetListener, mCalendar
				.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar
				.get(Calendar.DATE));
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
				mCalendar.set(year, monthOfYear, dayOfMonth);
				String date = Utilities.getDateByCalendar(mCalendar);
				PreferenceEngine.getInstance(ctx).saveBirthday(date);
				initView();
		}
	};
}