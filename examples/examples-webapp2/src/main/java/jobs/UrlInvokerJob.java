package jobs;

import gambler.examples.webapp2.exception.UnexpectedException;
import gambler.examples.webapp2.util.HttpClientPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class UrlInvokerJob extends QuartzJobBean {

	private final Logger log = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		JobDetail jobDetail = jobexecutioncontext.getJobDetail();
		log.info("execute job(" + jobDetail.getFullName() + ") start... @"
				+ new Date());
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		String method = jobDataMap.getString("method");
		String url = jobDataMap.getString("url");
		String expect = jobDataMap.getString("expect");
		String actualReturn = null;
		try {
			if ("GET".equalsIgnoreCase(method)) {
				actualReturn = HttpClientPool.getInstance().getMethod(url);
			} else if ("POST".equalsIgnoreCase(method)) {
				String paramsMap = jobDataMap.getString("params");
				if (StringUtils.isBlank(paramsMap)) {
					paramsMap = "{}";
				}
				JSONObject params = JSONObject.fromObject(paramsMap);
				Set<String> keys = params.keySet();
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (String key : keys) {
					nvps.add(new BasicNameValuePair(key, params.getString(key)));
				}
				actualReturn = HttpClientPool.getInstance().postMethod(url,
						nvps);
			}
			jobexecutioncontext.setResult(actualReturn);
			if (StringUtils.isNotBlank(expect) && !expect.equals(actualReturn)) {
				throw new UnexpectedException(String.format(
						"expect return: %s, actual return: %s", expect,
						actualReturn));
			}

			log.info(String.format("execute job(%s) successfully, return=%s!",
					jobDetail.getFullName(), actualReturn));
		} catch (Exception e) {
			throw new JobExecutionException(String.format(
					"execute job(%s,%s) fail!", jobDetail.getGroup(),
					jobDetail.getName()), e);
		}
		log.info("execute job(" + jobDetail.getFullName() + ") end! @"
				+ new Date());
	}

}
