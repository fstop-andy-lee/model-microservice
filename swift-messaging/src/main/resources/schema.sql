---- 帳號檔
drop table if exists master;
create table master (
  id  varchar(40) primary key,
  hold_mark varchar(100),
  balance decimal(18,2) ,
  status INT default 0,
  name varchar(200)
);
---- 銀行名錄檔
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

---- 電文檔
drop table if exists swift_message;
create table swift_message (
  id  varchar(40) primary key,
  status int default 0,
  msg varchar default null
);
---- 匯入資料檔
drop table if exists inward_rmt;
create table inward_rmt (
  id varchar(40) primary key,
  status int default 0,
  verify_status int default 0,
  txn_ref_no varchar(16),
  bank_op_code varchar(4),
  value_date int,  -- 4 to 6
  ccy varchar(3),
  inst_amt decimal(13,2),
  order_cust varchar(140),
  benef_cust varchar(40), -- 140
  benef_acct varchar(35),
  benef_name varchar(200),
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
---- 存同檔
drop table if exists bafotr;
create table bafotr (
  id varchar(40) primary key,  -- bank id : ccy
  bic varchar(11),
  ccy varchar(3),
  acct varchar(35),
  db_amt decimal(13, 2),
  cr_amt decimal(13, 2)
);  

---- 通知書檔
drop table if exists rmt_advice;
create table rmt_advice (
  id varchar(40) primary key,  -- our ref no
  value_date int,
  amt decimal(13, 2),
  ccy varchar(3),
  inst_amt decimal(13, 2),
  benef_name varchar(200),
  benef_acct varchar(35), -- A/C NO
  benef_cust varchar(140),
  order_cust varchar(140),
  order_bank varchar(140), -- 省略
  rmt_info varchar(140),
  status int default 0 
);  






