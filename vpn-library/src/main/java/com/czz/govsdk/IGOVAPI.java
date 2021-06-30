package com.czz.govsdk;

import com.czz.govsdk.GOVApiImpl.GovErrorListener;
import com.czz.govsdk.GOVApiImpl.GovSuccessListener;




public interface IGOVAPI {

	public void loginGovNet(String username, String password,
							GovSuccessListener successListener, GovErrorListener errorListener);
	public void logoutGovNet();
	public void updateGovPassword(String username, String oldPassword,
								  String newPassword, GovSuccessListener successListener,
								  GovErrorListener errorListener);
	public boolean isGovOn();
	public void openGovNetworkFirewall();
	public void closeGovNetworkFirewall();
}
