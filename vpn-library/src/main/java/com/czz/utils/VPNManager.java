package com.czz.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.codec.digest.DigestUtils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.ResultReceiver;
import android.util.Log;


public class VPNManager {
	private Class<? extends ConnectivityManager> connectivityManagerClass = ConnectivityManager.class;
	private static VPNManager instance;
	private Context mContext;
	private ConnectivityManager mConnectivityManager;
	private Class<?> bluetoothPanClass;
	private Object bluetoothPan;

	// private static final String CHINA_TELECOM_APN = "private.vpdn.bj";

	public static VPNManager getInstance(Context conext) {
		if (instance == null) {
			instance = new VPNManager(conext);

		}
		return instance;
	}

	public VPNManager(Context context) {
		this.mContext = context;
		mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			bluetoothPanClass = Class.forName("android.bluetooth.BluetoothPan");
			Constructor<?> bluetoothPanConstructor = bluetoothPanClass
					.getDeclaredConstructor(
							Context.class,
							Class.forName("android.bluetooth.BluetoothProfile$ServiceListener"));
			bluetoothPanConstructor.setAccessible(true);
			bluetoothPan = bluetoothPanConstructor.newInstance(
					mContext.getApplicationContext(), null);
		} catch (Throwable e) {
		}
	}

	public static final int WIFI_AP_STATE_DISABLED = 11;
	public static final int WIFI_AP_STATE_ENABLED = 13;

	// wifiAP状态
	public boolean getWifiApState() {
		int state = WIFI_AP_STATE_DISABLED;
		WifiManager mWiFiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);

		try {
			Class wifiManagerClass = mWiFiManager.getClass();
			Method method = wifiManagerClass.getMethod("getWifiApState");
			method.setAccessible(true);
			state = (Integer) method.invoke(mWiFiManager);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return state == WIFI_AP_STATE_ENABLED || state == 3;

	}

	// wifi热点开关
	// wifi热点开关
	public boolean setWifiApEnabled(boolean enabled) {
		Log.e("wsj", "=-=" +Build.VERSION.SDK_INT);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

			ConnectivityManager connManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

			Field iConnMgrField = null;
			try {
				iConnMgrField = connManager.getClass().getDeclaredField("mService");
				iConnMgrField.setAccessible(true);
				Object iConnMgr = iConnMgrField.get(connManager);
				Class<?> iConnMgrClass = Class.forName(iConnMgr.getClass().getName());

				if(enabled){
					Method startTethering = iConnMgrClass.getMethod("startTethering", int.class, ResultReceiver.class, boolean.class);
					startTethering.invoke(iConnMgr, 0, null, true);
				}else{
					Method startTethering = iConnMgrClass.getMethod("stopTethering", int.class);
					startTethering.invoke(iConnMgr, 0);
				}
				return  true;
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("wsj", "=-=" +e);
				return  false;
			}

		}else{
			WifiManager wifiManager = (WifiManager) mContext
					.getSystemService(Context.WIFI_SERVICE);
			if (enabled) { // disable WiFi in any case
				// wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi

				wifiManager.setWifiEnabled(false);

			}
			try {

				Method method = wifiManager.getClass().getMethod(
						"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
				// 返回热点打开状态

				method.setAccessible(true);
				return (Boolean) method.invoke(wifiManager, null, enabled);
			} catch (Exception e) {

				return false;

			}

		}

	}

	// usb tether
	public boolean isUsbTetheringOn() {
		try {
			String[] tethered = getTetheredIfaces();
			Method method = connectivityManagerClass
					.getMethod("getTetherableUsbRegexs");
			method.setAccessible(true);
			String[] mUsbRegexs = (String[]) method
					.invoke(this.mConnectivityManager);
			String usbIface = findIface(tethered, mUsbRegexs);
			if (usbIface != null) {
				return true;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getUsbTetheringIface() {
		String iface = null;
		try {

			String[] tethered = getTetheredIfaces();
			Method method = connectivityManagerClass
					.getMethod("getTetherableUsbRegexs");
			method.setAccessible(true);
			String[] mUsbRegexs = (String[]) method
					.invoke(this.mConnectivityManager);
			iface = findIface(tethered, mUsbRegexs);

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return iface;
	}

	public Boolean untetherIface(String iface) {
		int returnCode = 2;
		Object connectivityManager = mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		Method untether = null;
		try {
			untether = connectivityManager.getClass().getMethod("untether",
					new Class[] { String.class });
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnCode = (Integer) untether.invoke(connectivityManager,
					new Object[] { iface });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnCode != 2;
	}

	// setUsbTethering
	public int tetherIface(boolean iface) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		Method untether = null;
		try {
			untether = connectivityManager.getClass().getMethod(
					"setUsbTethering", new Class[] { boolean.class });
		} catch (Exception e) {
			e.printStackTrace();
		}
		int returnCode = 0;
		try {
			returnCode = (Integer) untether.invoke(connectivityManager,
					new Object[] { iface });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnCode;
	}

	private String[] getTetheredIfaces() {
		String[] result = null;
		try {
			Method method = connectivityManagerClass
					.getMethod("getTetheredIfaces");
			method.setAccessible(true);
			result = (String[]) method.invoke(this.mConnectivityManager);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String findIface(String[] ifaces, String[] regexes) {
		for (String iface : ifaces) {
			for (String regex : regexes) {
				if (iface.matches(regex)) {
					return iface;
				}
			}
		}
		return null;
	}

	// 蓝牙状态
	public boolean getBluetoothState() {
		BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
		boolean result = false;
		result = blueadapter.isEnabled();
		return result;
	}

	// 蓝牙开关
	public boolean setBluetoothEnable(boolean enable) {
		BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
		boolean result = false;
		if (enable) {
			result = blueadapter.enable();
		} else {
			result = blueadapter.disable();
		}
		return result;
	}

	// 检测wifi状态

	public boolean getWifiState() {
		boolean state = false;
		// 获取系统服务
		ConnectivityManager manager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// 获取状态
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 判断wifi已连接的条件
		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {

			return true;
		}

		return false;

	}

//	// 开关wifi
//	public boolean setWifiEnbled(boolean enabled) {
//		WifiManager wifiManager = (WifiManager) mContext
//				.getSystemService(Context.WIFI_SERVICE);
//
//		return wifiManager.setWifiEnabled(enabled);
//	}



	// GPRS状态
	public boolean getGPRSState() {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return networkInfo.isConnected();

	}
	// 对密码进行APN编码处理
	public String encodePassWord(String username, String password) {

		String md5Pwd = password
				+ DigestUtils.md5Hex(username + password).substring(1, 10);
		return md5Pwd;
	}

}
