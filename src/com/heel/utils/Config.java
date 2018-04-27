/**
 * 配置读取
 * 2018.3.7 GuoJS
 */
package com.heel.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	protected static String _fileName = "com.heel.properties";
	
	public static String read(String key) {
		Log.info("Config read.");
		Log.info("key:" + key);
		String value = "";
		
		// Check
		if (null == key) {
			Log.info("Invalid key.");
			return "";
		}
		if (0 >= key.trim().length()) {
			Log.info("Invalid key.");
			return "";
		}

		// Read properties
		try {
			InputStream is = Config.class.getClassLoader().getResourceAsStream(_fileName);
			Properties prop = new Properties();
			prop.load(is);
			value = prop.getProperty(key);
			Log.info("value:" + value);
		} catch (IOException e) {
			Log.error(e);
		} catch (Exception e) {
			Log.error(e);
		}
		
		return value;		
	}
}
