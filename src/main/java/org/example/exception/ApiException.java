package org.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public static ApiException badRequest(String message) {
        return new ApiException(message, HttpStatus.BAD_REQUEST);
    }

}
