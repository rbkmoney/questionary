package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.questionary.Activity;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import org.springframework.stereotype.Component;

@Component
public class ActivityConverter implements ThriftConverter<Activity, Questionary> {

    @Override
    public Activity toThrift(Questionary value, ThriftConverterContext ctx) {
        Activity activity = new Activity();
        activity.setCode(value.getOkvd());
        activity.setDescription(value.getActivityType());

        return activity;
    }

}
