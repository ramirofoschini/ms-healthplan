package com.sa.healthplan.model;

/**
 * Roles de los usuarios internos del sistema. Se exponen a Spring Security con
 * el prefijo {@code ROLE_}.
 */
public enum Role {
    AGENT,
    SUPERVISOR,
    ADMIN
}
