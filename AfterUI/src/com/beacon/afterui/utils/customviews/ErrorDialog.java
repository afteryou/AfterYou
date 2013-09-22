package com.beacon.afterui.utils.customviews;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;

import com.beacon.afterui.R;


public class ErrorDialog extends CustomDialog {
	
	
	public ErrorDialog(IDialogImpl dlg, Context context, int theme,
			final OnClickListener clickListener, String msg) {
		super(dlg, context, theme, true, null);
		init(context, clickListener, msg);
	}

	public ErrorDialog(IDialogImpl dlg, Context context,
			final OnClickListener clickListener, String msg) {
		super(dlg, context, true, null);
		init(context, clickListener, msg);

	}
	
	private void init(Context context, final OnClickListener clickListener,
			String msg) {
		this.setIcon(R.drawable.notice);
		this.setMessage(msg);
		this.setButton(DialogInterface.BUTTON_POSITIVE,
				context.getString(R.string.IDS_OK), clickListener);
		this.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (clickListener != null) {
					clickListener.onClick(dialog,
							DialogInterface.BUTTON_POSITIVE);
				} else {
					Button button = getButton(BUTTON_POSITIVE);
					if (button != null) {
						button.performClick();
					}
				}
			}
		});
	}
	
	public ErrorDialog(IDialogImpl dlg, Context context, 
			OnClickListener clickListener , int stringRid) {
		this(dlg, context,clickListener ,context.getString(stringRid));
	}
	
	public ErrorDialog(IDialogImpl dlg, Context context, int theme,
			OnClickListener clickListener , int stringRid) {
		this(dlg, context, theme, clickListener ,context.getString(stringRid));
	}
}
