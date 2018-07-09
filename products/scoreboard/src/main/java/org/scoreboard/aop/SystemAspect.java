package org.scoreboard.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SystemAspect {

	@Pointcut("execution(* org.scoreboard..*.*(..))")
	public void anyMethod() {
	}

}