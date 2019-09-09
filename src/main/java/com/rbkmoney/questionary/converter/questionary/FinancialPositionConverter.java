package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.geck.common.util.TBaseUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.enums.FinancialPosType;
import com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition;
import org.springframework.stereotype.Component;

@Component
public class FinancialPositionConverter implements ThriftConverter<com.rbkmoney.questionary.FinancialPosition, FinancialPosition>,
        JooqConverter<FinancialPosition, com.rbkmoney.questionary.FinancialPosition> {

    @Override
    public com.rbkmoney.questionary.FinancialPosition toThrift(FinancialPosition value, ThriftConverterContext ctx) {
        com.rbkmoney.questionary.FinancialPosition financialPosition = new com.rbkmoney.questionary.FinancialPosition();
        switch (value.getType()) {
            case statement_of_duty:
                financialPosition.setStatementOfDuty(new StatementOfDuty());
                break;
            case letter_of_guarantee:
                financialPosition.setLetterOfGuarantee(new LetterOfGuarantee());
                break;
            case annual_financial_statements:
                financialPosition.setAnnualFinancialStatements(new AnnualFinancialStatements());
                break;
            case annual_tax_return_with_mark:
                financialPosition.setAnnualTaxReturnWithMark(new AnnualTaxReturnWithMark());
                break;
            case annual_tax_return_without_mark:
                financialPosition.setAnnualTaxReturnWithoutMark(new AnnualTaxReturnWithoutMark());
                break;
            case quarterly_tax_return_with_mark:
                financialPosition.setQuarterlyTaxReturnWithMark(new QuarterlyTaxReturnWithMark());
                break;
            case quarterly_tax_return_without_mark:
                financialPosition.setQuarterlyTaxReturnWithoutMark(new QuarterlyTaxReturnWithoutMark());
                break;
            case annual_tax_return_without_mark_paper:
                financialPosition.setAnnualTaxReturnWithoutMarkPaper(new AnnualTaxReturnWithoutMarkPaper());
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown financial position: %s", value.getType()));
        }
        return financialPosition;

    }

    @Override
    public FinancialPosition toJooq(com.rbkmoney.questionary.FinancialPosition value, JooqConverterContext ctx) {
        FinancialPosition financialPosition = new FinancialPosition();
        financialPosition.setType(TBaseUtil.unionFieldToEnum(value, FinancialPosType.class));

        return financialPosition;
    }

}
