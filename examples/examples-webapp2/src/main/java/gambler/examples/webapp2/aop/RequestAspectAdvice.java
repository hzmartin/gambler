package gambler.examples.webapp2.aop;

import gambler.examples.webapp2.annotation.AuthRequired;
import gambler.examples.webapp2.annotation.LogRequestParam;
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
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

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
	private Logger logger = Logger.getLogger(RequestAspectAdvice.class);

	@Resource
	protected AuthUserService authUserService;

	@Around("within(@org.springframework.stereotype.Controller *)")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
		Method method = joinPointObject.getMethod();
		// =============start input param log build=====================
		StringBuilder execLogStr = new StringBuilder();
		execLogStr.append("处理请求：  " + pjp.getTarget().getClass().getName()
				+ "#" + pjp.getSignature().getName() + "(");
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
					"inconsistent argument length while invoke %s#%s!", pjp
							.getTarget().getClass().getName(), pjp
							.getSignature().getName()));
		}
		execLogStr.append(")");
		// =============end input param log build=====================

		if (!(pjp.getArgs()[0] instanceof HttpServletRequest)) {
			throw new IllegalArgumentException("非法参数, 方法"
					+ pjp.getSignature().getName()
					+ "的第一个参数必须是HttpServletRequest request");
		}
		HttpServletRequest request = (HttpServletRequest) pjp.getArgs()[0];
		ServerResponse serverResponse = new ServerResponse();
		AuthRequired authRequiredAnno = method
				.getAnnotation(AuthRequired.class);
		if (authRequiredAnno != null) {
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
					// check userperm
					boolean hasPermission = authUserService
							.checkUserPermission(request, requiredPerms);
					if (!hasPermission) {
						serverResponse
								.setResponseStatus(ResponseStatus.NO_PERMISSION);
					}
				}
				if (ResponseStatus.OK.getCode()
						.equals(serverResponse.getCode())) {
					if (method.isAnnotationPresent(ResponseBody.class)) {
						JSONObject object = JSONObject
								.fromObject(serverResponse);
						execLogStr.append(", result=" + object.toString());
						logger.info(execLogStr.toString());
						return serverResponse;
					} else {
						throw new UnsupportedOperationException("unsupported!");
					}
				}
			}

		}
		if (method.isAnnotationPresent(ResponseBody.class)) {
			try {
				Object result = pjp.proceed();
				serverResponse.setData(result);
			} catch (ActionException ae) {
				serverResponse.setResponseStatus(ae.getStatus());
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
			return result;
		}

	}

}