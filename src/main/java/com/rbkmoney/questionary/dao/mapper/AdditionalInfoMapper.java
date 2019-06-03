package com.rbkmoney.questionary.dao.mapper;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.questionary.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.rbkmoney.questionary.domain.Tables.ADDITIONAL_INFO;

public class AdditionalInfoMapper implements RowMapper<AdditionalInfo> {

    @Override
    public AdditionalInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setStaffCount(rs.getInt(ADDITIONAL_INFO.STAFF_COUNT.getName()));
        additionalInfo.setHasAccountant(rs.getBoolean(ADDITIONAL_INFO.HAS_ACCOUNTANT.getName()));
        additionalInfo.setAccounting(rs.getString(ADDITIONAL_INFO.ACCOUNTING.getName()));
        additionalInfo.setAccountingOrg(rs.getString(ADDITIONAL_INFO.ACCOUNTING_ORG.getName()));
        additionalInfo.setNKORelationTarget(rs.getString(ADDITIONAL_INFO.NKO_RELATION_TARGET.getName()));
        additionalInfo.setRelationshipWithNKO(rs.getString(ADDITIONAL_INFO.RELATIONSHIP_WITH_NKO.getName()));
        final String monthOperationCount = rs.getString(ADDITIONAL_INFO.MONTH_OPERATION_COUNT.getName());
        if (monthOperationCount != null) {
            additionalInfo.setMonthOperationCount(TypeUtil.toEnumField(monthOperationCount, MonthOperationCount.class));
        }
        final String monthSum = rs.getString(ADDITIONAL_INFO.MONTH_OPERATION_SUM.getName());
        if (monthSum != null) {
            additionalInfo.setMonthOperationSum(TypeUtil.toEnumField(monthSum, MonthOperationSum.class));
        }
        additionalInfo.setStorageFacilities(rs.getBoolean(ADDITIONAL_INFO.STORAGE_FACILITIES.getName()));
        additionalInfo.setMainCounterparties(rs.getString(ADDITIONAL_INFO.COUNTERPARTIES.getName()));
        final String relationProc = rs.getString(ADDITIONAL_INFO.RELATION_PROCESS.getName());
        if (relationProc != null) {
            additionalInfo.setRelationIndividualEntity(TypeUtil.toEnumField(relationProc, RelationIndividualEntity.class));
        }
        additionalInfo.setBenefitThirdParties(rs.getBoolean(ADDITIONAL_INFO.BENEFIT_THIRD_PARTIES.getName()));
        final String businessRep = rs.getString(ADDITIONAL_INFO.BUSINESS_REPUTATION.getName());
        if (businessRep != null) {
            additionalInfo.setBusinessReputation(TypeUtil.toEnumField(businessRep, BusinessReputation.class));
        }

        return additionalInfo;
    }

}
