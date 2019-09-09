package com.rbkmoney.questionary.service.impl;

import com.rbkmoney.dao.DaoException;
import com.rbkmoney.questionary.converter.ConverterManager;
import com.rbkmoney.questionary.converter.questionary.*;
import com.rbkmoney.questionary.dao.*;
import com.rbkmoney.questionary.domain.enums.QuestionaryEntityType;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import com.rbkmoney.questionary.domain.tables.pojos.*;
import com.rbkmoney.questionary.manage.*;
import com.rbkmoney.questionary.model.AdditionalInfoHolder;
import com.rbkmoney.questionary.model.IndividualEntityQuestionaryHolder;
import com.rbkmoney.questionary.model.LegalEntityQuestionaryHolder;
import com.rbkmoney.questionary.model.QuestionaryHolder;
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
    private final BeneficialOwnerDao beneficialOwnerDao;

    private final ConverterManager converterManager;

    @Override
    public long saveQuestionary(QuestionaryParams questionaryParams, Long version) throws QuestionaryVersionConflict {
        log.info("Converting thrift questionary to DB pojo");
        final QuestionaryHolder questionaryHolder = converterManager.convertFromThrift(questionaryParams, QuestionaryHolder.class);
        final Questionary questionary = questionaryHolder.getQuestionary();
        questionary.setVersion(++version);

        // Save questionary
        try {
            log.info("Save questionary: id={}, ownerId={}, version={}",
                    questionaryParams.getId(), questionaryParams.getOwnerId(), questionary.getVersion());
            final Long questionaryId = questionaryDao.saveQuestionary(questionary);
            log.info("QuestionaryId={}", questionaryId);
            if (questionaryHolder.getLegalEntityQuestionaryHolder() != null) {
                // Save legal entity questionary
                final LegalEntityQuestionaryHolder legalEntityQuestionaryHolder = questionaryHolder.getLegalEntityQuestionaryHolder();
                log.info("Save legal questionary: id={}", questionaryId);
                saveLegalEntityQuestionary(questionaryId, legalEntityQuestionaryHolder);
            } else if (questionaryHolder.getIndividualEntityQuestionaryHolder() != null) {
                // Save individual entity questionary
                final IndividualEntityQuestionaryHolder individualEntityQuestionaryHolder = questionaryHolder
                        .getIndividualEntityQuestionaryHolder();
                log.info("Save individual entity questionary: id={}", questionaryId);
                saveIndividualEntityQuestionary(questionaryId, individualEntityQuestionaryHolder);
            }
        } catch (DaoException ex) {
            if (ex.getCause() instanceof DuplicateKeyException) {
                throw new QuestionaryVersionConflict();
            }
            throw ex;
        }

        return questionary.getVersion();
    }

    @Override
    public Snapshot getQuestionary(String questionaryId, Reference reference) throws QuestionaryNotFound {
        Questionary questionary;
        if (reference.isSetHead()) {
            log.info("Get questionary head version: id={}", questionaryId);
            questionary = questionaryDao.getLatestQuestionary(questionaryId);
        } else {
            log.info("Get questionary by version: id={}, version={}", questionaryId, reference.getVersion());
            questionary = questionaryDao.getQuestionaryByIdAndVersion(questionaryId, reference.getVersion());
        }

        if (questionary == null) {
            throw new QuestionaryNotFound();
        }

        final var questionaryHolderBuilder = QuestionaryHolder.builder();
        questionaryHolderBuilder.questionary(questionary);
        if (questionary.getType() == QuestionaryEntityType.individual) {
            final var individualEntityQuestionaryHolderBuilder = IndividualEntityQuestionaryHolder.builder();
            final IndividualEntityQuestionary individualEntityQuestionary = questionaryDao.getIndividualEntityQuestionaryById(questionary.getId());
            individualEntityQuestionaryHolderBuilder.questionary(questionary);
            individualEntityQuestionaryHolderBuilder.individualEntityQuestionary(individualEntityQuestionary);
            individualEntityQuestionaryHolderBuilder.additionalInfoHolder(getAdditionalInfoById(questionary.getId()));
            individualEntityQuestionaryHolderBuilder.beneficialOwnerList(beneficialOwnerDao.getByQuestionaryId(questionary.getId()));
            questionaryHolderBuilder.individualEntityQuestionaryHolder(individualEntityQuestionaryHolderBuilder.build());
        } else if (questionary.getType() == QuestionaryEntityType.legal) {
            final var legalEntityQuestionaryHolderBuilder = LegalEntityQuestionaryHolder.builder();
            final LegalEntityQuestionary legalEntityQuestionary = questionaryDao.getLegalEntityQuestionaryById(questionary.getId());
            legalEntityQuestionaryHolderBuilder.questionary(questionary);
            legalEntityQuestionaryHolderBuilder.legalEntityQuestionary(legalEntityQuestionary);
            legalEntityQuestionaryHolderBuilder.legalOwner(legalOwnerDao.getById(legalEntityQuestionary.getLegalOwnerId()));
            legalEntityQuestionaryHolderBuilder.headList(headDao.getByQuestionaryId(questionary.getId()));
            legalEntityQuestionaryHolderBuilder.additionalInfoHolder(getAdditionalInfoById(questionary.getId()));
            legalEntityQuestionaryHolderBuilder.beneficialOwnerList(beneficialOwnerDao.getByQuestionaryId(questionary.getId()));
            legalEntityQuestionaryHolderBuilder.founderList(founderDao.getByQuestionaryId(questionary.getId()));
            questionaryHolderBuilder.legalEntityQuestionaryHolder(legalEntityQuestionaryHolderBuilder.build());
        }

        final QuestionaryParams questionaryParams = converterManager.convertToThrift(questionaryHolderBuilder.build(), QuestionaryParams.class);

        final com.rbkmoney.questionary.manage.Questionary thriftQuestionary = new com.rbkmoney.questionary.manage.Questionary();
        thriftQuestionary.setId(questionary.getQuestionaryId());
        thriftQuestionary.setOwnerId(questionary.getOwnerId());
        thriftQuestionary.setData(questionaryParams.getData());

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

        if (legalEntityQuestionaryHolder.getBeneficialOwnerList() != null) {
            final List<BeneficialOwner> beneficialOwnerList = legalEntityQuestionaryHolder.getBeneficialOwnerList().stream()
                    .peek(beneficialOwner -> beneficialOwner.setQuestionaryId(questionaryId))
                    .collect(Collectors.toList());
            beneficialOwnerDao.saveAll(beneficialOwnerList);
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

        final AdditionalInfoHolder additionalInfoHolder = individualEntityQuestionaryHolder.getAdditionalInfoHolder();
        if (additionalInfoHolder != null && additionalInfoHolder.getAdditionalInfo() != null) {
            additionalInfoHolder.getAdditionalInfo().setId(questionaryId);
            saveAdditionalInfo(additionalInfoHolder);
        }

        if (individualEntityQuestionaryHolder.getBeneficialOwnerList() != null) {
            final List<BeneficialOwner> beneficialOwnerList = individualEntityQuestionaryHolder.getBeneficialOwnerList().stream()
                    .peek(beneficialOwner -> beneficialOwner.setQuestionaryId(questionaryId))
                    .collect(Collectors.toList());
            beneficialOwnerDao.saveAll(beneficialOwnerList);
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

    private AdditionalInfoHolder getAdditionalInfoById(Long id) {
        final var additionalInfo = additionalInfoDao.getById(id);
        final List<FinancialPosition> financialPositionList = financialPositionDao.getByAdditionalInfoId(id);
        final List<BusinessInfo> businessInfoList = businessInfoDao.getByAdditionalInfoId(id);

        return AdditionalInfoHolder.builder()
                .additionalInfo(additionalInfo)
                .financialPositionList(financialPositionList)
                .businessInfoList(businessInfoList)
                .build();
    }

}

