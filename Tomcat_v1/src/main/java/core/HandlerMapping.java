package core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.HttpUtil;
import util.RequestMapping;

public class HandlerMapping {
	private Map<String, Handler> map;
	
	public HandlerMapping() {
		init();
	}
	
	private void init() {
		map = new HashMap<>();
		List<Object> controllers = HttpUtil.getControllers();
		for (Object object : controllers) {
			Class<?> class1 = object.getClass();
			Method[] methods = class1.getDeclaredMethods();
			for (Method method : methods) {
				RequestMapping anno = method.getAnnotation(RequestMapping.class);
				if (anno!=null) {
					String url = anno.value();
					Handler handler = new Handler(object, method);
					map.put(url, handler);
				}
			}
		}
	}

	public Handler getHandler(String uri) {
		return map.get(uri);
	}

}
