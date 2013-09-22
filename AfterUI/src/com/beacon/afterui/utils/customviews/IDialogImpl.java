package com.beacon.afterui.utils.customviews;


import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public interface IDialogImpl {

	public boolean setTitle(CharSequence title);

	public boolean setCustomTitle(View customTitleView);

	public boolean setMessage(CharSequence message);

	public boolean setView(View view);

	public boolean setView(View view, int viewSpacingLeft,
			int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom);

	public boolean setButton(int whichButton, CharSequence text,
			Message msg);

	public boolean setButton(int whichButton, CharSequence text,
			OnClickListener listener);

	public boolean setIcon(int resId);

	public boolean setIcon(Drawable icon);

	public boolean setInverseBackgroundForced(
			boolean forceInverseBackground);

	public boolean onCreate(CustomDialog dlg, Bundle savedInstanceState);

	public boolean show(CustomDialog dlg);
	
	public void init(CustomDialog dlg);

	public boolean onKeyUp(int keyCode, KeyEvent event);

	public boolean onKeyDown(int keyCode, KeyEvent event);

	public boolean getButton(int whichButton);

	public boolean getListView();

	public boolean getCancelable();
	
	public Object getReturnValue();

	public int getTheme();
	
	public boolean onCreateOptionsMenu(Menu menu);
	
	public boolean onPrepareOptionsMenu(Menu menu);
	
	public boolean onOptionsItemSelected(MenuItem item);
	
	public boolean onMenuItemSelected(int featureId, MenuItem item);

}