package com.beacon.afterui.views;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.custom.view.wheel.OnWheelChangedListener;
import com.beacon.afterui.custom.view.wheel.OnWheelClickedListener;
import com.beacon.afterui.custom.view.wheel.WheelView;
import com.beacon.afterui.custom.view.wheel.adapter.ArrayWheelAdapter;
import com.beacon.afterui.custom.view.wheel.adapter.NumericWheelAdapter;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.views.ProfileSettingsActivity.ProfileInfo;

public class ProfileSettingsHelper {

	// For date.

	private final static String months[] = new String[] { "January",
			"February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };

	private static Calendar sCalendar;

	private static WheelView mSingleWheel;
	private static int flag;

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

	public static void initForHeight(final View view, final TextView text,
			final String[] footItem, final String[] inchesItem, final int flag) {
		if (view == null) {
			return;
		}
		ProfileSettingsHelper.flag = flag;
		final WheelView foot = (WheelView) view.findViewById(R.id.foot_wheel);
		final WheelView inches = (WheelView) view
				.findViewById(R.id.inches_wheel);
		foot.setViewAdapter(new DateArrayAdapter(view.getContext(), footItem));
		inches.setViewAdapter(new DateArrayAdapter(view.getContext(),
				inchesItem));

		OnWheelChangedListener footListner = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {

				if (ProfileSettingsHelper.flag == 1) {
					PreferenceEngine.getInstance(view.getContext())
							.setSelfHeightFoot(footItem[newValue]);
					PreferenceEngine.getInstance(view.getContext())
							.setSelfHeightFootInt(newValue);
				} else {
					PreferenceEngine.getInstance(view.getContext())
							.setMatchHeightFoot(footItem[newValue]);
					PreferenceEngine.getInstance(view.getContext())
							.setMatchHeightFootInt(newValue);
					ProfileSettingsHelper.flag = 0;
				}

			}
		};

		OnWheelChangedListener inchesListner = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {

				if (ProfileSettingsHelper.flag == 1) {
					PreferenceEngine.getInstance(view.getContext())
							.setSelfHeightInches(inchesItem[newValue]);
					PreferenceEngine.getInstance(view.getContext())
							.setSelfHeightInchesInt(newValue);
				} else {
					PreferenceEngine.getInstance(view.getContext())
							.setMatchHeightInches(inchesItem[newValue]);
					PreferenceEngine.getInstance(view.getContext())
							.setMatchHeightInchesInt(newValue);
					ProfileSettingsHelper.flag = 0;
				}

			}
		};

		foot.addChangingListener(footListner);
		inches.addChangingListener(inchesListner);

	}

	public static void initForView(final View view, final TextView text,
			final String[] item, final int change, final int flag) {
		if (view == null) {
			return;
		}
		ProfileSettingsHelper.flag = flag;

		mSingleWheel = (WheelView) view.findViewById(R.id.single_wheel);
		// final String[] relation = view.getResources().getStringArray(
		// R.array.relation_choices);
		mSingleWheel.setViewAdapter(new DateArrayAdapter(view.getContext(),
				item));

		OnWheelChangedListener viewListener = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				switch (change) {
				case 1:
					if (ProfileSettingsHelper.flag == 1) {
						PreferenceEngine.getInstance(view.getContext())
								.setSelfReligion(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setSelfReligionInt(newValue);

					} else {
						PreferenceEngine.getInstance(view.getContext())
								.setMatchReligion(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setMatchReligionInt(newValue);
					}

					break;
				case 2:
					if (ProfileSettingsHelper.flag == 1) {
						PreferenceEngine.getInstance(view.getContext())
								.setSelfRelation(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setSelfRelationInt(newValue);
					} else {
						PreferenceEngine.getInstance(view.getContext())
								.setMatchRelation(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setMatchRelationInt(newValue);
					}

					break;
				case 3:
					if (ProfileSettingsHelper.flag == 1) {
						PreferenceEngine.getInstance(view.getContext())
								.saveHaveChildren(newValue == 0);
					} else {
						PreferenceEngine.getInstance(view.getContext())
								.saveMatchHaveChildren(newValue == 0);
					}

					break;
				case 4:
					if (ProfileSettingsHelper.flag == 1) {
						PreferenceEngine.getInstance(view.getContext())
								.saveWantChildren(newValue == 0);
					} else {
						PreferenceEngine.getInstance(view.getContext())
								.saveMatchWantChildren(newValue == 0);
					}

					break;
				// case 5:
				// if (ProfileSettingsHelper.flag == 1) {
				// PreferenceEngine.getInstance(view.getContext())
				// .setSelfHeightFoot(item[newValue].toString());
				// PreferenceEngine.getInstance(view.getContext())
				// .setSelfHeightFootInt(newValue);
				// } else {
				// PreferenceEngine.getInstance(view.getContext())
				// .setMatchHeight(item[newValue].toString());
				// PreferenceEngine.getInstance(view.getContext())
				// .setMatchHeightInt(newValue);
				// }

				// break;
				case 6:
					if (ProfileSettingsHelper.flag == 1) {
						PreferenceEngine.getInstance(view.getContext())
								.setSelfBodyType(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setSelfBodyTypeInt(newValue);
					} else {
						PreferenceEngine.getInstance(view.getContext())
								.setMatchBodyType(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setMatchBodyTypeInt(newValue);
					}

					break;
				case 7:
					if (ProfileSettingsHelper.flag == 1) {
						PreferenceEngine.getInstance(view.getContext())
								.setSelfCommunity(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setSelfCommunityInt(newValue);
					} else {
						PreferenceEngine.getInstance(view.getContext())
								.setMatchCommunity(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setSelfCommunityInt(newValue);
					}

					break;
				case 8:
					if (ProfileSettingsHelper.flag == 1) {
						PreferenceEngine.getInstance(view.getContext())
								.setSelfDiet(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setSelfDietInt(newValue);
					} else {
						PreferenceEngine.getInstance(view.getContext())
								.setMatchDiet(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setMatchDietInt(newValue);
					}

					break;
				case 9:
					if (ProfileSettingsHelper.flag == 1) {
						PreferenceEngine.getInstance(view.getContext())
								.setSelfSmoking(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setSelfSmokingInt(newValue);
					} else {
						PreferenceEngine.getInstance(view.getContext())
								.setMatchSmoking(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setMatchSmokingInt(newValue);
					}
					break;
				case 10:
					if (ProfileSettingsHelper.flag == 1) {
						PreferenceEngine.getInstance(view.getContext())
								.setSelfDrinking(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setSelfDrinkingInt(newValue);
					} else {
						PreferenceEngine.getInstance(view.getContext())
								.setMatchDrinking(item[newValue].toString());
						PreferenceEngine.getInstance(view.getContext())
								.setMatchDrinkingInt(newValue);

					}

					break;
				case 11:
					PreferenceEngine.getInstance(view.getContext())
							.setSelfEducation(item[newValue].toString());
					PreferenceEngine.getInstance(view.getContext())
							.setSelfEducationInt(newValue);
					break;
				case 12:
					PreferenceEngine.getInstance(view.getContext())
							.setSelfSalary(item[newValue].toString());
					PreferenceEngine.getInstance(view.getContext())
							.setSelfSalaryInt(newValue);
					break;
				case 13:
					PreferenceEngine.getInstance(view.getContext()).setWantAge(
							item[newValue].toString());
					PreferenceEngine.getInstance(view.getContext())
							.setWantAgeInt(newValue);

					break;
				default:
					ProfileSettingsHelper.flag = 0;

				}

			}
		};

		mSingleWheel.addChangingListener(viewListener);

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

			Log.d("test", " ----> : " + view.getLayoutParams());
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
}
