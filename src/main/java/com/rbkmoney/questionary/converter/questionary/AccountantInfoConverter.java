package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.converter.*;
import com.rbkmoney.questionary.domain.enums.AccountantInfoType;
import com.rbkmoney.questionary.domain.enums.WithoutChiefAccountantType;
import com.rbkmoney.questionary.domain.tables.pojos.AdditionalInfo;
import org.springframework.stereotype.Component;

@Component
public class AccountantInfoConverter implements ThriftConverter<AccountantInfo, AdditionalInfo>,
        JooqFillConverter<AdditionalInfo, AccountantInfo> {

    @Override
    public AccountantInfo toThrift(AdditionalInfo value, ThriftConverterContext ctx) {
        AccountantInfo accountantInfo = new AccountantInfo();
        if (value.getAccountantInfoType() == AccountantInfoType.with_chef_accountant) {
            accountantInfo.setWithChiefAccountant(new WithChiefAccountant());
        } else if (value.getAccountantInfoType() == AccountantInfoType.without_chef_accountant) {
            WithoutChiefAccountantType accountantInfoWithoutChiefType = value.getAccountantInfoWithoutChiefType();
            if (accountantInfoWithoutChiefType == WithoutChiefAccountantType.individual_accountant) {
                accountantInfo.setWithoutChiefAccountant(WithoutChiefAccountant.individual_accountant(new IndividualAccountant()));
            } else if (accountantInfoWithoutChiefType == WithoutChiefAccountantType.accounting_organization) {
                AccountingOrganization accountingOrganization = new AccountingOrganization();
                accountingOrganization.setInn(value.getAccountantInfoInn());
                accountantInfo.setWithoutChiefAccountant(WithoutChiefAccountant.accounting_organization(accountingOrganization));
            } else if (accountantInfoWithoutChiefType == WithoutChiefAccountantType.head_accounting) {
                accountantInfo.setWithoutChiefAccountant(WithoutChiefAccountant.head_accounting(new HeadAccounting()));
            } else {
                throw new IllegalArgumentException("Unknown withoutChefAccountant type: " + value.getAccountantInfoType());
            }
        }

        return accountantInfo;
    }

    @Override
    public void fillJooq(AdditionalInfo fillableValue, AccountantInfo value, JooqConverterContext ctx) {
        if (value.isSetWithChiefAccountant()) {
            fillableValue.setAccountantInfoType(AccountantInfoType.with_chef_accountant);
        } else if (value.isSetWithoutChiefAccountant()) {
            fillableValue.setAccountantInfoType(AccountantInfoType.without_chef_accountant);
            if (value.getWithoutChiefAccountant().isSetIndividualAccountant()) {
                fillableValue.setAccountantInfoWithoutChiefType(WithoutChiefAccountantType.individual_accountant);
            } else if (value.getWithoutChiefAccountant().isSetAccountingOrganization()) {
                fillableValue.setAccountantInfoWithoutChiefType(WithoutChiefAccountantType.accounting_organization);
                fillableValue.setAccountantInfoInn(value.getWithoutChiefAccountant().getAccountingOrganization().getInn());
            } else if (value.getWithoutChiefAccountant().isSetHeadAccounting()) {
                fillableValue.setAccountantInfoWithoutChiefType(WithoutChiefAccountantType.head_accounting);
            } else {
                throw new IllegalArgumentException("Unknown withoutChefAccountant type: " + value.getWithoutChiefAccountant());
            }
        } else {
            throw new IllegalArgumentException("Unknown accountantInfoType");
        }
    }
}
