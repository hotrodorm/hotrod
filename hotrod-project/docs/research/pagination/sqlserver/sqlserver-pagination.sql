-- TOP-N Queries (decorated SQL):
-- ==============================

-- your query
offset 0 rows fetch next LIMIT rows only

-- Example: retrieve the first 20 rows:
select * from customer order by last_name, first_name, address_id
offset 0 rows fetch next 20 rows only

-- TOP-N Queries (modified SQL):
-- =============================

-- you modified query here (adding TOP <N>)

-- Example: retrieve the first 20 rows:
select top 10 * from customer order by last_name, first_name, address_id

-- Standard Pagination Queries
-- ===========================
-- * VERY IMPORTANT: You must make sure you use an ORDER BY clause to ensure a stable order of the rows. Otherwise, the pagination may become unpredictable.
-- * First row is: 1

-- If we want to retrieve a maximum of LIMIT rows with an OFFSET:
-- your query here
offset OFFSET rows fetch next LIMIT rows only

-- For example, if we want rows 41 to 50 (OFFSET 40, LIMIT 10):
select * from customer order by last_name, first_name, address_id
offset 40 rows fetch next 10 rows only
