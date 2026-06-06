package com.sa.healthplan.exception;

/**
 * Se lanza al intentar crear un recurso que viola una restricción de unicidad
 * (ej. un cliente con un documento ya registrado). El GlobalExceptionHandler la
 * traduce a un HTTP 409 (Conflict).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
