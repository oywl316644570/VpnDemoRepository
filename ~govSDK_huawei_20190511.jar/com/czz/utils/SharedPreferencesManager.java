// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SharedPreferencesManager.java

package com.czz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.czz.govsdk.GlobalConfig;

public class SharedPreferencesManager
{

	private static SharedPreferences sp;
	private static SharedPreferencesManager spManager;

	private SharedPreferencesManager(Context context)
	{
		sp = context.getSharedPreferences("rootApnGovSDK", 0);
	}

	public static SharedPreferencesManager getInstance(Context context)
	{
		if (spManager == null)
			spManager = new SharedPreferencesManager(context);
		return spManager;
	}

	private android.content.SharedPreferences.Editor getSharedPreferencesEditor()
	{
		return sp.edit();
	}

	public int getInt(String key, int defaultValue)
	{
		return sp.getInt(key, defaultValue);
	}

	public String getString(String key, String defaultValue)
	{
		return sp.getString(key, defaultValue);
	}

	public boolean getBoolean(String key, boolean defaultValue)
	{
		return sp.getBoolean(key, defaultValue);
	}

	public long getLong(String key, long defaultValue)
	{
		return sp.getLong(key, defaultValue);
	}

	public float getFloat(String key, float defaultValue)
	{
		return sp.getFloat(key, defaultValue);
	}

	public void putInt(String key, int value)
	{
		android.content.SharedPreferences.Editor editor = getSharedPreferencesEditor().putInt(key, value);
		editor.commit();
	}

	public void putString(String key, String value)
	{
		android.content.SharedPreferences.Editor editor = getSharedPreferencesEditor().putString(key, value);
		editor.commit();
	}

	public void putBoolean(String key, boolean value)
	{
		android.content.SharedPreferences.Editor editor = getSharedPreferencesEditor().putBoolean(key, value);
		editor.commit();
	}

	public void putLong(String key, long value)
	{
		android.content.SharedPreferences.Editor editor = getSharedPreferencesEditor().putLong(key, value);
		editor.commit();
	}

	public void putFloat(String key, float value)
	{
		android.content.SharedPreferences.Editor editor = getSharedPreferencesEditor().putFloat(key, value);
		editor.commit();
	}

	public void removeByKey(String key)
	{
		android.content.SharedPreferences.Editor editor = getSharedPreferencesEditor();
		editor.remove(key);
		editor.commit();
	}
}
