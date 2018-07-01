package controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import http.HttpServletRequest;
import http.HttpServletResponse;
import util.RequestMapping;

public class FileController {
	@RequestMapping("/download.do")
	public void getPages(HttpServletRequest request,HttpServletResponse response) {
		response.setResponseHeader("Content-Disposition", "attachment;filename=pages.zip");
		response.setContentType("multipart/form-data");
		File file = new File("E://Tedu//Project//pages.zip");
		try (
			BufferedInputStream bis =
				new BufferedInputStream(new FileInputStream(file));
				){
			int d ;
			ByteArrayOutputStream bos = 
					new ByteArrayOutputStream();
			while((d=bis.read())!=-1){
				bos.write(d);
			}
			bos.close();
			response.setData(bos.toByteArray());
			response.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
