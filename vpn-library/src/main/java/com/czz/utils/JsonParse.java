package com.czz.utils;

import org.json.JSONException;
import org.json.JSONObject;


import android.text.TextUtils;

public class JsonParse {
	//
	public static boolean parseServeSwitch(String result) {
		if (TextUtils.isEmpty(result)) {
			return true;
		}
		int flag = -1;
		try {
			JSONObject jsonObj = new JSONObject(result);
			flag = jsonObj.getInt("flagSwitch");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (flag == 2) {
			return false;
		}
		return true;

	}

	
	/**
	 * 用户是否注册
	 * 
	 */
	public static int parseUserLoginRecord(String json) {
		int status = 0;
		try {
			JSONObject obj = new JSONObject(json);
			status = Integer.valueOf(obj.getString("status"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return status;
	}

	
}
