package org.springframework.lx.ext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author lx
 * @date 2021/12/21 22:05
 *
 */
/**
 * SmartInitializingSingleton  在所有单实例bean创建完成后执行
 * SmartInitializingSingleton原理：
 * 1）ioc容器创建对象refresh();
 * 2）finishBeanFactoryInitialization(beanFactory);初始化剩下的单实例bean
 * 		1）先创建所有的单实例bean：getBena()
 * 		2)获取所有创建好的单实例bean，判断他们是不是SmartInitializingSingleton类型的；
 * 			如果是就调用afterSingletonsInstantiated；
 *
 *
 */
@Service
public class UserService {
	@EventListener(classes = {ApplicationEvent.class})
	public void listen(ApplicationEvent event){
		System.out.println("UserService 监听到的事件："+event);
	}
}
