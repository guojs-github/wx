/**
 * 微信统一配置
 * 2018.2.28 GuoJS
 */
package com.heel.wx;

import com.heel.utils.Config;

public class WXConfig {
	protected static String _appId = "";
	protected static String _appSecret = "";
	protected static String _mchId = "";
	protected static String _apiKey = ""; // 商户支付用的Key，在后台进行设置的
	protected static String _notifyUrl = ""; 
	
	public static String appId() {
		if ("" == _appId) {
			_appId = Config.read("wx.appId");
		}
			
		return _appId;
	}

	public static String appSecret() {
		if ("" == _appSecret) {
			_appSecret = Config.read("wx.appSecret");
		}
			
		return _appSecret;
	}

	public static String mchId() {
		if ("" == _mchId) {
			_mchId = Config.read("wx.mchId");
		}

		return _mchId;
	}

	public static String apiKey() {
		if ("" == _apiKey) {
			_apiKey = Config.read("wx.apiKey");
		}

		return _apiKey;
	}

	public static String notifyUrl() {
		if ("" == _notifyUrl) {
			_notifyUrl = Config.read("wx.notifyUrl");
		}

		return _notifyUrl;
	}
}
