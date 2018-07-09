package org.scoreboard.session;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.scoreboard.utils.SpringContextHolder;

import com.chinatelecom.yiliao.common.framework.SysConfig;
import com.chinatelecom.yiliao.device.DeviceEnv;
import com.chinatelecom.yiliao.device.redis.RedisDevice;
import com.chinatelecom.yiliao.device.redis.RedisNodeType;

/**
 * 1. 使用前请根据项目需要实现为支持函数，可以采用memcache，redis等服务实现全局session<br/>
 * 2. 配置GlobalSessionFilter
 */
@SuppressWarnings("deprecation")
public class GlobalHttpSession implements HttpSession {

	private static final Logger logger = Logger
			.getLogger(GlobalHttpSession.class);

	private static final int MAX_SESSION_TIMEOUT = 60 * 60;// one hour

	private HttpSession session;

	private RedisDevice redis;

	private String sid;

	public GlobalHttpSession(String sid, HttpSession session) {
		this.sid = sid;
		this.session = session;
		DeviceEnv deviceEnv = SpringContextHolder.getBean("deviceEnv");
		redis = deviceEnv.getRedis();
	}

	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}

	@Override
	public String getId() {
		return StringUtils.isBlank(sid) ? session.getId() : sid;
	}

	@Override
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}

	@Override
	public ServletContext getServletContext() {
		return session.getServletContext();
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
	}

	@Override
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}

	@Override
	public Object getAttribute(String name) {
		String val = redis.get(RedisNodeType.ACTIVITY,
				SysConfig.getString("AppName") + ".SESSION_REDIS",
				getGlobalAttributeName(name));
		logger.debug("get attribute " + getGlobalAttributeName(name)
				+ ", value=[" + val + "]");
		return val;
	}

	private String getGlobalAttributeName(String name) {
		return getId() + "." + name;
	}

	@Override
	public Object getValue(String name) {
		return getAttribute(name);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getAttributeNames() {
		throw new UnsupportedOperationException("不支持该操作");
	}

	@Override
	public String[] getValueNames() {
		throw new UnsupportedOperationException("不支持该操作");
	}

	@Override
	public void setAttribute(String name, Object value) {
		logger.debug("set attribute " + getGlobalAttributeName(name)
				+ " of value[" + value + "], expire in seconds: "
				+ MAX_SESSION_TIMEOUT);
		redis.set(RedisNodeType.ACTIVITY, SysConfig.getString("AppName")
				+ ".SESSION_REDIS", getGlobalAttributeName(name), value);
		redis.expire(RedisNodeType.ACTIVITY,
				SysConfig.getString("AppName") + ".SESSION_REDIS",
				getGlobalAttributeName(name), MAX_SESSION_TIMEOUT);
	}

	@Override
	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		redis.del(RedisNodeType.ACTIVITY, SysConfig.getString("AppName")
				+ ".SESSION_REDIS", getGlobalAttributeName(name));
		redis.expire(RedisNodeType.ACTIVITY,
				SysConfig.getString("AppName") + ".SESSION_REDIS",
				getGlobalAttributeName(name), 0);
	}

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}

	@Override
	public void invalidate() {
		throw new UnsupportedOperationException("不支持该操作");
	}

	@Override
	public boolean isNew() {
		return session.isNew();
	}

}
