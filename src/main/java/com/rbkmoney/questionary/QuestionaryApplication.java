package com.rbkmoney.questionary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class QuestionaryApplication extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionaryApplication.class, args);
    }

}
