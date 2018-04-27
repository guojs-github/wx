/**
 * 日志输出
 * 2018.2.27 GuoJS
 */
package com.heel.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
	private static Logger _logger = LoggerFactory.getLogger("");

	public static void info(String message) {
		// Check
		if (null == message) 
			return;
		
		// Output
		_logger.info(message);
	}

	public static void error(Exception e) {
		// Check
		if (null == e) 
			return;
		
		// Output
		e.printStackTrace();
	}
}
