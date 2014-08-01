package gambler.examples.webapp2.aop;

import gambler.commons.advmap.XMLMap;
import gambler.examples.webapp2.annotation.AuthRequired;
import gambler.examples.webapp2.annotation.LogRequestParam;
import gambler.examples.webapp2.annotation.SkipRespObjectWrap;
import gambler.examples.webapp2.dto.AccountDto;
import gambler.examples.webapp2.exception.ActionException;
import gambler.examples.webapp2.resp.ResponseStatus;
import gambler.examples.webapp2.resp.ServerResponse;
import gambler.examples.webapp2.service.AuthUserService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
	private static final Logger logger = Logger
			.getLogger(RequestAspectAdvice.class);

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
		execLogStr.append("Process request:  "
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
		AuthRequired authRequired = method
				.getAnnotation(AuthRequired.class);
		if (authRequired != null
				&& sysconf.getBoolean("switch.enableAuthentication",
						Boolean.TRUE)) {
			long[] requiredPerms = authRequired.permission();
			boolean permRequired = requiredPerms != null
					&& requiredPerms.length > 0;
			// checklogin
			boolean hasLogined = authUserService.checkLogin(request);
			AccountDto loginUser = authUserService.getLoginUser(request);
			serverResponse.setResponseStatus(ResponseStatus.OK);
			if (!hasLogined) {
				serverResponse
						.setResponseStatus(ResponseStatus.USER_NOT_LOGGED);
			} else if (permRequired) {
				// check userperm
				boolean hasPermission = authUserService.checkUserPermission(
						loginUser.getUserId(), requiredPerms);
				if (!hasPermission) {
					serverResponse
							.setResponseStatus(ResponseStatus.NO_PERMISSION);
				}
			}
			if (!ResponseStatus.OK.getCode().equals(serverResponse.getCode())) {
				execLogStr.append(", login as " + loginUser);
				if (method.isAnnotationPresent(ResponseBody.class)) {
					JSONObject object = JSONObject.fromObject(serverResponse);
					execLogStr.append(", result=" + object.toString());
					logger.info(execLogStr.toString());
					if(method.isAnnotationPresent(SkipRespObjectWrap.class)){
						return serverResponse.getCode();
					}
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
		AccountDto loginUser = authUserService.getLoginUser(request);
		execLogStr.append(", login as " + loginUser);
		if (method.isAnnotationPresent(ResponseBody.class) && !method.isAnnotationPresent(SkipRespObjectWrap.class)) {
			try {
				Object result = pjp.proceed();
				serverResponse.setData(result);
			} catch (ActionException ae) {
				serverResponse.setResponseStatus(ae.getStatus(), ae.getArgs());
				serverResponse.setMsg(ae.getMessage());
			} catch (Exception e) {
				serverResponse.setResponseStatus(ResponseStatus.SERVER_BUSY);
				serverResponse.setMsg(e.getMessage());
				logger.error(execLogStr, e);
			}
			JSONObject object = JSONObject.fromObject(serverResponse);
			execLogStr.append(", result=" + object.toString());
			logger.info(execLogStr.toString());
			return serverResponse;
		} else {
			Object result = pjp.proceed();
			return result;
		}

	}

}