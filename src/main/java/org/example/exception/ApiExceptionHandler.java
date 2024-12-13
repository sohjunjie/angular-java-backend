package org.example.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.example.utils.Constants.API_MDC_CORRELATION_ID_KEY;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<ApiExceptionResponse> handleApiExceptionError(RuntimeException exp, HttpServletRequest req) {

        String correlationId = req.getHeader(API_MDC_CORRELATION_ID_KEY);
        String httpMethod = req.getMethod();
        String apiEndpoint = req.getServletPath();

        ApiExceptionResponse restExp = new ApiExceptionResponse();
        restExp.setCorrelationId(correlationId);
        restExp.setMethod(httpMethod);
        restExp.setEndpoint(apiEndpoint);
        restExp.setErrorDescription(exp.getMessage());
        restExp.setTimestamp(
                ZonedDateTime.now(ZoneId.of("UTC"))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
        );

        HttpStatus httpStatus;
        if(exp instanceof ApiException) {
            httpStatus = ((ApiException) exp).getHttpStatus();
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(restExp, headers, httpStatus);

    }

}
