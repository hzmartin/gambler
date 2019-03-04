package gambler.quartz.utils;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.rmi.UnexpectedException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@SuppressWarnings("deprecation")
@Component
public class HttpClientPool {

	private static final int httpTimeoutThreshold = 20000; // 访问外部http连接的超时值

	private static final Logger logger = LoggerFactory.getLogger(HttpClientPool.class);

	private static class HttpClientPoolHolder {
		static final HttpClientPool INSTANCE = new HttpClientPool();
	}

	private CloseableHttpClient client;

	MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000)
			.build();

	// connection config
	ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
			.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
			.setMessageConstraints(messageConstraints).build();

	// request config
	RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(httpTimeoutThreshold)
			.setConnectTimeout(httpTimeoutThreshold).setConnectionRequestTimeout(httpTimeoutThreshold).build();

	public HttpClientPool() {
		try {
			PoolingHttpClientConnectionManager cm = configConnectionManager(50, 200);
			client = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(defaultRequestConfig)
					.setRetryHandler(new SimpleHttpRequestRetryHandler()).build();
		} catch (Exception e) {
			logger.error("init httpclient error", e);
		}
	}

	static public HttpClientPool getInstance() {
		return HttpClientPoolHolder.INSTANCE;
	}

	private PoolingHttpClientConnectionManager configConnectionManager(int maxPerRoute, int maxTotal) throws Exception {
		SSLContextBuilder builder = SSLContexts.custom();
		builder.loadTrustMaterial(null, new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		});
		SSLContext sslContext = builder.build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
			@Override
			public void verify(String host, SSLSocket ssl) throws IOException {
			}

			@Override
			public void verify(String host, X509Certificate cert) throws SSLException {
			}

			@Override
			public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
			}

			@Override
			public boolean verify(String s, SSLSession sslSession) {
				return true;
			}
		});

		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", plainsf).register("https", sslsf).build();

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setDefaultMaxPerRoute(maxPerRoute);
		cm.setMaxTotal(maxTotal);
		cm.setDefaultConnectionConfig(connectionConfig);
		return cm;
	}

	public String getMethod(String url) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			}

			throw new UnexpectedException(String.format("request %s fail, status_code=%d, content=%s", get.getURI(),
					sl.getStatusCode(), content));
		} finally {
			get.releaseConnection();
		}

	}

	public String getMethod(String url, int timeoutInMills) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(timeoutInMills)
					.build();
			get.setConfig(requestConfig);
			HttpResponse response = client.execute(get);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			}

			throw new UnexpectedException(String.format("request %s fail, status_code=%d, content=%s", get.getURI(),
					sl.getStatusCode(), content));

		} finally {
			get.releaseConnection();
		}
	}

	/**
	 * @param url
	 * @param params
	 * @return
	 */
	public String postMethod(String url, List<NameValuePair> nvps) throws ClientProtocolException, IOException {
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

			throw new UnexpectedException(String.format("request %s fail, status_code=%d, content=%s", post.getURI(),
					sl.getStatusCode(), content));
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
	public String postMethod(String url, List<NameValuePair> nvps, int timeoutInMills)
			throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(timeoutInMills)
					.build();
			post.setConfig(requestConfig);

			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = client.execute(post);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			}
			throw new UnexpectedException(String.format("request %s fail, status_code=%d, content=%s", post.getURI(),
					sl.getStatusCode(), content));
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * @param uri
	 * @param body
	 * @param mimetype
	 *            - for example: ContentType.TEXT_PLAIN
	 * @param header
	 */
	public String postMethod(String uri, String body, ContentType mimetype, Header... headers)
			throws ClientProtocolException, IOException {

		HttpPost post = new HttpPost(uri);
		try {
			for (Header header : headers) {
				post.addHeader(header);
			}
			post.setEntity(new StringEntity(body, mimetype));
			HttpResponse response = client.execute(post);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				return content;
			}

			throw new UnexpectedException(String.format("request %s fail, status_code=%d, content=%s", post.getURI(),
					sl.getStatusCode(), content));
		} finally {
			post.releaseConnection();
		}
	}

	private class SimpleHttpRequestRetryHandler implements HttpRequestRetryHandler {

		@Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			if (executionCount > 3) {
				logger.warn("maximum tries reached for client http pool ");
				return false;
			}

			if (exception instanceof NoHttpResponseException // NoHttpResponseException 重试
					|| exception instanceof ConnectTimeoutException // 连接超时重试
			) {
				// exception instanceof SocketTimeoutException //响应超时不重试，避免造成业务数据不一致
				logger.warn(exception + " on " + executionCount + " call");
				return true;
			}
			return false;
		}

	}

	/**
	 * 定期轮询关闭无用连接
	 */
	@SuppressWarnings("unused")
	private class IdleConnectionMonitorThread extends Thread {

		private final HttpClientConnectionManager connMgr;
		private volatile boolean shutdown;
		private long waitMillis = 4500L;
		private int idleSeconds = 10;

		public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr, long waitMillis, int idleSeconds) {
			super();
			this.connMgr = connMgr;
			this.waitMillis = waitMillis;
			this.idleSeconds = idleSeconds;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(waitMillis);
						// Close expired connections
						connMgr.closeExpiredConnections();
						// wait time + close idle time < nginx close time(15s)
						connMgr.closeIdleConnections(idleSeconds, TimeUnit.SECONDS);
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
