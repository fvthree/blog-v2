package com.fvthree.blogpost.interceptors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fvthree.blogpost.security.JwtTokenProvider;
import com.fvthree.blogpost.user.dao.impl.UserDao;
import com.fvthree.blogpost.user.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserSecureEndpointInterceptor implements HandlerInterceptor {
	
	private static final Logger logger = LogManager.getLogger(UserSecureEndpointInterceptor.class);
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserDao userDao;
	
	public UserSecureEndpointInterceptor(JwtTokenProvider jwtTokenProvider, UserDao userDao) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDao = userDao;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String pathWithoutContext = requestURI.substring(contextPath.length());
		String[] pathSegments = pathWithoutContext.split("/");
		String id = pathSegments[5];
		logger.info("interceptor id = {} " , id);
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); 
            String username = jwtTokenProvider.getUsername(token);
            logger.info("interceptor token username = {}" , username);
            User user = null;
            try {
            	user = userDao.findById(Long.parseLong(id));
            	if (user != null && user.username().equals(username)) {
    	            return true;
                }
            } catch(Exception e) {
            	logger.error("logger in pre-handle = {}" , e.getMessage());
            	return false;
            }
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
