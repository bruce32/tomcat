package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class HttpUtil {
	private static Map<String, String> mimeMapping;
	private static Map<String, String> statueCodeMapping;
	private static List<Object> apps;
	static{
		init();
	}

	private static void init() {
		initMimeMapping();
		initStatueCodeMapping();
		initApps();
	}

	private static void initApps() {
		apps = new ArrayList<>();
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(HttpUtil.class
					.getClassLoader().getResourceAsStream("app.xml"));
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> elements = root.elements("bean");
			for (Element ele : elements) {
				String className = ele.attributeValue("class");
				if (className!=null) {
					apps.add(Class.forName(className).newInstance());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initStatueCodeMapping() {
		statueCodeMapping = new HashMap<>();
		Properties properties = new Properties();
		try {
			properties.load(HttpUtil.class.getClassLoader()
					.getResourceAsStream("statue.properties"));
			Enumeration<Object> keys = properties.keys();
			while(keys.hasMoreElements()){
				String key = (String) keys.nextElement();
				String value = properties.getProperty(key);
				statueCodeMapping.put(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void initMimeMapping() {
		mimeMapping = new HashMap<>();
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(HttpUtil.class
					.getClassLoader().getResourceAsStream("web.xml"));
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> elements = root.elements("mime-mapping");
			for (Element ele : elements) {
				String key = ele.elementTextTrim("extension");
				String value = ele.elementTextTrim("mime-type");
				mimeMapping.put(key, value);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static String getMimeType(String extension) {
		return mimeMapping.get(extension);
	}
	public static String getStatueDescribe(String statueCode) {
		return statueCodeMapping.get(statueCode);
	}
	public static List<Object> getControllers() {
		return apps;
	}
	
}
