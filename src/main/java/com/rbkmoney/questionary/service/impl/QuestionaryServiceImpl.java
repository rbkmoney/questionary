package com.rbkmoney.questionary.service.impl;

import com.rbkmoney.dao.DaoException;
import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.AdditionalInfo;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.BeneficialOwner;
import com.rbkmoney.questionary.Head;
import com.rbkmoney.questionary.converter.*;
import com.rbkmoney.questionary.dao.*;
import com.rbkmoney.questionary.domain.enums.QuestionaryEntityType;
import com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo;
import com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition;
import com.rbkmoney.questionary.domain.tables.pojos.Founder;
import com.rbkmoney.questionary.domain.tables.pojos.*;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import com.rbkmoney.questionary.manage.*;
import com.rbkmoney.questionary.model.*;
import com.rbkmoney.questionary.service.QuestionaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class QuestionaryServiceImpl implements QuestionaryService {

    private final QuestionaryDao questionaryDao;
    private final AdditionalInfoDao additionalInfoDao;
    private final BusinessInfoDao businessInfoDao;
    private final FinancialPositionDao financialPositionDao;
    private final LegalOwnerDao legalOwnerDao;
    private final HeadDao headDao;
    private final FounderDao founderDao;
    private final PropertyInfoDao propertyInfoDao;
    private final BeneficialOwnerDao beneficialOwnerDao;

    private final QuestionaryParamsConverter questionaryParamsConverter = new QuestionaryParamsConverter();
    private final AdditionalInfoConverter additionalInfoConverter = new AdditionalInfoConverter();
    private final FinancialPositionConverter financialPositionConverter = new FinancialPositionConverter();
    private final HeadConverter headConverter = new HeadConverter();
    private final FounderConverter founderConverter = new FounderConverter();
    private final BeneficialOwnerConverter beneficialOwnerConverter = new BeneficialOwnerConverter();

    @Override
    public long saveQuestionary(QuestionaryParams questionaryParams, Long version) throws QuestionaryVersionConflict {
        log.debug("Converting thrift questionary to DB pojo");
        final QuestionaryHolder questionaryHolder = questionaryParamsConverter.convertFromThrift(questionaryParams);
        final Questionary questionary = questionaryHolder.getQuestionary();
        questionary.setVersion(++version);

        // Save questionary
        try {
            final Long questionaryId = questionaryDao.saveQuestionary(questionary);
            if (questionaryHolder.getLegalEntityQuestionaryHolder() != null) {
                // Save legal entity questionary
                final LegalEntityQuestionaryHolder legalEntityQuestionaryHolder = questionaryHolder.getLegalEntityQuestionaryHolder();
                log.debug("Save legal questionary");
                saveLegalEntityQuestionary(questionaryId, legalEntityQuestionaryHolder);
            } else if (questionaryHolder.getIndividualEntityQuestionaryHolder() != null) {
                // Save individual entity questionary
                final IndividualEntityQuestionaryHolder individualEntityQuestionaryHolder = questionaryHolder
                        .getIndividualEntityQuestionaryHolder();
                log.debug("Save individual entity questionary");
                saveIndividualEntityQuestionary(questionaryId, individualEntityQuestionaryHolder);
            }
            log.info("Questionary successfully saved");
        } catch (DaoException ex) {
            if (ex.getCause() instanceof DuplicateKeyException) {
                throw new QuestionaryVersionConflict();
            }
        }

        return questionary.getVersion();
    }

    @Override
    public Snapshot getQuestionary(String questionaryId, Reference reference) throws QuestionaryNotFound {
        Questionary questionary = null;
        if (reference.isSetHead()) {
            questionary = questionaryDao.getLatestQuestionary(questionaryId);
        } else {
            questionary = questionaryDao.getQuestionaryByIdAndVersion(questionaryId, reference.getVersion());
        }

        if (questionary == null) {
            throw new QuestionaryNotFound();
        }

        final var thriftQuestionary = new com.rbkmoney.questionary.manage.Questionary();
        thriftQuestionary.setId(questionary.getQuestionaryId());
        thriftQuestionary.setOwnerId(questionary.getOwnerId());

        final QuestionaryData questionaryData = new QuestionaryData();
        final Contractor contractor = new Contractor();
        if (questionary.getType() == QuestionaryEntityType.legal) {
            final LegalEntity legalEntity = questionaryDao.getLegalEntityById(questionary.getId());
            final RussianLegalEntity russianLegalEntity = legalEntity.getRussianLegalEntity();

            final List<com.rbkmoney.questionary.Head> headList = headDao.getByQuestionaryId(questionary.getId()).stream()
                    .map(headConverter::convertToThrift)
                    .collect(Collectors.toList());
            russianLegalEntity.getFoundersInfo().setHeads(headList);

            final List<com.rbkmoney.questionary.Founder> founderList = founderDao.getByQuestionaryId(questionary.getId()).stream()
                    .map(founderConverter::convertToThrift)
                    .collect(Collectors.toList());
            russianLegalEntity.getFoundersInfo().setFounders(founderList);

            final List<String> propertyInfoList = propertyInfoDao.getByQuestionaryId(questionary.getId()).stream()
                    .map(PropertyInfo::getDescription)
                    .collect(Collectors.toList());
            russianLegalEntity.setPropertyInfo(propertyInfoList);

            final List<BeneficialOwner> beneficialOwnerList = beneficialOwnerDao.getByQuestionaryId(questionary.getId()).stream()
                    .map(beneficialOwnerConverter::convertToThrift)
                    .collect(Collectors.toList());
            russianLegalEntity.setBeneficialOwners(beneficialOwnerList);

            final AdditionalInfo additionalInfo = getAdditionalInfoById(questionary.getId());
            russianLegalEntity.setAdditionalInfo(additionalInfo);

            final LicenseInfo licenseInfo = new LicenseInfo();
            if (questionary.getLicenseExpirationDate() != null) {
                licenseInfo.setExpirationDate(TypeUtil.temporalToString(questionary.getLicenseExpirationDate()));
            }
            if (questionary.getLicenseEffectiveDate() != null) {
                licenseInfo.setEffectiveDate(TypeUtil.temporalToString(questionary.getLicenseEffectiveDate()));
            }
            if (questionary.getLicenseIssueDate() != null) {
                licenseInfo.setIssueDate(TypeUtil.temporalToString(questionary.getLicenseIssueDate()));
            }
            licenseInfo.setIssuerName(questionary.getLicenseIssuerName());
            licenseInfo.setOfficialNum(questionary.getLicenseOfficialNum());
            licenseInfo.setLicensedActivity(questionary.getLicenseLicensedActivity());
            russianLegalEntity.setLicenseInfo(licenseInfo);

            final Activity activity = new Activity();
            activity.setCode(questionary.getOkvd());
            activity.setDescription(questionary.getActivityType());
            russianLegalEntity.setPrincipalActivity(activity);

            contractor.setLegalEntity(legalEntity);
        } else if (questionary.getType() == QuestionaryEntityType.individual) {
            final IndividualEntity individualEntity = questionaryDao.getIndividualEntityById(questionary.getId());
            final RussianIndividualEntity russianIndividualEntity = individualEntity.getRussianIndividualEntity();

            final List<String> propertyInfoList = propertyInfoDao.getByQuestionaryId(questionary.getId()).stream()
                    .map(PropertyInfo::getDescription)
                    .collect(Collectors.toList());
            russianIndividualEntity.setPropertyInfo(propertyInfoList);

            final AdditionalInfo additionalInfo = getAdditionalInfoById(questionary.getId());
            russianIndividualEntity.setAdditionalInfo(additionalInfo);

            contractor.setIndividualEntity(individualEntity);
        }
        questionaryData.setContractor(contractor);

        final ShopInfo shopInfo = new ShopInfo();
        final ShopDetails shopDetails = new ShopDetails();
        shopDetails.setName(questionary.getShopName());
        shopDetails.setDescription(questionary.getShopDescription());
        shopInfo.setDetails(shopDetails);
        final ShopLocation shopLocation = new ShopLocation();
        if (questionary.getShopUrl() != null) {
            shopLocation.setUrl(questionary.getShopUrl());
        }
        shopInfo.setLocation(shopLocation);
        questionaryData.setShopInfo(shopInfo);

        final BankAccount bankAccount = new BankAccount();
        final RussianBankAccount russianBankAccount = new RussianBankAccount();
        russianBankAccount.setBankName(questionary.getBankName());
        russianBankAccount.setBankBik(questionary.getBankBik());
        russianBankAccount.setBankPostAccount(questionary.getBankPostAccount());
        russianBankAccount.setAccount(questionary.getBankAccount());
        bankAccount.setRussianBankAccount(russianBankAccount);
        questionaryData.setBankAccount(bankAccount);

        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(questionary.getEmail());
        contactInfo.setPhoneNumber(questionary.getPhoneNumber());
        questionaryData.setContactInfo(contactInfo);

        thriftQuestionary.setData(questionaryData);

        return new Snapshot(questionary.getVersion(), thriftQuestionary);
    }

    private void saveLegalEntityQuestionary(Long questionaryId, LegalEntityQuestionaryHolder legalEntityQuestionaryHolder) {
        final LegalEntityQuestionary legalEntityQuestionary = legalEntityQuestionaryHolder.getLegalEntityQuestionary();

        // Save legalOwner
        if (legalEntityQuestionaryHolder.getLegalOwner() != null) {
            final Long legalOwnerId = legalOwnerDao.save(legalEntityQuestionaryHolder.getLegalOwner());
            legalEntityQuestionary.setLegalOwnerId(legalOwnerId);
        }

        // Save legal entity
        legalEntityQuestionary.setId(questionaryId);
        final Long legaEntityQuestionaryId = questionaryDao.saveLegalEntity(legalEntityQuestionary);

        if (legalEntityQuestionaryHolder.getHeadList() != null) {
            final List<com.rbkmoney.questionary.domain.tables.pojos.Head> headList = legalEntityQuestionaryHolder.getHeadList().stream()
                    .peek(head -> head.setQuestionaryId(questionaryId))
                    .collect(Collectors.toList());
            headDao.saveAll(headList);
        }

        if (legalEntityQuestionaryHolder.getFounderList() != null) {
            final List<Founder> founderList = legalEntityQuestionaryHolder.getFounderList().stream()
                    .peek(founder -> founder.setQuestionaryId(questionaryId))
                    .collect(Collectors.toList());
            founderDao.saveAll(founderList);
        }

        if (legalEntityQuestionaryHolder.getPropertyInfoList() != null) {
            final List<PropertyInfo> propertyInfoList = legalEntityQuestionaryHolder.getPropertyInfoList().stream()
                    .peek(propertyInfo -> propertyInfo.setQuestionaryId(questionaryId))
                    .collect(Collectors.toList());
            propertyInfoDao.saveAll(propertyInfoList);
        }

        if (legalEntityQuestionaryHolder.getBeneficialOwnerList() != null) {
            for (var beneficialOwner : legalEntityQuestionaryHolder.getBeneficialOwnerList()) {
                beneficialOwner.setQuestionaryId(questionaryId);
                beneficialOwnerDao.save(beneficialOwner);
            }
        }

        // Save additional info
        final AdditionalInfoHolder additionalInfoHolder = legalEntityQuestionaryHolder.getAdditionalInfoHolder();
        if (additionalInfoHolder != null && additionalInfoHolder.getAdditionalInfo() != null) {
            additionalInfoHolder.getAdditionalInfo().setId(questionaryId);
            saveAdditionalInfo(additionalInfoHolder);
        }
    }

    private void saveIndividualEntityQuestionary(Long questionaryId, IndividualEntityQuestionaryHolder individualEntityQuestionaryHolder) {
        final IndividualEntityQuestionary individualEntityQuestionary = individualEntityQuestionaryHolder.getIndividualEntityQuestionary();
        individualEntityQuestionary.setId(questionaryId);
        questionaryDao.saveIndividualEntity(individualEntityQuestionary);

        if (individualEntityQuestionaryHolder.getPropertyInfoList() != null) {
            final List<PropertyInfo> propertyInfoList = individualEntityQuestionaryHolder.getPropertyInfoList().stream()
                    .peek(propertyInfo -> propertyInfo.setQuestionaryId(questionaryId))
                    .collect(Collectors.toList());
            propertyInfoDao.saveAll(propertyInfoList);
        }

        final AdditionalInfoHolder additionalInfoHolder = individualEntityQuestionaryHolder.getAdditionalInfoHolder();
        if (additionalInfoHolder != null && additionalInfoHolder.getAdditionalInfo() != null) {
            additionalInfoHolder.getAdditionalInfo().setId(questionaryId);
            saveAdditionalInfo(additionalInfoHolder);
        }
    }

    private void saveAdditionalInfo(AdditionalInfoHolder additionalInfoHolder) {
        final Long additionalInfoId = additionalInfoDao.save(additionalInfoHolder.getAdditionalInfo());
        if (additionalInfoHolder.getFinancialPositionList() != null) {
            final List<FinancialPosition> financialPositionList = additionalInfoHolder.getFinancialPositionList().stream()
                    .peek(financialPosition -> financialPosition.setAdditionalInfoId(additionalInfoId))
                    .collect(Collectors.toList());
            financialPositionDao.saveAll(financialPositionList);
        }
        if (additionalInfoHolder.getBusinessInfoList() != null) {
            final List<BusinessInfo> businessInfoList = additionalInfoHolder.getBusinessInfoList().stream()
                    .peek(businessInfo -> businessInfo.setAdditionalInfoId(additionalInfoId))
                    .collect(Collectors.toList());
            businessInfoDao.saveAll(businessInfoList);
        }
    }

    private AdditionalInfo getAdditionalInfoById(Long id) {
        final var additionalInfo = additionalInfoDao.getById(id);
        final List<FinancialPosition> financialPositionList = financialPositionDao.getByAdditionalInfoId(id);
        final List<BusinessInfo> businessInfoList = businessInfoDao.getByAdditionalInfoId(id);

        final AdditionalInfoHolder additionalInfoHolder = AdditionalInfoHolder.builder()
                .additionalInfo(additionalInfo)
                .financialPositionList(financialPositionList)
                .businessInfoList(businessInfoList)
                .build();

        return additionalInfoConverter.convertToThrift(additionalInfoHolder);
    }

}

