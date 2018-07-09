package org.scoreboard.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.chinatelecom.yiliao.common.framework.SysConfig;

public class GlobalHttpSessionFilter extends HttpServlet implements Filter {
	private static final Logger logger = Logger
			.getLogger(GlobalHttpSessionFilter.class);

	private static final long serialVersionUID = -365105405910803550L;

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		logger.debug("global session enabled: " + isGlobalSessionEnabled());
		if (isGlobalSessionEnabled()) {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			String sid = request.getSession().getId();
			filterChain.doFilter(new HttpServletRequestWrapper(sid, request),
					servletResponse);
		} else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public boolean isGlobalSessionEnabled() {
		return SysConfig.getBoolean("enableGlobalSession", true);
	}

}
