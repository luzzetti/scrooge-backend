--liquibase formatted sql

--changeset christian.luzzetti:create-charity-users

INSERT INTO public.scrooge_users (id, email)
VALUES ('b264b173-a4eb-445c-a33e-40e17b2f8e8d',
        'charity@charity.charity');

INSERT INTO public.bank_accounts (id, balance, iban, mnemonic_name, owner_id)
VALUES ('132ca516-6de3-49a1-95f4-b1539e923161',
        100000.00,
        'IT60X0542811101000000123456',
        'charity',
        'b264b173-a4eb-445c-a33e-40e17b2f8e8d');
