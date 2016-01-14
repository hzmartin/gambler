package gambler.examples.scheduler.aop;

import gambler.examples.scheduler.annotation.LogMethod;

import java.lang.reflect.Method;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
public class LogAspectAdvice
{
	private static final Logger logger = Logger.getLogger(LogAspectAdvice.class);

	@Before(value = "gambler.examples.scheduler.aop.SystemAspect.anyMethod()")
	public void doBefore(JoinPoint jp)
	{
		MethodSignature joinPointObject = (MethodSignature) jp.getSignature();
		Method method = joinPointObject.getMethod();
		if (!method.isAnnotationPresent(LogMethod.class))
		{
			return;
		}
		LogMethod logParamAnno = method.getAnnotation(LogMethod.class);
		String level = logParamAnno.level();
		int argsLen = jp.getArgs().length;
		StringBuilder logString = new StringBuilder();
		logString.append("Execute method: " + jp.getTarget().getClass().getName() + "#" + jp.getSignature().getName() + "(");
		for (int paramIndex = 0; paramIndex < argsLen; paramIndex++)
		{
			Object theArg = jp.getArgs()[paramIndex];
			logString.append(theArg + ",");
		}
		int lastCommaIndex = logString.lastIndexOf(",");
		if (lastCommaIndex != -1)
		{
			logString.deleteCharAt(lastCommaIndex);
		}
		logString.append(")");
		logger.log(Level.toLevel(level, Level.DEBUG), logString.toString());

	}

}