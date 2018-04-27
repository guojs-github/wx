package com.heel.wx;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;

import com.heel.utils.Log;


public class WXParam {
	public static JSONObject config(String url) {
		Log.info("Get config param");
		String nonceStr = WXUtil.createNonceStr();
		String timeStamp = WXUtil.timeStamp().toString();
		String ticket = JSAPITicket.instance().value();
		String signature = "";

		// Sign
		String temp = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timeStamp + "&url=" + url;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(temp.getBytes("UTF-8"));
			signature = WXUtil.byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			Log.error(e);
		} catch (UnsupportedEncodingException e) {
			Log.error(e);
		}
		
		JSONObject param = new JSONObject();
		param.put("appId", WXConfig.appId());
		param.put("timeStamp", timeStamp);
		param.put("nonceStr", nonceStr);
		param.put("signature", signature);

		return param;		
	}

}
