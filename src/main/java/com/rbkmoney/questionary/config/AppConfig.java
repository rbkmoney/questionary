package com.rbkmoney.questionary.config;

import com.rbkmoney.questionary.service.QuestionaryService;
import com.rbkmoney.questionary.service.impl.QuestionaryServiceMdcDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {

    @Bean
    @Primary
    @Autowired
    public QuestionaryServiceMdcDecorator questionaryServiceMdcDecorator(QuestionaryService questionaryService) {
        return new QuestionaryServiceMdcDecorator(questionaryService);
    }

}
