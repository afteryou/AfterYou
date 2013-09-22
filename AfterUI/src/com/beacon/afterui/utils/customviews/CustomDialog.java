package com.beacon.afterui.utils.customviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.log.AfterUIlog;
import com.beacon.afterui.utils.Utilities;

public class CustomDialog extends AlertDialog {

	public static final int AUTO_DIMISS_WHEN_BUTTON_CLICK = 0x0;
	public static final int NOT_DIMISS_WHEN_POSITIVE_BUTTON_CLICK = 0x1;
	public static final int NOT_DIMISS_WHEN_NEGATIVE_BUTTON_CLICK = 0x2;
	public static final int NOT_DIMISS_WHEN_NEUTRAL_BUTTON_CLICK = 0x4;
	private IDialogImpl mDlgImlp = null;

	private Boolean mCancelable = true;
	private Context uiContext = null;
	private boolean autoDismissable=false;
	private Runnable autoDismissRunnable;
	private int dismissTimes=1500;
	private int fadeOutTimes=1000;

	protected CustomDialog(IDialogImpl dlg, Context context,
			boolean cancelable, OnCancelListener cancelListener) {
		// super(new ContextThemeWrapper(context, R.style.Dialog), cancelable,
		// cancelListener);
		this(dlg, context);
		this.setCancelable(cancelable);
		this.setOnCancelListener(cancelListener);
		mCancelable = cancelable;
		uiContext = context;
	}
	
	protected CustomDialog(IDialogImpl dlg, Context context,int theme,
			boolean cancelable, OnCancelListener cancelListener) {
		// super(new ContextThemeWrapper(context, R.style.Dialog), cancelable,
		// cancelListener);
		this(dlg, context, theme);
		this.setCancelable(cancelable);
		this.setOnCancelListener(cancelListener);
		mCancelable = cancelable;
	}

	protected CustomDialog(IDialogImpl dlg, Context context) {
		super(context);
		mDlgImlp = dlg;
		mDlgImlp.init(this);
		uiContext = context;
	}

	protected CustomDialog(IDialogImpl dlg, Context context, int theme) {
		super(context, theme);
		mDlgImlp = dlg;
		mDlgImlp.init(this);
		uiContext = context;
	}

	@Override
	public void show() {
		if (mDlgImlp.show(this)) {
			super.show();	
			
			AfterUIlog.d(this, "[CustomDialog][show]dismissable:"+autoDismissable);
			if(autoDismissable){
	    		new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						AfterUIlog.d(this, "[CustomDialog][show]auto dismiss dialog");
						setOnDismissListener(new OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialog) {
								
								if(autoDismissRunnable!=null){
									
									int delay=Utilities.isDisableAnimationSetting(uiContext)?0:fadeOutTimes;
									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											autoDismissRunnable.run();
										}
									}, delay);
								}
								
							}
						});
						
						dismiss();
					}
				}, dismissTimes);
	    	}
			
			autoDismissable=false;
		
			if((uiContext != null) && (uiContext instanceof BaseActivity)){				
				BaseActivity activity = (BaseActivity)uiContext;
				activity.setCurrentShowingDialog(this);
			}
			
		}
	}

	public void configureAutoDismissableDialog(boolean dismissable,Runnable r) {
		AfterUIlog.d(this, "[CustomDialog][configureAutoDismissableDialog]dismissable:"+dismissable);
		this.autoDismissable=dismissable;
		this.autoDismissRunnable=r;
	}

	@Override
	public Button getButton(int whichButton) {
		if (mDlgImlp.getButton(whichButton)) {
			return super.getButton(whichButton);
		} else {
			return (Button) mDlgImlp.getReturnValue();
		}
	}

	@Override
	public ListView getListView() {
		if (mDlgImlp.getListView()) {
			return super.getListView();
		} else {
			return (ListView) mDlgImlp.getReturnValue();
		}
	}

	public boolean getCancelable() {
		if (mDlgImlp.getCancelable()) {
			return mCancelable;
		} else {
			return (Boolean) mDlgImlp.getReturnValue();
		}
	}
	
    @Override
    public void setTitle(CharSequence title) {
        /*if (title != null) {
            String toSave = title.toString();
            super.setTitle(toSave);
            mAlert.setTitle(toSave);
        }*/
    	if (mDlgImlp.setTitle(title)) {
			super.setTitle(title);
		}
    }
    
    @Override
    public void setCustomTitle(View customTitleView) {
    	super.setCustomTitle(customTitleView);
        //mAlert.setCustomTitle(customTitleView);
    	if (mDlgImlp.setCustomTitle(customTitleView)) {
			super.setCustomTitle(customTitleView);
		}
    }
    
    
    @Override
    public void setMessage(CharSequence message) {
    	super.setMessage(message);
        //.setMessage(message);
    	if (mDlgImlp.setMessage(message)) {
			super.setMessage(message);
		}
    }


    @Override
    public void setView(View view) {
    	super.setView(view);
        //mAlert.setView(view);
    	if (mDlgImlp.setView(view)) {
			super.setView(view);
		}
    }
    
    @Override
    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight,
            int viewSpacingBottom) {
        //mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    	if (mDlgImlp.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingRight)) {
			super.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingRight);
		}
    }


    @Override
    public void setButton(int whichButton, CharSequence text, Message msg) {
        //mAlert.setButton(whichButton, text, null, msg);
    	if (mDlgImlp.setButton(whichButton, text, msg)) {
			super.setButton(whichButton, text, msg);
		}
    }
    
    @Override
    public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
        //mAlert.setButton(whichButton, text, listener, null);
    	if (mDlgImlp.setButton(whichButton, text, listener)) {
			super.setButton(whichButton, text, listener);
		}
    }
    
    @Override
    public void setIcon(int resId) {
        //mAlert.setIcon(resId);
    	if (mDlgImlp.setIcon(resId)) {
			super.setIcon(resId);
		}
    }
    
    @Override
    public void setIcon(Drawable icon) {
        //mAlert.setIcon(icon);
    	if (mDlgImlp.setIcon(icon)) {
			super.setIcon(icon);
		}
    }

    @Override
    public void setInverseBackgroundForced(boolean forceInverseBackground) {
        //mAlert.setInverseBackgroundForced(forceInverseBackground);
    	if (mDlgImlp.setInverseBackgroundForced(forceInverseBackground)) {
			super.setInverseBackgroundForced(forceInverseBackground);
		}
    }


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setCanceledOnTouchOutside(false);
		if (mDlgImlp.onCreate(this, savedInstanceState)) {
			super.onCreate(savedInstanceState);
		}
	}
	
    //For fixing bug 129026: Language: Downloading progress disappear then display automatically when press search hard key
    //Default: do nothing. Handle search button in this method if need.
    private boolean isKeyCodeSearchHandled(KeyEvent event){
    	if(event.getKeyCode() == KeyEvent.KEYCODE_SEARCH){
    		AfterUIlog.i(this, "KEYCODE_SEARCH: do nothing.");
			return true;
		}
    	return false;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(isKeyCodeSearchHandled(event)) return true;
		boolean dlhHdl = mDlgImlp.onKeyDown(keyCode, event);
		if (dlhHdl) {
			boolean dlhHdls = super.onKeyDown(keyCode, event);
			return dlhHdls;
		} else {
			boolean dlhHdls = (Boolean)mDlgImlp.getReturnValue();
			return dlhHdls;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(isKeyCodeSearchHandled(event)) return true;
		boolean dlhHdl = mDlgImlp.onKeyUp(keyCode, event);
		if (dlhHdl) {
			boolean dlhHdls = super.onKeyUp(keyCode, event);
			return dlhHdls;
		} else {
			boolean dlhHdls = (Boolean)mDlgImlp.getReturnValue();
			return dlhHdls;
		}
	}

/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(isKeyCodeSearchHandled(event)) return true;
        if (mAlert.onKeyDown(keyCode, event)) return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	if(isKeyCodeSearchHandled(event)) return true;
        if (mAlert.onKeyUp(keyCode, event)) return true;
        return super.onKeyUp(keyCode, event);
    }*/
	
	public void setCancelable(boolean flag) {
		AfterUIlog.e(this, "setCancelable:"+flag);
		super.setCancelable(flag);
		mCancelable = flag;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean dlhHdl = mDlgImlp.onCreateOptionsMenu(menu);
		if (dlhHdl) {
			boolean dlhHdls = super.onCreateOptionsMenu(menu);
			return dlhHdls;
		} else {
			boolean dlhHdls = (Boolean)mDlgImlp.getReturnValue();
			return dlhHdls;
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean dlhHdl = mDlgImlp.onPrepareOptionsMenu(menu);
		if (dlhHdl) {
			boolean dlhHdls = super.onPrepareOptionsMenu(menu);
			return dlhHdls;
		} else {
			boolean dlhHdls = (Boolean)mDlgImlp.getReturnValue();
			return dlhHdls;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean dlhHdl = mDlgImlp.onOptionsItemSelected(item);
		if (dlhHdl) {
			boolean dlhHdls = super.onOptionsItemSelected(item);
			return dlhHdls;
		} else {
			boolean dlhHdls = (Boolean)mDlgImlp.getReturnValue();
			return dlhHdls;
		}
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean dlhHdl = mDlgImlp.onMenuItemSelected(featureId, item);
		if (dlhHdl) {
			boolean dlhHdls = super.onMenuItemSelected(featureId, item);
			return dlhHdls;
		} else {
			boolean dlhHdls = (Boolean)mDlgImlp.getReturnValue();
			return dlhHdls;
		}
	}

	public static class Builder {
		private IDialogBuilderImpl dialogBuilderImpl;
		protected CustomDialog mDialog;

		public Builder(Context context, IDialogBuilderImpl builderImpl) {
			dialogBuilderImpl = builderImpl;
		}

		public Builder setTitle(int titleId) {
			dialogBuilderImpl.setTitle(titleId);
			return this;
		}

		public Builder setTitle(CharSequence title) {
			title = (CharSequence)(title.toString().toUpperCase());
			dialogBuilderImpl.setTitle(title);
			return this;
		}

		public Builder setCustomTitle(View customTitleView) {
			dialogBuilderImpl.setCustomTitle(customTitleView);
			return this;
		}

		public Builder setMessage(int messageId) {
			dialogBuilderImpl.setMessage(messageId);
			return this;
		}

		public Builder setMessage(CharSequence message) {
			dialogBuilderImpl.setMessage(message);
			return this;
		}
		
		public Builder setMessageGravity(int gravity) {
			dialogBuilderImpl.setMessageGravity(gravity);
			return this;
		}

		public Builder setIcon(int iconId) {
			dialogBuilderImpl.setIcon(iconId);
			return this;
		}

		public Builder setIcon(Drawable icon) {
			dialogBuilderImpl.setIcon(icon);
			return this;
		}

		public Builder setPositiveButton(int textId, OnClickListener listener) {
			dialogBuilderImpl.setPositiveButton(textId, listener);
			return this;
		}

		public Builder setPositiveButton(CharSequence text,
				OnClickListener listener) {
			dialogBuilderImpl.setPositiveButton(text, listener);
			return this;
		}

		public Builder setNegativeButton(int textId, OnClickListener listener) {
			dialogBuilderImpl.setNegativeButton(textId, listener);
			return this;
		}

		public Builder setNegativeButton(CharSequence text,
				OnClickListener listener) {
			dialogBuilderImpl.setNegativeButton(text, listener);
			return this;
		}

		public Builder setNeutralButton(int textId, OnClickListener listener) {
			dialogBuilderImpl.setNeutralButton(textId, listener);
			return this;
		}

		public Builder setNeutralButton(CharSequence text,
				OnClickListener listener) {
			dialogBuilderImpl.setNeutralButton(text, listener);
			return this;
		}

		public Builder setAutoDismissable(boolean dismissable) {
			dialogBuilderImpl.setAutoDismissable(dismissable);
			return this;
		}
		
		public Builder setAutoDismissRunnable(Runnable r){
			dialogBuilderImpl.setAutoDismissRunnable(r);
			return this;
		}

		public Builder setCancelable(boolean cancelable) {
			dialogBuilderImpl.setCancelable(cancelable);
			return this;
		}

		public Builder setOnCancelListener(OnCancelListener onCancelListener) {
			dialogBuilderImpl.setOnCancelListener(onCancelListener);
			return this;
		}

		public Builder setOnKeyListener(OnKeyListener onKeyListener) {
			dialogBuilderImpl.setOnKeyListener(onKeyListener);
			return this;
		}

		public Builder setItems(int itemsId, OnClickListener listener) {
			dialogBuilderImpl.setItems(itemsId, listener);
			return this;
		}

		public Builder setItems(CharSequence[] items, OnClickListener listener) {
			dialogBuilderImpl.setItems(items, listener);
			return this;
		}

		public Builder setAdapter(ListAdapter adapter, OnClickListener listener) {
			dialogBuilderImpl.setAdapter(adapter, listener);
			return this;
		}

		public Builder setAdapter(ListAdapter adapter, int ckeckedItem,
				OnClickListener listener) {
			dialogBuilderImpl.setAdapter(adapter, listener);
			return this;
		}

		public Builder setCursor(Cursor cursor, OnClickListener listener,
				String labelColumn) {
			dialogBuilderImpl.setCursor(cursor, listener, labelColumn);
			return this;
		}

		public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems,
				OnMultiChoiceClickListener listener) {
			dialogBuilderImpl.setMultiChoiceItems(itemsId, checkedItems,
					listener);
			return this;
		}

		public Builder setMultiChoiceItems(CharSequence[] items,
				boolean[] checkedItems, OnMultiChoiceClickListener listener) {
			dialogBuilderImpl
					.setMultiChoiceItems(items, checkedItems, listener);
			return this;
		}

		public Builder setMultiChoiceItems(Cursor cursor,
				String isCheckedColumn, String labelColumn,
				OnMultiChoiceClickListener listener) {
			dialogBuilderImpl.setMultiChoiceItems(cursor, isCheckedColumn,
					labelColumn, listener);
			return this;
		}

		public Builder setSingleChoiceItems(int itemsId, int checkedItem,
				OnClickListener listener) {
			dialogBuilderImpl.setSingleChoiceItems(itemsId, checkedItem,
					listener);
			return this;
		}

		public Builder setSingleChoiceItems(Cursor cursor, int checkedItem,
				String labelColumn, OnClickListener listener) {
			dialogBuilderImpl.setSingleChoiceItems(cursor, checkedItem,
					labelColumn, listener);
			return this;
		}

		public Builder setSingleChoiceItems(CharSequence[] items,
				int checkedItem, OnClickListener listener) {
			dialogBuilderImpl
					.setSingleChoiceItems(items, checkedItem, listener);
			return this;
		}

		public Builder setSingleChoiceItems(ListAdapter adapter,
				int checkedItem, OnClickListener listener) {
			dialogBuilderImpl.setSingleChoiceItems(adapter, checkedItem,
					listener);
			return this;
		}

		public Builder setOnItemSelectedListener(OnItemSelectedListener listener) {
			dialogBuilderImpl.setOnItemSelectedListener(listener);
			return this;
		}

		public Builder setView(View view) {
			dialogBuilderImpl.setView(view);
			return this;
		}

		public Builder setView(View view, int viewSpacingLeft,
				int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
			dialogBuilderImpl.setView(view);// TODO: igarin
			return this;
		}

		public Builder setInverseBackgroundForced(boolean useInverseBackground) {
			dialogBuilderImpl.setInverseBackgroundForced(useInverseBackground);
			return this;
		}

		public Builder setIsLowerCaseTitle(boolean isUCT) {
			// TODO: igarin
			return this;
		}

		public Builder setCustomPaddingDialog(boolean isCustomPadding) {
			dialogBuilderImpl.setCustomPaddingDialog(isCustomPadding);
			return this;
		}

		public Builder setAutoDismissStrategy(int autoDismissStrategy) {
			// TODO Auto-generated method stub
			return this;
		}

		public void newDialog() {
			// TODO Auto-generated method stub

		}

		public CustomDialog create() {
			return dialogBuilderImpl.create();
		}

		public CustomDialog show() {
			CustomDialog dialog = dialogBuilderImpl.create();
			dialog.show();
			return dialog;
		}
		
		public Builder setMenuEnabled(Activity context) {
			dialogBuilderImpl.setMenuEnabled(context);
			return this;
		}
	}

}
