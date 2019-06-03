package com.rbkmoney.questionary.dao.mapper;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.rbkmoney.questionary.domain.Tables.LEGAL_OWNER;

public class LegalOwnerMapper implements RowMapper<LegalOwnerInfo> {

    @Override
    public LegalOwnerInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        LegalOwnerInfo legalOwnerInfo = new LegalOwnerInfo();
        legalOwnerInfo.setInn(rs.getString(LEGAL_OWNER.LEGAL_OWNER_INN.getName()));
        legalOwnerInfo.setPdlCategory(rs.getBoolean(LEGAL_OWNER.PDL_CATEGORY.getName()));

        final RussianPrivateEntity russianPrivateEntity = new RussianPrivateEntity();
        final PersonAnthroponym personAnthroponym = new PersonAnthroponym();
        personAnthroponym.setFirstName(rs.getString(LEGAL_OWNER.PRIVATE_ENTITY_FIRST_NAME.getName()));
        personAnthroponym.setSecondName(rs.getString(LEGAL_OWNER.PRIVATE_ENTITY_SECOND_NAME.getName()));
        personAnthroponym.setMiddleName(rs.getString(LEGAL_OWNER.PRIVATE_ENTITY_MIDDLE_NAME.getName()));
        russianPrivateEntity.setFio(personAnthroponym);
        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPhoneNumber(rs.getString(LEGAL_OWNER.PRIVATE_ENTITY_PHONE_NUMBER.getName()));
        contactInfo.setEmail(rs.getString(LEGAL_OWNER.PRIVATE_ENTITY_EMAIL.getName()));
        russianPrivateEntity.setContactInfo(contactInfo);
        russianPrivateEntity.setActualAddress(rs.getString(LEGAL_OWNER.PRIVATE_ENTITY_ACTUAL_ADDRESS.getName()));
        final LocalDateTime birthDate = rs.getObject(LEGAL_OWNER.PRIVATE_ENTITY_BIRTH_DATE.getName(), LocalDateTime.class);
        if (birthDate != null) {
            russianPrivateEntity.setBirthDate(TypeUtil.temporalToString(birthDate));
        }
        russianPrivateEntity.setBirthPlace(rs.getString(LEGAL_OWNER.PRIVATE_ENTITY_BIRTH_PLACE.getName()));
        russianPrivateEntity.setResidenceAddress(rs.getString(LEGAL_OWNER.PRIVATE_ENTITY_RESIDENCE_ADDRESS.getName()));
        russianPrivateEntity.setCitizenship(rs.getString(LEGAL_OWNER.PRIVATE_ENTITY_CITIZENSHIP.getName()));
        legalOwnerInfo.setRussianPrivateEntity(russianPrivateEntity);

        IdentityDocument identityDocument = new IdentityDocument();
        RussianDomesticPassport russianDomesticPassport = new RussianDomesticPassport();
        russianDomesticPassport.setIssuerCode(rs.getString(LEGAL_OWNER.IDENTITY_DOC_ISSUER_CODE.getName()));
        russianDomesticPassport.setIssuer(rs.getString(LEGAL_OWNER.IDENTITY_DOC_ISSUER.getName()));
        final LocalDateTime identityDocIssuedAt = rs.getObject(LEGAL_OWNER.IDENTITY_DOC_ISSUED_AT.getName(), LocalDateTime.class);
        if (identityDocIssuedAt != null) {
            russianDomesticPassport.setIssuedAt(TypeUtil.temporalToString(identityDocIssuedAt));
        }
        russianDomesticPassport.setNumber(rs.getString(LEGAL_OWNER.IDENTITY_DOC_NUMBER.getName()));
        russianDomesticPassport.setSeries(rs.getString(LEGAL_OWNER.IDENTITY_DOC_SERIES.getName()));
        identityDocument.setRussianDomesticPassword(russianDomesticPassport);
        legalOwnerInfo.setIdentityDocument(identityDocument);

        MigrationCardInfo migrationCardInfo = new MigrationCardInfo();
        migrationCardInfo.setCardNumber(rs.getString(LEGAL_OWNER.MIGRATION_CARD_NUMBER.getName()));
        final LocalDateTime cardBegDate = rs.getObject(LEGAL_OWNER.MIGRATION_CARD_BEGINNING_DATE.getName(), LocalDateTime.class);
        if (cardBegDate != null) {
            migrationCardInfo.setBeginningDate(TypeUtil.temporalToString(cardBegDate));
        }
        final LocalDateTime cardExpDate = rs.getObject(LEGAL_OWNER.MIGRATION_CARD_EXPIRATION_DATE.getName(), LocalDateTime.class);
        if (cardExpDate != null) {
            migrationCardInfo.setExpirationDate(TypeUtil.temporalToString(cardExpDate));
        }
        legalOwnerInfo.setMigrationCardInfo(migrationCardInfo);

        ResidenceApprove residenceApprove = new ResidenceApprove();
        residenceApprove.setName(rs.getString(LEGAL_OWNER.RESIDENCE_APPROVE_NAME.getName()));
        residenceApprove.setNumber(rs.getString(LEGAL_OWNER.RESIDENCE_APPROVE_NUMBER.getName()));
        residenceApprove.setSeries(rs.getString(LEGAL_OWNER.RESIDENCE_APPROVE_SERIES.getName()));
        final LocalDateTime resApproveBegDate = rs.getObject(LEGAL_OWNER.RESIDENCE_APPROVE_BEGINNING_DATE.getName(), LocalDateTime.class);
        if (resApproveBegDate != null) {
            residenceApprove.setBeginningDate(TypeUtil.temporalToString(resApproveBegDate));
        }
        final LocalDateTime resApproveExpDate = rs.getObject(LEGAL_OWNER.RESIDENCE_APPROVE_EXPIRATION_DATE.getName(), LocalDateTime.class);
        if (resApproveExpDate != null) {
            residenceApprove.setExpirationDate(TypeUtil.temporalToString(resApproveExpDate));
        }
        legalOwnerInfo.setResidenceApprove(residenceApprove);

        return legalOwnerInfo;
    }

}
