package com.ly.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ly.interview.*")
public class JavaDailyInterviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaDailyInterviewApplication.class, args);
    }

}
