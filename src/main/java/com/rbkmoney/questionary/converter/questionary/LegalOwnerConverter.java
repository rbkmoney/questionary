package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.enums.IdentityDocumentType;
import com.rbkmoney.questionary.domain.tables.pojos.LegalOwner;
import com.rbkmoney.questionary.util.ThriftUtil;
import org.springframework.stereotype.Component;

@Component
public class LegalOwnerConverter implements ThriftConverter<LegalOwnerInfo, LegalOwner>,
        JooqConverter<LegalOwner, LegalOwnerInfo> {

    @Override
    public LegalOwnerInfo toThrift(LegalOwner value, ThriftConverterContext ctx) {
        LegalOwnerInfo legalOwnerInfo = new LegalOwnerInfo();
        legalOwnerInfo.setInn(value.getLegalOwnerInn());
        legalOwnerInfo.setPdlCategory(value.getPdlCategory());
        legalOwnerInfo.setPdlRelationDegree(value.getPdlRelationDegree());

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
        ThriftUtil.setIfNotEmpty(residenceApprove, legalOwnerInfo::setResidenceApprove);

        MigrationCardInfo migrationCardInfo = new MigrationCardInfo();
        if (value.getMigrationCardExpirationDate() != null) {
            migrationCardInfo.setExpirationDate(TypeUtil.temporalToString(value.getMigrationCardExpirationDate()));
        }
        if (value.getMigrationCardBeginningDate() != null) {
            migrationCardInfo.setBeginningDate(TypeUtil.temporalToString(value.getMigrationCardBeginningDate()));
        }
        migrationCardInfo.setCardNumber(value.getMigrationCardNumber());
        ThriftUtil.setIfNotEmpty(migrationCardInfo, legalOwnerInfo::setMigrationCardInfo);

        IdentityDocument identityDocument = new IdentityDocument();
        RussianDomesticPassport russianDomesticPassport = new RussianDomesticPassport();
        russianDomesticPassport.setSeriesNumber(value.getIdentityDocSeriesNumber());
        if (value.getIdentityDocIssuedAt() != null) {
            russianDomesticPassport.setIssuedAt(TypeUtil.temporalToString(value.getIdentityDocIssuedAt()));
        }
        russianDomesticPassport.setIssuer(value.getIdentityDocIssuer());
        russianDomesticPassport.setIssuerCode(value.getIdentityDocIssuerCode());
        ThriftUtil.setIfNotEmpty(russianDomesticPassport, identityDocument::setRussianDomesticPassword);
        ThriftUtil.setIfNotEmpty(identityDocument, legalOwnerInfo::setIdentityDocument);

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
        ThriftUtil.setIfNotEmpty(contactInfo, russianPrivateEntity::setContactInfo);

        russianPrivateEntity.setFio(value.getPrivateEntityFio());

        legalOwnerInfo.setRussianPrivateEntity(russianPrivateEntity);

        AuthorityConfirmingDocument authorityConfirmingDocument = ctx.convert(value, AuthorityConfirmingDocument.class);
        ThriftUtil.setIfNotEmpty(authorityConfirmingDocument, legalOwnerInfo::setAuthorityConfirmingDocument);

        legalOwnerInfo.setSnils(value.getSnils());
        legalOwnerInfo.setPdlCategory(value.getPdlCategory());
        legalOwnerInfo.setTermOfOffice(value.getTermOfOffice());
        legalOwnerInfo.setHeadPosition(value.getHeadPosition());

        return legalOwnerInfo;
    }

    @Override
    public LegalOwner toJooq(LegalOwnerInfo value, JooqConverterContext ctx) {
        LegalOwner legalOwner = new LegalOwner();
        legalOwner.setLegalOwnerInn(value.getInn());
        legalOwner.setPdlCategory(value.isPdlCategory());
        legalOwner.setPdlRelationDegree(value.getPdlRelationDegree());
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
                if (russianPassport.isSetIssuedAt()) {
                    legalOwner.setIdentityDocIssuedAt(TypeUtil.stringToLocalDateTime(russianPassport.getIssuedAt()));
                }
                legalOwner.setIdentityDocIssuer(russianPassport.getIssuer());
                legalOwner.setIdentityDocIssuerCode(russianPassport.getIssuerCode());
                legalOwner.setIdentityDocSeriesNumber(russianPassport.getSeriesNumber());
            }
        }

        RussianPrivateEntity russianPrivateEntity = value.getRussianPrivateEntity();
        if (russianPrivateEntity != null) {
            if (russianPrivateEntity.isSetFio()) {
                legalOwner.setPrivateEntityFio(russianPrivateEntity.getFio());
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

        if (value.getAuthorityConfirmingDocument() != null) {
            if (value.getAuthorityConfirmingDocument().isSetDate()) {
                legalOwner.setAuthorityConfirmDocDate(TypeUtil.stringToLocalDateTime(value.getAuthorityConfirmingDocument().getDate()));
            }
            legalOwner.setAuthorityConfirmDocNumber(value.getAuthorityConfirmingDocument().getNumber());
            legalOwner.setAuthorityConfirmDocType(value.getAuthorityConfirmingDocument().getType());
        }

        legalOwner.setSnils(value.getSnils());
        legalOwner.setPdlRelationDegree(value.getPdlRelationDegree());
        legalOwner.setTermOfOffice(value.getTermOfOffice());
        legalOwner.setHeadPosition(value.getHeadPosition());

        return legalOwner;
    }
}
