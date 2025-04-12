--liquibase formatted sql

--changeset christian.luzzetti:init-tables
create table bank_accounts
(
    id            uuid           not null,
    balance       numeric(38, 2) not null,
    iban          varchar(50)    not null,
    mnemonic_name varchar(50)    not null,
    owner_id      uuid           not null,
    primary key (id)
);

create table scrooge_users
(
    id    uuid        not null,
    email varchar(50) not null,
    primary key (id)
);

create table transactions
(
    id                uuid           not null,
    amount            numeric(38, 2) not null,
    created_at        timestamptz    not null,
    source_account_id uuid           not null,
    target_account_id uuid           not null,
    primary key (id)
);

alter table if exists bank_accounts
    add constraint FK_BANK_ACCOUNT_TO_USERS foreign key (owner_id) references scrooge_users;

alter table if exists transactions
    add constraint FK_TRANSACTION_TO_BANK_ACCOUNT_SOURCE foreign key (source_account_id) references bank_accounts;

alter table if exists transactions
    add constraint FK_TRANSACTION_TO_BANK_ACCOUNT_TARGET foreign key (target_account_id) references bank_accounts