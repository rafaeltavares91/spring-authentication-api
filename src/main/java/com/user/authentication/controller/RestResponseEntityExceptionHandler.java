package com.user.authentication.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.user.authentication.dto.ApiError;
import com.user.authentication.excetion.EmailAlreadyExistsException;
import com.user.authentication.excetion.InvalidEmailPasswordException;

/**
 *
 * @author Rafael Tavares
 *
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler {
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(Exception exception, WebRequest request) {
        return new ResponseEntity<Object>(
        		ApiError.of("E-mail already exists", HttpStatus.UNPROCESSABLE_ENTITY.value()), 
        		new HttpHeaders(), 
        		HttpStatus.UNPROCESSABLE_ENTITY);
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(Exception exception, WebRequest request) {
        return new ResponseEntity<Object>(
        		ApiError.of("Missing fields", HttpStatus.BAD_REQUEST.value()), 
        		new HttpHeaders(), 
        		HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(InvalidEmailPasswordException.class)
    public ResponseEntity<Object> handleBadCredentialsExceptionx(Exception exception, WebRequest request) {
        return new ResponseEntity<Object>(
        		ApiError.of("Invalid e-mail or password", HttpStatus.FORBIDDEN.value()), 
        		new HttpHeaders(), 
        		HttpStatus.FORBIDDEN);
    }
	 
	@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleBadCredentialsException(Exception exception, WebRequest request) {
        return new ResponseEntity<Object>(
        		ApiError.of("Invalid e-mail or password", HttpStatus.FORBIDDEN.value()), 
        		new HttpHeaders(), 
        		HttpStatus.FORBIDDEN);
    }

}