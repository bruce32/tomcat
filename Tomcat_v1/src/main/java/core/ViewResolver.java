package core;

import java.io.File;
import java.io.IOException;

import http.HttpServletRequest;
import http.HttpServletResponse;

public class ViewResolver {

	public void process(Object returnVal, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path = (String) returnVal;
		if (path.contains("redirect")) {
			response.sendRedirect(path.split(":")[1]);
			return ;
		}
		File file = new File(path);
		if (file.exists()) {
			response.setEntity(file);
			response.flush();
		}
	}

}
