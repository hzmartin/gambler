package org.scoreboard.aop;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import org.scoreboard.annotation.DataSource;
import org.scoreboard.datasource.DataSourceContextHolder;

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
public class DataSourceAspectAdvice {
	private Logger logger = Logger.getLogger(DataSourceAspectAdvice.class);

	@Around(value = "org.scoreboard.aop.SystemAspect.anyMethod()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		try {
			MethodSignature joinPointObject = (MethodSignature) pjp
					.getSignature();
			Method method = joinPointObject.getMethod();
			logger.debug("select datasource in method: " + method);
			if (method.isAnnotationPresent(DataSource.class)) {
				DataSource dsAnno = method.getAnnotation(DataSource.class);
				String name = dsAnno.value();
				DataSourceContextHolder.selectDataSource(name);
				logger.debug("select datasource " + name);
			}

			Object result = pjp.proceed();

			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			if (StringUtils.isNotBlank(DataSourceContextHolder
					.getDataSourceName())) {
				DataSourceContextHolder.clearDataSourceSelection();
				logger.debug("clear datasource selection");
			}
		}

	}

}