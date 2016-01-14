package gambler.examples.scheduler.util;

import gambler.commons.advmap.XMLMap;
import gambler.examples.scheduler.exception.UnexpectedException;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientPool {

	private static final int httpTimeoutThreshold = 20000; // 访问外部http连接的超时值

	private static Logger logger = Logger.getLogger(HttpClientPool.class);

	private static final XMLMap sysconf = SpringContextHolder
			.getBean("sysconf");

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
		cm.setDefaultMaxPerRoute(sysconf.getInteger(
				"httpPoolDefaultMaxPerRoute", 50));
		cm.setMaxTotal(sysconf.getInteger("httpPoolMaxTotal", 200));

		cm.setDefaultConnectionConfig(connectionConfig);

		client = HttpClients.custom().setConnectionManager(cm)
				.setDefaultRequestConfig(defaultRequestConfig).build();
		if (sysconf.getBoolean("enableIdleConnectionMonitor", false)) {
			new IdleConnectionMonitorThread(cm).start();
			logger.info("idle connection monitor started!");
		}
	}

	static public HttpClientPool getInstance() {
		return HttpClientPoolHolder.INSTANCE;
	}

	public String getMethod(String url) throws ClientProtocolException,
			IOException, UnexpectedException {
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			}

			throw new UnexpectedException(String.format(
					"request %s fail, status_code=%d, content=%s",
					get.getURI(), sl.getStatusCode(), content));
		} finally {
			get.releaseConnection();
		}

	}

	public String getMethod(String url, int timeoutInMills)
			throws ClientProtocolException, IOException, UnexpectedException {
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig
					.copy(defaultRequestConfig)
					.setSocketTimeout(timeoutInMills).build();
			get.setConfig(requestConfig);
			HttpResponse response = client.execute(get);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			}

			throw new UnexpectedException(String.format(
					"request %s fail, status_code=%d, content=%s",
					get.getURI(), sl.getStatusCode(), content));

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
	 * @throws UnexpectedException
	 */
	public String postMethod(String url, List<NameValuePair> nvps)
			throws ClientProtocolException, IOException, UnexpectedException {
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = client.execute(post);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			}

			throw new UnexpectedException(String.format(
					"request %s fail, status_code=%d, content=%s",
					post.getURI(), sl.getStatusCode(), content));
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
	 * @throws UnexpectedException
	 */
	public String postMethod(String url, List<NameValuePair> nvps,
			int timeoutInMills) throws ClientProtocolException, IOException,
			UnexpectedException {
		HttpPost post = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig
					.copy(defaultRequestConfig)
					.setSocketTimeout(timeoutInMills).build();
			post.setConfig(requestConfig);

			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = client.execute(post);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			}
			throw new UnexpectedException(String.format(
					"request %s fail, status_code=%d, content=%s",
					post.getURI(), sl.getStatusCode(), content));
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * @param uri
	 * @param body
	 * @param mimetype
	 *            - for example: ContentType.TEXT_PLAIN
	 * @return
	 * @throws UnexpectedException
	 */
	public String postMethod(String uri, String body, ContentType mimetype)
			throws ClientProtocolException, IOException, UnexpectedException {

		HttpPost post = new HttpPost(uri);
		try {
			post.setEntity(new StringEntity(body, mimetype));
			HttpResponse response = client.execute(post);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			}

			throw new UnexpectedException(String.format(
					"request %s fail, status_code=%d, content=%s",
					post.getURI(), sl.getStatusCode(), content));
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * 定期轮询关闭无用连接
	 */
	public static class IdleConnectionMonitorThread extends Thread {

		private final HttpClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
			super();
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(4500);
						// Close expired connections
						connMgr.closeExpiredConnections();
						// wait time + close idle time < nginx close time(15s)
						connMgr.closeIdleConnections(sysconf.getInteger(
								"httpConnectionIdleTimeInSeconds", 10),
								TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException ex) {
				// terminate
			}
		}

		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}

	}
}
