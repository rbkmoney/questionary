CREATE SCHEMA IF NOT EXISTS qs;

CREATE TYPE qs.questionary_entity_type AS ENUM ('legal', 'individual');

CREATE TABLE qs.license_info
(
    id                BIGSERIAL NOT NULL,
    official_num      CHARACTER VARYING,
    issuer_name       CHARACTER VARYING,
    issue_date        TIMESTAMP WITHOUT TIME ZONE,
    effective_date    TIMESTAMP WITHOUT TIME ZONE,
    expiration_date   TIMESTAMP WITHOUT TIME ZONE,
    licensed_activity CHARACTER VARYING,

    CONSTRAINT license_info_pkey PRIMARY KEY (id)
);

CREATE TYPE qs.identity_document_type AS ENUM ('russian_passport');

CREATE TABLE qs.identity_document
(
    id          BIGSERIAL                 NOT NULL,
    type        qs.identity_document_type NOT NULL,
    series      CHARACTER VARYING,
    number      CHARACTER VARYING,
    issuer      CHARACTER VARYING,
    issuer_code CHARACTER VARYING,
    issued_at   TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT identity_document_pkey PRIMARY KEY (id)
);

CREATE TABLE qs.migration_card
(
    id              BIGSERIAL NOT NULL,
    card_number     CHARACTER VARYING,
    beginning_date  TIMESTAMP WITHOUT TIME ZONE,
    expiration_date TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT migration_card_pkey PRIMARY KEY (id)
);

CREATE TABLE qs.residence_approve
(
    id              BIGSERIAL,
    name            CHARACTER VARYING,
    series          CHARACTER VARYING,
    number          CHARACTER VARYING,
    beginning_date  TIMESTAMP WITHOUT TIME ZONE,
    expiration_date TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT residence_approve_pkey PRIMARY KEY (id)
);

CREATE TABLE qs.russian_private_entity
(
    id                BIGSERIAL NOT NULL,
    first_name        CHARACTER VARYING,
    second_name       CHARACTER VARYING,
    middle_name       CHARACTER VARYING,
    birth_date        TIMESTAMP WITHOUT TIME ZONE,
    birth_place       TIMESTAMP WITHOUT TIME ZONE,
    citizenship       CHARACTER VARYING,
    residence_address CHARACTER VARYING,
    actual_address    CHARACTER VARYING,
    phone_number      CHARACTER VARYING,
    email             CHARACTER VARYING,

    CONSTRAINT russian_private_entity_pkey PRIMARY KEY (id)
);

CREATE TABLE qs.legal_owner
(
    id                   BIGSERIAL NOT NULL,
    private_entity_id    BIGINT,
    identity_document_id BIGINT,
    migration_card_id    BIGINT,
    residence_approve_id BIGINT,
    inn                  CHARACTER VARYING,
    pdl_category         BOOLEAN   NOT NULL DEFAULT FALSE,

    CONSTRAINT legal_approve_pkey PRIMARY KEY (id),
    CONSTRAINT fk_legal_owner_to_private_entity FOREIGN KEY (private_entity_id) REFERENCES qs.russian_private_entity (id),
    CONSTRAINT fk_legal_owner_to_identity_doc FOREIGN KEY (identity_document_id) REFERENCES qs.identity_document (id),
    CONSTRAINT fk_legal_owner_to_migration_card FOREIGN KEY (migration_card_id) REFERENCES qs.migration_card (id),
    CONSTRAINT fk_legal_owner_to_residence_approve FOREIGN KEY (residence_approve_id) REFERENCES qs.residence_approve (id)
);

CREATE TABLE qs.questionary
(
    id                BIGSERIAL                  NOT NULL,
    questionary_id    CHARACTER VARYING          NOT NULL,
    version           BIGINT                     NOT NULL,
    owner_id          BIGINT,
    license_info_id   BIGINT,
    type              qs.questionary_entity_type NOT NULL,
    inn               CHARACTER VARYING,
    phone_number      CHARACTER VARYING,
    email             CHARACTER VARYING,
    shop_name         CHARACTER VARYING,
    shop_description  CHARACTER VARYING,
    shop_url          CHARACTER VARYING,
    reg_date          TIMESTAMP WITHOUT TIME ZONE,
    reg_place         CHARACTER VARYING,
    okvd              CHARACTER VARYING,
    activity_type     CHARACTER VARYING,
    tax_resident      BOOLEAN                    NOT NULL DEFAULT FALSE,
    bank_account      CHARACTER VARYING,
    bank_name         CHARACTER VARYING,
    bank_post_account CHARACTER VARYING,
    bank_bik          CHARACTER VARYING,

    UNIQUE (id),
    CONSTRAINT questionary_pkey PRIMARY KEY (questionary_id, version),
    CONSTRAINT fk_questionary_to_license_info FOREIGN KEY (license_info_id) REFERENCES qs.license_info (id)
);

CREATE INDEX questionary_owner_idx on qs.questionary (owner_id);
CREATE INDEX questionary_idx on qs.questionary (questionary_id);

CREATE TABLE qs.individual_entity_questionary
(
    id                          BIGSERIAL NOT NULL,
    private_entity_id           BIGINT,
    identity_document_id        BIGINT,
    migration_card_id           BIGINT,
    residence_approve_id        BIGINT,
    ogrnip                      CHARACTER VARYING,
    foreign_public_person       BOOLEAN   NOT NULL DEFAULT FALSE,
    foreign_relative_person     BOOLEAN   NOT NULL DEFAULT FALSE,
    behalf_of_foreign           BOOLEAN   NOT NULL DEFAULT FALSE,
    worldwide_org_public_person BOOLEAN   NOT NULL DEFAULT FALSE,
    has_representative          BOOLEAN   NOT NULL DEFAULT FALSE,
    beneficial_owner            BOOLEAN   NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_individual_entity_to_questionary FOREIGN KEY (id) REFERENCES qs.questionary (id),
    CONSTRAINT fk_individual_entity_to_private_entity_id FOREIGN KEY (private_entity_id) REFERENCES qs.russian_private_entity (id),
    CONSTRAINT fk_individual_entity_to_identity_document FOREIGN KEY (identity_document_id) REFERENCES qs.identity_document (id),
    CONSTRAINT fk_individual_entity_to_migration_card FOREIGN KEY (migration_card_id) REFERENCES qs.migration_card (id),
    CONSTRAINT fk_individual_entity_to_residence_approve FOREIGN KEY (residence_approve_id) REFERENCES qs.residence_approve (id)
);

CREATE TABLE qs.legal_entity_questionary
(
    id                        BIGSERIAL NOT NULL,
    legal_owner_id            BIGINT,
    name                      CHARACTER VARYING,
    foreign_name              CHARACTER VARYING,
    legal_form                CHARACTER VARYING,
    ogrn                      CHARACTER VARYING,
    reg_address               CHARACTER VARYING,
    reg_actual_address        CHARACTER VARYING,
    additional_space          CHARACTER VARYING,
    okato_code                CHARACTER VARYING,
    okpo_code                 CHARACTER VARYING,
    postal_address            CHARACTER VARYING,
    owner_resident            BOOLEAN   NOT NULL DEFAULT FALSE,
    fatca                     BOOLEAN   NOT NULL DEFAULT FALSE,
    founder_owner_first_name  CHARACTER VARYING,
    founder_owner_second_name CHARACTER VARYING,
    founder_owner_middle_name CHARACTER VARYING,
    founder_owner_inn         CHARACTER VARYING,
    founder_owner_position    CHARACTER VARYING,

    CONSTRAINT fk_legal_entity_to_questionary FOREIGN KEY (id) REFERENCES qs.questionary (id),
    CONSTRAINT fk_legal_entity_to_legal_owner FOREIGN KEY (legal_owner_id) REFERENCES qs.legal_owner (id)
);

CREATE TYPE qs.founder_type AS ENUM ('individual', 'legal','international_legal');

CREATE TABLE qs.founder
(
    id                  BIGSERIAL       NOT NULL,
    questionary_id      BIGINT          NOT NULL,
    type                qs.founder_type NOT NULL,
    first_name          CHARACTER VARYING,
    second_name         CHARACTER VARYING,
    middle_name         CHARACTER VARYING,
    inn                 CHARACTER VARYING,
    ogrn                CHARACTER VARYING,
    full_name           CHARACTER VARYING,
    country             CHARACTER VARYING,

    CONSTRAINT founder_pk PRIMARY KEY (id),
    CONSTRAINT fk_founder_to_questionary FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id)
);

CREATE INDEX founder_questionary_idx ON qs.founder (questionary_id);

CREATE TABLE qs.head
(
    id                  BIGSERIAL NOT NULL,
    questionary_id      BIGINT    NOT NULL,
    questionary_version BIGINT    NOT NULL,
    first_name          CHARACTER VARYING,
    second_name         CHARACTER VARYING,
    middle_name         CHARACTER VARYING,
    inn                 CHARACTER VARYING,
    position            CHARACTER VARYING,

    CONSTRAINT head_pk PRIMARY KEY (id),
    CONSTRAINT fk_head_to_questionary FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id)
);

CREATE INDEX head_questionary_idx ON qs.head (questionary_id);

CREATE TYPE qs.month_operation_count AS ENUM ('lt_ten', 'btw_ten_to_fifty', 'gt_fifty');

CREATE TYPE qs.month_operation_sum AS ENUM ('lt_five_hundred_thousand', 'btw_five_hundred_thousand_to_one_million', 'gt_one_million');

CREATE TYPE qs.relation_process AS ENUM ('insolvency_proceedings', 'bankrupt_judicial_decision', 'liquidation_process');

CREATE TYPE qs.business_reputation AS ENUM ('provide_reviews', 'no_reviews');

CREATE TYPE qs.financial_pos_type AS ENUM (
    'annual_financial_statements',
    'annual_tax_return_with_mark',
    'quarterly_tax_return_with_mark',
    'annual_tax_return_without_mark',
    'quarterly_tax_return_without_mark',
    'annual_tax_return_without_mark_paper',
    'statement_of_duty',
    'letter_of_guarantee'
    );

CREATE TYPE qs.business_info_type AS ENUM (
    'wholesale_trade_business',
    'retail_trade_business',
    'production_business',
    'building_business',
    'transport_business',
    'securities_trading_business',
    'mediation_in_property_business',
    'another_business'
    );

CREATE TABLE qs.property_info
(
    id                  BIGSERIAL NOT NULL,
    questionary_id      BIGINT    NOT NULL,
    description         CHARACTER VARYING,

    CONSTRAINT property_info_pkey PRIMARY KEY (id),
    CONSTRAINT fk_property_info_to_questionary FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id)
);

CREATE INDEX property_info_questionary_idx ON qs.property_info (questionary_id);

CREATE TABLE qs.additional_info
(
    id                    BIGSERIAL NOT NULL,
    staff_count           INTEGER,
    has_accountant        BOOLEAN   NOT NULL DEFAULT FALSE,
    accounting            CHARACTER VARYING,
    accounting_org        CHARACTER VARYING,
    nko_relation_target   CHARACTER VARYING,
    relationship_with_nko CHARACTER VARYING,
    month_operation_count qs.month_operation_count,
    month_operation_sum   qs.month_operation_sum,
    storage_facilities    BOOLEAN   NOT NULL DEFAULT FALSE,
    counterparties        CHARACTER VARYING,
    relation_process      qs.relation_process,
    benefit_third_parties BOOLEAN   NOT NULL DEFAULT FALSE,
    business_reputation   qs.business_reputation,

    CONSTRAINT additional_info_pkey PRIMARY KEY (id),
    CONSTRAINT fk_additional_info_to_questionary FOREIGN KEY (id) REFERENCES qs.questionary (id)
);

CREATE TABLE qs.financial_position
(
    id                      BIGSERIAL             NOT NULL,
    additional_info_id      BIGINT                NOT NULL,
    type                    qs.financial_pos_type NOT NULL,
    description             CHARACTER VARYING,

    CONSTRAINT financial_position_pkey PRIMARY KEY (id),
    CONSTRAINT fk_financial_position_to_additional_info FOREIGN KEY (additional_info_id)
        REFERENCES qs.additional_info (id)
);

CREATE INDEX financial_position_additional_info_idx ON qs.financial_position (additional_info_id);

CREATE TABLE qs.business_info
(
    id                      BIGSERIAL             NOT NULL,
    additional_info_id      BIGINT                NOT NULL,
    type                    qs.business_info_type NOT NULL,
    description             CHARACTER VARYING,

    CONSTRAINT business_info_pkey PRIMARY KEY (id),
    CONSTRAINT fk_business_info_to_additional_info FOREIGN KEY (additional_info_id)
        REFERENCES qs.additional_info (id)
);

CREATE INDEX business_info_additional_info_idx ON qs.business_info (additional_info_id);

CREATE TABLE qs.legal_org_info
(
    id                  BIGSERIAL NOT NULL,
    questionary_id      BIGINT    NOT NULL,
    description         CHARACTER VARYING,

    CONSTRAINT legal_org_info_pkey PRIMARY KEY (id),
    CONSTRAINT fk_legal_org_info_to_questionary FOREIGN KEY (questionary_id)
        REFERENCES qs.questionary (id)
);

CREATE INDEX legal_org_info_questionary_idx ON qs.legal_org_info (questionary_id);

CREATE TABLE qs.beneficial_owner
(
    id                   BIGSERIAL NOT NULL,
    questionary_id       BIGINT    NOT NULL,
    questionary_version  BIGINT    NOT NULL,
    private_entity_id    BIGINT,
    identity_document_id BIGINT,
    migration_card_id    BIGINT,
    residence_approve_id BIGINT,
    ownership_percentage SMALLINT,
    inn                  CHARACTER VARYING,
    pdl_category         BOOLEAN   NOT NULL DEFAULT FALSE,

    CONSTRAINT beneficial_owner_pkey PRIMARY KEY (id),
    CONSTRAINT fk_beneficial_owner_to_questionary_data FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id),
    CONSTRAINT fk_beneficial_owner_to_private_entity FOREIGN KEY (private_entity_id) REFERENCES qs.russian_private_entity (id),
    CONSTRAINT fk_beneficial_owner_to_identity_doc FOREIGN KEY (identity_document_id) REFERENCES qs.identity_document (id),
    CONSTRAINT fk_beneficial_owner_to_migration_card FOREIGN KEY (migration_card_id) REFERENCES qs.migration_card (id),
    CONSTRAINT fk_beneficial_owner_to_residence_approve FOREIGN KEY (residence_approve_id) REFERENCES qs.residence_approve (id)
);

CREATE INDEX beneficial_owner_questionary_idx ON qs.beneficial_owner (questionary_id, questionary_version)
