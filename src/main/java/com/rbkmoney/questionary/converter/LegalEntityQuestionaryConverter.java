package com.rbkmoney.questionary.converter;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.domain.tables.pojos.LegalEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.LegalOwner;
import com.rbkmoney.questionary.domain.tables.pojos.PropertyInfo;
import com.rbkmoney.questionary.model.AdditionalInfoHolder;
import com.rbkmoney.questionary.model.LegalEntityQuestionaryHolder;

import java.util.List;
import java.util.stream.Collectors;

public class LegalEntityQuestionaryConverter implements ThriftConverter<LegalEntityQuestionaryHolder, RussianLegalEntity> {

    private final LegalOwnerConverter legalOwnerConverter;
    private final FounderConverter founderConverter;
    private final HeadConverter headConverter;
    private final AdditionalInfoConverter additionalInfoConverter;
    private final BeneficialOwnerConverter beneficialOwnerConverter;

    LegalEntityQuestionaryConverter() {
        this.legalOwnerConverter = new LegalOwnerConverter();
        this.founderConverter = new FounderConverter();
        this.headConverter = new HeadConverter();
        this.additionalInfoConverter = new AdditionalInfoConverter();
        this.beneficialOwnerConverter = new BeneficialOwnerConverter();
    }

    @Override
    public RussianLegalEntity convertToThrift(LegalEntityQuestionaryHolder value) {
        final RussianLegalEntity russianLegalEntity = new RussianLegalEntity();
        russianLegalEntity.setName(value.getLegalEntityQuestionary().getName());
        russianLegalEntity.setForeignName(value.getLegalEntityQuestionary().getForeignName());
        russianLegalEntity.setLegalForm(value.getLegalEntityQuestionary().getLegalForm());
        russianLegalEntity.setInn(value.getQuestionary().getInn());

        final RegistrationInfo registrationInfo = new RegistrationInfo();
        LegalRegistrationInfo legalRegistrationInfo = new LegalRegistrationInfo();
        legalRegistrationInfo.setOgrn(value.getLegalEntityQuestionary().getOgrn());
        legalRegistrationInfo.setRegistrationPlace(value.getQuestionary().getRegPlace());
        legalRegistrationInfo.setRegistrationDate(TypeUtil.temporalToString(value.getQuestionary().getRegDate()));
        legalRegistrationInfo.setActualAddress(value.getLegalEntityQuestionary().getRegActualAddress());
        legalRegistrationInfo.setRegistrationAddress(value.getLegalEntityQuestionary().getRegAddress());
        registrationInfo.setLegalRegistrationInfo(legalRegistrationInfo);

        russianLegalEntity.setRegistrationInfo(registrationInfo);

        russianLegalEntity.setAdditionalSpace(value.getLegalEntityQuestionary().getAdditionalSpace());

        if (value.getPropertyInfoList() != null) {
            final List<String> propertyList = value.getPropertyInfoList().stream()
                    .map(PropertyInfo::getDescription)
                    .collect(Collectors.toList());
            russianLegalEntity.setPropertyInfo(propertyList);
        }

        russianLegalEntity.setOkatoCode(value.getLegalEntityQuestionary().getOkatoCode());
        russianLegalEntity.setOkpoCode(value.getLegalEntityQuestionary().getOkpoCode());
        russianLegalEntity.setPostalAddress(value.getLegalEntityQuestionary().getPostalAddress());

        final FoundersInfo foundersInfo = new FoundersInfo();
        if (value.getFounderList() != null) {
            final List<com.rbkmoney.questionary.Founder> founderList = value.getFounderList().stream()
                    .map(founderConverter::convertToThrift)
                    .collect(Collectors.toList());
            foundersInfo.setFounders(founderList);
        }
        if (value.getHeadList() != null) {
            final List<com.rbkmoney.questionary.Head> headList = value.getHeadList().stream()
                    .map(headConverter::convertToThrift)
                    .collect(Collectors.toList());
            foundersInfo.setHeads(headList);
        }
        if (value.getHead() != null) {
            foundersInfo.setLegalOwner(headConverter.convertToThrift(value.getHead()));
        }
        russianLegalEntity.setFoundersInfo(foundersInfo);

        final LicenseInfo licenseInfo = new LicenseInfo();
        if (value.getQuestionary().getLicenseIssueDate() != null) {
            licenseInfo.setIssueDate(TypeUtil.temporalToString(value.getQuestionary().getLicenseIssueDate()));
        }
        if (value.getQuestionary().getLicenseEffectiveDate() != null) {
            licenseInfo.setEffectiveDate(TypeUtil.temporalToString(value.getQuestionary().getLicenseEffectiveDate()));
        }
        if (value.getQuestionary().getLicenseExpirationDate() != null) {
            licenseInfo.setExpirationDate(TypeUtil.temporalToString(value.getQuestionary().getLicenseExpirationDate()));
        }
        licenseInfo.setLicensedActivity(value.getQuestionary().getLicenseLicensedActivity());
        licenseInfo.setOfficialNum(value.getQuestionary().getLicenseOfficialNum());
        licenseInfo.setIssuerName(value.getQuestionary().getLicenseIssuerName());
        russianLegalEntity.setLicenseInfo(licenseInfo);

        final ResidencyInfo residencyInfo = new ResidencyInfo();
        LegalResidencyInfo legalResidencyInfo = new LegalResidencyInfo();
        legalResidencyInfo.setTaxResident(value.getQuestionary().getTaxResident());
        legalResidencyInfo.setOwnerResident(value.getLegalEntityQuestionary().getOwnerResident());
        legalResidencyInfo.setFatca(value.getLegalEntityQuestionary().getFatca());
        residencyInfo.setLegalResidencyInfo(legalResidencyInfo);
        russianLegalEntity.setResidencyInfo(residencyInfo);

        if (value.getBeneficialOwnerList() != null) {
            final List<BeneficialOwner> beneficialOwnerList = value.getBeneficialOwnerList().stream()
                    .map(beneficialOwnerConverter::convertToThrift)
                    .collect(Collectors.toList());
            russianLegalEntity.setBeneficialOwners(beneficialOwnerList);
        }

        if (value.getAdditionalInfoHolder() != null) {
            russianLegalEntity.setAdditionalInfo(additionalInfoConverter.convertToThrift(value.getAdditionalInfoHolder()));
        }

        return russianLegalEntity;
    }

    @Override
    public LegalEntityQuestionaryHolder convertFromThrift(RussianLegalEntity value) {
        final LegalEntityQuestionary legalEntityQuestionary = new LegalEntityQuestionary();
        legalEntityQuestionary.setName(value.getName());
        legalEntityQuestionary.setAdditionalSpace(value.getAdditionalSpace());
        legalEntityQuestionary.setForeignName(value.getForeignName());
        if (value.isSetResidencyInfo() && value.getResidencyInfo().isSetLegalResidencyInfo()) {
            legalEntityQuestionary.setOwnerResident(value.getResidencyInfo().getLegalResidencyInfo().isOwnerResident());
            legalEntityQuestionary.setFatca(value.getResidencyInfo().getLegalResidencyInfo().isFatca());
        }
        if (value.isSetFoundersInfo() && value.getFoundersInfo().isSetLegalOwner()) {
            legalEntityQuestionary.setFounderOwnerPosition(value.getFoundersInfo().getLegalOwner().getPosition());
            legalEntityQuestionary.setFounderOwnerInn(value.getFoundersInfo().getLegalOwner().getIndividualPerson().getInn());
            legalEntityQuestionary.setFounderOwnerFirstName(value.getFoundersInfo().getLegalOwner().getIndividualPerson().getFio().getFirstName());
            legalEntityQuestionary.setFounderOwnerSecondName(value.getFoundersInfo().getLegalOwner().getIndividualPerson().getFio().getSecondName());
            legalEntityQuestionary.setFounderOwnerMiddleName(value.getFoundersInfo().getLegalOwner().getIndividualPerson().getFio().getMiddleName());
        }
        legalEntityQuestionary.setLegalForm(value.getLegalForm());
        if (value.isSetRegistrationInfo() && value.getRegistrationInfo().isSetLegalRegistrationInfo()) {
            legalEntityQuestionary.setOgrn(value.getRegistrationInfo().getLegalRegistrationInfo().getOgrn());
            legalEntityQuestionary.setRegAddress(value.getRegistrationInfo().getLegalRegistrationInfo().getRegistrationAddress());
            legalEntityQuestionary.setRegActualAddress(value.getRegistrationInfo().getLegalRegistrationInfo().getActualAddress());
        }
        legalEntityQuestionary.setOkatoCode(value.getOkatoCode());
        legalEntityQuestionary.setOkpoCode(value.getOkpoCode());
        legalEntityQuestionary.setPostalAddress(value.getPostalAddress());

        LegalOwner legalOwnerInfo = null;
        if (value.isSetLegalOwnerInfo()) {
            legalOwnerInfo = legalOwnerConverter.convertFromThrift(value.getLegalOwnerInfo());
        }

        List<com.rbkmoney.questionary.domain.tables.pojos.Founder> founderList = null;
        if (value.isSetFoundersInfo() && value.getFoundersInfo().isSetFounders()) {
            founderList = value.getFoundersInfo().getFounders().stream()
                    .map(founderConverter::convertFromThrift)
                    .collect(Collectors.toList());
        }

        List<com.rbkmoney.questionary.domain.tables.pojos.Head> headList = null;
        if (value.isSetFoundersInfo() && value.getFoundersInfo().isSetHeads()) {
            headList = value.getFoundersInfo().getHeads().stream()
                    .map(headConverter::convertFromThrift)
                    .collect(Collectors.toList());
        }

        com.rbkmoney.questionary.domain.tables.pojos.Head legalOwnerHead = null;
        if (value.isSetFoundersInfo() && value.getFoundersInfo().isSetLegalOwner()) {
            legalOwnerHead = headConverter.convertFromThrift(value.getFoundersInfo().getLegalOwner());
        }

        List<PropertyInfo> propertyInfoList = null;
        if (value.isSetPropertyInfo()) {
            propertyInfoList = value.getPropertyInfo().stream()
                    .map(s -> {
                        PropertyInfo propertyInfo = new PropertyInfo();
                        propertyInfo.setDescription(s);
                        return propertyInfo;
                    })
                    .collect(Collectors.toList());
        }

        List<com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner> beneficialOwnerHolderList = null;
        if (value.isSetBeneficialOwners()) {
            beneficialOwnerHolderList = value.getBeneficialOwners().stream()
                    .map(beneficialOwnerConverter::convertFromThrift)
                    .collect(Collectors.toList());
        }

        AdditionalInfoHolder additionalInfoHolder = null;
        if (value.isSetAdditionalInfo()) {
            additionalInfoHolder = additionalInfoConverter.convertFromThrift(value.getAdditionalInfo());
        }

        return LegalEntityQuestionaryHolder.builder()
                .legalEntityQuestionary(legalEntityQuestionary)
                .legalOwner(legalOwnerInfo)
                .founderList(founderList)
                .headList(headList)
                .head(legalOwnerHead)
                .propertyInfoList(propertyInfoList)
                .additionalInfoHolder(additionalInfoHolder)
                .beneficialOwnerList(beneficialOwnerHolderList)
                .build();
    }
}
