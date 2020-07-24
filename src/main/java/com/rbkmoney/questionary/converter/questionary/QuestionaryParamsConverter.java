package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.enums.QuestionaryEntityType;
import com.rbkmoney.questionary.domain.tables.pojos.InternationalBankInfo;
import com.rbkmoney.questionary.domain.tables.pojos.InternationalLegalEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import com.rbkmoney.questionary.manage.QuestionaryData;
import com.rbkmoney.questionary.manage.QuestionaryParams;
import com.rbkmoney.questionary.model.IndividualEntityQuestionaryHolder;
import com.rbkmoney.questionary.model.InternationalLegalEntityQuestionaryHolder;
import com.rbkmoney.questionary.model.LegalEntityQuestionaryHolder;
import com.rbkmoney.questionary.model.QuestionaryHolder;
import com.rbkmoney.questionary.util.ThriftUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class QuestionaryParamsConverter implements ThriftConverter<QuestionaryParams, QuestionaryHolder>,
        JooqConverter<QuestionaryHolder, QuestionaryParams> {

    @Override
    public QuestionaryParams toThrift(QuestionaryHolder value, ThriftConverterContext ctx) {
        final QuestionaryParams questionaryParams = new QuestionaryParams();
        Questionary questionary = value.getQuestionary();
        questionaryParams.setId(questionary.getQuestionaryId());
        questionaryParams.setOwnerId(questionary.getOwnerId());
        questionaryParams.setPartyId(questionary.getPartyId());

        final QuestionaryData questionaryData = new QuestionaryData();
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPhoneNumber(questionary.getPhoneNumber());
        contactInfo.setEmail(questionary.getEmail());
        questionaryData.setContactInfo(contactInfo);

        final ShopInfo shopInfo = new ShopInfo();
        final ShopDetails shopDetails = new ShopDetails();
        shopDetails.setName(questionary.getShopName());
        shopDetails.setDescription(questionary.getShopDescription());
        ThriftUtil.setIfNotEmpty(shopDetails, shopInfo::setDetails);
        ShopLocation shopLocation = new ShopLocation();
        if (!StringUtils.isEmpty(questionary.getShopUrl())) {
            shopLocation.setUrl(questionary.getShopUrl());
        }
        ThriftUtil.setIfNotEmpty(shopLocation, shopInfo::setLocation);
        ThriftUtil.setIfNotEmpty(shopInfo, questionaryData::setShopInfo);

        final BankAccount bankAccount = new BankAccount();

        if (questionary.getType() == QuestionaryEntityType.international) {
            InternationalBankInfo internationalBankInfo =
                    value.getInternationalLegalEntityQuestionaryHolder().getInternationalBankInfo();
            if (internationalBankInfo != null) {
                InternationalBankAccount internationalBankAccount = ctx.convert(
                        internationalBankInfo,
                        InternationalBankAccount.class
                );
                ThriftUtil.setIfNotEmpty(internationalBankAccount, bankAccount::setInternationalBankAccount);
            }
        } else {
            RussianBankAccount russianBankAccount = new RussianBankAccount();
            russianBankAccount.setBankName(questionary.getBankName());
            russianBankAccount.setBankPostAccount(questionary.getBankPostAccount());
            russianBankAccount.setAccount(questionary.getBankAccount());
            russianBankAccount.setBankBik(questionary.getBankBik());
            ThriftUtil.setIfNotEmpty(russianBankAccount, bankAccount::setRussianBankAccount);
        }

        ThriftUtil.setIfNotEmpty(bankAccount, questionaryData::setBankAccount);

        if (value.getIndividualEntityQuestionaryHolder() != null) {
            Contractor contractor = new Contractor();
            IndividualEntity individualEntity = new IndividualEntity();
            individualEntity.setRussianIndividualEntity(
                    ctx.convert(value.getIndividualEntityQuestionaryHolder(), RussianIndividualEntity.class)
            );
            contractor.setIndividualEntity(individualEntity);

            questionaryData.setContractor(contractor);
        } else if (value.getLegalEntityQuestionaryHolder() != null) {
            Contractor contractor = new Contractor();
            LegalEntity legalEntity = new LegalEntity();
            legalEntity.setRussianLegalEntity(
                    ctx.convert(value.getLegalEntityQuestionaryHolder(), RussianLegalEntity.class)
            );
            contractor.setLegalEntity(legalEntity);

            questionaryData.setContractor(contractor);
        } else if (value.getInternationalLegalEntityQuestionaryHolder() != null) {
            InternationalLegalEntityQuestionaryHolder internationalLegalEntityHolder =
                    value.getInternationalLegalEntityQuestionaryHolder();
            Contractor contractor = new Contractor();
            LegalEntity legalEntity = new LegalEntity();
            legalEntity.setInternationalLegalEntity(
                    ctx.convert(
                            internationalLegalEntityHolder.getInternationalLegalEntityQuestionary(),
                            InternationalLegalEntity.class
                    )
            );
            contractor.setLegalEntity(legalEntity);
            questionaryData.setContractor(contractor);
        }

        questionaryParams.setData(questionaryData);

        return questionaryParams;
    }

    @Override
    public QuestionaryHolder toJooq(QuestionaryParams value, JooqConverterContext ctx) {
        final Questionary questionary = new Questionary();
        questionary.setQuestionaryId(value.getId());
        questionary.setOwnerId(value.getOwnerId());
        questionary.setPartyId(value.getPartyId());
        QuestionaryData data = value.getData();
        if (data.isSetContactInfo()) {
            questionary.setPhoneNumber(data.getContactInfo().getPhoneNumber());
            questionary.setEmail(data.getContactInfo().getEmail());
        }
        if (data.isSetShopInfo()) {
            if (data.getShopInfo().isSetDetails()) {
                questionary.setShopName(data.getShopInfo().getDetails().getName());
                questionary.setShopDescription(data.getShopInfo().getDetails().getDescription());
            }
            if (data.getShopInfo().isSetLocation() && data.getShopInfo().getLocation().isSetUrl()) {
                questionary.setShopUrl(data.getShopInfo().getLocation().getUrl());
            }
        }
        if (data.isSetBankAccount() && data.getBankAccount().isSetRussianBankAccount()) {
            RussianBankAccount russianBankAccount = data.getBankAccount().getRussianBankAccount();
            questionary.setBankName(russianBankAccount.getBankName());
            questionary.setBankAccount(russianBankAccount.getAccount());
            questionary.setBankBik(russianBankAccount.getBankBik());
            questionary.setBankPostAccount(russianBankAccount.getBankPostAccount());
        }

        final QuestionaryHolder.QuestionaryHolderBuilder questionaryHolderBuilder = QuestionaryHolder.builder();
        questionaryHolderBuilder.questionary(questionary);

        if (data.isSetContractor()) {
            Contractor contractor = data.getContractor();
            if (contractor.isSetIndividualEntity()) {
                questionary.setType(QuestionaryEntityType.individual);
                IndividualEntity individualEntity = contractor.getIndividualEntity();
                if (individualEntity.isSetRussianIndividualEntity()) {
                    final RussianIndividualEntity russianIndividualEntity = individualEntity.getRussianIndividualEntity();
                    questionary.setInn(russianIndividualEntity.getInn());
                    questionary.setHasBeneficialOwners(russianIndividualEntity.isHasBeneficialOwners());
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

                    if (russianIndividualEntity.isSetPrincipalActivity()) {
                        questionary.setOkvd(russianIndividualEntity.getPrincipalActivity().getCode());
                        questionary.setActivityType(russianIndividualEntity.getPrincipalActivity().getDescription());
                    }

                    if (russianIndividualEntity.isSetPropertyInfoDocumentType()) {
                        ctx.fill(questionary, russianIndividualEntity.getPropertyInfoDocumentType());
                    }

                    final IndividualEntityQuestionaryHolder individualEntityQuestionaryHolder =
                            ctx.convert(russianIndividualEntity, IndividualEntityQuestionaryHolder.class);
                    individualEntityQuestionaryHolder.setQuestionary(questionary);

                    questionaryHolderBuilder.individualEntityQuestionaryHolder(individualEntityQuestionaryHolder);
                }
            } else if (contractor.isSetLegalEntity()) {
                final LegalEntity legalEntity = contractor.getLegalEntity();
                if (legalEntity.isSetRussianLegalEntity()) {
                    questionary.setType(QuestionaryEntityType.legal);
                    final RussianLegalEntity russianLegalEntity = legalEntity.getRussianLegalEntity();
                    questionary.setInn(russianLegalEntity.getInn());
                    questionary.setHasBeneficialOwners(russianLegalEntity.isHasBeneficialOwners());
                    if (russianLegalEntity.isSetRegistrationInfo() &&
                            russianLegalEntity.getRegistrationInfo().isSetLegalRegistrationInfo()) {
                        String registrationDate = russianLegalEntity.getRegistrationInfo().getLegalRegistrationInfo().getRegistrationDate();
                        if (registrationDate != null) {
                            questionary.setRegDate(TypeUtil.stringToLocalDateTime(registrationDate));
                        }
                        questionary.setRegPlace(russianLegalEntity.getRegistrationInfo().getLegalRegistrationInfo().getRegistrationPlace());
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

                    if (russianLegalEntity.isSetPropertyInfoDocumentType()) {
                        ctx.fill(questionary, russianLegalEntity.getPropertyInfoDocumentType());
                    }

                    final LegalEntityQuestionaryHolder legalEntityQuestionaryHolder =
                            ctx.convert(russianLegalEntity, LegalEntityQuestionaryHolder.class);
                    legalEntityQuestionaryHolder.setQuestionary(questionary);

                    questionaryHolderBuilder.legalEntityQuestionaryHolder(legalEntityQuestionaryHolder);
                } else if (legalEntity.isSetInternationalLegalEntity()) {
                    questionary.setType(QuestionaryEntityType.international);
                    var internationalLegalEntityQuestionary = ctx.convert(
                            legalEntity.getInternationalLegalEntity(),
                            InternationalLegalEntityQuestionary.class
                    );
                    InternationalBankInfo internationalBankInfo = null;
                    if (data.isSetBankAccount() && data.getBankAccount().isSetInternationalBankAccount()) {
                        internationalBankInfo = ctx.convert(
                                data.getBankAccount().getInternationalBankAccount(),
                                InternationalBankInfo.class
                        );
                    }
                    var internationalLegalEntityHolder = InternationalLegalEntityQuestionaryHolder.builder()
                                    .questionary(questionary)
                                    .internationalLegalEntityQuestionary(internationalLegalEntityQuestionary)
                                    .internationalBankInfo(internationalBankInfo)
                                    .build();
                    questionaryHolderBuilder.internationalLegalEntityQuestionaryHolder(internationalLegalEntityHolder);
                }
            }
        }

        return questionaryHolderBuilder.build();
    }
}
