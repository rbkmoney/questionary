package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.enums.IdentityDocumentType;
import com.rbkmoney.questionary.domain.tables.pojos.IndividualEntityQuestionary;
import com.rbkmoney.questionary.model.AdditionalInfoHolder;
import com.rbkmoney.questionary.model.IndividualEntityQuestionaryHolder;
import com.rbkmoney.questionary.util.ThriftUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IndividualEntityQuestionaryConverter implements ThriftConverter<RussianIndividualEntity, IndividualEntityQuestionaryHolder>,
        JooqConverter<IndividualEntityQuestionaryHolder, RussianIndividualEntity> {

    @Override
    public RussianIndividualEntity toThrift(IndividualEntityQuestionaryHolder value, ThriftConverterContext ctx) {
        final RussianIndividualEntity russianIndividualEntity = new RussianIndividualEntity();

        russianIndividualEntity.setName(value.getIndividualEntityQuestionary().getName());
        russianIndividualEntity.setSnils(value.getIndividualEntityQuestionary().getSnils());
        russianIndividualEntity.setInn(value.getQuestionary().getInn());
        russianIndividualEntity.setPdlCategory(value.getIndividualEntityQuestionary().getPdlCategory());
        russianIndividualEntity.setPdlRelationDegree(value.getIndividualEntityQuestionary().getPdlRelationDegree());
        russianIndividualEntity.setHasBeneficialOwners(value.getQuestionary().getHasBeneficialOwners());

        final Activity activity = ctx.convert(value.getQuestionary(), Activity.class);

        ThriftUtil.setIfNotEmpty(activity, russianIndividualEntity::setPrincipalActivity);

        final RegistrationInfo registrationInfo = new RegistrationInfo();
        IndividualRegistrationInfo individualRegistrationInfo = new IndividualRegistrationInfo();
        individualRegistrationInfo.setOgrnip(value.getIndividualEntityQuestionary().getOgrnip());
        individualRegistrationInfo.setRegistrationPlace(value.getQuestionary().getRegPlace());
        if (value.getQuestionary().getRegDate() != null) {
            individualRegistrationInfo.setRegistrationDate(TypeUtil.temporalToString(value.getQuestionary().getRegDate()));
        }
        ThriftUtil.setIfNotEmpty(individualRegistrationInfo, registrationInfo::setIndividualRegistrationInfo);
        ThriftUtil.setIfNotEmpty(registrationInfo, russianIndividualEntity::setRegistrationInfo);

        final ResidencyInfo residencyInfo = new ResidencyInfo();
        IndividualResidencyInfo individualResidencyInfo = new IndividualResidencyInfo();
        individualResidencyInfo.setUsaTaxResident(value.getIndividualEntityQuestionary().getUsaTaxResident());
        individualResidencyInfo.setExceptUsaTaxResident(value.getIndividualEntityQuestionary().getExceptUsaTaxResident());
        ThriftUtil.setIfNotEmpty(individualResidencyInfo, residencyInfo::setIndividualResidencyInfo);
        ThriftUtil.setIfNotEmpty(residencyInfo, russianIndividualEntity::setResidencyInfo);

        final IndividualPersonCategories individualPersonCategories = new IndividualPersonCategories();
        individualPersonCategories.setBeneficialOwner(value.getIndividualEntityQuestionary().getBeneficialOwner());
        individualPersonCategories.setHasRepresentative(value.getIndividualEntityQuestionary().getHasRepresentative());
        individualPersonCategories.setWorldwideOrgPublicPerson(value.getIndividualEntityQuestionary().getWorldwideOrgPublicPerson());
        individualPersonCategories.setForeignPublicPerson(value.getIndividualEntityQuestionary().getForeignPublicPerson());
        individualPersonCategories.setForeignRelativePerson(value.getIndividualEntityQuestionary().getForeignRelativePerson());
        individualPersonCategories.setBehalfOfForeign(value.getIndividualEntityQuestionary().getBehalfOfForeign());
        ThriftUtil.setIfNotEmpty(individualPersonCategories, russianIndividualEntity::setIndividualPersonCategories);

        var russianPrivateEntity = new com.rbkmoney.questionary.RussianPrivateEntity();
        if (value.getIndividualEntityQuestionary().getPrivateEntityBirthDate() != null) {
            russianPrivateEntity.setBirthDate(TypeUtil.temporalToString(value.getIndividualEntityQuestionary().getPrivateEntityBirthDate()));
        }
        russianPrivateEntity.setBirthPlace(value.getIndividualEntityQuestionary().getPrivateEntityBirthPlace());
        russianPrivateEntity.setCitizenship(value.getIndividualEntityQuestionary().getPrivateEntityCitizenship());
        russianPrivateEntity.setResidenceAddress(value.getIndividualEntityQuestionary().getPrivateEntityResidenceAddress());
        russianPrivateEntity.setActualAddress(value.getIndividualEntityQuestionary().getPrivateEntityActualAddress());

        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(value.getIndividualEntityQuestionary().getPrivateEntityEmail());
        contactInfo.setPhoneNumber(value.getIndividualEntityQuestionary().getPrivateEntityPhoneNumber());
        ThriftUtil.setIfNotEmpty(contactInfo, russianPrivateEntity::setContactInfo);

        russianPrivateEntity.setFio(value.getIndividualEntityQuestionary().getPrivateEntityFio());
        ThriftUtil.setIfNotEmpty(russianPrivateEntity, russianIndividualEntity::setRussianPrivateEntity);

        LicenseInfo licenseInfo = ctx.convert(value.getQuestionary(), LicenseInfo.class);
        ThriftUtil.setIfNotEmpty(licenseInfo, russianIndividualEntity::setLicenseInfo);

        final IdentityDocument identityDocument = new IdentityDocument();
        RussianDomesticPassport russianDomesticPassport = new RussianDomesticPassport();
        russianDomesticPassport.setSeriesNumber(value.getIndividualEntityQuestionary().getIdentityDocSeriesNumber());
        if (value.getIndividualEntityQuestionary().getIdentityDocIssuedAt() != null) {
            russianDomesticPassport.setIssuedAt(TypeUtil.temporalToString(value.getIndividualEntityQuestionary().getIdentityDocIssuedAt()));
        }
        russianDomesticPassport.setIssuer(value.getIndividualEntityQuestionary().getIdentityDocIssuer());
        russianDomesticPassport.setIssuerCode(value.getIndividualEntityQuestionary().getIdentityDocIssuerCode());
        ThriftUtil.setIfNotEmpty(russianDomesticPassport, identityDocument::setRussianDomesticPassword);
        ThriftUtil.setIfNotEmpty(identityDocument, russianIndividualEntity::setIdentityDocument);

        final MigrationCardInfo migrationCardInfo = new MigrationCardInfo();
        if (value.getIndividualEntityQuestionary().getMigrationCardExpirationDate() != null) {
            migrationCardInfo.setExpirationDate(TypeUtil.temporalToString(value.getIndividualEntityQuestionary().getMigrationCardExpirationDate()));
        }
        if (value.getIndividualEntityQuestionary().getMigrationCardBeginningDate() != null) {
            migrationCardInfo.setBeginningDate(TypeUtil.temporalToString(value.getIndividualEntityQuestionary().getMigrationCardBeginningDate()));
        }
        migrationCardInfo.setCardNumber(value.getIndividualEntityQuestionary().getMigrationCardNumber());
        ThriftUtil.setIfNotEmpty(migrationCardInfo, russianIndividualEntity::setMigrationCardInfo);

        final ResidenceApprove residenceApprove = new ResidenceApprove();
        residenceApprove.setName(value.getIndividualEntityQuestionary().getResidenceApproveName());
        residenceApprove.setSeries(value.getIndividualEntityQuestionary().getResidenceApproveSeries());
        residenceApprove.setNumber(value.getIndividualEntityQuestionary().getResidenceApproveNumber());
        if (value.getIndividualEntityQuestionary().getResidenceApproveBeginningDate() != null) {
            residenceApprove.setBeginningDate(TypeUtil.temporalToString(value.getIndividualEntityQuestionary().getResidenceApproveBeginningDate()));
        }
        if (value.getIndividualEntityQuestionary().getResidenceApproveExpirationDate() != null) {
            residenceApprove.setExpirationDate(TypeUtil.temporalToString(value.getIndividualEntityQuestionary().getResidenceApproveExpirationDate()));
        }
        ThriftUtil.setIfNotEmpty(residenceApprove, russianIndividualEntity::setResidenceApprove);

        if (value.getAdditionalInfoHolder() != null) {
            AdditionalInfo additionalInfo = ctx.convert(value.getAdditionalInfoHolder(), AdditionalInfo.class);
            ThriftUtil.setIfNotEmpty(additionalInfo, russianIndividualEntity::setAdditionalInfo);
        }

        if (value.getQuestionary().getPropertyInfoDocType() != null) {
            PropertyInfoDocumentType propertyInfoDocumentType = ctx.convert(value.getQuestionary(), PropertyInfoDocumentType.class);
            ThriftUtil.setIfNotEmpty(propertyInfoDocumentType, russianIndividualEntity::setPropertyInfoDocumentType);
        }

        if (value.getBeneficialOwnerList() != null) {
            List<BeneficialOwner> beneficialOwnerList = value.getBeneficialOwnerList().stream()
                    .map(beneficialOwner -> ctx.convert(beneficialOwner, BeneficialOwner.class))
                    .collect(Collectors.toList());
            russianIndividualEntity.setBeneficialOwners(beneficialOwnerList);
        }

        return russianIndividualEntity;
    }

    @Override
    public IndividualEntityQuestionaryHolder toJooq(RussianIndividualEntity value, JooqConverterContext ctx) {
        final IndividualEntityQuestionary individualEntityQuestionary = new IndividualEntityQuestionary();
        individualEntityQuestionary.setName(value.getName());
        individualEntityQuestionary.setSnils(value.getSnils());
        individualEntityQuestionary.setPdlCategory(value.isPdlCategory());
        individualEntityQuestionary.setPdlRelationDegree(value.getPdlRelationDegree());
        if (value.getIndividualPersonCategories() != null) {
            individualEntityQuestionary.setForeignPublicPerson(value.getIndividualPersonCategories().isForeignPublicPerson());
            individualEntityQuestionary.setForeignRelativePerson(value.getIndividualPersonCategories().isForeignRelativePerson());
            individualEntityQuestionary.setBehalfOfForeign(value.getIndividualPersonCategories().isBehalfOfForeign());
            individualEntityQuestionary.setWorldwideOrgPublicPerson(value.getIndividualPersonCategories().isWorldwideOrgPublicPerson());
            individualEntityQuestionary.setHasRepresentative(value.getIndividualPersonCategories().isHasRepresentative());
            individualEntityQuestionary.setBeneficialOwner(value.getIndividualPersonCategories().isBeneficialOwner());
        }
        if (value.isSetRegistrationInfo() && value.getRegistrationInfo().isSetIndividualRegistrationInfo()) {
            individualEntityQuestionary.setOgrnip(value.getRegistrationInfo().getIndividualRegistrationInfo().getOgrnip());
        }

        if (value.isSetRussianPrivateEntity()) {
            if (value.getRussianPrivateEntity().isSetFio()) {
                individualEntityQuestionary.setPrivateEntityFio(value.getRussianPrivateEntity().getFio());
            }
            if (value.getRussianPrivateEntity().isSetContactInfo()) {
                individualEntityQuestionary.setPrivateEntityEmail(value.getRussianPrivateEntity().getContactInfo().getEmail());
                individualEntityQuestionary.setPrivateEntityPhoneNumber(value.getRussianPrivateEntity().getContactInfo().getPhoneNumber());
            }
            individualEntityQuestionary.setPrivateEntityActualAddress(value.getRussianPrivateEntity().getActualAddress());
            individualEntityQuestionary.setPrivateEntityResidenceAddress(value.getRussianPrivateEntity().getResidenceAddress());
            if (value.getRussianPrivateEntity().isSetBirthDate()) {
                individualEntityQuestionary.setPrivateEntityBirthDate(TypeUtil.stringToLocalDateTime(value.getRussianPrivateEntity().getBirthDate()));
            }
            individualEntityQuestionary.setPrivateEntityBirthPlace(value.getRussianPrivateEntity().getBirthPlace());
            individualEntityQuestionary.setPrivateEntityCitizenship(value.getRussianPrivateEntity().getCitizenship());
        }

        if (value.isSetResidencyInfo()) {
            if (value.getResidencyInfo().isSetIndividualResidencyInfo()) {
                individualEntityQuestionary.setUsaTaxResident(value.getResidencyInfo().getIndividualResidencyInfo().isUsaTaxResident());
                individualEntityQuestionary.setExceptUsaTaxResident(value.getResidencyInfo().getIndividualResidencyInfo().isExceptUsaTaxResident());
            }
        }

        if (value.isSetResidenceApprove()) {
            if (value.getResidenceApprove().isSetBeginningDate()) {
                individualEntityQuestionary.setResidenceApproveBeginningDate(TypeUtil.stringToLocalDateTime(value.getResidenceApprove().getBeginningDate()));
            }
            if (value.getResidenceApprove().isSetExpirationDate()) {
                individualEntityQuestionary.setResidenceApproveExpirationDate(TypeUtil.stringToLocalDateTime(value.getResidenceApprove().getExpirationDate()));
            }
            individualEntityQuestionary.setResidenceApproveName(value.getResidenceApprove().getName());
            individualEntityQuestionary.setResidenceApproveNumber(value.getResidenceApprove().getNumber());
            individualEntityQuestionary.setResidenceApproveSeries(value.getResidenceApprove().getSeries());
        }
        if (value.isSetMigrationCardInfo()) {
            if (value.getMigrationCardInfo().isSetBeginningDate()) {
                individualEntityQuestionary.setMigrationCardBeginningDate(TypeUtil.stringToLocalDateTime(value.getMigrationCardInfo().getBeginningDate()));
            }
            if (value.getMigrationCardInfo().isSetExpirationDate()) {
                individualEntityQuestionary.setMigrationCardExpirationDate(TypeUtil.stringToLocalDateTime(value.getMigrationCardInfo().getExpirationDate()));
            }
            individualEntityQuestionary.setMigrationCardNumber(value.getMigrationCardInfo().getCardNumber());
        }
        if (value.isSetIdentityDocument()) {
            if (value.getIdentityDocument().isSetRussianDomesticPassword()) {
                RussianDomesticPassport russianPassport = value.getIdentityDocument().getRussianDomesticPassword();
                individualEntityQuestionary.setIdentityDocType(IdentityDocumentType.russian_passport);
                if (russianPassport.isSetIssuedAt()) {
                    individualEntityQuestionary.setIdentityDocIssuedAt(TypeUtil.stringToLocalDateTime(russianPassport.getIssuedAt()));
                }
                individualEntityQuestionary.setIdentityDocIssuer(russianPassport.getIssuer());
                individualEntityQuestionary.setIdentityDocIssuerCode(russianPassport.getIssuerCode());
                individualEntityQuestionary.setIdentityDocSeriesNumber(russianPassport.getSeriesNumber());
            }
        }

        AdditionalInfoHolder additionalInfo = null;
        if (value.isSetAdditionalInfo()) {
            additionalInfo = ctx.convert(value.getAdditionalInfo(), AdditionalInfoHolder.class);
        }

        List<com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner> beneficialOwnerList = null;
        if (value.isSetBeneficialOwners()) {
            beneficialOwnerList = value.getBeneficialOwners().stream()
                    .map(beneficialOwner -> ctx.convert(beneficialOwner, com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner.class))
                    .collect(Collectors.toList());
        }

        return IndividualEntityQuestionaryHolder.builder()
                .individualEntityQuestionary(individualEntityQuestionary)
                .additionalInfoHolder(additionalInfo)
                .beneficialOwnerList(beneficialOwnerList)
                .build();
    }
}
