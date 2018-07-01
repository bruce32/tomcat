package core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WebServer {
	private ServerSocket serverSocket;
	private Executor pool;
	private DispatchServlet dispatchServlet;
	
	public WebServer() {
		try {
			serverSocket = new ServerSocket(8080);
			pool = Executors.newFixedThreadPool(30);
			dispatchServlet = new DispatchServlet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			while(true){
				pool.execute(new ServiceHandler(serverSocket.accept(), dispatchServlet));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WebServer service = new WebServer();
		service.run();
	}

}
