/**
 * 微信API调用
 * 2018.3.13 GuoJS
 */
package com.heel.wx;

import org.json.JSONObject;

import com.heel.utils.Log;
import com.heel.utils.Net;

public class WXRequest {
		
	public static JSONObject oauth2AccessToken(String code) {
		Log.info("WXRequest:OAuth2 access token.");
		Log.info("Code:" + code);
		
		JSONObject result = null;
		final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		
		try {
			String requestUrl = url; 		
			requestUrl = requestUrl.replace("APPID", WXConfig.appId());
			requestUrl = requestUrl.replace("SECRET", WXConfig.appSecret());
			requestUrl = requestUrl.replace("CODE", code);
			Log.info("requestUrl:" + requestUrl);
			
			result = Net.httpsRequestGet(requestUrl, null);

		} catch (Exception ex) {
			Log.error(ex);
		} 
		
		return result;		
	}
	
	public static boolean invalidAccessToken(JSONObject result) {
		Log.info("WXRequest:Invalid access token");

		// Invalid access token ?
		if (!result.has("errcode")) return false;
		if (WXErrCode.InvalidAccessToken() != result.getLong("errcode")) return false;
		
		return true;		
	}
	
	public static JSONObject jsapiTicket() {
		Log.info("WXRequest:Get JSAPI ticket.");
		
		JSONObject result = null;
		final String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=JSAPI";
		
		try {
			String requestUrl = url; 		
			requestUrl = requestUrl.replace("ACCESS_TOKEN", AccessToken.instance().value());
			Log.info("requestUrl:" + requestUrl);
			
			result = Net.httpsRequestGet(requestUrl, null);
			
		} catch (Exception ex) {
			Log.error(ex);
		} 
		
		return result;				
	}
	
	public static JSONObject accessToken() {
		Log.info("WXRequest:Get access token.");
		
		JSONObject result = null;
		final String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		
		try {
			String requestUrl = url; 		
			requestUrl = requestUrl.replace("APPID", WXConfig.appId());
			requestUrl = requestUrl.replace("APPSECRET", WXConfig.appSecret());
			Log.info("requestUrl:" + requestUrl);
			
			result = Net.httpsRequestGet(requestUrl, null);

		} catch (Exception ex) {
			Log.error(ex);
		} 
		
		return result;				
	}
	
}
