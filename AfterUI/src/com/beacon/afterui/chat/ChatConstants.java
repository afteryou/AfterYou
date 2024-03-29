package com.beacon.afterui.chat;

public interface ChatConstants {
    
    public static final int LOGIN_SUCCESS           = 100;

    /** XMPP connection is NULL. */
    public static final int XMPP_NULL               = 1001;
    
    /** XMPP is not connected. */
    public static final int XMPP_NOT_CONNECTED      = 1002;
    
    /** username or password is not NULL. */
    public static final int USERNAME_PASSWORD_NULL  = 1003;
    
    public static final int XMPP_CONNECTION_FAILED  = 1004;
    
    public static final int LOGIN_FAILED            = 1005;
    
    public static final String AVAILABLE            = "available";
    
    public static final String AWAY                 = "away";
    
    public static final String EXTENDED_AWAY       = "xa";
    
    public static final String UN_AVAILABLE         = "unavailable";
    
    public static final String CHAT                 = "chat";
    
    public static final String DO_NOT_DISTURB       = "dnd";
    
}
