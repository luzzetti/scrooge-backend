--liquibase formatted sql

--changeset christian.luzzetti:add-causale-column-to-transactions
ALTER TABLE transactions
ADD COLUMN causale VARCHAR(150);