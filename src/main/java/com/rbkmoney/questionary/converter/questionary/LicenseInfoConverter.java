package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.LicenseInfo;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import org.springframework.stereotype.Component;

@Component
public class LicenseInfoConverter implements ThriftConverter<LicenseInfo, Questionary> {

    @Override
    public LicenseInfo toThrift(Questionary value, ThriftConverterContext ctx) {
        final LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setIssuerName(value.getLicenseIssuerName());
        licenseInfo.setOfficialNum(value.getLicenseOfficialNum());
        licenseInfo.setLicensedActivity(value.getLicenseLicensedActivity());
        if (value.getLicenseExpirationDate() != null) {
            licenseInfo.setExpirationDate(TypeUtil.temporalToString(value.getLicenseExpirationDate()));
        }
        if (value.getLicenseEffectiveDate() != null) {
            licenseInfo.setEffectiveDate(TypeUtil.temporalToString(value.getLicenseEffectiveDate()));
        }
        if (value.getLicenseIssueDate() != null) {
            licenseInfo.setIssueDate(TypeUtil.temporalToString(value.getLicenseIssueDate()));
        }

        return licenseInfo;
    }

}
