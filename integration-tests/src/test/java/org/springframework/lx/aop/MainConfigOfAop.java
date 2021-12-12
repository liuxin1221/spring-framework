package org.springframework.lx.aop;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @author lx
 * @date 2021/12/11 15:04
 */
@Configuration
@EnableAspectJAutoProxy
public class MainConfigOfAop {

	@Bean
	public  MathClalculator clalculator(){
		return new MathClalculator();
	}
	@Bean
	public  LogAspects logAspects(){
		return  new LogAspects();
	}

}
