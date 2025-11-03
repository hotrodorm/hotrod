
create table payment (
  id number(18) primary key not null,
--  order_id number(18) not null,
  amount number(14,2) not null,
--  customer_name varchar2(40) not null,
--  credit_card_number varchar2(20) not null,
--  last_4_digits char(4) not null,
  placed_at date not null,
  status number(2) not null,
  charged_at date null,
--  last_try_at date null,
--  retries number(6) not null,
  payment_processor_id varchar2(24) not null  
);

-- full table scan

select * from payment where amount < 100;

-- Index Only Scan

create index ix1_placed_status_amount on payment (placed_at, status, amount);

select amount from payment where placed_at between '2017-05-01' and '2017-05-31' and status = 1;

-- Index Data Scan

select amount from payment where status = 1;

-- Index Full Scan

select status, amount from payment;

-- Index Range Scan

select amount from payment where placed_at between '2017-05-01' and '2017-05-31' and status = 1;

-- Index Range Scan (reverse)

create index ix4_status_amount on payment (status, amount);

select amount from payment where status = 1 and amount < 20;

-- Index Unique Scan

create index ix2_processor_id on payment (payment_processor_id);

select amount from payment where payment_processor_id = 'ax33-4r5y44-ww-00192387';

-- Index Merge

create index ix3_charged on payment (charged_at);

select amount from payment where charged between '2017-05-01' and '2017-05-02' or charged between '2018-05-01' and '2018-05-02';


