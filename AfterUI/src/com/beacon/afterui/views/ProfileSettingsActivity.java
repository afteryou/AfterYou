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
import android.widget.NumberPicker;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomDialog;
import com.beacon.afterui.utils.customviews.CustomerDatePickDialog;
import com.beacon.afterui.utils.customviews.CustomerNumberPickerDialog;
import com.beacon.afterui.utils.customviews.ErrorDialog;

public class ProfileSettingsActivity extends BaseActivity implements OnClickListener{

	private Calendar mCalendar;

	private Context ctx;

	private CharSequence[] religion;
	private CharSequence[] relation;
	private CharSequence[] hvChld;
	private CharSequence[] bodyType;
	private CharSequence[] self_community;
	private CharSequence[] self_diet_list;
	
	private String[] height;
	
	private Dialog mReligion;
	private int mCurrentRlg = 0;
	private int mPreviousRlg = 0;
	
	private Dialog mRelation;
	private int mCurrentRltn = 0;
	private int mPreviousRltn = 0;
	
	private Dialog mBodyType;
	private int mCurrentBdyType = 0;
	private int mPreviousBdyType = 0;
	
	private Dialog mSlfCommunity;
	private int mCurrentSlfComm = 0;
	private int mPreviousSlfComm = 0;
	
	private Dialog mSlfDiet;
	private int mCurrentSlfDiet = 0;
	private int mPreviousSlfDiet = 0;
	
	private Dialog mHaveChild;
	private int mCurrentHvChld = 0;
	private int mPreviousHvChld = 0;
	
	private int mCurrentWntChld = 0;
	
	private int mCurrentHgt = 0;
	
	TextView b_day,rlg,rtln,hvChild, wntChild,hgt_txt,slf_body_type_txt,slf_community_txt,self_diet;

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
		if(wntChild == null)
		{
			wntChild = hvChild;
		}
		if(height == null)
		{
			height = getResources().getStringArray(R.array.height_choices);
		}
		if(bodyType == null)
		{
			bodyType = getResources().getStringArray(R.array.body_type_choices);
		}
		if(self_community == null)
		{
			self_community = getResources().getStringArray(R.array.self_community);
		}
		if(self_diet_list == null)
		{
			self_diet_list = getResources().getStringArray(R.array.diet);
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
				getHaveChildDialog(false).show();

			}
		});
		if (havechild_txt) {
			hvChild.setText(getResources().getString(R.string.IDS_YES));
			mCurrentHvChld = 0;
		}else{
			hvChild.setText(getResources().getString(R.string.IDS_NO));
			mCurrentHvChld = 1;
		}
		
		boolean wantchild_txt = PreferenceEngine.getInstance(this)
				.getWantChildren();
		wntChild = (TextView) findViewById(R.id.wantchild_edit_text);
		wntChild.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getHaveChildDialog(true).show();

			}
		});
		if (havechild_txt) {
			hvChild.setText(getResources().getString(R.string.IDS_YES));
			mCurrentHvChld = 0;
		}else{
			hvChild.setText(getResources().getString(R.string.IDS_NO));
			mCurrentHvChld = 1;
		}
		if(wantchild_txt){
			wntChild.setText(getResources().getString(R.string.IDS_YES));
			mCurrentWntChld = 0;
		}else{
			wntChild.setText(getResources().getString(R.string.IDS_NO));
			mCurrentWntChld = 1;
		}
		
		String height_txt = PreferenceEngine.getInstance(ctx).getSelfHeight();
		hgt_txt = (TextView)findViewById(R.id.height_edit_text);
		hgt_txt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getHeightDialog().show();
				
			}
		});
		if(height_txt != null)
		{
			hgt_txt.setText(height_txt);
			mCurrentHgt = PreferenceEngine.getInstance(ctx).getSelfHeightInt();
		}
		
		String body_type_txt = PreferenceEngine.getInstance(ctx).getSelfBodyType();
		slf_body_type_txt = (TextView)findViewById(R.id.bodyType_edit_text);
		slf_body_type_txt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getSelfBodyType().show();
				
			}
		});
		if(body_type_txt != null)
		{
			slf_body_type_txt.setText(body_type_txt);
			mCurrentBdyType = PreferenceEngine.getInstance(ctx).getSelfBodyTypeInt();
		}
		
		String self_community_txt = PreferenceEngine.getInstance(ctx).getSelfCommunity();
		slf_community_txt = (TextView)findViewById(R.id.comm_edit_text);
		slf_community_txt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getSelfCommunity().show();
				
			}
		});
		if(self_community_txt != null)
		{
			slf_community_txt.setText(self_community_txt);
			mCurrentBdyType = PreferenceEngine.getInstance(ctx).getSelfCommunityInt();
		}
		
		String self_diet_txt = PreferenceEngine.getInstance(ctx).getSelfDiet();
		self_diet = (TextView)findViewById(R.id.diet_edit_text);
		self_diet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getSelfDiet().show();
				
			}
		});
		if(self_diet_txt != null)
		{
			self_diet.setText(self_diet_txt);
			mCurrentSlfDiet = PreferenceEngine.getInstance(ctx).getSelfDietInt();
		}
	}


	protected Dialog getSelfDiet() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mSlfDiet = builder
				.setTitle(this.getString(R.string.diet_label))
				.setSingleChoiceItems(self_diet_list, mCurrentSlfDiet,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfDiet = whichButton;
							}
						})
				.setPositiveButton(this.getString(R.string.IDS_OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								mPreviousSlfComm = mCurrentSlfDiet;
								changeSelfDiet(mPreviousSlfDiet, mCurrentSlfDiet);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfDiet = mPreviousSlfDiet;

							}
						}).create();
		mSlfDiet.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentSlfDiet = mPreviousSlfDiet;
			}
		});

		return mSlfDiet;
	}


	protected Dialog getSelfCommunity() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mSlfCommunity = builder
				.setTitle(this.getString(R.string.comm_label))
				.setSingleChoiceItems(self_community, mCurrentSlfComm,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfComm = whichButton;
							}
						})
				.setPositiveButton(this.getString(R.string.IDS_OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								mPreviousSlfComm = mCurrentSlfComm;
								changeSelfComm(mPreviousSlfComm, mCurrentSlfComm);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfComm = mPreviousSlfComm;

							}
						}).create();
		mSlfCommunity.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentSlfComm = mPreviousSlfComm;
			}
		});

		return mSlfCommunity;
	}

	protected Dialog getSelfBodyType() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mBodyType = builder
				.setTitle(this.getString(R.string.bodyType_label))
				.setSingleChoiceItems(bodyType, mCurrentBdyType,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentBdyType = whichButton;
							}
						})
				.setPositiveButton(this.getString(R.string.IDS_OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								mPreviousBdyType = mCurrentBdyType;
								changeSelfBodyType(mPreviousBdyType, mCurrentBdyType);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentBdyType = mPreviousBdyType;

							}
						}).create();
		mBodyType.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentBdyType = mPreviousBdyType;
			}
		});

		return mBodyType;
	}

	protected void changeSelfBodyType(int mPreviousBdyType2,
			int mCurrentBdyType2) {
		String selectedBodyType = bodyType[mCurrentBdyType].toString();
		PreferenceEngine.getInstance(ctx).setSelfBodyType(selectedBodyType);
		PreferenceEngine.getInstance(ctx).setSelfBodyTypeInt(mCurrentBdyType);
		initView();
		
	}
	
	protected void changeSelfComm(int mPreviousSlfComm2, int mCurrentBdyType2) {
		String selectedSlfComm = self_community[mCurrentSlfComm].toString();
		PreferenceEngine.getInstance(ctx).setSelfCommunity(selectedSlfComm);
		PreferenceEngine.getInstance(ctx).setSelfCommunityInt(mCurrentSlfComm);
		initView();
		
	}
	

	protected void changeSelfDiet(int mPreviousSlfDiet2, int mCurrentSlfDiet2) {
		String selectedSlfDiet = self_diet_list[mCurrentSlfDiet].toString();
		PreferenceEngine.getInstance(ctx).setSelfDiet(selectedSlfDiet);
		PreferenceEngine.getInstance(ctx).setSelfDietInt(mCurrentSlfDiet);
		initView();
		
	}

	protected Dialog getHaveChildDialog(final boolean wntChld) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));
		String label = this.getString(R.string.havechild_label);
		
		if(wntChld)
		{
			label = this.getString(R.string.wantchild_label);
		}

		mHaveChild = builder
				.setTitle(label)
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
								if(wntChld)
								{
									changeSelfWntChild(mPreviousHvChld, mCurrentHvChld);
								}
								else
								{
									changeSelfHaveChild(mPreviousHvChld, mCurrentHvChld);
								}
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
	
	protected void changeSelfWntChild(int mPreviousHvChld2, int mCurrentHvChld2) {
		mCurrentWntChld = mCurrentHvChld2;
		PreferenceEngine.getInstance(ctx).saveWantChildren(mCurrentWntChld==0);
		initView();
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
	
	protected Dialog getHeightDialog() {
		return new CustomerNumberPickerDialog(new AfterYouDialogImpl(this), this,
				R.style.Theme_CustomDialog, mValuelistener,height, mCurrentHgt);
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
	
	private NumberPicker.OnValueChangeListener mValuelistener = new NumberPicker.OnValueChangeListener() {
		
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			if(oldVal != newVal)
			{
				PreferenceEngine.getInstance(ctx).setSelfHeightInt(newVal);
				PreferenceEngine.getInstance(ctx).setSelfHeight(height[newVal]);
				mCurrentHgt = newVal;
			}
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