package de.blinkt.openvpn;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Administrator on 2016/4/19.
 */
public class VPNLoginStatus {
    public static Handler mHandler;
    public  static  String VPN_STATE;
    public static  final int ERROR_MESSAGE=111;
    public static  final int LOGIN_STATUS=222;
    public static  final int NO_NETWORK=333;
    public static void setmHandler(Handler mHandler) {
        VPNLoginStatus.mHandler = mHandler;
    }
    public  static void logErr(Object errMsg){
        if(mHandler!=null){
                Message msg=Message.obtain();
                msg.what=ERROR_MESSAGE;
                msg.obj=errMsg;
            mHandler.sendMessage(msg);
        }
    }
    public  static void logLoginStatus(String state){

        VPN_STATE=state;
        if(mHandler==null){
            return;
        }
        if("CONNECTED".equals(state)){

                Message msg=Message.obtain();
                msg.what=LOGIN_STATUS;
                msg.arg1=1;
                msg.obj=state;
                mHandler.sendMessage(msg);

        }else if("WAIT".equals(state)){
            Message msg=Message.obtain();
            msg.what=LOGIN_STATUS;
            msg.arg1=2;
            msg.obj=state;
            mHandler.sendMessage(msg);
        }else {
            Message msg=Message.obtain();
            msg.what=LOGIN_STATUS;
            msg.arg1=3;
            msg.obj=state;
            mHandler.sendMessage(msg);
        }

    }
    public  static void logNoNetwork(){
        if(mHandler!=null){
            Message msg=Message.obtain();
            msg.what=NO_NETWORK;
            mHandler.sendMessage(msg);
        }

    }
}
