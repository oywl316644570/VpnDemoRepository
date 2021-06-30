// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GOVAPIMessageManager.java

package com.czz.govsdk;

import android.content.Context;
import android.content.Intent;

public class GOVAPIMessageManager
{

	public static final String GOV_ACTION_LOGIN = "com.czz.broadcast.LOGIN";
	public static final String GOV_ACTION_UPDATE_PASSWORD = "com.czz.broadcast.UPDATE_PASSWORD";
	public static final String GOV_ACTION_RESET_PASSWORD = "com.czz.broadcast.RESET_PASSWORD";
	public static final String GOV_ACTION_LOGIN_OUT = "com.czz.broadcast.LOGIN_OUT";
	public static final String GOV_ACTION_GOVSTATE = "com.czz.broadcast.GOVSTATE";
	public static final String GOV_ACTION_EULA = "com.czz.broadcast.EULA";
	public static final String GOV_STATE = "gov_state";
	public static final String ERROR_INFO = "error_info";
	public static final String GOV_STATE_OK = "1";
	public static final String GOV_STATE_ERROR = "2";
	public static final String GOV_STATE_LOGOUT_OK = "3";
	public static final String GOV_STATE_IS_DISABLE = "4";
	public static final String EULA_STATE_AGREE = "5";
	public static final String EULA_STATE_DISAGREE = "6";
	public static final String GOV_USER_NAME = "gov_username";
	public static final String GOV_PASSWORD = "gov_password";
	public static final String GOV_UPDATE_PASSWORD_FLAG = "update_password_flag";
	public static final String GOV_RESET_PASSWORD_FLAG = "reset_password_flag";
	public static final String GOV_EULA_STATE_FLAG = "eula_state_flag";

	public GOVAPIMessageManager()
	{
	}

	public static String encodeContent(String content)
	{
		long i = Long.parseLong(content);
		i = (i - 256L) * 2L + 1L;
		return (new StringBuilder()).append(i).append("").toString();
	}

	public static void logOutGovInfo(Context context)
	{
		Intent it = new Intent();
		it.setAction("com.czz.broadcast.LOGIN_OUT");
		it.putExtra("gov_state", "3");
		context.sendBroadcast(it);
	}

	public static void resetPasswordInfo(boolean updateFlag, Context context)
	{
		Intent it = new Intent();
		it.setAction("com.czz.broadcast.RESET_PASSWORD");
		it.putExtra("reset_password_flag", updateFlag);
		context.sendBroadcast(it);
	}

	public static void updatePasswordInfo(int updateFlag, Context context)
	{
		Intent it = new Intent();
		it.setAction("com.czz.broadcast.UPDATE_PASSWORD");
		it.putExtra("update_password_flag", updateFlag);
		context.sendBroadcast(it);
	}

	public static void logGovIsDisableInfo(Context context)
	{
		Intent it = new Intent();
		it.setAction("com.czz.broadcast.LOGIN_OUT");
		it.putExtra("gov_state", "4");
		context.sendBroadcast(it);
	}

	public static void eulaInfo(String flag, Context context)
	{
		Intent it = new Intent();
		it.setAction("com.czz.broadcast.EULA");
		it.putExtra("eula_state_flag", flag);
		context.sendBroadcast(it);
	}
}
