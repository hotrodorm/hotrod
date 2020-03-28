-- TOP-N Queries
-- =============

-- If we want to retrieve a the first LIMIT rows:

-- your query here
limit LIMIT

-- Example: retrieve the first 20 rows:
select * from customer order by last_name, first_name, address_id 
limit 20

-- Standard Pagination Queries
-- ===========================
-- * VERY IMPORTANT: You must make sure you use an ORDER BY clause to ensure a stable order of the rows. Otherwise, the pagination may become unpredictable.
-- * First row is: 1

-- If we want to retrieve a maximum of LIMIT rows with an OFFSET:
-- your query here
offset OFFSET limit LIMIT 

-- For example, if we want rows 41 to 50 (OFFSET 40, LIMIT 10):
select * from customer order by last_name, first_name, address_id
offset 40 limit 10
