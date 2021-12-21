package org.springframework.lx.ext;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lx
 * @date 2021/12/15 21:27
 */
/**
 * BeanPostProcessor:bean后置处理器，bean创建对象初始化前后进行拦截工作的
 * BeanFactoryPostProcessor:beanFactory的后置处理器；
 * 		在BeanFactory标准初始化之后调用
 *
 */
@Configuration
@ComponentScan("org.springframework.lx.ext")
public class ExtConfig {
}
