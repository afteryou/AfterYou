package com.beacon.afterui.views;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomDialog;
import com.beacon.afterui.utils.customviews.CustomerDatePickDialog;
import com.beacon.afterui.utils.customviews.CustomerNumberPickerDialog;
import com.beacon.afterui.utils.customviews.ErrorDialog;

public class ProfileSettingsActivity extends BaseActivity implements
		OnClickListener {

	private Calendar mCalendar;

	private boolean isFacebook;

	private Context ctx;

	private CharSequence[] religion;
	private CharSequence[] mMatchReligion;
	private CharSequence[] relation;
	private CharSequence[] mMatchRelation;
	private CharSequence[] hvChld;
	private CharSequence[] mMatchHvChild;
	private CharSequence[] mMatchWntChild;
	private CharSequence[] bodyType;
	private CharSequence[] mMatchBodyType;
	private CharSequence[] self_community;
	private CharSequence[] mMatchCommunity;
	private CharSequence[] self_smoking;
	private CharSequence[] mMatchSmoking;
	private CharSequence[] self_drinking;
	private CharSequence[] mMatchDrinking;
	private CharSequence[] self_education;
	private CharSequence[] self_salary;
	private CharSequence[] want_age_range;
	private CharSequence[] self_diet_list;
	private CharSequence[] mMatchDietList;
	private String[] language_list;
	private String[] mMatchLanguageList;

	private String[] height;
	private String[] mMatchHeight;

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

	private Dialog mSlfSmoking;
	private int mCurrentSlfSmoking = 0;
	private int mPreviousSlfSmoking = 0;

	private Dialog mSlfDrinking;
	private int mCurrentSlfDriking = 0;
	private int mPreviousSlfDrinking = 0;

	private Dialog mSlfEducation;
	private int mCurrentSlfEducation = 0;
	private int mPreviousSlfEducation = 0;

	private Dialog mSlfSalary;
	private int mCurrentSlfSalary = 0;
	private int mPreviousSlfSalary = 0;

	private Dialog mWantAge;
	private int mCurrentWntAge = 0;
	private int mPreviousWntAge = 0;

	private Dialog mSlfLanguages;
	private boolean[] mCurrentSlfLang;
	private boolean[] mMatchCurrentLagn;

	private Dialog mSlfDiet;
	private int mCurrentSlfDiet = 0;
	private int mPreviousSlfDiet = 0;

	private Dialog mHaveChild;
	private int mCurrentHvChld = 0;
	private int mPreviousHvChld = 0;

	private int mCurrentWntChld = 0;

	private int mCurrentHgt = 0;

	private Typeface tf;

	TextView b_day, rlg, rtln, hvChild, wntChild, hgt_txt, slf_body_type_txt,
			slf_community_txt, self_diet, slf_lang, slf_smoking, slf_drinking,
			slf_eduction, slf_salary, wnt_age;

	private ImageButton mDoneBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_setup);
		if (getIntent().hasExtra(AppConstants.FACEBOOK_USER)) {
			isFacebook = true;
		}
		mDoneBtn = (ImageButton) findViewById(R.id.donebtn);
		mDoneBtn.setOnClickListener(this);
		ctx = this;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCalendar = Calendar.getInstance();

		if (religion == null) {
			religion = getResources().getStringArray(R.array.religion_choices);
		}
		if (relation == null) {
			relation = getResources().getStringArray(R.array.relation_choices);
		}
		if (hvChld == null) {
			hvChld = getResources().getStringArray(R.array.selfhvChld_choices);
		}
		if (wntChild == null) {
			wntChild = hvChild;
		}
		if (height == null) {
			height = getResources().getStringArray(R.array.height_choices);
		}
		if (mMatchHeight == null) {
			mMatchHeight = height;
		}
		if (bodyType == null) {
			bodyType = getResources().getStringArray(R.array.body_type_choices);
		}
		if (mMatchBodyType == null) {
			mMatchBodyType = bodyType;
		}
		if (self_community == null) {
			self_community = getResources().getStringArray(
					R.array.self_community);
		}
		if (mMatchCommunity == null) {
			mMatchCommunity = self_community;
		}
		if (self_smoking == null) {
			self_smoking = getResources().getStringArray(R.array.smoking);
		}
		if (mMatchSmoking == null) {
			mMatchSmoking = self_smoking;
		}
		if (self_drinking == null) {
			self_drinking = getResources().getStringArray(R.array.smoking);
		}
		if (mMatchDrinking == null) {
			mMatchDrinking = self_drinking;
		}
		if (self_education == null) {
			self_education = getResources().getStringArray(R.array.education);
		}
		if (self_salary == null) {
			self_salary = getResources().getStringArray(R.array.salary);
		}
		if (want_age_range == null) {
			want_age_range = getResources().getStringArray(R.array.age_range);
		}

		if (self_diet_list == null) {
			self_diet_list = getResources().getStringArray(R.array.diet);
		}
		if (mMatchDietList == null) {
			mMatchDietList = self_diet_list;
		}
		if (language_list == null) {
			language_list = getResources().getStringArray(
					R.array.languages_list);
			mCurrentSlfLang = new boolean[language_list.length];
		}
		if (mMatchLanguageList == null) {
			mMatchLanguageList = language_list;
			mMatchCurrentLagn = new boolean[mMatchLanguageList.length];
		}
		if (mMatchReligion == null) {
			mMatchReligion = getResources().getStringArray(
					R.array.religion_choices);
		}
		if (mMatchRelation == null) {
			mMatchRelation = getResources().getStringArray(
					R.array.relation_choices);
		}
		if (mMatchHvChild == null) {
			mMatchHvChild = getResources().getStringArray(
					R.array.selfhvChld_choices);
		}

		if (mMatchWntChild == null) {
			mMatchWntChild = mMatchHvChild;
		}

		tf = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-It.otf");
		initView();
		matchInitView();
	}

	private void initView() {

		String birthday = PreferenceEngine.getInstance(this).getBirthday();
		b_day = (TextView) findViewById(R.id.birthday_edit_text);
		b_day.setTypeface(tf);
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
		rlg.setTypeface(tf);
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
		rtln.setTypeface(tf);
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
		hvChild.setTypeface(tf);
		hvChild.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getHaveChildDialog(false).show();

			}
		});
		if (havechild_txt) {
			hvChild.setText(getResources().getString(R.string.IDS_YES));
			mCurrentHvChld = 0;
		} else {
			hvChild.setText(getResources().getString(R.string.IDS_NO));
			mCurrentHvChld = 1;
		}

		boolean wantchild_txt = PreferenceEngine.getInstance(this)
				.getWantChildren();
		wntChild = (TextView) findViewById(R.id.wantchild_edit_text);
		wntChild.setTypeface(tf);
		wntChild.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getHaveChildDialog(true).show();

			}
		});
		if (havechild_txt) {
			hvChild.setText(getResources().getString(R.string.IDS_YES));
			mCurrentHvChld = 0;
		} else {
			hvChild.setText(getResources().getString(R.string.IDS_NO));
			mCurrentHvChld = 1;
		}
		if (wantchild_txt) {
			wntChild.setText(getResources().getString(R.string.IDS_YES));
			mCurrentWntChld = 0;
		} else {
			wntChild.setText(getResources().getString(R.string.IDS_NO));
			mCurrentWntChld = 1;
		}

		String height_txt = PreferenceEngine.getInstance(ctx).getSelfHeight();
		hgt_txt = (TextView) findViewById(R.id.height_edit_text);
		hgt_txt.setTypeface(tf);
		hgt_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getHeightDialog().show();

			}
		});
		if (height_txt != null) {
			hgt_txt.setText(height_txt);
			mCurrentHgt = PreferenceEngine.getInstance(ctx).getSelfHeightInt();
		}

		String body_type_txt = PreferenceEngine.getInstance(ctx)
				.getSelfBodyType();
		slf_body_type_txt = (TextView) findViewById(R.id.bodyType_edit_text);
		slf_body_type_txt.setTypeface(tf);
		slf_body_type_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelfBodyType().show();

			}
		});
		if (body_type_txt != null) {
			slf_body_type_txt.setText(body_type_txt);
			mCurrentBdyType = PreferenceEngine.getInstance(ctx)
					.getSelfBodyTypeInt();
		}

		String self_community_txt = PreferenceEngine.getInstance(ctx)
				.getSelfCommunity();
		slf_community_txt = (TextView) findViewById(R.id.comm_edit_text);
		slf_community_txt.setTypeface(tf);
		slf_community_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelfCommunity().show();

			}
		});
		if (self_community_txt != null) {
			slf_community_txt.setText(self_community_txt);
			mCurrentBdyType = PreferenceEngine.getInstance(ctx)
					.getSelfCommunityInt();
		}

		String self_diet_txt = PreferenceEngine.getInstance(ctx).getSelfDiet();
		self_diet = (TextView) findViewById(R.id.diet_edit_text);
		self_diet.setTypeface(tf);
		self_diet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelfDiet().show();

			}
		});
		if (self_diet_txt != null) {
			self_diet.setText(self_diet_txt);
			mCurrentSlfDiet = PreferenceEngine.getInstance(ctx)
					.getSelfDietInt();
		}

		String self_lang_txt = PreferenceEngine.getInstance(ctx)
				.getSelfLangList();
		slf_lang = (TextView) findViewById(R.id.languages_edit_text);
		slf_lang.setTypeface(tf);
		slf_lang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelfLanguage().show();

			}
		});
		if (self_lang_txt != null) {
			slf_lang.setText(self_lang_txt);
			mCurrentSlfLang = PreferenceEngine.getInstance(ctx)
					.getSelfLangBoolean(language_list);
		}

		String self_smok_txt = PreferenceEngine.getInstance(ctx)
				.getSelfSmoking();
		slf_smoking = (TextView) findViewById(R.id.smoke_edit_text);
		slf_smoking.setTypeface(tf);
		slf_smoking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelfSmoking().show();

			}
		});
		if (self_smok_txt != null) {
			slf_smoking.setText(self_smok_txt);
			mCurrentSlfSmoking = PreferenceEngine.getInstance(ctx)
					.getSelfSmokingInt();
		}

		String self_drinking_txt = PreferenceEngine.getInstance(ctx)
				.getSelfDrinking();
		slf_drinking = (TextView) findViewById(R.id.drink_edit_text);
		slf_drinking.setTypeface(tf);
		slf_drinking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelfDrinking().show();

			}
		});
		if (self_drinking_txt != null) {
			slf_drinking.setText(self_drinking_txt);
			mCurrentSlfDriking = PreferenceEngine.getInstance(ctx)
					.getSelfDrinkInt();
		}

		String self_education_txt = PreferenceEngine.getInstance(ctx)
				.getSelfEducation();
		slf_eduction = (TextView) findViewById(R.id.education_text);
		slf_eduction.setTypeface(tf);
		slf_eduction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelfEducation().show();

			}
		});
		if (self_education_txt != null) {
			slf_eduction.setText(self_education_txt);
			mCurrentSlfEducation = PreferenceEngine.getInstance(ctx)
					.getSelfEducationInt();
		}

		String self_salary_txt = PreferenceEngine.getInstance(ctx)
				.getSelfSalary();
		slf_salary = (TextView) findViewById(R.id.salary_edit_text);
		slf_salary.setTypeface(tf);
		slf_salary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelfSalary().show();

			}
		});
		if (self_salary_txt != null) {
			slf_salary.setText(self_salary_txt);
			mCurrentSlfSalary = PreferenceEngine.getInstance(ctx)
					.getSelfSalaryInt();
		}

		String wnt_age_txt = PreferenceEngine.getInstance(ctx).getWantAge();
		wnt_age = (TextView) findViewById(R.id.match_birthday_edit_text);
		wnt_age.setTypeface(tf);
		wnt_age.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getWantAge().show();

			}
		});
		if (wnt_age_txt != null) {
			wnt_age.setText(wnt_age_txt);
			mCurrentWntAge = PreferenceEngine.getInstance(ctx).getWantAgeInt();
		}
	}

	protected Dialog getWantAge() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mWantAge = builder
				.setTitle(this.getString(R.string.IDS_AgeRange_Label))
				.setSingleChoiceItems(want_age_range, mCurrentWntAge,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentWntAge = whichButton;
								mPreviousWntAge = mCurrentWntAge;
								changeWntAge(mPreviousWntAge, mCurrentWntAge);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentWntAge = mPreviousWntAge;

							}
						}).create();
		mWantAge.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentWntAge = mPreviousWntAge;
			}
		});

		return mWantAge;
	}

	protected void changeWntAge(int mPreviousWntAge2, int mCurrentWntAge2) {
		String selectedWntAge = want_age_range[mCurrentWntAge].toString();
		PreferenceEngine.getInstance(ctx).setWantAge(selectedWntAge);
		PreferenceEngine.getInstance(ctx).setWantAgeInt(mCurrentWntAge);
		initView();

	}

	protected Dialog getSelfSalary() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mSlfSalary = builder
				.setTitle(this.getString(R.string.IDS_Salary_Label))
				.setSingleChoiceItems(self_salary, mCurrentSlfSalary,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfSalary = whichButton;
								mPreviousSlfSalary = mCurrentSlfSalary;
								changeSelfSalary(mPreviousSlfSalary,
										mCurrentSlfSalary);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfSalary = mPreviousSlfSalary;

							}
						}).create();
		mSlfSalary.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentSlfSalary = mPreviousSlfSalary;
			}
		});

		return mSlfSalary;
	}

	protected void changeSelfSalary(int mPreviousSlfSalary2,
			int mCurrentSlfSalary2) {
		String selectedSlfSalary = self_salary[mCurrentSlfSalary].toString();
		PreferenceEngine.getInstance(ctx).setSelfSalary(selectedSlfSalary);
		PreferenceEngine.getInstance(ctx).setSelfSalaryInt(mCurrentSlfSalary);
		initView();

	}

	protected Dialog getSelfEducation() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mSlfEducation = builder
				.setTitle(this.getString(R.string.IDS_Education_Label))
				.setSingleChoiceItems(self_education, mCurrentSlfEducation,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfEducation = whichButton;
								mPreviousSlfEducation = mCurrentSlfEducation;
								changeSelfEducation(mPreviousSlfEducation,
										mCurrentSlfEducation);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfEducation = mPreviousSlfEducation;

							}
						}).create();
		mSlfEducation.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentSlfEducation = mPreviousSlfEducation;
			}
		});

		return mSlfEducation;
	}

	protected void changeSelfEducation(int mPreviousSlfEducation2,
			int mCurrentSlfEducation2) {
		String selectedSlfEducation = self_education[mCurrentSlfEducation]
				.toString();
		PreferenceEngine.getInstance(ctx)
				.setSelfEducation(selectedSlfEducation);
		PreferenceEngine.getInstance(ctx).setSelfEducationInt(
				mCurrentSlfEducation);
		initView();

	}

	protected Dialog getSelfSmoking() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mSlfSmoking = builder
				.setTitle(this.getString(R.string.smoke_label))
				.setSingleChoiceItems(self_smoking, mCurrentSlfSmoking,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfSmoking = whichButton;
								mPreviousSlfSmoking = mCurrentSlfSmoking;
								changeSelfSmoking(mPreviousSlfSmoking,
										mCurrentSlfSmoking);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfSmoking = mPreviousSlfSmoking;

							}
						}).create();
		mSlfSmoking.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentSlfSmoking = mPreviousSlfSmoking;
			}
		});

		return mSlfSmoking;
	}

	protected void changeSelfSmoking(int mPreviousSlfSmoking2,
			int mCurrentSlfSmoking2) {
		String selectedSlfSmoking = self_smoking[mCurrentSlfSmoking].toString();
		PreferenceEngine.getInstance(ctx).setSelfSmoking(selectedSlfSmoking);
		PreferenceEngine.getInstance(ctx).setSelfSmokingInt(mCurrentSlfSmoking);
		initView();

	}

	protected void changeMatchSmoking(int mCurrentSlfSmoking2) {
		String selectedSlfSmoking = mMatchSmoking[mCurrentSlfSmoking2]
				.toString();
		PreferenceEngine.getInstance(ctx).setMatchSmoking(selectedSlfSmoking);
		PreferenceEngine.getInstance(ctx).setMatchSmokingInt(
				mCurrentSlfSmoking2);
		matchInitView();

	}

	protected Dialog getSelfDrinking() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mSlfDrinking = builder
				.setTitle(this.getString(R.string.drink_label))
				.setSingleChoiceItems(self_drinking, mCurrentSlfDriking,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfDriking = whichButton;
								mPreviousSlfDrinking = mCurrentSlfDriking;
								changeSelfDrinking(mPreviousSlfDrinking,
										mCurrentSlfDriking);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentSlfDriking = mPreviousSlfDrinking;

							}
						}).create();
		mSlfDrinking.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mCurrentSlfDriking = mPreviousSlfDrinking;
			}
		});

		return mSlfDrinking;
	}

	protected void changeSelfDrinking(int mPreviousSlfDrinking2,
			int mCurrentSlfDriking2) {
		String selectedSlfDrinking = self_drinking[mCurrentSlfDriking]
				.toString();
		PreferenceEngine.getInstance(ctx).setSelfDrinking(selectedSlfDrinking);
		PreferenceEngine.getInstance(ctx)
				.setSelfDrinkingInt(mCurrentSlfDriking);
		initView();

	}

	protected void changeMatchDrinking(int mCurrentSlfDriking2) {
		String selectedSlfDrinking = mMatchDrinking[mCurrentSlfDriking2]
				.toString();
		PreferenceEngine.getInstance(ctx).setMatchDrinking(selectedSlfDrinking);
		PreferenceEngine.getInstance(ctx).setMatchDrinkingInt(
				mCurrentSlfDriking2);
		matchInitView();

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
								mPreviousSlfComm = mCurrentSlfDiet;
								changeSelfDiet(mPreviousSlfDiet,
										mCurrentSlfDiet);
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

	protected Dialog getSelfLanguage() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mSlfLanguages = builder
				.setTitle(this.getString(R.string.languages_label))
				.setMultiChoiceItems(language_list, mCurrentSlfLang,
						new OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								System.out.println("Which----" + which
										+ " isChecked ==" + isChecked);
								mCurrentSlfLang[which] = isChecked;

							}
						})
				.setPositiveButton(this.getString(R.string.IDS_OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeSelfLang();
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).create();
		mSlfLanguages.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});

		return mSlfLanguages;
	}

	protected void changeSelfLang() {
		char separator = ';';
		StringBuffer toSave = new StringBuffer();

		for (int i = 0; i < mCurrentSlfLang.length; i++) {
			if (mCurrentSlfLang[i]) {
				toSave.append(language_list[i]).append(separator);
			}
		}
		PreferenceEngine.getInstance(ctx).setSelfLangList(toSave.toString());
		initView();
	}

	protected void changeMatchLang() {
		char separator = ';';
		StringBuffer toSave = new StringBuffer();

		for (int i = 0; i < mMatchCurrentLagn.length; i++) {
			if (mMatchCurrentLagn[i]) {
				toSave.append(mMatchLanguageList[i]).append(separator);
			}
		}
		PreferenceEngine.getInstance(ctx).setMatchLangList(toSave.toString());
		matchInitView();
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
								mPreviousSlfComm = mCurrentSlfComm;
								changeSelfComm(mPreviousSlfComm,
										mCurrentSlfComm);
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
								mPreviousBdyType = mCurrentBdyType;
								changeSelfBodyType(mPreviousBdyType,
										mCurrentBdyType);
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

	protected void changeMatchBodyType(int mCurrentBdyType2) {
		String selectedBodyType = mMatchBodyType[mCurrentBdyType2].toString();
		PreferenceEngine.getInstance(ctx).setMatchBodyType(selectedBodyType);
		PreferenceEngine.getInstance(ctx).setMatchBodyTypeInt(mCurrentBdyType2);
		matchInitView();

	}

	protected void changeSelfComm(int mPreviousSlfComm2, int mCurrentBdyType2) {
		String selectedSlfComm = self_community[mCurrentSlfComm].toString();
		PreferenceEngine.getInstance(ctx).setSelfCommunity(selectedSlfComm);
		PreferenceEngine.getInstance(ctx).setSelfCommunityInt(mCurrentSlfComm);
		initView();

	}

	protected void changeMatchComm(int mCurrentBdyType2) {
		String selectedSlfComm = mMatchCommunity[mCurrentBdyType2].toString();
		PreferenceEngine.getInstance(ctx).setMatchCommunity(selectedSlfComm);
		PreferenceEngine.getInstance(ctx)
				.setMatchCommunityInt(mCurrentBdyType2);
		matchInitView();

	}

	protected void changeSelfDiet(int mPreviousSlfDiet2, int mCurrentSlfDiet2) {
		String selectedSlfDiet = self_diet_list[mCurrentSlfDiet].toString();
		PreferenceEngine.getInstance(ctx).setSelfDiet(selectedSlfDiet);
		PreferenceEngine.getInstance(ctx).setSelfDietInt(mCurrentSlfDiet);
		initView();

	}

	protected void changeMatchDiet(int mCurrentSlfDiet2) {
		String selectedSlfDiet = mMatchDietList[mCurrentSlfDiet2].toString();
		PreferenceEngine.getInstance(ctx).setMatchDiet(selectedSlfDiet);
		PreferenceEngine.getInstance(ctx).setMatchDietInt(mCurrentSlfDiet2);
		matchInitView();

	}

	protected Dialog getHaveChildDialog(final boolean wntChld) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));
		String label = this.getString(R.string.havechild_label);

		if (wntChld) {
			label = this.getString(R.string.wantchild_label);
		}

		mHaveChild = builder
				.setTitle(label)
				.setSingleChoiceItems(hvChld, mCurrentHvChld,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mCurrentHvChld = whichButton;
								mPreviousHvChld = mCurrentHvChld;
								if (wntChld) {
									changeSelfWntChild(mPreviousHvChld,
											mCurrentHvChld);
								} else {
									changeSelfHaveChild(mPreviousHvChld,
											mCurrentHvChld);
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
		PreferenceEngine.getInstance(ctx)
				.saveWantChildren(mCurrentWntChld == 0);
		initView();
	}

	protected void changeMatchWntChild(int mCurrentHvChld2) {
		mCurrentWntChld = mCurrentHvChld2;
		PreferenceEngine.getInstance(ctx).saveMatchWantChildren(
				mCurrentWntChld == 0);
		matchInitView();
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

	protected void changeMatchRelation(int mCurrentRtn) {
		String selectedRelation = mMatchRelation[mCurrentRtn].toString();
		PreferenceEngine.getInstance(ctx).setMatchRelation(selectedRelation);
		PreferenceEngine.getInstance(ctx).setMatchRelationInt(mCurrentRtn);
		matchInitView();
	}

	protected void changeSelfHaveChild(int mPreviousHvChld2, int mCurrentHvChld2) {
		PreferenceEngine.getInstance(ctx)
				.saveHaveChildren(mCurrentHvChld2 == 0);
		initView();

	}

	protected void changeMatchHaveChild(int mCurrentHvChld2) {
		PreferenceEngine.getInstance(ctx).saveMatchHaveChildren(
				mCurrentHvChld2 == 0);
		matchInitView();
	}

	private Dialog getDatePickDialog() {
		return new CustomerDatePickDialog(new AfterYouDialogImpl(this), this,
				R.style.Theme_CustomDialog, mDateSetListener,
				mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DATE));
	}

	protected Dialog getHeightDialog() {
		return new CustomerNumberPickerDialog(new AfterYouDialogImpl(this),
				this, R.style.Theme_CustomDialog, mValuelistener, height,
				mCurrentHgt);
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
			if (oldVal != newVal) {
				PreferenceEngine.getInstance(ctx).setSelfHeightInt(newVal);
				PreferenceEngine.getInstance(ctx).setSelfHeight(height[newVal]);
				mCurrentHgt = newVal;
			}
			initView();

		}
	};
	private NumberPicker.OnValueChangeListener mMatchValuelistener = new NumberPicker.OnValueChangeListener() {

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			if (oldVal != newVal) {
				PreferenceEngine.getInstance(ctx).setMatchHeightInt(newVal);
				PreferenceEngine.getInstance(ctx).setMatchHeight(
						mMatchHeight[newVal]);
			}
			matchInitView();

		}
	};

	private void showErrorDialog() {
		ErrorDialog errDialog = new ErrorDialog(new AfterYouDialogImpl(this),
				this, R.style.Theme_CustomDialog,
				new DialogInterface.OnClickListener() {

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
			if (b_day.getText() != null && b_day.getText().length() > 0
					&& rlg.getText() != null && rlg.getText().length() > 0
					&& rtln.getText() != null && rtln.getText().length() > 0) {
				intent = new Intent(ProfileSettingsActivity.this,
						CapturePictureActivity.class);
				if (isFacebook) {
					intent.putExtra(AppConstants.FACEBOOK_USER, true);
				}
				startActivity(intent);
			} else {
				showErrorDialog();
			}
			break;
		}

	}

	protected void changeMatchReligion(int mCurrentRlg2) {
		String selectedReligion = mMatchReligion[mCurrentRlg2].toString();
		PreferenceEngine.getInstance(ctx).setMatchReligion(selectedReligion);
		PreferenceEngine.getInstance(ctx).setMatchReligionInt(mCurrentRlg2);
		matchInitView();
	}

	private void matchInitView() {

		String matchReligion_txt = PreferenceEngine.getInstance(this)
				.getMatchReligion();
			TextView match_rlg = (TextView) findViewById(R.id.match_religion_edit_text);
			match_rlg.setTypeface(tf);
			match_rlg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getMatchReligionDialog().show();
				}
			});

		if (matchReligion_txt != null) {
			match_rlg.setText(matchReligion_txt);
		}

		String relation_txt = PreferenceEngine.getInstance(this)
				.getMatchRelation();
		TextView match_rtln = (TextView) findViewById(R.id.match_relation_edit_text);
		match_rtln.setTypeface(tf);
		match_rtln.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchRelationDialog().show();

			}
		});
		if (relation_txt != null) {
			match_rtln.setText(relation_txt);
		}

		boolean havechild_txt = PreferenceEngine.getInstance(this)
				.getMatchHaveChildren();
		TextView matchHvChlid = (TextView) findViewById(R.id.match_havechild_edit_text);
		matchHvChlid.setTypeface(tf);
		matchHvChlid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchHaveChildDialog(false).show();

			}
		});
		if (havechild_txt) {
			matchHvChlid.setText(getResources().getString(R.string.IDS_YES));
			mCurrentHvChld = 0;
		} else {
			matchHvChlid.setText(getResources().getString(R.string.IDS_NO));
		}

		boolean wantchild_txt = PreferenceEngine.getInstance(this)
				.getMatchWantChildren();
		TextView matchWntChild = (TextView) findViewById(R.id.match_wantchild_edit_text);
		matchWntChild.setTypeface(tf);
		matchWntChild.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchHaveChildDialog(true).show();

			}
		});
		if (havechild_txt) {
			matchHvChlid.setText(getResources().getString(R.string.IDS_YES));
		} else {
			matchHvChlid.setText(getResources().getString(R.string.IDS_NO));
		}
		if (wantchild_txt) {
			matchWntChild.setText(getResources().getString(R.string.IDS_YES));
		} else {
			matchWntChild.setText(getResources().getString(R.string.IDS_NO));
		}

		String height_txt = PreferenceEngine.getInstance(this).getMatchHeight();
		TextView matchHeightTxt = (TextView) findViewById(R.id.match_height_edit_text);
		matchHeightTxt.setTypeface(tf);
		matchHeightTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchHeightDialog().show();

			}
		});
		if (height_txt != null) {
			matchHeightTxt.setText(height_txt);
		}

		String body_type_txt = PreferenceEngine.getInstance(ctx)
				.getMatchBodyType();
		TextView match_body_type = (TextView) findViewById(R.id.match_bodyType_edit_text);
		match_body_type.setTypeface(tf);
		match_body_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchBodyType().show();

			}
		});
		if (body_type_txt != null) {
			match_body_type.setText(body_type_txt);
		}

		String match_community_txt = PreferenceEngine.getInstance(ctx)
				.getMatchCommunity();
		TextView match_community = (TextView) findViewById(R.id.match_comm_edit_text);
		match_community.setTypeface(tf);
		match_community.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchCommunity().show();

			}
		});
		if (match_community_txt != null) {
			match_community.setText(match_community_txt);

		}
		String match_diet_txt = PreferenceEngine.getInstance(ctx)
				.getMatchDiet();
		TextView match_diet = (TextView) findViewById(R.id.match_diet_edit_text);
		match_diet.setTypeface(tf);
		match_diet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchDiet().show();

			}
		});
		if (match_diet_txt != null) {
			match_diet.setText(match_diet_txt);
		}

		String match_smok_txt = PreferenceEngine.getInstance(ctx)
				.getMatchSmoking();
		TextView match_smoking = (TextView) findViewById(R.id.match_smoke_edit_text);
		match_smoking.setTypeface(tf);
		match_smoking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchSmoking().show();

			}
		});
		if (match_smok_txt != null) {
			match_smoking.setText(match_smok_txt);

		}
		String match_drinking_txt = PreferenceEngine.getInstance(ctx)
				.getMatchDrinking();
		TextView match_drinking = (TextView) findViewById(R.id.match_drink_edit_text);
		match_drinking.setTypeface(tf);
		match_drinking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchDrinking().show();

			}
		});
		if (match_drinking_txt != null) {
			match_drinking.setText(match_drinking_txt);

		}
		String match_lang_txt = PreferenceEngine.getInstance(ctx)
				.getMatchLangList();
		TextView match_lang = (TextView) findViewById(R.id.match_languages_edit_text);
		match_lang.setTypeface(tf);
		match_lang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchLanguage().show();

			}
		});
		if (match_lang_txt != null) {
			match_lang.setText(match_lang_txt);

		}

	}

	protected Dialog getMatchLanguage() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mMatchCurrentLagn = PreferenceEngine.getInstance(ctx)
				.getMatchLangBoolean(mMatchLanguageList);
		Dialog match_languages = builder
				.setTitle(this.getString(R.string.languages_label))
				.setMultiChoiceItems(mMatchLanguageList, mMatchCurrentLagn,
						new OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								System.out.println("Which----" + which
										+ " isChecked ==" + isChecked);
								mMatchCurrentLagn[which] = isChecked;

							}
						})
				.setPositiveButton(this.getString(R.string.IDS_OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeMatchLang();
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return match_languages;
	}

	protected Dialog getMatchDrinking() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx).getMatchDrinkInt();
		Dialog match_drinking = builder
				.setTitle(this.getString(R.string.drink_label))
				.setSingleChoiceItems(mMatchDrinking, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								changeMatchDrinking(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return match_drinking;
	}

	protected Dialog getMatchSmoking() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx)
				.getMatchSmokingInt();
		Dialog match_smoking = builder
				.setTitle(this.getString(R.string.smoke_label))
				.setSingleChoiceItems(mMatchSmoking, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								changeMatchSmoking(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return match_smoking;
	}

	protected Dialog getMatchDiet() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx).getMatchDietInt();
		Dialog diet = builder
				.setTitle(this.getString(R.string.diet_label))
				.setSingleChoiceItems(mMatchDietList, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeMatchDiet(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return diet;
	}

	protected Dialog getMatchCommunity() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx)
				.getMatchCommunityInt();
		Dialog community = builder
				.setTitle(this.getString(R.string.comm_label))
				.setSingleChoiceItems(mMatchCommunity, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								changeMatchComm(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return community;
	}

	protected Dialog getMatchBodyType() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx)
				.getMatchBodyTypeInt();
		Dialog body_type = builder
				.setTitle(this.getString(R.string.bodyType_label))
				.setSingleChoiceItems(mMatchBodyType, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								changeMatchBodyType(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return body_type;
	}

	protected Dialog getMatchHeightDialog() {
		return new CustomerNumberPickerDialog(new AfterYouDialogImpl(this),
				this, R.style.Theme_CustomDialog, mMatchValuelistener,
				mMatchHeight, PreferenceEngine.getInstance(this)
						.getMatchHeightInt());
	}

	protected Dialog getMatchHaveChildDialog(final boolean wntChld) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));
		String label = null;
		int checkedItem = -1;
		if (wntChld) {
			label = this.getString(R.string.wantchild_label);
			checkedItem = PreferenceEngine.getInstance(ctx)
					.getMatchWantChildren() ? 0 : 1;
		} else {
			label = this.getString(R.string.havechild_label);
			checkedItem = PreferenceEngine.getInstance(ctx).getHaveChildren() ? 0
					: 1;
		}

		Dialog haveChildDialog = builder
				.setTitle(label)
				.setSingleChoiceItems(mMatchHvChild, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if (wntChld) {
									changeMatchWntChild(whichButton);
								} else {
									changeMatchHaveChild(whichButton);
								}
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return haveChildDialog;
	}

	private Dialog getMatchReligionDialog() {

		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx)
				.getMatchReligionInt();
		Dialog religion = builder
				.setTitle(this.getString(R.string.religion_label))
				.setSingleChoiceItems(mMatchReligion, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeMatchReligion(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return religion;
	}

	protected Dialog getMatchRelationDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx)
				.getMatchRelationInt();
		Dialog relation = builder
				.setTitle(this.getString(R.string.relation_label))
				.setSingleChoiceItems(mMatchRelation, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeMatchRelation(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return relation;
	}
}