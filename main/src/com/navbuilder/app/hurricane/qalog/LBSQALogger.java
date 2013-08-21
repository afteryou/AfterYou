package com.navbuilder.app.hurricane.qalog;


import com.navbuilder.app.hurricane.common.LBSManager;
import com.nbi.common.NBIException;
import com.nbi.common.NBIRequest;
import com.nbi.common.qa.QALogFeatureHandler;
import com.nbi.common.qa.QALogUploadListener;
import com.nbi.common.qa.QALogUploadRequest;

public class LBSQALogger {
    private QALogUploadRequest mQAlogUploadRequest;
    
    public boolean isLoggingAvailable() {
        return QALogFeatureHandler.isQALogingAvail();
    }
    
    public void persistLogging(final LBSQALogUploadListener listener) {
    	
        mQAlogUploadRequest = new QALogUploadRequest();
        mQAlogUploadRequest.Upload(LBSManager.getNBIContext(), new QALogUploadListener() {
            
            @Override
            public void onRequestCancelled(NBIRequest request) {
            	if(listener != null) {
            		listener.onRequestCancelled(request);
            	}
            }
            
            @Override
            public void onRequestError(NBIException exception, NBIRequest request) {
            	if(listener != null) {
            		listener.onRequestError(exception, request);
            	}
            }
            
            @Override
            public void onRequestProgress(int percentage, NBIRequest request) {
            	if(listener != null) {
            		listener.onRequestProgress(percentage, request);
            	}
            }
            
            @Override
            public void onRequestStart(NBIRequest request) {
            	if(listener != null) {
            		listener.onRequestStart(request);
            	}
            }
            
            @Override
            public void onRequestTimeOut(NBIRequest request) {
            	if(listener != null) {
            		listener.onRequestTimeOut(request);
            	}
                
            }
            
            @Override
            public void onRequestComplete(NBIRequest request) {
            	if(listener != null) {
            		listener.onRequestComplete(request);
            	}
            }
            
            @Override
            public void uploadComplete(String logID, int size) {
            	if(listener != null) {
            		listener.onLogComplete(logID, size);
            	}
            }
            
        });
        mQAlogUploadRequest.startRequest();
    }
    
    public void cancel() {
        if (mQAlogUploadRequest != null) {
            mQAlogUploadRequest.cancelRequest();
            mQAlogUploadRequest = null;
        }
    }
    
    public boolean clear() {
        mQAlogUploadRequest = new QALogUploadRequest();
        return mQAlogUploadRequest.Clear();
    }
    
    public void log(String message) {
        if (message == null) {
            throw new IllegalArgumentException("invalid message");
        }
        QALogUploadRequest.logMessage(message);
    }
}
