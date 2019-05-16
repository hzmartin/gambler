package gambler.examples.scheduler.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import gambler.examples.scheduler.resp.ResponseStatus;

public class LogBean {

	private static Logger logger = Logger.getLogger(LogBean.class);

	private String path = "";

	private String uid = "";

	private String code = ResponseStatus.OK.getCode();

	private long spend = 0;

	private long start = 0;

	private Object headers;

	private Object params;

	private Object payload;

	private Map<String, Object> props = new LinkedHashMap<String, Object>();

	private Object result;

	private Object error;

	private String ip = "";

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getSpend() {
		return spend;
	}

	public void setSpend(long spend) {
		this.spend = spend;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public Object getHeaders() {
		return headers;
	}

	public void setHeaders(Object headers) {
		this.headers = headers;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public Map<String, Object> getProps() {
		return props;
	}

	public void setProps(Map<String, Object> props) {
		this.props = props;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object getError() {
		return error;
	}

	public void setError(Object error) {
		this.error = error;
	}

	public LogBean addProp(String key, Object value) {
		if (this.getProps() != null) {
			this.getProps().put(key, (value == null ? "null" : value));
		}
		return this;
	}

	public Object delProp(String key) {
		if (this.getProps() != null) {
			return this.getProps().remove(key);
		}
		return null;
	}

	public Object getProp(String key) {
		if (this.getProps() != null) {
			return this.getProps().get(key);
		}
		return null;
	}

	public void log() {
		logger.info(JSON.toJSONString(this));
	}

	public void print() {
		this.print(System.currentTimeMillis());
	}

	public void print(long now) {
		this.setSpend(now - this.getStart());
		this.log();
	}

	private static final ThreadLocal<LogBean> LOG_BEAN_THREAD_LOCAL = new ThreadLocal<LogBean>();

	public static LogBean get()
	{
		LogBean logBean = LOG_BEAN_THREAD_LOCAL.get();
		if (logBean == null)
		{
			logBean = new LogBean();
			LOG_BEAN_THREAD_LOCAL.set(logBean);
		}
		return logBean;
	}

	public static void remove() {
		LOG_BEAN_THREAD_LOCAL.remove();
	}

	public static LogBean start() {
		LogBean logBean = get();
		logBean.setStart(System.currentTimeMillis());
		return logBean;
	}

	public static void end() {
		get().print();
		remove();
	}
}
