package com.czz.utils;


import com.czz.govsdk.GlobalConfig;
import com.czz.receiver.SharedNetwokReceiver;

import android.content.Context;
import android.content.IntentFilter;


public class NetworkFirewallManager {
    private static NetworkFirewallManager instance;
    private static Context mConext;
    private static VPNManager mAPNManager;
    private SharedNetwokReceiver netwokReceiver;


    public static NetworkFirewallManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkFirewallManager();
            mConext = context;
            mAPNManager = VPNManager.getInstance(context);
            SharedPreferencesManager.init(context);
        }
        return instance;
    }

    private static void NetworkFirewallManager() {

    }

    // 记录手机状态
    public void recordPhoneState() {
//		if(mAPNManager.getWifiState()){
//			SharedPreferencesManager.putBoolean(
//					GlobalConfig.SHARED_PREFERENCES_KEY_WIFI_STATE,
//					true);
//			mAPNManager.setWifiEnbled(false);
//		}else{
//			SharedPreferencesManager.putBoolean(
//					GlobalConfig.SHARED_PREFERENCES_KEY_WIFI_STATE,
//					false);
//		}
        if (mAPNManager.getWifiApState()) {
            SharedPreferencesManager.putBoolean(
                    GlobalConfig.SHARED_PREFERENCES_KEY_WIFI_AP_STATE,
                    true);
            mAPNManager.setWifiApEnabled(false);
        } else {
            SharedPreferencesManager.putBoolean(
                    GlobalConfig.SHARED_PREFERENCES_KEY_WIFI_AP_STATE,
                    false);
        }
        if (mAPNManager.getBluetoothState()) {
            SharedPreferencesManager.putBoolean(
                    GlobalConfig.SHARED_PREFERENCES_KEY_BLUETOOTH_STATE,
                    true);
            mAPNManager
                    .setBluetoothEnable(false);
        } else {
            SharedPreferencesManager.putBoolean(
                    GlobalConfig.SHARED_PREFERENCES_KEY_BLUETOOTH_STATE,
                    false);
        }

        SharedPreferencesManager.putBoolean(
                GlobalConfig.SHARED_PREFERENCES_KEY_IS_RECORD_STATE, true);
        // SharedPreferencesManager.putBoolean(
        // GlobalConfig.SHARED_PREFERENCES_KEY_GPRS_STATE, VPNManager
        // .getInstance(mConext).getGprsSwitchState());
    }

    // 恢复手机状态
    public void recoverPhoneState() {
        if (SharedPreferencesManager.getBoolean(
                GlobalConfig.SHARED_PREFERENCES_KEY_IS_RECORD_STATE, false)) {
//			if (SharedPreferencesManager.getBoolean(
//					GlobalConfig.SHARED_PREFERENCES_KEY_WIFI_STATE, false)) {
//
//				mAPNManager.setWifiEnbled(true);
//
//			}
            if (SharedPreferencesManager.getBoolean(
                    GlobalConfig.SHARED_PREFERENCES_KEY_WIFI_AP_STATE, false)) {
                mAPNManager.setWifiApEnabled(true);
            }
            if (SharedPreferencesManager.getBoolean(
                    GlobalConfig.SHARED_PREFERENCES_KEY_BLUETOOTH_STATE, false)) {

                mAPNManager
                        .setBluetoothEnable(true);

            }
            SharedPreferencesManager.putBoolean(
                    GlobalConfig.SHARED_PREFERENCES_KEY_IS_RECORD_STATE, false);
        }

    }

    // 禁用各种网络共享
    public void openNetworkFirewall() {

        netwokReceiver = new SharedNetwokReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter
                .addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        // intentFilter.addAction("android.net.conn.TETHER_STATE_CHANGED");
        intentFilter
                .addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        //intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        //mConext.getApplication().registerReceiver(netwokReceiver, intentFilter);
        mConext.getApplicationContext().registerReceiver(netwokReceiver, intentFilter);


    }

    // 放开各种网络共享
    public void closeNetworFirewall() {
        //关闭网络监听
        if (netwokReceiver != null) {
            mConext.getApplicationContext().unregisterReceiver(
                    netwokReceiver);
            netwokReceiver = null;
        }

    }
}
