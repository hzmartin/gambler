package org.scoreboard.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class HttpRequestUtils {

	private static final Logger logger = Logger
			.getLogger(HttpRequestUtils.class);

	public static <T> T getJsonResultByPostMethod(String url,
			Map<String, String> params, Class<T> retClass)
			throws ClientProtocolException, IOException {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<Entry<String, String>> entrySet = params.entrySet();
		for (Entry<String, String> entry : entrySet) {
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		String resultJson = HttpClientPool.getInstance().postMethod(url, nvps);
		logger.info("请求url " + url + " 得到回应：" + resultJson);
		T t = JSONObject.parseObject(resultJson, retClass);
		return t;
	}

	public static <T> T getJsonResultByGetMethod(String url, Class<T> retClass)
			throws ClientProtocolException, IOException {
		String resultJson = HttpClientPool.getInstance().getMethod(url);
		logger.info("请求url " + url + " 得到回应：" + resultJson);
		T t = JSONObject.parseObject(resultJson, retClass);
		return t;
	}

}
