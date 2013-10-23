package com.beacon.afterui.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Declares the metadata needed for the functioning of back-end .
 * 
 * @author sushil
 * 
 */
public class AfterYouMetadata {

    public static final String AUTHORITY = "com.beacon.afterui";

    public static final class RosterTable implements BaseColumns {

        private RosterTable() {

        }

        public static final String TABLE_NAME = "roster_table";

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.afteryou.roster";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.dir/vnd.afteryou.roster.item";

        public static final String NAME = "name";

        public static final String USER_NAME = "user_name";

        public static final String SUBSCRIPTION_TYPE = "sub_type";

        public static final String STATUS = "status";

        public static final String STATUS_TEXT = "status_text";

        public static final String AVATAR = "avatar";

    }

    public static final class MessageTable implements BaseColumns {

        private MessageTable() {

        }

        public static final String TABLE_NAME = "message_table";

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + TABLE_NAME);

        public static final String CONTENT_TYPE         = "vnd.android.cursor.dir/vnd.afteryou.messages";

        public static final String CONTENT_ITEM_TYPE    = "vnd.android.cursor.dir/vnd.afteryou.message.item";

        public static final String MESSAGE              = "message";

        public static final String SENDER               = "sender";
        
        public static final String RECEIVER             = "receiver";

        /** Should be an integer, 1 is un-read, 0 is read. */
        public static final String READ_STATUS          = "read";
        
        public static final String TIME                 = "time";
        
        public static final int MESSAGE_READ            = 1;
        public static final int MESSAGE_UNREAD          = 0;

        /**
         * sending=0, sent=1, failed=2.
         * */
        public static final String STATUS           = "status";
        
        public static final int MESSAGE_SENDING     = 0;
        public static final int MESSAGE_SUCCESS     = 1;
        public static final int MESSAGE_FAILED      = 2;
    }

    /** Auth queries can be directed to this table. */
    public static final class AuthTable implements BaseColumns {

        private AuthTable() {

        }

        public static final String TABLE_NAME = "auth_table";

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.afteryou.auth";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.dir/vnd.afteryou.auth.source";

        /** Source could be app login(after you), FB, open fire chat server. */
        public static final String AUTH_SOURCE = "auth_source";

        /**
         * A unique ID associated with each source. Not sure, if we need this.
         * But it will be needed if we allow multiple user login without
         * removing the old data. This is for future.
         */
        public static final String AUTH_ID = "auth_id";

        /**
         * If user aborted the login request. This will help us not report back
         * to UI.
         */
        public static final String ABORT = "abort";

        /**
         * Authentication status. UI can make use of this column to know about
         * what's happening in the background. If status is failure then more
         * details could be retrieved from status message.
         */
        public static final String STATUS = "status";

        /**
         * This field will keep the messages in the context of status. If 200 is
         * received from server then success, if failure occurs then what made
         * it fail.
         */
        public static final String STATUS_MESSAGE = "status_message";
    }

    /**
     * The aim was to define it open ended so that different auth type
     * information can be kept in the same table. For e.g. After authentication
     * of FB, we get AUTH_TOKEN for FB. So for FB, AUTH_TOKEN will be a
     * mime_type(key) and it's value will be stored in DATA column. Similarly,
     * if we have to keep other details for openfire server or our app auth, we
     * can use the same table. We can add as much information as needed using
     * this open ended system.
     */
    public static final class AuthDetails implements BaseColumns {

        private AuthDetails() {

        }

        public static final String TABLE_NAME = "auth_details";

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.afteryou.users";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.dir/vnd.afteryou.user.details";

        /** Foreign key from auth table(_id). */
        public static final String AUTH_ID = "auth_id";

        /** key. */
        public static final String MIME_TYPE = "mime_type";

        /** data. */
        public static final String DATA = "data";
    }
}
