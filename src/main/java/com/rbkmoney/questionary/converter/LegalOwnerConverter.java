package com.rbkmoney.questionary.converter;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.domain.enums.IdentityDocumentType;
import com.rbkmoney.questionary.domain.tables.pojos.LegalOwner;

public class LegalOwnerConverter implements ThriftConverter<LegalOwner, LegalOwnerInfo> {

    @Override
    public LegalOwnerInfo convertToThrift(LegalOwner value) {
        LegalOwnerInfo legalOwnerInfo = new LegalOwnerInfo();
        legalOwnerInfo.setInn(value.getLegalOwnerInn());
        legalOwnerInfo.setPdlCategory(value.getPdlCategory());

        ResidenceApprove residenceApprove = new ResidenceApprove();
        residenceApprove.setName(value.getResidenceApproveName());
        residenceApprove.setSeries(value.getResidenceApproveSeries());
        residenceApprove.setNumber(value.getResidenceApproveNumber());
        if (value.getResidenceApproveBeginningDate() != null) {
            residenceApprove.setBeginningDate(TypeUtil.temporalToString(value.getResidenceApproveBeginningDate()));
        }
        if (value.getResidenceApproveExpirationDate() != null) {
            residenceApprove.setExpirationDate(TypeUtil.temporalToString(value.getResidenceApproveExpirationDate()));
        }
        legalOwnerInfo.setResidenceApprove(residenceApprove);

        MigrationCardInfo migrationCardInfo = new MigrationCardInfo();
        if (value.getMigrationCardExpirationDate() != null) {
            migrationCardInfo.setExpirationDate(TypeUtil.temporalToString(value.getMigrationCardExpirationDate()));
        }
        if (value.getMigrationCardBeginningDate() != null) {
            migrationCardInfo.setBeginningDate(TypeUtil.temporalToString(value.getMigrationCardBeginningDate()));
        }
        migrationCardInfo.setCardNumber(value.getMigrationCardNumber());
        legalOwnerInfo.setMigrationCardInfo(migrationCardInfo);

        IdentityDocument identityDocument = new IdentityDocument();
        RussianDomesticPassport russianDomesticPassport = new RussianDomesticPassport();
        russianDomesticPassport.setSeries(value.getIdentityDocSeries());
        russianDomesticPassport.setNumber(value.getIdentityDocNumber());
        if (value.getIdentityDocIssuedAt() != null) {
            russianDomesticPassport.setIssuedAt(TypeUtil.temporalToString(value.getIdentityDocIssuedAt()));
        }
        russianDomesticPassport.setIssuer(value.getIdentityDocIssuer());
        russianDomesticPassport.setIssuerCode(value.getIdentityDocIssuerCode());
        identityDocument.setRussianDomesticPassword(russianDomesticPassport);
        legalOwnerInfo.setIdentityDocument(identityDocument);

        RussianPrivateEntity russianPrivateEntity = new RussianPrivateEntity();
        if (value.getPrivateEntityBirthDate() != null) {
            russianPrivateEntity.setBirthDate(TypeUtil.temporalToString(value.getPrivateEntityBirthDate()));
        }
        russianPrivateEntity.setBirthPlace(value.getPrivateEntityBirthPlace());
        russianPrivateEntity.setCitizenship(value.getPrivateEntityCitizenship());
        russianPrivateEntity.setResidenceAddress(value.getPrivateEntityResidenceAddress());
        russianPrivateEntity.setActualAddress(value.getPrivateEntityActualAddress());

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(value.getPrivateEntityEmail());
        contactInfo.setPhoneNumber(value.getPrivateEntityPhoneNumber());
        russianPrivateEntity.setContactInfo(contactInfo);

        PersonAnthroponym personAnthroponym = new PersonAnthroponym();
        personAnthroponym.setFirstName(value.getPrivateEntityFirstName());
        personAnthroponym.setSecondName(value.getPrivateEntitySecondName());
        personAnthroponym.setMiddleName(value.getPrivateEntityMiddleName());
        russianPrivateEntity.setFio(personAnthroponym);

        legalOwnerInfo.setRussianPrivateEntity(russianPrivateEntity);

        return legalOwnerInfo;
    }

    @Override
    public LegalOwner convertFromThrift(LegalOwnerInfo value) {
        LegalOwner legalOwner = new LegalOwner();
        legalOwner.setLegalOwnerInn(value.getInn());
        legalOwner.setPdlCategory(value.isPdlCategory());
        if (value.isSetResidenceApprove()) {
            if (value.getResidenceApprove().isSetBeginningDate()) {
                legalOwner.setResidenceApproveBeginningDate(TypeUtil.stringToLocalDateTime(value.getResidenceApprove().getBeginningDate()));
            }
            if (value.getResidenceApprove().isSetExpirationDate()) {
                legalOwner.setResidenceApproveExpirationDate(TypeUtil.stringToLocalDateTime(value.getResidenceApprove().getExpirationDate()));
            }
            legalOwner.setResidenceApproveName(value.getResidenceApprove().getName());
            legalOwner.setResidenceApproveNumber(value.getResidenceApprove().getNumber());
            legalOwner.setResidenceApproveSeries(value.getResidenceApprove().getSeries());
        }
        if (value.isSetMigrationCardInfo()) {
            if (value.getMigrationCardInfo().isSetBeginningDate()) {
                legalOwner.setMigrationCardBeginningDate(TypeUtil.stringToLocalDateTime(value.getMigrationCardInfo().getBeginningDate()));
            }
            if (value.getMigrationCardInfo().isSetExpirationDate()) {
                legalOwner.setMigrationCardExpirationDate(TypeUtil.stringToLocalDateTime(value.getMigrationCardInfo().getExpirationDate()));
            }
            legalOwner.setMigrationCardNumber(value.getMigrationCardInfo().getCardNumber());
        }
        if (value.isSetIdentityDocument()) {
            if (value.getIdentityDocument().isSetRussianDomesticPassword()) {
                RussianDomesticPassport russianPassport = value.getIdentityDocument().getRussianDomesticPassword();
                legalOwner.setIdentityDocType(IdentityDocumentType.russian_passport);
                legalOwner.setIdentityDocIssuedAt(TypeUtil.stringToLocalDateTime(russianPassport.getIssuedAt()));
                legalOwner.setIdentityDocIssuer(russianPassport.getIssuer());
                legalOwner.setIdentityDocIssuerCode(russianPassport.getIssuerCode());
                legalOwner.setIdentityDocNumber(russianPassport.getNumber());
                legalOwner.setIdentityDocSeries(russianPassport.getSeries());
            }
        }

        RussianPrivateEntity russianPrivateEntity = value.getRussianPrivateEntity();
        if (russianPrivateEntity != null) {
            if (russianPrivateEntity.isSetFio()) {
                legalOwner.setPrivateEntityFirstName(russianPrivateEntity.getFio().getFirstName());
                legalOwner.setPrivateEntitySecondName(russianPrivateEntity.getFio().getSecondName());
                legalOwner.setPrivateEntityMiddleName(russianPrivateEntity.getFio().getMiddleName());
            }
            if (russianPrivateEntity.isSetContactInfo()) {
                legalOwner.setPrivateEntityEmail(russianPrivateEntity.getContactInfo().getEmail());
                legalOwner.setPrivateEntityPhoneNumber(russianPrivateEntity.getContactInfo().getPhoneNumber());
            }
            legalOwner.setPrivateEntityActualAddress(russianPrivateEntity.getActualAddress());
            legalOwner.setPrivateEntityResidenceAddress(russianPrivateEntity.getResidenceAddress());
            if (russianPrivateEntity.isSetBirthDate()) {
                legalOwner.setPrivateEntityBirthDate(TypeUtil.stringToLocalDateTime(russianPrivateEntity.getBirthDate()));
            }
            legalOwner.setPrivateEntityBirthPlace(russianPrivateEntity.getBirthPlace());
            legalOwner.setPrivateEntityCitizenship(russianPrivateEntity.getCitizenship());
        }

        return legalOwner;
    }
}
