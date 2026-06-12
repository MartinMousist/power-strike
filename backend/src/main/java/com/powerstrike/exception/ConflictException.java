package com.powerstrike.exception;

/** Conflicto de estado (ej. duplicado) → se mapea a HTTP 409 en GlobalExceptionHandler (DEF-EXP-02). */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
