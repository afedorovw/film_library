package ru.edu.filmlibrary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeworksApplication implements CommandLineRunner {

    @Value("${server.port}")
    private String port;

    public static void main(String[] args) {
        SpringApplication.run(HomeworksApplication.class, args);
    }

    @Override
    public void run(String... args) {

        System.out.println("Swagger path: http://localhost:" + port + "/swagger-ui/index.html");

    }
}
