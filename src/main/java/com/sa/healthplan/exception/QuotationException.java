package com.sa.healthplan.exception;

/**
 * Se lanza cuando una cotización no puede calcularse aunque la petición sea
 * válida (ej. plan inactivo, edad sin franja, o sin precio vigente para una
 * franja). El GlobalExceptionHandler la traduce a un HTTP 422 (Unprocessable
 * Entity): la entrada es correcta, pero falta configuración para procesarla.
 */
public class QuotationException extends RuntimeException {

    public QuotationException(String message) {
        super(message);
    }
}
