package ru.task.gibdd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.task.gibdd.models.ResponseMessage;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(OverNumberLimit.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseMessage handlerOverNumberLimit(OverNumberLimit ex) {
		return ResponseMessage.builder()
				.message(ex.getMessage())
				.errorCode(HttpStatus.FORBIDDEN.value())
				.build();
	}
}
