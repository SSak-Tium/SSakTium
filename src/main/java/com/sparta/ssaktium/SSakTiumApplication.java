package com.sparta.ssaktium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SSakTiumApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSakTiumApplication.class, args);
    }

}
