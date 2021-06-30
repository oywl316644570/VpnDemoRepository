package com.czz.govsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.czz.utils.JsonParse;
import com.czz.utils.NetworkFirewallManager;
import com.czz.utils.PhoneInfoManager;
import com.czz.utils.VPNManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;

import de.blinkt.openvpn.R;
import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ConfigParser;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.PRNGFixes;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VpnStatus;

public class GOVApiImpl implements IGOVAPI {
    private static final String TAG = "GOVApiImpl";
    private Context mContext;
    private VPNManager mApnManager;
    private long lastLoginTime;
    private String mImei;
    private String mImsi;
    public boolean isCancle = false;
    private Handler mHandler = new Handler();
    private NetworkFirewallManager mFirewallManager;
    protected OpenVPNService mService;

    GOVApiImpl(Context context) {
        this.mContext = context;
        //vpn
        PRNGFixes.apply();
        VpnStatus.initLogCache(mContext.getApplicationContext().getCacheDir());
        // StorageUtil.init(context);//ouyang 注释掉
        mFirewallManager = NetworkFirewallManager.getInstance(context);
        this.mApnManager = VPNManager.getInstance(context);
        //  VpnStatus.addStateListener(mStateListener);
    }

    @Override
    public boolean isGovOn() {
        // TODO Auto-generated method stub

        return VpnStatus.getLastLevel() == VpnStatus.ConnectionStatus.LEVEL_CONNECTED;
    }

    @Override
    public void loginGovNet(final String userName, String userPassword,
                            final GovSuccessListener successListener, final GovErrorListener errorListener) {
        isCancle = false;
        if (TextUtils.isEmpty(userName)) {
            errorListener.onErrorResponse("账号不能为空");
            return;
        } else if (!GlobalConfig.USERNAME_PATTERN.matcher(userName).matches()) {
            errorListener.onErrorResponse("账号格式错误");
            return;
        } else if (TextUtils.isEmpty(userPassword)) {
            errorListener.onErrorResponse("密码不能为空");
            return;
        } else if (!GlobalConfig.PASSWORD_PATTERN.matcher(userPassword).matches()) {
            errorListener.onErrorResponse("密码格式错误");
            return;
        }
        this.mImei = PhoneInfoManager.getIMEI(mContext);
        this.mImsi = PhoneInfoManager.getIMSI(mContext);
        Log.e("czz", mImei + "--" + mImsi);
        if (("000000000000000".equals(this.mImei)) || (this.mImsi == null)) {
            errorListener.onErrorResponse("读取手机设备号失败");
        } else {

            if (System.currentTimeMillis()
                    - lastLoginTime < 2000L) {
                errorListener.onErrorResponse("请勿频繁登录退出,请间隔2秒以上再操作!");

            } else {
//                SharedPreferencesManager
//                        .putLong(
//                                GlobalConfig.SHARED_PREFERENCES_KEY_LAST_LOGIN_TIME,
//                                System.currentTimeMillis());
                lastLoginTime = System.currentTimeMillis();
                ConfigParser cp = new ConfigParser();
                VpnProfile vpnProfile = null;
                try {
                    InputStreamReader isr = new InputStreamReader(mContext.getResources().openRawResource(R.raw.clientczz));

                    cp.parseConfig(isr);
                    vpnProfile = cp.convertProfile();


                } catch (IOException | ConfigParser.ConfigParseError e) {
                    //Toast.makeText(LoginActivity.this, "获取证书失败", Toast.LENGTH_SHORT).show();
                    errorListener.onErrorResponse("获取证书失败");
                    return;
                }
                if (vpnProfile == null) {
                    errorListener.onErrorResponse("获取证书失败,请查看证书是否过期");
                } else {
                    vpnProfile.mUsername = userName;

                    vpnProfile.mPassword = userPassword + DigestUtils.md5Hex(userName + userPassword).substring(1, 10);

                    vpnProfile.mConnections[0].mServerName = GlobalConfig.VPN_SERVER_IP;
                    //	VPNLoginStatus.setmHandler(mhandler);
                    Intent intent = new Intent(mContext, OpenVPNService.class);
                    intent.putExtra("vpnProfile", vpnProfile);
                    intent.putExtra(OpenVPNService.ALWAYS_SHOW_NOTIFICATION, true);

                    mContext.startService(intent);
                    VpnStatus.mLoginStateListener = new VpnStatus.StateListener() {

                        @Override
                        public void updateState(String state, final String logmessage, final int localizedResId, VpnStatus.ConnectionStatus level) {


                            if (localizedResId == R.string.state_auth_failed) {

                                //   VpnStatus.removeStateListener(this);
                                VpnStatus.mLoginStateListener = null;
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorListener.onErrorResponse(mContext.getString(R.string.main_hint_vpn_user_check_fail));
                                        exitGovApn();
                                    }
                                });
                            } else if (localizedResId == R.string.state_nonetwork) {


                                //  VpnStatus.removeStateListener(this);
                                VpnStatus.mLoginStateListener = null;
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorListener.onErrorResponse(mContext.getString(R.string.state_nonetwork));
                                        exitGovApn();
                                    }
                                });
                            } else if (level.equals(VpnStatus.ConnectionStatus.LEVEL_NOTCONNECTED)) {


                                VpnStatus.mLoginStateListener = null;
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorListener.onErrorResponse(mContext.getString(localizedResId));

                                        exitGovApn();
                                    }
                                });
                            } else if (level.equals(VpnStatus.ConnectionStatus.LEVEL_CONNECTED)) {
                                VpnStatus.mLoginStateListener = null;
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
//                                        successListener.onSuccessResponse(mContext.getString(localizedResId));
                                        checkMobile(userName, successListener, errorListener);
                                    }
                                });

                            }


                        }
                    };
                }
            }
        }
    }


    @Override
    public void updateGovPassword(String username, String oldPassword,
                                  String newPassword, GovSuccessListener successListener,
                                  GovErrorListener errorListener) {
        Matcher localMatcher1 = GlobalConfig.PASSWORD_PATTERN
                .matcher(oldPassword);
        Matcher localMatcher2 = GlobalConfig.PASSWORD_PATTERN
                .matcher(newPassword);
        if (TextUtils.isEmpty(username)) {
            errorListener.onErrorResponse("账号不能为空");
        } else if (!GlobalConfig.USERNAME_PATTERN.matcher(username).matches()) {
            errorListener.onErrorResponse("账号格式错误");
        } else if (TextUtils.isEmpty(oldPassword)) {
            errorListener.onErrorResponse("原密码不能为空");
        } else if (!localMatcher1.matches()) {
            errorListener.onErrorResponse("原密码格式错误");
        } else if (TextUtils.isEmpty(newPassword)) {
            errorListener.onErrorResponse("新密码不能为空");
        } else if (!localMatcher2.matches()) {
            errorListener.onErrorResponse("新密码格式错误");
        } else if (oldPassword.equals(newPassword)) {
            errorListener.onErrorResponse("新密码与原密码相同");
        } else {
            submitPassword(username, oldPassword, newPassword, successListener,
                    errorListener);
        }
    }

    // 切换接入点并恢复手机状态
    @Override
    public void logoutGovNet() {
        isCancle = true;
        exitGovApn();

    }

    // 开启网络防火墙
    @Override
    public void openGovNetworkFirewall() {

        mFirewallManager.recordPhoneState();
        mFirewallManager.openNetworkFirewall();

    }

    @Override
    public void closeGovNetworkFirewall() {
        mFirewallManager.closeNetworFirewall();
        mFirewallManager.recoverPhoneState();

    }

    // 切换接入点

    private void exitGovApn() {

        ProfileManager.setConntectedVpnProfileDisconnected(mContext);
        mService = OpenVPNService.getOpenVPNService();

        if (mService != null && mService.getManagement() != null) {

            mService.getManagement().stopVPN(false);
            mService.stopSelf();

        }

    }


    // 检测终端是否变更
    private void checkMobile(String phoneNum,
                             final GovSuccessListener successListener,
                             final GovErrorListener errorListener) {
        final String userName = phoneNum;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // urlConnection请求服务器，验证
                try {
                    // 1：url对象
                    URL url = new URL(GlobalConfig.CHECK_MOBILE);

                    // 2;url.openconnection
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();

                    // 3设置请求参数
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(5 * 1000);
                    // map.put("name", userName);
                    // map.put("IMEI", mImei);
                    // map.put("IMSI", mImsi);
                    // 请求头的信息
                    String body = "name=" + URLEncoder.encode(userName)
                            + "&IMEI=" + URLEncoder.encode(mImei) + "&IMSI="
                            + URLEncoder.encode(mImsi);
                    // conn.setRequestProperty("Content-Length",
                    // String.valueOf(body.length()));
                    // conn.setRequestProperty("Cache-Control", "max-age=0");
                    // conn.setRequestProperty("Origin",
                    // "http://192.168.1.100:8081");

                    // 设置conn可以写请求的内容
                    conn.setDoOutput(true);
                    conn.setDoInput(true); // 发送POST请求必须设置允许输入
                    // setDoInput的默认值就是true
                    conn.getOutputStream();
                    conn.getOutputStream().write(body.getBytes());
                    // 4响应码
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        // 创建字节输出流对象
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // 定义读取的长度
                        int len = 0;
                        // 定义缓冲区
                        byte buffer[] = new byte[1024];
                        // 按照缓冲区的大小，循环读取
                        while ((len = is.read(buffer)) != -1) {
                            // 根据读取的长度写入到os对象中
                            baos.write(buffer, 0, len);
                        }
                        // 释放资源
                        is.close();
                        baos.close();
                        // 返回字符串
                        final String result = new String(baos.toByteArray());
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (!isCancle) {
                                    loginRespond(result, successListener,
                                            errorListener);
                                }

                            }
                        });
                    } else {

                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (!isCancle) {
                                    exitGovApn();
                                    errorListener.onErrorResponse("请求服务器失败");
                                    return;
                                }
                            }
                        });

                    }
                } catch (Exception e) {
                    Log.e("=-=", "err= " + e.getMessage());
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (!isCancle) {
                                exitGovApn();
                                errorListener.onErrorResponse("连接服务器异常");
                                return;
                            }
                        }
                    });

                }
            }
        }).start();

    }

    // 登录响应
    private void loginRespond(String response,
                              GovSuccessListener successListener, GovErrorListener errorListener) {
        if (TextUtils.isEmpty(response)) {
            exitGovApn();
            errorListener.onErrorResponse("后台请求数据异常");
            return;
        }
        if (JsonParse.parseServeSwitch(response)) {
            try {
                JSONObject jsonO = new JSONObject(response);
                int userStatus = jsonO.getInt("userStatus");

                if (userStatus == 1) {
                    int result = jsonO.getInt("result");
                    if (result == 1) {
                        // GOVAPIMessageManager.logGovInfo(
                        // true,
                        // "",
                        // GOVAPIService.this
                        // .getApplicationContext());
                        Log.i("=-=", "login success");
                        successListener.onSuccessResponse("登录成功");
                    } else if (result == 2) {
                        exitGovApn();
                        errorListener.onErrorResponse("检测到移动终端已更换");
                    } else {
                        exitGovApn();
                        errorListener.onErrorResponse("检测到SIM已更换");
                    }

                } else if (userStatus == 2) {
                    int result = jsonO.getInt("result");
                    if (result == 1) {
                        successListener.onSuccessResponse("注册成功");
                        return;
                    } else {
                        exitGovApn();
                        errorListener.onErrorResponse("用户注册失败");
                    }

                } else if (userStatus == 3) {
                    exitGovApn();
                    errorListener.onErrorResponse("用户未审核");
                } else if (userStatus == 4) {
                    exitGovApn();
                    errorListener.onErrorResponse("所在单位已被禁用");

                } else {
                    exitGovApn();
                    errorListener.onErrorResponse("该用户不存在");
                }

            } catch (JSONException localJSONException) {
                exitGovApn();
                errorListener.onErrorResponse("后台请求数据异常");
                return;
            }
        } else {
            exitGovApn();
            errorListener.onErrorResponse("后台服务已停止");
        }
    }

    // 修改密码响应
    private void updateRespond(String response,
                               GovSuccessListener successListener, GovErrorListener errorListener) {
        // * 返回值：
        // * 1：设置成功
        // * 2：旧PIN码输入错误
        // * 3：用户不存在
        // 4：新/旧PIN码未输入
        // 5：用户未注册，无权修改
        // 6：单位已被禁用，无权修改

        if (response != null && !"".equals(response)) {
            if (!JsonParse.parseServeSwitch(response)) {
                errorListener.onErrorResponse("后台服务已停止");
                return;
            }
            String result = null;
            try {
                result = new JSONObject(response).getString("result");
            } catch (JSONException e) {
                errorListener.onErrorResponse("后台请求数据异常");
            }
            if ("1".equals(result)) {
                successListener.onSuccessResponse("密码修改成功，请退出后重新登录");

            } else if ("2".equals(result)) {
                errorListener.onErrorResponse("原密码输入错误");
            } else if ("3".equals(result)) {
                errorListener.onErrorResponse("未查找到该用户");

            } else if ("4".equals(result)) {
                errorListener.onErrorResponse("密码参数不能为空");

            } else if ("5".equals(result)) {
                errorListener.onErrorResponse("该用户还未注册");

            } else if ("6".equals(result)) {
                errorListener.onErrorResponse("您所在单位已被禁用");

            } else {
                errorListener.onErrorResponse("未知错误");
            }

        } else {
            // 修改失败
            errorListener.onErrorResponse("后台请求数据异常");
        }

    }

    //
    // 修改密码
    private void submitPassword(final String username, final String oldPinCode,
                                final String newPinCode, final GovSuccessListener successListener,
                                final GovErrorListener errorListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // urlConnection请求服务器，验证
                try {
                    // 1：url对象
                    URL url = new URL(GlobalConfig.CHANGE_PIN_CODE);

                    // 2;url.openconnection
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();

                    // 3设置请求参数
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(5 * 1000);
                    // map.put("OldPinCode", oldPinCode);
                    // map.put("loginName", username);
                    // map.put("NewPinCode", newPinCode);
                    // 请求头的信息
                    String body = "OldPinCode=" + URLEncoder.encode(oldPinCode)
                            + "&loginName=" + URLEncoder.encode(username)
                            + "&NewPinCode=" + URLEncoder.encode(newPinCode);
                    // conn.setRequestProperty("Content-Length",
                    // String.valueOf(body.length()));
                    // conn.setRequestProperty("Cache-Control", "max-age=0");
                    // conn.setRequestProperty("Origin",
                    // "http://192.168.1.100:8081");

                    // 设置conn可以写请求的内容
                    conn.setDoOutput(true);
                    conn.setDoInput(true); // 发送POST请求必须设置允许输入
                    // setDoInput的默认值就是true
                    conn.getOutputStream().write(body.getBytes());

                    // 4响应码
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        // 创建字节输出流对象
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // 定义读取的长度
                        int len = 0;
                        // 定义缓冲区
                        byte buffer[] = new byte[1024];
                        // 按照缓冲区的大小，循环读取
                        while ((len = is.read(buffer)) != -1) {
                            // 根据读取的长度写入到os对象中
                            baos.write(buffer, 0, len);
                        }
                        // 释放资源
                        is.close();
                        baos.close();
                        // 返回字符串
                        final String result = new String(baos.toByteArray());
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                updateRespond(result, successListener,
                                        errorListener);
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                errorListener.onErrorResponse("请求服务器失败");
                            }
                        });

                    }
                } catch (Exception e) {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            errorListener.onErrorResponse("连接服务器异常");
                        }
                    });

                }
            }
        }).start();

    }

    public interface GovSuccessListener {
        void onSuccessResponse(String response);

    }

    public interface GovErrorListener {

        void onErrorResponse(String errorResponse);
    }

}
