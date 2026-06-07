package com.user.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user.dto.response.ErrorRespDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorRespDto> handleUserNotFound(UserNotFoundException e) {
		
		ErrorRespDto error=new ErrorRespDto();
		error.setMessage(e.getMessage());
		error.setTimestamp(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ErrorRespDto> handleDuplicateUser(UserAlreadyExistException e){
		ErrorRespDto error=new ErrorRespDto();
		error.setMessage(e.getMessage());
		error.setTimestamp(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
