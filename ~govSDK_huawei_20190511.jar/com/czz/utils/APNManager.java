// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   APNManager.java

package com.czz.utils;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.czz.govsdk.GlobalConfig;
import com.huawei.android.app.admin.*;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class APNManager
{

	private static final String TAG = "APNManager";
	private static APNManager instance;
	private Context mContext;
	private TelephonyManager mTelephonyManager;
	private static final int CHINA_UNICOM = 1;
	private static final int CHINA_MOBILE = 2;
	private static final int CHINA_TELECOM = 3;
	private static final String CHINA_UNICOM_APN = "BJDXWGB.YDOA.BJAPN";
	private static final String CHINA_MOBILE_APN = "DXQWG";
	private DeviceRestrictionManager mDeviceRestrictionManager;
	private DeviceNetworkManager mDeviceNetworkManager;
	private DevicePolicyManager mDevicePolicyManager;
	private DeviceApplicationManager mDeviceApplicationManager;
	private ComponentName mAdminName;
	private static final String CHINA_TELECOM_APN = "private.vpdn.bj";
	public static final int REQUEST_ENABLE = 110;
	public static final int WIFI_AP_STATE_DISABLED = 11;
	public static final int WIFI_AP_STATE_ENABLED = 13;

	public static APNManager getInstance(Context conext, ComponentName mComponentName)
	{
		if (instance == null)
			instance = new APNManager(conext, mComponentName);
		return instance;
	}

	public APNManager(Context context, ComponentName mComponentName)
	{
		mDeviceRestrictionManager = null;
		mDeviceNetworkManager = null;
		mDevicePolicyManager = null;
		mDeviceApplicationManager = null;
		mAdminName = null;
		mContext = context;
		mDevicePolicyManager = (DevicePolicyManager)context.getSystemService("device_policy");
		mDeviceRestrictionManager = new DeviceRestrictionManager();
		mDeviceNetworkManager = new DeviceNetworkManager();
		mDeviceApplicationManager = new DeviceApplicationManager();
		mAdminName = mComponentName;
		mTelephonyManager = (TelephonyManager)context.getSystemService("phone");
	}

	public boolean isActiveProcess()
	{
		return mDevicePolicyManager != null && mDevicePolicyManager.isAdminActive(mAdminName);
	}

	public void activeProcess(Activity activity)
	{
		if (!isActiveProcess())
		{
			Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
			intent.putExtra("android.app.extra.DEVICE_ADMIN", mAdminName);
			activity.startActivityForResult(intent, 110);
		}
	}

	public void addPersistentAppHuawei(List packageNames)
	{
		try
		{
			mDeviceApplicationManager.addPersistentApp(mAdminName, packageNames);
		}
		catch (Exception exception) { }
	}

	public void removePersistentAppHuawei(List packageNames)
	{
		try
		{
			mDeviceApplicationManager.removePersistentApp(mAdminName, packageNames);
		}
		catch (Exception exception) { }
	}

	public boolean setWifiDisabledHuaWei(boolean disabled)
	{
		mDeviceRestrictionManager.setWifiDisabled(mAdminName, disabled);
		return true;
		Exception e;
		e;
		Log.i("APNManager", e.toString());
		return false;
	}

	public boolean setWifiApDisabledHuaWei(boolean disabled)
	{
		mDeviceRestrictionManager.setWifiApDisabled(mAdminName, disabled);
		return true;
		Exception e;
		e;
		Log.i("APNManager", e.toString());
		return false;
	}

	public boolean setBluetoothDisabledHuaWei(boolean disabled)
	{
		mDeviceRestrictionManager.setBluetoothDisabled(mAdminName, disabled);
		return true;
		Exception e;
		e;
		Log.i("APNManager", e.toString());
		return false;
	}

	public boolean setUSBDataDisabledHuaWei(boolean disabled)
	{
		mDeviceRestrictionManager.setUSBDataDisabled(mAdminName, disabled);
		return true;
		Exception e;
		e;
		Log.i("APNManager", e.toString());
		return false;
	}

	public boolean loginToGovAPNHuaWei(Map apnInfo)
	{
		mDeviceNetworkManager.addApn(mAdminName, apnInfo);
		List apnIds = queryApnHuaWei(apnInfo);
		if (apnIds == null || apnIds.size() <= 0)
			break MISSING_BLOCK_LABEL_68;
		mDeviceNetworkManager.setPreferApn(mAdminName, (String)apnIds.get(0));
		return true;
		Exception e;
		e;
		Log.i("APNManager", e.toString());
		return false;
	}

	public boolean logoutGovAPNHuaWei(String id)
	{
		Map defaultApninfo = new HashMap();
		switch (getPhoneOperator())
		{
		case 2: // '\002'
			defaultApninfo.put("apn", "jxwtest-ddn.bj");
			break;

		case 1: // '\001'
			defaultApninfo.put("apn", "bjydzw.ydoa.bjapn");
			break;

		case 3: // '\003'
			defaultApninfo.put("apn", "private.vpdn.bj");
			break;
		}
		List apnIds = queryApnHuaWei(defaultApninfo);
		if (apnIds != null && apnIds.size() > 0)
		{
			String apnid;
			for (Iterator iterator = apnIds.iterator(); iterator.hasNext(); mDeviceNetworkManager.deleteApn(mAdminName, apnid))
				apnid = (String)iterator.next();

		}
		if (!TextUtils.isEmpty(id))
			mDeviceNetworkManager.setPreferApn(mAdminName, id);
		return true;
	}

	public void addAPNHuaWei(Map apnInfo)
	{
		try
		{
			mDeviceNetworkManager.addApn(mAdminName, apnInfo);
		}
		catch (Exception exception) { }
	}

	public List queryApnHuaWei(Map apnInfo)
	{
		List apnIds = null;
		try
		{
			apnIds = mDeviceNetworkManager.queryApn(mAdminName, apnInfo);
		}
		catch (Exception exception) { }
		return apnIds;
	}

	public String getApnMncHuaWei()
	{
		String mnc = null;
		String imsi = ((TelephonyManager)mContext.getSystemService("phone")).getSubscriberId();
		if (!TextUtils.isEmpty(imsi))
			mnc = imsi.substring(3, 5);
		return mnc;
	}

	public boolean updateApnHuaWei(Map apnInfo, String id)
	{
		try
		{
			mDeviceNetworkManager.updateApn(mAdminName, apnInfo, id);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	public String getPreferApnHuaWei()
	{
		String apnId = null;
		ConnectivityManager conManager = (ConnectivityManager)mContext.getSystemService("connectivity");
		NetworkInfo ni = conManager.getActiveNetworkInfo();
		if (ni != null)
		{
			String apn = ni.getExtraInfo();
			String imsi = ((TelephonyManager)mContext.getSystemService("phone")).getSubscriberId();
			Map apnInfo = new HashMap();
			apnInfo.put("apn", apn);
			if (!TextUtils.isEmpty(imsi))
			{
				apnInfo.put("mcc", imsi.substring(0, 3));
				apnInfo.put("mnc", imsi.substring(3, 5));
			}
			try
			{
				List apnIds = mDeviceNetworkManager.queryApn(mAdminName, apnInfo);
				if (apnIds != null && apnIds.size() > 0)
					apnId = (String)apnIds.get(0);
			}
			catch (Exception exception) { }
		}
		return apnId;
	}

	public Map getGovApnInfoHuaWei(String username, String password)
	{
		Map apnInfo = new HashMap();
		apnInfo.put("name", "ÕþÎñÍø");
		switch (getPhoneOperator())
		{
		case 2: // '\002'
			apnInfo.put("apn", "jxwtest-ddn.bj");
			apnInfo.put("user", (new StringBuilder()).append(username).append("@jxwtest").toString());
			break;

		case 1: // '\001'
			apnInfo.put("apn", "bjydzw.ydoa.bjapn");
			apnInfo.put("user", (new StringBuilder()).append(username).append("@bjgov").toString());
			break;

		case 3: // '\003'
			apnInfo.put("apn", "private.vpdn.bj");
			apnInfo.put("user", (new StringBuilder()).append(username).append("@bjsjxw.VPDN.bj").toString());
			break;
		}
		apnInfo.put("password", encodePassWord(username, password));
		apnInfo.put("authtype", "3");
		apnInfo.put("type", "default");
		String imsi = ((TelephonyManager)mContext.getSystemService("phone")).getSubscriberId();
		if (!TextUtils.isEmpty(imsi))
		{
			apnInfo.put("mcc", imsi.substring(0, 3));
			apnInfo.put("mnc", imsi.substring(3, 5));
		}
		return apnInfo;
	}

	public boolean getWifiApState()
	{
		int state = 11;
		WifiManager mWiFiManager = (WifiManager)mContext.getSystemService("wifi");
		try
		{
			Class wifiManagerClass = mWiFiManager.getClass();
			Method method = wifiManagerClass.getMethod("getWifiApState", new Class[0]);
			method.setAccessible(true);
			state = ((Integer)method.invoke(mWiFiManager, new Object[0])).intValue();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		return state == 13 || state == 3;
	}

	public boolean setWifiApEnabled(boolean enabled)
	{
		WifiManager wifiManager;
		wifiManager = (WifiManager)mContext.getSystemService("wifi");
		if (enabled)
			wifiManager.setWifiEnabled(false);
		Method method;
		method = wifiManager.getClass().getMethod("setWifiApEnabled", new Class[] {
			android/net/wifi/WifiConfiguration, Boolean.TYPE
		});
		method.setAccessible(true);
		return ((Boolean)method.invoke(wifiManager, new Object[] {
			null, Boolean.valueOf(enabled)
		})).booleanValue();
		Exception e;
		e;
		return false;
	}

	public boolean getBluetoothState()
	{
		BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
		boolean result = false;
		result = blueadapter.isEnabled();
		return result;
	}

	public boolean setBluetoothEnable(boolean enable)
	{
		BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
		boolean result = false;
		if (enable)
			result = blueadapter.enable();
		else
			result = blueadapter.disable();
		return result;
	}

	public boolean getWifiState()
	{
		WifiManager wifiManager = (WifiManager)mContext.getSystemService("wifi");
		if (wifiManager == null)
			return false;
		else
			return wifiManager.isWifiEnabled();
	}

	public boolean setWifiEnbled(boolean enabled)
	{
		WifiManager wifiManager = (WifiManager)mContext.getSystemService("wifi");
		return wifiManager.setWifiEnabled(enabled);
	}

	public boolean setMobileData(boolean pBoolean)
	{
		if (android.os.Build.VERSION.SDK_INT > 19)
		{
			TelephonyManager telephonyService = (TelephonyManager)mContext.getSystemService("phone");
			Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", new Class[] {
				Boolean.TYPE
			});
			setMobileDataEnabledMethod.invoke(telephonyService, new Object[] {
				Boolean.valueOf(pBoolean)
			});
		} else
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager)mContext.getSystemService("connectivity");
			Method method = mConnectivityManager.getClass().getMethod("setMobileDataEnabled", new Class[] {
				Boolean.TYPE
			});
			method.invoke(mConnectivityManager, new Object[] {
				Boolean.valueOf(pBoolean)
			});
		}
		return true;
		Exception e;
		e;
		e.printStackTrace();
		return false;
	}

	public boolean getGprsSwitchState()
	{
		Boolean isOpen = Boolean.valueOf(false);
		try
		{
			if (android.os.Build.VERSION.SDK_INT > 19)
			{
				TelephonyManager telephonyService = (TelephonyManager)mContext.getSystemService("phone");
				Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled", new Class[0]);
				if (null != getMobileDataEnabledMethod)
					isOpen = (Boolean)getMobileDataEnabledMethod.invoke(telephonyService, new Object[0]);
			} else
			{
				ConnectivityManager mConnectivityManager = (ConnectivityManager)mContext.getSystemService("connectivity");
				Method method = mConnectivityManager.getClass().getMethod("getMobileDataEnabled", new Class[0]);
				isOpen = (Boolean)method.invoke(mConnectivityManager, new Object[0]);
			}
		}
		catch (Exception e)
		{
			isOpen = Boolean.valueOf(getGPRSState());
		}
		return isOpen.booleanValue();
	}

	public boolean getGPRSState()
	{
		ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService("connectivity");
		NetworkInfo networkInfo = cm.getNetworkInfo(0);
		return networkInfo.isConnected();
	}

	public int getSelectedAPNType()
	{
		ConnectivityManager conManager = (ConnectivityManager)mContext.getSystemService("connectivity");
		NetworkInfo ni = conManager.getActiveNetworkInfo();
		if (ni != null)
		{
			String apn = ni.getExtraInfo();
			int type = getPhoneOperator();
			if (type == 1)
				return !"bjydzw.ydoa.bjapn".equals(apn) ? -1 : 1;
			if (type == 2)
				return !"jxwtest-ddn.bj".equals(apn) ? -1 : 1;
			if (type == 3)
				return !"private.vpdn.bj".equals(apn) ? -1 : 1;
			return !"jxwtest-ddn.bj".equals(apn) && !"bjydzw.ydoa.bjapn".equals(apn) && !"private.vpdn.bj".equals(apn) ? -1 : 1;
		} else
		{
			return -2;
		}
	}

	public String getAPNName()
	{
		ConnectivityManager conManager = (ConnectivityManager)mContext.getSystemService("connectivity");
		NetworkInfo ni = conManager.getActiveNetworkInfo();
		if (ni != null)
		{
			String apn = ni.getExtraInfo();
			return apn;
		} else
		{
			return "";
		}
	}

	public int getPhoneOperator()
	{
		TelephonyManager manager = (TelephonyManager)mContext.getSystemService("phone");
		String numeric = manager.getSimOperator();
		if ("46001".equals(numeric) || "46006".equals(numeric) || "46009".equals(numeric))
			return 1;
		if ("46000".equals(numeric) || "46002".equals(numeric) || "46004".equals(numeric) || "46007".equals(numeric))
			return 2;
		return !"46003".equals(numeric) && !"46012".equals(numeric) && !"46013".equals(numeric) && !"46099".equals(numeric) && !"46011".equals(numeric) ? 0 : 3;
	}

	public boolean isInternetOn()
	{
		return isMobileDataOn() && !isGovnetOn();
	}

	public boolean isMobileDataOn()
	{
		ConnectivityManager conManager = (ConnectivityManager)mContext.getSystemService("connectivity");
		NetworkInfo ni = conManager.getActiveNetworkInfo();
		return ni != null && ni.getType() == 0;
	}

	public boolean isGovnetOn()
	{
		ConnectivityManager conManager = (ConnectivityManager)mContext.getSystemService("connectivity");
		NetworkInfo ni = conManager.getActiveNetworkInfo();
		if (ni != null)
		{
			String apn = ni.getExtraInfo();
			int type = getPhoneOperator();
			if (type == 1)
			{
				if ("bjydzw.ydoa.bjapn".equals(apn))
					return true;
			} else
			if (type == 2)
			{
				if ("jxwtest-ddn.bj".equals(apn))
					return true;
			} else
			if (type == 3)
			{
				if ("private.vpdn.bj".equals(apn))
					return true;
			} else
			if ("jxwtest-ddn.bj".equals(apn) || "bjydzw.ydoa.bjapn".equals(apn) || "private.vpdn.bj".equals(apn))
				return true;
		}
		return false;
	}

	private String getOperators()
	{
		String simOperator = mTelephonyManager.getSimOperator();
		return simOperator;
	}

	public String encodeDefaultAPN()
	{
		String numeric = getOperators();
		if (numeric.equals("46001") || "46006".equals(numeric) || "46009".equals(numeric))
			return "3gnet";
		if (numeric.equals("46000") || numeric.equals("46002") || "46004".equals(numeric) || numeric.equals("46007"))
		{
			GlobalConfig.gov_apn = "jxwtest-ddn.bj";
			return "cmnet";
		}
		if (numeric.equals("46003") || numeric.equals("46012") || numeric.equals("46013") || numeric.equals("46099") || "46011".equals(numeric))
		{
			GlobalConfig.gov_apn = "private.vpdn.bj";
			return "ctnet";
		} else
		{
			return "";
		}
	}

	public String encodeAPN()
	{
		int type = getPhoneOperator();
		if (type == 1)
			return "BJDXWGB.YDOA.BJAPN";
		if (type == 2)
			return "DXQWG";
		if (type == 3)
			return "private.vpdn.bj";
		else
			return "";
	}

	public String encodePassWord(String username, String password)
	{
		String md5Pwd = (new StringBuilder()).append(password).append(md5((new StringBuilder()).append(username).append(password).toString()).substring(1, 10)).toString();
		return md5Pwd;
	}

	public static String md5(String string)
	{
		if (TextUtils.isEmpty(string))
			return "";
		MessageDigest md5 = null;
		String result;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte bytes[] = md5.digest(string.getBytes());
		result = "";
		byte abyte0[] = bytes;
		int i = abyte0.length;
		for (int j = 0; j < i; j++)
		{
			byte b = abyte0[j];
			String temp = Integer.toHexString(b & 0xff);
			if (temp.length() == 1)
				temp = (new StringBuilder()).append("0").append(temp).toString();
			result = (new StringBuilder()).append(result).append(temp).toString();
		}

		return result;
		NoSuchAlgorithmException e;
		e;
		e.printStackTrace();
		return "";
	}
}
