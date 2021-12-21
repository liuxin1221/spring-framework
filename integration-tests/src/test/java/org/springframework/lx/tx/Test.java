package org.springframework.lx.tx;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author lx
 * @date 2021/12/13 21:59
 */
/**
 *  @EnableTransactionManagement开启事务管理功能
 *
 */
public class Test {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TxConfig.class);
		StudentService studentService = context.getBean("studentService", StudentService.class);
		studentService.insert();
		context.close();
	}
}
