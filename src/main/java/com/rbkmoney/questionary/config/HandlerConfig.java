package com.rbkmoney.questionary.config;

import com.rbkmoney.questionary.handler.QuestionaryHandler;
import com.rbkmoney.questionary.manage.QuestionaryManagerSrv;
import com.rbkmoney.questionary.service.QuestionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfig {

    @Bean
    @Autowired
    public QuestionaryManagerSrv.Iface questionaryHandler(QuestionaryService questionaryService) {
        return new QuestionaryHandler(questionaryService);
    }

}
