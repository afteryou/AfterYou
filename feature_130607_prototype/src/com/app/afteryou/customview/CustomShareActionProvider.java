package com.app.afteryou.customview;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.ShareActionProvider;

import com.app.afteryou.R;

public class CustomShareActionProvider extends ShareActionProvider {

	public CustomShareActionProvider(Context context) {
		super(context);
	}

	@Override
	public View onCreateActionView(MenuItem forItem) {

		View view = super.onCreateActionView(forItem);
		try {
			Field imageField = view.getClass().getDeclaredField("mExpandActivityOverflowButtonImage");
			imageField.setAccessible(true);
			ImageView image = (ImageView) imageField.get(view);
			image.setImageResource(R.drawable.ic_share);
			Field dbField = view.getClass().getDeclaredField("mDefaultActivityButton");
			dbField.setAccessible(true);
			final View mDefaultActivityButton = (View) dbField.get(view);
			mDefaultActivityButton.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
				public boolean onPreDraw() {
					mDefaultActivityButton.setVisibility(View.GONE);
					return true;
				}
			});

			Field mActivityChooserContentField = view.getClass().getDeclaredField("mActivityChooserContent");
			mActivityChooserContentField.setAccessible(true);
			final View mActivityChooserContent = (View) mActivityChooserContentField.get(view);
			mActivityChooserContent.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
				public boolean onPreDraw() {
					mActivityChooserContent.setBackgroundDrawable(null);
					return true;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

}
