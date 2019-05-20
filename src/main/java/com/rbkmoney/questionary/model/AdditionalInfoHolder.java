package com.rbkmoney.questionary.model;

import com.rbkmoney.questionary.domain.tables.pojos.AdditionalInfo;
import com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo;
import com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class AdditionalInfoHolder {

    private AdditionalInfo additionalInfo;

    private List<FinancialPosition> financialPositionList;

    private List<BusinessInfo> businessInfoList;

}
