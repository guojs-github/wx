/**
 * 微信参数access token
 * 2018.3.13 GuoJS
 */
package com.heel.wx;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.heel.utils.Log;

public class AccessToken extends WXObject {
	protected static AccessToken _instance = null;
	
	public static AccessToken instance() {
		if (null == _instance) {
			synchronized(AccessToken.class) {
				if (null == _instance) {
					_instance = new AccessToken();
				}				
			} // synchronized
		}
		
		return _instance;
	}
	
	public String value() { // Return access token value
		Log.info("AccessToken: value.");
		
		synchronized(AccessToken.class) {
			return super.value();
		}
	}

	public void refreshForced() {
		Log.info("AccessToken: refresh forced.");

		refresh();
	}
	
	public void refreshAsNeed(String result) {
		Log.info("AccessToken: refresh as need.");
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			
			if (WXRequest.invalidAccessToken(json))
				refresh();
						
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			json = null;
		}
	}
	
	protected String refresh() {
		Log.info("AccessToken: refresh.");
		
		String value = "";
		try {
			JSONObject result = WXRequest.accessToken();
			String accessToken = result.getString("access_token"); 
			Long expireIn = result.getLong("expires_in"); 
			expireIn -= 60L;  // 提前1分钟刷新
			
			if ((null != accessToken) && (null != expireIn)) {
				_expireTime = new Date();				
				_expireTime.setTime(_expireTime.getTime() + expireIn * 1000); /* 计算超时时间 */
				_value = accessToken;				
				
				value = _value;

				Log.info("access token:" + accessToken);				
				Log.info("expire in:" + expireIn.toString());
				Log.info("expire time:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(_expireTime));
			} 
		} catch (Exception ex) {
			Log.error(ex);
		}
		
		return value;
	}
	
}
