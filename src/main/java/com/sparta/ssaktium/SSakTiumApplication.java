package com.sparta.ssaktium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableJpaAuditing
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableElasticsearchRepositories(basePackages = "com.sparta.ssaktium.domain.boards.repository")
public class SSakTiumApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSakTiumApplication.class, args);
    }

}
