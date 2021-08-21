drop table if exists master;
create table master (
  id  varchar(40) primary key,
  hold_mark varchar(100),
  balance decimal(18,2) ,
  status int default 0,
  cif varchar(12) default null
);
----
drop table if exists bank_info;
create table bank_info (
  id  varchar(40) primary key,
  bic varchar(11),   -- swift
  iban varchar(34), --  International Bank Account Number (EUR)
  aba_no varchar(9), -- American Ba​​nkers Association (US)
  bsb_no varchar(6), -- Bank State Branch Number (AU)
  name varchar(200),
  addr varchar(500),
  corr_flag boolean default false  
);
create unique index idx_bank_info_bic on bank_info (bic);

----
drop table if exists swift_message;
create table swift_message (
  id  varchar(40) primary key,
  status int default 0,
  msg varchar default null
);
----
drop table if exists inward_rmt;
create table inward_rmt (
  id varchar(40) primary key,
  status int default 0,
  verify_status int default 0,
  txn_ref_no varchar(16),
  bank_op_code varchar(4),
  value_date int,  -- 4 to 6
  ccy varchar(3),
  amt decimal(13,2),
  order_cust varchar(140),
  benef_cust varchar(100), -- 140
  sender_corr varchar(35),
  receiver_corr varchar(35),
  rmt_info varchar(140),
  detail_charge varchar(3),
  sender_charge_ccy varchar(3),
  sender_charge decimal(13, 2),
  receiver_charge_ccy varchar(3),
  receiver_charge decimal(13, 2),
  acct_inst varchar(35),
  bank_info varchar(210) -- bank to bank
);
----




