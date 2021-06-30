package com.czz.govsdk;

import java.util.regex.Pattern;


public class GlobalConfig {


    public static String VPN_SERVER_IP = "203.86.53.210";
    public static String SERVER_IP = "http://10.180.136.1:9090/apmp_cy";
    public static String GET_USER_LOGIN_RECORD = SERVER_IP
            + "/app/mobileUserLoginRecord";
    public static String REGISTER_APN_USER = SERVER_IP + "/app/toRegisterVpn";
    public static String CHECK_APN_PIN_CODE = SERVER_IP + "/app/checkPinCode";
    public static String CHANGE_PIN_CODE = SERVER_IP + "/app/setNewPinCode";
    public static String CHECK_MOBILE = SERVER_IP + "/app/checkMobile";

    public static final String SHARED_PREFERENCES_NAME = "apnGovSDK";
    ;
    // 上次登录时间
    public static final String SHARED_PREFERENCES_KEY_LAST_LOGIN_TIME = "last_login_time";

    // 是否记录状态
    public static final String SHARED_PREFERENCES_KEY_IS_RECORD_STATE = "is_record_state";
    // 移动热点状态
    public static final String SHARED_PREFERENCES_KEY_WIFI_AP_STATE = "wifi_ap_state";
    // 蓝牙
    public static final String SHARED_PREFERENCES_KEY_BLUETOOTH_STATE = "bluetooth_state";
    // 蓝牙共享
    public static final String SHARED_PREFERENCES_KEY_BLUETOOTH_SHARE_STATE = "bluetooth_share_state";

    // 手机号正则表达式 验证规则：第一位：1，第二位：3、4、5、6、7、8、9，第三位-第十一位：数字
    private static final String MOBILE_NUMBER_REGULAR_EXPRESSION = "^1(3|4|5|6|7|8|9)\\d{9}$";
    private static final String PASSWORD_REGULAR_EXPRESSION = "^([0-9]){6}$";
    private static final String IP_REGULAR_EXPRESSION = "^(10\\.12[4-7]\\.([0-9]|[1-9][0-9]|[1-2][0-9][0-9])\\.([1-9]{1}|[1-9][0-9]|[1-2][0-9][0-9]))$";
    public static final Pattern USERNAME_PATTERN = Pattern
            .compile(MOBILE_NUMBER_REGULAR_EXPRESSION);
    public static final Pattern PASSWORD_PATTERN = Pattern
            .compile(PASSWORD_REGULAR_EXPRESSION);


}
