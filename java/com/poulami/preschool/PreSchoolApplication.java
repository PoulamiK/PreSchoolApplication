package com.poulami.preschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.poulami.preschool.repository")
@EntityScan("com.poulami.preschool.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class PreSchoolApplication {

    public static void main(String[] args) {

        SpringApplication.run(PreSchoolApplication.class, args);
    }

}
