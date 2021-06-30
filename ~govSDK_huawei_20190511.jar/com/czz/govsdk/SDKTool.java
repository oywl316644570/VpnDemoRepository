// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SDKTool.java

package com.czz.govsdk;


public class SDKTool
{

	public static final String GOV_STATE = "gov_state";
	public static final String GOV_STATE_OK = "1";
	public static final String GOV_STATE_ERROR = "2";
	public static final String GOV_STATE_LOGOUT_OK = "3";
	public static final String GOV_STATE_IS_DISABLE = "4";
	public static final String EULA_STATE_AGREE = "5";
	public static final String EULA_STATE_DISAGREE = "6";
	public static final String GOV_STATE_ID = "gov_state_id";
	public static final String GOV_USER_NAME = "gov_username";
	public static final String GOV_PASSWORD = "gov_password";
	public static final String ERROR_INFO = "error_info";
	public static final String GOV_UPDATE_PASSWORD_FLAG = "update_password_flag";
	public static final String EULA_STATE_FLAG = "eula_state_flag";
	public static final String GOV_ACTION_LOGIN = "com.czz.broadcast.LOGIN";
	public static final String GOV_ACTION_UPDATE_PASSWORD = "com.czz.broadcast.UPDATE_PASSWORD";
	public static final String GOV_ACTION_EULA = "com.czz.broadcast.EULA";
	public static final String GOV_ACTION_LOGIN_OUT = "com.czz.broadcast.LOGIN_OUT";
	public static final String GOV_ACTION_GOVSTATE = "com.czz.broadcast.GOVSTATE";
	public static final String GOV_SDK_USERNAME = "gov_sdk_username";
	public static final String GOV_SDK_PASSWORD = "gov_sdk_password";
	public static final int GOV_API_LOGIN = 1;
	public static final int GOV_API_LOGIN_OUT = 2;
	public static final int GOV_API_UPDATEPASSWORD = 3;
	public static final int GOV_API_EULA = 4;
	public static final String GOV_API_TYPE = "api_type";

	public SDKTool()
	{
	}
}
