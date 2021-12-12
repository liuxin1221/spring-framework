package org.springframework.lx.aop;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author lx
 * @date 2021/12/11 15:27
 */
public class AopTest {

	@Test
	public  void test01() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfigOfAop.class);
		MathClalculator mathClalculator = context.getBean("clalculator", MathClalculator.class);
		mathClalculator.div(10,5);
		context.close();
	}
}
