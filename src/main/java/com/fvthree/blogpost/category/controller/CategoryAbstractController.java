package com.fvthree.blogpost.category.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.fvthree.blogpost.exceptions.DBException;
import com.fvthree.blogpost.exceptions.HTTP404Exception;
import com.fvthree.blogpost.exceptions.RestAPIException;
import com.fvthree.blogpost.exceptions.RestAPIExceptionInfo;

import jakarta.servlet.http.HttpServletResponse;

public abstract class CategoryAbstractController implements ApplicationEventPublisherAware {

	protected ApplicationEventPublisher eventPublisher;
	
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RestAPIException.class)
    public @ResponseBody RestAPIExceptionInfo handleAuthenticationException(RestAPIException ex,
    		WebRequest request, HttpServletResponse response) {
    	return new RestAPIExceptionInfo("Invalid credentials.", ex.getMessage());
    }
    
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DBException.class)
    public @ResponseBody RestAPIExceptionInfo handleDBException(DBException ex, WebRequest request, HttpServletResponse response) {
    	return new RestAPIExceptionInfo("DB error", ex.getMessage());
    }
    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
    	this.eventPublisher = eventPublisher;
    }
    
    public static <T> T checkResourceFound(final T resource) {
    	if (resource == null) {
    		throw new HTTP404Exception("Resource not found");
    	}
    	return resource;
    }
 }
