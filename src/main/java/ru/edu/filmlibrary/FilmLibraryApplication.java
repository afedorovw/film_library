package ru.edu.filmlibrary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmLibraryApplication implements CommandLineRunner {

    @Value("${server.port}")
    private String port;

    @Value("${server.address}")
    private String url;

    public static void main(String[] args) {
        SpringApplication.run(FilmLibraryApplication.class, args);
    }

    @Override
    public void run(String... args) {

        System.out.println("Swagger path: http://" + url + ":" + port + "/swagger-ui/index.html\n" +
                "UI: http://" + url + ":" + port);
    }
}


