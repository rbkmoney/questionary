package com.rbkmoney.questionary.converter;

import com.rbkmoney.geck.common.util.TBaseUtil;
import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.domain.enums.IdentityDocumentType;
import com.rbkmoney.questionary.domain.tables.pojos.IndividualEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.PropertyInfo;
import com.rbkmoney.questionary.model.AdditionalInfoHolder;
import com.rbkmoney.questionary.model.IndividualEntityQuestionaryHolder;

import java.util.List;
import java.util.stream.Collectors;

public class IndividualEntityQuestionaryConverter implements ThriftConverter<IndividualEntityQuestionaryHolder, RussianIndividualEntity> {

    private final AdditionalInfoConverter additionalInfoConverter;

    IndividualEntityQuestionaryConverter() {
        this.additionalInfoConverter = new AdditionalInfoConverter();
    }

    @Override
    public RussianIndividualEntity convertToThrift(IndividualEntityQuestionaryHolder value) {
        final RussianIndividualEntity russianIndividualEntity = new RussianIndividualEntity();

        russianIndividualEntity.setInn(value.getQuestionary().getInn());
        if (value.getPropertyInfoList() != null) {
            final List<String> propertyInfoList = value.getPropertyInfoList().stream()
                    .map(PropertyInfo::getDescription)
                    .collect(Collectors.toList());
            russianIndividualEntity.setPropertyInfo(propertyInfoList);
        }

        final Activity activity = new Activity();
        activity.setCode(value.getQuestionary().getOkvd());
        activity.setDescription(value.getQuestionary().getActivityType());
        russianIndividualEntity.setPrincipalActivity(activity);

        final RegistrationInfo registrationInfo = new RegistrationInfo();
        IndividualRegistrationInfo individualRegistrationInfo = new IndividualRegistrationInfo();
        individualRegistrationInfo.setOgrnip(value.getIndividualEntityQuestionary().getOgrnip());
        individualRegistrationInfo.setRegistrationPlace(value.getQuestionary().getRegPlace());
        if (value.getQuestionary().getRegDate() != null) {
            individualRegistrationInfo.setRegistrationDate(TypeUtil.temporalToString(value.getQuestionary().getRegDate()));
        }
        registrationInfo.setIndividualRegistrationInfo(individualRegistrationInfo);
        russianIndividualEntity.setRegistrationInfo(registrationInfo);

        final ResidencyInfo residencyInfo = new ResidencyInfo();
        IndividualResidencyInfo individualResidencyInfo = new IndividualResidencyInfo();
        individualResidencyInfo.setTaxResident(value.getQuestionary().getTaxResident());
        residencyInfo.setIndividualResidencyInfo(individualResidencyInfo);
        russianIndividualEntity.setResidencyInfo(residencyInfo);

        final IndividualPersonCategories individualPersonCategories = new IndividualPersonCategories();
        individualPersonCategories.setBeneficialOwner(value.getIndividualEntityQuestionary().getBeneficialOwner());
        individualPersonCategories.setHasRepresentative(value.getIndividualEntityQuestionary().getHasRepresentative());
        individualPersonCategories.setWorldwideOrgPublicPerson(value.getIndividualEntityQuestionary().getWorldwideOrgPublicPerson());
        individualPersonCategories.setForeignPublicPerson(value.getIndividualEntityQuestionary().getForeignPublicPerson());
        individualPersonCategories.setForeignRelativePerson(value.getIndividualEntityQuestionary().getForeignRelativePerson());
        individualPersonCategories.setBehalfOfForeign(value.getIndividualEntityQuestionary().getBehalfOfForeign());
        russianIndividualEntity.setIndividualPersonCategories(individualPersonCategories);

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
        russianPrivateEntity.setContactInfo(contactInfo);

        final PersonAnthroponym personAnthroponym = new PersonAnthroponym();
        personAnthroponym.setFirstName(value.getIndividualEntityQuestionary().getPrivateEntityFirstName());
        personAnthroponym.setSecondName(value.getIndividualEntityQuestionary().getPrivateEntitySecondName());
        personAnthroponym.setMiddleName(value.getIndividualEntityQuestionary().getPrivateEntityMiddleName());
        russianPrivateEntity.setFio(personAnthroponym);
        russianIndividualEntity.setRussianPrivateEntity(russianPrivateEntity);

        final LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setIssuerName(value.getQuestionary().getLicenseIssuerName());
        licenseInfo.setOfficialNum(value.getQuestionary().getLicenseOfficialNum());
        licenseInfo.setLicensedActivity(value.getQuestionary().getLicenseLicensedActivity());
        if (value.getQuestionary().getLicenseExpirationDate() != null) {
            licenseInfo.setExpirationDate(TypeUtil.temporalToString(value.getQuestionary().getLicenseExpirationDate()));
        }
        if (value.getQuestionary().getLicenseEffectiveDate() != null) {
            licenseInfo.setEffectiveDate(TypeUtil.temporalToString(value.getQuestionary().getLicenseEffectiveDate()));
        }
        if (value.getQuestionary().getLicenseIssueDate() != null) {
            licenseInfo.setIssueDate(TypeUtil.temporalToString(value.getQuestionary().getLicenseIssueDate()));
        }
        russianIndividualEntity.setLicenseInfo(licenseInfo);

        final IdentityDocument identityDocument = new IdentityDocument();
        RussianDomesticPassport russianDomesticPassport = new RussianDomesticPassport();
        russianDomesticPassport.setSeries(value.getIndividualEntityQuestionary().getIdentityDocSeries());
        russianDomesticPassport.setNumber(value.getIndividualEntityQuestionary().getIdentityDocNumber());
        if (value.getIndividualEntityQuestionary().getIdentityDocIssuedAt() != null) {
            russianDomesticPassport.setIssuedAt(TypeUtil.temporalToString(value.getIndividualEntityQuestionary().getIdentityDocIssuedAt()));
        }
        russianDomesticPassport.setIssuer(value.getIndividualEntityQuestionary().getIdentityDocIssuer());
        russianDomesticPassport.setIssuerCode(value.getIndividualEntityQuestionary().getIdentityDocIssuerCode());
        identityDocument.setRussianDomesticPassword(russianDomesticPassport);
        russianIndividualEntity.setIdentityDocument(identityDocument);

        final MigrationCardInfo migrationCardInfo = new MigrationCardInfo();
        if (value.getIndividualEntityQuestionary().getMigrationCardExpirationDate() != null) {
            migrationCardInfo.setExpirationDate(TypeUtil.temporalToString(value.getIndividualEntityQuestionary().getMigrationCardExpirationDate()));
        }
        if (value.getIndividualEntityQuestionary().getMigrationCardBeginningDate() != null) {
            migrationCardInfo.setBeginningDate(TypeUtil.temporalToString(value.getIndividualEntityQuestionary().getMigrationCardBeginningDate()));
        }
        migrationCardInfo.setCardNumber(value.getIndividualEntityQuestionary().getMigrationCardNumber());
        russianIndividualEntity.setMigrationCardInfo(migrationCardInfo);

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
        russianIndividualEntity.setResidenceApprove(residenceApprove);

        if (value.getAdditionalInfoHolder() != null) {
            russianIndividualEntity.setAdditionalInfo(additionalInfoConverter.convertToThrift(value.getAdditionalInfoHolder()));
        }

        return russianIndividualEntity;
    }

    @Override
    public IndividualEntityQuestionaryHolder convertFromThrift(RussianIndividualEntity value) {
        final IndividualEntityQuestionary individualEntityQuestionary = new IndividualEntityQuestionary();
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
                individualEntityQuestionary.setPrivateEntityFirstName(value.getRussianPrivateEntity().getFio().getFirstName());
                individualEntityQuestionary.setPrivateEntitySecondName(value.getRussianPrivateEntity().getFio().getSecondName());
                individualEntityQuestionary.setPrivateEntityMiddleName(value.getRussianPrivateEntity().getFio().getMiddleName());
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
            ;
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
                individualEntityQuestionary.setIdentityDocIssuedAt(TypeUtil.stringToLocalDateTime(russianPassport.getIssuedAt()));
                individualEntityQuestionary.setIdentityDocIssuer(russianPassport.getIssuer());
                individualEntityQuestionary.setIdentityDocIssuerCode(russianPassport.getIssuerCode());
                individualEntityQuestionary.setIdentityDocNumber(russianPassport.getNumber());
                individualEntityQuestionary.setIdentityDocSeries(russianPassport.getSeries());
            }
        }

        AdditionalInfoHolder additionalInfo = null;
        if (value.isSetAdditionalInfo()) {
            additionalInfo = additionalInfoConverter.convertFromThrift(value.getAdditionalInfo());
        }

        return IndividualEntityQuestionaryHolder.builder()
                .individualEntityQuestionary(individualEntityQuestionary)
                .additionalInfoHolder(additionalInfo)
                .build();
    }
}
