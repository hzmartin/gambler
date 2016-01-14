package gambler.examples.scheduler.session;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.lang.StringUtils;

/**
 * 1. 使用前请根据项目需要实现为支持函数，可以采用memcache，redis等服务实现全局session<br/>
 * 2. 配置GlobalSessionFilter
 */
public class GlobalHttpSession implements HttpSession {

	private HttpSession session;
	private String sid;

	public GlobalHttpSession(String sid, HttpSession session) {
		this.sid = sid;
		this.session = session;
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
		throw new UnsupportedOperationException("不支持该操作");
	}

	@Override
	public Object getValue(String name) {
		return getAttribute(name);
	}

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
		throw new UnsupportedOperationException("不支持该操作");
	}

	@Override
	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		throw new UnsupportedOperationException("不支持该操作");
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
