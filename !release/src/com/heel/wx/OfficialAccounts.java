/**
 * 微信公众号相关功能
 * 2018.2.27 GuoJS
 */
package com.heel.wx;

import org.json.JSONObject;

import com.heel.utils.Log;
import com.heel.wx.pay.WXPay;
import com.heel.wx.pay.WXPayParam;

public class OfficialAccounts {
		
	public static JSONObject oauth2AccessToken(String code) {
		/*
			获得wx code参考发起调用
			权限样例：
			snsapi_base
			snsapi_userinfo

			链接样例1：
			https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd83231a58bf0ef5a&redirect_uri=http%3a%2f%2fwl.zhouzhongyuan.club%2fpet&response_type=code&scope=snsapi_base&state=pet

			链接样例2：
			https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd1d5c242c8a2968f&redirect_uri=http%3a%2f%2fwl.zhouzhongyuan.club%2fyigo%2fpub%2fweixin&response_type=code&scope=snsapi_base&state=pet
		*/

		Log.info("OfficialAccounts: OAuth2 access token.");
		Log.info("Code:" + code);
		
		return  WXRequest.oauth2AccessToken(code);		
	}

	public static JSONObject prepay(String openId, String orderNO, String description, long amount) {
		Log.info("prepay.");
		
		return WXPay.prepay(openId, orderNO, description, amount, 1800);
	}

	public static JSONObject prepay(String openId, String orderNO, String description, long amount, long timeout ) {
		Log.info("prepay.");
		
		return WXPay.prepay(openId, orderNO, description, amount, timeout);
	}
	
	public static JSONObject configParam(String url) {
		Log.info("OfficialAccounts: JSAPI config param");
		
		return WXParam.config(url);	
	}
	
	public static String accessToken() {
		Log.info("OfficialAccounts: get access token.");
		
		return AccessToken.instance().value();
	}

	public static String ticket() {
		Log.info("OfficialAccounts: get JSAPI ticket.");
		
		return JSAPITicket.instance().value();
	}

	public static void checkAccessToken(String result) {
		Log.info("OfficialAccounts: check access token.");

		AccessToken.instance().refreshAsNeed(result);
	}
}
