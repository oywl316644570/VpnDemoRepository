package com.czz.utils;

import android.content.Context;
import android.provider.Settings;

import java.util.UUID;

public class PhoneInfoManager {

    private static String mIMEI;
    private static String mIMSI;
    private static String IMEI_KEY = "imei_key";
    private static String IMSI_KEY = "imsi_key";

    // 获取IMEI
    public static String getIMEI(Context context) {
        //mIMEI = StorageUtil.deDecode(IMEI_KEY); 欧阳注释掉
        mIMEI = "";
        if (mIMEI == null || mIMEI.equals("")) {
            //生成设备码 并保存设备码
            //mIMEI = getUUID();
            mIMEI = getAndoidID(context);
            //StorageUtil.enCode(IMEI_KEY, mIMEI); //欧阳注释掉
        }
        return mIMEI;
    }

    // 获取IMSI
    public static String getIMSI(Context context) {
        //mIMSI = StorageUtil.deDecode(IMSI_KEY); //欧阳注释掉
        mIMSI = "";
        if (mIMSI == null || mIMSI.equals("")) {
            //生成设备码 并保存设备码
            //mIMSI = getUUID();
            String andoidID = getAndoidID(context);
            mIMSI = andoidID + "a";
            //StorageUtil.enCode(IMSI_KEY, mIMSI); //欧阳注释掉
        }
        return mIMSI;
    }

    public static String getUUID() {
        String id = null;
        UUID uuid = UUID.randomUUID();
        id = uuid.toString();

        //去掉随机ID的短横线
        id = id.replace("-", "");

        //将随机ID换成数字
        int num = id.hashCode();
        //去绝对值
        num = num < 0 ? -num : num;

        id = String.valueOf(num);

        return id;
    }

    public static String getAndoidID(Context mContext) {

        String android_id = Settings.System.getString(
                mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        return android_id;
    }


}
