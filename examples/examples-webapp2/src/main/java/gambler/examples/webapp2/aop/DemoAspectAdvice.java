package gambler.examples.webapp2.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
public class DemoAspectAdvice {

	/**
	 * 前置通知
	 * 
	 * @param jp
	 */
	@Before(value = "within(@org.springframework.stereotype.Controller *)")
	public void doBefore(JoinPoint jp) {
		System.out.println("===========进入before advice============ \n");
		System.out.print("准备在" + jp.getTarget().getClass() + "对象上用");
		System.out.print(jp.getSignature().getName() + "方法进行对 '");
		System.out.print(jp.getArgs()[0] + "'进行删除！\n\n");
		System.out.println("要进入切入点方法了 \n");
	}
	
}