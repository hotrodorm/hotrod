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
