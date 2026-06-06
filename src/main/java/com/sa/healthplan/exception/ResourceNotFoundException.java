package com.sa.healthplan.exception;

/**
 * Se lanza cuando se solicita una entidad por su identificador y no existe.
 * Es unchecked para no obligar a propagar {@code throws} por todas las capas;
 * el {@link GlobalExceptionHandler} la traduce a un HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String entidad, Object id) {
        super("No se encontró " + entidad + " con id " + id);
    }
}
