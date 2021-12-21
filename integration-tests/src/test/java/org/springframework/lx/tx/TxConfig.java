package org.springframework.lx.tx;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @author lx
 * @date 2021/12/13 21:38
 */
/**
 * 1）.@EnableTransactionManagement原理：
 *  	通过TransactionManagementConfigurationSelector.class 导入一些组件
 *  	导入两个组件：AutoProxyRegistrar,ProxyTransactionManagementConfiguration
 * 2）.AutoProxyRegistrar：给容器中注入一个InfrastructureAdvisorAutoProxyCreator组件
 *      InfrastructureAdvisorAutoProxyCreator:
 *      	利用后置处理器机制在对象创建以后，包装对象，返回一个代理对象（增强器），代理对象执行方法利用拦截器链进行调用
 *3）.ProxyTransactionManagementConfiguration：
 * 		1.给容器注入事务增强器
 * 			1.事务增强器要用事务注解的信息，AnnotationTransactionAttributeSource解析事务注解
 * 			2.事务拦截器：TransactionInterceptor保存了事务属性信息，事务管理器
 * 				他是一个MethodInterceptor; 在目标方法执行的时候执行拦截器链；
 * 			事务拦截器：
 * 				1.获取事务相关的属性
 * 				2.再获取TransactionManager，如果没有添加指定任何TransactionManager，最终从容器中按照类型获取一个TransactionManager
 * 				3.执行目标方法
 * 					如果异常，获取都事务管理器，利用事务管理器回滚
 * 					如果正常，利用事务管理器，提交事务
 *
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("org.springframework.lx.tx")
public class TxConfig {
	@Bean
	public DataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setUser("root");
		dataSource.setPassword("123456");
		dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
		return  dataSource;
	}
	@Bean
	public JdbcTemplate jdbcTemplate() throws PropertyVetoException {
		return new JdbcTemplate(dataSource());
	}
	//事务管理器
	@Bean
	public TransactionManager transactionManager() throws PropertyVetoException {
		return new DataSourceTransactionManager(dataSource());
	}

}
