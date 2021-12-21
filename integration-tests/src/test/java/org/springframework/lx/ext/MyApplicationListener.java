package org.springframework.lx.ext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author lx
 * @date 2021/12/21 21:44
 * 事件是在容器刷新完成后执行的
 */
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {
	/**
	 * 当容器中发布事件后，触发当前方法
	 *
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		System.out.println("收到事件;"+event);

	}
}
