package http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpServletRequest implements HttpServlet{

	private InputStream inputStream;
	private String requestMethod;
	private String url;
	private String uri;
	private String protocol;
	private Map<String, String> requestHeaderMap;
	private Map<String, String> userInfoMap;
	
	public HttpServletRequest(Socket socket) throws IOException {
		inputStream = socket.getInputStream();
		requestHeaderMap = new HashMap<>();
		userInfoMap = new HashMap<>();
		parseAll();
	}
	/**
	 * 处理所有的请求信息
	 * @throws IOException 
	 */
	private void parseAll() throws IOException {
		parseRequestLine();
		parseMessageHeader();
		parseMessageBody();
	}
	/**
	 * 处理报文
	 * @throws IOException 
	 */
	private void parseMessageBody() throws IOException {
		String length = requestHeaderMap.get("Content-Length");
		if (length != null) {
			int len = Integer.parseInt(length);
			byte[] bs = new byte[len];
			inputStream.read(bs, 0, len);
			parseUserInfo(URLDecoder.decode(new String(bs),"utf-8"));
		}
	}
	/**
	 * 处理报头
	 * @throws IOException 
	 */
	private void parseMessageHeader() throws IOException {
		String line;
		while(true){
			line = readLine();
			if ("".equals(line)) {
				break;
			}
			String[] split = line.split(":\\s");
			if (split.length==2) {
				requestHeaderMap.put(split[0], split[1]);
			}
		}
	}
	/**
	 * 处理请求行
	 * @throws IOException 
	 */
	private void parseRequestLine() throws IOException {
		String line = readLine();
		String[] split = line.split("\\s");
		requestMethod = split[0];
		url = split[1];
		parseurl();
		protocol = split[2];
	}
	/**
	 * 进一步解析url
	 */
	private void parseurl() {
		if (url.contains("?")) {
			String[] split = url.split("\\?");
			uri = split[0];
			String userInfo = split[1];
			parseUserInfo(userInfo);
			return;
		}
		uri = url;
	}
	/**
	 * 解析用户提交的信息
	 * @param userInfo
	 */
	private void parseUserInfo(String userInfo) {
		String[] split = userInfo.split("&");
		for (String string : split) {
			String[] ss = string.split("=");
			String key = ss[0];
			String value = ss.length==2 ? ss[1] : "";
			userInfoMap.put(key, value);
		}
	}
	/**
	 * 读取一行信息
	 * @return
	 * @throws IOException
	 */
	private String readLine() throws IOException {
		StringBuffer buffer = new StringBuffer();
		int c1 = 0;
		int c2 = 0;
		while(true){
			c1 = inputStream.read();
			if (c2==CR && c1==LF) {
				break;
			}
			buffer.append((char)c1);
			c2 = c1;
		}
		return URLDecoder.decode(buffer.toString().trim(), "utf-8");
	}
	
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getRequestHeaderValue(String key) {
		return requestHeaderMap.get(key);
	}
	public Set<String> getRequestHeaders() {
		return requestHeaderMap.keySet();
	}
	public String getUri() {
		return uri;
	}
	public String getUserInfo(String key) {
		return userInfoMap.get(key);
	}
	public Set<String> getUserInfoKeys() {
		return userInfoMap.keySet();
	}
}
