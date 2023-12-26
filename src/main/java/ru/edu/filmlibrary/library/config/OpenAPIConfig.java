package ru.edu.filmlibrary.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;


@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI libraryProject() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Онлайн Фильмотеки")
                        .description("Сервис, позволяющий арендовать фильм")
                        .version("0.1")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                        .contact(new Contact().name("Aleksandr Fedorov")));
    }
}
