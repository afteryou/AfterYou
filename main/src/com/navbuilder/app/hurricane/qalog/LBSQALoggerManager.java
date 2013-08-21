/*
 * (C) Copyright 2012 by TeleCommunication Systems, Inc.
 *
 * The information contained herein is confidential, proprietary
 * to TeleCommunication Systems, Inc., and considered a trade secret
 * as defined in section 499C of the penal code of the State of
 * California. Use of this information by anyone other than
 * authorized employees of TeleCommunication Systems is granted only
 * under a written non-disclosure agreement, expressly prescribing
 * the scope and manner of such use.
 *
 */

package com.navbuilder.app.hurricane.qalog;

public class LBSQALoggerManager {
    private static LBSQALogger logger;
    
    private static LBSQALogger getLogger() {
        if (logger == null) {
            logger = new LBSQALogger();
        }
        return logger;
    }
    
    public static void log(String message) {
        getLogger().log(message);
    }
    
    public static boolean isLoggingAvailable() {
        return getLogger().isLoggingAvailable();
    }
    
    public static void persistLogging(LBSQALogUploadListener listener) {
    	getLogger().persistLogging(listener);
    }
    
    public static void cancel() {
    	getLogger().cancel();
    }
    
    public static void clear() {
    	getLogger().clear();
    }
}
