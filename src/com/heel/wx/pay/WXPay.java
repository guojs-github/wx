/**
 * 微信支付
 * 2018.3.1 GuoJS
 */
package com.heel.wx.pay;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.heel.utils.Log;
import com.heel.wx.WXConfig;

public class WXPay {
	protected static final String PAY_METHOD_JS = "JSAPI";

	public static JSONObject prepay(String openId, String orderNO, String description, long amount, long timeout) {
		Log.info("WX prepay.");
		final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

		JSONObject result = null;
		try {
			// Check
			if (null == openId)
				throw new Exception("Invalid openId.");
			if (0 >= openId.trim().length())
				throw new Exception("Invalid openId.");
			Log.info("openId:" + openId);

			if (null == orderNO)
				throw new Exception("Invalid orderNO.");
			if (0 >= orderNO.trim().length())
				throw new Exception("Invalid orderNO.");
			Log.info("orderNO:" + orderNO);
						
			if (null == description)
				throw new Exception("Invalid description.");
			if (0 >= description.trim().length())
				throw new Exception("Invalid description.");
			Log.info("description:" + description);

			if (0 >= amount)
				throw new Exception("Invalid amount.");
			Log.info("amount:" + amount);
			
			// Pay
			String appId = WXConfig.appId();
			Log.info("appId:" + appId);
			String mchId = WXConfig.mchId();
			Log.info("mchId:" + mchId);
			String apiKey = WXConfig.apiKey();
			Log.info("apiKey:" + apiKey);
			String notifyUrl = WXConfig.notifyUrl();			
			Log.info("notifyUrl:" + notifyUrl);
			String expireTime = WXPayParam.expireTime(timeout);
			Log.info("expireTime:" + expireTime);
			try {
				// Prepare parameters
				Map<String,String> param = WXPayParam.prepay(
						appId
						, mchId
						, apiKey
						, notifyUrl
						, PAY_METHOD_JS
						, orderNO
						, amount 
						, description
						, openId
						, expireTime
				);

				// Request				
				Map<String,String> recvMap = WXPayUtil.httpsRequest(url, param);

				// Generate pay param
				String prepayId = recvMap.get("prepay_id");				
				Map<String,String> payParam = WXPayParam.pay(prepayId);
				
				payParam.put("time_expire", expireTime);
				result = new JSONObject(payParam);
			} catch (IOException | ParserConfigurationException | SAXException ex) {
				Log.error(ex);
			}

		} catch (Exception ex) {
			Log.error(ex);
		}
		
		return result;
	}
		
}
