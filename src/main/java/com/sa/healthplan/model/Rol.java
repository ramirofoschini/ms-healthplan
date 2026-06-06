package com.sa.healthplan.model;

/**
 * Roles de los usuarios internos del sistema.
 * Se persisten como texto (ver {@code @Enumerated(EnumType.STRING)} en Usuario)
 * y se exponen a Spring Security con el prefijo {@code ROLE_}.
 */
public enum Rol {
    AGENTE,
    SUPERVISOR,
    ADMIN
}
