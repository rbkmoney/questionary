package com.rbkmoney.questionary.converter;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.domain.enums.IdentityDocumentType;

public class BeneficialOwnerConverter implements ThriftConverter<com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner, BeneficialOwner> {
    @Override
    public BeneficialOwner convertToThrift(com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner value) {
        final BeneficialOwner beneficialOwner = new BeneficialOwner();
        beneficialOwner.setPdlCategory(value.getPdlCategory());
        beneficialOwner.setInn(value.getInn());
        if (value.getOwnershipPercentage() != null) {
            beneficialOwner.setOwnershipPercentage(value.getOwnershipPercentage().byteValue());
        }

        final ResidenceApprove residenceApprove = new ResidenceApprove();
        residenceApprove.setName(value.getResidenceApproveName());
        residenceApprove.setSeries(value.getResidenceApproveSeries());
        residenceApprove.setNumber(value.getResidenceApproveNumber());
        if (value.getResidenceApproveBeginningDate() != null) {
            residenceApprove.setBeginningDate(TypeUtil.temporalToString(value.getResidenceApproveBeginningDate()));
        }
        if (value.getResidenceApproveExpirationDate() != null) {
            residenceApprove.setExpirationDate(TypeUtil.temporalToString(value.getResidenceApproveExpirationDate()));
        }
        beneficialOwner.setResidenceApprove(residenceApprove);

        final MigrationCardInfo migrationCardInfo = new MigrationCardInfo();
        if (value.getMigrationCardExpirationDate() != null) {
            migrationCardInfo.setExpirationDate(TypeUtil.temporalToString(value.getMigrationCardExpirationDate()));
        }
        if (value.getMigrationCardBeginningDate() != null) {
            migrationCardInfo.setBeginningDate(TypeUtil.temporalToString(value.getMigrationCardBeginningDate()));
        }
        migrationCardInfo.setCardNumber(value.getMigrationCardNumber());
        beneficialOwner.setMigrationCardInfo(migrationCardInfo);

        final IdentityDocument identityDocument = new IdentityDocument();
        RussianDomesticPassport russianDomesticPassport = new RussianDomesticPassport();
        russianDomesticPassport.setSeries(value.getIdentityDocSeries());
        russianDomesticPassport.setNumber(value.getIdentityDocNumber());
        if (value.getIdentityDocIssuedAt() != null) {
            russianDomesticPassport.setIssuedAt(TypeUtil.temporalToString(value.getIdentityDocIssuedAt()));
        }
        russianDomesticPassport.setIssuer(value.getIdentityDocIssuer());
        russianDomesticPassport.setIssuerCode(value.getIdentityDocIssuerCode());
        identityDocument.setRussianDomesticPassword(russianDomesticPassport);
        beneficialOwner.setIdentityDocument(identityDocument);

        final RussianPrivateEntity russianPrivateEntity = new RussianPrivateEntity();
        if (value.getPrivateEntityBirthDate() != null) {
            russianPrivateEntity.setBirthDate(TypeUtil.temporalToString(value.getPrivateEntityBirthDate()));
        }
        russianPrivateEntity.setBirthPlace(value.getPrivateEntityBirthPlace());
        russianPrivateEntity.setCitizenship(value.getPrivateEntityCitizenship());
        russianPrivateEntity.setResidenceAddress(value.getPrivateEntityResidenceAddress());
        russianPrivateEntity.setActualAddress(value.getPrivateEntityActualAddress());

        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(value.getPrivateEntityEmail());
        contactInfo.setPhoneNumber(value.getPrivateEntityPhoneNumber());
        russianPrivateEntity.setContactInfo(contactInfo);

        final PersonAnthroponym personAnthroponym = new PersonAnthroponym();
        personAnthroponym.setFirstName(value.getPrivateEntityFirstName());
        personAnthroponym.setSecondName(value.getPrivateEntitySecondName());
        personAnthroponym.setMiddleName(value.getPrivateEntityMiddleName());
        russianPrivateEntity.setFio(personAnthroponym);

        beneficialOwner.setRussianPrivateEntity(russianPrivateEntity);

        return beneficialOwner;
    }

    @Override
    public com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner convertFromThrift(BeneficialOwner value) {
        var beneficialOwner = new com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner();
        beneficialOwner.setInn(value.getInn());
        beneficialOwner.setOwnershipPercentage((short) value.getOwnershipPercentage());
        beneficialOwner.setPdlCategory(value.isPdlCategory());
        if (value.isSetResidenceApprove()) {
            if (value.getResidenceApprove().isSetBeginningDate()) {
                beneficialOwner.setResidenceApproveBeginningDate(TypeUtil.stringToLocalDateTime(value.getResidenceApprove().getBeginningDate()));
            }
            if (value.getResidenceApprove().isSetExpirationDate()) {
                beneficialOwner.setResidenceApproveExpirationDate(TypeUtil.stringToLocalDateTime(value.getResidenceApprove().getExpirationDate()));
            }
            beneficialOwner.setResidenceApproveName(value.getResidenceApprove().getName());
            beneficialOwner.setResidenceApproveNumber(value.getResidenceApprove().getNumber());
            beneficialOwner.setResidenceApproveSeries(value.getResidenceApprove().getSeries());
            ;
        }
        if (value.isSetMigrationCardInfo()) {
            if (value.getMigrationCardInfo().isSetBeginningDate()) {
                beneficialOwner.setMigrationCardBeginningDate(TypeUtil.stringToLocalDateTime(value.getMigrationCardInfo().getBeginningDate()));
            }
            if (value.getMigrationCardInfo().isSetExpirationDate()) {
                beneficialOwner.setMigrationCardExpirationDate(TypeUtil.stringToLocalDateTime(value.getMigrationCardInfo().getExpirationDate()));
            }
            beneficialOwner.setMigrationCardNumber(value.getMigrationCardInfo().getCardNumber());
        }
        if (value.isSetIdentityDocument()) {
            if (value.getIdentityDocument().isSetRussianDomesticPassword()) {
                RussianDomesticPassport russianPassport = value.getIdentityDocument().getRussianDomesticPassword();
                beneficialOwner.setIdentityDocType(IdentityDocumentType.russian_passport);
                beneficialOwner.setIdentityDocIssuedAt(TypeUtil.stringToLocalDateTime(russianPassport.getIssuedAt()));
                beneficialOwner.setIdentityDocIssuer(russianPassport.getIssuer());
                beneficialOwner.setIdentityDocIssuerCode(russianPassport.getIssuerCode());
                beneficialOwner.setIdentityDocNumber(russianPassport.getNumber());
                beneficialOwner.setIdentityDocSeries(russianPassport.getSeries());
            }
        }

        if (value.isSetRussianPrivateEntity()) {
            if (value.getRussianPrivateEntity().isSetFio()) {
                beneficialOwner.setPrivateEntityFirstName(value.getRussianPrivateEntity().getFio().getFirstName());
                beneficialOwner.setPrivateEntitySecondName(value.getRussianPrivateEntity().getFio().getSecondName());
                beneficialOwner.setPrivateEntityMiddleName(value.getRussianPrivateEntity().getFio().getMiddleName());
            }
            if (value.getRussianPrivateEntity().isSetContactInfo()) {
                beneficialOwner.setPrivateEntityEmail(value.getRussianPrivateEntity().getContactInfo().getEmail());
                beneficialOwner.setPrivateEntityPhoneNumber(value.getRussianPrivateEntity().getContactInfo().getPhoneNumber());
            }
            beneficialOwner.setPrivateEntityActualAddress(value.getRussianPrivateEntity().getActualAddress());
            beneficialOwner.setPrivateEntityResidenceAddress(value.getRussianPrivateEntity().getResidenceAddress());
            if (value.getRussianPrivateEntity().isSetBirthDate()) {
                beneficialOwner.setPrivateEntityBirthDate(TypeUtil.stringToLocalDateTime(value.getRussianPrivateEntity().getBirthDate()));
            }
            beneficialOwner.setPrivateEntityBirthPlace(value.getRussianPrivateEntity().getBirthPlace());
            beneficialOwner.setPrivateEntityCitizenship(value.getRussianPrivateEntity().getCitizenship());
        }

        return beneficialOwner;
    }
}
