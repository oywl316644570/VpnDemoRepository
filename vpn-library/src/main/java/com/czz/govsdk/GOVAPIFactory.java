package com.czz.govsdk;



import android.content.Context;

public class GOVAPIFactory {
	private static IGOVAPI mIGOVAPI;
	public static IGOVAPI createGOVAPI(Context context) {
		if(mIGOVAPI==null){
			mIGOVAPI=new GOVApiImpl(context);
		}
		return mIGOVAPI;
	}

	private GOVAPIFactory() {

	}
}
