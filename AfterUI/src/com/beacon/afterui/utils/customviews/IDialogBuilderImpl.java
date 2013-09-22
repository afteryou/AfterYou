package com.beacon.afterui.utils.customviews;


import android.app.Activity;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;


public interface IDialogBuilderImpl {

    public CustomDialog create();
    
	public abstract void setTitle(int titleId);

	public abstract void setTitle(CharSequence title);

	public abstract void setCustomTitle(View customTitleView);

	public abstract void setMessage(int messageId);

	public abstract void setMessage(CharSequence message);
	
	public abstract void setMessageGravity(int gravity);

	public abstract void setIcon(int iconId);

	public abstract void setIcon(Drawable icon);

	public abstract void setPositiveButton(int textId,
			final OnClickListener listener);

	public abstract void setPositiveButton(CharSequence text,
			final OnClickListener listener);

	public abstract void setNegativeButton(int textId,
			final OnClickListener listener);

	public abstract void setNegativeButton(CharSequence text,
			final OnClickListener listener);

	public abstract void setNeutralButton(int textId,
			final OnClickListener listener);

	public abstract void setNeutralButton(CharSequence text,
			final OnClickListener listener);

	public abstract void setAutoDismissable(boolean dismissable);
	
	public abstract void setAutoDismissRunnable(Runnable r);

	public abstract void setCancelable(boolean cancelable);

	public abstract void setOnCancelListener(
			OnCancelListener onCancelListener);

	public abstract void setOnKeyListener(OnKeyListener onKeyListener);

	public abstract void setItems(int itemsId, final OnClickListener listener);

	public abstract void setItems(CharSequence[] items,
			final OnClickListener listener);

	public abstract void setAdapter(final ListAdapter adapter,
			final OnClickListener listener);

	public abstract void setAdapter(final ListAdapter adapter,
			int ckeckedItem, final OnClickListener listener);

	public abstract void setCursor(final Cursor cursor,
			final OnClickListener listener, String labelColumn);

	public abstract void setMultiChoiceItems(int itemsId,
			boolean[] checkedItems, final OnMultiChoiceClickListener listener);

	public abstract void setMultiChoiceItems(CharSequence[] items,
			boolean[] checkedItems, final OnMultiChoiceClickListener listener);

	public abstract void setMultiChoiceItems(Cursor cursor,
			String isCheckedColumn, String labelColumn,
			final OnMultiChoiceClickListener listener);

	public abstract void setSingleChoiceItems(int itemsId, int checkedItem,
			final OnClickListener listener);

	public abstract void setSingleChoiceItems(Cursor cursor,
			int checkedItem, String labelColumn, final OnClickListener listener);

	public abstract void setSingleChoiceItems(CharSequence[] items,
			int checkedItem, final OnClickListener listener);

	public abstract void setSingleChoiceItems(ListAdapter adapter,
			int checkedItem, final OnClickListener listener);

	public abstract void setOnItemSelectedListener(
			final AdapterView.OnItemSelectedListener listener);

	public abstract void setView(View view);

	public abstract void setView(View view, int viewSpacingLeft,
			int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom);

	public abstract void setInverseBackgroundForced(
			boolean useInverseBackground);

	public abstract void setIsLowerCaseTitle(boolean isUCT);

	public abstract void setCustomPaddingDialog(boolean isCustomPadding);

	public abstract void setAutoDismissStrategy(int autoDismissStrategy);
	
	public abstract void setMenuEnabled(Activity context);
}