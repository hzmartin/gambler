package gambler.examples.scheduler.aop;

import gambler.commons.advmap.XMLMap;
import gambler.examples.scheduler.annotation.AuthRequired;
import gambler.examples.scheduler.annotation.LogRequestParam;
import gambler.examples.scheduler.annotation.SkipRespObjectWrap;
import gambler.examples.scheduler.domain.auth.User;
import gambler.examples.scheduler.dto.AccountDto;
import gambler.examples.scheduler.exception.ActionException;
import gambler.examples.scheduler.resp.ResponseStatus;
import gambler.examples.scheduler.resp.ServerResponse;
import gambler.examples.scheduler.service.AuthUserService;

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

	private static final String lineSeparator = System.getProperty(
			"line.separator", "\n");

	@Autowired
	private XMLMap sysconf;

	@Around("within(@org.springframework.stereotype.Controller *)")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
		Method method = joinPointObject.getMethod();
		String fullMethodSign = pjp.getTarget().getClass().getName() + "#"
				+ pjp.getSignature().getName();
		// =============start input param log build=====================
		StringBuilder execEndLog = new StringBuilder();
		execEndLog.append("Request " + fullMethodSign + "(");
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
						execEndLog.append(name + "=" + theArg + ",");
					}
				}
			}
		} else {
			logger.error(String.format(
					"Inconsistent argument length while invoke %s#%s!", pjp
							.getTarget().getClass().getName(), pjp
							.getSignature().getName()));
		}
		if (execEndLog.lastIndexOf(",", execEndLog.length() - 1) != -1) {
            execEndLog = execEndLog.deleteCharAt(execEndLog.length() - 1);
        }
        execEndLog.append(")");
		// =============end input param log build=====================

		if (!(pjp.getArgs()[0] instanceof HttpServletRequest)) {
			throw new IllegalArgumentException(
					"Illegal Argument, the 1st paramter of Method("
							+ pjp.getSignature().getName()
							+ ") must be HttpServletRequest");
		}
		HttpServletRequest request = (HttpServletRequest) pjp.getArgs()[0];
		String xff = request.getHeader("X-Forwarded-For");
		String remoteAddr = request.getRemoteAddr();
		String target = request.getRequestURI();
		ServerResponse serverResponse = new ServerResponse();
		AuthRequired authRequired = method.getAnnotation(AuthRequired.class);
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
				User user = authUserService.findUserById(loginUser.getUserId());
				boolean hasPermission = authUserService.checkUserPermission(
						user, requiredPerms);
				if (!hasPermission) {
					serverResponse
							.setResponseStatus(ResponseStatus.NO_PERMISSION);
				}
			}
			if (!ResponseStatus.OK.getCode().equals(serverResponse.getCode())) {
				if (method.isAnnotationPresent(ResponseBody.class)) {
					execEndLog.append("\tlogin as " + loginUser + "\tresult="
	                        + previewServerResponseStr(serverResponse) + lineSeparator);
	                    logger.info(execEndLog.toString());
					if (method.isAnnotationPresent(SkipRespObjectWrap.class)) {
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
		execEndLog.append("\tlogin as " + loginUser);
        execEndLog.append("\txff=" + xff + ", remoteAddr=" + remoteAddr);
        long execStartTime = System.currentTimeMillis();
		if (method.isAnnotationPresent(ResponseBody.class)
				&& !method.isAnnotationPresent(SkipRespObjectWrap.class)) {
			try {
				Object result = pjp.proceed();
				serverResponse.setData(result);
			} catch (ActionException ae) {
				serverResponse.setResponseStatus(ae.getStatus(), ae.getArgs());
				serverResponse.setMsg(ae.getMessage());
			} catch (Exception e) {
				serverResponse.setResponseStatus(ResponseStatus.SERVER_BUSY);
				serverResponse.setMsg(e.getMessage());
				logger.error("server error: " + e.getMessage(), e);
			} finally{
				long execTime = System.currentTimeMillis() - execStartTime;
                execEndLog.append("\texec " + execTime + " mills");
			}
			String resultStr = previewServerResponseStr(serverResponse);
			execEndLog.append(lineSeparator + "\tresult=" + resultStr
	                + lineSeparator);
			logger.info(execEndLog.toString());
			return serverResponse;
		} else {
			Object result = pjp.proceed();
			return result;
		}

	}

	private final String previewServerResponseStr(ServerResponse serverResponse) {
		String resultStr = JSONObject.fromObject(serverResponse).toString();
		int maxResultLogLength = sysconf.getInteger(
				"max_length_of_response_log", 1000);
		if (maxResultLogLength != -1) {
			boolean tooLong = resultStr.length() > maxResultLogLength;
			resultStr = StringUtils.substring(resultStr, 0, maxResultLogLength);
			if (tooLong) {
				resultStr = resultStr + " ... ";
			}
		}
		return resultStr;
	}

}