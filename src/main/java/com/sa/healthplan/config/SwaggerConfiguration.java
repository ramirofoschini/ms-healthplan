package com.sa.healthplan.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger. Declara el esquema de seguridad (HTTP Basic)
 * para que Swagger UI permita autenticarse y probar los endpoints protegidos.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ms-healthplan API",
                version = "0.1.0",
                description = "Herramienta interna para tasar y vender planes de obra social: "
                        + "gestión de planes y precios, clientes con su grupo familiar y "
                        + "simulación de cotizaciones por edad.",
                license = @License(name = "Apache 2.0"),
                contact = @Contact(name = "Ramiro Foschini",
                        url = "https://www.linkedin.com/in/ramirofoschini",
                        email = "foschiniramiro@gmail.com")
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class SwaggerConfiguration {

}
