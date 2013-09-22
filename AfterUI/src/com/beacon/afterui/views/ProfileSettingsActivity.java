package com.beacon.afterui.views;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomDialog;
import com.beacon.afterui.utils.customviews.CustomerDatePickDialog;
import com.beacon.afterui.utils.customviews.ErrorDialog;

public class ProfileSettingsActivity extends BaseActivity implements OnClickListener{

	private Calendar mCalendar;

	private Context ctx;

	private CharSequence[] religion;
	private CharSequence[] relation;
	private CharSequence[] hvChld;
	
	private Dialog mReligion;
	private int mCurrentRlg = 0;
	private int mPreviousRlg = 0;
	
	private Dialog mRelation;
	private int mCurrentRltn = 0;
	private int mPreviousRltn = 0;
	
	private Dialog mHaveChild;
	private int mCurrentHvChld = 0;
	private int mPreviousHvChld = 0;
	
	TextView b_day,rlg,rtln,hvChild;

	private ImageButton mDoneBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_setup);
		mDoneBtn = (ImageButton)findViewById(R.id.donebtn);
		mDoneBtn.setOnClickListener(this);
		ctx = this;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCalendar = Calendar.getInstance();
		initView();
		if (religion == null) {
			religion = getResources().getStringArray(R.array.religion_choices);
		}
		if(relation == null)
		{
			relation = getResources().getStringArray(R.array.relation_choices);
		}
		if(hvChld == null)
		{
			hvChld = getResources().getStringArray(R.array.selfhvChld_choices);
		}
	}

	private void initView() {

		String birthday = PreferenceEngine.getInstance(this).getBirthday();
		b_day = (TextView) findViewById(R.id.birthday_edit_text);
		b_day.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDatePickDialog().show();
			}
		});
		if (birthday != null) {
			b_day.setText(birthday);
		}
		String religion_txt = PreferenceEngine.getInstance(this)
				.getSelfReligion();
		rlg = (TextView) findViewById(R.id.religion_edit_text);
		rlg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getReligionDialog().show();

			}
		});
		if (religion_txt != null) {
			rlg.setText(religion_txt);
			mCurrentRlg = PreferenceEngine.getInstance(this)
					.getSelfReligionInt();
		}

		String relation_txt = PreferenceEngine.getInstance(this)
				.getSelfRelation();
		rtln = (TextView) findViewById(R.id.relation_edit_text);
		rtln.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRelationDialog().show();

			}
		});
		if (relation_txt != null) {
			rtln.setText(relation_txt);
			mCurrentRltn = PreferenceEngine.getInstance(this)
					.getSelfRelationInt();
		}
		
		boolean havechild_txt = PreferenceEngine.getInstance(this)
				.getHaveChildren();
		hvChild = (TextView) findViewById(R.id.havechild_edit_text);
		hvChild.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getHaveChildDialog().show();

			}
		});
		if (havechild_txt) {
			hvChild.setText(getResources().getString(R.string.IDS_YES));
			mCurrentHvChld = 0;
		}else{
			hvChild.setText(getResources().getString(R.string.IDS_NO));
			mCurrentHvChld = 1;
		}
	}

	protected Dialog getHaveChildDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mHaveChild = builder
				.setTitle(this.getString(R.string.havechild_label))
				.setSingleChoiceItems(hvChld, mCurrentHvChld,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentHvChld = whichButton;
							}
						})
				.setPositiveButton(this.getString(R.string.IDS_OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								mPreviousHvChld = mCurrentHvChld;
								changeSelfHaveChild(mPreviousHvChld, mCurrentHvChld);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentHvChld = mPreviousHvChld;

							}
						}).create();
		mHaveChild.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentHvChld = mPreviousHvChld;
			}
		});

		return mHaveChild;
	}

	protected Dialog getRelationDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mRelation = builder
				.setTitle(this.getString(R.string.relation_label))
				.setSingleChoiceItems(relation, mCurrentRltn,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentRltn = whichButton;
							}
						})
				.setPositiveButton(this.getString(R.string.IDS_OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								mPreviousRltn = mCurrentRltn;
								changeSelfRelation(mPreviousRltn, mCurrentRltn);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentRltn = mPreviousRltn;

							}
						}).create();
		mRelation.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentRltn = mPreviousRltn;
			}
		});

		return mRelation;
	}

	private Dialog getReligionDialog() {

		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mReligion = builder
				.setTitle(this.getString(R.string.religion_label))
				.setSingleChoiceItems(religion, mCurrentRlg,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentRlg = whichButton;
							}
						})
				.setPositiveButton(this.getString(R.string.IDS_OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								mPreviousRlg = mCurrentRlg;
								changeSelfReligion(mPreviousRlg, mCurrentRlg);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentRlg = mPreviousRlg;

							}
						}).create();
		mReligion.setOnCancelListener(new OnCancelListener() {
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
	
	protected void changeSelfRelation(int mPreviousRtn, int mCurrentRtn) {
		String selectedRelation = relation[mCurrentRtn].toString();
		PreferenceEngine.getInstance(ctx).setSelfRelation(selectedRelation);
		PreferenceEngine.getInstance(ctx).setSelfRelationInt(mCurrentRtn);
		initView();
	}
	protected void changeSelfHaveChild(int mPreviousHvChld2, int mCurrentHvChld2) {
		PreferenceEngine.getInstance(ctx).saveHaveChildren(mCurrentHvChld2==0);
		initView();
		
	}

	private Dialog getDatePickDialog() {
		return new CustomerDatePickDialog(new AfterYouDialogImpl(this), this,
				R.style.Theme_CustomDialog, mDateSetListener,
				mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DATE));
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

	private void showErrorDialog()
	{
		ErrorDialog errDialog = new ErrorDialog(new AfterYouDialogImpl(this), this,
				R.style.Theme_CustomDialog, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}, getResources().getString(R.string.IDS_MANDATORY_PROFILE));
		errDialog.show();
	}
	private Intent intent;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.donebtn:
			if(b_day.getText()!= null &&  b_day.getText().length() > 0 && rlg.getText() != null && rlg.getText().length()>0 && rtln.getText()!=null && rtln.getText().length()>0)
			{
				intent = new Intent(ProfileSettingsActivity.this,CapturePictureActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			}
			else{
				showErrorDialog();
			}
			break;
		}
		
	}
}