package com.fvthree.blogpost.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fvthree.blogpost.interceptors.UserSecureEndpointInterceptor;
import com.fvthree.blogpost.security.JwtTokenProvider;
import com.fvthree.blogpost.user.dao.impl.UserDao;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserDao userDao;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(new UserSecureEndpointInterceptor(jwtTokenProvider, userDao))
			.addPathPatterns("/api/v1/users/profile/**");
	}

}
