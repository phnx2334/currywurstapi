package com.wurst.curry.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class CookingPlanException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public CookingPlanException(String message ) {
		super(message);
	}
	
	public CookingPlanException(String message, Throwable throwable ) {
		super(message, throwable);
	}
	
}
