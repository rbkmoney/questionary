package com.rbkmoney.questionary.dao.mapper;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.rbkmoney.questionary.domain.Tables.LEGAL_ENTITY_QUESTIONARY;
import static com.rbkmoney.questionary.domain.Tables.QUESTIONARY;

public class LegalEntityQuestionaryMapper implements RowMapper<LegalEntity> {

    private final LegalOwnerMapper legalOwnerMapper;

    public LegalEntityQuestionaryMapper() {
        this.legalOwnerMapper = new LegalOwnerMapper();
    }

    @Override
    public LegalEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        final RussianLegalEntity russianLegalEntity = new RussianLegalEntity();
        russianLegalEntity.setName(rs.getString(LEGAL_ENTITY_QUESTIONARY.NAME.getName()));
        russianLegalEntity.setForeignName(rs.getString(LEGAL_ENTITY_QUESTIONARY.FOREIGN_NAME.getName()));
        russianLegalEntity.setLegalForm(rs.getString(LEGAL_ENTITY_QUESTIONARY.LEGAL_FORM.getName()));
        russianLegalEntity.setInn(rs.getString(QUESTIONARY.INN.getName()));
        russianLegalEntity.setAdditionalSpace(rs.getString(LEGAL_ENTITY_QUESTIONARY.ADDITIONAL_SPACE.getName()));
        russianLegalEntity.setOkatoCode(rs.getString(LEGAL_ENTITY_QUESTIONARY.OKATO_CODE.getName()));
        russianLegalEntity.setOkpoCode(rs.getString(LEGAL_ENTITY_QUESTIONARY.OKPO_CODE.getName()));
        russianLegalEntity.setPostalAddress(rs.getString(LEGAL_ENTITY_QUESTIONARY.POSTAL_ADDRESS.getName()));

        final LegalRegistrationInfo legalRegistrationInfo = new LegalRegistrationInfo();
        legalRegistrationInfo.setRegistrationAddress(rs.getString(LEGAL_ENTITY_QUESTIONARY.REG_ADDRESS.getName()));
        legalRegistrationInfo.setRegistrationDate(rs.getString(QUESTIONARY.REG_DATE.getName()));
        legalRegistrationInfo.setRegistrationPlace(rs.getString(QUESTIONARY.REG_PLACE.getName()));
        legalRegistrationInfo.setOgrn(rs.getString(LEGAL_ENTITY_QUESTIONARY.OGRN.getName()));
        legalRegistrationInfo.setActualAddress(rs.getString(LEGAL_ENTITY_QUESTIONARY.REG_ACTUAL_ADDRESS.getName()));
        russianLegalEntity.setRegistrationInfo(RegistrationInfo.legal_registration_info(legalRegistrationInfo));

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
        russianLegalEntity.setLicenseInfo(licenseInfo);

        final LegalResidencyInfo legalResidencyInfo = new LegalResidencyInfo();
        legalResidencyInfo.setTaxResident(rs.getBoolean(LEGAL_ENTITY_QUESTIONARY.TAX_RESIDENT.getName()));
        legalResidencyInfo.setFatca(rs.getBoolean(LEGAL_ENTITY_QUESTIONARY.FATCA.getName()));
        legalResidencyInfo.setOwnerResident(rs.getBoolean(LEGAL_ENTITY_QUESTIONARY.OWNER_RESIDENT.getName()));
        russianLegalEntity.setResidencyInfo(ResidencyInfo.legal_residency_info(legalResidencyInfo));

        final FoundersInfo foundersInfo = new FoundersInfo();
        final Head legalOwner = new Head();
        final IndividualPerson loIndividualPerson = new IndividualPerson();
        final PersonAnthroponym loPersonAnthroponym = new PersonAnthroponym();
        loPersonAnthroponym.setFirstName(rs.getString(LEGAL_ENTITY_QUESTIONARY.FOUNDER_OWNER_FIRST_NAME.getName()));
        loPersonAnthroponym.setSecondName(rs.getString(LEGAL_ENTITY_QUESTIONARY.FOUNDER_OWNER_SECOND_NAME.getName()));
        loPersonAnthroponym.setMiddleName(rs.getString(LEGAL_ENTITY_QUESTIONARY.FOUNDER_OWNER_MIDDLE_NAME.getName()));
        loIndividualPerson.setFio(loPersonAnthroponym);
        loIndividualPerson.setInn(rs.getString(LEGAL_ENTITY_QUESTIONARY.FOUNDER_OWNER_INN.getName()));
        legalOwner.setIndividualPerson(loIndividualPerson);
        legalOwner.setPosition(rs.getString(LEGAL_ENTITY_QUESTIONARY.FOUNDER_OWNER_POSITION.getName()));
        foundersInfo.setLegalOwner(legalOwner);
        russianLegalEntity.setFoundersInfo(foundersInfo);

        russianLegalEntity.setLegalOwnerInfo(legalOwnerMapper.mapRow(rs, rowNum));

        return LegalEntity.russian_legal_entity(russianLegalEntity);
    }

}
