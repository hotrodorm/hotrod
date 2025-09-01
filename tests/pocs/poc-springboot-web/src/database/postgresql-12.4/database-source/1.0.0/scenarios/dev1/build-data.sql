insert into product (id, name, price, sku) values (1, 'Cup',     6, 1234);
insert into product (id, name, price, sku) values (2, 'Table', 150, 1235);
insert into product (id, name, price, sku) values (3, 'Knife',   8, 1236);
insert into product (id, name, price, sku) values (4, 'Spoon',   2, 1237);
insert into product (id, name, price, sku) values (5, 'Fork',    2, 1238);
insert into product (id, name, price, sku) values (6, 'Chair',  56, 1239);
insert into product (id, name, price, sku) values (7, 'Dish',    4, 1240);
insert into product (id, name, price, sku) values (8, 'Pan',    19, 1241);
insert into product (id, name, price, sku) values (9, 'Pot',    29, 1242);

insert into historic_price (product_id, from_date, price, sku) values (3, '2015-01-01', 5, null);
insert into historic_price (product_id, from_date, price, sku) values (3, '2017-06-15', 6, 1241);
insert into historic_price (product_id, from_date, price, sku) values (3, '2020-03-01', 8, null);

insert into island (id, segment, x_start, x_end, height) values (1, 10, 1, 4, 500);
insert into island (id, segment, x_start, x_end, height) values (2, 10, 1, 2, 501);
insert into island (id, segment, x_start, x_end, height) values (3, 10, 2, 3, 502);
insert into island (id, segment, x_start, x_end, height) values (4, 10, 3, 4, 503);
insert into island (id, segment, x_start, x_end, height) values (5, 10, 5, 8, 504);
insert into island (id, segment, x_start, x_end, height) values (5, 10, 5, 6, 505);
insert into island (id, segment, x_start, x_end, height) values (6, 10, 7, 9, 506);
insert into island (id, segment, x_start, x_end, height) values (10, 12, 3, 5, 400);
insert into island (id, segment, x_start, x_end, height) values (11, 12, 1, 3, 401);
insert into island (id, segment, x_start, x_end, height) values (12, 12, 4, 6, 402);
insert into island (id, segment, x_start, x_end, height) values (13, 12, 7, 9, 403);
insert into island (id, segment, x_start, x_end, height) values (14, 12, 7, 9, 404);
insert into island (id, segment, x_start, x_end, height) values (15, 12, 8, 10, 405);

insert into federal_branch (id, name) values (101, 'Virginia');
insert into federal_branch (id, name) values (102, 'Maryland');
insert into federal_branch (id, name) values (103, 'Delaware');

insert into account (id, current_balance, name, type, active, created_on) values (1234001, 120, 'CHK1004', 'CHK', 1, '2015-12-10');
insert into account (id, current_balance, name, type, active, created_on) values (1234004, 500, 'SAV7018', 'SAV', 1, '2015-12-08');
insert into account (id, current_balance, name, type, active, created_on) values (1234005, 45, 'CHK2301', 'CHK', 0, '2015-9-1');
  
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234001, 100, 'Time 001', 100, 101);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234004, 101, 'Time 004', 150, 103);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234005, 102, 'Time 005', 160, 102);
insert into transaction (account_id, seq_id, time, amount, fed_branch_id) 
  values (1234005, 115, 'Time 007', -110, 103);

insert into item (id, description, price, created_on, active, icon, store_code) values (1, 'Sweater', 123.45, '2018-01-21 08:05:12', true, null, '11bb9554-c616-42e6-a9c6-88d3bba4221c'); 
insert into item (id, description, price, created_on, active, icon, store_code) values (2, 'Pants', 789.01, '2018-02-22 12:34:56', false, null, '22779554-c616-42e6-a9c6-88d3bba4221c'); 
insert into item (id, description, price, created_on, active, icon, store_code) values (3, 'Jacker', 123.45, '2018-03-21 03:03:03', true, null, '33bb9554-c616-42e6-a9c6-88d3bba4221c'); 
insert into item (id, description, price, created_on, active, icon, store_code) values (4, 'Hat', 123.45, '2018-04-21 04:04:04', true, null, '44bb9554-c616-42e6-a9c6-88d3bba4221c'); 



