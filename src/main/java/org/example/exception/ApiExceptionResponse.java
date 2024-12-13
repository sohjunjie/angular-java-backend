package org.example.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiExceptionResponse {

    private String timestamp;
    private String errorDescription;
    private String method;
    private String endpoint;
    private String correlationId;

}
