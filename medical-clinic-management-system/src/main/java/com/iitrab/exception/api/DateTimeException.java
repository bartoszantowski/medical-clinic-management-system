package com.iitrab.exception.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Generic date and time exception indicating that some resource could not be found.
 * Will resolve to the {@link HttpStatus#NOT_FOUND} if handled by the Spring's exception handler.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DateTimeException extends BusinessException {

    public DateTimeException(String message) {
        super(message);
    }
}
