
package com.beacon.afterui.utils.customviews;


import android.app.AlertDialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.Drawable;

import com.beacon.afterui.utils.customviews.DialogHelper.CustomOnKeyListener;

public interface CustomProgressDialog {

	public static final int STYLE_SPINNER = 0;
	public static final int STYLE_HORIZONTAL = 1;

	public abstract void onStart();

	public abstract void setProgress(int value);

	public abstract void setSecondaryProgress(int secondaryProgress);

	public abstract int getProgress();

	public abstract int getSecondaryProgress();

	public abstract int getMax();

	public abstract void setMax(int max);

	public abstract void incrementProgressBy(int diff);

	public abstract void incrementSecondaryProgressBy(int diff);

	public abstract void setProgressDrawable(Drawable d);

	public abstract void setIndeterminateDrawable(Drawable d);

	public abstract void setIndeterminate(boolean indeterminate);

	public abstract boolean isIndeterminate();

	public abstract void setMessage(CharSequence message);

	public abstract void setProgressStyle(int style);
	
	public abstract AlertDialog getDialog();
	
	public abstract void dismiss();
	
	public abstract boolean isShowing();
	
	public abstract void setCancelable(boolean flag);

	public abstract void setOnCancelListener(OnCancelListener onCancelListener);

	public abstract void setOnDismissListener(
			OnDismissListener onDismissListener);

	public abstract void setOnKeyListener(
			CustomOnKeyListener customOnKeyListener);

	public abstract void setTitle(String string);

	public abstract void show();

	public abstract void setOnKeyListener(OnKeyListener onKeyListener);
	
	public abstract void setCanceledOnTouchOutside(boolean flag);

}