// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GlobalConfig.java

package com.czz.govsdk;

import java.util.regex.Pattern;

public class GlobalConfig
{

	public static String appType = "2";
	public static String SERVER_IP;
	public static String CHANGE_PIN_CODE;
	public static String CHECK_MOBILE;
	public static Pattern APN_USERNAME_PATTERN = Pattern.compile("^1(3|4|5|6|7|8|9)\\d{9}$");
	public static Pattern APN_PASSWORD_PATTERN = Pattern.compile("^([0-9]){6}$");
	public static String gov_apn = "";
	private static final String MOBILE_NUMBER_REGULAR_EXPRESSION = "^1(3|4|5|6|7|8|9)\\d{9}$";
	private static final String PASSWORD_REGULAR_EXPRESSION = "^([0-9]){6}$";
	private static final String IP_REGULAR_EXPRESSION = "^(10\\.12[4-7]\\.([0-9]|[1-9][0-9]|[1-2][0-9][0-9])\\.([1-9]{1}|[1-9][0-9]|[1-2][0-9][0-9]))$";
	public static final Pattern PASSWORD_PATTERN = Pattern.compile("^([0-9]){6}$");
	public static final String SHARED_PREFERENCES_NAME = "rootApnGovSDK";
	public static final String SHARED_PREFERENCES_KEY_IS_RECORD_STATE = "is_record_state";
	public static final String SHARED_PREFERENCES_KEY_WIFI_STATE = "wifi_state";
	public static final String SHARED_PREFERENCES_KEY_WIFI_AP_STATE = "wifi_ap_state";
	public static final String SHARED_PREFERENCES_KEY_GPRS_STATE = "gprs_state";
	public static final String SHARED_PREFERENCES_KEY_BLUETOOTH_STATE = "bluetooth_state";
	public static final String SHARED_PREFERENCES_KEY_BLUETOOTH_SHARE_STATE = "bluetooth_share_state";
	public static final String SHARED_PREFERENCES_KEY_LAST_LOGIN_TIME = "last_login_time";
	public static final String SHARED_PREFERENCES_KEY_DEFAULT_INTERNET_APN_ID = "default_internet_apn_id";
	public static final String SHARED_PREFERENCES_KEY_EULA_PREFIX = "eula_useraccepted_";
	public static final String GOV_APN_NAME = "ÕþÎñÍø";
	public static final String CHINA_MOBILE_APN = "jxwtest-ddn.bj";
	public static final String CHINA_UNICOM_APN = "bjydzw.ydoa.bjapn";
	public static final String CHINA_TELECOM_APN = "private.vpdn.bj";
	public static final String CHINA_MOBILE_APN_USER_SUFFIX = "@jxwtest";
	public static final String CHINA_UNICOM_APN_USER_SUFFIX = "@bjgov";
	public static final String CHINA_TELECOM_APN_USER_SUFFIX = "@bjsjxw.VPDN.bj";
	public static final String APN_TYPE = "default";
	public static final String APN_AUTHTYPE = "3";

	public GlobalConfig()
	{
	}

	static 
	{
		SERVER_IP = "http://172.28.255.20:8088/apmp_cg";
		CHANGE_PIN_CODE = (new StringBuilder()).append(SERVER_IP).append("/app/setNewPinCode").toString();
		CHECK_MOBILE = (new StringBuilder()).append(SERVER_IP).append("/app/checkMobile").toString();
	}
}
