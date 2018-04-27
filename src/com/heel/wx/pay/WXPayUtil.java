/**
 * 微信支付工具类
 * 2018.3.1 GuoJS
 */
package com.heel.wx.pay;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.heel.utils.Log;
import com.heel.utils.MD5;
import com.heel.wx.WXUtil;

public class WXPayUtil extends WXUtil {

    public static String getHostIp() {  
        try {  
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
                NetworkInterface intf = en.nextElement();  
                
                if (intf.getName().equals("usbnet0")) {  
                    continue;  
                }
                
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                    InetAddress inetAddress = enumIpAddr.nextElement();  
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                    	//Log.i("ip", inetAddress.getHostAddress());
                    	return inetAddress.getHostAddress();
                    }  
                }  
            }  
        } catch (SocketException ex) {  
        	Log.error(ex);
        }  
        
        return null;
    } // getHostIp

	public static String createSign(SortedMap<String,String> parameters,String apiKey){
        StringBuffer sb = new StringBuffer();  
        Set<Entry<String, String>> es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）  
        Iterator<Entry<String, String>> it = es.iterator();  
        while(it.hasNext()) {
            Entry<String, String> entry = it.next();
            String k = entry.getKey();  
            String v = entry.getValue();
            if(null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {  
            	sb.append(k + "=" + v + "&"); 
            }  
        }  
        sb.append("key=" + apiKey);
        String sign = null;
		sign = MD5.encode(sb.toString(),"utf-8").toUpperCase();

        return sign;
    } // createSign
    
}
