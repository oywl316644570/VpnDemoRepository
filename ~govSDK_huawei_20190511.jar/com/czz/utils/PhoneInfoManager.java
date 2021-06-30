// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PhoneInfoManager.java

package com.czz.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

public class PhoneInfoManager
{

	private static String mIMEI;
	private static String mIMSI;

	public PhoneInfoManager()
	{
	}

	public static String getIMEI(Context context)
	{
		String deviceId = null;
		if (mIMEI != null)
			return mIMEI;
		TelephonyManager telManager = (TelephonyManager)context.getSystemService("phone");
		try
		{
			deviceId = telManager.getDeviceId();
		}
		catch (Exception e)
		{
			Log.e("gov", (new StringBuilder()).append("无法获取设备id").append(e.getLocalizedMessage()).toString());
		}
		if (TextUtils.isEmpty(deviceId))
			deviceId = android.provider.Settings.System.getString(context.getContentResolver(), "android_id");
		return deviceId;
	}

	public static String getIMSI(Context context)
	{
		String ismi = null;
		if (mIMSI != null)
			return mIMSI;
		TelephonyManager telManager = (TelephonyManager)context.getSystemService("phone");
		try
		{
			ismi = telManager.getSubscriberId();
		}
		catch (Exception e)
		{
			Log.e("gov", (new StringBuilder()).append("无法获取设备imsi").append(e.getLocalizedMessage()).toString());
		}
		if (TextUtils.isEmpty(ismi))
		{
			String ipAddress = getIPAddress(context);
			if (!TextUtils.isEmpty(ipAddress))
				ismi = md5(ipAddress);
			if (ismi.length() > 16)
				ismi = ismi.substring(0, 16);
		}
		return ismi;
	}

	public static String getIPAddress(Context context)
	{
		NetworkInfo info;
		info = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
		if (info == null || !info.isConnected())
			break MISSING_BLOCK_LABEL_156;
		if (info.getType() != 0)
			break MISSING_BLOCK_LABEL_121;
		Enumeration en = NetworkInterface.getNetworkInterfaces();
_L4:
		Enumeration enumIpAddr;
		if (!en.hasMoreElements())
			break MISSING_BLOCK_LABEL_156;
		NetworkInterface intf = (NetworkInterface)en.nextElement();
		enumIpAddr = intf.getInetAddresses();
_L2:
		InetAddress inetAddress;
		if (!enumIpAddr.hasMoreElements())
			break; /* Loop/switch isn't completed */
		inetAddress = (InetAddress)enumIpAddr.nextElement();
		if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
			return inetAddress.getHostAddress();
		if (true) goto _L2; else goto _L1
_L1:
		if (true) goto _L4; else goto _L3
_L3:
		SocketException e;
		e;
		e.printStackTrace();
		break MISSING_BLOCK_LABEL_156;
		if (info.getType() == 1)
		{
			WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
			return ipAddress;
		}
		return null;
	}

	public static String intIP2StringIP(int ip)
	{
		return (new StringBuilder()).append(ip & 0xff).append(".").append(ip >> 8 & 0xff).append(".").append(ip >> 16 & 0xff).append(".").append(ip >> 24 & 0xff).toString();
	}

	public static String md5(String string)
	{
		if (TextUtils.isEmpty(string))
			return "";
		MessageDigest md5 = null;
		StringBuilder result;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte bytes[] = md5.digest(string.getBytes());
		result = new StringBuilder();
		byte abyte0[] = bytes;
		int i = abyte0.length;
		for (int j = 0; j < i; j++)
		{
			byte b = abyte0[j];
			String temp = Integer.toHexString(b & 0xff);
			if (temp.length() == 1)
				temp = (new StringBuilder()).append("0").append(temp).toString();
			result.append(temp);
		}

		return result.toString();
		NoSuchAlgorithmException e;
		e;
		e.printStackTrace();
		return "";
	}
}
