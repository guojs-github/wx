/**
 * 网络相关处理
 * 2018.2.28 GuoJS
 */
package com.heel.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.json.JSONObject;

public class Net {
	private static final String GET = "GET";
	private static final String POST = "POST";

	public static JSONObject httpsRequestGet(String url, String out) {
		Log.info("getHTTPS");
		Log.info("url:" + url);
		Log.info("out:" + out);
		return httpsRequest(url, GET, out);
	}
	
	protected static JSONObject httpsRequest(String url, String method, String out) {
		Log.info("Net.getHTTPS");
		Log.info("url:" + url);
		Log.info("method:" + method);
		Log.info("out:" + out);
		
		JSONObject result = null;
		
		InputStreamReader isr = null;
		BufferedReader br = null;
		HttpsURLConnection httpUrlConn = null;
		
		try {
			StringBuffer buffer = new StringBuffer();
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, null, new SecureRandom());

			// 获取SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();			
			URL requestUrl = new URL(url);
			httpUrlConn = (HttpsURLConnection) requestUrl.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(method);
			Log.info("open connection.");

			// request
			if (GET.equalsIgnoreCase(method))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != out) {
				OutputStream os = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				os.write(out.getBytes("UTF-8"));
				os.close();

				Log.info("send data:" + os );				
			}
			

			// 将返回的输入流转换成字符串
			InputStream is = httpUrlConn.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			String str = null;
			while (null != (str = br.readLine())) {
				buffer.append(str);
			}
			br.close(); br = null;
			isr.close(); isr = null;
			Log.info("receive data:" + buffer.toString() );
			
			// close
			httpUrlConn.disconnect(); httpUrlConn = null;		
			Log.info("disconnect.");
			
			result = new JSONObject(buffer.toString());
			
		} catch (ConnectException e) {
			Log.error(e);
		} catch (Exception e) {
			Log.error(e);
		} finally {
			if (null != br) {
				try { br.close(); } catch (Exception e) { Log.error(e);} finally { br = null;}
			}
			if (null != isr) {
				try { isr.close(); } catch (Exception e) { Log.error(e);} finally { br = null;}
			}
			if (null != httpUrlConn) {
				try { httpUrlConn.disconnect(); } catch (Exception e) { Log.error(e);} finally { br = null;}
			}
		}
		
		return result;
	}
}
