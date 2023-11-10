package com.fvthree.blogpost.exceptions;

import org.springframework.http.HttpStatus;

public class RestAPIException extends RuntimeException {
	
	private static final long serialVersionUID = 2522133760325645649L;
	
	private HttpStatus status;
	
	private String message;

	public RestAPIException() {
		super();
	}
	
	public RestAPIException(String message, Throwable cause) {
		super(message,cause);
	}
	
	public RestAPIException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public RestAPIException(String message) {
		super(message);
	}
	
	public RestAPIException(Throwable cause) {
		super(cause);
	}
}
