create table payment (
  id decimal(18) primary key not null,
  amount decimal(14,2) not null,
  placed_at date not null,
  status decimal(2) not null,
  charged_at date null,
  payment_processor_id varchar(24) not null  
);

-- Index Only Scan

create index ix1_placed_status_amount on payment (placed_at, status, amount);

-- Index Range Scan (reverse)

create index ix4_status_amount on payment (status, amount);

-- Index Unique Scan

create unique index ix2_processor_id on payment (payment_processor_id);

-- Index Merge

create index ix3_charged on payment (charged_at);
