package com.navbuilder.app.hurricane.qalog;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

import com.nbi.common.NBIException;
import com.nbi.common.NBIRequest;

public class LBSQALogUploadListener {
    
    private boolean            mCanceled       = false;
    ProgressDialog             mProgressDialog = null;
    protected Context          context         = null;
    private String             mLoading        = "Loading...";
    
    private OnClickListener mOnClickListenner = null;
    
    public LBSQALogUploadListener(Context context) {
    	this.context = context;
    }
    
    public void onLogComplete(String logID, int size) {
    	if (context!=null)
    	{
    		Builder dlg = new Builder(context);
    		dlg.setPositiveButton(android.R.string.ok, mOnClickListenner);
    		dlg.setTitle("QALog upload completed");
    		dlg.setMessage("QA Log ID: " + logID);
    		dlg.show();
    	}
    }
    
    // Dismisses progress dialog on request cancel event
    public void onRequestCancelled(NBIRequest request) {
    	mCanceled = true;
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    
    // On request error event makes error message and sends it via handler
    // object.
    public void onRequestError(NBIException exception, NBIRequest request) {
        if (!mCanceled) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
            Builder dlg = new Builder(context);
            dlg.setTitle("Error");
            dlg.setMessage(exception.toString());
            dlg.show();
        }
    }
    
    // Tracks request progress event with progress bar UI widget
    public void onRequestProgress(int percentage, NBIRequest request) {
        if (mProgressDialog != null) {
            mProgressDialog.setProgress(percentage);
        }
    }
    
    /*
     * On request start initializes progress bar widget, sets loading
     * message and provides request cancel listener.
     */
    public void onRequestStart(NBIRequest request) {
        if (context != null) {
            mProgressDialog = new ProgressDialog(context);
        }
        
        if (mProgressDialog != null) {
            // mProgressDialog = ProgressDialog.show(mContext, "",
            // "Loading...", true, true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMessage(mLoading);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
			mProgressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					LBSQALoggerManager.cancel();	
				}
			});
        }
    }
    
    // On request time out dismisses progress bar and sends timeout error
    // message.
    public void onRequestTimeOut(NBIRequest request) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    
    // On request complete dismisses progress dialog
    public void onRequestComplete(NBIRequest request) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    
    // Check whether request is canceled
    protected boolean isCanceled() {
        return mCanceled;
    }
    
    public void setCompleteDialogButtonListenner(OnClickListener listenner) {
    	this.mOnClickListenner = listenner;
    }
    
}
