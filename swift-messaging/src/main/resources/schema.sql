drop schema if exists swift CASCADE;
create schema swift;

---- 銀行名錄檔
drop table if exists swift.bank_info;
create table swift.bank_info (
  id  varchar(40) primary key,
  bic varchar(11),   -- swift
  iban varchar(34), --  International Bank Account Number (EUR)
  aba_no varchar(9), -- American Ba​​nkers Association (US)
  bsb_no varchar(6), -- Bank State Branch Number (AU)
  name varchar(200),
  addr varchar(500),
  corr_flag boolean default false  
);
create unique index idx_bank_info_bic on swift.bank_info (bic);

---- 電文檔
drop table if exists swift.swift_message;
create table swift.swift_message (
  id  varchar(40) primary key,
  status int default 0,
  msg varchar default null
);




