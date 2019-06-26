package com.rbkmoney.questionary;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.manage.Head;
import com.rbkmoney.questionary.manage.*;
import com.rbkmoney.questionary.service.QuestionaryService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class QuestionaryTest extends AbstractIntegrationTest {

    @Autowired
    private QuestionaryService questionaryService;

    @Test
    public void ipQuestionaryTest() throws QuestionaryVersionConflict, QuestionaryNotFound {
        final QuestionaryParams questionaryParams = buildQuestionaryParams(EntityType.INDIVIDUAL);
        final long questionaryVersion = questionaryService.saveQuestionary(questionaryParams, 1L);
        final Reference reference = new Reference();
        reference.setVersion(questionaryVersion);
        final Snapshot questionarySnapshot = questionaryService.getQuestionary("54376457", reference);
        Assert.assertEquals(questionaryParams.getData(), questionarySnapshot.getQuestionary().getData());
    }

    @Test
    public void legalQuestionaryTest() throws QuestionaryVersionConflict, QuestionaryNotFound {
        final QuestionaryParams questionaryParams = buildQuestionaryParams(EntityType.LEGAL);
        final long questionaryVersion = questionaryService.saveQuestionary(questionaryParams, 1L);
        final Reference reference = new Reference();
        reference.setHead(new Head());
        final Snapshot questionarySnapshot = questionaryService.getQuestionary("54376457", reference);
        Assert.assertEquals(questionaryParams.getData(), questionarySnapshot.getQuestionary().getData());
    }

    private QuestionaryParams buildQuestionaryParams(EntityType entityType) {
        final QuestionaryParams questionaryParams = new QuestionaryParams();
        questionaryParams.setId("54376457");
        questionaryParams.setOwnerId("64");

        final QuestionaryData questionaryData = new QuestionaryData();
        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail("test@mail.com");
        contactInfo.setPhoneNumber("77003245235");
        questionaryData.setContactInfo(contactInfo);

        final BankAccount bankAccount = new BankAccount();
        final RussianBankAccount russianBankAccount = new RussianBankAccount();
        russianBankAccount.setAccount("326563547");
        russianBankAccount.setBankPostAccount("354653");
        russianBankAccount.setBankBik("645765876");
        russianBankAccount.setBankName("test");
        bankAccount.setRussianBankAccount(russianBankAccount);
        questionaryData.setBankAccount(bankAccount);

        final ShopInfo shopInfo = new ShopInfo();
        final ShopLocation shopLocation = new ShopLocation();
        shopLocation.setUrl("testUrl");
        shopInfo.setLocation(shopLocation);
        final ShopDetails shopDetails = new ShopDetails();
        shopDetails.setName("TestShopName");
        shopDetails.setDescription("TestShopDescription");
        shopInfo.setDetails(shopDetails);
        questionaryData.setShopInfo(shopInfo);

        final Contractor contractor;
        switch (entityType) {
            case INDIVIDUAL:
                contractor = buildIndividualEntity();
                break;
            case LEGAL:
                contractor = buildLegalEntity();
                break;
            default:
                throw new RuntimeException("Unknown entity type: " + entityType);
        }

        questionaryData.setContractor(contractor);

        questionaryParams.setData(questionaryData);

        return questionaryParams;
    }

    private Contractor buildIndividualEntity() {
        final RussianIndividualEntity russianIndividualEntity = new RussianIndividualEntity();
        final LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setLicensedActivity("testLicenseActivity");
        licenseInfo.setOfficialNum("5234654");
        licenseInfo.setIssuerName("testIssuerName");
        licenseInfo.setIssueDate(TypeUtil.temporalToString(LocalDateTime.now()));
        licenseInfo.setEffectiveDate(TypeUtil.temporalToString(LocalDateTime.now().plus(1, ChronoUnit.HOURS)));
        licenseInfo.setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now().plus(2, ChronoUnit.HOURS)));
        russianIndividualEntity.setLicenseInfo(licenseInfo);

        final ResidencyInfo residencyInfo = new ResidencyInfo();
        final IndividualResidencyInfo individualResidencyInfo = new IndividualResidencyInfo();
        individualResidencyInfo.setTaxResident(false);
        residencyInfo.setIndividualResidencyInfo(individualResidencyInfo);
        russianIndividualEntity.setResidencyInfo(residencyInfo);

        final IndividualPersonCategories individualPersonCategories = new IndividualPersonCategories();
        individualPersonCategories.setHasRepresentative(false);
        individualPersonCategories.setForeignRelativePerson(false);
        individualPersonCategories.setBehalfOfForeign(false);
        individualPersonCategories.setForeignPublicPerson(false);
        individualPersonCategories.setWorldwideOrgPublicPerson(false);
        individualPersonCategories.setBeneficialOwner(false);
        russianIndividualEntity.setIndividualPersonCategories(individualPersonCategories);

        final RussianPrivateEntity privateEntity = new RussianPrivateEntity();
        privateEntity.setCitizenship("testCitizenShip");
        privateEntity.setResidenceAddress("testResidencesAddress");
        privateEntity.setBirthPlace("testBirthPlace");
        privateEntity.setBirthDate(TypeUtil.temporalToString(LocalDateTime.now()));
        privateEntity.setActualAddress("testActualAddress");
        final PersonAnthroponym personAnthroponym = new PersonAnthroponym();
        personAnthroponym.setFirstName("testFirstName");
        personAnthroponym.setSecondName("testSecondName");
        personAnthroponym.setMiddleName("middleName");
        privateEntity.setFio(personAnthroponym);
        russianIndividualEntity.setRussianPrivateEntity(privateEntity);

        return Contractor.individual_entity(IndividualEntity.russian_individual_entity(russianIndividualEntity));
    }

    private Contractor buildLegalEntity() {
        final RussianLegalEntity russianLegalEntity = new RussianLegalEntity();
        final ResidencyInfo residencyInfo = new ResidencyInfo();
        final LegalResidencyInfo legalResidencyInfo = new LegalResidencyInfo();
        legalResidencyInfo.setTaxResident(true);
        legalResidencyInfo.setOwnerResident(false);
        legalResidencyInfo.setFatca(false);
        residencyInfo.setLegalResidencyInfo(legalResidencyInfo);
        russianLegalEntity.setResidencyInfo(residencyInfo);

        final LegalOwnerInfo legalOwnerInfo = new LegalOwnerInfo();
        final IdentityDocument identityDocument = new IdentityDocument();
        final RussianDomesticPassport russianDomesticPassport = new RussianDomesticPassport();
        russianDomesticPassport.setSeries("4315");
        russianDomesticPassport.setNumber("643543643");
        russianDomesticPassport.setIssuedAt(TypeUtil.temporalToString(LocalDateTime.now()));
        russianDomesticPassport.setIssuerCode("876856");
        identityDocument.setRussianDomesticPassword(russianDomesticPassport);
        legalOwnerInfo.setIdentityDocument(identityDocument);

        legalOwnerInfo.setPdlCategory(false);

        final ResidenceApprove residenceApprove = new ResidenceApprove();
        residenceApprove.setSeries("12345");
        residenceApprove.setBeginningDate(TypeUtil.temporalToString(LocalDateTime.now()));
        residenceApprove.setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now().plus(1, ChronoUnit.HOURS)));
        legalOwnerInfo.setResidenceApprove(residenceApprove);

        final MigrationCardInfo migrationCardInfo = new MigrationCardInfo();
        migrationCardInfo.setCardNumber("537667368675");
        migrationCardInfo.setBeginningDate(TypeUtil.temporalToString(LocalDateTime.now()));
        migrationCardInfo.setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now().plus(1, ChronoUnit.HOURS)));
        legalOwnerInfo.setMigrationCardInfo(migrationCardInfo);

        legalOwnerInfo.setInn("53768567");

        final RussianPrivateEntity privateEntity = new RussianPrivateEntity();
        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail("test@mail.com");
        contactInfo.setPhoneNumber("77002349485");
        privateEntity.setContactInfo(contactInfo);
        privateEntity.setActualAddress("testActualAddress");
        privateEntity.setBirthDate(TypeUtil.temporalToString(LocalDateTime.now()));
        privateEntity.setCitizenship("testCitizenship");
        legalOwnerInfo.setRussianPrivateEntity(privateEntity);

        russianLegalEntity.setLegalOwnerInfo(legalOwnerInfo);

        russianLegalEntity.setName("testName");
        russianLegalEntity.setForeignName("testForeignName");
        final Activity activity = new Activity();
        activity.setCode("645765");
        activity.setDescription("testDescription");
        russianLegalEntity.setPrincipalActivity(activity);

        final FoundersInfo foundersInfo = new FoundersInfo();
        final Founder founder = new Founder();
        final RussianLegalEntityFounder russianLegalEntityFounder = new RussianLegalEntityFounder();
        russianLegalEntityFounder.setFullName("testFullName");
        russianLegalEntityFounder.setInn("65765838");
        founder.setRussianLegalEntityFounder(russianLegalEntityFounder);
        foundersInfo.setFounders(Collections.singletonList(founder));
        russianLegalEntity.setFoundersInfo(foundersInfo);

        final BeneficialOwner beneficialOwner = new BeneficialOwner();
        beneficialOwner.setInn("536457647");
        beneficialOwner.setOwnershipPercentage((byte) 5);
        final RussianPrivateEntity beneficialPrivateEntity = new RussianPrivateEntity();
        final PersonAnthroponym personAnthroponym = new PersonAnthroponym();
        personAnthroponym.setFirstName("TestFirstName");
        personAnthroponym.setSecondName("TestSecondName");
        personAnthroponym.setMiddleName("TestMiddleName");
        beneficialPrivateEntity.setFio(personAnthroponym);
        beneficialOwner.setRussianPrivateEntity(beneficialPrivateEntity);
        beneficialOwner.setPdlCategory(false);
        russianLegalEntity.setBeneficialOwners(Collections.singletonList(beneficialOwner));

        final AdditionalInfo additionalInfo = new AdditionalInfo();
        final FinancialPosition financialPosition = new FinancialPosition();
        financialPosition.setQuarterlyTaxReturnWithoutMark(new QuarterlyTaxReturnWithoutMark());
        additionalInfo.setFinancialPosition(Collections.singletonList(financialPosition));
        additionalInfo.setNKORelationTarget("testNkoRelationTarget");
        additionalInfo.setRelationIndividualEntity(RelationIndividualEntity.bankrupt_judicial_decision);
        additionalInfo.setMainCounterparties("testMainCounterParties");
        additionalInfo.setStaffCount(10);
        additionalInfo.setHasAccountant(false);
        additionalInfo.setBenefitThirdParties(false);
        additionalInfo.setStorageFacilities(false);
        additionalInfo.setMonthOperationSum(MonthOperationSum.lt_five_hundred_thousand);

        russianLegalEntity.setAdditionalInfo(additionalInfo);

        return Contractor.legal_entity(LegalEntity.russian_legal_entity(russianLegalEntity));
    }

    private static enum EntityType {
        INDIVIDUAL,
        LEGAL
    }

}
