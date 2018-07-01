package http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import util.HttpUtil;

public class HttpServletResponse implements HttpServlet{

	private OutputStream outputStream;
	private String protocol;
	private String statueCode;
	private String statueDescribe;
	private Map<String, String> responseHeaderMap;
	/**
	 * 除了响应文件数据，也可以响应二进制数据，ajax请求时，相应json
	 */
	private File entity;
	private byte[] data ;
	
	public HttpServletResponse(Socket socket, HttpServletRequest request) throws IOException {
		outputStream = socket.getOutputStream();
		protocol = request.getProtocol();
		responseHeaderMap = new HashMap<>();
		setStatueCode("200");
	}
	public void flush() throws IOException {
		sendStatueLine();
		sendResponseHeader();
		sendResponseBody();
	}
	/**
	 * 发送响应体
	 * @throws IOException 
	 */
	private void sendResponseBody() throws IOException {
		BufferedOutputStream bos =
				new BufferedOutputStream(outputStream);
		int len;
		if (data!=null) {
			len = data.length;
			bos.write(data, 0, len);
		}else if(entity!=null){
			BufferedInputStream is =
					new BufferedInputStream(new FileInputStream(entity));
			byte[] bs = new byte[8*1024];
			while((len=is.read(bs))!=-1){
				bos.write(bs,0,len);
			}
			is.close();
		}
		bos.close();
	}
	/**
	 * 发送相应头
	 * @throws IOException 
	 */
	private void sendResponseHeader() throws IOException {
			Set<Entry<String,String>> set = responseHeaderMap.entrySet();
			String line ;
			for (Entry<String, String> entry : set) {
				line = entry.getKey()+": "+entry.getValue();
				sendLine(line);
			}
			sendLine("");
	}
	/**
	 * 发送状态行
	 * @throws IOException 
	 */
	private void sendStatueLine() throws IOException {
		String line = protocol+" "+statueCode+" "+statueDescribe;
		sendLine(line);
	}
	/**
	 * 发送一行
	 * @param line
	 * @throws IOException
	 */
	private void sendLine(String line) throws IOException{
		outputStream.write(line.getBytes("iso-8859-1"));
		outputStream.write(CR);
		outputStream.write(LF);
	}
	public void sendRedirect(String url) {
		setStatueCode("302");
		setResponseHeader("Location", url);
	}
	public void setStatueCode(String statueCode) {
		this.statueCode = statueCode;
		statueDescribe = HttpUtil.getStatueDescribe(statueCode);
	}
	public void setResponseHeader(String key, String value) {
		responseHeaderMap.put(key, value);
	}
	public void setEntity(File entity) {
		this.entity = entity;
		String fileName = entity.getName();
		String extension = fileName.substring(fileName.lastIndexOf('.')+1);
		String mimeType = HttpUtil.getMimeType(extension);
		setContentType(mimeType);
		setContentLength(entity.length());
	}
	public void setData(byte[] data) {
		this.data = data;
		setContentLength((long) data.length);
	}
	public void setContentLength(Long length) {
		setResponseHeader("Content-Length", length+"");
	}
	public void setContentType(String mimeType) {
		setResponseHeader("Content-Type", mimeType);
	}
}
