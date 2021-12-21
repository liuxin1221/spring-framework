package org.springframework.lx.ext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author lx
 * @date 2021/12/15 21:32
 */
public class Test {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExtConfig.class);
		context.publishEvent(new ApplicationEvent("我发布的事件") {
			@Override
			public Object getSource() {
				return super.getSource();
			}
		});
		context.close();
	}
}
