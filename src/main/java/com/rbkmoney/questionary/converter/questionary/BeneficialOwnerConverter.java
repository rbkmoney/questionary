package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.enums.IdentityDocumentType;
import com.rbkmoney.questionary.domain.enums.ResidencyInfoType;
import com.rbkmoney.questionary.util.ThriftUtil;
import org.springframework.stereotype.Component;

@Component
public class BeneficialOwnerConverter implements ThriftConverter<BeneficialOwner, com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner>,
        JooqConverter<com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner, BeneficialOwner> {

    @Override
    public BeneficialOwner toThrift(com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner value, ThriftConverterContext ctx) {
        final BeneficialOwner beneficialOwner = new BeneficialOwner();
        beneficialOwner.setSnils(value.getSnils());
        beneficialOwner.setPdlCategory(value.getPdlCategory());
        beneficialOwner.setInn(value.getInn());
        beneficialOwner.setPdlRelationDegree(value.getPdlRelationDegree());
        if (value.getOwnershipPercentage() != null) {
            beneficialOwner.setOwnershipPercentage(value.getOwnershipPercentage().byteValue());
        }

        ResidencyInfo residencyInfo = new ResidencyInfo();
        if (value.getResidencyInfoType() == ResidencyInfoType.individual) {
            IndividualResidencyInfo individualResidencyInfo = new IndividualResidencyInfo();
            individualResidencyInfo.setExceptUsaTaxResident(value.getExceptUsaTaxResident());
            individualResidencyInfo.setUsaTaxResident(value.getUsaTaxResident());
            residencyInfo.setIndividualResidencyInfo(individualResidencyInfo);
        } else if (value.getResidencyInfoType() == ResidencyInfoType.legal) {
            LegalResidencyInfo legalResidencyInfo = new LegalResidencyInfo();
            legalResidencyInfo.setFatca(value.getFatca());
            legalResidencyInfo.setOwnerResident(value.getOwnerResident());
            legalResidencyInfo.setTaxResident(value.getTaxResident());
            residencyInfo.setLegalResidencyInfo(legalResidencyInfo);
        }
        ThriftUtil.setIfNotEmpty(residencyInfo, beneficialOwner::setResidencyInfo);

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
        ThriftUtil.setIfNotEmpty(residenceApprove, beneficialOwner::setResidenceApprove);

        final MigrationCardInfo migrationCardInfo = new MigrationCardInfo();
        if (value.getMigrationCardExpirationDate() != null) {
            migrationCardInfo.setExpirationDate(TypeUtil.temporalToString(value.getMigrationCardExpirationDate()));
        }
        if (value.getMigrationCardBeginningDate() != null) {
            migrationCardInfo.setBeginningDate(TypeUtil.temporalToString(value.getMigrationCardBeginningDate()));
        }
        migrationCardInfo.setCardNumber(value.getMigrationCardNumber());
        ThriftUtil.setIfNotEmpty(migrationCardInfo, beneficialOwner::setMigrationCardInfo);

        final IdentityDocument identityDocument = new IdentityDocument();
        RussianDomesticPassport russianDomesticPassport = new RussianDomesticPassport();
        russianDomesticPassport.setSeriesNumber(value.getIdentityDocSeriesNumber());
        if (value.getIdentityDocIssuedAt() != null) {
            russianDomesticPassport.setIssuedAt(TypeUtil.temporalToString(value.getIdentityDocIssuedAt()));
        }
        russianDomesticPassport.setIssuer(value.getIdentityDocIssuer());
        russianDomesticPassport.setIssuerCode(value.getIdentityDocIssuerCode());
        ThriftUtil.setIfNotEmpty(russianDomesticPassport, identityDocument::setRussianDomesticPassword);
        ThriftUtil.setIfNotEmpty(identityDocument, beneficialOwner::setIdentityDocument);

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
        ThriftUtil.setIfNotEmpty(contactInfo, russianPrivateEntity::setContactInfo);

        russianPrivateEntity.setFio(value.getPrivateEntityFio());

        beneficialOwner.setRussianPrivateEntity(russianPrivateEntity);

        return beneficialOwner;
    }

    @Override
    public com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner toJooq(BeneficialOwner value, JooqConverterContext ctx) {
        var beneficialOwner = new com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner();
        beneficialOwner.setSnils(value.getSnils());
        beneficialOwner.setInn(value.getInn());
        beneficialOwner.setOwnershipPercentage((short) value.getOwnershipPercentage());
        beneficialOwner.setPdlCategory(value.isPdlCategory());
        beneficialOwner.setPdlRelationDegree(value.getPdlRelationDegree());
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
        }
        if (value.isSetResidencyInfo()) {
            if (value.getResidencyInfo().isSetIndividualResidencyInfo()) {
                beneficialOwner.setUsaTaxResident(value.getResidencyInfo().getIndividualResidencyInfo().isUsaTaxResident());
                beneficialOwner.setExceptUsaTaxResident(value.getResidencyInfo().getIndividualResidencyInfo().isExceptUsaTaxResident());
                beneficialOwner.setResidencyInfoType(ResidencyInfoType.individual);
            } else if (value.getResidencyInfo().isSetLegalResidencyInfo()) {
                beneficialOwner.setTaxResident(value.getResidencyInfo().getLegalResidencyInfo().isTaxResident());
                beneficialOwner.setOwnerResident(value.getResidencyInfo().getLegalResidencyInfo().isOwnerResident());
                beneficialOwner.setFatca(value.getResidencyInfo().getLegalResidencyInfo().isFatca());
                beneficialOwner.setResidencyInfoType(ResidencyInfoType.legal);
            }
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
                beneficialOwner.setIdentityDocSeriesNumber(russianPassport.getSeriesNumber());
            }
        }

        if (value.isSetRussianPrivateEntity()) {
            if (value.getRussianPrivateEntity().isSetFio()) {
                beneficialOwner.setPrivateEntityFio(value.getRussianPrivateEntity().getFio());
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
