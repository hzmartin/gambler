package org.scoreboard.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import org.scoreboard.annotation.LogRequestParam;
import org.scoreboard.annotation.SkipOauthVerify;
import org.scoreboard.annotation.SkipResultWrap;
import org.scoreboard.bean.YixinAccessToken;
import org.scoreboard.constants.IConstants;
import org.scoreboard.exception.ActionException;
import org.scoreboard.response.ResponseStatus;
import org.scoreboard.response.ServerResponse;
import org.scoreboard.service.PublicAccountService;

import com.alibaba.fastjson.JSON;

/**
 * 定义切面
 * 
 * @Aspect : 标记为切面类
 * @Pointcut : 指定匹配切点
 * @Before : 指定前置通知，value中指定切入点匹配
 * @AfterReturning ：后置通知，具有可以指定返回值
 * @AfterThrowing ：异常通知
 */
@Component
@Aspect
public class RequestAspectAdvice {
	private Logger logger = Logger.getLogger(RequestAspectAdvice.class);

	private static final String lineSeparator = System.getProperty(
			"line.separator", "\n");

	@Resource
	private PublicAccountService loginService;

	@Around("within(@org.springframework.stereotype.Controller *)")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
		Method method = joinPointObject.getMethod();
		String fullMethodSign = pjp.getTarget().getClass().getName() + "#"
				+ pjp.getSignature().getName();
		logger.debug("process request in method: " + method);
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
						String name = logParam.value();
						if (StringUtils.isBlank(name)) {
							name = "param[" + paramIndex + "/" + argsLen + "]";
						}
						execEndLog.append(name + "=" + theArg + ",");
					}
				}
			}
		} else {
			logger.error(String.format(
					"inconsistent argument length while invoke %s#%s!", pjp
							.getTarget().getClass().getName(), pjp
							.getSignature().getName()));
		}
		if (execEndLog.lastIndexOf(",", execEndLog.length() - 1) != -1) {
			execEndLog = execEndLog.deleteCharAt(execEndLog.length() - 1);
		}
		execEndLog.append(")");
		// =============end input param log build=====================

		if (!(pjp.getArgs()[0] instanceof HttpServletRequest)) {
			throw new IllegalArgumentException("非法参数, 方法"
					+ pjp.getSignature().getName()
					+ "的第一个参数必须是HttpServletRequest request");
		}
		HttpServletRequest request = (HttpServletRequest) pjp.getArgs()[0];
		HttpServletResponse response = (HttpServletResponse) pjp.getArgs()[1];
		String openId = loginService.getOpenIdOfLoginUser(request);
		logger.debug("method=" + method + ", sessionId="
				+ request.getSession().getId() + ", openId=" + openId);
		if (StringUtils.isBlank(openId)
				&& !method.isAnnotationPresent(SkipOauthVerify.class)) {
			// didn't login
			String code = request.getParameter("code");
			logger.debug(String.format(
					"login in openid=%s, code=%s", openId, code));
			if (StringUtils.isBlank(openId)) {
				if (StringUtils.isBlank(code)) {
					loginService.login(response);
					logger.debug(String
							.format("login in after redirect openid=%s, code=%s",
									openId, code));
					return null;
				} else {
					YixinAccessToken accessToken = loginService
							.getOauthAccessToken(code);
					openId = accessToken.getOpenid();
					logger.debug("set cur_open_id=" + openId);
					if (StringUtils.isNotBlank(openId)) {
						request.getSession().setAttribute(
								IConstants.CUR_OPEN_ID_SESSION_KEY, openId);
					} else {
						loginService.login(response);
					}
				}
			}
		}
		ServerResponse serverResponse = new ServerResponse();
		String xff = request.getHeader("X-Forwarded-For");
		String remoteAddr = request.getRemoteAddr();
		execEndLog.append("\txff=" + xff + ", remoteAddr=" + remoteAddr);
		long execStartTime = System.currentTimeMillis();
		if (method.isAnnotationPresent(ResponseBody.class)
				&& !(method.isAnnotationPresent(SkipResultWrap.class))) {
			try {
				Object result = pjp.proceed();
				serverResponse.setData(result);
			} catch (ActionException ae) {
				serverResponse.setCode(ae.getCode());
				serverResponse.setData(ae.getData());
				serverResponse.setMessage(ae.getMessage());
				serverResponse.setDebug(ae.getDebug());
			} catch (Exception e) {
				serverResponse.setStatus(ResponseStatus.SERVER_BUSY);
				serverResponse.setDebug(e.getMessage());
				logger.error("server error: " + e.getMessage(), e);
			} finally {
				long execTime = System.currentTimeMillis() - execStartTime;
				execEndLog.append("\texec " + execTime + " mills");
			}
			String resultStr = previewServerResponseStr(serverResponse);
			execEndLog.append(lineSeparator + "\tresult=" + resultStr
					+ lineSeparator);
			logger.info(execEndLog.toString());
			return serverResponse;
		} else {
			logger.info(execEndLog.toString());
			Object result = pjp.proceed();
			return result;
		}

	}

	private static final String previewServerResponseStr(
			ServerResponse serverResponse) {
		String resultStr = JSON.toJSONString(serverResponse);
		int maxResultLogLength = 1000;
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
