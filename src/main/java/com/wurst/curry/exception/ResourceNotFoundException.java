package com.wurst.curry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ResourceNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message ) {
		super(message);
	}
	
	public ResourceNotFoundException( HttpStatus status, String message ) {
		super(message);
	}
	
	
	public ResourceNotFoundException(String message, Throwable throwable ) {
		super(message, throwable);
	}
	
}
