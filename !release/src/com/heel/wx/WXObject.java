/**
 * 微信参数对象
 * 2018.3.13 GuoJS
 */
package com.heel.wx;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.heel.utils.Log;

public class WXObject {
	protected String _value = null;  
	protected Date _expireTime = null;

	public String value() { // Return object value
		Log.info("WXObject: value.");
		String value = get();
		return value;
	}

	protected String get() { // Get object value
		Log.info("WXObject: get.");
		
		if (needRefresh()) 
			_value = refresh();
		
		return _value;
	}
	
	protected boolean needRefresh() {
		Log.info("WXObject: need refresh.");
		
		if (null == _value)
			return true;
		if (null == _expireTime)
			return true;
				
		Date now = new Date();
		
		Log.info("value:" + _value);
		Log.info("expire time:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(_expireTime));
		
		if (_expireTime.before(now))
			return true;
		
		return false;
	}
	
	protected String refresh() {
		Log.info("WXObject: refresh.");
		
		return "";		
	}
	
}
