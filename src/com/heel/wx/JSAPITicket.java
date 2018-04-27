/**
 * JSAPITicket管理对象
 * 2018.3.13 GuoJS
 */
package com.heel.wx;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.heel.utils.Log;

public class JSAPITicket extends WXObject {
	protected static JSAPITicket _instance = null;
	
	public static JSAPITicket instance() {
		if (null == _instance) {
			synchronized(AccessToken.class) {
				if (null == _instance) {
					_instance = new JSAPITicket();
				}				
			} // synchronized
		}
		
		return _instance;
	}
	
	
	public String value() { // Return ticket value
		Log.info("JSAPITicket: value.");
		
		synchronized(JSAPITicket.class) {
			return super.value();
		}
	}
		
	protected String refresh() {
		Log.info("JSAPITicket: refresh.");
		
		String value = "";
		try {
			JSONObject result = WXRequest.jsapiTicket();
			
			if (WXRequest.invalidAccessToken(result)) { // 如果是因为access token失效导致失败，则刷新access token重试
				AccessToken.instance().refreshForced();
				result = WXRequest.jsapiTicket();
			}
			
			String ticket = result.getString("ticket"); 
			Long expireIn = result.getLong("expires_in");
			expireIn = 300L;  // 五分钟刷一次，反正access token不变的情况下ticket也不会变化
			
			if ((null != ticket) && (null != expireIn)) {
				_expireTime = new Date();				
				_expireTime.setTime(_expireTime.getTime() + expireIn * 1000); //计算超时时间
				_value = ticket;
				
				value = _value;
				Log.info("ticket:" + ticket);				
				Log.info("expire in:" + expireIn.toString());
				Log.info("expire time:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(_expireTime));
			} 
		} catch (Exception ex) {
			Log.error(ex);
		}
		
		return value;
	}
	
}
