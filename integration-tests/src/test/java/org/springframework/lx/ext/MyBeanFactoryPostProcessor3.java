package org.springframework.lx.ext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author lx
 * @date 2021/12/15 21:34
 * 注解@Order对BeanFactoryPostProcessor的执行顺序无用，但是实现Ordered可以进行调用排序
 * 还有一个PriorityOrdered 优先级比Ordered还高
 */
@Component
public class MyBeanFactoryPostProcessor3 implements BeanFactoryPostProcessor,Ordered {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("3333333MyBeanFactoryPostProcessor-----postProcessBeanFactory");
		int count = beanFactory.getBeanDefinitionCount();
		String[] definitionNames = beanFactory.getBeanDefinitionNames();
		System.out.println("当前BeanFactory中有"+count+"个Bean");
		System.out.println(Arrays.asList(definitionNames));
	}

	@Override
	public int getOrder() {
		return 2;
	}
}
