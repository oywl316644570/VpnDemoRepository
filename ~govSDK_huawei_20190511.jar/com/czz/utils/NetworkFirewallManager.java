// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NetworkFirewallManager.java

package com.czz.utils;

import android.content.ComponentName;
import android.content.Context;
import com.czz.govsdk.GlobalConfig;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.czz.utils:
//			APNManager, SharedPreferencesManager

public class NetworkFirewallManager
{

	private static NetworkFirewallManager instance;
	private static Context mConext;
	private static APNManager mAPNManager;
	private static SharedPreferencesManager sharedPreferencesManager;

	public static NetworkFirewallManager getInstance(Context context, ComponentName mComponentName)
	{
		if (instance == null)
		{
			instance = new NetworkFirewallManager();
			mConext = context;
			mAPNManager = APNManager.getInstance(context, mComponentName);
			sharedPreferencesManager = SharedPreferencesManager.getInstance(context);
		}
		return instance;
	}

	private NetworkFirewallManager()
	{
	}

	public void recordPhoneState()
	{
		sharedPreferencesManager.putBoolean("wifi_state", mAPNManager.getWifiState());
		sharedPreferencesManager.putBoolean("wifi_ap_state", mAPNManager.getWifiApState());
		sharedPreferencesManager.putBoolean("is_record_state", true);
	}

	public void recoverPhoneState(boolean isRecover)
	{
		if (sharedPreferencesManager.getBoolean("is_record_state", false))
		{
			if (isRecover)
			{
				if (sharedPreferencesManager.getBoolean("wifi_state", false))
					mAPNManager.setWifiEnbled(sharedPreferencesManager.getBoolean("wifi_state", false));
				if (sharedPreferencesManager.getBoolean("wifi_ap_state", false))
					mAPNManager.setWifiApEnabled(sharedPreferencesManager.getBoolean("wifi_ap_state", false));
			}
			sharedPreferencesManager.putBoolean("is_record_state", false);
		}
	}

	public void openNetworkFirewall()
	{
		List list = new ArrayList();
		list.add(mConext.getPackageName());
		mAPNManager.addPersistentAppHuawei(list);
		mAPNManager.setWifiDisabledHuaWei(true);
		mAPNManager.setWifiApDisabledHuaWei(true);
	}

	public void closeNetworFirewall()
	{
		List list = new ArrayList();
		list.add(mConext.getPackageName());
		mAPNManager.removePersistentAppHuawei(list);
		mAPNManager.setWifiDisabledHuaWei(false);
		mAPNManager.setWifiApDisabledHuaWei(false);
	}
}
