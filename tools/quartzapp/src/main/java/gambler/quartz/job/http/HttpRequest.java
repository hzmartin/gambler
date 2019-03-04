package gambler.quartz.job.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import gambler.quartz.utils.HttpClientPool;

public class HttpRequest {

	private String scheme;// https or http

	private String host;

	private int port = 80;

	private String method;

	private String contextPath;

	private String queryString;

	private String body;

	private List<Header> headers = new ArrayList<Header>();

	private HttpClientPool httpClient = HttpClientPool.getInstance();

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String invoke() throws Exception {
		if (method.equalsIgnoreCase("get")) {
			return httpGet();
		}
		if (method.equalsIgnoreCase("post")) {
			return httpPost();
		} else {
			throw new UnsupportedOperationException("unsupported http method");
		}
	}

	@Override
	public String toString() {
		return "HttpRequest [scheme=" + scheme + ", host=" + host + ", port=" + port + ", method=" + method
				+ ", contextPath=" + contextPath + ", queryString=" + queryString + ", body=" + body + "]";
	}

	private String httpPost() throws Exception {

		String url = scheme + "://" + host + ":" + port + contextPath;

		if (StringUtils.isBlank(body)) {
			Map<String, String> params = new HashMap<String, String>();
			if (StringUtils.isNotBlank(queryString)) {
				String[] paramStrs = queryString.trim().split("&");
				for (String param : paramStrs) {
					String[] p = param.split("=");
					if (p.length == 2) {
						params.put(p[0], p[1]);
					}
				}
			}
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				nvps.add(new BasicNameValuePair(key, params.get(key)));
			}
			return httpClient.postMethod(url, nvps);
		} else {
			return httpClient.postMethod(url, body, ContentType.APPLICATION_JSON, headers.toArray(new Header[0]));
		}
	}

	private String httpGet() throws Exception {
		StringBuilder params = new StringBuilder();
		if (StringUtils.isNotBlank(queryString)) {
			String[] paramStrs = queryString.trim().split("&");
			for (String param : paramStrs) {
				String[] p = param.split("=");
				if (p.length == 2) {
					params.append(p[0] + "=" + p[1] + "&");
				}
			}
		}
		String url = scheme + "://" + host + ":" + port + contextPath;
		if (StringUtils.isNotBlank(params.toString())) {
			String finalParamStr = params.toString();
			if (params.toString().endsWith("&")) {
				finalParamStr = params.substring(0, params.length() - 1);
			}
			url = url + "?" + finalParamStr;
		}
		return httpClient.getMethod(url);

	}

	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}

	public HttpClientPool getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClientPool httpClient) {
		this.httpClient = httpClient;
	}

}
