package com.user.authentication.excetion;

/**
 *
 * @author Rafael Tavares
 *
 */
public class InvalidEmailPasswordException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidEmailPasswordException() { }

	public InvalidEmailPasswordException(String message) {
		super(message);
	}

	public InvalidEmailPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidEmailPasswordException(Throwable cause) {
		super(cause);
	}

	public InvalidEmailPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
