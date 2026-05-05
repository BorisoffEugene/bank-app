-- схема
create schema if not exists cash;

-- таблица cash
create table if not exists cash.cash(
	id bigserial primary key,
	create_date timestamp not null default localtimestamp,
	account varchar not null,
	action varchar not null,
	amount int not null check(amount > 0),
	status varchar not null default 'OK',
	error varchar
);

comment on table cash.cash is 'Пополнение / снятие денег';
comment on column cash.cash.id is 'ID транзакции';
comment on column cash.cash.create_date is 'Время транзакции';
comment on column cash.cash.account is 'Аккаунт (accounts.accounts.login)';
comment on column cash.cash.action is 'Действие ENUM(PUT - пополнение, GET - снятие)';
comment on column cash.cash.amount is 'Сумма пополнения / снятия';
comment on column cash.cash.status is 'Статус действия ENUM(OK - успех, ERROR - ошибка)';
comment on column cash.cash.error is 'Текст ошибки';

create index if not exists idx_cash_account on cash.cash (account);