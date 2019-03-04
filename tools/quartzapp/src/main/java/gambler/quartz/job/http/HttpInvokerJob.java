package gambler.quartz.job.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import gambler.quartz.job.AbstractQuartzJob;

/**
 * 
 * @author hzwangqh
 * 
 */
public class HttpInvokerJob extends AbstractQuartzJob {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		JSONArray hosts = JSON.parseArray(jobDataMap.getString("hosts"));
		JSONArray results = new JSONArray();
		HttpRequest httpRequest = constructHttpRequest(jobDataMap);
		HttpResponseAssert httpAssert = constructHttpResponseAssert(jobDataMap);
		for (int i = 0; i < hosts.size(); i++) {
			try {
				httpRequest.setHost(hosts.getString(i));
				String actual = httpRequest.invoke();
				results.add(actual);
				if (httpAssert != null) {
					httpAssert.setActual(actual);
					boolean success = httpAssert.assertResponse();
					if (!success) {
						log.error("HTTP INVOKE ERROR: "
								+ String.format("request: (%s), assert: (%s)", httpRequest, httpAssert));
					} else {
						log.info(String.format("job(%s) assert successfully!", jobDetail.getKey().toString()));
					}
				}
			} catch (Exception e) {
				results.add("ERROR: " + e.getMessage());
				log.error(e.getMessage(), e);
			}
		}

		context.setResult(results);
	}

	private HttpRequest constructHttpRequest(JobDataMap jobDataMap) {

		String method = jobDataMap.getString("method");
		String scheme = jobDataMap.getString("scheme");
		int port = jobDataMap.getInt("port");
		String contextPath = jobDataMap.getString("contextPath");
		String queryString = jobDataMap.getString("queryString");
		String body = jobDataMap.getString("body");
		JSONObject headersObj = JSON.parseObject(jobDataMap.getString("headers"));

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setMethod(method);
		httpRequest.setScheme(scheme);
		httpRequest.setQueryString(queryString);
		httpRequest.setPort(port);
		httpRequest.setContextPath(contextPath);
		httpRequest.setBody(body);
		if (headersObj != null) {
			List<Header> headers = new ArrayList<Header>();
			Set<Entry<String, Object>> entrySet = headersObj.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				headers.add(new BasicHeader(entry.getKey(), entry.getValue().toString()));
			}
			httpRequest.setHeaders(headers);
		}
		return httpRequest;
	}

	private HttpResponseAssert constructHttpResponseAssert(JobDataMap jobDataMap) {
		JSONObject assert1 = JSON.parseObject(jobDataMap.getString("assert"));
		if (assert1 == null) {
			return null;
		}
		String assertType = assert1.getString("type");// text or json
		// text required
		String assertExpect = assert1.getString("expect");

		// json required
		JSONArray assertFields = assert1.getJSONArray("fields");
		JSONArray assertExpects = assert1.getJSONArray("expects");
		HttpResponseAssert httpAssert = new HttpResponseAssert();
		httpAssert.setType(assertType);
		httpAssert.setExpect(assertExpect);
		if (assertFields != null) {
			httpAssert.setFields(assertFields.toArray(new String[0]));
		}
		if (assertExpects != null) {
			httpAssert.setExpects(assertExpects.toArray(new String[0]));
		}
		return httpAssert;
	}

}
