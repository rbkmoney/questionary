CREATE SCHEMA IF NOT EXISTS qs;

CREATE TYPE qs.questionary_entity_type AS ENUM ('legal', 'individual');

CREATE TABLE qs.questionary
(
    id            BIGSERIAL                         NOT NULL,
    owner_id      BIGINT                            NOT NULL,
    type          qs.questionary_entity_type NOT NULL,
    inn           CHARACTER VARYING,
    phone_number  CHARACTER VARYING                 NOT NULL,
    email         CHARACTER VARYING                 NOT NULL,
    site          CHARACTER VARYING                 NOT NULL,
    reg_date      TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
    reg_place     CHARACTER VARYING                 NOT NULL,
    okvd          CHARACTER VARYING                 NOT NULL,
    activity_type CHARACTER VARYING                 NOT NULL,
    property_info CHARACTER VARYING                 NOT NULL,
    tax_resident  BOOLEAN                           NOT NULL DEFAULT FALSE,

    CONSTRAINT questionary_pkey PRIMARY KEY (id)
);

CREATE INDEX questionary_owner_id on qs.questionary (owner_id);

CREATE TABLE qs.individual_entity_questionary
(
    id                          BIGSERIAL         NOT NULL,
    residence_name              CHARACTER VARYING,
    residence_series            CHARACTER VARYING,
    residence_number            CHARACTER VARYING,
    residence_beginning_date    TIMESTAMP WITHOUT TIME ZONE,
    residence_expiration_date   TIMESTAMP WITHOUT TIME ZONE,
    ogrnip                      CHARACTER VARYING NOT NULL,
    foreign_public_person       BOOLEAN           NOT NULL DEFAULT FALSE,
    foreign_relative_person     BOOLEAN           NOT NULL DEFAULT FALSE,
    behalf_of_foreign           BOOLEAN           NOT NULL DEFAULT FALSE,
    worldwide_org_public_person BOOLEAN           NOT NULL DEFAULT FALSE,
    has_representative          BOOLEAN           NOT NULL DEFAULT FALSE,
    beneficial_owner            BOOLEAN           NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_individual_entity_to_questionary FOREIGN KEY (id) REFERENCES qs.questionary (id)
);

CREATE TABLE qs.legal_entity_questionary
(
    id                 BIGSERIAL         NOT NULL,
    name               CHARACTER VARYING NOT NULL,
    foreign_name       CHARACTER VARYING,
    legal_form         CHARACTER VARYING NOT NULL,
    ogrn               CHARACTER VARYING NOT NULL,
    reg_address        CHARACTER VARYING NOT NULL,
    reg_actual_address CHARACTER VARYING,
    additional_space   CHARACTER VARYING NOT NULL,
    okato_code         CHARACTER VARYING,
    okpo_code          CHARACTER VARYING,
    postal_address     CHARACTER VARYING NOT NULL,
    owner_resident     BOOLEAN           NOT NULL DEFAULT FALSE,
    fatca              BOOLEAN           NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_legal_entity_to_questionary FOREIGN KEY (id) REFERENCES qs.questionary (id)
);

CREATE TABLE qs.license_info
(
    id             BIGSERIAL                   NOT NULL,
    questionary_id BIGINT                      NOT NULL,
    type           CHARACTER VARYING           NOT NULL,
    number         CHARACTER VARYING           NOT NULL,
    issue_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    issuer         CHARACTER VARYING           NOT NULL,
    validity       TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT license_info_pkey PRIMARY KEY (id),
    CONSTRAINT fk_license_info_to_questionary_data FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id)
);

CREATE UNIQUE INDEX license_info_to_q_data_id ON qs.license_info (questionary_id);

CREATE TYPE qs.month_operation_count AS ENUM ('lt_ten', 'btw_ten_to_fifty', 'gt_fifty');

CREATE TYPE qs.month_operation_sum AS ENUM ('lt_five_hundred_thousand', 'btw_five_hundred_thousand_to_one_million', 'gt_one_million');

CREATE TYPE qs.relation_process AS ENUM ('insolvency_proceedings', 'bankrupt_judicial_decision', 'liquidation_process');

CREATE TYPE qs.business_reputation AS ENUM ('provide_revieqs', 'no_reviews');

CREATE TABLE qs.additional_info
(
    id                    BIGSERIAL                       NOT NULL,
    questionary_id        BIGINT                          NOT NULL,
    staff_count           BOOLEAN                         NOT NULL DEFAULT FALSE,
    accounting            CHARACTER VARYING,
    accounting_org        CHARACTER VARYING,
    nko_relation_target   CHARACTER VARYING               NOT NULL,
    relationship_with_nko CHARACTER VARYING               NOT NULL,
    month_operation_count qs.month_operation_count NOT NULL,
    month_operation_sum   qs.month_operation_sum   NOT NULL,
    storage_facilities    BOOLEAN                         NOT NULL DEFAULT FALSE NOT NULL,
    counterparties        CHARACTER VARYING               NOT NULL,
    relation_process      qs.relation_process      NOT NULL,
    benefit_third_parties BOOLEAN                         NOT NULL DEFAULT FALSE NOT NULL,
    business_reputation   qs.business_reputation   NOT NULL,
    bank_details          CHARACTER VARYING               NOT NULL,

    CONSTRAINT additional_info_pkey PRIMARY KEY (id),
    CONSTRAINT fk_additional_info_to_questionary_data FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id)
);

CREATE INDEX additional_info_to_questionary_id ON qs.additional_info (questionary_id);

CREATE TABLE qs.financial_position
(
    id                 BIGSERIAL         NOT NULL,
    additional_info_id BIGINT            NOT NULL,
    description        CHARACTER VARYING NOT NULL,

    CONSTRAINT financial_position_pkey PRIMARY KEY (id),
    CONSTRAINT fk_financial_position_to_additional_info FOREIGN KEY (additional_info_id) REFERENCES qs.additional_info (id)
);

CREATE INDEX financial_position_additional_info_id ON qs.financial_position (additional_info_id);

CREATE TABLE qs.business_info
(
    id                 BIGSERIAL         NOT NULL,
    additional_info_id BIGINT            NOT NULL,
    description        CHARACTER VARYING NOT NULL,

    CONSTRAINT business_info_pkey PRIMARY KEY (id),
    CONSTRAINT fk_business_info_to_additional_info FOREIGN KEY (additional_info_id) REFERENCES qs.additional_info (id)
);

CREATE INDEX business_info_additional_info_id ON qs.business_info (additional_info_id);

CREATE TABLE qs.license_activity
(
    id              BIGSERIAL         NOT NULL,
    license_info_id BIGINT            NOT NULL,
    name            CHARACTER VARYING NOT NULL,

    CONSTRAINT license_activity_pkey PRIMARY KEY (id),
    CONSTRAINT fk_license_activities_to_license_info FOREIGN KEY (license_info_id) REFERENCES qs.license_info (id)
);

CREATE INDEX license_activities_q_data_id ON qs.license_activity (license_info_id);

CREATE TABLE qs.legal_org_info
(
    id             BIGSERIAL         NOT NULL,
    questionary_id BIGINT            NOT NULL,
    description    CHARACTER VARYING NOT NULL,

    CONSTRAINT legal_org_info_pkey PRIMARY KEY (id),
    CONSTRAINT fk_legal_org_info_to_questionary FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id)
);

CREATE TABLE qs.russian_private_entity
(
    id                BIGSERIAL                   NOT NULL,
    questionary_id    BIGINT                      NOT NULL,
    first_name        CHARACTER VARYING           NOT NULL,
    second_name       CHARACTER VARYING           NOT NULL,
    middle_name       CHARACTER VARYING           NOT NULL,
    birth_date        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    citizenship       CHARACTER VARYING           NOT NULL,
    residence_address CHARACTER VARYING           NOT NULL,
    actual_address    CHARACTER VARYING,
    contact_info      CHARACTER VARYING,

    CONSTRAINT russian_private_entity_pkey PRIMARY KEY (id),
    CONSTRAINT fk_russian_private_entity_to_questionary FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id)
);

CREATE INDEX russian_private_entity_questionary_id ON qs.russian_private_entity (questionary_id);

CREATE TABLE qs.identity_document
(
    id             BIGSERIAL NOT NULL,
    questionary_id BIGINT    NOT NULL,
    name           BIGSERIAL NOT NULL,

    CONSTRAINT identity_document_pkey PRIMARY KEY (id),
    CONSTRAINT fk_identity_doc_questionary FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id)
);

CREATE INDEX identity_document_questionary_id ON qs.identity_document (questionary_id);

CREATE TABLE qs.russian_passport
(
    id          BIGSERIAL                   NOT NULL,
    serial      CHARACTER VARYING,
    number      CHARACTER VARYING           NOT NULL,
    issuer      CHARACTER VARYING           NOT NULL,
    issuer_code CHARACTER VARYING,
    issued_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT fk_russian_passport_to_identity_document FOREIGN KEY (id) REFERENCES qs.identity_document (id)
);

CREATE TABLE qs.migration_card
(
    id              BIGSERIAL                   NOT NULL,
    questionary_id  BIGINT                      NOT NULL,
    card_number     CHARACTER VARYING           NOT NULL,
    beginning_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    expiration_date TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT migration_card_pkey PRIMARY KEY (id),
    CONSTRAINT fk_migration_card_to_questionary_data FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id)
);

CREATE INDEX migration_card_questionary_id ON qs.migration_card (questionary_id);

CREATE TABLE qs.residence_approve
(
    id              BIGSERIAL                   NOT NULL,
    questionary_id  BIGINT                      NOT NULL,
    name            CHARACTER VARYING           NOT NULL,
    series          CHARACTER VARYING,
    number          CHARACTER VARYING           NOT NULL,
    beginning_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    expiration_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT residence_approve_pkey PRIMARY KEY (id),
    CONSTRAINT fk_residence_approve_to_questionary FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id)
);

CREATE INDEX residence_approve_questionary_id ON qs.residence_approve (questionary_id);

CREATE TABLE qs.legal_owner
(
    id                   BIGSERIAL NOT NULL,
    questionary_id       BIGINT    NOT NULL,
    private_entity_id    BIGINT    NOT NULL,
    identity_document_id BIGINT    NOT NULL,
    migration_card_id    BIGINT,
    residence_approve_id BIGINT,
    inn                  CHARACTER VARYING,
    pdl_category         BOOLEAN   NOT NULL,

    CONSTRAINT legal_approve_pkey PRIMARY KEY (id),
    CONSTRAINT fk_legal_owner_to_questionary_data FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id),
    CONSTRAINT fk_legal_owner_to_private_entity FOREIGN KEY (private_entity_id) REFERENCES qs.russian_private_entity (id),
    CONSTRAINT fk_legal_owner_to_identity_doc FOREIGN KEY (identity_document_id) REFERENCES qs.identity_document (id),
    CONSTRAINT fk_legal_owner_to_migration_card FOREIGN KEY (migration_card_id) REFERENCES qs.migration_card (id),
    CONSTRAINT fk_legal_owner_to_residence_approve FOREIGN KEY (residence_approve_id) REFERENCES qs.residence_approve (id)
);

CREATE UNIQUE INDEX legal_owner_questionary_id ON qs.legal_owner (questionary_id);

CREATE TABLE qs.beneficial_owner
(
    id                   BIGSERIAL NOT NULL,
    questionary_id       BIGINT    NOT NULL,
    private_entity_id    BIGINT    NOT NULL,
    identity_document_id BIGINT    NOT NULL,
    migration_card_id    BIGINT,
    residence_approve_id BIGINT,
    ownership_percentage SMALLINT  NOT NULL,
    inn                  CHARACTER VARYING,
    pdl_category         BOOLEAN   NOT NULL,

    CONSTRAINT beneficial_owner_pkey PRIMARY KEY (id),
    CONSTRAINT fk_beneficial_owner_to_questionary_data FOREIGN KEY (questionary_id) REFERENCES qs.questionary (id),
    CONSTRAINT fk_beneficial_owner_to_private_entity FOREIGN KEY (private_entity_id) REFERENCES qs.russian_private_entity (id),
    CONSTRAINT fk_beneficial_owner_to_identity_doc FOREIGN KEY (identity_document_id) REFERENCES qs.identity_document (id),
    CONSTRAINT fk_beneficial_owner_to_migration_card FOREIGN KEY (migration_card_id) REFERENCES qs.migration_card (id),
    CONSTRAINT fk_beneficial_owner_to_residence_approve FOREIGN KEY (residence_approve_id) REFERENCES qs.residence_approve (id)
);

CREATE INDEX beneficial_owner_questionary_id ON qs.beneficial_owner (questionary_id)
