CREATE TABLE qs.international_legal_entity_questionary
(
    id                 BIGSERIAL          NOT NULL,
	ext_id             BIGINT             NOT NULL,
    legal_name         CHARACTER VARYING,
    trading_name       CHARACTER VARYING,
    registered_address CHARACTER VARYING,
    actual_address     CHARACTER VARYING,
    registered_number  CHARACTER VARYING,

    CONSTRAINT international_legal_entity_questionary_pkey PRIMARY KEY (ext_id),
    FOREIGN KEY (ext_id) REFERENCES qs.questionary (id)
);

CREATE TABLE qs.international_bank_info
(
    id                                    BIGSERIAL          NOT NULL,
	ext_id                                BIGINT             NOT NULL,
    bank_number                           CHARACTER VARYING,
    bank_iban                             CHARACTER VARYING,
    bank_account_holder                   CHARACTER VARYING,
    bank_name                             CHARACTER VARYING,
    bank_address                          CHARACTER VARYING,
    bank_aba_rtn                          CHARACTER VARYING,
    bank_bic                              CHARACTER VARYING,
    bank_country                          INTEGER,
    correspondent_account_number          CHARACTER VARYING,
    correspondent_account_iban            CHARACTER VARYING,
    correspondent_account_holder          CHARACTER VARYING,
    correspondent_account_bank_name       CHARACTER VARYING,
    correspondent_account_bank_address    CHARACTER VARYING,
    correspondent_account_bank_aba_rtn    CHARACTER VARYING,
    correspondent_account_bank_bic        CHARACTER VARYING,
    correspondent_account_bank_country    INTEGER,

    CONSTRAINT international_bank_info_pkey PRIMARY KEY (ext_id),
    FOREIGN KEY (ext_id) REFERENCES qs.questionary (id)
);

