package com.rbkmoney.questionary.converter;


import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.domain.enums.RelationProcess;
import com.rbkmoney.questionary.model.AdditionalInfoHolder;

import java.util.List;
import java.util.stream.Collectors;

public class AdditionalInfoConverter implements ThriftConverter<AdditionalInfoHolder, AdditionalInfo> {

    private final FinancialPositionConverter financialPositionConverter;
    private final BusinessInfoConverter businessInfoConverter;

    public AdditionalInfoConverter() {
        this.financialPositionConverter = new FinancialPositionConverter();
        this.businessInfoConverter = new BusinessInfoConverter();
    }

    @Override
    public AdditionalInfo convertToThrift(AdditionalInfoHolder value) {
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
            additionalInfo.setStorageFacilities(value.getAdditionalInfo().getStorageFacilities());
            additionalInfo.setRelationshipWithNKO(value.getAdditionalInfo().getRelationshipWithNko());
            additionalInfo.setNKORelationTarget(value.getAdditionalInfo().getNkoRelationTarget());
            additionalInfo.setAccountingOrg(value.getAdditionalInfo().getAccountingOrg());
            additionalInfo.setAccounting(value.getAdditionalInfo().getAccounting());
            additionalInfo.setStaffCount(value.getAdditionalInfo().getStaffCount());
            additionalInfo.setHasAccountant(value.getAdditionalInfo().getHasAccountant());
            additionalInfo.setBenefitThirdParties(value.getAdditionalInfo().getBenefitThirdParties());
        }

        if (value.getBusinessInfoList() != null) {
            final List<BusinessInfo> businessInfoList = value.getBusinessInfoList().stream()
                    .map(businessInfoConverter::convertToThrift)
                    .collect(Collectors.toList());
            additionalInfo.setBusinessInfo(businessInfoList);
        }

        if (value.getFinancialPositionList() != null) {
            final List<FinancialPosition> financialPositionList = value.getFinancialPositionList().stream()
                    .map(financialPositionConverter::convertToThrift)
                    .collect(Collectors.toList());
            additionalInfo.setFinancialPosition(financialPositionList);
        }

        return additionalInfo;
    }

    @Override
    public AdditionalInfoHolder convertFromThrift(AdditionalInfo value) {
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
        additionalInfo.setAccounting(value.getAccounting());
        additionalInfo.setAccountingOrg(value.getAccountingOrg());
        additionalInfo.setBenefitThirdParties(value.isBenefitThirdParties());
        additionalInfo.setStaffCount(value.getStaffCount());
        additionalInfo.setHasAccountant(value.isHasAccountant());
        additionalInfo.setNkoRelationTarget(value.getNKORelationTarget());
        additionalInfo.setRelationshipWithNko(value.getRelationshipWithNKO());
        additionalInfo.setStorageFacilities(value.isStorageFacilities());
        additionalInfo.setCounterparties(value.getMainCounterparties());

        List<com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo> businessInfoList = null;
        if (value.isSetBusinessInfo()) {
            businessInfoList = value.getBusinessInfo().stream()
                    .map(businessInfoConverter::convertFromThrift)
                    .collect(Collectors.toList());
        }

        List<com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition> financialPositionList = null;
        if (value.isSetFinancialPosition()) {
            financialPositionList = value.getFinancialPosition().stream()
                    .map(financialPositionConverter::convertFromThrift)
                    .collect(Collectors.toList());
        }

        return AdditionalInfoHolder.builder()
                .additionalInfo(additionalInfo)
                .financialPositionList(financialPositionList)
                .businessInfoList(businessInfoList)
                .build();
    }
}
