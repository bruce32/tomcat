package core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import http.HttpServletRequest;
import http.HttpServletResponse;

public class ServiceHandler implements Runnable{

	private Socket socket;
	private DispatchServlet dispatchServlet;
	
	public ServiceHandler(Socket socket, DispatchServlet dispatchServlet) {
		this.socket = socket;
		this.dispatchServlet = dispatchServlet;
	}

	@Override
	public void run() {
		try {
			HttpServletRequest request = new HttpServletRequest(socket);
			HttpServletResponse response = new HttpServletResponse(socket,request);
			String uri = request.getUri();
			File entity ;
			if (uri.endsWith(".do")) {
				dispatchServlet.service(request,response);
			}else {
				if ("/".equals(uri)) {
					entity = new File("webapps/index.html");
				}else {
					entity = new File("webapps"+uri);
				}
				if (!entity.exists()) {
					entity = new File("webapps/web/404.html");
					response.setStatueCode("404");
				}
				response.setEntity(entity);
				response.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
