package com.rbkmoney.questionary.dao.mapper;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.rbkmoney.questionary.domain.Tables.*;

public class IndividualEntityQuestionaryMapper implements RowMapper<IndividualEntity> {

    @Override
    public IndividualEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        final RussianIndividualEntity russianIndividualEntity = new RussianIndividualEntity();
        russianIndividualEntity.setInn(rs.getString(QUESTIONARY.INN.getName()));
        russianIndividualEntity.setSnils(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.SNILS.getName()));

        final IndividualPersonCategories individualPersonCategories = new IndividualPersonCategories();
        individualPersonCategories.setBehalfOfForeign(rs.getBoolean(INDIVIDUAL_ENTITY_QUESTIONARY.BEHALF_OF_FOREIGN.getName()));
        individualPersonCategories.setForeignPublicPerson(rs.getBoolean(INDIVIDUAL_ENTITY_QUESTIONARY.FOREIGN_PUBLIC_PERSON.getName()));
        individualPersonCategories.setForeignRelativePerson(rs.getBoolean(INDIVIDUAL_ENTITY_QUESTIONARY.FOREIGN_RELATIVE_PERSON.getName()));
        individualPersonCategories.setWorldwideOrgPublicPerson(rs.getBoolean(INDIVIDUAL_ENTITY_QUESTIONARY.WORLDWIDE_ORG_PUBLIC_PERSON.getName()));
        individualPersonCategories.setHasRepresentative(rs.getBoolean(INDIVIDUAL_ENTITY_QUESTIONARY.HAS_REPRESENTATIVE.getName()));
        individualPersonCategories.setBeneficialOwner(rs.getBoolean(INDIVIDUAL_ENTITY_QUESTIONARY.BENEFICIAL_OWNER.getName()));
        russianIndividualEntity.setIndividualPersonCategories(individualPersonCategories);

        final IndividualResidencyInfo individualResidencyInfo = new IndividualResidencyInfo();
        individualResidencyInfo.setUsaTaxResident(rs.getBoolean(INDIVIDUAL_ENTITY_QUESTIONARY.USA_TAX_RESIDENT.getName()));
        individualResidencyInfo.setExceptUsaTaxResident(rs.getBoolean(INDIVIDUAL_ENTITY_QUESTIONARY.EXCEPT_USA_TAX_RESIDENT.getName()));
        russianIndividualEntity.setResidencyInfo(ResidencyInfo.individual_residency_info(individualResidencyInfo));

        final IndividualRegistrationInfo individualRegistrationInfo = new IndividualRegistrationInfo();
        individualRegistrationInfo.setOgrnip(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.OGRNIP.getName()));
        final LocalDateTime regDate = rs.getObject(QUESTIONARY.REG_DATE.getName(), LocalDateTime.class);
        if (regDate != null) {
            individualRegistrationInfo.setRegistrationDate(TypeUtil.temporalToString(regDate));
        }
        individualRegistrationInfo.setRegistrationPlace(rs.getString(QUESTIONARY.REG_PLACE.getName()));

        russianIndividualEntity.setRegistrationInfo(RegistrationInfo.individual_registration_info(individualRegistrationInfo));

        final RussianPrivateEntity russianPrivateEntity = new RussianPrivateEntity();
        final PersonAnthroponym personAnthroponym = new PersonAnthroponym();
        personAnthroponym.setFirstName(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.PRIVATE_ENTITY_FIRST_NAME.getName()));
        personAnthroponym.setSecondName(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.PRIVATE_ENTITY_SECOND_NAME.getName()));
        personAnthroponym.setMiddleName(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.PRIVATE_ENTITY_MIDDLE_NAME.getName()));
        russianPrivateEntity.setFio(personAnthroponym);
        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPhoneNumber(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.PRIVATE_ENTITY_PHONE_NUMBER.getName()));
        contactInfo.setEmail(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.PRIVATE_ENTITY_EMAIL.getName()));
        russianPrivateEntity.setContactInfo(contactInfo);
        russianPrivateEntity.setActualAddress(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.PRIVATE_ENTITY_ACTUAL_ADDRESS.getName()));
        final LocalDateTime birthDate = rs.getObject(INDIVIDUAL_ENTITY_QUESTIONARY.PRIVATE_ENTITY_BIRTH_DATE.getName(), LocalDateTime.class);
        if (birthDate != null) {
            russianPrivateEntity.setBirthDate(TypeUtil.temporalToString(birthDate));
        }
        russianPrivateEntity.setBirthPlace(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.PRIVATE_ENTITY_BIRTH_PLACE.getName()));
        russianPrivateEntity.setResidenceAddress(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.PRIVATE_ENTITY_RESIDENCE_ADDRESS.getName()));
        russianPrivateEntity.setCitizenship(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.PRIVATE_ENTITY_CITIZENSHIP.getName()));
        russianIndividualEntity.setRussianPrivateEntity(russianPrivateEntity);

        final LicenseInfo licenseInfo = new LicenseInfo();
        final LocalDateTime licenseIssueDate = rs.getObject(QUESTIONARY.LICENSE_ISSUE_DATE.getName(), LocalDateTime.class);
        if (licenseIssueDate != null) {
            licenseInfo.setIssueDate(TypeUtil.temporalToString(licenseIssueDate));
        }
        final LocalDateTime licenseEffectiveDate = rs.getObject(QUESTIONARY.LICENSE_EFFECTIVE_DATE.getName(), LocalDateTime.class);
        if (licenseEffectiveDate != null) {
            licenseInfo.setEffectiveDate(TypeUtil.temporalToString(licenseEffectiveDate));
        }
        final LocalDateTime licenseExpDate = rs.getObject(QUESTIONARY.LICENSE_EXPIRATION_DATE.getName(), LocalDateTime.class);
        if (licenseExpDate != null) {
            licenseInfo.setExpirationDate(TypeUtil.temporalToString(licenseExpDate));
        }
        licenseInfo.setLicensedActivity(rs.getString(QUESTIONARY.LICENSE_LICENSED_ACTIVITY.getName()));
        licenseInfo.setOfficialNum(rs.getString(QUESTIONARY.LICENSE_OFFICIAL_NUM.getName()));
        licenseInfo.setIssuerName(rs.getString(QUESTIONARY.LICENSE_ISSUER_NAME.getName()));
        russianIndividualEntity.setLicenseInfo(licenseInfo);

        final IdentityDocument identityDocument = new IdentityDocument();
        final RussianDomesticPassport russianDomesticPassport = new RussianDomesticPassport();
        russianDomesticPassport.setIssuerCode(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.IDENTITY_DOC_ISSUER_CODE.getName()));
        russianDomesticPassport.setIssuer(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.IDENTITY_DOC_ISSUER.getName()));
        russianDomesticPassport.setIssuedAt(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.IDENTITY_DOC_ISSUED_AT.getName()));
        russianDomesticPassport.setNumber(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.IDENTITY_DOC_NUMBER.getName()));
        russianDomesticPassport.setSeries(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.IDENTITY_DOC_SERIES.getName()));
        identityDocument.setRussianDomesticPassword(russianDomesticPassport);
        russianIndividualEntity.setIdentityDocument(identityDocument);

        final MigrationCardInfo migrationCardInfo = new MigrationCardInfo();
        migrationCardInfo.setCardNumber(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.MIGRATION_CARD_NUMBER.getName()));
        migrationCardInfo.setBeginningDate(rs.getString(LEGAL_OWNER.MIGRATION_CARD_BEGINNING_DATE.getName()));
        migrationCardInfo.setExpirationDate(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.MIGRATION_CARD_EXPIRATION_DATE.getName()));
        russianIndividualEntity.setMigrationCardInfo(migrationCardInfo);

        final ResidenceApprove residenceApprove = new ResidenceApprove();
        residenceApprove.setName(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.RESIDENCE_APPROVE_NAME.getName()));
        residenceApprove.setNumber(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.RESIDENCE_APPROVE_NUMBER.getName()));
        residenceApprove.setSeries(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.RESIDENCE_APPROVE_SERIES.getName()));
        residenceApprove.setBeginningDate(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.RESIDENCE_APPROVE_BEGINNING_DATE.getName()));
        residenceApprove.setExpirationDate(rs.getString(INDIVIDUAL_ENTITY_QUESTIONARY.RESIDENCE_APPROVE_EXPIRATION_DATE.getName()));
        russianIndividualEntity.setResidenceApprove(residenceApprove);

        return IndividualEntity.russian_individual_entity(russianIndividualEntity);
    }
}
