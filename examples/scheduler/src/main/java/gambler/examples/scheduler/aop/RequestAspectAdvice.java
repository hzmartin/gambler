package gambler.examples.scheduler.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

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
import gambler.examples.scheduler.util.LogBean;

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
		LogBean logBean = LogBean.get();
		MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
		Method method = joinPointObject.getMethod();
		Object[] args = pjp.getArgs();
		Parameter[] parameters = method.getParameters();
		Map<String, Object> params = new HashMap<String, Object>();
		for (int i = 0; i < parameters.length; i++) {
			if (args[i] instanceof HttpServletRequest) {
				continue;
			}
			if (args[i] instanceof HttpServletResponse) {
				continue;
			}
			if (parameters[i].getAnnotation(RequestBody.class) != null) {
				logBean.setPayload(args[i]);
				break;
			} else {
				LogRequestParam anno = parameters[i].getAnnotation(LogRequestParam.class);
				if (anno != null) {
					params.put(anno.name(), args[i]);
				} else {
					params.put("p" + i, args[i]);
				}
			}
		}
		logBean.setParams(params);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String target = request.getRequestURI();
		ServerResponse serverResponse = new ServerResponse();
		AuthRequired authRequired = method.getAnnotation(AuthRequired.class);
		if (authRequired != null && sysconf.getBoolean("switch.enableAuthentication", Boolean.TRUE)) {
			long[] requiredPerms = authRequired.permission();
			boolean permRequired = requiredPerms != null && requiredPerms.length > 0;
			// checklogin
			boolean hasLogined = authUserService.checkLogin(request);
			AccountDto loginUser = authUserService.getLoginUser(request);
			if (loginUser != null) {
				logBean.setUid(loginUser.getUserId());
			}
			serverResponse.setResponseStatus(ResponseStatus.OK);
			if (!hasLogined) {
				serverResponse.setResponseStatus(ResponseStatus.USER_NOT_LOGGED);
			} else if (permRequired) {
				// check userperm
				User user = authUserService.findUserById(loginUser.getUserId());
				boolean hasPermission = authUserService.checkUserPermission(user, requiredPerms);
				if (!hasPermission) {
					serverResponse.setResponseStatus(ResponseStatus.NO_PERMISSION);
				}
			}
			if (!ResponseStatus.OK.getCode().equals(serverResponse.getCode())) {
				if (method.isAnnotationPresent(ResponseBody.class)) {
					if (method.isAnnotationPresent(SkipRespObjectWrap.class)) {
						return serverResponse.getCode();
					}
					return serverResponse;
				} else {
					if (serverResponse.getCode().equals(ResponseStatus.USER_NOT_LOGGED.getCode())) {
						return new ModelAndView("signin", "nextUrl", target);
					} else if (serverResponse.getCode().equals(ResponseStatus.NO_PERMISSION.getCode())) {
						return new ModelAndView("403");
					} else {
						return new ModelAndView("500");
					}
				}
			}
		}

		AccountDto loginUser = authUserService.getLoginUser(request);
		if (loginUser != null) {
			logBean.setUid(loginUser.getUserId());
		}
		if (method.isAnnotationPresent(ResponseBody.class) && !method.isAnnotationPresent(SkipRespObjectWrap.class)) {
			try {
				Object result = pjp.proceed();
				serverResponse.setData(result);
			} catch (ActionException ae) {
				serverResponse.setResponseStatus(ae.getStatus(), ae.getArgs());
				serverResponse.setMsg(ae.getMessage());
			} catch (Exception e) {
				serverResponse.setResponseStatus(ResponseStatus.SERVER_BUSY);
				logger.error(e.getMessage(), e);
			}
			return serverResponse;
		} else {
			Object result = pjp.proceed();
			return result;
		}

	}

}