package com.rbkmoney.questionary.converter.questionary;


import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.enums.RelationProcess;
import com.rbkmoney.questionary.model.AdditionalInfoHolder;
import com.rbkmoney.questionary.util.ThriftUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdditionalInfoConverter implements ThriftConverter<AdditionalInfo, AdditionalInfoHolder>,
        JooqConverter<AdditionalInfoHolder, AdditionalInfo> {

    @Override
    public AdditionalInfo toThrift(AdditionalInfoHolder value, ThriftConverterContext ctx) {
        AdditionalInfo additionalInfo = new AdditionalInfo();
        if (value.getAdditionalInfo() != null) {
            if (value.getAdditionalInfo().getBusinessReputation() != null) {
                BusinessReputation businessReputation = Enum.valueOf(BusinessReputation.class,
                        value.getAdditionalInfo().getBusinessReputation().name());
                additionalInfo.setBusinessReputation(businessReputation);
            }
            if (value.getAdditionalInfo().getRelationProcess() != null) {
                RelationIndividualEntity relationIndividualEntity = Enum.valueOf(RelationIndividualEntity.class,
                        value.getAdditionalInfo().getRelationProcess().name());
                additionalInfo.setRelationIndividualEntity(relationIndividualEntity);
            }
            if (value.getAdditionalInfo().getMonthOperationSum() != null) {
                MonthOperationSum monthOperationSum = Enum.valueOf(MonthOperationSum.class,
                        value.getAdditionalInfo().getMonthOperationSum().name());
                additionalInfo.setMonthOperationSum(monthOperationSum);
            }
            if (value.getAdditionalInfo().getMonthOperationCount() != null) {
                MonthOperationCount monthOperationCount = Enum.valueOf(MonthOperationCount.class,
                        value.getAdditionalInfo().getMonthOperationCount().name());
                additionalInfo.setMonthOperationCount(monthOperationCount);
            }
            additionalInfo.setMainCounterparties(value.getAdditionalInfo().getCounterparties());
            additionalInfo.setRelationshipWithNKO(value.getAdditionalInfo().getRelationshipWithNko());
            additionalInfo.setNKORelationTarget(value.getAdditionalInfo().getNkoRelationTarget());
            additionalInfo.setStaffCount(value.getAdditionalInfo().getStaffCount());
            additionalInfo.setBenefitThirdParties(value.getAdditionalInfo().getBenefitThirdParties());

            AccountantInfo accountantInfo = ctx.convert(value.getAdditionalInfo(), AccountantInfo.class);
            ThriftUtil.setIfNotEmpty(accountantInfo, additionalInfo::setAccountantInfo);
        }

        if (value.getBusinessInfoList() != null) {
            final List<BusinessInfo> businessInfoList = value.getBusinessInfoList().stream()
                    .map(businessInfo -> ctx.convert(businessInfo, BusinessInfo.class))
                    .collect(Collectors.toList());
            if (!businessInfoList.isEmpty()) {
                additionalInfo.setBusinessInfo(businessInfoList);
            }
        }

        if (value.getFinancialPositionList() != null) {
            List<FinancialPosition> financialPositionList = value.getFinancialPositionList().stream()
                    .map(financialPosition -> ctx.convert(financialPosition, FinancialPosition.class))
                    .collect(Collectors.toList());
            if (!financialPositionList.isEmpty()) {
                additionalInfo.setFinancialPosition(financialPositionList);
            }
        }

        return additionalInfo;
    }

    @Override
    public AdditionalInfoHolder toJooq(AdditionalInfo value, JooqConverterContext ctx) {
        com.rbkmoney.questionary.domain.tables.pojos.AdditionalInfo additionalInfo =
                new com.rbkmoney.questionary.domain.tables.pojos.AdditionalInfo();
        if (value.isSetBusinessReputation()) {
            com.rbkmoney.questionary.domain.enums.BusinessReputation businessReputation =
                    Enum.valueOf(com.rbkmoney.questionary.domain.enums.BusinessReputation.class, value.getBusinessReputation().name());
            additionalInfo.setBusinessReputation(businessReputation);
        }
        if (value.isSetRelationIndividualEntity()) {
            RelationProcess relationProcess = Enum.valueOf(RelationProcess.class, value.getRelationIndividualEntity().name());
            additionalInfo.setRelationProcess(relationProcess);
        }
        if (value.isSetMonthOperationCount()) {
            com.rbkmoney.questionary.domain.enums.MonthOperationCount monthOperationCount =
                    Enum.valueOf(com.rbkmoney.questionary.domain.enums.MonthOperationCount.class, value.getMonthOperationCount().name());
            additionalInfo.setMonthOperationCount(monthOperationCount);
        }
        if (value.isSetMonthOperationSum()) {
            com.rbkmoney.questionary.domain.enums.MonthOperationSum monthOperationSum =
                    Enum.valueOf(com.rbkmoney.questionary.domain.enums.MonthOperationSum.class, value.getMonthOperationSum().name());
            additionalInfo.setMonthOperationSum(monthOperationSum);
        }
        additionalInfo.setBenefitThirdParties(value.isBenefitThirdParties());
        additionalInfo.setStaffCount(value.getStaffCount());
        additionalInfo.setNkoRelationTarget(value.getNKORelationTarget());
        additionalInfo.setRelationshipWithNko(value.getRelationshipWithNKO());
        additionalInfo.setCounterparties(value.getMainCounterparties());

        List<com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo> businessInfoList = null;
        if (value.isSetBusinessInfo()) {
            businessInfoList = value.getBusinessInfo().stream()
                    .map(businessInfo -> ctx.convert(businessInfo, com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo.class))
                    .collect(Collectors.toList());
        }

        List<com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition> financialPositionList = null;
        if (value.isSetFinancialPosition()) {
            financialPositionList = value.getFinancialPosition().stream()
                    .map(financialPosition -> ctx.convert(financialPosition, com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition.class))
                    .collect(Collectors.toList());
        }

        if (value.getAccountantInfo() != null) {
            ctx.fill(additionalInfo, value.getAccountantInfo());
        }

        return AdditionalInfoHolder.builder()
                .additionalInfo(additionalInfo)
                .financialPositionList(financialPositionList)
                .businessInfoList(businessInfoList)
                .build();
    }
}
