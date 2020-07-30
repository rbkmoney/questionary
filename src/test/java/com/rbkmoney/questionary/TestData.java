package com.rbkmoney.questionary;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.geck.serializer.kit.mock.MockMode;
import com.rbkmoney.geck.serializer.kit.mock.MockTBaseProcessor;
import com.rbkmoney.geck.serializer.kit.tbase.TBaseHandler;
import com.rbkmoney.questionary.manage.QuestionaryData;
import com.rbkmoney.questionary.manage.QuestionaryParams;
import io.github.benas.randombeans.api.EnhancedRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static final String QUESTIONARY_ID = "54376457";

    public static final String PARTY_ID = "12345";

    public static QuestionaryParams buildQuestionaryParams(EntityType entityType) throws IOException {
        final QuestionaryParams questionaryParams = new QuestionaryParams();
        questionaryParams.setId(QUESTIONARY_ID);
        questionaryParams.setOwnerId("64");
        questionaryParams.setPartyId(PARTY_ID);

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

    private static Contractor buildIndividualEntity() throws IOException {
        final RussianIndividualEntity russianIndividualEntity = new RussianIndividualEntity();
        final LicenseInfo licenseInfo = EnhancedRandom.random(LicenseInfo.class);
        licenseInfo.setIssueDate(TypeUtil.temporalToString(LocalDateTime.now()));
        licenseInfo.setEffectiveDate(TypeUtil.temporalToString(LocalDateTime.now().plus(1, ChronoUnit.HOURS)));
        licenseInfo.setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now().plus(2, ChronoUnit.HOURS)));
        russianIndividualEntity.setLicenseInfo(licenseInfo);
        russianIndividualEntity.setPdlRelationDegree("test_pdl_relation_degree");
        russianIndividualEntity.setPdlCategory(true);
        russianIndividualEntity.setHasBeneficialOwners(true);
        russianIndividualEntity.setName("test name");
        russianIndividualEntity.setSnils("test snils");

        final ResidencyInfo residencyInfo = new ResidencyInfo();
        final IndividualResidencyInfo individualResidencyInfo = new IndividualResidencyInfo();
        individualResidencyInfo.setUsaTaxResident(true);
        individualResidencyInfo.setExceptUsaTaxResident(false);
        residencyInfo.setIndividualResidencyInfo(individualResidencyInfo);
        russianIndividualEntity.setResidencyInfo(residencyInfo);

        final IndividualPersonCategories individualPersonCategories = EnhancedRandom.random(IndividualPersonCategories.class);
        russianIndividualEntity.setIndividualPersonCategories(individualPersonCategories);

        final RussianPrivateEntity privateEntity = EnhancedRandom.random(RussianPrivateEntity.class);
        privateEntity.setBirthDate(TypeUtil.temporalToString(LocalDateTime.now()));
        privateEntity.setFio("Just another FIO");
        russianIndividualEntity.setRussianPrivateEntity(privateEntity);

        russianIndividualEntity.setPropertyInfoDocumentType(PropertyInfoDocumentType.lease_contract(new LeaseContract()));

        final ResidenceApprove residenceApprove = EnhancedRandom.random(ResidenceApprove.class);
        residenceApprove.setBeginningDate(TypeUtil.temporalToString(LocalDateTime.now()));
        residenceApprove.setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now().plus(1, ChronoUnit.HOURS)));
        russianIndividualEntity.setResidenceApprove(residenceApprove);

        final MigrationCardInfo migrationCardInfo = EnhancedRandom.random(MigrationCardInfo.class);
        migrationCardInfo.setBeginningDate(TypeUtil.temporalToString(LocalDateTime.now()));
        migrationCardInfo.setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now().plus(1, ChronoUnit.HOURS)));
        russianIndividualEntity.setMigrationCardInfo(migrationCardInfo);

        final RegistrationInfo registrationInfo = new RegistrationInfo();
        IndividualRegistrationInfo individualRegistrationInfo = EnhancedRandom.random(IndividualRegistrationInfo.class);
        individualRegistrationInfo.setRegistrationDate(TypeUtil.temporalToString(LocalDateTime.now()));
        individualRegistrationInfo.setRegistrationPlace("test reg place individual");
        registrationInfo.setIndividualRegistrationInfo(individualRegistrationInfo);
        russianIndividualEntity.setRegistrationInfo(registrationInfo);

        final IdentityDocument identityDocument = new IdentityDocument();
        final RussianDomesticPassport russianDomesticPassport = EnhancedRandom.random(RussianDomesticPassport.class);
        russianDomesticPassport.setIssuedAt(TypeUtil.temporalToString(LocalDateTime.now()));
        identityDocument.setRussianDomesticPassword(russianDomesticPassport);
        russianIndividualEntity.setIdentityDocument(identityDocument);

        Activity activity = new Activity();
        activity = new MockTBaseProcessor(MockMode.ALL).process(activity, new TBaseHandler<>(Activity.class));
        russianIndividualEntity.setPrincipalActivity(activity);

        BeneficialOwner beneficialOwner = new BeneficialOwner();
        beneficialOwner = new MockTBaseProcessor(MockMode.ALL).process(beneficialOwner, new TBaseHandler<>(BeneficialOwner.class));
        beneficialOwner.setOwnershipPercentage((byte) 5);
        beneficialOwner.getResidenceApprove().setBeginningDate(TypeUtil.temporalToString(LocalDateTime.now()));
        beneficialOwner.getResidenceApprove().setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now()));
        beneficialOwner.getMigrationCardInfo().setBeginningDate(TypeUtil.temporalToString(LocalDateTime.now()));
        beneficialOwner.getMigrationCardInfo().setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now()));
        beneficialOwner.getIdentityDocument().getRussianDomesticPassword().setIssuedAt(TypeUtil.temporalToString(LocalDateTime.now()));
        beneficialOwner.getRussianPrivateEntity().setBirthDate(TypeUtil.temporalToString(LocalDateTime.now()));
        RussianPrivateEntity beneficialPrivateEntity = new RussianPrivateEntity();
        beneficialPrivateEntity.setBirthDate(TypeUtil.temporalToString(LocalDateTime.now()));
        beneficialPrivateEntity = new MockTBaseProcessor(MockMode.ALL).process(beneficialPrivateEntity, new TBaseHandler<>(RussianPrivateEntity.class));
        beneficialOwner.setRussianPrivateEntity(beneficialPrivateEntity);
        beneficialOwner.setPdlCategory(false);
        russianIndividualEntity.setBeneficialOwners(Collections.singletonList(beneficialOwner));

        final AdditionalInfo additionalInfo = new AdditionalInfo();
        final FinancialPosition financialPosition = new FinancialPosition();
        financialPosition.setQuarterlyTaxReturnWithoutMark(new QuarterlyTaxReturnWithoutMark());
        additionalInfo.setFinancialPosition(Collections.singletonList(financialPosition));
        additionalInfo.setNKORelationTarget("testNkoRelationTarget");
        additionalInfo.setRelationIndividualEntity(RelationIndividualEntity.liquidation_process);
        additionalInfo.setMainCounterparties("testMainCounterParties");
        additionalInfo.setStaffCount(17);
        additionalInfo.setBenefitThirdParties(true);
        additionalInfo.setMonthOperationSum(MonthOperationSum.gt_one_million);
        additionalInfo.setAccountantInfo(AccountantInfo.with_chief_accountant(new WithChiefAccountant()));
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo = new MockTBaseProcessor(MockMode.ALL).process(businessInfo, new TBaseHandler<>(BusinessInfo.class));
        additionalInfo.setBusinessInfo(Collections.singletonList(businessInfo));
        additionalInfo.setBusinessReputation(BusinessReputation.provide_reviews);
        additionalInfo.setMonthOperationCount(MonthOperationCount.lt_ten);
        BankAccount bankAccount = new BankAccount();
        RussianBankAccount russianBankAccount = new RussianBankAccount();
        russianBankAccount = new MockTBaseProcessor(MockMode.ALL)
                .process(russianBankAccount, new TBaseHandler<>(RussianBankAccount.class));
        bankAccount.setRussianBankAccount(russianBankAccount);
        additionalInfo.setBankAccount(bankAccount);
        russianIndividualEntity.setAdditionalInfo(additionalInfo);

        return Contractor.individual_entity(IndividualEntity.russian_individual_entity(russianIndividualEntity));
    }

    private static Contractor buildLegalEntity() throws IOException {
        final RussianLegalEntity russianLegalEntity = new RussianLegalEntity();
        final ResidencyInfo residencyInfo = new ResidencyInfo();
        final LegalResidencyInfo legalResidencyInfo = new LegalResidencyInfo();
        legalResidencyInfo.setTaxResident(true);
        legalResidencyInfo.setOwnerResident(false);
        legalResidencyInfo.setFatca(false);
        residencyInfo.setLegalResidencyInfo(legalResidencyInfo);
        russianLegalEntity.setResidencyInfo(residencyInfo);
        russianLegalEntity.setPropertyInfoDocumentType(PropertyInfoDocumentType.certificate_of_ownership(new CertificateOfOwnership()));
        russianLegalEntity.setHasBeneficialOwners(true);

        LegalOwnerInfo legalOwnerInfo = new LegalOwnerInfo();
        legalOwnerInfo = new MockTBaseProcessor(MockMode.ALL).process(legalOwnerInfo, new TBaseHandler<>(LegalOwnerInfo.class));

        final IdentityDocument identityDocument = new IdentityDocument();
        final RussianDomesticPassport russianDomesticPassport = EnhancedRandom.random(RussianDomesticPassport.class);
        russianDomesticPassport.setIssuedAt(TypeUtil.temporalToString(LocalDateTime.now()));
        identityDocument.setRussianDomesticPassword(russianDomesticPassport);
        legalOwnerInfo.setIdentityDocument(identityDocument);

        final ResidenceApprove residenceApprove = EnhancedRandom.random(ResidenceApprove.class);
        residenceApprove.setBeginningDate(TypeUtil.temporalToString(LocalDateTime.now()));
        residenceApprove.setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now().plus(1, ChronoUnit.HOURS)));
        legalOwnerInfo.setResidenceApprove(residenceApprove);

        final MigrationCardInfo migrationCardInfo = EnhancedRandom.random(MigrationCardInfo.class);
        migrationCardInfo.setBeginningDate(TypeUtil.temporalToString(LocalDateTime.now()));
        migrationCardInfo.setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now().plus(1, ChronoUnit.HOURS)));
        legalOwnerInfo.setMigrationCardInfo(migrationCardInfo);

        RussianPrivateEntity privateEntity = new RussianPrivateEntity();
        privateEntity = new MockTBaseProcessor(MockMode.ALL).process(privateEntity, new TBaseHandler<>(RussianPrivateEntity.class));
        privateEntity.setBirthDate(TypeUtil.temporalToString(LocalDateTime.now()));
        legalOwnerInfo.setRussianPrivateEntity(privateEntity);

        legalOwnerInfo.getAuthorityConfirmingDocument().setDate(TypeUtil.temporalToString(LocalDateTime.now()));

        russianLegalEntity.setLegalOwnerInfo(legalOwnerInfo);

        russianLegalEntity.setName("testName");
        russianLegalEntity.setForeignName("testForeignName");
        Activity activity = new Activity();
        activity = new MockTBaseProcessor(MockMode.ALL).process(activity, new TBaseHandler<>(Activity.class));
        russianLegalEntity.setPrincipalActivity(activity);

        final FoundersInfo foundersInfo = new FoundersInfo();
        final Founder founder = new Founder();
        RussianLegalEntityFounder russianLegalEntityFounder = new RussianLegalEntityFounder();
        russianLegalEntityFounder = new MockTBaseProcessor(MockMode.ALL).process(russianLegalEntityFounder, new TBaseHandler<>(RussianLegalEntityFounder.class));
        founder.setRussianLegalEntityFounder(russianLegalEntityFounder);
        foundersInfo.setFounders(Collections.singletonList(founder));
        Head head = new Head();
        head = new MockTBaseProcessor(MockMode.ALL).process(head, new TBaseHandler<>(Head.class));
        foundersInfo.setLegalOwner(head);
        foundersInfo.setHeads(Collections.singletonList(head));
        russianLegalEntity.setFoundersInfo(foundersInfo);

        BeneficialOwner beneficialOwner = new BeneficialOwner();
        beneficialOwner = new MockTBaseProcessor(MockMode.ALL).process(beneficialOwner, new TBaseHandler<>(BeneficialOwner.class));
        beneficialOwner.setOwnershipPercentage((byte) 5);
        beneficialOwner.getResidenceApprove().setBeginningDate(TypeUtil.temporalToString(LocalDateTime.now()));
        beneficialOwner.getResidenceApprove().setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now()));
        beneficialOwner.getMigrationCardInfo().setBeginningDate(TypeUtil.temporalToString(LocalDateTime.now()));
        beneficialOwner.getMigrationCardInfo().setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now()));
        beneficialOwner.getIdentityDocument().getRussianDomesticPassword().setIssuedAt(TypeUtil.temporalToString(LocalDateTime.now()));
        final RussianPrivateEntity beneficialPrivateEntity = new RussianPrivateEntity();
        beneficialPrivateEntity.setFio("Test FIO");
        beneficialPrivateEntity.setContactInfo(EnhancedRandom.random(ContactInfo.class));
        beneficialOwner.setRussianPrivateEntity(beneficialPrivateEntity);
        beneficialOwner.setPdlCategory(false);
        russianLegalEntity.setBeneficialOwners(Collections.singletonList(beneficialOwner));

        final RegistrationInfo registrationInfo = new RegistrationInfo();
        LegalRegistrationInfo legalRegistrationInfo = EnhancedRandom.random(LegalRegistrationInfo.class);
        legalRegistrationInfo.setRegistrationDate(TypeUtil.temporalToString(LocalDateTime.now()));
        legalRegistrationInfo.setRegistrationPlace("test reg place legal");
        registrationInfo.setLegalRegistrationInfo(legalRegistrationInfo);
        russianLegalEntity.setRegistrationInfo(registrationInfo);

        LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo = new MockTBaseProcessor(MockMode.ALL).process(licenseInfo, new TBaseHandler<>(LicenseInfo.class));
        licenseInfo.setEffectiveDate(TypeUtil.temporalToString(LocalDateTime.now()));
        licenseInfo.setExpirationDate(TypeUtil.temporalToString(LocalDateTime.now()));
        licenseInfo.setIssueDate(TypeUtil.temporalToString(LocalDateTime.now()));
        russianLegalEntity.setLicenseInfo(licenseInfo);

        final AdditionalInfo additionalInfo = new AdditionalInfo();
        final FinancialPosition financialPosition = new FinancialPosition();
        financialPosition.setQuarterlyTaxReturnWithoutMark(new QuarterlyTaxReturnWithoutMark());
        additionalInfo.setFinancialPosition(Collections.singletonList(financialPosition));
        additionalInfo.setNKORelationTarget("testNkoRelationTarget");
        additionalInfo.setRelationIndividualEntity(RelationIndividualEntity.bankrupt_judicial_decision);
        additionalInfo.setMainCounterparties("testMainCounterParties");
        additionalInfo.setStaffCount(10);
        additionalInfo.setBenefitThirdParties(false);
        additionalInfo.setMonthOperationSum(MonthOperationSum.lt_five_hundred_thousand);
        WithoutChiefAccountant withoutChiefAccountant = new WithoutChiefAccountant();
        withoutChiefAccountant.setAccountingOrganization(EnhancedRandom.random(AccountingOrganization.class));
        additionalInfo.setAccountantInfo(AccountantInfo.without_chief_accountant(withoutChiefAccountant));
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo = new MockTBaseProcessor(MockMode.ALL).process(businessInfo, new TBaseHandler<>(BusinessInfo.class));
        additionalInfo.setBusinessInfo(Collections.singletonList(businessInfo));
        additionalInfo.setBusinessReputation(BusinessReputation.provide_reviews);
        additionalInfo.setMonthOperationCount(MonthOperationCount.lt_ten);
        BankAccount bankAccount = new BankAccount();
        RussianBankAccount russianBankAccount = new RussianBankAccount();
        russianBankAccount = new MockTBaseProcessor(MockMode.ALL)
                .process(russianBankAccount, new TBaseHandler<>(RussianBankAccount.class));
        bankAccount.setRussianBankAccount(russianBankAccount);
        additionalInfo.setBankAccount(bankAccount);

        russianLegalEntity.setAdditionalInfo(additionalInfo);

        return Contractor.legal_entity(LegalEntity.russian_legal_entity(russianLegalEntity));
    }

    public static enum EntityType {
        INDIVIDUAL,
        LEGAL
    }

}
