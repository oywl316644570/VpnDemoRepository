// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GOVAPIFactory.java

package com.czz.govsdk;

import android.content.ComponentName;
import android.content.Context;

// Referenced classes of package com.czz.govsdk:
//			GOVApiImpl, IGOVAPI

public class GOVAPIFactory
{

	private static GOVApiImpl govApiImpl;

	public static IGOVAPI createGOVAPI(Context context, ComponentName mComponentName)
	{
		if (govApiImpl == null)
			govApiImpl = new GOVApiImpl(context, mComponentName);
		return govApiImpl;
	}

	private GOVAPIFactory()
	{
	}
}
