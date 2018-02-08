package com.nokia.boss.util;

@SuppressWarnings("serial")
public class RangeException extends RuntimeException{
	
	public RangeException() {}

	public RangeException(String message) {
		super(message);
	}

	public RangeException(String message,Throwable cause) {
		super(message,cause);
	}
	
	public RangeException(Throwable cause) {
		super(cause);
	}


}
