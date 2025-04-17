package com.allieat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AllieatApplication {
    public static void main(String[] args) {
        SpringApplication.run(AllieatApplication.class, args);
    }
}
