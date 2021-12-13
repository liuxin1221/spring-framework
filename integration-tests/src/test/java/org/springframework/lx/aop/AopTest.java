package org.springframework.lx.aop;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author lx
 * @date 2021/12/11 15:27
 */
/**
 * 1. @EnableAspectJAutoProxy给容器中注入AnnotationAwareAspectJAutoProxyCreator这个bean
 * 2.AnnotationAwareAspectJAutoProxyCreator:
 * 		-->AspectJAwareAdvisorAutoProxyCreator
 * 		-->AbstractAdvisorAutoProxyCreator
 * 		-->AbstractAutoProxyCreator
 * 			implements 	SmartInstantiationAwareBeanPostProcessor
 * 		--> InstantiationAwareBeanPostProcessor
 * 		--> BeanPostProcessor
 *
 *
 * AbstractAutoProxyCreator -->setBeanFactory()
 * AbstractAutoProxyCreator有后置处理器
 * AbstractAdvisorAutoProxyCreator.setBeanFactory()-->initBeanFactory()
 * AnnotationAwareAspectJAutoProxyCreator.initBeanFactory()
 * 流程：
 * 1）.传入配置类创建ioc容器
 * 2）.注册配置类。调用refresh（）刷新容器
 * 3）.registerBeanPostProcessors(beanFactory);注册bean的后置处理器，方便拦截bean的创建
 *   1.先获取ioc容器已经定义了的需要创建的所有BeanPostProcessor
 *   2.给容器中添加别的BeanPostProcessor
 *   3.优先注册实现了PriorityOrdered接口的BeanPostProcessor
 *   4.然后注册实现了Ordered接口的BeanPostProcessor
 *   5.最后注册其他的BeanPostProcessor
 *   6.注册BeanPostProcessor 实际上就是创建BeanPostProcessor对象，保存在容器中：
 *   	创建InstantiationAwareBeanPostProcessor的BeanPostProcessor（AnnotationAwareAspectJAutoProxyCreator）
 *   	1.创建bean实例
 *   	2.populateBean 给属性赋值
 *   	3.initializeBean：初始化bean
 *   		1.invokeAwareMethods（）：处理Aware接口的方法回调
 *   		2.applyBeanPostProcessorsBeforeInitialization；应用后置处理器的postProcessBeforeInitialization(result, beanName)
 *   		3.invokeInitMethods（）应用初始化方法
 *   		4.applyBeanPostProcessorsAfterInitialization（）执行后置处理器的postProcessAfterInitialization(result, beanName)
 *   	4.BeanPostProcessor（AnnotationAwareAspectJAutoProxyCreator）创建成功；-->aspectJAdvisorsBuilder
 *   7.把BeanPostProcessor注册到BeanFactory中。
 *   	PostProcessorRegistrationDelegate的beanFactory.addBeanPostProcessor(postProcessor);
 *   -----创建和注册	AnnotationAwareAspectJAutoProxyCreator的过程------
 *   AnnotationAwareAspectJAutoProxyCreator-->InstantiationAwareBeanPostProcessor
 *4）.finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory);完成BeanFactory初始化工作；创建剩下的单实例
 * 		1.遍历获取容器的所有bean，依次创建对象getBean（beanName）
 * 			getBean-->doGetBean()-->getSingleton()-->
 * 		2.创建bean
 * 			【AnnotationAwareAspectJAutoProxyCreator在所有bean创建之前会有一个拦截，会调用InstantiationAwareBeanPostProcessor（）】
 * 			1）.先从缓存中获取当前bean，如果能获取到，说明bean是之前被创建过的。直接使用，否则再创建，只要创建好的bean都会被缓存起来
 * 				1）.后置处理器先尝试返回对象；
 * 					bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
 * 				拿到所有后置处理器，调用InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation
 * 					if (bean != null) {
 * 						bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
 *                                    }
 * 			2）.createBean();创建bean；AnnotationAwareAspectJAutoProxyCreator会在任何bean创建之前先尝试返回
 * 				【InstantiationAwareBeanPostProcessor是在创建Bean实例之前先尝试用后置处理器返回对象】
 * 				1）. resolveBeforeInstantiation(beanName, mbdToUse);解析BeforeInstantiationResolved
 * 				 希望后置处理器在此能返回一个代理对象；如果能返回代理对象就使用，如果不能就继续
 * 				2). doCreateBean(beanName, mbdToUse, args);真的去创建一个bean实例和3.6一样
 *
 *AnnotationAwareAspectJAutoProxyCreator【InstantiationAwareBeanPostProcessor】的作用：
 * 1）.在每个bean创建之前调用postProcessBeforeInstantiation（）；
 * 	关注LogAspects和MathClalculator的创建
 * 		1.判断当前bean是否在advisedBean中（0保存了所有需要增强的bean
 * 		2.判断当前bean是否是基础类型的Advive,PointCut,Advisor,AopInfrastureBean	或者是切面（@Aspect）
 * 		3.是否需要跳过
 * 2）创建对象postProcessBeforeInstantiation；
 * 			return wrapIfNecessary(bean, beanName, cacheKey); 包装如果需要的话
 * 		1.获取当前bean所有的增强器（通知方法） 	Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
 * 			1.找到候选的所有增强器（找到哪些通知方法是需要切入当前bean方法）
 * 			2.获取到能在bean使用的增强器
 * 			3.给增强器排序
 * 		2.保存当前bean在advisedBeans
 * 		3.如果当前bean需要增强，创建当前bean的代理对象
 * 			1.获取所有增强器（通知方法）
 * 			2.保存到proxyFactory
 * 			3.创建代理对象，spring自动决定是用jdk还是cglib
 * 		4.给容器中返回当前组件使用cglib增强了的代理对象
 * 		5.以后容器中获取到的就是这个组件的代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程
 *3）目标方法的执行流程
 *		容器中保存了组件的代理对象（cglib增强后的对象），这个对象里面保存了详细信息（比如增强器，目标对象等等）
 * 		1.CglibAopProxy.intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
 * 		2.根据ProxyFactory对象获取将要执行的目标方法的拦截器链
 * 				List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
 *				1.创建一个长度为advisors.length（advisors里面装的是增强器）的List<Object> interceptorList保存所有拦截器
 *				2.遍历所有的增强器，把它转化为Interceptor --- registry.getInterceptors(advisor);
 *				3.将增强器转为List<MethodInterceptor> interceptors
 *				 如果是MethodInterceptor，则直接添加到集合interceptors中，如果不是，使用AdvisorAdapter将其转为MethodInterceptor
 *				 转换完成后直接返回interceptors
 * 		3.如果没有拦截器链，直接执行目标方法
 * 			拦截器链（每个通知方法又被包装为方法拦截器，利用MethodInterceptor机制）
 * 		4.如果有拦截器链，把需要执行的目标方法，拦截器链等信息传入创建一个CglibMethodInvocation对象中，并调用它的proceed()，返回一个Object retVal;
 * 		5.拦截器链的触发过程
 * 			1.如果没有拦截器或者拦截器的索引和拦截器数组-1大小一致时（也就是执行到最后一个拦截器）会直接执行目标方法invokeJoinpoint();
 * 			2.链式获取每一个拦截器，拦截器执行invoke方法，每个拦截器等待下一个拦截器执行完成返回以后再执行；拦截器链的机制，保证了通知方法和目标方法的执行顺序
 * 效果：
 * 正常执行--》前置通知--》目标方法--》后置通知--》返回通知    出现异常--》前置通知--》目标方法--》后置通知--》异常通知
 *
 *
 *
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
