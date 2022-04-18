package com.ecinema.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECinemaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ECinemaApplication.class, args);
    }

    @Override
    public void run(String... args) {}

}
