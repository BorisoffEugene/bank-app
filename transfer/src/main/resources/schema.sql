-- схема
create schema if not exists transfer;

-- таблица transfer
create table if not exists transfer.transfer(
	id bigserial primary key,
	create_date timestamp not null default localtimestamp,
	account_from varchar not null,
	account_to varchar not null,
	amount int not null check(amount > 0),
	status varchar not null default 'OK',
	error varchar
);

comment on table transfer.transfer is 'Перевод денег';
comment on column transfer.transfer.id is 'ID транзакции';
comment on column transfer.transfer.create_date is 'Время транзакции';
comment on column transfer.transfer.account_from is 'Аккаунт (accounts.accounts.login) источник';
comment on column transfer.transfer.account_to is 'Аккаунт (accounts.accounts.login) получатель';
comment on column transfer.transfer.amount is 'Сумма перевода';
comment on column transfer.transfer.status is 'Статус действия ENUM(OK - успех, ERROR - ошибка)';
comment on column transfer.transfer.error is 'Текст ошибки';

create index if not exists idx_transfer_account on transfer.transfer (account_from);
create index if not exists idx_transfer_account on transfer.transfer (account_to);