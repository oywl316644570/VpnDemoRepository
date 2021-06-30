package com.czz.utils;

import android.content.Context;

import com.tencent.mmkv.MMKV;

public class StorageUtil {
    public static void init(Context context) {
        MMKV.initialize(context);
    }

    public static void enCode(String key, String value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static String deDecode(String key) {
        return MMKV.defaultMMKV().decodeString(key, "");
    }

    public static void enCodeBoolean(String key, boolean value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static boolean deCodeBoolean(String key) {
       return MMKV.defaultMMKV().decodeBool(key, false);
    }
}
