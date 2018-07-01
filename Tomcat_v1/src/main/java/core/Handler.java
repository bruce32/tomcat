package core;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Handler implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object object;
	private Method method;
	
	public Handler(){
		
	}
	
	public Handler(Object object, Method method) {
		super();
		this.object = object;
		this.method = method;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	
}
