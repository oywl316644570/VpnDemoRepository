package com.czz.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.czz.govsdk.GlobalConfig;

/**
 * 共享数据管理器 读、写
 */
public class SharedPreferencesManager {

	private static SharedPreferences sp;

	public static void init(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(
					GlobalConfig.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//			sp.registerOnSharedPreferenceChangeListener(listener);
		}
	}

	private static Editor getSharedPreferencesEditor() {
		return sp.edit();
	}

	public static int getInt(String key, int defaultValue) {
		return sp.getInt(key, defaultValue);
	}

	public static String getString(String key, String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}

	public static long getLong(String key, long defaultValue) {
		return sp.getLong(key, defaultValue);
	}

	public static float getFloat(String key, float defaultValue) {
		return sp.getFloat(key, defaultValue);
	}

	public static void putInt(String key, int value) {
		Editor editor = getSharedPreferencesEditor().putInt(key, value);
		editor.commit();
	}

	public static void putString(String key, String value) {
		Editor editor = getSharedPreferencesEditor().putString(key, value);
		editor.commit();
	}

	public static void putBoolean(String key, boolean value) {
		Editor editor = getSharedPreferencesEditor().putBoolean(key, value);
		editor.commit();
	}

	public static void putLong(String key, long value) {
		Editor editor = getSharedPreferencesEditor().putLong(key, value);
		editor.commit();
	}

	public static void putFloat(String key, float value) {
		Editor editor = getSharedPreferencesEditor().putFloat(key, value);
		editor.commit();
	}

	public static void removeByKey(String key) {
		Editor editor = getSharedPreferencesEditor();
		editor.remove(key);
		editor.commit();
	}

}
