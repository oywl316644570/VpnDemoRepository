package com.czz.receiver;

import com.czz.utils.VPNManager;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class SharedNetwokReceiver extends BroadcastReceiver {

    public static final String ACTION_TETHER_STATE_CHANGED = "android.net.conn.TETHER_STATE_CHANGED";
    public static final String EXTRA_AVAILABLE_TETHER = "availableArray";
    public static final String EXTRA_ACTIVE_TETHER = "activeArray";
    public static final String EXTRA_ERRORED_TETHER = "erroredArray";
    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    public static final int WIFI_AP_STATE_DISABLING = 0;
    public static final int WIFI_AP_STATE_DISABLED = 1;
    public static final int WIFI_AP_STATE_ENABLING = 2;
    public static final int WIFI_AP_STATE_ENABLED = 3;
    public static final int WIFI_AP_STATE_FAILED = 4;

    public static final int NEW_WIFI_AP_STATE_DISABLING = 10;
    public static final int NEW_WIFI_AP_STATE_DISABLED = 11;
    public static final int NEW_WIFI_AP_STATE_ENABLING = 12;
    public static final int NEW_WIFI_AP_STATE_ENABLED = 13;
    public static final int NEW_WIFI_AP_STATE_FAILED = 14;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;
        String action = intent.getAction();
        // usb
        // if (action.equals(ACTION_TETHER_STATE_CHANGED)) {
        // new Thread() {
        // @Override
        // public void run() {
        // try {
        // sleep(500);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // if (VPNManager.getInstance(mContext).isUsbTetheringOn()) {
        // String iface = VPNManager.getInstance(mContext)
        // .getUsbTetheringIface();
        // VPNManager.getInstance(mContext).untetherIface(iface);
        // }
        // };
        //
        // }.start();
        //
        // }
        if (action.equals(WIFI_AP_STATE_CHANGED_ACTION)) {

            int wifi_ap_state = intent.getIntExtra(EXTRA_WIFI_AP_STATE,
                    WIFI_AP_STATE_FAILED);
            if (wifi_ap_state == WIFI_AP_STATE_ENABLED
                    || wifi_ap_state == NEW_WIFI_AP_STATE_ENABLED) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        boolean a = VPNManager.getInstance(mContext)
                                .setWifiApEnabled(false);

                    }

                    ;

                }.start();

            }

        }
        // wifi热点

        if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            if (state == BluetoothAdapter.STATE_ON) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        VPNManager.getInstance(mContext).setBluetoothEnable(
                                false);
                    }

                    ;

                }.start();

            }

        }
        // 这个监听wifi的打开
//		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
//
//			int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
//			if (WifiManager.WIFI_STATE_ENABLED == wifiState) {
//				new Thread() {
//					@Override
//					public void run() {
//						try {
//							sleep(500);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						VPNManager.getInstance(mContext).setWifiEnbled(false);
//					};
//
//				}.start();
//
//			}
//
//		}
    }
}
