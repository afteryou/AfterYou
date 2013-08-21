package com.navbuilder.app.hurricane.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;

public class WindowUtils {

	public static DisplayMetrics getDisplayMetrics(Context ctx) {
		return ctx.getResources().getDisplayMetrics();
	}

	public static int getStatusBarHeight(Activity activity) {
		Rect rectgle = getWindowVisibleDisplayFrame(activity);
		return rectgle.top;
	}

	public static Rect getRootViewVisibleDisplayFrame(Activity activity) {
		Rect rect = getWindowVisibleDisplayFrame(activity);
		rect.top = rect.top + getActionBarHeight(activity);
		return rect;
	}

	public static Rect getWindowVisibleDisplayFrame(Activity activity) {
		Rect rectgle = new Rect();
		Window window = activity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		return rectgle;
	}

	public static int getScreenWidth(Context ctx) {
		return getDisplayMetrics(ctx).widthPixels;
	}
	
	public static int getScreenHeight(Context ctx) {
		return getDisplayMetrics(ctx).heightPixels;
	}

	public static int getActionBarHeight(Activity activity) {
		try {
			ActionBar actionBar = activity.getActionBar();
			return actionBar.getHeight();
		} catch (Exception e) {
			// Ignore
		}
		return 0;
	}
	
	public static boolean isLandscape(Context ctx) {
		return ctx.getResources().getConfiguration().orientation 
			== Configuration.ORIENTATION_LANDSCAPE;
	}

	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	}
}
