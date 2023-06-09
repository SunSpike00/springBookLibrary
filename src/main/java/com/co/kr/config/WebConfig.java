package com.co.kr.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.co.kr.util.AdminDuplecheckInterceptor;
import com.co.kr.util.DuplecheckInterceptor;
import com.co.kr.util.LoginDataInterceptor;
import com.co.kr.util.LoginInterceptor;
import com.co.kr.util.UserPwCheckInterceptor;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Configuration	
public class WebConfig implements WebMvcConfigurer {

	@Bean
	@Description("Thymeleaf template resolver serving HTML")
	public ClassLoaderTemplateResolver templateResolver() {
		
		var templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheable(true);
		templateResolver.setTemplateMode("html");
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}
	
	@Bean
	@Description("Thymeleaf template engine with Spring integration")
	public SpringTemplateEngine templateEngine() {
		var templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.addDialect(new LayoutDialect()); //dependency 미리 설정해놓음
		return templateEngine;
	}
	
	@Bean
	@Description("Thymeleaf view resolver")
	public ViewResolver viewResolver() {
		var viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index.html");
	}
	
	// 인터셉터 설정을 모아둔 메소드
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
    	//허용되야 할 url들
    	//mapping 경로 외에도 이미지 경로, css 경로, js 경로 등 다양하게 허용해야 인터셉터 사용 가능.
    	List<String> patterns = List.of("/", "/login", "/sign", "/bookajax", "/css/**", "/js/**", "/bookStore", "/register",
				"/resources/bookImg/**", "/resources/bookCoverImg/**","/bookInfo", "/mybatis/**", "/.ico");
    	
        registry.addInterceptor(loginInterceptor())
        		.addPathPatterns("/**")
        		.excludePathPatterns(patterns);
        
        registry.addInterceptor(duplecheckInterceptor())
        		.addPathPatterns("/register");
        
        registry.addInterceptor(loginDataInterceptor())
        		.addPathPatterns("/bookStore");
        
        registry.addInterceptor(userPwCheckInterceptor())
        		.addPathPatterns("/userUpdate")
        		.excludePathPatterns("/register");
        
        registry.addInterceptor(adminDuplecheckInterceptor())
        		.addPathPatterns("/adminRegister");
    }
    
    //단순히 new로 하면 Autowired를 사용하지 못해서 서비스를 찾지 못함.
    //로그인 여부 확인 인터셉터
    @Bean
    public LoginInterceptor loginInterceptor() {
    	return new LoginInterceptor();
    }
	
    //회원가입 중복 체크 인터셉터
    @Bean
    public DuplecheckInterceptor duplecheckInterceptor() {
    	return new DuplecheckInterceptor();
    }
    
    //로그인 정보 일치 확인 인터셉터
    @Bean
    public LoginDataInterceptor loginDataInterceptor() {
    	return new LoginDataInterceptor();
    }
    
    //회원 정보 수정시 패스워드 일치 확인 인터셉터
    @Bean
    public UserPwCheckInterceptor userPwCheckInterceptor() {
    	return new UserPwCheckInterceptor();
    }
    
    @Bean
    public AdminDuplecheckInterceptor adminDuplecheckInterceptor() {
    	return new AdminDuplecheckInterceptor();
    }
    
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/bookCoverImg/**").addResourceLocations("file:///C:/bookCoverImg/");
		registry.addResourceHandler("/resources/bookImg/**").addResourceLocations("file:///C:/bookImg/");
	}
}