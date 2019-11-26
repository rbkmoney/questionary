package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.tables.pojos.LegalEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.LegalOwner;
import com.rbkmoney.questionary.model.AdditionalInfoHolder;
import com.rbkmoney.questionary.model.LegalEntityQuestionaryHolder;
import com.rbkmoney.questionary.util.ThriftUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LegalEntityQuestionaryConverter implements ThriftConverter<RussianLegalEntity, LegalEntityQuestionaryHolder>,
        JooqConverter<LegalEntityQuestionaryHolder, RussianLegalEntity> {

    @Override
    public RussianLegalEntity toThrift(LegalEntityQuestionaryHolder value, ThriftConverterContext ctx) {
        final RussianLegalEntity russianLegalEntity = new RussianLegalEntity();
        russianLegalEntity.setName(value.getLegalEntityQuestionary().getName());
        russianLegalEntity.setForeignName(value.getLegalEntityQuestionary().getForeignName());
        russianLegalEntity.setLegalForm(value.getLegalEntityQuestionary().getLegalForm());
        russianLegalEntity.setInn(value.getQuestionary().getInn());

        final RegistrationInfo registrationInfo = new RegistrationInfo();
        final LegalRegistrationInfo legalRegistrationInfo = new LegalRegistrationInfo();
        legalRegistrationInfo.setOgrn(value.getLegalEntityQuestionary().getOgrn());
        legalRegistrationInfo.setRegistrationPlace(value.getQuestionary().getRegPlace());
        if (value.getQuestionary().getRegDate() != null) {
            legalRegistrationInfo.setRegistrationDate(TypeUtil.temporalToString(value.getQuestionary().getRegDate()));
        }
        legalRegistrationInfo.setActualAddress(value.getLegalEntityQuestionary().getRegActualAddress());
        legalRegistrationInfo.setRegistrationAddress(value.getLegalEntityQuestionary().getRegAddress());
        registrationInfo.setLegalRegistrationInfo(legalRegistrationInfo);
        ThriftUtil.setIfNotEmpty(legalRegistrationInfo, registrationInfo::setLegalRegistrationInfo);
        ThriftUtil.setIfNotEmpty(registrationInfo, russianLegalEntity::setRegistrationInfo);

        russianLegalEntity.setAdditionalSpace(value.getLegalEntityQuestionary().getAdditionalSpace());

        Activity activity = ctx.convert(value.getQuestionary(), Activity.class);
        ThriftUtil.setIfNotEmpty(activity, russianLegalEntity::setPrincipalActivity);

        russianLegalEntity.setOkatoCode(value.getLegalEntityQuestionary().getOkatoCode());
        russianLegalEntity.setOkpoCode(value.getLegalEntityQuestionary().getOkpoCode());
        russianLegalEntity.setPostalAddress(value.getLegalEntityQuestionary().getPostalAddress());

        if (value.getQuestionary().getPropertyInfoDocType() != null) {
            PropertyInfoDocumentType propertyInfoDocumentType = ctx.convert(value.getQuestionary(), PropertyInfoDocumentType.class);
            russianLegalEntity.setPropertyInfoDocumentType(propertyInfoDocumentType);
        }

        final FoundersInfo foundersInfo = new FoundersInfo();
        if (value.getFounderList() != null) {
            final List<com.rbkmoney.questionary.Founder> founderList = value.getFounderList().stream()
                    .map(founder -> ctx.convert(founder, Founder.class))
                    .collect(Collectors.toList());
            if (!founderList.isEmpty()) {
                foundersInfo.setFounders(founderList);
            }
        }
        if (value.getHeadList() != null) {
            final List<com.rbkmoney.questionary.Head> headList = value.getHeadList().stream()
                    .map(head -> ctx.convert(head, Head.class))
                    .collect(Collectors.toList());
            if (!headList.isEmpty()) {
                foundersInfo.setHeads(headList);
            }
        }

        if (value.getLegalOwner() != null) {
            final LegalOwnerInfo legalOwnerInfo = ctx.convert(value.getLegalOwner(), LegalOwnerInfo.class);
            ThriftUtil.setIfNotEmpty(legalOwnerInfo, russianLegalEntity::setLegalOwnerInfo);
        }

        final Head head = new Head();
        final IndividualPerson individualPerson = new IndividualPerson();
        individualPerson.setInn(value.getLegalEntityQuestionary().getFounderOwnerInn());
        individualPerson.setFio(value.getLegalEntityQuestionary().getFounderOwnerFio());
        ThriftUtil.setIfNotEmpty(individualPerson, head::setIndividualPerson);
        head.setPosition(value.getLegalEntityQuestionary().getFounderOwnerPosition());
        ThriftUtil.setIfNotEmpty(head, foundersInfo::setLegalOwner);
        ThriftUtil.setIfNotEmpty(foundersInfo, russianLegalEntity::setFoundersInfo);

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
        ThriftUtil.setIfNotEmpty(licenseInfo, russianLegalEntity::setLicenseInfo);

        final ResidencyInfo residencyInfo = new ResidencyInfo();
        LegalResidencyInfo legalResidencyInfo = new LegalResidencyInfo();
        legalResidencyInfo.setTaxResident(value.getLegalEntityQuestionary().getTaxResident());
        legalResidencyInfo.setOwnerResident(value.getLegalEntityQuestionary().getOwnerResident());
        legalResidencyInfo.setFatca(value.getLegalEntityQuestionary().getFatca());
        ThriftUtil.setIfNotEmpty(legalResidencyInfo, residencyInfo::setLegalResidencyInfo);
        ThriftUtil.setIfNotEmpty(residencyInfo, russianLegalEntity::setResidencyInfo);

        if (value.getBeneficialOwnerList() != null) {
            final List<BeneficialOwner> beneficialOwnerList = value.getBeneficialOwnerList().stream()
                    .map(beneficialOwner -> ctx.convert(beneficialOwner, BeneficialOwner.class))
                    .collect(Collectors.toList());
            if (!beneficialOwnerList.isEmpty()) {
                russianLegalEntity.setBeneficialOwners(beneficialOwnerList);
            }
        }

        if (value.getAdditionalInfoHolder() != null) {
            final AdditionalInfo additionalInfo = ctx.convert(value.getAdditionalInfoHolder(), AdditionalInfo.class);
            ThriftUtil.setIfNotEmpty(additionalInfo, russianLegalEntity::setAdditionalInfo);
        }

        return russianLegalEntity;
    }

    @Override
    public LegalEntityQuestionaryHolder toJooq(RussianLegalEntity value, JooqConverterContext ctx) {
        final LegalEntityQuestionary legalEntityQuestionary = new LegalEntityQuestionary();
        legalEntityQuestionary.setName(value.getName());
        legalEntityQuestionary.setAdditionalSpace(value.getAdditionalSpace());
        legalEntityQuestionary.setForeignName(value.getForeignName());
        if (value.isSetResidencyInfo() && value.getResidencyInfo().isSetLegalResidencyInfo()) {
            legalEntityQuestionary.setOwnerResident(value.getResidencyInfo().getLegalResidencyInfo().isOwnerResident());
            legalEntityQuestionary.setFatca(value.getResidencyInfo().getLegalResidencyInfo().isFatca());
            legalEntityQuestionary.setTaxResident(value.getResidencyInfo().getLegalResidencyInfo().isTaxResident());
        }
        if (value.isSetFoundersInfo() && value.getFoundersInfo().isSetLegalOwner()) {
            legalEntityQuestionary.setFounderOwnerPosition(value.getFoundersInfo().getLegalOwner().getPosition());
            legalEntityQuestionary.setFounderOwnerInn(value.getFoundersInfo().getLegalOwner().getIndividualPerson().getInn());
            legalEntityQuestionary.setFounderOwnerFio(value.getFoundersInfo().getLegalOwner().getIndividualPerson().getFio());
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
            legalOwnerInfo = ctx.convert(value.getLegalOwnerInfo(), LegalOwner.class);
        }

        List<com.rbkmoney.questionary.domain.tables.pojos.Founder> founderList = null;
        if (value.isSetFoundersInfo() && value.getFoundersInfo().isSetFounders()) {
            founderList = value.getFoundersInfo().getFounders().stream()
                    .map(founder -> ctx.convert(founder, com.rbkmoney.questionary.domain.tables.pojos.Founder.class))
                    .collect(Collectors.toList());
        }

        List<com.rbkmoney.questionary.domain.tables.pojos.Head> headList = null;
        if (value.isSetFoundersInfo() && value.getFoundersInfo().isSetHeads()) {
            headList = value.getFoundersInfo().getHeads().stream()
                    .map(head -> ctx.convert(head, com.rbkmoney.questionary.domain.tables.pojos.Head.class))
                    .collect(Collectors.toList());
        }

        if (value.isSetFoundersInfo() && value.getFoundersInfo().isSetLegalOwner()) {
            final Head legalOwner = value.getFoundersInfo().getLegalOwner();
            if (legalOwner != null) {
                if (legalOwner.isSetIndividualPerson()) {
                    legalEntityQuestionary.setFounderOwnerInn(legalOwner.getIndividualPerson().getInn());
                    if (legalOwner.getIndividualPerson().isSetFio()) {
                        legalEntityQuestionary.setFounderOwnerPosition(legalOwner.getPosition());
                        legalEntityQuestionary.setFounderOwnerFio(legalOwner.getIndividualPerson().getFio());
                    }
                }
                legalEntityQuestionary.setFounderOwnerPosition(legalOwner.getPosition());
            }
        }

        List<com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner> beneficialOwnerHolderList = null;
        if (value.isSetBeneficialOwners()) {
            beneficialOwnerHolderList = value.getBeneficialOwners().stream()
                    .map(beneficialOwner -> ctx.convert(beneficialOwner, com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner.class))
                    .collect(Collectors.toList());
        }

        AdditionalInfoHolder additionalInfoHolder = null;
        if (value.isSetAdditionalInfo()) {
            additionalInfoHolder = ctx.convert(value.getAdditionalInfo(), AdditionalInfoHolder.class);
        }

        return LegalEntityQuestionaryHolder.builder()
                .legalEntityQuestionary(legalEntityQuestionary)
                .legalOwner(legalOwnerInfo)
                .founderList(founderList)
                .headList(headList)
                .additionalInfoHolder(additionalInfoHolder)
                .beneficialOwnerList(beneficialOwnerHolderList)
                .build();
    }
}
