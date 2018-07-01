package core;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import http.HttpServletRequest;
import http.HttpServletResponse;

public class DispatchServlet {
	private HandlerMapping handlerMapping;
	private ViewResolver viewResolver;
	
	public DispatchServlet() {
		init();
	}
	private void init() {
		handlerMapping = new HandlerMapping();
		viewResolver = new ViewResolver();
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IOException 
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		String uri = request.getUri();
		Handler handler = handlerMapping.getHandler(uri);
		Object object = handler.getObject();
		Method method = handler.getMethod();
		Class<?>[] types = method.getParameterTypes();
		List<Object> params = new ArrayList<>();
		for (Class<?> class1 : types) {
			if (class1==HttpServletRequest.class) {
				params.add(request);
			}
			if (class1==HttpServletResponse.class) {
				params.add(response);
			}
		}
		Object returnVal = method.invoke(object, params.toArray());
		if (returnVal == null) {
			return;
		}
		viewResolver.process(returnVal,request,response);
	}

}












