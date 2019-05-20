package com.rbkmoney.questionary.converter;

import com.rbkmoney.geck.common.util.TBaseUtil;
import com.rbkmoney.questionary.*;
import com.rbkmoney.questionary.domain.enums.BusinessInfoType;
import com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo;

public class BusinessInfoConverter implements ThriftConverter<BusinessInfo, com.rbkmoney.questionary.BusinessInfo> {

    @Override
    public com.rbkmoney.questionary.BusinessInfo convertToThrift(BusinessInfo value) {
        com.rbkmoney.questionary.BusinessInfo businessInfo = new com.rbkmoney.questionary.BusinessInfo();
        switch (value.getType()) {
            case building_business:
                businessInfo.setBuildingBusiness(new BuildingBusiness());
                break;
            case transport_business:
                businessInfo.setTransportBusiness(new TransportBusiness());
                break;
            case production_business:
                businessInfo.setProdictionBusiness(new ProductionBusiness());
                break;
            case retail_trade_business:
                businessInfo.setRetailTradeBusiness(new RetailTradeBusiness());
                break;
            case wholesale_trade_business:
                businessInfo.setWholesaleTradeBusiness(new WholesaleTradeBusiness());
                break;
            case securities_trading_business:
                businessInfo.setSecuritiesTradingBusiness(new SecuritiesTradingBusiness());
                break;
            case mediation_in_property_business:
                businessInfo.setMediationInPropertyBusiness(new MediationInPropertyBusiness());
                break;
            case another_business:
                businessInfo.setAnotherBusiness(new AnotherBusiness(value.getDescription()));
                break;
            default:
                throw new RuntimeException(String.format("Unknown business info: %s", businessInfo));
        }
        return businessInfo;
    }

    @Override
    public BusinessInfo convertFromThrift(com.rbkmoney.questionary.BusinessInfo value) {
        BusinessInfo businessInfo = new BusinessInfo();
        BusinessInfoType businessInfoType = TBaseUtil.unionFieldToEnum(value, BusinessInfoType.class);
        businessInfo.setType(businessInfoType);
        if (businessInfoType == BusinessInfoType.another_business && value.isSetAnotherBusiness()) {
            businessInfo.setDescription(value.getAnotherBusiness().getDescription());
        }

        return businessInfo;
    }
}
