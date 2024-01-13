package com.fvthree.blogpost.exceptions;

import org.springframework.http.HttpStatus;

public class DBException extends RuntimeException {
	
	private static final long serialVersionUID = 2522133760325645649L;
	
	private HttpStatus status;
	
	private String message;

	public DBException() {
		super();
	}
	
	public DBException(String message, Throwable cause) {
		super(message,cause);
	}
	
	public DBException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public DBException(String message) {
		super(message);
	}
	
	public DBException(Throwable cause) {
		super(cause);
	}
}
