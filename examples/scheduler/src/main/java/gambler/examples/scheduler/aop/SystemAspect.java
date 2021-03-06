package gambler.examples.scheduler.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SystemAspect
{

	@Pointcut("execution(* gambler.examples.scheduler..*.*(..))")
	public void anyMethod()
	{
	}

}