package com.powerstrike.exception;

/** Recurso no encontrado → se mapea a HTTP 404 en GlobalExceptionHandler (DEF-EXP-01). */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
