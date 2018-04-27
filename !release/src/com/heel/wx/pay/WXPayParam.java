/**
 * 微信参数准备
 * 2018.3.1 GuoJS
 */
package com.heel.wx.pay;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.heel.utils.Log;
import com.heel.wx.WXConfig;

public class WXPayParam {

	public static Map<String,String> prepay(
			String appId
			, String mchId
			, String apiKey
			, String notifyUrl
			, String tradeType
			, String orderNO
			, long amount
			, String description
			, String openId
			, String expireTime) throws IOException, ParserConfigurationException, SAXException {
		Log.info("WXPayParam: prepay.");
		String nonceStr = WXPayUtil.createNonceStr();
		String spbillCreateIp = WXPayUtil.getHostIp();
		
		SortedMap<String,String> map = new TreeMap<String,String>();
		map.put("appid", appId);
		map.put("mch_id", mchId);
		map.put("nonce_str", nonceStr);
		map.put("spbill_create_ip", spbillCreateIp);
		map.put("notify_url", notifyUrl);
		map.put("trade_type", tradeType);
		map.put("body", description);
		map.put("out_trade_no", orderNO);
		map.put("total_fee", String.valueOf(amount));
		map.put("openid", openId);
		map.put("time_expire", expireTime);
		map.put("sign", WXPayUtil.createSign(map, apiKey));

		return map;
	}
	
	public static Map<String,String> pay(String prepayId) {
		Log.info("WXPayParam: pay.");		
		String appId = WXConfig.appId();
		String nonceStr = WXPayUtil.createNonceStr();
		String timeStamp = WXPayUtil.timeStamp().toString();
		String packageStr = "prepay_id=" + prepayId;
		
		SortedMap<String,String> map = new TreeMap<String,String>();
		map.put("appId", appId);
		map.put("timeStamp", timeStamp);
		map.put("nonceStr", nonceStr);
		map.put("package", packageStr);
		map.put("signType", "MD5");
		map.put("paySign", WXPayUtil.createSign(map, WXConfig.apiKey()));		
		
		return map;
	}
	
	public static String expireTime(long timeout) {
		Log.info("WXPayParam: expire time.");		

		/* 超时时长控制在1分钟到1个小时之间，默认为半小时 */
		long timeoutLength = ((60 <= timeout) && (3600 >= timeout)) ? timeout: 1800;
		Date expireTime = new Date();
		expireTime.setTime(expireTime.getTime() + timeoutLength * 1000); /* 计算超时时间 */		
		
		return (new SimpleDateFormat("yyyyMMddHHmmss")).format(expireTime);
	}

}
