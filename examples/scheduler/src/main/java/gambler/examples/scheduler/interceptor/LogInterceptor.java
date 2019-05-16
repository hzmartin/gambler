package gambler.examples.scheduler.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import gambler.examples.scheduler.util.IPUtil;
import gambler.examples.scheduler.util.LogBean;

public class LogInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		LogBean.start();
		LogBean logBean = LogBean.get();
		logBean.setPath(request.getRequestURI());
		logBean.setIp(IPUtil.getRequestIp(request));
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		LogBean.end();
	}
}
