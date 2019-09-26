package com.rbkmoney.questionary;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.geck.serializer.kit.mock.MockMode;
import com.rbkmoney.geck.serializer.kit.mock.MockTBaseProcessor;
import com.rbkmoney.geck.serializer.kit.tbase.TBaseHandler;
import com.rbkmoney.questionary.manage.Head;
import com.rbkmoney.questionary.manage.*;
import com.rbkmoney.questionary.service.QuestionaryService;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static com.rbkmoney.questionary.TestData.*;

public class QuestionaryTest extends AbstractIntegrationTest {

    @Autowired
    private QuestionaryService questionaryService;

    @Test
    public void ipQuestionaryTest() throws QuestionaryVersionConflict, QuestionaryNotFound, IOException {
        final QuestionaryParams questionaryParams = buildQuestionaryParams(EntityType.INDIVIDUAL);
        final long questionaryVersion = questionaryService.saveQuestionary(questionaryParams, 2L);
        final Reference reference = new Reference();
        reference.setVersion(questionaryVersion);
        final Snapshot questionarySnapshot = questionaryService.getQuestionary("54376457", reference);
        compareIndividualQuestionary(questionaryParams.getData(), questionarySnapshot.getQuestionary().getData());
    }

    @Test
    public void legalQuestionaryTest() throws QuestionaryVersionConflict, QuestionaryNotFound, IOException {
        final QuestionaryParams questionaryParams = buildQuestionaryParams(EntityType.LEGAL);
        final long questionaryVersion = questionaryService.saveQuestionary(questionaryParams, 1L);
        final Reference reference = new Reference();
        reference.setHead(new Head());
        final Snapshot questionarySnapshot = questionaryService.getQuestionary("54376457", reference);
        compareLegalQuestionary(questionaryParams.getData(), questionarySnapshot.getQuestionary().getData());
    }

    @Test
    public void questionaryVersionTest() throws QuestionaryVersionConflict, QuestionaryNotFound {
        final QuestionaryParams questionaryParams = new QuestionaryParams();
        questionaryParams.setId("765432634");
        questionaryParams.setOwnerId("64");

        final QuestionaryData questionaryData = new QuestionaryData();
        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail("test@mail.com");
        contactInfo.setPhoneNumber("7903245126");
        questionaryData.setContactInfo(contactInfo);

        questionaryParams.setData(questionaryData);

        final long firstQuestionaryVersion = questionaryService.saveQuestionary(questionaryParams, 0L);
        final Snapshot firstQuestionary = questionaryService.getQuestionary("765432634", Reference.head(new Head()));

        firstQuestionary.getQuestionary().getData().getContactInfo().setEmail("test2@mail.com");
        questionaryParams.setData(firstQuestionary.getQuestionary().getData());
        final long secondQuestionaryVersion = questionaryService.saveQuestionary(questionaryParams, firstQuestionaryVersion);
        final Snapshot secondQuestionary = questionaryService.getQuestionary("765432634", Reference.version(secondQuestionaryVersion));

        Assert.assertEquals("test2@mail.com", secondQuestionary.getQuestionary().getData().getContactInfo().getEmail());
        Assert.assertEquals(2L, secondQuestionaryVersion);
    }

    @Test(expected = QuestionaryVersionConflict.class)
    public void questionaryDuplicateVersionTest() throws QuestionaryVersionConflict {
        final QuestionaryParams questionaryParams = new QuestionaryParams();
        questionaryParams.setId("87723261");
        questionaryParams.setOwnerId("64");

        final QuestionaryData questionaryData = new QuestionaryData();
        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail("test@mail.com");
        contactInfo.setPhoneNumber("7903245126");
        questionaryData.setContactInfo(contactInfo);

        questionaryParams.setData(questionaryData);

        questionaryService.saveQuestionary(questionaryParams, 1L);
        questionaryService.saveQuestionary(questionaryParams, 1L);
    }

    private void compareIndividualQuestionary(QuestionaryData data, QuestionaryData questionaryData) {
        Assert.assertEquals("ShopInfo (description) not equals", data.getShopInfo().getDetails().getDescription(),
                questionaryData.getShopInfo().getDetails().getDescription());
        Assert.assertEquals("ShopInfo (name) not equals", data.getShopInfo().getDetails().getName(), questionaryData.getShopInfo().getDetails().getName());

        compareBankAccount(data.getBankAccount(), questionaryData.getBankAccount());

        compareContactInfo(data.getContactInfo(), questionaryData.getContactInfo());

        RussianIndividualEntity expectedRussianIndividualEntity = data.getContractor().getIndividualEntity().getRussianIndividualEntity();
        RussianIndividualEntity actualRussianIndividualEntity = questionaryData.getContractor().getIndividualEntity().getRussianIndividualEntity();
        Assert.assertEquals("RussianIndividualEntity (inn) not equals",
                expectedRussianIndividualEntity.getInn(), actualRussianIndividualEntity.getInn());
        Assert.assertEquals("RussianIndividualEntity (snils) not equals",
                expectedRussianIndividualEntity.getSnils(), actualRussianIndividualEntity.getSnils());

        compareLicenseInfo(expectedRussianIndividualEntity.getLicenseInfo(), actualRussianIndividualEntity.getLicenseInfo());

        compareRussianPrivateEntity(expectedRussianIndividualEntity.getRussianPrivateEntity(),
                actualRussianIndividualEntity.getRussianPrivateEntity());

        compareIndividualPersonCategories(expectedRussianIndividualEntity.getIndividualPersonCategories(),
                actualRussianIndividualEntity.getIndividualPersonCategories());

        compareResidenceApprove(expectedRussianIndividualEntity.getResidenceApprove(),
                actualRussianIndividualEntity.getResidenceApprove());

        compareMigrationCardInfo(expectedRussianIndividualEntity.getMigrationCardInfo(),
                actualRussianIndividualEntity.getMigrationCardInfo());

        compareRegistrationInfo(expectedRussianIndividualEntity.getRegistrationInfo(),
                actualRussianIndividualEntity.getRegistrationInfo());

        compareRussianDomesticPassword(expectedRussianIndividualEntity.getIdentityDocument().getRussianDomesticPassword(),
                actualRussianIndividualEntity.getIdentityDocument().getRussianDomesticPassword());

        comparePropertyInfoDocType(expectedRussianIndividualEntity.getPropertyInfoDocumentType(),
                actualRussianIndividualEntity.getPropertyInfoDocumentType());

        comparePrincipalActivity(expectedRussianIndividualEntity.getPrincipalActivity(), actualRussianIndividualEntity.getPrincipalActivity());

        compareResidencyInfo(expectedRussianIndividualEntity.getResidencyInfo(), actualRussianIndividualEntity.getResidencyInfo());

        for (int i = 0; i < expectedRussianIndividualEntity.getBeneficialOwners().size(); i++) {
            BeneficialOwner expectedBeneficialOwner = expectedRussianIndividualEntity.getBeneficialOwners().get(i);
            BeneficialOwner actualBeneficialOwner = actualRussianIndividualEntity.getBeneficialOwners().get(i);
            compareBeneficialOwner(expectedBeneficialOwner, actualBeneficialOwner);
        }

        compareAdditionalInfo(expectedRussianIndividualEntity.getAdditionalInfo(), actualRussianIndividualEntity.getAdditionalInfo());
    }

    private void compareLegalQuestionary(QuestionaryData data, QuestionaryData questionaryData) {
        Assert.assertEquals("ShopInfo (description) not equals", data.getShopInfo().getDetails().getDescription(),
                questionaryData.getShopInfo().getDetails().getDescription());
        Assert.assertEquals("ShopInfo (name) not equals", data.getShopInfo().getDetails().getName(), questionaryData.getShopInfo().getDetails().getName());

        compareBankAccount(data.getBankAccount(), questionaryData.getBankAccount());

        compareContactInfo(data.getContactInfo(), questionaryData.getContactInfo());

        RussianLegalEntity expectedLegalEntity = data.getContractor().getLegalEntity().getRussianLegalEntity();
        RussianLegalEntity actualLegalEntity = questionaryData.getContractor().getLegalEntity().getRussianLegalEntity();

        Assert.assertEquals("RussianLegalEntity (inn) not equals",
                expectedLegalEntity.getInn(), actualLegalEntity.getInn());
        Assert.assertEquals("RussianLegalEntity (name) not equals",
                expectedLegalEntity.getName(), actualLegalEntity.getName());
        Assert.assertEquals("RussianLegalEntity (additionalSpace) not equals",
                expectedLegalEntity.getAdditionalSpace(), actualLegalEntity.getAdditionalSpace());
        Assert.assertEquals("RussianLegalEntity (foreignName) not equals",
                expectedLegalEntity.getForeignName(), actualLegalEntity.getForeignName());
        Assert.assertEquals("RussianLegalEntity (legalForm) not equals",
                expectedLegalEntity.getLegalForm(), actualLegalEntity.getLegalForm());
        Assert.assertEquals("RussianLegalEntity (okatoCode) not equals",
                expectedLegalEntity.getOkatoCode(), actualLegalEntity.getOkatoCode());
        Assert.assertEquals("RussianLegalEntity (okpoCode) not equals",
                expectedLegalEntity.getOkpoCode(), actualLegalEntity.getOkpoCode());
        Assert.assertEquals("RussianLegalEntity (postalAddress) not equals",
                expectedLegalEntity.getPostalAddress(), actualLegalEntity.getPostalAddress());
        Assert.assertEquals("RussianLegalEntity (additionalSpace) not equals",
                expectedLegalEntity.getAdditionalSpace(), actualLegalEntity.getAdditionalSpace());

        compareFoundersInfo(expectedLegalEntity.getFoundersInfo(), actualLegalEntity.getFoundersInfo());

        compareLicenseInfo(expectedLegalEntity.getLicenseInfo(), actualLegalEntity.getLicenseInfo());

        comparePropertyInfoDocType(expectedLegalEntity.getPropertyInfoDocumentType(), actualLegalEntity.getPropertyInfoDocumentType());

        compareRegistrationInfo(expectedLegalEntity.getRegistrationInfo(), actualLegalEntity.getRegistrationInfo());

        compareResidencyInfo(expectedLegalEntity.getResidencyInfo(), actualLegalEntity.getResidencyInfo());

        comparePrincipalActivity(expectedLegalEntity.getPrincipalActivity(), actualLegalEntity.getPrincipalActivity());

        compareLegalOwnerInfo(expectedLegalEntity.getLegalOwnerInfo(), actualLegalEntity.getLegalOwnerInfo());

        for (int i = 0; i < expectedLegalEntity.getBeneficialOwners().size(); i++) {
            BeneficialOwner expectedBeneficialOwner = expectedLegalEntity.getBeneficialOwners().get(i);
            BeneficialOwner actualBeneficialOwner = actualLegalEntity.getBeneficialOwners().get(i);
            compareBeneficialOwner(expectedBeneficialOwner, actualBeneficialOwner);
        }

        compareAdditionalInfo(expectedLegalEntity.getAdditionalInfo(), actualLegalEntity.getAdditionalInfo());
    }

    private void compareLegalOwnerInfo(LegalOwnerInfo expectedLegalOwnerInfo, LegalOwnerInfo actualLegalOwnerInfo) {
        Assert.assertEquals("LegalOwnerInfo (inn) not equals",
                expectedLegalOwnerInfo.getInn(), actualLegalOwnerInfo.getInn());
        Assert.assertEquals("LegalOwnerInfo (snils) not equals",
                expectedLegalOwnerInfo.getSnils(), actualLegalOwnerInfo.getSnils());
        Assert.assertEquals("LegalOwnerInfo (pdlRelationDegree) not equals",
                expectedLegalOwnerInfo.getPdlRelationDegree(), actualLegalOwnerInfo.getPdlRelationDegree());
        Assert.assertEquals("LegalOwnerInfo (termOfOffice) not equals",
                expectedLegalOwnerInfo.getTermOfOffice(), actualLegalOwnerInfo.getTermOfOffice());
        compareRussianPrivateEntity(expectedLegalOwnerInfo.getRussianPrivateEntity(), actualLegalOwnerInfo.getRussianPrivateEntity());
        compareResidenceApprove(expectedLegalOwnerInfo.getResidenceApprove(), actualLegalOwnerInfo.getResidenceApprove());
        compareAuthorityConfirmationDocument(expectedLegalOwnerInfo.getAuthorityConfirmingDocument(),
                actualLegalOwnerInfo.getAuthorityConfirmingDocument());
        compareMigrationCardInfo(expectedLegalOwnerInfo.getMigrationCardInfo(), actualLegalOwnerInfo.getMigrationCardInfo());
        compareRussianDomesticPassword(expectedLegalOwnerInfo.getIdentityDocument().getRussianDomesticPassword(),
                actualLegalOwnerInfo.getIdentityDocument().getRussianDomesticPassword());
    }

    private void compareAuthorityConfirmationDocument(AuthorityConfirmingDocument exceptedAuthorityConfirmingDocument,
                                                      AuthorityConfirmingDocument actualAuthorityConfirmingDocument) {
        Assert.assertEquals("AuthorityConfirmationDocument (date) not equals",
                exceptedAuthorityConfirmingDocument.getDate(), actualAuthorityConfirmingDocument.getDate());
        Assert.assertEquals("AuthorityConfirmationDocument (number) not equals",
                exceptedAuthorityConfirmingDocument.getNumber(), actualAuthorityConfirmingDocument.getNumber());
        Assert.assertEquals("AuthorityConfirmationDocument (type) not equals",
                exceptedAuthorityConfirmingDocument.getType(), actualAuthorityConfirmingDocument.getType());
    }

    private void compareFoundersInfo(FoundersInfo expectedFoundersInfo, FoundersInfo actualFoundersInfo) {
        compareLegalOwner(expectedFoundersInfo.getLegalOwner(), actualFoundersInfo.getLegalOwner());
        for (int i = 0; i < expectedFoundersInfo.getFounders().size(); i++) {
            Founder expectedFounder = expectedFoundersInfo.getFounders().get(i);
            Founder actualFounder = actualFoundersInfo.getFounders().get(i);
            compareFounder(expectedFounder, actualFounder);
        }
        for (int i = 0; i < expectedFoundersInfo.getHeads().size(); i++) {
            com.rbkmoney.questionary.Head expectedHead = expectedFoundersInfo.getHeads().get(i);
            com.rbkmoney.questionary.Head actualHead = actualFoundersInfo.getHeads().get(i);
            compareLegalOwner(expectedHead, actualHead);
        }
    }

    private void compareFounder(Founder expectedFounder, Founder actualFounder) {
        if (expectedFounder.isSetIndividualPersonFounder()) {
            Assert.assertEquals("IndividualPersonFounder (inn) not equals",
                    expectedFounder.getIndividualPersonFounder().getInn(), actualFounder.getIndividualPersonFounder().getInn());
            comparePersonAnthroponym(expectedFounder.getIndividualPersonFounder().getFio(),
                    actualFounder.getIndividualPersonFounder().getFio());
        } else if (expectedFounder.isSetInternationalLegalEntityFounder()) {
            Assert.assertEquals("InternationalLegalEntityFounder (country) not equals",
                    expectedFounder.getInternationalLegalEntityFounder().getCountry(),
                    actualFounder.getInternationalLegalEntityFounder().getCountry());
            Assert.assertEquals("InternationalLegalEntityFounder (fullName) not equals",
                    expectedFounder.getInternationalLegalEntityFounder().getFullName(),
                    actualFounder.getInternationalLegalEntityFounder().getFullName());
        } else if (expectedFounder.isSetRussianLegalEntityFounder()) {
            Assert.assertEquals("RussianLegalEntityFounder (fullName) not equals",
                    expectedFounder.getRussianLegalEntityFounder().getFullName(),
                    actualFounder.getRussianLegalEntityFounder().getFullName());
            Assert.assertEquals("RussianLegalEntityFounder (inn) not equals",
                    expectedFounder.getRussianLegalEntityFounder().getInn(),
                    actualFounder.getRussianLegalEntityFounder().getInn());
            Assert.assertEquals("RussianLegalEntityFounder (ogrn) not equals",
                    expectedFounder.getRussianLegalEntityFounder().getOgrn(),
                    actualFounder.getRussianLegalEntityFounder().getOgrn());
        }
    }

    private void compareLegalOwner(com.rbkmoney.questionary.Head expectedLegalOwner, com.rbkmoney.questionary.Head actualLegalOwner) {
        Assert.assertEquals("LegalOwner (position) not equals",
                expectedLegalOwner.getPosition(), actualLegalOwner.getPosition());
        Assert.assertEquals("LegalOwner (inn) not equals",
                expectedLegalOwner.getIndividualPerson().getInn(), actualLegalOwner.getIndividualPerson().getInn());
        comparePersonAnthroponym(expectedLegalOwner.getIndividualPerson().getFio(), actualLegalOwner.getIndividualPerson().getFio());
    }

    private void compareAdditionalInfo(AdditionalInfo expectedAdditionalInfo, AdditionalInfo actualAdditionalInfo) {
        compareAccountantInfo(expectedAdditionalInfo.getAccountantInfo(), actualAdditionalInfo.getAccountantInfo());
        for (int i = 0; i < expectedAdditionalInfo.getBusinessInfo().size(); i++) {
            BusinessInfo expectedBusinessInfo = expectedAdditionalInfo.getBusinessInfo().get(i);
            BusinessInfo actualBusinessInfo = actualAdditionalInfo.getBusinessInfo().get(i);
            compareBusinessInfo(expectedBusinessInfo, actualBusinessInfo);
        }
        compareBusinessReputation(expectedAdditionalInfo.getBusinessReputation(), actualAdditionalInfo.getBusinessReputation());
        for (int i = 0; i < expectedAdditionalInfo.getFinancialPosition().size(); i++) {
            FinancialPosition expectedFinancialPosition = expectedAdditionalInfo.getFinancialPosition().get(i);
            FinancialPosition actualFinancialPosition = actualAdditionalInfo.getFinancialPosition().get(i);
            compareFinancialPosition(expectedFinancialPosition, actualFinancialPosition);
        }
        Assert.assertEquals("AdditionalInfo (mainCounterparties) not equals",
                expectedAdditionalInfo.getMainCounterparties(),
                actualAdditionalInfo.getMainCounterparties());
        Assert.assertEquals("AdditionalInfo (nkoRelationTarget) not equals",
                expectedAdditionalInfo.getNKORelationTarget(),
                actualAdditionalInfo.getNKORelationTarget());
        Assert.assertEquals("AdditionalInfo (relationWithNKO) not equals",
                expectedAdditionalInfo.getRelationshipWithNKO(),
                actualAdditionalInfo.getRelationshipWithNKO());
        Assert.assertEquals("AdditionalInfo (monthOperationCount) not equals",
                expectedAdditionalInfo.getMonthOperationCount().getValue(),
                actualAdditionalInfo.getMonthOperationCount().getValue());
        Assert.assertEquals("AdditionalInfo (monthOperationSum) not equals",
                expectedAdditionalInfo.getMonthOperationSum().getValue(),
                actualAdditionalInfo.getMonthOperationSum().getValue());
        Assert.assertEquals("AdditionalInfo (staffCount) not equals",
                expectedAdditionalInfo.getStaffCount(),
                actualAdditionalInfo.getStaffCount());
        //compareBankAccount(expectedAdditionalInfo.getBankAccount(), actualAdditionalInfo.getBankAccount()); // TODO: Лишнее?
    }

    private void compareBankAccount(BankAccount expectedBankAccount, BankAccount actualBankAccount) {
        Assert.assertEquals("AdditionalInfo (bankPostAccount) not equals",
                expectedBankAccount.getRussianBankAccount().getBankPostAccount(),
                actualBankAccount.getRussianBankAccount().getBankPostAccount());
        Assert.assertEquals("AdditionalInfo (bankName) not equals",
                expectedBankAccount.getRussianBankAccount().getBankName(),
                actualBankAccount.getRussianBankAccount().getBankName());
        Assert.assertEquals("AdditionalInfo (bankBik) not equals",
                expectedBankAccount.getRussianBankAccount().getBankBik(),
                actualBankAccount.getRussianBankAccount().getBankBik());
        Assert.assertEquals("AdditionalInfo (bankAccount) not equals",
                expectedBankAccount.getRussianBankAccount().getAccount(),
                actualBankAccount.getRussianBankAccount().getAccount());
    }

    private void compareFinancialPosition(FinancialPosition expectedFinancialPosition, FinancialPosition actualFinancialPosition) {
        Assert.assertEquals("FinancialPosition (anualFinancialStatements) not equals",
                expectedFinancialPosition.isSetAnnualFinancialStatements(),
                actualFinancialPosition.isSetAnnualFinancialStatements());
        Assert.assertEquals("FinancialPosition (annualTaxReturnWithMark) not equals",
                expectedFinancialPosition.isSetAnnualTaxReturnWithMark(),
                actualFinancialPosition.isSetAnnualTaxReturnWithMark());
        Assert.assertEquals("FinancialPosition (annualTaxReturnWithoutMark) not equals",
                expectedFinancialPosition.isSetAnnualTaxReturnWithoutMark(),
                actualFinancialPosition.isSetAnnualTaxReturnWithoutMark());
        Assert.assertEquals("FinancialPosition (annualTaxReturnWithoutMarkPaper) not equals",
                expectedFinancialPosition.isSetAnnualTaxReturnWithoutMarkPaper(),
                actualFinancialPosition.isSetAnnualTaxReturnWithoutMarkPaper());
        Assert.assertEquals("FinancialPosition (letterOfGuarantee) not equals",
                expectedFinancialPosition.isSetLetterOfGuarantee(),
                actualFinancialPosition.isSetLetterOfGuarantee());
        Assert.assertEquals("FinancialPosition (quarterlyTaxReturnWithMark) not equals",
                expectedFinancialPosition.isSetQuarterlyTaxReturnWithMark(),
                actualFinancialPosition.isSetQuarterlyTaxReturnWithMark());
        Assert.assertEquals("FinancialPosition (quarterlyTaxReturnWithoutMark) not equals",
                expectedFinancialPosition.isSetQuarterlyTaxReturnWithoutMark(),
                actualFinancialPosition.isSetQuarterlyTaxReturnWithoutMark());
        Assert.assertEquals("FinancialPosition (stateOfDuty) not equals",
                expectedFinancialPosition.isSetStatementOfDuty(),
                actualFinancialPosition.isSetStatementOfDuty());
    }

    private void compareBusinessReputation(BusinessReputation expectedBusinessReputation, BusinessReputation actualBusinessReputation) {
        Assert.assertEquals("BusinessReputation (value) not equals",
                expectedBusinessReputation.getValue(),
                actualBusinessReputation.getValue());
    }

    private void compareBusinessInfo(BusinessInfo expectedBusinessInfo, BusinessInfo actualBusinessInfo) {
        Assert.assertEquals("BusinessInfo (buildingBusiness) not equals",
                expectedBusinessInfo.isSetBuildingBusiness(),
                actualBusinessInfo.isSetBuildingBusiness());
        Assert.assertEquals("BusinessInfo (mediationInPropertyBusiness) not equals",
                expectedBusinessInfo.isSetMediationInPropertyBusiness(),
                actualBusinessInfo.isSetMediationInPropertyBusiness());
        Assert.assertEquals("BusinessInfo (productionBusiness) not equals",
                expectedBusinessInfo.isSetProductionBusiness(),
                actualBusinessInfo.isSetProductionBusiness());
        Assert.assertEquals("BusinessInfo (retailTradeBusiness) not equals",
                expectedBusinessInfo.isSetRetailTradeBusiness(),
                actualBusinessInfo.isSetRetailTradeBusiness());
        Assert.assertEquals("BusinessInfo (securitiesTradingBusiness) not equals",
                expectedBusinessInfo.isSetSecuritiesTradingBusiness(),
                actualBusinessInfo.isSetSecuritiesTradingBusiness());
        Assert.assertEquals("BusinessInfo (transportBusiness) not equals",
                expectedBusinessInfo.isSetTransportBusiness(),
                actualBusinessInfo.isSetTransportBusiness());
        Assert.assertEquals("BusinessInfo (wholesaleTradeBusiness) not equals",
                expectedBusinessInfo.isSetWholesaleTradeBusiness(),
                actualBusinessInfo.isSetWholesaleTradeBusiness());
    }

    private void compareAccountantInfo(AccountantInfo expectedAdditionalInfoAccountantInfo, AccountantInfo actualAdditionalInfoAccountantInfo) {
        Assert.assertEquals("AccountantInfo (withChiefAccountant) not equals",
                expectedAdditionalInfoAccountantInfo.isSetWithChiefAccountant(),
                actualAdditionalInfoAccountantInfo.isSetWithChiefAccountant());
        Assert.assertEquals("AccountantInfo (withoutChiefAccountant) not equals",
                expectedAdditionalInfoAccountantInfo.isSetWithoutChiefAccountant(),
                actualAdditionalInfoAccountantInfo.isSetWithoutChiefAccountant());
        if (expectedAdditionalInfoAccountantInfo.isSetWithoutChiefAccountant()) {
            Assert.assertEquals("AccountantInfo (accountingOrganization) not equals",
                    expectedAdditionalInfoAccountantInfo.getWithoutChiefAccountant().isSetAccountingOrganization(),
                    actualAdditionalInfoAccountantInfo.getWithoutChiefAccountant().isSetAccountingOrganization());
            Assert.assertEquals("AccountantInfo (headAccounting) not equals",
                    expectedAdditionalInfoAccountantInfo.getWithoutChiefAccountant().isSetHeadAccounting(),
                    actualAdditionalInfoAccountantInfo.getWithoutChiefAccountant().isSetHeadAccounting());
            Assert.assertEquals("AccountantInfo (individualAccountant) not equals",
                    expectedAdditionalInfoAccountantInfo.getWithoutChiefAccountant().isSetIndividualAccountant(),
                    actualAdditionalInfoAccountantInfo.getWithoutChiefAccountant().isSetIndividualAccountant());
            Assert.assertEquals("AccountantInfo (accountingOrganization-inn) not equals",
                    expectedAdditionalInfoAccountantInfo.getWithoutChiefAccountant().getAccountingOrganization().getInn(),
                    actualAdditionalInfoAccountantInfo.getWithoutChiefAccountant().getAccountingOrganization().getInn());
        }
    }


    private void compareRussianPrivateEntity(RussianPrivateEntity expectedRussianPrivateEntity,
                                             RussianPrivateEntity actualRussianPrivateEntity) {
        Assert.assertEquals("PrivateEntity (actualAddress) not equals",
                expectedRussianPrivateEntity.getActualAddress(),
                actualRussianPrivateEntity.getActualAddress());
        Assert.assertEquals("PrivateEntity (birthDate) not equals",
                expectedRussianPrivateEntity.getBirthDate(),
                actualRussianPrivateEntity.getBirthDate());
        Assert.assertEquals("PrivateEntity (residenceAddress) not equals",
                expectedRussianPrivateEntity.getResidenceAddress(),
                actualRussianPrivateEntity.getResidenceAddress());
        Assert.assertEquals("PrivateEntity (citizenship) not equals",
                expectedRussianPrivateEntity.getCitizenship(),
                actualRussianPrivateEntity.getCitizenship());
        compareContactInfo(expectedRussianPrivateEntity.getContactInfo(),
                actualRussianPrivateEntity.getContactInfo());
        comparePersonAnthroponym(expectedRussianPrivateEntity.getFio(),
                actualRussianPrivateEntity.getFio());
    }

    private void compareBeneficialOwner(BeneficialOwner expectedBeneficialOwner, BeneficialOwner actualBeneficialOwner) {
        Assert.assertEquals("BeneficialOwner (ownershipPercentage) not equals",
                expectedBeneficialOwner.getOwnershipPercentage(),
                actualBeneficialOwner.getOwnershipPercentage());
        Assert.assertEquals("BeneficialOwner (inn) not equals",
                expectedBeneficialOwner.getInn(),
                actualBeneficialOwner.getInn());
        Assert.assertEquals("BeneficialOwner (pdlRelationDegree) not equals",
                expectedBeneficialOwner.getPdlRelationDegree(),
                actualBeneficialOwner.getPdlRelationDegree());
        Assert.assertEquals("BeneficialOwner (snils) not equals",
                expectedBeneficialOwner.getSnils(),
                actualBeneficialOwner.getSnils());
        compareRussianPrivateEntity(expectedBeneficialOwner.getRussianPrivateEntity(),
                actualBeneficialOwner.getRussianPrivateEntity());
        compareResidenceApprove(expectedBeneficialOwner.getResidenceApprove(), actualBeneficialOwner.getResidenceApprove());
        compareMigrationCardInfo(expectedBeneficialOwner.getMigrationCardInfo(), actualBeneficialOwner.getMigrationCardInfo());
        compareRussianDomesticPassword(expectedBeneficialOwner.getIdentityDocument().getRussianDomesticPassword(),
                actualBeneficialOwner.getIdentityDocument().getRussianDomesticPassword());
        compareResidencyInfo(expectedBeneficialOwner.getResidencyInfo(), actualBeneficialOwner.getResidencyInfo());
    }

    private void compareContactInfo(ContactInfo expectedContactInfo, ContactInfo actualContactInfo) {
        Assert.assertEquals("ContactInfo (email) not equals", expectedContactInfo.getEmail(),
                actualContactInfo.getEmail());
        Assert.assertEquals("ContactInfo (phoneNumber) not equals", expectedContactInfo.getPhoneNumber(),
                actualContactInfo.getPhoneNumber());
    }

    private void comparePersonAnthroponym(PersonAnthroponym expectedPersonAnthroponym, PersonAnthroponym actualPersonAnthroponym) {
        Assert.assertEquals("PersonAnthroponym (firstName) not equals", expectedPersonAnthroponym.getFirstName(),
                actualPersonAnthroponym.getFirstName());
        Assert.assertEquals("PersonAnthroponym (secondName) not equals", expectedPersonAnthroponym.getSecondName(),
                actualPersonAnthroponym.getSecondName());
        Assert.assertEquals("PersonAnthroponym (middleName) not equals", expectedPersonAnthroponym.getMiddleName(),
                actualPersonAnthroponym.getMiddleName());
    }

    private void compareRegistrationInfo(RegistrationInfo expectedRegistrationInfo, RegistrationInfo actualRegistrationInfo) {
        if (expectedRegistrationInfo.isSetIndividualRegistrationInfo()) {
            Assert.assertEquals("RegistrationInfo-IP (ogrnip) not equals",
                    expectedRegistrationInfo.getIndividualRegistrationInfo().getOgrnip(),
                    actualRegistrationInfo.getIndividualRegistrationInfo().getOgrnip());
            Assert.assertEquals("RegistrationInfo-IP (registrationDate) not equals",
                    expectedRegistrationInfo.getIndividualRegistrationInfo().getRegistrationDate(),
                    actualRegistrationInfo.getIndividualRegistrationInfo().getRegistrationDate());
            Assert.assertEquals("RegistrationInfo-IP (registrationPlace) not equals",
                    expectedRegistrationInfo.getIndividualRegistrationInfo().getRegistrationPlace(),
                    actualRegistrationInfo.getIndividualRegistrationInfo().getRegistrationPlace());
        } else if (expectedRegistrationInfo.isSetLegalRegistrationInfo()) {
            Assert.assertEquals("RegistrationInfo-LE (registrationDate) not equals",
                    expectedRegistrationInfo.getLegalRegistrationInfo().getRegistrationDate(),
                    actualRegistrationInfo.getLegalRegistrationInfo().getRegistrationDate());
            Assert.assertEquals("RegistrationInfo-LE (registrationPlace) not equals",
                    expectedRegistrationInfo.getLegalRegistrationInfo().getRegistrationPlace(),
                    actualRegistrationInfo.getLegalRegistrationInfo().getRegistrationPlace());
            Assert.assertEquals("RegistrationInfo-LE (actualAddress) not equals",
                    expectedRegistrationInfo.getLegalRegistrationInfo().getActualAddress(),
                    actualRegistrationInfo.getLegalRegistrationInfo().getActualAddress());
            Assert.assertEquals("RegistrationInfo-LE (ogrn) not equals",
                    expectedRegistrationInfo.getLegalRegistrationInfo().getOgrn(),
                    actualRegistrationInfo.getLegalRegistrationInfo().getOgrn());
        }
    }

    private void compareLegalRegistrationInfo(LegalRegistrationInfo expectedRegistrationInfo,
                                                   LegalRegistrationInfo actualRegistrationInfo) {
        Assert.assertEquals("RegistrationInfo-LG (actualAddress) not equals",
                expectedRegistrationInfo.getActualAddress(),
                actualRegistrationInfo.getActualAddress());
        Assert.assertEquals("RegistrationInfo-LG (registrationDate) not equals",
                expectedRegistrationInfo.getRegistrationDate(),
                actualRegistrationInfo.getRegistrationDate());
        Assert.assertEquals("RegistrationInfo-LG (registrationPlace) not equals",
                expectedRegistrationInfo.getRegistrationPlace(),
                actualRegistrationInfo.getRegistrationPlace());
        Assert.assertEquals("RegistrationInfo-LG (registrationAddress) not equals",
                expectedRegistrationInfo.getRegistrationAddress(),
                actualRegistrationInfo.getRegistrationAddress());
        Assert.assertEquals("RegistrationInfo-LG (ogrn) not equals",
                expectedRegistrationInfo.getOgrn(),
                actualRegistrationInfo.getOgrn());
    }

    private void compareMigrationCardInfo(MigrationCardInfo expectedMigrationCardInfo,
                                          MigrationCardInfo actualMigrationCardInfo) {
        Assert.assertEquals("MigrationCardInfo (beginningDate) not equals",
                expectedMigrationCardInfo.getBeginningDate(),
                actualMigrationCardInfo.getBeginningDate());
        Assert.assertEquals("MigrationCardInfo (expirationDate) not equals",
                expectedMigrationCardInfo.getExpirationDate(),
                actualMigrationCardInfo.getExpirationDate());
        Assert.assertEquals("MigrationCardInfo (cardNumber) not equals",
                expectedMigrationCardInfo.getCardNumber(),
                actualMigrationCardInfo.getCardNumber());
    }

    private void compareRussianDomesticPassword(RussianDomesticPassport expectedRussianDomesticPassport,
                                                RussianDomesticPassport actualRussianDomesticPassport) {
        Assert.assertEquals("IdentityDocument-RU (issuedAt) not equals",
                expectedRussianDomesticPassport.getIssuedAt(),
                actualRussianDomesticPassport.getIssuedAt());
        Assert.assertEquals("IdentityDocument-RU (issuer) not equals",
                expectedRussianDomesticPassport.getIssuer(),
                actualRussianDomesticPassport.getIssuer());
        Assert.assertEquals("IdentityDocument-RU (issuerCode) not equals",
                expectedRussianDomesticPassport.getIssuerCode(),
                actualRussianDomesticPassport.getIssuerCode());
        Assert.assertEquals("IdentityDocument-RU (number) not equals",
                expectedRussianDomesticPassport.getNumber(),
                actualRussianDomesticPassport.getNumber());
        Assert.assertEquals("IdentityDocument-RU (series) not equals",
                expectedRussianDomesticPassport.getSeries(),
                actualRussianDomesticPassport.getSeries());
    }

    private void compareResidenceApprove(ResidenceApprove expectedResidenceApprove,
                                         ResidenceApprove actualResidenceApprove) {
        Assert.assertEquals("ResidenceApprove (beginningDate) not equals",
                expectedResidenceApprove.getBeginningDate(),
                actualResidenceApprove.getBeginningDate());
        Assert.assertEquals("ResidenceApprove (expirationDate) not equals",
                expectedResidenceApprove.getExpirationDate(),
                actualResidenceApprove.getExpirationDate());
        Assert.assertEquals("ResidenceApprove (name) not equals",
                expectedResidenceApprove.getName(),
                actualResidenceApprove.getName());
        Assert.assertEquals("ResidenceApprove (number) not equals",
                expectedResidenceApprove.getNumber(),
                actualResidenceApprove.getNumber());
        Assert.assertEquals("ResidenceApprove (series) not equals",
                expectedResidenceApprove.getSeries(),
                actualResidenceApprove.getSeries());
    }

    private void compareLicenseInfo(LicenseInfo expectedLicenseInfo, LicenseInfo actualLicenseInfo) {
        Assert.assertEquals("LicenseInfo (effectiveDate) not equals",
                expectedLicenseInfo.getEffectiveDate(),
                actualLicenseInfo.getEffectiveDate());
        Assert.assertEquals("LicenseInfo (expirationDate) not equals",
                expectedLicenseInfo.getExpirationDate(),
                actualLicenseInfo.getExpirationDate());
        Assert.assertEquals("LicenseInfo (issueDate) not equals",
                expectedLicenseInfo.getIssueDate(),
                actualLicenseInfo.getIssueDate());
        Assert.assertEquals("LicenseInfo (issuerName) not equals",
                expectedLicenseInfo.getIssuerName(),
                actualLicenseInfo.getIssuerName());
        Assert.assertEquals("LicenseInfo (issuerName) not equals",
                expectedLicenseInfo.getLicensedActivity(),
                actualLicenseInfo.getLicensedActivity());
        Assert.assertEquals("LicenseInfo (officialNum) not equals",
                expectedLicenseInfo.getOfficialNum(),
                actualLicenseInfo.getOfficialNum());
    }

    private void compareIndividualPersonCategories(IndividualPersonCategories expectedIndividualPersonCategories,
                                                   IndividualPersonCategories actualIndividualPersonCategories) {
        Assert.assertEquals("IndividualPersonCategories (behalfOfForeign) not equals",
                expectedIndividualPersonCategories.isBehalfOfForeign(),
                actualIndividualPersonCategories.isBehalfOfForeign());
        Assert.assertEquals("IndividualPersonCategories (beneficialOwner) not equals",
                expectedIndividualPersonCategories.isBeneficialOwner(),
                actualIndividualPersonCategories.isBeneficialOwner());
        Assert.assertEquals("IndividualPersonCategories (foreignPublicPerson) not equals",
                expectedIndividualPersonCategories.isForeignPublicPerson(),
                actualIndividualPersonCategories.isForeignPublicPerson());
        Assert.assertEquals("IndividualPersonCategories (foreignPublicPerson) not equals",
                expectedIndividualPersonCategories.isForeignPublicPerson(),
                actualIndividualPersonCategories.isForeignPublicPerson());
        Assert.assertEquals("IndividualPersonCategories (foreignRelativePerson) not equals",
                expectedIndividualPersonCategories.isForeignRelativePerson(),
                actualIndividualPersonCategories.isForeignRelativePerson());
        Assert.assertEquals("IndividualPersonCategories (hasRepresentative) not equals",
                expectedIndividualPersonCategories.isHasRepresentative(),
                actualIndividualPersonCategories.isHasRepresentative());
        Assert.assertEquals("IndividualPersonCategories (worldwideOrgPublicPerson) not equals",
                expectedIndividualPersonCategories.isWorldwideOrgPublicPerson(),
                actualIndividualPersonCategories.isWorldwideOrgPublicPerson());
    }

    private void comparePropertyInfoDocType(PropertyInfoDocumentType expectedPropertyInfoDocumentType,
                                            PropertyInfoDocumentType actualPropertyInfoDocumentType) {
        Assert.assertEquals("PropertyInfoDocType (certificateOfOwnership) not equals",
                expectedPropertyInfoDocumentType.isSetCertificateOfOwnership(),
                actualPropertyInfoDocumentType.isSetCertificateOfOwnership());
        Assert.assertEquals("PropertyInfoDocType (leaseContract) not equals",
                expectedPropertyInfoDocumentType.isSetLeaseContract(),
                actualPropertyInfoDocumentType.isSetLeaseContract());
        Assert.assertEquals("PropertyInfoDocType (otherPropertyInfoDocumentType) not equals",
                expectedPropertyInfoDocumentType.isSetOtherPropertyInfoDocumentType(),
                actualPropertyInfoDocumentType.isSetOtherPropertyInfoDocumentType());
        if (expectedPropertyInfoDocumentType.isSetOtherPropertyInfoDocumentType()) {
            Assert.assertEquals("PropertyInfoDocType (name) not equals",
                    expectedPropertyInfoDocumentType.getOtherPropertyInfoDocumentType().getName(),
                    actualPropertyInfoDocumentType.getOtherPropertyInfoDocumentType().getName());
        }
    }

    private void comparePrincipalActivity(Activity expectedActivity, Activity actualActivity) {
        Assert.assertEquals("PrincipalActivity (code) not equals",
                expectedActivity.getCode(),
                actualActivity.getCode());
        Assert.assertEquals("PrincipalActivity (description) not equals",
                expectedActivity.getDescription(),
                actualActivity.getDescription());
    }

    private void compareResidencyInfo(ResidencyInfo expectedResidencyInfo, ResidencyInfo actualResidencyInfo) {
        if (expectedResidencyInfo.isSetIndividualResidencyInfo()) {
            Assert.assertEquals("ResidencyInfo (taxResident) not equals",
                    expectedResidencyInfo.getIndividualResidencyInfo().isUsaTaxResident(),
                    actualResidencyInfo.getIndividualResidencyInfo().isUsaTaxResident());
            Assert.assertEquals("ResidencyInfo (exceptUsaTaxResident) not equals",
                    expectedResidencyInfo.getIndividualResidencyInfo().isExceptUsaTaxResident(),
                    actualResidencyInfo.getIndividualResidencyInfo().isExceptUsaTaxResident());
        } else if (expectedResidencyInfo.isSetLegalResidencyInfo()) {
            Assert.assertEquals("ResidencyInfo (fatca) not equals",
                    expectedResidencyInfo.getLegalResidencyInfo().isFatca(),
                    actualResidencyInfo.getLegalResidencyInfo().isFatca());
            Assert.assertEquals("ResidencyInfo (ownerResident) not equals",
                    expectedResidencyInfo.getLegalResidencyInfo().isOwnerResident(),
                    actualResidencyInfo.getLegalResidencyInfo().isOwnerResident());
            Assert.assertEquals("ResidencyInfo (taxResident) not equals",
                    expectedResidencyInfo.getLegalResidencyInfo().isTaxResident(),
                    actualResidencyInfo.getLegalResidencyInfo().isTaxResident());
        }
    }

}
