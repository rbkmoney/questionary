ALTER TABLE qs.legal_owner
    DROP COLUMN IF EXISTS private_entity_first_name,
    DROP COLUMN IF EXISTS private_entity_second_name,
    DROP COLUMN IF EXISTS private_entity_second_name,
    DROP COLUMN IF EXISTS private_entity_middle_name,
    DROP COLUMN IF EXISTS identity_doc_series,
    DROP COLUMN IF EXISTS identity_doc_number;

ALTER TABLE qs.legal_owner
    ADD COLUMN private_entity_fio VARCHAR,
    ADD COLUMN identity_doc_series_number VARCHAR,
    ADD COLUMN head_position VARCHAR;

ALTER TABLE qs.beneficial_owner
    DROP COLUMN IF EXISTS private_entity_first_name,
    DROP COLUMN IF EXISTS private_entity_second_name,
    DROP COLUMN IF EXISTS identity_doc_series,
    DROP COLUMN IF EXISTS identity_doc_number;

ALTER TABLE qs.beneficial_owner
    ADD COLUMN private_entity_fio VARCHAR,
    ADD COLUMN identity_doc_series_number VARCHAR;

ALTER TABLE qs.individual_entity_questionary
    DROP COLUMN IF EXISTS private_entity_first_name,
    DROP COLUMN IF EXISTS private_entity_second_name,
    DROP COLUMN IF EXISTS private_entity_middle_name,
    DROP COLUMN IF EXISTS identity_doc_series,
    DROP COLUMN IF EXISTS identity_doc_number;

ALTER TABLE qs.individual_entity_questionary
    ADD COLUMN private_entity_fio VARCHAR,
    ADD COLUMN identity_doc_series_number VARCHAR;

ALTER TABLE qs.additional_info
    ADD COLUMN has_beneficiary BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN has_liquidation_process BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE qs.questionary ADD COLUMN has_beneficial_owners BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE qs.individual_entity_questionary
    ADD COLUMN pdl_category BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN pdl_relation_degree VARCHAR;

ALTER TABLE qs.legal_entity_questionary
    DROP COLUMN IF EXISTS founder_owner_first_name,
    DROP COLUMN IF EXISTS founder_owner_second_name,
    DROP COLUMN IF EXISTS founder_owner_middle_name;

ALTER TABLE qs.legal_entity_questionary ADD COLUMN founder_owner_fio VARCHAR;

ALTER TABLE qs.founder
    DROP COLUMN IF EXISTS first_name,
    DROP COLUMN IF EXISTS second_name,
    DROP COLUMN IF EXISTS middle_name;

ALTER TABLE qs.founder ADD COLUMN fio VARCHAR;

ALTER TABLE qs.head
    DROP COLUMN IF EXISTS first_name,
    DROP COLUMN IF EXISTS second_name,
    DROP COLUMN IF EXISTS middle_name;

ALTER TABLE qs.head ADD COLUMN fio VARCHAR;

