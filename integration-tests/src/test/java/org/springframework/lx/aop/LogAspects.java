package org.springframework.lx.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.assertj.core.internal.bytebuddy.asm.Advice;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

/**
 * @author lx
 * @date 2021/12/11 15:06
 */
@Aspect
@Order(1)
public class LogAspects {

	@Pointcut("execution(* org.springframework.lx.aop.MathClalculator.*(..))")
	void pointCut() {
	}

	@Before("pointCut()")
	public  void logStart(JoinPoint joinPoint){
		System.out.println(joinPoint.getSignature().getName()+"----111before{"+ Arrays.asList(joinPoint.getArgs()) +"}");
	}
	@After("pointCut()")
	public  void logEnd(JoinPoint joinPoint){
		System.out.println(joinPoint.getSignature().getName()+" 11method end");
	}
	@AfterReturning(value = "pointCut()",returning = "result")
	public  void logReturn(Object result){
		System.out.println("11Return{"+result+"}");
	}
	@AfterThrowing(value = "pointCut()",throwing = "exception")
	public  void logException(Exception exception){
		System.out.println("111Exception{"+exception+"}");
	}
}