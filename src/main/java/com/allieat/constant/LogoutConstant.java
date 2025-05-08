package com.allieat.constant;

public class LogoutConstant {
    public static final String BLACKLIST_PREFIX = "blacklist:";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String TOKEN_EXPIRED = "Token has already expired";
    public static final long TOKEN_MAX_LIFETIME_MILLIS = 30 * 60 * 1000L;  //token有效期限

    private LogoutConstant() {
    }
}
