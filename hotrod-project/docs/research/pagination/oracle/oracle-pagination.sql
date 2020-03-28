-- TOP-N Queries
-- =============

-- If we want to retrieve a the first LIMIT rows:
select * from (
  -- your query here
) where rownum <= LIMIT

-- Example: retrieve the first 20 rows:
select * from (
  select * from customer order by last_name, first_name, address_id
) where rownum <= 20

-- Standard Pagination Queries
-- ===========================
-- * VERY IMPORTANT: You must make sure you use an ORDER BY clause to ensure a stable order of the rows. Otherwise, the pagination may become unpredictable.
-- * The syntax is tricky since it names an intermediate table and also an extra pseudo column.
-- * We need to make sure the table and column names are unique. I purposedly chose very exotic names to avoid names conflict.
-- * First row is: 1

-- If we want to retrieve a maximum of LIMIT rows with an OFFSET:

select * from ( select rownum "pagination_$%&)(_rownum", "pagination_$%&)(_resultset".* from (
  -- your query here
) "pagination_$%&)(_resultset" where rownum <= OFFSET + LIMIT) where "pagination_$%&)(_rownum" > OFFSET;

-- For example, if we want rows 41 to 50 (OFFSET 40, LIMIT 10):

select * from ( select rownum "pagination_$%&)(_rownum", "pagination_$%&)(_resultset".* from (
  select * from customer order by last_name, first_name, address_id
) "pagination_$%&)(_resultset" where rownum <= 50 ) where "pagination_$%&)(_rownum" > 40;

-- OPTION 2 for Pagination:
-- ========================

select * from account order by name;

select * from (
  select a.*, row_number() over(order by name) as my_row_number from account a
) x where my_row_number between OFFSET and OFFSET + LIMIT - 1;

select * from (
  select a.*, row_number() over(order by name) as my_row_number from account a
) x where my_row_number between 40 and 49;
