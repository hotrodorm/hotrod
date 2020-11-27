package hr3.springboot.poc.rest;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GenericExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(GenericExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
		ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), ex.getMessage(),
				request.getDescription(false));

		LOG.error("Error inesperado", ex);
//		final String path = request.getPath().pathWithinApplication().value();
//		LOG.debug("Returning HTTP status: {} for path: {}, message: {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), path, message);

		return message;
	}

}
