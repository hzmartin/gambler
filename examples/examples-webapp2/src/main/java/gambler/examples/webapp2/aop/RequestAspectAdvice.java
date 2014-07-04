package gambler.examples.webapp2.aop;

import gambler.commons.advmap.XMLMap;
import gambler.examples.webapp2.annotation.AuthRequired;
import gambler.examples.webapp2.annotation.LogRequestParam;
import gambler.examples.webapp2.exception.ActionException;
import gambler.examples.webapp2.resp.ResponseStatus;
import gambler.examples.webapp2.resp.ServerResponse;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.vo.Account;
import gambler.examples.webapp2.vo.NaviItem;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 定义切面
 * 
 * @Aspect : 标记为切面类
 * @Pointcut : 指定匹配切点
 * @Before : 指定前置通知，value中指定切入点匹配
 * @AfterReturning ：后置通知，具有可以指定返回值
 * @AfterThrowing ：异常通知
 * 
 */
@Component
@Aspect
public class RequestAspectAdvice {
	private static final Logger logger = Logger.getLogger(RequestAspectAdvice.class);

	@Resource
	protected AuthUserService authUserService;

	@Autowired
	private XMLMap sysconf;

	@Around("within(@org.springframework.stereotype.Controller *)")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
		Method method = joinPointObject.getMethod();
		// =============start input param log build=====================
		StringBuilder execLogStr = new StringBuilder();
		execLogStr.append("Process request：  "
				+ pjp.getTarget().getClass().getName() + "#"
				+ pjp.getSignature().getName() + "(");
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		int argsLen = pjp.getArgs().length;
		if (parameterAnnotations.length == argsLen) {
			for (int paramIndex = 0; paramIndex < argsLen; paramIndex++) {
				Annotation[] annotations = parameterAnnotations[paramIndex];
				for (Annotation annotation : annotations) {
					if (annotation instanceof LogRequestParam) {
						LogRequestParam logParam = (LogRequestParam) annotation;
						Object theArg = pjp.getArgs()[paramIndex];
						String name = logParam.name();
						if (StringUtils.isBlank(name)) {
							name = "param[" + paramIndex + "/" + argsLen + "]";
						}
						execLogStr.append(name + "=" + theArg + ",");
					}
				}
			}
			int lastCommaIndex = execLogStr.lastIndexOf(",");
			if (lastCommaIndex != -1) {
				execLogStr.deleteCharAt(lastCommaIndex);
			}
		} else {
			logger.error(String.format(
					"Inconsistent argument length while invoke %s#%s!", pjp
							.getTarget().getClass().getName(), pjp
							.getSignature().getName()));
		}
		execLogStr.append(")");
		// =============end input param log build=====================

		if (!(pjp.getArgs()[0] instanceof HttpServletRequest)) {
			throw new IllegalArgumentException(
					"Illegal Argument, the 1st paramter of Method("
							+ pjp.getSignature().getName()
							+ ") must be HttpServletRequest");
		}
		HttpServletRequest request = (HttpServletRequest) pjp.getArgs()[0];
		String target = request.getRequestURI();
		ServerResponse serverResponse = new ServerResponse();
		AuthRequired authRequiredAnno = method
				.getAnnotation(AuthRequired.class);
		if (authRequiredAnno != null && sysconf.getBoolean("switch.enableAuthentication", Boolean.TRUE)) {
			boolean loginRequired = authRequiredAnno.loginRequired();
			long[] requiredPerms = authRequiredAnno.requiredPerms();
			boolean permRequired = requiredPerms != null
					&& requiredPerms.length > 0;
			if (loginRequired || permRequired) {
				// checklogin
				boolean hasLogined = authUserService.checkLogin(request);
				serverResponse.setResponseStatus(ResponseStatus.OK);
				if (!hasLogined) {
					serverResponse
							.setResponseStatus(ResponseStatus.USER_NOT_LOGGED);
				} else if (permRequired) {
					Account loginUser = authUserService.getLoginUser(request);
					// check userperm
					boolean hasPermission = authUserService
							.checkUserPermission(loginUser.getUserId(),
									requiredPerms);
					if (!hasPermission) {
						serverResponse
								.setResponseStatus(ResponseStatus.NO_PERMISSION);
					}
				}
				if (!ResponseStatus.OK.getCode().equals(
						serverResponse.getCode())) {
					if (method.isAnnotationPresent(ResponseBody.class)) {
						JSONObject object = JSONObject
								.fromObject(serverResponse);
						execLogStr.append(", result=" + object.toString());
						logger.info(execLogStr.toString());
						return serverResponse;
					} else {
						if (serverResponse.getCode().equals(
								ResponseStatus.USER_NOT_LOGGED.getCode())) {
							return new ModelAndView("signin", "nextUrl", target);
						} else if (serverResponse.getCode().equals(
								ResponseStatus.NO_PERMISSION.getCode())) {
							return new ModelAndView("403");
						} else {
							return new ModelAndView("500");
						}
					}
				}
			}

		}
		if (method.isAnnotationPresent(ResponseBody.class)) {
			try {
				Object result = pjp.proceed();
				serverResponse.setData(result);
			} catch (ActionException ae) {
				serverResponse.setResponseStatus(ae.getStatus(), ae.getArgs());
				serverResponse.setDebug(ae.getMessage());
			} catch (Exception e) {
				serverResponse.setResponseStatus(ResponseStatus.SERVER_BUSY);
				serverResponse.setDebug(e.getMessage());
				logger.error(execLogStr, e);
			}
			JSONObject object = JSONObject.fromObject(serverResponse);
			execLogStr.append(", result=" + object.toString());
			logger.info(execLogStr.toString());
			return serverResponse;
		} else {
			Object result = pjp.proceed();
			if (result instanceof ModelAndView) {
				ModelAndView result1 = (ModelAndView) result;
				result1.getModel().put("msmode", sysconf.getString("msmode"));
				Account loginUser = authUserService.getLoginUser(request);
				if (loginUser != null) {
					List<NaviItem> menus = new ArrayList<NaviItem>();
					int count = sysconf.getInteger("mainnav.count", 0);
					for (int i = 0; i < count; i++) {
						String name = sysconf.getString("mainnav.name"
								+ (i + 1));
						String url = sysconf.getString("mainnav.url" + (i + 1));
						if (StringUtils.isBlank(name)
								|| StringUtils.isBlank(url)) {
							continue;
						}
						Long pid = sysconf.getLong("mainnav.perm" + (i + 1));
						if (pid != null) {
							//check nav item perm
							boolean hasThisPerm = authUserService
									.checkUserPermission(loginUser.getUserId(),
											pid);
							if (hasThisPerm) {
								//passed
								menus.add(new NaviItem(name, url));
							}
						} else {
							//no perm required
							menus.add(new NaviItem(name, url));
						}
					}

					result1.getModel().put("mainnav", menus);
				}
			} else {
				logger.warn("目前Controller返回只支持ModelAndView与@ResponseBody");
			}
			return result;
		}

	}

}