--liquibase formatted sql

--changeset christian.luzzetti:create-status-column-for-soft-delete
ALTER TABLE bank_accounts
    ADD COLUMN status VARCHAR(6) CHECK (status IN ('OPEN', 'CLOSED')) default 'OPEN';