package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.AuthorityConfirmingDocument;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.JooqFillConverter;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.tables.pojos.LegalOwner;
import org.springframework.stereotype.Component;

@Component
public class AuthorityDocumentConverter implements ThriftConverter<AuthorityConfirmingDocument, LegalOwner>,
        JooqFillConverter<LegalOwner, AuthorityConfirmingDocument> {

    @Override
    public AuthorityConfirmingDocument toThrift(LegalOwner value, ThriftConverterContext ctx) {
        AuthorityConfirmingDocument authorityConfirmingDocument = new AuthorityConfirmingDocument();
        if (value.getAuthorityConfirmDocDate() != null) {
            authorityConfirmingDocument.setDate(TypeUtil.temporalToString(value.getAuthorityConfirmDocDate()));
        }
        authorityConfirmingDocument.setNumber(value.getAuthorityConfirmDocNumber());
        authorityConfirmingDocument.setType(value.getAuthorityConfirmDocType());

        return authorityConfirmingDocument;
    }

    @Override
    public void fillJooq(LegalOwner fillableValue, AuthorityConfirmingDocument value, JooqConverterContext ctx) {
        if (value.getDate() != null) {
            fillableValue.setAuthorityConfirmDocDate(TypeUtil.stringToLocalDateTime(value.getDate()));
        }
        fillableValue.setAuthorityConfirmDocNumber(value.getNumber());
        fillableValue.setAuthorityConfirmDocType(value.getType());
    }
}
