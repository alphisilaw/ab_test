package com.web.abt.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.web.abt.util.CookieUtils;
import com.web.abt.util.HttpsclientUtil;

public class ProxyService {
	
	private static ProxyService proxyService = new ProxyService();
	
	private static final Map<String, String> htmlMap = new HashMap<String, String>();
	
	private ProxyService(){}
	
	public static ProxyService newInstance() {
		return proxyService;
	}

	/**
	 * 手机预览
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void previewVersionHtml(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String htmlId = request.getParameter("htmlId");
		String html = htmlMap.get(htmlId);
		response.setContentType("text/html;charset=utf-8");
		if (html != null) {
			response.getWriter().write(html);
		} else 
			response.getWriter().write("<html style='font-size:50px'>请点击预览重新生成二维码</html>");
	}
	
	/**
	 * 生成预览
	 * @param request
	 * @param response
	 */
	public void saveVersionHtml(HttpServletRequest request, HttpServletResponse response) {
		String isMobile = request.getParameter("isMobile");
		String url = request.getParameter("url");
		List<Header> requestHeaders = getRequestHeaders(isMobile);
		CloseableHttpClient httpClient = HttpsclientUtil.getHttpsClient(requestHeaders);
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse clientResponse = httpClient.execute(get);
			
			setResponseHeaders(response, clientResponse);
			
			byte[] result = EntityUtils.toByteArray(clientResponse.getEntity());
			
			String preivewUrl = replaceBody(request, response, result);

			response.setContentType("text/plain;charset=utf-8");
			response.getWriter().write(preivewUrl);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String replaceBody(HttpServletRequest request, HttpServletResponse response, byte[] content) throws UnsupportedEncodingException {

		String uuid = UUID.randomUUID().toString();
		String host = getHost(request);
		String preivewUrl = host + "/abt_spier/previewVersionHtml?htmlId=" + uuid;
		
		String formated = getReplaceBody(request, response, content);
		setToMap(uuid, formated);
		return preivewUrl;
	}

	private String getReplaceBody(HttpServletRequest request,
			HttpServletResponse response, byte[] content)
			throws UnsupportedEncodingException {
		String contentType = response.getContentType();
		String[] temp = null;
		String formated = new String(content, (contentType!=null&&(temp=contentType.split("=")).length==2)?temp[1]:"utf-8");
		Document doc = Jsoup.parse(formated);
		Elements body = doc.select("body");
		
		String newBodyStr = request.getParameter("str");
		if (newBodyStr != null) {
			newBodyStr = URLDecoder.decode(newBodyStr, "utf-8");
		}
		body.after(newBodyStr);
		body.remove();
		
		formated = doc.toString();
		return formated;
	}

	private void setToMap(final String uuid, String formated) {
		htmlMap.put(uuid, formated);
		new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
         	   htmlMap.remove(uuid);    
            } 
         }, 1000*60*5);
	}
	
	/**
	 * 自定义代理
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	public void writeProxy(HttpServletRequest request, HttpServletResponse response) 
			throws UnsupportedEncodingException {

		String spiderdomain = CookieUtils.getCookie(request, "spiderdomain");
		if (StringUtils.isEmpty(spiderdomain)) {
			return;
		}
		String url = request.getParameter("url");
		if (StringUtils.isNotEmpty(url)) {
			url = URLDecoder.decode(url, "utf-8");
		}
		String isMobile = CookieUtils.getCookie(request, "isMobile");
		write(request, response, url, isMobile);
	}
	/**
	 * 代理请求
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	public void write(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		String uri = StringUtils.substringAfter(request.getRequestURI(), request.getContextPath());
		String spiderdomain = CookieUtils.getCookie(request, "spiderdomain");
		if (StringUtils.isEmpty(spiderdomain)) {
			return;
		}
		String url = request.getParameter("url");
		if (StringUtils.isNotEmpty(url)) {
			url = URLDecoder.decode(url, "utf-8");
		}
		String domain = URLDecoder.decode(spiderdomain, "utf-8");
		String queryString = request.getQueryString();
		String forwardUrl = domain + uri + (StringUtils.isBlank(queryString) ? "" : "?" + queryString);
		String isMobile = CookieUtils.getCookie(request, "isMobile");
		write(request, response, forwardUrl, isMobile);
		
	}
	
	public void write(HttpServletRequest request, HttpServletResponse response, String url, String isMobile) {
		List<Header> requestHeaders = getRequestHeaders(request, isMobile);
		CloseableHttpClient httpClient = HttpsclientUtil.getHttpsClient(requestHeaders);
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse clientResponse = httpClient.execute(get);
			
			setResponseHeaders(response, clientResponse);
			
			byte[] result = EntityUtils.toByteArray(clientResponse.getEntity());
			
			result = formatHtml(request, response, result);
			
			response.getOutputStream().write(result);;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private byte[] formatHtml(HttpServletRequest request, HttpServletResponse response, byte[] content) throws UnsupportedEncodingException {
		String contentType = response.getContentType();
		String charset = response.getCharacterEncoding();
		if (contentType != null && contentType.contains("text/html")) {
			String formated = new String(content, charset);
			String host = getHost(request);
			Document doc = Jsoup.parse(formated);
			Elements links = doc.select("link[href]");
			for (Element link : links) {
				String href = StringUtils.trim(link.attr("href"));
				
				if (!(href.startsWith("http") || href.startsWith("//"))) {
					continue;
				}
				
				href = new StringBuilder()
					.append(host)
					.append(request.getContextPath())
					.append("/proxySource?url=")
					.append(URLEncoder.encode(href, "utf-8"))
					.toString();
				
				link.attr("href", href);
			}
			
			formated = doc.toString();
			response.setCharacterEncoding("utf-8");
			return formated.getBytes("utf-8");
		} else {
			return content;
		}
	}

	private String getHost(HttpServletRequest request) {
		String host = request.getServerName();
		host = "http://" + host;
		String Port = String.valueOf(request.getServerPort());
		if (!Port.equals("80")) {
			host = host + ":" + Port;
		}
		return host;
	}

	private List<Header> getRequestHeaders(HttpServletRequest request, String isMobile) {
		List<Header> requestHeaders = new ArrayList<Header>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = request.getHeader(name);
			if ("host".equalsIgnoreCase(name)) {
				continue;
			}
			Header header = null;
			if ("User-Agent".equalsIgnoreCase(name)) {
				String userAgent = "";
				if ("1".equals(isMobile)) {
					userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A403 Safari/8536.25";
				} else {
					userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2351.3 Safari/537.36";
				}
				header = new BasicHeader(name, userAgent);
			} else {
				header = new BasicHeader(name, value);
			}
			requestHeaders.add(header);
		}
		return requestHeaders;
	}
	
	private List<Header> getRequestHeaders(String isMobile) {
		List<Header> requestHeaders = new ArrayList<Header>();
		String userAgent = "";
		if ("1".equals(isMobile)) {
			userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A403 Safari/8536.25";
		} else {
			userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2351.3 Safari/537.36";
		}
		Header header = new BasicHeader("User-Agent", userAgent);
		requestHeaders.add(header);
		return requestHeaders;
	}

	private void setResponseHeaders(HttpServletResponse response, HttpResponse clientResponse) {
		Header[] responseHeaders = clientResponse.getAllHeaders();
		if (responseHeaders != null) {
			for (Header header : responseHeaders) {
				String name = header.getName();
				String value = header.getValue();
				if ("Content-Type".equalsIgnoreCase(name) && value != null) {
					response.setHeader(name, value);
				}
			}
		}
	}
}
