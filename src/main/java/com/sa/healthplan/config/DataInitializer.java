package com.sa.healthplan.config;

import com.sa.healthplan.model.Rol;
import com.sa.healthplan.model.Usuario;
import com.sa.healthplan.repository.UsuarioRepository;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Siembra un usuario admin inicial si la tabla de usuarios está vacía.
 * Evita guardar un hash fijo en SQL: la contraseña se codifica con el
 * PasswordEncoder a partir de ADMIN_PASSWORD (default solo para desarrollo).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_USER:admin}")
    private String adminUser;

    @Value("${ADMIN_PASSWORD:1234}")
    private String adminPassword;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }
        Usuario admin = new Usuario();
        admin.setUsername(adminUser);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setEnabled(true);
        admin.setRoles(Set.of(Rol.ADMIN));
        usuarioRepository.save(admin);
        log.info("Usuario admin inicial creado: {}", adminUser);
    }
}
