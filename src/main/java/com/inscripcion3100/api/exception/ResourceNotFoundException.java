package com.inscripcion3100.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue){
        super(String.format("No se encontró " + resourceName + " con " + fieldName + " = " + fieldValue));
    }

    public ResourceNotFoundException(String message){
        super(message);
    }
}
