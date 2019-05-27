package com.user.authentication.dto;

/**
 *
 * @author Rafael Tavares
 *
 */
public class ApiError {

	private String message;
	private Integer errorCode;
	
	public static ApiError of(String message, Integer errorCode) {
		return new ApiError(message, errorCode);
	}
	
	private ApiError(String message, Integer errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public Integer getErrorCode() {
		return errorCode;
	}
	
}
