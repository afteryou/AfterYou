package com.beacon.afterui.utils.customviews;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.beacon.afterui.R;
import com.beacon.afterui.log.AfterUIlog;


public class AfterYouDialogImpl implements IDialogImpl {
	
	public Object retValue = null;
	private Activity menuEnabledContext = null;
    
    public static final int AUTO_DIMISS_WHEN_BUTTON_CLICK = 0x0;
    
    public static final int NOT_DIMISS_WHEN_POSITIVE_BUTTON_CLICK = 0x1;
    public static final int NOT_DIMISS_WHEN_NEGATIVE_BUTTON_CLICK = 0x2;
    public static final int NOT_DIMISS_WHEN_NEUTRAL_BUTTON_CLICK = 0x4;
    
    private CustomDialogMgr mAlert;
    private Context mContext;
    private boolean mDismissable;
    private DialogInterface.OnClickListener mOnButtonClickListener;

    public AfterYouDialogImpl(Context context) {
        mContext = context;
        mAlert = new CustomDialogMgr(context);
    }
    
    @Override
    public boolean show(final CustomDialog dlg) {
//        if (mDismissable == true && (mAlert.getButtonNumber() == CustomDialogMgr.BIT_BUTTON_NEGATIVE ||
//                mAlert.getButtonNumber() == CustomDialogMgr.BIT_BUTTON_POSITIVE ||
//                mAlert.getButtonNumber() == CustomDialogMgr.BIT_BUTTON_NEUTRAL)) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (mOnButtonClickListener != null) {
//                        mOnButtonClickListener.onClick(dlg, mAlert.getButtonNumber());
//                    }
//                    mAlert.getButtonHandler().onClick(dlg.getCurrentFocus());
//                }
//            }, 2000);
//        }
        return true;
    }

    private void configureAutoDismissableDialog(boolean dismissable, DialogInterface.OnClickListener listener) {
        mDismissable = dismissable;
        mOnButtonClickListener = listener;
    }

    public boolean getButton(int whichButton) {
        this.retValue = mAlert.getButton(whichButton);
        return false;
    }
    
    public boolean getListView() {
    	this.retValue = mAlert.getListView();
        return false;
    }
    
    public boolean getCancelable() {
    	this.retValue = mAlert.getCancelable();
        return false;
    }
    
    @Override
    public boolean setTitle(CharSequence title) {
        if (title != null) {
            String toSave = title.toString();
            mAlert.setTitle(toSave);
            this.retValue = toSave;
        }
        return false;
    }

    public boolean setCustomTitle(View customTitleView) {
        mAlert.setCustomTitle(customTitleView);
        return false;
    }
    
    public boolean setMessage(CharSequence message) {
        mAlert.setMessage(message);
        return false;
    }


    public boolean setView(View view) {
        mAlert.setView(view);
        return false;
    }
    

    public boolean setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight,
            int viewSpacingBottom) {
        mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
        return false;
    }


    public boolean setButton(int whichButton, CharSequence text, Message msg) {
        mAlert.setButton(whichButton, text, null, msg);
        return false;
    }
    
    public boolean setButton(int whichButton, CharSequence text, OnClickListener listener) {
        mAlert.setButton(whichButton, text, listener, null);
        return false;
    }

    public boolean setIcon(int resId) {
        mAlert.setIcon(resId);
        return false;
    }
    
    public boolean setIcon(Drawable icon) {
        mAlert.setIcon(icon);
        return false;
    }

    public boolean setInverseBackgroundForced(boolean forceInverseBackground) {
        mAlert.setInverseBackgroundForced(forceInverseBackground);
        return false;
    }
    
    @Override
    public boolean onCreate(CustomDialog dlg, Bundle savedInstanceState) {
        mAlert.setWindow(dlg.getWindow());
        mAlert.installContent();
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	boolean alh = mAlert.onKeyDown(keyCode, event);
        if (alh) {
        	this.retValue = new Boolean(true);
        	return false;
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	boolean alh = mAlert.onKeyUp(keyCode, event);
        if (alh) {
        	this.retValue = new Boolean(true);
        	return false;
        }
        return true;
    }
    
	@Override
	public Object getReturnValue() {
		Object tmp = retValue;
		retValue = null;
		return tmp;
	}
    
    public static class AfterYouBuilderImpl implements IDialogBuilderImpl {
        private final CustomDialogMgr.AlertParams P;
        protected CustomDialog mDialog;
        protected AfterYouDialogImpl mDialogImpl;
        private Context mContext;
        private Activity menuEnabledContext = null;
        

        public AfterYouBuilderImpl(Context context) {
            P = new CustomDialogMgr.AlertParams(context);
            mContext = context;
            
        }
        
        protected void newDialog() {
            mDialogImpl = new AfterYouDialogImpl(mContext);
            this.mDialog = new CustomDialog(mDialogImpl, P.mContext, mDialogImpl.getTheme());
            mDialogImpl.mAlert.setDialogInterface(mDialog);
            mDialogImpl.menuEnabledContext = this.menuEnabledContext;
        }

        public CustomDialog create() {
            newDialog();
            P.apply(this.mDialogImpl.mAlert);
            this.mDialog.setCancelable(P.mCancelable);
            AfterUIlog.d(this, "[AfterYouBuilderImpl][create]mDismissable:"+P.mAutoDismissable);
            if(P.mAutoDismissable){
                mDialog.configureAutoDismissableDialog(P.mAutoDismissable,P.autoDismissRunnable);
            	mDialog.getWindow().getAttributes().windowAnimations=R.style.autoDismissFadeOut;
            }
            this.mDialog.setOnCancelListener(P.mOnCancelListener);
            if (P.mOnKeyListener != null) {
                this.mDialog.setOnKeyListener(P.mOnKeyListener);
            }
            return this.mDialog;
        }

        public CustomDialog show() {
            CustomDialog dialog = create();
            dialog.show();
            return dialog;
        }
        
        public void setTitle(int titleId) {
            P.mTitle = P.mContext.getText(titleId).toString();
        }

        public void setTitle(CharSequence title) {
            P.mTitle = title.toString();
        }
        

        public void setCustomTitle(View customTitleView) {
            P.mCustomTitleView = customTitleView;
        }

        public void setMessage(int messageId) {
            P.mMessage = P.mContext.getText(messageId);
        }

        public void setMessage(CharSequence message) {
            P.mMessage = message;
        }

        public void setMessageGravity(int gravity) {
        	P.mMessageGravity = gravity;
        }
        
        public void setIcon(int iconId) {
            P.mIconId = iconId;
        }

        public void setIcon(Drawable icon) {
            P.mIcon = icon;
        }

        public void setPositiveButton(int textId, final OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId);
            P.mPositiveButtonListener = listener;
        }

        public void setPositiveButton(CharSequence text, final OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonListener = listener;
        }

        public void setNegativeButton(int textId, final OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId);
            P.mNegativeButtonListener = listener;
        }

        public void setNegativeButton(CharSequence text, final OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
        }

        public void setNeutralButton(int textId, final OnClickListener listener) {
            P.mNeutralButtonText = P.mContext.getText(textId);
            P.mNeutralButtonListener = listener;
        }

        public void setNeutralButton(CharSequence text, final OnClickListener listener) {
            P.mNeutralButtonText = text;
            P.mNeutralButtonListener = listener;
        }

        public void setAutoDismissable(boolean dismissable) {
            P.mAutoDismissable = dismissable;
        }
        
		public void setAutoDismissRunnable(Runnable r) {
			P.autoDismissRunnable=r;
		}

		public void setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
        }
        
        public void setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
        }

        public void setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
        }

        public void setItems(int itemsId, final OnClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnClickListener = listener;
        }

        public void setItems(CharSequence[] items, final OnClickListener listener) {
            P.mItems = items;
            P.mOnClickListener = listener;
        }

        public void setAdapter(final ListAdapter adapter, final OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
        }
        
        public void setAdapter(final ListAdapter adapter, int ckeckedItem, final OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
            P.mCheckedItem = ckeckedItem;
        }

        public void setCursor(final Cursor cursor, final OnClickListener listener,
                String labelColumn) {
            P.mCursor = cursor;
            P.mLabelColumn = labelColumn;
            P.mOnClickListener = listener;
        }

        public void setMultiChoiceItems(int itemsId, boolean[] checkedItems, 
                final OnMultiChoiceClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnCheckboxClickListener = listener;
            P.mCheckedItems = checkedItems;
            P.mIsMultiChoice = true;
        }

        public void setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, 
                final OnMultiChoiceClickListener listener) {
            P.mItems = items;
            P.mOnCheckboxClickListener = listener;
            P.mCheckedItems = checkedItems;
            P.mIsMultiChoice = true;
        }

        public void setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, 
                final OnMultiChoiceClickListener listener) {
            P.mCursor = cursor;
            P.mOnCheckboxClickListener = listener;
            P.mIsCheckedColumn = isCheckedColumn;
            P.mLabelColumn = labelColumn;
            P.mIsMultiChoice = true;
        }

        public void setSingleChoiceItems(int itemsId, int checkedItem, 
                final OnClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
        }

        public void setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, 
                final OnClickListener listener) {
            P.mCursor = cursor;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mLabelColumn = labelColumn;
            P.mIsSingleChoice = true;
        }

        public void setSingleChoiceItems(CharSequence[] items, int checkedItem, final OnClickListener listener) {
            P.mItems = items;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
        } 

        public void setSingleChoiceItems(ListAdapter adapter, int checkedItem, final OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
        }

        public void setOnItemSelectedListener(final AdapterView.OnItemSelectedListener listener) {
            P.mOnItemSelectedListener = listener;
        }

        public void setView(View view) {
            P.mView = view;
            P.mViewSpacingSpecified = false;
            if (view instanceof ListView) {
            	P.mIsLowerCaseTitle = true;
            }
        }

        public void setView(View view, int viewSpacingLeft, int viewSpacingTop,
                int viewSpacingRight, int viewSpacingBottom) {
            P.mView = view;
            P.mViewSpacingSpecified = true;
            P.mViewSpacingLeft = viewSpacingLeft;
            P.mViewSpacingTop = viewSpacingTop;
            P.mViewSpacingRight = viewSpacingRight;
            P.mViewSpacingBottom = viewSpacingBottom;
        }

        public void setInverseBackgroundForced(boolean useInverseBackground) {
            P.mForceInverseBackground = useInverseBackground;
        }
        
        public void setIsLowerCaseTitle(boolean isUCT) {
            P.mIsLowerCaseTitle=isUCT;
        }
        public void setCustomPaddingDialog(boolean isCustomPadding){
            P.mIsCustomPadding=isCustomPadding;
        }
        
        public void setAutoDismissStrategy(int autoDismissStrategy){
            P.autoDismissStrategy = autoDismissStrategy;
        }

		@Override
		public void setMenuEnabled(Activity context) {
			this.menuEnabledContext = context;
		}
    }

	@Override
	public int getTheme() {
		return R.style.Theme_CustomDialog;
	}

	@Override
	public void init(CustomDialog dlg) {
		this.mAlert.setDialogInterface(dlg);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(menuEnabledContext != null) {
			retValue = new Boolean(menuEnabledContext.onCreateOptionsMenu(menu));
			return false;
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(menuEnabledContext != null) {
			retValue = new Boolean(menuEnabledContext.onPrepareOptionsMenu(menu));
			return false;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(menuEnabledContext != null) {
			retValue = new Boolean(menuEnabledContext.onOptionsItemSelected(item));
			return false;
		}
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(menuEnabledContext != null) {
			retValue = new Boolean(menuEnabledContext.onMenuItemSelected(featureId, item));
			return false;
		}
		return true;
	}
   
} 