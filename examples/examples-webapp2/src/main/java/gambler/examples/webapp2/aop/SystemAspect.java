package gambler.examples.webapp2.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SystemAspect
{

	@Pointcut("execution(* gambler.examples.webapp2..*.*(..))")
	public void anyMethod()
	{
	}

}