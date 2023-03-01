package com.sa.healthplan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Microservice HealthPlan",
                version = "1.1",
                description = "Microservicio con artquitectura RestFull desarrollado con Springboot",
                license = @License(name = "Apache 2.0", url = "http://foo.bar"),
                contact = @Contact(url = "", name = "Ramiro Foschini", email = "foschiniramiro@gmail.com")
        )
)
public class HealthplanApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthplanApplication.class, args);
    }

}
