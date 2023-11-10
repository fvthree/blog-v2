package com.fvthree.blogpost.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.fvthree.blogpost.exceptions.RestAPIException;
import com.fvthree.blogpost.exceptions.RestAPIExceptionInfo;

import jakarta.servlet.http.HttpServletResponse;

public abstract class PostAbstractController {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RestAPIException.class)
    public @ResponseBody RestAPIExceptionInfo handleRestAPIException(RestAPIException ex,
    		WebRequest request, HttpServletResponse response) {
    	return new RestAPIExceptionInfo("Insert failed in database.", ex.getLocalizedMessage());
    }
}
