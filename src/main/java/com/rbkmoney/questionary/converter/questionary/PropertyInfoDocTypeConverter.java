package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.JooqFillConverter;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import org.springframework.stereotype.Component;

@Component
public class PropertyInfoDocTypeConverter implements ThriftConverter<PropertyInfoDocumentType, Questionary>,
        JooqFillConverter<Questionary, PropertyInfoDocumentType> {

    @Override
    public PropertyInfoDocumentType toThrift(Questionary value, ThriftConverterContext ctx) {
        if (value.getPropertyInfoDocType() == null) {
            return null;
        }
        switch (value.getPropertyInfoDocType()) {
            case lease_contract:
                return PropertyInfoDocumentType.lease_contract(new LeaseContract());
            case certificate_of_ownership:
                return PropertyInfoDocumentType.certificate_of_ownership(new CertificateOfOwnership());
            case sublease_contract:
                return PropertyInfoDocumentType.sublease_contract(new SubleaseContract());
            case other_property_info_document_type:
                OtherPropertyInfoDocumentType otherPropertyInfoDocumentType = new OtherPropertyInfoDocumentType();
                otherPropertyInfoDocumentType.setName(value.getPropertyInfoDocName());
                return PropertyInfoDocumentType.other_property_info_document_type(otherPropertyInfoDocumentType);
            default:
                throw new IllegalArgumentException("Unknown propertyInfoDocType: " + value.getPropertyInfoDocType());
        }
    }

    @Override
    public void fillJooq(Questionary fillableValue, PropertyInfoDocumentType value, JooqConverterContext ctx) {
        if (value.isSetCertificateOfOwnership()) {
            fillableValue.setPropertyInfoDocType(com.rbkmoney.questionary.domain.enums.PropertyInfoDocumentType.certificate_of_ownership);
        } else if (value.isSetLeaseContract()) {
            fillableValue.setPropertyInfoDocType(com.rbkmoney.questionary.domain.enums.PropertyInfoDocumentType.lease_contract);
        } else if (value.isSetSubleaseContract()) {
            fillableValue.setPropertyInfoDocType(com.rbkmoney.questionary.domain.enums.PropertyInfoDocumentType.sublease_contract);
        } else if (value.isSetOtherPropertyInfoDocumentType()) {
            fillableValue.setPropertyInfoDocType(com.rbkmoney.questionary.domain.enums.PropertyInfoDocumentType.other_property_info_document_type);
            fillableValue.setPropertyInfoDocName(value.getOtherPropertyInfoDocumentType().getName());
        } else {
            throw new IllegalArgumentException("Unknown propertyInfoDocType: " + value.getClass().getName());
        }

    }
}
