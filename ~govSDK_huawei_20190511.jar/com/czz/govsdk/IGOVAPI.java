// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IGOVAPI.java

package com.czz.govsdk;


// Referenced classes of package com.czz.govsdk:
//			GOVApiImpl

public interface IGOVAPI
{

	public abstract void loginGovNet(String s, String s1, GOVApiImpl.GovSuccessListener govsuccesslistener, GOVApiImpl.GovErrorListener goverrorlistener);

	public abstract void loginOutGovNet(boolean flag);

	public abstract void updateGovPassword(String s, String s1, String s2, GOVApiImpl.GovSuccessListener govsuccesslistener, GOVApiImpl.GovErrorListener goverrorlistener);

	public abstract boolean isGovOn();

	public abstract boolean isInternetOn();

	public abstract boolean openGovNetworkFirewall();
}
