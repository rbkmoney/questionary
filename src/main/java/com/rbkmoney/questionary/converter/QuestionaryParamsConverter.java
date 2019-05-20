package com.rbkmoney.questionary.converter;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.domain.enums.QuestionaryEntityType;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import com.rbkmoney.questionary.manage.QuestionaryData;
import com.rbkmoney.questionary.manage.QuestionaryParams;
import com.rbkmoney.questionary.model.IndividualEntityQuestionaryHolder;
import com.rbkmoney.questionary.model.LegalEntityQuestionaryHolder;
import com.rbkmoney.questionary.model.QuestionaryHolder;

public class QuestionaryParamsConverter implements ThriftConverter<QuestionaryHolder, QuestionaryParams> {

    private final IndividualEntityQuestionaryConverter individualEntityQuestionaryConverter;

    private final LegalEntityQuestionaryConverter legalEntityQuestionaryConverter;

    public QuestionaryParamsConverter() {
        this.individualEntityQuestionaryConverter = new IndividualEntityQuestionaryConverter();
        this.legalEntityQuestionaryConverter = new LegalEntityQuestionaryConverter();
    }

    @Override
    public QuestionaryParams convertToThrift(QuestionaryHolder value) {
        final QuestionaryParams questionaryParams = new QuestionaryParams();
        questionaryParams.setId(value.getQuestionary().getQuestionaryId());
        questionaryParams.setOwnerId(value.getQuestionary().getOwnerId());

        final QuestionaryData questionaryData = new QuestionaryData();
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPhoneNumber(value.getQuestionary().getPhoneNumber());
        contactInfo.setEmail(value.getQuestionary().getEmail());
        questionaryData.setContactInfo(contactInfo);

        final ShopInfo shopInfo = new ShopInfo();
        ShopDetails shopDetails = new ShopDetails();
        shopDetails.setName(value.getQuestionary().getShopName());
        shopDetails.setDescription(value.getQuestionary().getShopDescription());
        shopInfo.setDetails(shopDetails);
        ShopLocation shopLocation = new ShopLocation();
        shopLocation.setUrl(value.getQuestionary().getShopUrl());
        shopInfo.setLocation(shopLocation);
        questionaryData.setShopInfo(shopInfo);

        final BankAccount bankAccount = new BankAccount();
        RussianBankAccount russianBankAccount = new RussianBankAccount();
        russianBankAccount.setBankName(value.getQuestionary().getBankName());
        russianBankAccount.setBankPostAccount(value.getQuestionary().getBankPostAccount());
        russianBankAccount.setAccount(value.getQuestionary().getBankAccount());
        russianBankAccount.setBankBik(value.getQuestionary().getBankBik());
        bankAccount.setRussianBankAccount(russianBankAccount);
        questionaryData.setBankAccount(bankAccount);

        if (value.getIndividualEntityQuestionaryHolder() != null) {
            Contractor contractor = new Contractor();
            IndividualEntity individualEntity = new IndividualEntity();
            individualEntity.setRussianIndividualEntity(
                    individualEntityQuestionaryConverter.convertToThrift(value.getIndividualEntityQuestionaryHolder())
            );
            contractor.setIndividualEntity(individualEntity);

            questionaryData.setContractor(contractor);
        } else if (value.getLegalEntityQuestionaryHolder() != null) {
            Contractor contractor = new Contractor();
            LegalEntity legalEntity = new LegalEntity();
            legalEntity.setRussianLegalEntity(
                    legalEntityQuestionaryConverter.convertToThrift(value.getLegalEntityQuestionaryHolder())
            );
            contractor.setLegalEntity(legalEntity);

            questionaryData.setContractor(contractor);
        }

        questionaryParams.setData(questionaryData);

        return questionaryParams;
    }

    @Override
    public QuestionaryHolder convertFromThrift(QuestionaryParams value) {
        final Questionary questionary = new Questionary();
        questionary.setQuestionaryId(value.getId());
        questionary.setOwnerId(value.getOwnerId());
        if (value.getData().isSetContactInfo()) {
            questionary.setPhoneNumber(value.getData().getContactInfo().getPhoneNumber());
            questionary.setEmail(value.getData().getContactInfo().getEmail());
        }
        if (value.getData().isSetShopInfo()) {
            if (value.getData().getShopInfo().isSetDetails()) {
                questionary.setShopName(value.getData().getShopInfo().getDetails().getName());
                questionary.setShopDescription(value.getData().getShopInfo().getDetails().getDescription());
            }
            if (value.getData().getShopInfo().isSetLocation() && value.getData().getShopInfo().getLocation().isSetUrl()) {
                questionary.setShopUrl(value.getData().getShopInfo().getLocation().getUrl());
            }
        }
        if (value.getData().isSetBankAccount() && value.getData().getBankAccount().isSetRussianBankAccount()) {
            questionary.setBankName(value.getData().getBankAccount().getRussianBankAccount().getBankName());
            questionary.setBankAccount(value.getData().getBankAccount().getRussianBankAccount().getAccount());
            questionary.setBankBik(value.getData().getBankAccount().getRussianBankAccount().getBankBik());
            questionary.setBankPostAccount(value.getData().getBankAccount().getRussianBankAccount().getBankPostAccount());
        }

        final QuestionaryHolder.QuestionaryHolderBuilder questionaryHolderBuilder = QuestionaryHolder.builder();
        questionaryHolderBuilder.questionary(questionary);

        if (value.getData().isSetContractor()) {
            if (value.getData().getContractor().isSetIndividualEntity()) {
                questionary.setType(QuestionaryEntityType.individual);
                IndividualEntity individualEntity = value.getData().getContractor().getIndividualEntity();
                if (individualEntity.isSetRussianIndividualEntity()) {
                    final RussianIndividualEntity russianIndividualEntity = individualEntity.getRussianIndividualEntity();

                    questionary.setInn(russianIndividualEntity.getInn());
                    if (russianIndividualEntity.isSetLicenseInfo()) {
                        questionary.setLicenseOfficialNum(russianIndividualEntity.getLicenseInfo().getOfficialNum());
                        questionary.setLicenseIssuerName(russianIndividualEntity.getLicenseInfo().getIssuerName());
                        if (russianIndividualEntity.getLicenseInfo().isSetIssueDate()) {
                            questionary.setLicenseIssueDate(
                                    TypeUtil.stringToLocalDateTime(russianIndividualEntity.getLicenseInfo().getIssueDate())
                            );
                        }
                        if (russianIndividualEntity.getLicenseInfo().isSetEffectiveDate()) {
                            questionary.setLicenseEffectiveDate(
                                    TypeUtil.stringToLocalDateTime(russianIndividualEntity.getLicenseInfo().getEffectiveDate())
                            );
                        }
                        if (russianIndividualEntity.getLicenseInfo().isSetExpirationDate()) {
                            questionary.setLicenseExpirationDate(
                                    TypeUtil.stringToLocalDateTime(russianIndividualEntity.getLicenseInfo().getExpirationDate())
                            );
                        }
                        questionary.setLicenseLicensedActivity(russianIndividualEntity.getLicenseInfo().getLicensedActivity());
                    }

                    if (russianIndividualEntity.isSetRegistrationInfo() &&
                            russianIndividualEntity.getRegistrationInfo().isSetIndividualRegistrationInfo()) {
                        final String registrationDate = russianIndividualEntity.getRegistrationInfo().getIndividualRegistrationInfo().getRegistrationDate();
                        if (registrationDate != null) {
                            questionary.setRegDate(TypeUtil.stringToLocalDateTime(registrationDate));
                        }
                        questionary.setRegPlace(russianIndividualEntity.getRegistrationInfo().getIndividualRegistrationInfo().getRegistrationPlace());
                    }
                    if (russianIndividualEntity.isSetResidencyInfo() &&
                            russianIndividualEntity.getResidencyInfo().isSetIndividualResidencyInfo()) {
                        questionary.setTaxResident(russianIndividualEntity.getResidencyInfo().getIndividualResidencyInfo().isTaxResident());
                    }
                    if (russianIndividualEntity.isSetPrincipalActivity()) {
                        questionary.setOkvd(russianIndividualEntity.getPrincipalActivity().getCode());
                        questionary.setActivityType(russianIndividualEntity.getPrincipalActivity().getDescription());
                    }
                    final IndividualEntityQuestionaryHolder individualEntityQuestionaryHolder =
                            individualEntityQuestionaryConverter.convertFromThrift(russianIndividualEntity);
                    individualEntityQuestionaryHolder.setQuestionary(questionary);

                    questionaryHolderBuilder.individualEntityQuestionaryHolder(individualEntityQuestionaryHolder);
                }
            } else if (value.getData().getContractor().isSetLegalEntity()) {
                questionary.setType(QuestionaryEntityType.legal);
                final LegalEntity legalEntity = value.getData().getContractor().getLegalEntity();
                if (legalEntity.isSetRussianLegalEntity()) {
                    final RussianLegalEntity russianLegalEntity = legalEntity.getRussianLegalEntity();
                    questionary.setInn(russianLegalEntity.getInn());
                    if (russianLegalEntity.isSetRegistrationInfo() &&
                            russianLegalEntity.getRegistrationInfo().isSetLegalRegistrationInfo()) {
                        String registrationDate = russianLegalEntity.getRegistrationInfo().getLegalRegistrationInfo().getRegistrationDate();
                        if (registrationDate != null) {
                            questionary.setRegDate(TypeUtil.stringToLocalDateTime(registrationDate));
                        }
                        questionary.setRegPlace(russianLegalEntity.getRegistrationInfo().getLegalRegistrationInfo().getRegistrationPlace());
                    }
                    if (russianLegalEntity.isSetResidencyInfo() &&
                            russianLegalEntity.getResidencyInfo().isSetLegalResidencyInfo()) {
                        questionary.setTaxResident(russianLegalEntity.getResidencyInfo().getLegalResidencyInfo().isTaxResident());
                    }
                    if (russianLegalEntity.isSetPrincipalActivity()) {
                        questionary.setOkvd(russianLegalEntity.getPrincipalActivity().getCode());
                        questionary.setActivityType(russianLegalEntity.getPrincipalActivity().getDescription());
                    }
                    if (russianLegalEntity.isSetLicenseInfo()) {
                        questionary.setLicenseIssuerName(russianLegalEntity.getLicenseInfo().getIssuerName());
                        questionary.setLicenseOfficialNum(russianLegalEntity.getLicenseInfo().getOfficialNum());
                        questionary.setLicenseLicensedActivity(russianLegalEntity.getLicenseInfo().getLicensedActivity());
                        if (russianLegalEntity.getLicenseInfo().isSetIssueDate()) {
                            questionary.setLicenseIssueDate(
                                    TypeUtil.stringToLocalDateTime(russianLegalEntity.getLicenseInfo().getIssueDate()))
                            ;
                        }
                        if (russianLegalEntity.getLicenseInfo().isSetExpirationDate()) {
                            questionary.setLicenseExpirationDate(
                                    TypeUtil.stringToLocalDateTime(russianLegalEntity.getLicenseInfo().getExpirationDate())
                            );
                        }
                        if (russianLegalEntity.getLicenseInfo().isSetEffectiveDate()) {
                            questionary.setLicenseEffectiveDate(
                                    TypeUtil.stringToLocalDateTime(russianLegalEntity.getLicenseInfo().getEffectiveDate())
                            );
                        }
                    }
                    final LegalEntityQuestionaryHolder legalEntityQuestionaryHolder
                            = legalEntityQuestionaryConverter.convertFromThrift(russianLegalEntity);
                    legalEntityQuestionaryHolder.setQuestionary(questionary);

                    questionaryHolderBuilder.legalEntityQuestionaryHolder(legalEntityQuestionaryHolder);
                }
            }
        }

        return questionaryHolderBuilder.build();
    }
}
