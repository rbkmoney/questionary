package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.file.storage.base.Residence;
import com.rbkmoney.questionary.InternationalBankAccount;
import com.rbkmoney.questionary.InternationalBankDetails;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.tables.pojos.InternationalBankInfo;
import org.springframework.stereotype.Component;

@Component
public class InternationalBankAccountConverter
        implements ThriftConverter<InternationalBankAccount, InternationalBankInfo>,
        JooqConverter<InternationalBankInfo, InternationalBankAccount> {

    @Override
    public InternationalBankAccount toThrift(InternationalBankInfo bankInfo,
                                             ThriftConverterContext ctx) {
        InternationalBankAccount bankAccount = new InternationalBankAccount();
        bankAccount.setNumber(bankInfo.getBankNumber());
        bankAccount.setIban(bankInfo.getBankIban());
        bankAccount.setAccountHolder(bankInfo.getBankAccountHolder());
        InternationalBankDetails bankDetails = new InternationalBankDetails();
        bankDetails.setName(bankInfo.getBankName());
        bankDetails.setAddress(bankInfo.getBankAddress());
        bankDetails.setAbaRtn(bankInfo.getBankAbaRtn());
        bankDetails.setBic(bankInfo.getBankBic());
        bankDetails.setCountry(bankInfo.getBankCountry() == null ?
                null : Residence.findByValue(bankInfo.getBankCountry()));
        bankAccount.setBank(bankDetails);

        InternationalBankAccount correspondentAccount = new InternationalBankAccount();
        correspondentAccount.setNumber(bankInfo.getCorrespondentAccountNumber());
        correspondentAccount.setIban(bankInfo.getCorrespondentAccountIban());
        correspondentAccount.setAccountHolder(bankInfo.getCorrespondentAccountHolder());
        InternationalBankDetails corrBankDetails = new InternationalBankDetails();
        corrBankDetails.setName(bankInfo.getCorrespondentAccountBankName());
        corrBankDetails.setAddress(bankInfo.getCorrespondentAccountBankAddress());
        corrBankDetails.setAbaRtn(bankInfo.getCorrespondentAccountBankAbaRtn());
        corrBankDetails.setBic(bankInfo.getCorrespondentAccountBankBic());
        corrBankDetails.setCountry(bankInfo.getCorrespondentAccountBankCountry() == null ?
                null : Residence.findByValue(bankInfo.getCorrespondentAccountBankCountry()));
        correspondentAccount.setBank(corrBankDetails);
        bankAccount.setCorrespondentAccount(correspondentAccount);

        return bankAccount;
    }

    @Override
    public InternationalBankInfo toJooq(InternationalBankAccount internationalBankAccount,
                                        JooqConverterContext ctx) {
        InternationalBankInfo bankInfo = new InternationalBankInfo();
        bankInfo.setBankNumber(internationalBankAccount.getNumber());
        bankInfo.setBankIban(internationalBankAccount.getIban());
        bankInfo.setBankAccountHolder(internationalBankAccount.getAccountHolder());
        if (internationalBankAccount.isSetBank()) {
            InternationalBankDetails internationalBankDetails = internationalBankAccount.getBank();
            bankInfo.setBankName(internationalBankDetails.getName());
            bankInfo.setBankAddress(internationalBankDetails.getAddress());
            bankInfo.setBankAbaRtn(internationalBankDetails.getAbaRtn());
            bankInfo.setBankBic(internationalBankDetails.getBic());
            bankInfo.setBankCountry(internationalBankDetails.getCountry() != null ?
                    internationalBankDetails.getCountry().getValue() : null);
        }
        if (internationalBankAccount.isSetCorrespondentAccount()) {
            InternationalBankAccount correspondentAccount = internationalBankAccount.getCorrespondentAccount();
            bankInfo.setCorrespondentAccountNumber(correspondentAccount.getNumber());
            bankInfo.setCorrespondentAccountIban(correspondentAccount.getIban());
            bankInfo.setCorrespondentAccountHolder(correspondentAccount.getAccountHolder());
            if (correspondentAccount.isSetBank()) {
                InternationalBankDetails correspondentAccountBank = correspondentAccount.getBank();
                bankInfo.setCorrespondentAccountBankName(correspondentAccountBank.getName());
                bankInfo.setCorrespondentAccountBankAddress(correspondentAccountBank.getAddress());
                bankInfo.setCorrespondentAccountBankAbaRtn(correspondentAccountBank.getAbaRtn());
                bankInfo.setCorrespondentAccountBankBic(correspondentAccountBank.getBic());
                bankInfo.setCorrespondentAccountBankCountry(correspondentAccountBank.getCountry() != null ?
                        correspondentAccountBank.getCountry().getValue() : null);
            }
        }
        return bankInfo;
    }

}
