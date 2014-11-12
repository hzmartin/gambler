package gambler.examples.webapp2.aop;

import gambler.examples.webapp2.annotation.DataSource;
import gambler.examples.webapp2.datasource.DataSourceContextHolder;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

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

	@Around(value = "gambler.examples.webapp2.aop.SystemAspect.anyMethod()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		try {
			MethodSignature joinPointObject = (MethodSignature) pjp
					.getSignature();
			Method method = joinPointObject.getMethod();
			if (method.isAnnotationPresent(DataSource.class)) {
				DataSource dsAnno = method.getAnnotation(DataSource.class);
				String name = dsAnno.value();
				DataSourceContextHolder.selectDataSource(name);
				logger.info("select datasource " + name);
			}

			Object result = pjp.proceed();

			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			if (StringUtils.isNotBlank(DataSourceContextHolder.getDataSourceName())) {
				DataSourceContextHolder.clearDataSourceSelection();
				logger.info("clear datasource selection");
			}
		}

	}

}