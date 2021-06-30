// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GOVApiImpl.java

package com.czz.govsdk;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import com.czz.utils.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package com.czz.govsdk:
//			GlobalConfig, IGOVAPI

public class GOVApiImpl
	implements IGOVAPI
{
	public static interface GovErrorListener
	{

		public abstract void onErrorResponse(String s);
	}

	public static interface GovSuccessListener
	{

		public abstract void onSuccessResponse(String s);
	}


	private static final String TAG = "GOVApiImpl";
	private Context mContext;
	private APNManager mApnManager;
	private NetworkFirewallManager mFirewallManager;
	private String mImei;
	private String mImsi;
	public boolean isCancle;
	public boolean isLoggingIn;
	private Handler mHandler;
	private SharedPreferencesManager sharedPreferencesManager;

	GOVApiImpl(Context context, ComponentName mComponentName)
	{
		isCancle = false;
		isLoggingIn = false;
		mHandler = new Handler();
		mContext = context;
		mFirewallManager = NetworkFirewallManager.getInstance(context, mComponentName);
		sharedPreferencesManager = SharedPreferencesManager.getInstance(context);
		mApnManager = APNManager.getInstance(context, mComponentName);
	}

	public void loginGovNet(String userName, String userPassword, GovSuccessListener successListener, GovErrorListener errorListener)
	{
		if (isLoggingIn)
			errorListener.onErrorResponse("请勿重复登陆");
		else
			isLoggingIn = true;
		isCancle = false;
		if (!mApnManager.getGPRSState())
		{
			isLoggingIn = false;
			errorListener.onErrorResponse("移动数据网络异常");
		} else
		{
			mImei = PhoneInfoManager.getIMEI(mContext);
			mImsi = PhoneInfoManager.getIMSI(mContext);
			if ("000000000000000".equals(mImei) || mImsi == null)
			{
				isLoggingIn = false;
				errorListener.onErrorResponse("读取手机设备号失败");
			} else
			if (TextUtils.isEmpty(userName))
			{
				isLoggingIn = false;
				errorListener.onErrorResponse("账号不能为空");
			} else
			if (!GlobalConfig.APN_USERNAME_PATTERN.matcher(userName).matches())
			{
				isLoggingIn = false;
				errorListener.onErrorResponse("账号格式错误");
			} else
			if (TextUtils.isEmpty(userPassword))
			{
				isLoggingIn = false;
				errorListener.onErrorResponse("密码不能为空");
			} else
			if (!GlobalConfig.APN_PASSWORD_PATTERN.matcher(userPassword).matches())
			{
				isLoggingIn = false;
				errorListener.onErrorResponse("密码格式错误");
			} else
			if (System.currentTimeMillis() - sharedPreferencesManager.getLong("last_login_time", 0L) < 2000L)
			{
				isLoggingIn = false;
				errorListener.onErrorResponse("请勿频繁登录退出,请间隔2秒以上再操作!");
			} else
			{
				sharedPreferencesManager.putLong("last_login_time", System.currentTimeMillis());
				String perferApnId = mApnManager.getPreferApnHuaWei();
				if (perferApnId == null)
				{
					isLoggingIn = false;
					errorListener.onErrorResponse("获取当前接入点id失败");
				} else
				{
					Map apnInfos = mApnManager.getGovApnInfoHuaWei(userName, userPassword);
					if (apnInfos.size() != 8)
					{
						isLoggingIn = false;
						errorListener.onErrorResponse("初始化接入点参数失败");
					} else
					{
						List ids = mApnManager.queryApnHuaWei(apnInfos);
						if (ids != null && ids.contains(perferApnId))
						{
							repeatCheckNetwork(userName, successListener, errorListener);
						} else
						{
							sharedPreferencesManager.putString("default_internet_apn_id", perferApnId);
							boolean bool = mApnManager.loginToGovAPNHuaWei(apnInfos);
							if (bool)
							{
								repeatCheckNetwork(userName, successListener, errorListener);
							} else
							{
								isLoggingIn = false;
								errorListener.onErrorResponse("建立政务网接入点失败");
							}
						}
					}
				}
			}
		}
	}

	public void updateGovPassword(String username, String oldPassword, String newPassword, GovSuccessListener successListener, GovErrorListener errorListener)
	{
		Matcher localMatcher1 = GlobalConfig.PASSWORD_PATTERN.matcher(oldPassword);
		Matcher localMatcher2 = GlobalConfig.PASSWORD_PATTERN.matcher(newPassword);
		if (TextUtils.isEmpty(username))
			errorListener.onErrorResponse("账号不能为空");
		else
		if (!GlobalConfig.APN_USERNAME_PATTERN.matcher(username).matches())
			errorListener.onErrorResponse("账号格式错误");
		else
		if (TextUtils.isEmpty(oldPassword))
			errorListener.onErrorResponse("原密码不能为空");
		else
		if (!localMatcher1.matches())
			errorListener.onErrorResponse("原密码格式错误");
		else
		if (TextUtils.isEmpty(newPassword))
			errorListener.onErrorResponse("新密码不能为空");
		else
		if (!localMatcher2.matches())
			errorListener.onErrorResponse("新密码格式错误");
		else
		if (oldPassword.equals(newPassword))
			errorListener.onErrorResponse("新密码与原密码相同");
		else
			submitPassword(username, oldPassword, newPassword, successListener, errorListener);
	}

	public void loginOutGovNet(boolean isRecover)
	{
		isCancle = true;
		isLoggingIn = false;
		exitGovApn();
		mFirewallManager.closeNetworFirewall();
		mFirewallManager.recoverPhoneState(isRecover);
	}

	public boolean isGovOn()
	{
		return mApnManager.isGovnetOn();
	}

	public boolean isInternetOn()
	{
		return mApnManager.isInternetOn();
	}

	public boolean openGovNetworkFirewall()
	{
		boolean flag = false;
		if (mApnManager.isActiveProcess())
		{
			mFirewallManager.recordPhoneState();
			mFirewallManager.openNetworkFirewall();
			flag = true;
		}
		return flag;
	}

	private void exitGovApn()
	{
		String str = sharedPreferencesManager.getString("default_internet_apn_id", null);
		if (TextUtils.isEmpty(str))
		{
			Map apnInfo = new HashMap();
			apnInfo.put("apn", mApnManager.encodeDefaultAPN());
			apnInfo.put("mnc", mApnManager.getApnMncHuaWei());
			List ids = mApnManager.queryApnHuaWei(apnInfo);
			if (ids.size() > 0)
				str = (String)ids.get(0);
		}
		mApnManager.logoutGovAPNHuaWei(str);
	}

	private void repeatCheckNetwork(final String userName, final GovSuccessListener successListener, final GovErrorListener errorListener)
	{
		(new Thread() {

			final String val$userName;
			final GovSuccessListener val$successListener;
			final GovErrorListener val$errorListener;
			final GOVApiImpl this$0;

			public void run()
			{
				try
				{
					sleep(400L);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				for (int i = 0; i < 60; i++)
				{
					try
					{
						sleep(200L);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					if (isCancle)
						return;
					if (mApnManager.isGovnetOn())
					{
						mHandler.post(new Runnable() {

							final 1 this$1;

							public void run()
							{
								checkMobile(userName, successListener, errorListener);
							}

					
					{
						this.this$1 = 1.this;
						super();
					}
						});
						return;
					}
					if (i == 59)
						mHandler.post(new Runnable() {

							final 1 this$1;

							public void run()
							{
								exitGovApn();
								isLoggingIn = false;
								errorListener.onErrorResponse("建立政务网失败，请检查账号、密码是否填写错误");
							}

					
					{
						this.this$1 = 1.this;
						super();
					}
						});
				}

			}

			
			{
				this.this$0 = GOVApiImpl.this;
				userName = s;
				successListener = govsuccesslistener;
				errorListener = goverrorlistener;
				super();
			}
		}).start();
	}

	private void checkMobile(String phoneNum, final GovSuccessListener successListener, final GovErrorListener errorListener)
	{
		final String userName = phoneNum;
		(new Thread(new Runnable() {

			final String val$userName;
			final GovSuccessListener val$successListener;
			final GovErrorListener val$errorListener;
			final GOVApiImpl this$0;

			public void run()
			{
				try
				{
					URL url = new URL(GlobalConfig.CHECK_MOBILE);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST");
					conn.setConnectTimeout(5000);
					String body = (new StringBuilder()).append("name=").append(URLEncoder.encode(userName)).append("&IMEI=").append(URLEncoder.encode(mImei)).append("&IMSI=").append(URLEncoder.encode(mImsi)).toString();
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.getOutputStream();
					conn.getOutputStream().write(body.getBytes());
					int code = conn.getResponseCode();
					if (code == 200)
					{
						InputStream is = conn.getInputStream();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						int len = 0;
						byte buffer[] = new byte[1024];
						while ((len = is.read(buffer)) != -1) 
							baos.write(buffer, 0, len);
						is.close();
						baos.close();
						final String result = new String(baos.toByteArray());
						mHandler.post(new Runnable() {

							final String val$result;
							final 2 this$1;

							public void run()
							{
								if (!isCancle)
									loginRespond(result, successListener, errorListener);
							}

					
					{
						this.this$1 = 2.this;
						result = s;
						super();
					}
						});
					} else
					{
						mHandler.post(new Runnable() {

							final 2 this$1;

							public void run()
							{
								if (!isCancle)
								{
									exitGovApn();
									isLoggingIn = false;
									errorListener.onErrorResponse("请求服务器失败");
									return;
								} else
								{
									return;
								}
							}

					
					{
						this.this$1 = 2.this;
						super();
					}
						});
					}
				}
				catch (Exception e)
				{
					mHandler.post(new Runnable() {

						final 2 this$1;

						public void run()
						{
							if (!isCancle)
							{
								exitGovApn();
								isLoggingIn = false;
								errorListener.onErrorResponse("连接服务器异常");
								return;
							} else
							{
								return;
							}
						}

					
					{
						this.this$1 = 2.this;
						super();
					}
					});
				}
			}

			
			{
				this.this$0 = GOVApiImpl.this;
				userName = s;
				successListener = govsuccesslistener;
				errorListener = goverrorlistener;
				super();
			}
		})).start();
	}

	private void loginRespond(String response, GovSuccessListener successListener, GovErrorListener errorListener)
	{
label0:
		{
			isLoggingIn = false;
			if (TextUtils.isEmpty(response))
			{
				exitGovApn();
				errorListener.onErrorResponse("后台请求数据异常");
				return;
			}
			if (!JsonParse.parseServeSwitch(response))
				break label0;
			int userStatus;
			try
			{
				JSONObject jsonO = new JSONObject(response);
				userStatus = jsonO.getInt("userStatus");
				int result;
				if (userStatus == 1)
				{
					result = jsonO.getInt("result");
					if (result == 1)
						successListener.onSuccessResponse("登录成功");
					else
					if (result == 2)
					{
						exitGovApn();
						errorListener.onErrorResponse("检测到移动终端已更换");
					} else
					{
						exitGovApn();
						errorListener.onErrorResponse("检测到SIM已更换");
					}
					break MISSING_BLOCK_LABEL_248;
				}
				if (userStatus != 2)
					break MISSING_BLOCK_LABEL_164;
				result = jsonO.getInt("result");
				if (result == 1)
				{
					successListener.onSuccessResponse("注册成功");
					return;
				}
			}
			catch (JSONException localJSONException)
			{
				exitGovApn();
				errorListener.onErrorResponse("后台请求数据异常");
				return;
			}
		}
		exitGovApn();
		errorListener.onErrorResponse("用户注册失败");
		break MISSING_BLOCK_LABEL_248;
		if (userStatus == 3)
		{
			exitGovApn();
			errorListener.onErrorResponse("用户未审核");
		} else
		if (userStatus == 4)
		{
			exitGovApn();
			errorListener.onErrorResponse("所在单位已被禁用");
		} else
		{
			exitGovApn();
			errorListener.onErrorResponse("该用户不存在");
		}
		break MISSING_BLOCK_LABEL_248;
		exitGovApn();
		errorListener.onErrorResponse("后台服务已停止");
	}

	private void updateRespond(String response, GovSuccessListener successListener, GovErrorListener errorListener)
	{
		if (response != null && !"".equals(response))
		{
			if (!JsonParse.parseServeSwitch(response))
			{
				errorListener.onErrorResponse("后台服务已停止");
				return;
			}
			String result = null;
			try
			{
				result = (new JSONObject(response)).getString("result");
			}
			catch (JSONException e)
			{
				errorListener.onErrorResponse("后台请求数据异常");
			}
			if ("1".equals(result))
				successListener.onSuccessResponse("密码修改成功，请退出后重新登录");
			else
			if ("2".equals(result))
				errorListener.onErrorResponse("原密码输入错误");
			else
			if ("3".equals(result))
				errorListener.onErrorResponse("未查找到该用户");
			else
			if ("4".equals(result))
				errorListener.onErrorResponse("密码参数不能为空");
			else
			if ("5".equals(result))
				errorListener.onErrorResponse("该用户还未注册");
			else
			if ("6".equals(result))
				errorListener.onErrorResponse("您所在单位已被禁用");
			else
				errorListener.onErrorResponse("未知错误");
		} else
		{
			errorListener.onErrorResponse("后台请求数据异常");
		}
	}

	private void submitPassword(final String username, final String oldPinCode, final String newPinCode, final GovSuccessListener successListener, final GovErrorListener errorListener)
	{
		(new Thread(new Runnable() {

			final String val$oldPinCode;
			final String val$username;
			final String val$newPinCode;
			final GovSuccessListener val$successListener;
			final GovErrorListener val$errorListener;
			final GOVApiImpl this$0;

			public void run()
			{
				try
				{
					URL url = new URL(GlobalConfig.CHANGE_PIN_CODE);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST");
					conn.setConnectTimeout(5000);
					String body = (new StringBuilder()).append("OldPinCode=").append(URLEncoder.encode(oldPinCode)).append("&loginName=").append(URLEncoder.encode(username)).append("&NewPinCode=").append(URLEncoder.encode(newPinCode)).toString();
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.getOutputStream().write(body.getBytes());
					int code = conn.getResponseCode();
					if (code == 200)
					{
						InputStream is = conn.getInputStream();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						int len = 0;
						byte buffer[] = new byte[1024];
						while ((len = is.read(buffer)) != -1) 
							baos.write(buffer, 0, len);
						is.close();
						baos.close();
						final String result = new String(baos.toByteArray());
						mHandler.post(new Runnable() {

							final String val$result;
							final 3 this$1;

							public void run()
							{
								updateRespond(result, successListener, errorListener);
							}

					
					{
						this.this$1 = 3.this;
						result = s;
						super();
					}
						});
					} else
					{
						mHandler.post(new Runnable() {

							final 3 this$1;

							public void run()
							{
								errorListener.onErrorResponse("请求服务器失败");
							}

					
					{
						this.this$1 = 3.this;
						super();
					}
						});
					}
				}
				catch (Exception e)
				{
					mHandler.post(new Runnable() {

						final 3 this$1;

						public void run()
						{
							errorListener.onErrorResponse("连接服务器异常");
						}

					
					{
						this.this$1 = 3.this;
						super();
					}
					});
				}
			}

			
			{
				this.this$0 = GOVApiImpl.this;
				oldPinCode = s;
				username = s1;
				newPinCode = s2;
				successListener = govsuccesslistener;
				errorListener = goverrorlistener;
				super();
			}
		})).start();
	}








}
