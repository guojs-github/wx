/**
 * 微信常用功能
 * 2018.3.9 GuoJS
 */
package com.heel.wx;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.heel.utils.Log;
import com.heel.utils.MD5;
import com.heel.wx.pay.WXPayUtil;

public class WXUtil {

	public static Long timeStamp() {
		return System.currentTimeMillis()/1000;		
	}
	
	public static String createNonceStr(){
		String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int length = 15;
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		return sb.toString();
	}
	
	public static Map<String, String> httpsRequest(String url, Map<String,String> param) 
			throws IOException, ParserConfigurationException, SAXException {
		Log.info("httpsRequest.");

		String paramString = WXPayUtil.getRequestXml(param);
		String recvString = WXPayUtil.httpsRequestPost(url , paramString);
		Map<String,String> recvMap = WXPayUtil.doXMLParse(recvString);		
		
		return recvMap;
	}
	
	protected static String getRequestXml(Map<String,String> parameters){
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set<Entry<String, String>> es = parameters.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while(it.hasNext()) {  
	        Entry<String, String> entry = it.next();
	        String key = entry.getKey();
	        String value=entry.getValue();
	        sb.append("<"+key+">"+"<![CDATA["+value+"]]></"+key+">");
        }
		sb.append("</xml>");
		return sb.toString();
	} // create request data
	
	protected static String httpsRequestPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(param.getBytes("UTF-8"));
            outputStream.close();
            //out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            //out.print(param);
            // flush输出流的缓冲
            //out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    protected static Map<String, String> doXMLParse(String strxml) 
    		throws IOException, ParserConfigurationException, SAXException {  
    	strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");  

        if(null == strxml || "".equals(strxml)) {  
            return null;  
        }  
          
        Map<String, String> m = new HashMap<String, String>();  
            
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));  
        //SAXBuilder builder = new SAXBuilder();  
        DocumentBuilderFactory domfac=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder2 = domfac.newDocumentBuilder();
        Document doc = builder2.parse(in);
        //Document doc = builder.build(in);  
        Element root = doc.getDocumentElement();
        NodeList list = root.getChildNodes(); 
        for(int i=0;i<list.getLength();i++){  
            Node e =  list.item(i);  
            String k = e.getNodeName();  
            if(k=="#text"){
            	continue;
            }
            String v = "";  
            NodeList children = e.getChildNodes();  
            if(children.getLength()==0) {  
                v = e.getTextContent();  
            } else {  
                v = getChildrenText(children);  
            }  
                
            m.put(k, v);  
        }  
            
        //关闭流  
        in.close();  
            
        return m;  
    }  

    protected static String getChildrenText(NodeList children) {  
        StringBuffer sb = new StringBuffer();  
        if(children.getLength()>0) {  
            for(int i=0;i<children.getLength();i++){
                Node e = children.item(i);
                String name = e.getNodeName();  
                String value = e.getTextContent(); 
                NodeList list = e.getChildNodes();                  
                if(list.getLength()>0) {
                	sb.append("<" + name + ">");
                    sb.append(getChildrenText(list));  
                    sb.append("</" + name + ">");  
                }else 
                	sb.append(value);  
            }  
        }  
            
        return sb.toString();  
    } 

	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

}
