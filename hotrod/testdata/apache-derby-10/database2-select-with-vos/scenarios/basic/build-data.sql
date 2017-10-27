insert into category (id, name, percent_interest, interest_free_days) values (1, 'Entry Level', 0.01, 30);
insert into category (id, name, percent_interest, interest_free_days) values (2, 'Mid Level', 0.02, 20);
insert into category (id, name, percent_interest, interest_free_days) values (3, 'Senior Level', 0.05, 1);

insert into person (id, name, type, category_id) values (10, 'Peter', 2, 1);
insert into person (id, name, type, category_id) values (11, 'John', 3, 3);

insert into account (id, account_number, person_id, type, active, balance) values (50, 'T500-1', 10, 3, 1, 123.45);
insert into account (id, account_number, person_id, type, active, balance) values (51, 'T500-2', 10, 4, 1, 234.56);
insert into account (id, account_number, person_id, type, active, balance) values (52, 'T517-1', 11, 2, 1, 1000.00);

insert into "transaction" (id, executed_on, amount, account_id, type) values (1001, '2017-10-24', 70, 50, 1);
insert into "transaction" (id, executed_on, amount, account_id, type) values (1002, '2017-10-25', 71, 50, 2);

insert into "transaction" (id, executed_on, amount, account_id, type) values (1003, '2017-10-26', 72, 51, 3);

insert into "transaction" (id, executed_on, amount, account_id, type) values (1004, '2017-10-27', 73, 52, 4);
