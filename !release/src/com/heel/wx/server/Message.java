package com.heel.wx.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class Message extends HttpServlet {
	private static final long serialVersionUID = 0L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		System.out.println("Start process wx server get message...");
		
		String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();
        // if(CheckUtil.checkSignature(signature, timestamp, nonce)){
            out.print(echostr);
        // }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		System.out.println("Start process wx server post message...");

		PrintWriter writer = response.getWriter();
        Map<String, String[]> params = request.getParameterMap();
        String queryString = "";
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                queryString += key + "=" + value + "&";
            }
        }
        // 去掉最后一个空格
        queryString = queryString.substring(0, queryString.length() - 1);
        writer.println("POST " + request.getRequestURL() + " " + queryString);		
        System.out.println("POST " + request.getRequestURL() + " " + queryString);
        
        Map<String, String> data = Message.xmlToMap(request);
        System.out.println(data.toString());
	}
	
	public static Map<String, String> xmlToMap(HttpServletRequest request){

        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();

        try {
            InputStream ins = request.getInputStream();
            Document doc = reader.read(ins);
            Element root = doc.getRootElement();
            List<Element> list = root.elements();

            for (Element element : list) {
                map.put(element.getName(), element.getText());
            }
            ins.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return map;
    }


}
