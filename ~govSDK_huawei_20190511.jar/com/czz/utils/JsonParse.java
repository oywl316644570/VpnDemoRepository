// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JsonParse.java

package com.czz.utils;

import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParse
{

	public JsonParse()
	{
	}

	public static boolean parseServeSwitch(String result)
	{
		if (TextUtils.isEmpty(result))
			return true;
		int flag = -1;
		try
		{
			JSONObject jsonObj = new JSONObject(result);
			flag = jsonObj.getInt("flagSwitch");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return flag != 2;
	}

	public static int parseUserLoginRecord(String json)
	{
		int status = 0;
		try
		{
			JSONObject obj = new JSONObject(json);
			status = Integer.valueOf(obj.getString("status")).intValue();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return status;
	}
}
