-- схема
create schema if not exists accounts;

-- таблица accounts
create table if not exists accounts.accounts(
    login varchar primary key,
    name varchar not null,
    birthdate date not null,
    sum int not null default 0 check(sum >= 0)
);

comment on table accounts.accounts is 'Акаунт';
comment on column accounts.accounts.login is 'Логин';
comment on column accounts.accounts.name is 'Имя';
comment on column accounts.accounts.birthdate is 'Дата рождения';
comment on column accounts.accounts.sum is 'Текущая сумма на счету';
