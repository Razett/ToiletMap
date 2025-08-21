package com.razett.toiletmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ToiletMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToiletMapApplication.class, args);
    }

}
