package com.beacon.afterui.provider;

public class Status {

    public static final class AuthStatus {

        private AuthStatus() {

        }

        public static final int MAKING_CONNECTION               = 0;
        public static final int READING_DATA                    = 1;
        public static final int READING_DATA_COMPLETED          = 2;
        public static final int QUERY_COMPLETED_WITH_SUCCESS    = 3;
        public static final int QUERY_COMPLETED_WITH_FAILURE    = 4;

    }
}
