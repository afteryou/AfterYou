package com.beacon.afterui.views;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.custom.view.wheel.WheelView;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.CustomDialog;
import com.beacon.afterui.utils.customviews.CustomerDatePickDialog;
import com.beacon.afterui.utils.customviews.ErrorDialog;

public class ProfileSettingsActivity extends BaseActivity implements
		OnClickListener {

	private Calendar mCalendar;

	private boolean isFacebook;

	private Context ctx;

	private String[] religion;
	private String[] relation;
	private String[] hvChld;
	private String[] wntChild;
	private String[] bodyType;
	private String[] self_community;
	private String[] self_smoking;
	private String[] self_drinking;
	private String[] self_education;
	private String[] self_salary;
	private String[] want_age_range;
	private String[] self_diet_list;
	private String[] language_list;

	private String[] foot;
	private String[] inches;

	private boolean[] mCurrentSlfLang;
	private boolean[] mMatchCurrentLagn;

	private Typeface tf;

	private TextView b_day, rlg, rtln;

	private TextView mDoneBtn;

	private TextView hvChild;

	private TextView wantChild;

	private TextView hgt_foot;

	private TextView hgt_inches;

	private TextView slf_body_type_txt;

	private TextView slf_community_txt;

	private TextView self_diet;

	private TextView slf_lang;

	private TextView slf_smoking;

	private TextView slf_drinking;

	private TextView slf_eduction;

	private TextView slf_salary;

	private TextView wnt_age;

	private TextView mMatch_Rlg;
	private TextView mMatch_Rtln;
	private TextView mMatchHvChlid;
	private TextView mMatchWntChildTxt;
	private TextView mMatchHeightFoot;
	private TextView mMatchHeightInches;
	private TextView mMatch_Body_Type;
	private TextView mMatch_Community;
	private TextView mMatch_Diet;
	private TextView mMatch_Smoking;
	private TextView mMatch_Drinking;
	private TextView mMatch_Lang;

	private static final boolean isTest = true;

	private LayoutInflater mLayoutInflator;

	private View mCurrentView;
	private ViewGroup mCurrentLayout;
	private boolean mShowing = false;

	public static enum ProfileInfo {
		RELIGION, RELATIONSHIP, HAVECHILDREN, WANTCHILDREN, LANGUAGES, HEIGHT, BODYTYPE, COMMUNITY, DIET, SMOKING, DRINGKING, EDUCATION, SALARY, AGE_RANGE
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.profile_setup);
		setBehindLeftContentView(R.layout.profile_setup);
		setBehindRightContentView(R.layout.profile_setup);

		mLayoutInflator = getLayoutInflater();

		Typeface typeFaceRegular = Typeface.createFromAsset(getAssets(),
				"fonts/MyriadPro-Regular.otf");
		tf = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-It.otf");

		int[] selfText = { R.id.done_btn, R.id.about_you_txt,
				R.id.date_of_birth_txt, R.id.religion_label_txt,
				R.id.relation_label_txt, R.id.havechild_label_txt,
				R.id.wantchild_label_txt, R.id.languages_label_txt,
				R.id.height_label_txt, R.id.bodyType_label_txt,
				R.id.comm_label_txt, R.id.diet_label_txt, R.id.smoke_label_txt,
				R.id.drink_label_txt, R.id.education_label_txt,
				R.id.salary_label_txt };

		for (int i = 0; i < selfText.length; i++) {

			TextView textview = (TextView) findViewById(selfText[i]);
			textview.setTypeface(typeFaceRegular);
		}

		TextView required_txt = (TextView) findViewById(R.id.required_txt);
		required_txt.setTypeface(tf);

		int[] matchText = { R.id.ideal_match, R.id.match_date_of_birth_txt,
				R.id.match_religion_label_txt, R.id.match_relation_label_txt,
				R.id.match_havechild_label_txt, R.id.match_wantchild_label_txt,
				R.id.match_languages_label_txt, R.id.match_height_label_txt,
				R.id.match_bodyType_label_txt, R.id.match_comm_label_txt,
				R.id.match_diet_label_txt, R.id.match_smoke_label_txt,
				R.id.match_drink_label_txt };

		for (int i = 0; i < matchText.length; i++) {
			TextView textview = (TextView) findViewById(matchText[i]);
			textview.setTypeface(typeFaceRegular);
		}

		if (getIntent().hasExtra(AppConstants.FACEBOOK_USER)) {
			isFacebook = true;
		}
		mDoneBtn = (TextView) findViewById(R.id.done_btn);
		mDoneBtn.setOnClickListener(this);
		ctx = this;
		setListeners();
		matchListner();
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
			wntChild = hvChld;
		}
		if (foot == null) {
			foot = getResources().getStringArray(R.array.height_foot);
		}
		if (inches == null) {
			inches = getResources().getStringArray(R.array.height_inches);
		}
		if (bodyType == null) {
			bodyType = getResources().getStringArray(R.array.body_type_choices);
		}
		if (self_community == null) {
			self_community = getResources().getStringArray(
					R.array.self_community);
		}
		if (self_smoking == null) {
			self_smoking = getResources().getStringArray(R.array.smoking);
		}
		if (self_drinking == null) {
			self_drinking = getResources().getStringArray(R.array.smoking);
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
		if (language_list == null) {
			language_list = getResources().getStringArray(
					R.array.languages_list);
			mCurrentSlfLang = new boolean[language_list.length];
		}

		initView();
		matchInitView();
	}

	private void setListeners() {
		final int flag = 1;
		b_day = (TextView) findViewById(R.id.birthday_edit_text);
		b_day.setTypeface(tf);
		final View view = findViewById(R.id.brith_lay);

		final FrameLayout birth_day_holder = (FrameLayout) findViewById(R.id.birth_day_holder);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getDatePickDialog().show();
				// mBirthDayViewStub.inflate();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(
							R.layout.wheel_date_picker, birth_day_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForBirthDate(view, b_day);
					// final WheelView month = (WheelView) view
					// .findViewById(R.id.month);
					// final WheelView year = (WheelView) view
					// .findViewById(R.id.year);
					// final WheelView day = (WheelView) view
					// .findViewById(R.id.day);
				} else {
					mShowing = false;
					Calendar calendar = ProfileSettingsHelper.getBirthDate();
					String date = Utilities.getDateByCalendar(calendar);
					PreferenceEngine.getInstance(ctx).saveBirthday(date);
					b_day.setText(date);

					birth_day_holder.removeAllViews();
				}
			}
		});

		rlg = (TextView) findViewById(R.id.religion_edit_text);
		rlg.setTypeface(tf);
		final View religion_lay = findViewById(R.id.religion_lay);
		final FrameLayout religion_holder = (FrameLayout) findViewById(R.id.religion_holder);
		religion_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getReligionDialog().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							religion_holder, true);
					WheelView religionWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, rlg, religion,
							ProfileInfo.RELIGION, flag);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getSelfReligionInt();
					if (currentItem != 0) {
						religionWheel.setCurrentItem(currentItem);

					}
				} else {
					mShowing = false;
					int i = PreferenceEngine.getInstance(ctx)
							.getSelfReligionInt();
					rlg.setText(religion[i]);
					religion_holder.removeAllViews();
				}

			}
		});

		rtln = (TextView) findViewById(R.id.relation_edit_text);
		rtln.setTypeface(tf);
		final View relation_lay = findViewById(R.id.relation_lay);
		final FrameLayout relation_holder = (FrameLayout) findViewById(R.id.relation_holder);
		relation_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getRelationDialog().show();

				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							relation_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, rtln, relation,
							ProfileInfo.RELATIONSHIP, flag);
					WheelView relationWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getSelfRelationInt();
					if (currentItem != 0) {
						relationWheel.setCurrentItem(currentItem);

					}

				} else {
					mShowing = false;
					int i = PreferenceEngine.getInstance(ctx)
							.getSelfRelationInt();
					rtln.setText(relation[i]);
					relation_holder.removeAllViews();
				}

			}
		});

		hvChild = (TextView) findViewById(R.id.havechild_edit_text);
		hvChild.setTypeface(tf);
		final View have_child_lay = findViewById(R.id.havechild_lay);
		final FrameLayout have_child_holder = (FrameLayout) findViewById(R.id.have_child_holder);
		have_child_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getHaveChildDialog(false).show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							have_child_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, hvChild, hvChld,
							ProfileInfo.HAVECHILDREN, flag);
					WheelView haveChildWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					boolean currentItem = false;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getHaveChildren();
					if (currentItem == true) {
						haveChildWheel.setCurrentItem(0);
					} else {
						haveChildWheel.setCurrentItem(1);
					}
				} else {
					mShowing = false;
					boolean have_child = PreferenceEngine.getInstance(ctx)
							.getHaveChildren();
					if (have_child) {
						hvChild.setText(getResources().getString(
								R.string.IDS_YES));
					} else {
						hvChild.setText(getResources().getString(
								R.string.IDS_NO));
					}
					have_child_holder.removeAllViews();
				}

			}
		});

		wantChild = (TextView) findViewById(R.id.wantchild_edit_text);
		wantChild.setTypeface(tf);
		final View want_child_lay = findViewById(R.id.want_child_lay);
		final FrameLayout want_child_holder = (FrameLayout) findViewById(R.id.want_child_holder);
		want_child_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getHaveChildDialog(true).show();

				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							want_child_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, wantChild,
							wntChild, ProfileInfo.WANTCHILDREN, flag);
					WheelView wantChildWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					boolean currentItem = false;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getWantChildren();
					if (currentItem == true) {
						wantChildWheel.setCurrentItem(0);
					} else {
						wantChildWheel.setCurrentItem(1);
					}
				} else {
					mShowing = false;
					boolean wantchild_txt = PreferenceEngine.getInstance(ctx)
							.getWantChildren();
					if (wantchild_txt) {
						wantChild.setText(getResources().getString(
								R.string.IDS_YES));
					} else {
						wantChild.setText(getResources().getString(
								R.string.IDS_NO));
					}
					want_child_holder.removeAllViews();
				}
			}
		});

		hgt_foot = (TextView) findViewById(R.id.height_foot);
		hgt_inches = (TextView) findViewById(R.id.height_inches);
		hgt_foot.setTypeface(tf);
		hgt_inches.setTypeface(tf);
		final View height_lay = findViewById(R.id.height_lay);
		final FrameLayout height_holder = (FrameLayout) findViewById(R.id.height_holder);
		height_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getHeightDialog().show();
				Log.d(TAG, "Click on height");
				if (!mShowing) {

					View view = mLayoutInflator.inflate(R.layout.height_wheel,
							height_holder, true);

					WheelView footwheel = (WheelView) view
							.findViewById(R.id.foot_wheel);
					WheelView incheswheel = (WheelView) view
							.findViewById(R.id.inches_wheel);

					Log.d(TAG, "Get view");
					mShowing = true;
					ProfileSettingsHelper.initForHeight(view, hgt_foot,
							hgt_inches, foot, inches, flag);

					int footIndex = 0;
					footIndex = PreferenceEngine.getInstance(ctx)
							.getSelfHeightFootInt();
					if (footIndex != 0) {
						footwheel.setCurrentItem(footIndex);

					}

					int inchesIndex = 0;
					inchesIndex = PreferenceEngine.getInstance(ctx)
							.getSelfHeightInchesInt();
					if (inchesIndex != 0) {
						incheswheel.setCurrentItem(inchesIndex);

					}

				} else {
					mShowing = false;
					int footIndex = PreferenceEngine.getInstance(ctx)
							.getSelfHeightFootInt();
					int inchesIndex = PreferenceEngine.getInstance(ctx)
							.getSelfHeightInchesInt();
					String height_foot = foot[footIndex];
					String height_inches = inches[inchesIndex];
					hgt_foot.setText(height_foot);
					hgt_inches.setText(height_inches);
					height_holder.removeAllViews();
				}

			}
		});

		slf_body_type_txt = (TextView) findViewById(R.id.bodyType_edit_text);
		slf_body_type_txt.setTypeface(tf);
		final View body_type_child_lay = findViewById(R.id.bodyType_lay);
		final FrameLayout body_type_holder = (FrameLayout) findViewById(R.id.body_type_holder);
		body_type_child_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getSelfBodyType().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							body_type_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, slf_body_type_txt,
							bodyType, ProfileInfo.BODYTYPE, flag);
					WheelView bodyWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getSelfBodyTypeInt();
					if (currentItem != 0) {
						bodyWheel.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getSelfBodyTypeInt();
					slf_body_type_txt.setText(bodyType[index]);
					body_type_holder.removeAllViews();
				}

			}
		});

		slf_community_txt = (TextView) findViewById(R.id.comm_edit_text);
		slf_community_txt.setTypeface(tf);
		final View community_lay = findViewById(R.id.comm_lay);
		final FrameLayout community_holder = (FrameLayout) findViewById(R.id.community_holder);
		community_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getSelfCommunity().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							community_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, slf_community_txt,
							self_community, ProfileInfo.COMMUNITY, flag);
					WheelView communitWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getSelfCommunityInt();
					if (currentItem != 0) {
						communitWheel.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getSelfCommunityInt();
					slf_community_txt.setText(self_community[index]);
					community_holder.removeAllViews();
				}

			}
		});

		self_diet = (TextView) findViewById(R.id.diet_edit_text);
		self_diet.setTypeface(tf);
		final View diet_lay = findViewById(R.id.diet_lay);
		final FrameLayout diet_holder = (FrameLayout) findViewById(R.id.diet_holder);
		diet_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getSelfDiet().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							diet_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, self_diet,
							self_diet_list, ProfileInfo.DIET, flag);
					WheelView dietWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getSelfDietInt();
					if (currentItem != 0) {
						dietWheel.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getSelfDietInt();
					self_diet.setText(self_diet_list[index]);
					diet_holder.removeAllViews();
				}

			}
		});

		slf_lang = (TextView) findViewById(R.id.languages_edit_text);
		slf_lang.setTypeface(tf);
		slf_lang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelfLanguage().show();

			}
		});

		slf_smoking = (TextView) findViewById(R.id.smoke_edit_text);
		slf_smoking.setTypeface(tf);
		final View smoke_lay = findViewById(R.id.smoke_lay);
		final FrameLayout smoke_holder = (FrameLayout) findViewById(R.id.smoke_holder);
		smoke_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getSelfSmoking().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							smoke_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, slf_smoking,
							self_smoking, ProfileInfo.SMOKING, flag);
					WheelView smokingWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getSelfSmokingInt();
					if (currentItem != 0) {
						smokingWheel.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getSelfSmokingInt();
					slf_smoking.setText(self_smoking[index]);
					smoke_holder.removeAllViews();
				}

			}
		});

		slf_drinking = (TextView) findViewById(R.id.drink_edit_text);
		slf_drinking.setTypeface(tf);
		final View drink_lay = findViewById(R.id.drink_lay);
		final FrameLayout drink_holder = (FrameLayout) findViewById(R.id.drink_holder);
		drink_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getSelfDrinking().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							drink_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, slf_drinking,
							self_drinking, ProfileInfo.DRINGKING, flag);
					WheelView drinkingWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getSelfDrinkInt();
					if (currentItem != 0) {
						drinkingWheel.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getSelfDrinkInt();
					slf_drinking.setText(self_drinking[index]);
					drink_holder.removeAllViews();
				}

			}
		});

		slf_eduction = (TextView) findViewById(R.id.education_text);
		slf_eduction.setTypeface(tf);
		final View education_lay = findViewById(R.id.education_lay);
		final FrameLayout education_holder = (FrameLayout) findViewById(R.id.education_holder);
		education_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getSelfEducation().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							education_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, slf_eduction,
							self_education, ProfileInfo.EDUCATION, flag);
					WheelView educationWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getSelfEducationInt();
					if (currentItem != 0) {
						educationWheel.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getSelfEducationInt();
					slf_eduction.setText(self_education[index]);
					education_holder.removeAllViews();
				}

			}
		});

		slf_salary = (TextView) findViewById(R.id.salary_edit_text);
		slf_salary.setTypeface(tf);
		final View salary_lay = findViewById(R.id.salary_lay);
		final FrameLayout salary_holder = (FrameLayout) findViewById(R.id.salary_holder);
		salary_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getSelfSalary().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							salary_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, slf_salary,
							self_salary, ProfileInfo.SALARY, flag);
					WheelView salaryWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getSelfSalaryInt();
					if (currentItem != 0) {
						salaryWheel.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getSelfSalaryInt();
					slf_salary.setText(self_salary[index]);
					salary_holder.removeAllViews();
				}

			}
		});

		wnt_age = (TextView) findViewById(R.id.match_birthday_edit_text);
		wnt_age.setTypeface(tf);
		final View match_age_range_lay = findViewById(R.id.match_age_range_lay);
		final FrameLayout match_age_range_holder = (FrameLayout) findViewById(R.id.match_age_range_holder);
		match_age_range_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getWantAge().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							match_age_range_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, wnt_age,
							want_age_range, ProfileInfo.AGE_RANGE, flag);
					WheelView wantAgeWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getWantAgeInt();
					if (currentItem != 0) {
						wantAgeWheel.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getWantAgeInt();
					wnt_age.setText(want_age_range[index]);
					match_age_range_holder.removeAllViews();
				}

			}
		});

	}

	private void matchListner() {
		final int flag = 0;
		mMatch_Rlg = (TextView) findViewById(R.id.match_religion_edit_text);
		mMatch_Rlg.setTypeface(tf);
		final View match_religion_lay = findViewById(R.id.match_religion_lay);
		final FrameLayout match_religion_holder = (FrameLayout) findViewById(R.id.match_religion_holder);
		match_religion_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getMatchReligionDialog().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							match_religion_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, mMatch_Rlg,
							religion, ProfileInfo.RELIGION, flag);
					WheelView matchRlg = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getMatchReligionInt();
					if (currentItem != 0) {
						matchRlg.setCurrentItem(currentItem);
					}

				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getMatchReligionInt();
					mMatch_Rlg.setText(religion[index]);
					match_religion_holder.removeAllViews();
				}
			}
		});

		mMatch_Rtln = (TextView) findViewById(R.id.match_relation_edit_text);
		mMatch_Rtln.setTypeface(tf);
		final View match_relation_lay = findViewById(R.id.match_relation_lay);
		final FrameLayout match_relation_holder = (FrameLayout) findViewById(R.id.match_relation_holder);
		match_relation_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getMatchRelationDialog().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							match_relation_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, mMatch_Rtln,
							relation, ProfileInfo.RELATIONSHIP, flag);
					WheelView matchRelation = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getMatchRelationInt();
					if (currentItem != 0) {
						matchRelation.setCurrentItem(currentItem);
					}

				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getMatchRelationInt();
					mMatch_Rtln.setText(relation[index]);
					match_relation_holder.removeAllViews();
				}

			}
		});

		mMatchHvChlid = (TextView) findViewById(R.id.match_havechild_edit_text);
		mMatchHvChlid.setTypeface(tf);
		final View match_havechild_lay = findViewById(R.id.match_havechild_lay);
		final FrameLayout match_havechild_holder = (FrameLayout) findViewById(R.id.match_havechild_holder);
		match_havechild_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getMatchHaveChildDialog(false).show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							match_havechild_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, mMatchHvChlid,
							hvChld, ProfileInfo.HAVECHILDREN, 0);
					WheelView matchHaveChildWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					boolean currentItem = false;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getMatchHaveChildren();
					if (currentItem == true) {
						matchHaveChildWheel.setCurrentItem(0);
					} else {
						matchHaveChildWheel.setCurrentItem(1);
					}
				} else {
					mShowing = false;
					boolean have_child = PreferenceEngine.getInstance(ctx)
							.getMatchHaveChildren();
					if (have_child) {
						mMatchHvChlid.setText(getResources().getString(
								R.string.IDS_YES));
					} else {
						mMatchHvChlid.setText(getResources().getString(
								R.string.IDS_NO));
					}
					match_havechild_holder.removeAllViews();
				}

			}
		});

		mMatchWntChildTxt = (TextView) findViewById(R.id.match_wantchild_edit_text);
		mMatchWntChildTxt.setTypeface(tf);
		final View match_wantchild_lay = findViewById(R.id.match_wantchild_lay);
		final FrameLayout match_wantchild_holder = (FrameLayout) findViewById(R.id.match_wantchild_holder);
		match_wantchild_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getMatchHaveChildDialog(true).show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							match_wantchild_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, mMatchWntChildTxt,
							wntChild, ProfileInfo.WANTCHILDREN, flag);
					WheelView matchWantChildWheel = (WheelView) view
							.findViewById(R.id.single_wheel);
					boolean currentItem = false;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getMatchWantChildren();
					if (currentItem == true) {
						matchWantChildWheel.setCurrentItem(0);
					} else {
						matchWantChildWheel.setCurrentItem(1);
					}
				} else {
					mShowing = false;
					boolean have_child = PreferenceEngine.getInstance(ctx)
							.getMatchWantChildren();
					if (have_child) {
						mMatchWntChildTxt.setText(getResources().getString(
								R.string.IDS_YES));
					} else {
						mMatchWntChildTxt.setText(getResources().getString(
								R.string.IDS_NO));
					}
					match_wantchild_holder.removeAllViews();
				}

			}
		});

		mMatchHeightFoot = (TextView) findViewById(R.id.match_height_foot);
		mMatchHeightFoot.setTypeface(tf);
		mMatchHeightInches = (TextView) findViewById(R.id.match_height_inches);
		mMatchHeightInches.setTypeface(tf);
		final View match_height_lay = findViewById(R.id.match_height_lay);
		final FrameLayout match_height_holder = (FrameLayout) findViewById(R.id.match_height_holder);
		match_height_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getMatchHeightDialog().show();
				if (!mShowing) {

					View view = mLayoutInflator.inflate(R.layout.height_wheel,
							match_height_holder, true);
					Log.d(TAG, "Get view");
					mShowing = true;
					ProfileSettingsHelper.initForHeight(view, mMatchHeightFoot,
							mMatchHeightInches, foot, inches, flag);
					Log.d(TAG, "success of init for height");

					WheelView footwheel = (WheelView) view
							.findViewById(R.id.foot_wheel);
					WheelView incheswheel = (WheelView) view
							.findViewById(R.id.inches_wheel);

					int footIndex = 0;
					footIndex = PreferenceEngine.getInstance(ctx)
							.getMatchHeightFootInt();
					if (footIndex != 0) {
						footwheel.setCurrentItem(footIndex);

					}

					int inchesIndex = 0;
					inchesIndex = PreferenceEngine.getInstance(ctx)
							.getMatchHeightInchesInt();
					if (inchesIndex != 0) {
						incheswheel.setCurrentItem(inchesIndex);

					}
				} else {
					mShowing = false;
					int footIndex = PreferenceEngine.getInstance(ctx)
							.getMatchHeightFootInt();
					int inchesIndex = PreferenceEngine.getInstance(ctx)
							.getMatchHeightInchesInt();
					String height_foot = foot[footIndex];
					String height_inches = inches[inchesIndex];
					mMatchHeightFoot.setText(height_foot);
					mMatchHeightInches.setText(height_inches);
					match_height_holder.removeAllViews();
				}

			}
		});
		mMatch_Body_Type = (TextView) findViewById(R.id.match_bodyType_edit_text);
		mMatch_Body_Type.setTypeface(tf);
		final View match_bodyType_lay = findViewById(R.id.match_bodyType_lay);
		final FrameLayout match_bodyType_holder = (FrameLayout) findViewById(R.id.match_bodyType_holder);
		match_bodyType_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getMatchBodyType().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							match_bodyType_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, mMatch_Body_Type,
							bodyType, ProfileInfo.BODYTYPE, flag);
					WheelView matchBodyType = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getMatchBodyTypeInt();
					if (currentItem != 0) {
						matchBodyType.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getMatchBodyTypeInt();
					mMatch_Body_Type.setText(bodyType[index]);
					match_bodyType_holder.removeAllViews();
				}

			}
		});

		mMatch_Community = (TextView) findViewById(R.id.match_comm_edit_text);
		mMatch_Community.setTypeface(tf);
		final View match_comm_lay = findViewById(R.id.match_comm_lay);
		final FrameLayout match_comm_holder = (FrameLayout) findViewById(R.id.match_comm_holder);
		match_comm_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getMatchCommunity().show();

				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							match_comm_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, mMatch_Community,
							self_community, ProfileInfo.COMMUNITY, flag);
					WheelView matchCommunity = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getMatchCommunityInt();
					if (currentItem != 0) {
						matchCommunity.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getMatchCommunityInt();
					mMatch_Community.setText(self_community[index]);
					match_comm_holder.removeAllViews();
				}
			}
		});

		mMatch_Diet = (TextView) findViewById(R.id.match_diet_edit_text);
		mMatch_Diet.setTypeface(tf);
		final View match_diet_lay = findViewById(R.id.match_diet_lay);
		final FrameLayout match_diet_holder = (FrameLayout) findViewById(R.id.match_diet_holder);
		match_diet_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getMatchDiet().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							match_diet_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, mMatch_Diet,
							self_diet_list, ProfileInfo.DIET, flag);
					WheelView matchDiet = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getMatchDietInt();
					if (currentItem != 0) {
						matchDiet.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getMatchDietInt();
					mMatch_Diet.setText(self_diet_list[index]);
					match_diet_holder.removeAllViews();
				}

			}
		});

		mMatch_Smoking = (TextView) findViewById(R.id.match_smoke_edit_text);
		mMatch_Smoking.setTypeface(tf);
		final View match_smoke_lay = findViewById(R.id.match_smoke_lay);
		final FrameLayout match_smoke_holder = (FrameLayout) findViewById(R.id.match_smoke_holder);
		match_smoke_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getMatchSmoking().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							match_smoke_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, mMatch_Smoking,
							self_smoking, ProfileInfo.SMOKING, flag);
					WheelView matchSmoking = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getMatchSmokingInt();
					if (currentItem != 0) {
						matchSmoking.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getMatchSmokingInt();
					mMatch_Smoking.setText(self_smoking[index]);
					match_smoke_holder.removeAllViews();
				}

			}
		});
		mMatch_Drinking = (TextView) findViewById(R.id.match_drink_edit_text);
		mMatch_Drinking.setTypeface(tf);
		final View match_drink_lay = findViewById(R.id.match_drink_lay);
		final FrameLayout match_drink_holder = (FrameLayout) findViewById(R.id.match_drink_holder);
		match_drink_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getMatchDrinking().show();
				if (!mShowing) {
					View view = mLayoutInflator.inflate(R.layout.wheel_single,
							match_drink_holder, true);
					mShowing = true;
					ProfileSettingsHelper.initForView(view, mMatch_Drinking,
							self_drinking, ProfileInfo.DRINGKING, flag);
					WheelView matchDrinking = (WheelView) view
							.findViewById(R.id.single_wheel);
					int currentItem = 0;
					currentItem = PreferenceEngine.getInstance(ctx)
							.getMatchDrinkInt();
					if (currentItem != 0) {
						matchDrinking.setCurrentItem(currentItem);
					}
				} else {
					mShowing = false;
					int index = PreferenceEngine.getInstance(ctx)
							.getMatchDrinkInt();
					mMatch_Drinking.setText(self_drinking[index]);
					match_drink_holder.removeAllViews();
				}

			}
		});

		mMatch_Lang = (TextView) findViewById(R.id.match_languages_edit_text);
		mMatch_Lang.setTypeface(tf);
		mMatch_Lang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMatchLanguage().show();

			}
		});
	}

	private void initView() {

		// Birthday date.
		String birthday = PreferenceEngine.getInstance(this).getBirthday();
		if (birthday != null) {
			b_day.setText(birthday);
		}

		// Self Religion.
		String religion_txt = PreferenceEngine.getInstance(this)
				.getSelfReligion();

		if (religion_txt != null) {
			rlg.setText(religion_txt);

		}

		// Self Relation.

		String relation_txt = PreferenceEngine.getInstance(this)
				.getSelfRelation();

		if (relation_txt != null) {
			rtln.setText(relation_txt);

		}

		// Self have children.
		boolean havechild_txt = PreferenceEngine.getInstance(this)
				.getHaveChildren();
		if (havechild_txt) {
			hvChild.setText(getResources().getString(R.string.IDS_YES));
		} else {
			hvChild.setText(getResources().getString(R.string.IDS_NO));
		}

		// Self want children.
		boolean wantchild_txt = PreferenceEngine.getInstance(this)
				.getWantChildren();
		if (havechild_txt) {
			hvChild.setText(getResources().getString(R.string.IDS_YES));
		} else {
			hvChild.setText(getResources().getString(R.string.IDS_NO));
		}
		if (wantchild_txt) {
			wantChild.setText(getResources().getString(R.string.IDS_YES));
		} else {
			wantChild.setText(getResources().getString(R.string.IDS_NO));
		}

		// Self height
		String foot_txt = PreferenceEngine.getInstance(ctx).getSelfHeightFoot();
		String inches_txt = PreferenceEngine.getInstance(ctx)
				.getSelfHeightInches();
		String height_txt;
		if (foot_txt == null || inches_txt == null) {
			height_txt = null;
		} else {
			height_txt = foot_txt + " " + inches_txt;
		}

		if (height_txt != null) {
			hgt_foot.setText(foot_txt);
			hgt_inches.setText(inches_txt);
		}

		// Self body type.
		String body_type_txt = PreferenceEngine.getInstance(ctx)
				.getSelfBodyType();
		if (body_type_txt != null) {
			slf_body_type_txt.setText(body_type_txt);
		}

		// Self Community
		String self_community_txt = PreferenceEngine.getInstance(ctx)
				.getSelfCommunity();
		if (self_community_txt != null) {
			slf_community_txt.setText(self_community_txt);
		}

		// Self diet.
		String self_diet_txt = PreferenceEngine.getInstance(ctx).getSelfDiet();
		if (self_diet_txt != null) {
			self_diet.setText(self_diet_txt);
		}

		// Self language.
		String self_lang_txt = PreferenceEngine.getInstance(ctx)
				.getSelfLangList();
		if (self_lang_txt != null) {
			slf_lang.setText(self_lang_txt);
		}

		// Self smoking
		String self_smok_txt = PreferenceEngine.getInstance(ctx)
				.getSelfSmoking();
		if (self_smok_txt != null) {
			slf_smoking.setText(self_smok_txt);
		}

		// Self drinking.
		String self_drinking_txt = PreferenceEngine.getInstance(ctx)
				.getSelfDrinking();
		if (self_drinking_txt != null) {
			slf_drinking.setText(self_drinking_txt);
		}

		// Self education
		String self_education_txt = PreferenceEngine.getInstance(ctx)
				.getSelfEducation();
		if (self_education_txt != null) {
			slf_eduction.setText(self_education_txt);
		}

		// Self salary.
		String self_salary_txt = PreferenceEngine.getInstance(ctx)
				.getSelfSalary();
		if (self_salary_txt != null) {
			slf_salary.setText(self_salary_txt);
		}

		// Want age
		String wnt_age_txt = PreferenceEngine.getInstance(ctx).getWantAge();
		if (wnt_age_txt != null) {
			wnt_age.setText(wnt_age_txt);
		}
	}

	protected Dialog getWantAge() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx).getWantAgeInt();
		Dialog want_age_dialog = builder
				.setTitle(this.getString(R.string.IDS_AgeRange_Label))
				.setSingleChoiceItems(want_age_range, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeWntAge(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return want_age_dialog;
	}

	protected void changeWntAge(int mCurrentWntAge2) {
		String selectedWntAge = want_age_range[mCurrentWntAge2].toString();
		PreferenceEngine.getInstance(ctx).setWantAge(selectedWntAge);
		PreferenceEngine.getInstance(ctx).setWantAgeInt(mCurrentWntAge2);
		initView();

	}

	protected Dialog getSelfSalary() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int chekedItem = PreferenceEngine.getInstance(ctx).getSelfSalaryInt();
		Dialog self_salary_dialog = builder
				.setTitle(this.getString(R.string.IDS_Salary_Label))
				.setSingleChoiceItems(self_salary, chekedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeSelfSalary(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return self_salary_dialog;
	}

	protected void changeSelfSalary(int mCurrentSlfSalary2) {
		String selectedSlfSalary = self_salary[mCurrentSlfSalary2].toString();
		PreferenceEngine.getInstance(ctx).setSelfSalary(selectedSlfSalary);
		PreferenceEngine.getInstance(ctx).setSelfSalaryInt(mCurrentSlfSalary2);
		initView();

	}

	protected Dialog getSelfEducation() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx)
				.getSelfEducationInt();
		Dialog self_education_dialog = builder
				.setTitle(this.getString(R.string.IDS_Education_Label))
				.setSingleChoiceItems(self_education, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeSelfEducation(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return self_education_dialog;
	}

	protected void changeSelfEducation(int mCurrentSlfEducation2) {
		String selectedSlfEducation = self_education[mCurrentSlfEducation2]
				.toString();
		PreferenceEngine.getInstance(ctx)
				.setSelfEducation(selectedSlfEducation);
		PreferenceEngine.getInstance(ctx).setSelfEducationInt(
				mCurrentSlfEducation2);
		initView();

	}

	protected Dialog getSelfSmoking() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx).getSelfSmokingInt();
		Dialog self_smoking_dialog = builder
				.setTitle(this.getString(R.string.smoke_label))
				.setSingleChoiceItems(self_smoking, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeSelfSmoking(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return self_smoking_dialog;
	}

	protected void changeSelfSmoking(int mCurrentSlfSmoking2) {
		String selectedSlfSmoking = self_smoking[mCurrentSlfSmoking2]
				.toString();
		PreferenceEngine.getInstance(ctx).setSelfSmoking(selectedSlfSmoking);
		PreferenceEngine.getInstance(ctx)
				.setSelfSmokingInt(mCurrentSlfSmoking2);
		initView();

	}

	// protected void changeMatchSmoking(int mCurrentSlfSmoking2) {
	// String selectedSlfSmoking = mMatchSmoking[mCurrentSlfSmoking2]
	// .toString();
	// PreferenceEngine.getInstance(ctx).setMatchSmoking(selectedSlfSmoking);
	// PreferenceEngine.getInstance(ctx).setMatchSmokingInt(
	// mCurrentSlfSmoking2);
	// matchInitView();
	//
	// }

	protected Dialog getSelfDrinking() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx).getSelfDrinkInt();
		Dialog self_drinking_dialog = builder
				.setTitle(this.getString(R.string.drink_label))
				.setSingleChoiceItems(self_drinking, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeSelfDrinking(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return self_drinking_dialog;
	}

	protected void changeSelfDrinking(int mCurrentSlfDriking2) {
		String selectedSlfDrinking = self_drinking[mCurrentSlfDriking2]
				.toString();
		PreferenceEngine.getInstance(ctx).setSelfDrinking(selectedSlfDrinking);
		PreferenceEngine.getInstance(ctx).setSelfDrinkingInt(
				mCurrentSlfDriking2);
		initView();

	}

	// protected void changeMatchDrinking(int mCurrentSlfDriking2) {
	// String selectedSlfDrinking = mMatchDrinking[mCurrentSlfDriking2]
	// .toString();
	// PreferenceEngine.getInstance(ctx).setMatchDrinking(selectedSlfDrinking);
	// PreferenceEngine.getInstance(ctx).setMatchDrinkingInt(
	// mCurrentSlfDriking2);
	// matchInitView();
	//
	// }

	protected Dialog getSelfDiet() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx).getSelfDietInt();
		Dialog self_diet_dialog = builder
				.setTitle(this.getString(R.string.diet_label))
				.setSingleChoiceItems(self_diet_list, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeSelfDiet(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return self_diet_dialog;
	}

	protected Dialog getSelfLanguage() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mCurrentSlfLang = PreferenceEngine.getInstance(ctx).getSelfLangBoolean(
				language_list);
		Dialog self_lang_dialog = builder
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
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return self_lang_dialog;
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
				toSave.append(language_list[i]).append(separator);
			}
		}
		PreferenceEngine.getInstance(ctx).setMatchLangList(toSave.toString());
		matchInitView();
	}

	protected Dialog getSelfCommunity() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx)
				.getSelfCommunityInt();
		Dialog self_community_dialog = builder
				.setTitle(this.getString(R.string.comm_label))
				.setSingleChoiceItems(self_community, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeSelfComm(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return self_community_dialog;
	}

	protected Dialog getSelfBodyType() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(ctx)
				.getSelfBodyTypeInt();
		Dialog bodytype_dialog = builder
				.setTitle(this.getString(R.string.bodyType_label))
				.setSingleChoiceItems(bodyType, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeSelfBodyType(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return bodytype_dialog;
	}

	protected void changeSelfBodyType(int mCurrentBdyType2) {
		String selectedBodyType = bodyType[mCurrentBdyType2].toString();
		PreferenceEngine.getInstance(ctx).setSelfBodyType(selectedBodyType);
		PreferenceEngine.getInstance(ctx).setSelfBodyTypeInt(mCurrentBdyType2);
		initView();

	}

	// protected void changeMatchBodyType(int mCurrentBdyType2) {
	// String selectedBodyType = mMatchBodyType[mCurrentBdyType2].toString();
	// PreferenceEngine.getInstance(ctx).setMatchBodyType(selectedBodyType);
	// PreferenceEngine.getInstance(ctx).setMatchBodyTypeInt(mCurrentBdyType2);
	// matchInitView();
	//
	// }

	protected void changeSelfComm(int mCurrentBdyType2) {
		String selectedSlfComm = self_community[mCurrentBdyType2].toString();
		PreferenceEngine.getInstance(ctx).setSelfCommunity(selectedSlfComm);
		PreferenceEngine.getInstance(ctx).setSelfCommunityInt(mCurrentBdyType2);
		initView();

	}

	// protected void changeMatchComm(int mCurrentBdyType2) {
	// String selectedSlfComm = mMatchCommunity[mCurrentBdyType2].toString();
	// PreferenceEngine.getInstance(ctx).setMatchCommunity(selectedSlfComm);
	// PreferenceEngine.getInstance(ctx)
	// .setMatchCommunityInt(mCurrentBdyType2);
	// matchInitView();
	//
	// }

	protected void changeSelfDiet(int mCurrentSlfDiet2) {
		String selectedSlfDiet = self_diet_list[mCurrentSlfDiet2].toString();
		PreferenceEngine.getInstance(ctx).setSelfDiet(selectedSlfDiet);
		PreferenceEngine.getInstance(ctx).setSelfDietInt(mCurrentSlfDiet2);
		initView();

	}

	// protected void changeMatchDiet(int mCurrentSlfDiet2) {
	// String selectedSlfDiet = mMatchDietList[mCurrentSlfDiet2].toString();
	// PreferenceEngine.getInstance(ctx).setMatchDiet(selectedSlfDiet);
	// PreferenceEngine.getInstance(ctx).setMatchDietInt(mCurrentSlfDiet2);
	// matchInitView();
	//
	// }

	protected Dialog getHaveChildDialog(final boolean wntChld) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));
		String label = null;
		int checkedItem = -1;

		if (wntChld) {
			label = this.getString(R.string.wantchild_label);
			checkedItem = PreferenceEngine.getInstance(ctx).getWantChildren() ? 0
					: 1;
		} else {
			label = this.getString(R.string.havechild_label);
			checkedItem = PreferenceEngine.getInstance(ctx).getHaveChildren() ? 0
					: 1;
		}

		Dialog have_child_dialog = builder
				.setTitle(label)
				.setSingleChoiceItems(hvChld, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if (wntChld) {
									changeSelfWntChild(whichButton);
								} else {
									changeSelfHaveChild(whichButton);
								}
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return have_child_dialog;
	}

	protected Dialog getRelationDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(this)
				.getSelfRelationInt();
		Dialog relation_dialog = builder
				.setTitle(this.getString(R.string.relation_label))
				.setSingleChoiceItems(relation, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								changeSelfRelation(whichButton);
							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return relation_dialog;
	}

	private Dialog getReligionDialog() {

		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		int checkedItem = PreferenceEngine.getInstance(this)
				.getSelfReligionInt();
		Dialog religion_dialog = builder
				.setTitle(this.getString(R.string.religion_label))
				.setSingleChoiceItems(religion, checkedItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								changeSelfReligion(whichButton);

							}
						})
				.setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
				.create();

		return religion_dialog;
	}

	protected void changeSelfWntChild(int mCurrentHvChld2) {
		PreferenceEngine.getInstance(ctx)
				.saveWantChildren(mCurrentHvChld2 == 0);
		initView();
	}

	protected void changeMatchWntChild(int mCurrentHvChld2) {
		PreferenceEngine.getInstance(ctx).saveMatchWantChildren(
				mCurrentHvChld2 == 0);
		matchInitView();
	}

	protected void changeSelfReligion(int mCurrentRlg2) {
		String selectedReligion = religion[mCurrentRlg2].toString();
		PreferenceEngine.getInstance(ctx).setSelfReligion(selectedReligion);
		PreferenceEngine.getInstance(ctx).setSelfReligionInt(mCurrentRlg2);
		initView();
	}

	protected void changeSelfRelation(int mCurrentRtn) {
		String selectedRelation = relation[mCurrentRtn].toString();
		PreferenceEngine.getInstance(ctx).setSelfRelation(selectedRelation);
		PreferenceEngine.getInstance(ctx).setSelfRelationInt(mCurrentRtn);
		initView();
	}

	// protected void changeMatchRelation(int mCurrentRtn) {
	// String selectedRelation = mMatchRelation[mCurrentRtn].toString();
	// PreferenceEngine.getInstance(ctx).setMatchRelation(selectedRelation);
	// PreferenceEngine.getInstance(ctx).setMatchRelationInt(mCurrentRtn);
	// matchInitView();
	// }

	protected void changeSelfHaveChild(int mCurrentHvChld2) {
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

	// protected Dialog getHeightDialog() {
	// return new CustomerNumberPickerDialog(new AfterYouDialogImpl(this),
	// this, R.style.Theme_CustomDialog, mValuelistener, foot,
	// PreferenceEngine.getInstance(ctx).getSelfHeightInt());
	// }

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mCalendar.set(year, monthOfYear, dayOfMonth);
			String date = Utilities.getDateByCalendar(mCalendar);
			PreferenceEngine.getInstance(ctx).saveBirthday(date);
			initView();
		}
	};

	// private NumberPicker.OnValueChangeListener mValuelistener = new
	// NumberPicker.OnValueChangeListener() {
	//
	// @Override
	// public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
	// if (oldVal != newVal) {
	// PreferenceEngine.getInstance(ctx).setSelfHeightInt(newVal);
	// PreferenceEngine.getInstance(ctx).setSelfHeight(foot[newVal]);
	// }
	// initView();
	//
	// }
	// };
	// private NumberPicker.OnValueChangeListener mMatchValuelistener = new
	// NumberPicker.OnValueChangeListener() {
	//
	// @Override
	// public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
	// if (oldVal != newVal) {
	// PreferenceEngine.getInstance(ctx).setMatchHeightInt(newVal);
	// PreferenceEngine.getInstance(ctx).setMatchHeight(
	// mMatchfoot[newVal]);
	// }
	// matchInitView();
	//
	// }
	// };

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
		case R.id.done_btn:
			if (b_day.getText() != null && b_day.getText().length() > 0
					&& rlg.getText() != null && rlg.getText().length() > 0
					&& rtln.getText() != null && rtln.getText().length() > 0) {
				intent = new Intent(ProfileSettingsActivity.this,
						CapturePictureActivity.class);
				if (isFacebook) {
					if (isTest) {
						PreferenceEngine.getInstance(ctx).setUsername(
								"peace_manav@def.com");
						PreferenceEngine.getInstance(ctx).setPassword("peace");
					}
					// TODO Set Facebook Username
					intent.putExtra(AppConstants.FACEBOOK_USER, true);
				}
				startActivity(intent);
			} else {
				showErrorDialog();
			}
			break;
		}

	}

	// protected void changeMatchReligion(int mCurrentRlg2) {
	// String selectedReligion = mMatchReligion[mCurrentRlg2].toString();
	// PreferenceEngine.getInstance(ctx).setMatchReligion(selectedReligion);
	// PreferenceEngine.getInstance(ctx).setMatchReligionInt(mCurrentRlg2);
	// matchInitView();
	// }

	private void matchInitView() {

		// set match religion
		String matchReligion_txt = PreferenceEngine.getInstance(this)
				.getMatchReligion();
		if (matchReligion_txt != null) {
			mMatch_Rlg.setText(matchReligion_txt);
		}

		// set match relationship
		String relation_txt = PreferenceEngine.getInstance(this)
				.getMatchRelation();
		if (relation_txt != null) {
			mMatch_Rtln.setText(relation_txt);
		}

		// set match have child
		boolean havechild_txt = PreferenceEngine.getInstance(this)
				.getMatchHaveChildren();
		if (havechild_txt) {
			mMatchHvChlid.setText(getResources().getString(R.string.IDS_YES));
		} else {
			mMatchHvChlid.setText(getResources().getString(R.string.IDS_NO));
		}

		// set match want child
		boolean wantchild_txt = PreferenceEngine.getInstance(this)
				.getMatchWantChildren();
		if (havechild_txt) {
			mMatchHvChlid.setText(getResources().getString(R.string.IDS_YES));
		} else {
			mMatchHvChlid.setText(getResources().getString(R.string.IDS_NO));
		}
		if (wantchild_txt) {
			mMatchWntChildTxt.setText(getResources()
					.getString(R.string.IDS_YES));
		} else {
			mMatchWntChildTxt
					.setText(getResources().getString(R.string.IDS_NO));
		}

		// set match height
		String foot_txt = PreferenceEngine.getInstance(ctx)
				.getMatchHeightFoot();
		String inches_txt = PreferenceEngine.getInstance(ctx)
				.getMatchHeightInches();
		String height_txt;
		if (foot_txt == null || inches_txt == null) {
			height_txt = null;
		} else {
			height_txt = foot_txt + " " + inches_txt;
		}
		if (height_txt != null) {
			mMatchHeightFoot.setText(foot_txt);
			mMatchHeightInches.setText(inches_txt);
		}

		// set match body type
		String body_type_txt = PreferenceEngine.getInstance(ctx)
				.getMatchBodyType();
		if (body_type_txt != null) {
			mMatch_Body_Type.setText(body_type_txt);
		}

		// set match community type
		String match_community_txt = PreferenceEngine.getInstance(ctx)
				.getMatchCommunity();
		if (match_community_txt != null) {
			mMatch_Community.setText(match_community_txt);
		}

		// set match diet
		String match_diet_txt = PreferenceEngine.getInstance(ctx)
				.getMatchDiet();
		if (match_diet_txt != null) {
			mMatch_Diet.setText(match_diet_txt);
		}

		// set match smoking
		String match_smok_txt = PreferenceEngine.getInstance(ctx)
				.getMatchSmoking();
		if (match_smok_txt != null) {
			mMatch_Smoking.setText(match_smok_txt);
		}

		// set match drinking
		String match_drinking_txt = PreferenceEngine.getInstance(ctx)
				.getMatchDrinking();
		if (match_drinking_txt != null) {
			mMatch_Drinking.setText(match_drinking_txt);
		}

		// set match language
		String match_lang_txt = PreferenceEngine.getInstance(ctx)
				.getMatchLangList();
		if (match_lang_txt != null) {
			mMatch_Lang.setText(match_lang_txt);
		}

	}

	protected Dialog getMatchLanguage() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				new AfterYouDialogImpl.AfterYouBuilderImpl(this));

		mMatchCurrentLagn = PreferenceEngine.getInstance(ctx)
				.getMatchLangBoolean(language_list);
		Dialog match_languages = builder
				.setTitle(this.getString(R.string.languages_label))
				.setMultiChoiceItems(language_list, mMatchCurrentLagn,
						new OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
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

	// protected Dialog getMatchDrinking() {
	// CustomDialog.Builder builder = new CustomDialog.Builder(this,
	// new AfterYouDialogImpl.AfterYouBuilderImpl(this));
	//
	// int checkedItem = PreferenceEngine.getInstance(ctx).getMatchDrinkInt();
	// Dialog match_drinking = builder
	// .setTitle(this.getString(R.string.drink_label))
	// .setSingleChoiceItems(mMatchDrinking, checkedItem,
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	//
	// changeMatchDrinking(whichButton);
	// }
	// })
	// .setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
	// .create();
	//
	// return match_drinking;
	// }

	// protected Dialog getMatchSmoking() {
	// CustomDialog.Builder builder = new CustomDialog.Builder(this,
	// new AfterYouDialogImpl.AfterYouBuilderImpl(this));
	//
	// int checkedItem = PreferenceEngine.getInstance(ctx)
	// .getMatchSmokingInt();
	// Dialog match_smoking = builder
	// .setTitle(this.getString(R.string.smoke_label))
	// .setSingleChoiceItems(mMatchSmoking, checkedItem,
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	//
	// changeMatchSmoking(whichButton);
	// }
	// })
	// .setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
	// .create();
	//
	// return match_smoking;
	// }

	// protected Dialog getMatchDiet() {
	// CustomDialog.Builder builder = new CustomDialog.Builder(this,
	// new AfterYouDialogImpl.AfterYouBuilderImpl(this));
	//
	// int checkedItem = PreferenceEngine.getInstance(ctx).getMatchDietInt();
	// Dialog diet = builder
	// .setTitle(this.getString(R.string.diet_label))
	// .setSingleChoiceItems(mMatchDietList, checkedItem,
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	// changeMatchDiet(whichButton);
	// }
	// })
	// .setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
	// .create();
	//
	// return diet;
	// }

	// protected Dialog getMatchCommunity() {
	// CustomDialog.Builder builder = new CustomDialog.Builder(this,
	// new AfterYouDialogImpl.AfterYouBuilderImpl(this));
	//
	// int checkedItem = PreferenceEngine.getInstance(ctx)
	// .getMatchCommunityInt();
	// Dialog community = builder
	// .setTitle(this.getString(R.string.comm_label))
	// .setSingleChoiceItems(mMatchCommunity, checkedItem,
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	//
	// changeMatchComm(whichButton);
	// }
	// })
	// .setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
	// .create();
	//
	// return community;
	// }

	// protected Dialog getMatchBodyType() {
	// CustomDialog.Builder builder = new CustomDialog.Builder(this,
	// new AfterYouDialogImpl.AfterYouBuilderImpl(this));
	//
	// int checkedItem = PreferenceEngine.getInstance(ctx)
	// .getMatchBodyTypeInt();
	// Dialog body_type = builder
	// .setTitle(this.getString(R.string.bodyType_label))
	// .setSingleChoiceItems(mMatchBodyType, checkedItem,
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	//
	// changeMatchBodyType(whichButton);
	// }
	// })
	// .setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
	// .create();
	//
	// return body_type;
	// }

	// protected Dialog getMatchHeightDialog() {
	// return new CustomerNumberPickerDialog(new AfterYouDialogImpl(this),
	// this, R.style.Theme_CustomDialog, mMatchValuelistener,
	// mMatchfoot, PreferenceEngine.getInstance(this)
	// .getMatchHeightInt());
	// }

	// protected Dialog getMatchHaveChildDialog(final boolean wntChld) {
	// CustomDialog.Builder builder = new CustomDialog.Builder(this,
	// new AfterYouDialogImpl.AfterYouBuilderImpl(this));
	// String label = null;
	// int checkedItem = -1;
	// if (wntChld) {
	// label = this.getString(R.string.wantchild_label);
	// checkedItem = PreferenceEngine.getInstance(ctx)
	// .getMatchWantChildren() ? 0 : 1;
	// } else {
	// label = this.getString(R.string.havechild_label);
	// checkedItem = PreferenceEngine.getInstance(ctx)
	// .getMatchHaveChildren() ? 0 : 1;
	// }
	//
	// Dialog haveChildDialog = builder
	// .setTitle(label)
	// .setSingleChoiceItems(mMatchHvChild, checkedItem,
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	// if (wntChld) {
	// changeMatchWntChild(whichButton);
	// } else {
	// changeMatchHaveChild(whichButton);
	// }
	// }
	// })
	// .setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
	// .create();
	//
	// return haveChildDialog;
	// }

	// private Dialog getMatchReligionDialog() {
	//
	// CustomDialog.Builder builder = new CustomDialog.Builder(this,
	// new AfterYouDialogImpl.AfterYouBuilderImpl(this));
	//
	// int checkedItem = PreferenceEngine.getInstance(ctx)
	// .getMatchReligionInt();
	// Dialog religion = builder
	// .setTitle(this.getString(R.string.religion_label))
	// .setSingleChoiceItems(mMatchReligion, checkedItem,
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	// changeMatchReligion(whichButton);
	// }
	// })
	// .setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
	// .create();
	//
	// return religion;
	// }

	// protected Dialog getMatchRelationDialog() {
	// CustomDialog.Builder builder = new CustomDialog.Builder(this,
	// new AfterYouDialogImpl.AfterYouBuilderImpl(this));
	//
	// int checkedItem = PreferenceEngine.getInstance(ctx)
	// .getMatchRelationInt();
	// Dialog relation = builder
	// .setTitle(this.getString(R.string.relation_label))
	// .setSingleChoiceItems(mMatchRelation, checkedItem,
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	// changeMatchRelation(whichButton);
	// }
	// })
	// .setNegativeButton(this.getString(R.string.IDS_CANCEL), null)
	// .create();
	//
	// return relation;
	// }
}