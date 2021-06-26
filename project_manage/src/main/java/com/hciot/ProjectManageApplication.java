package com.hciot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ProjectManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManageApplication.class, args);
    }

}
