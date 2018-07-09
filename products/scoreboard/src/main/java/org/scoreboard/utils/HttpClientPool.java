package org.scoreboard.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientPool {

	public static int httpTimeoutThreshold = 20000; // 访问外部http连接的超时值

	private static Logger logger = Logger.getLogger(HttpClientPool.class);

	private static class HttpClientPoolHolder {
		static final HttpClientPool INSTANCE = new HttpClientPool();
	}

	private CloseableHttpClient client;

	MessageConstraints messageConstraints = MessageConstraints.custom()
			.setMaxHeaderCount(200).setMaxLineLength(2000).build();

	// connection config
	ConnectionConfig connectionConfig = ConnectionConfig.custom()
			.setMalformedInputAction(CodingErrorAction.IGNORE)
			.setUnmappableInputAction(CodingErrorAction.IGNORE)
			.setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints)
			.build();

	// request config
	RequestConfig defaultRequestConfig = RequestConfig.custom()
			.setSocketTimeout(httpTimeoutThreshold)
			.setConnectTimeout(httpTimeoutThreshold)
			.setConnectionRequestTimeout(httpTimeoutThreshold).build();

	private HttpClientPool() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setDefaultMaxPerRoute(50);
		cm.setMaxTotal(200);

		cm.setDefaultConnectionConfig(connectionConfig);

		client = HttpClients.custom().setConnectionManager(cm)
				.setDefaultRequestConfig(defaultRequestConfig).build();

	}

	static public HttpClientPool getInstance() {
		return HttpClientPoolHolder.INSTANCE;
	}

	public String getMethod(String url) throws ClientProtocolException,
			IOException {
		HttpGet get = new HttpGet(url);

		try {

			HttpResponse response = client.execute(get);
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, "utf-8");
			}

			throw new IOException("服务器HTTP状态码\t- " + sl.getStatusCode());
		} finally {
			get.releaseConnection();
		}

	}

	public String getMethod(String url, int timeout)
			throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig
					.copy(defaultRequestConfig).setSocketTimeout(timeout)
					.build();
			get.setConfig(requestConfig);
			HttpResponse response = client.execute(get);
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, "utf-8");
			}

			return null;

		} finally {
			get.releaseConnection();
		}
	}

	/**
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String postMethod(String url, List<NameValuePair> nvps)
			throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		try {

			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = client.execute(post);
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, "utf-8");
			} else {
				logger.error(String.format(
						"req url failed, url: %s ,retcode: %s", url,
						sl.getStatusCode()));
			}

			return null;
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String postMethod(String url, List<NameValuePair> nvps, int timeout)
			throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig
					.copy(defaultRequestConfig).setSocketTimeout(timeout)
					.build();
			post.setConfig(requestConfig);

			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = client.execute(post);
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, "utf-8");
			} else {
				logger.error(String.format(
						"req url failed, url: %s ,retcode: %s", url,
						sl.getStatusCode()));
			}
			return null;
		} finally {
			post.releaseConnection();
		}
	}

	public String postMethod(String uri, String body, ContentType mimetype)
			throws ClientProtocolException, IOException {

		HttpPost post = new HttpPost(uri);
		try {
			post.setEntity(new StringEntity(body, mimetype));
			HttpResponse response = client.execute(post);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			} else {
				logger.warn(String.format(
						"req url failed, url: %s ,retcode: %d", uri,
						sl.getStatusCode()));
			}

			return null;
		} finally {
			post.releaseConnection();
		}
	}

	public String postMultiFormDataMethod(String uri, String name, File file) throws ClientProtocolException,
			IOException {

		HttpPost post = new HttpPost(uri);
		try {
			FileBody fileBody = new FileBody(file);
			MultipartEntityBuilder multipartEntity = MultipartEntityBuilder
					.create();
			multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntity.addPart(name, fileBody);

			post.setEntity(multipartEntity.build());
			HttpResponse response = client.execute(post);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			} else {
				logger.warn(String.format(
						"req url failed, url: %s ,retcode: %d", uri,
						sl.getStatusCode()));
			}

			return null;
		} finally {
			post.releaseConnection();
		}
	}
}
