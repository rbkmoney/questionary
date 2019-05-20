package com.rbkmoney.questionary.model;

import com.rbkmoney.questionary.domain.tables.pojos.IndividualEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.PropertyInfo;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class IndividualEntityQuestionaryHolder {

    private Questionary questionary;

    private IndividualEntityQuestionary individualEntityQuestionary;

    private List<PropertyInfo> propertyInfoList;

    private AdditionalInfoHolder additionalInfoHolder;

}
