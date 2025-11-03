-- TOP-N Queries
-- =============

-- If we want to retrieve a the first LIMIT rows:
-- your query here
fetch first LIMIT rows only

-- Example: retrieve the first 20 rows:
select * from customer order by last_name, first_name, address_id
fetch first 20 rows only

-- Standard Pagination Queries
-- ===========================
-- * VERY IMPORTANT: You must make sure you use an ORDER BY clause to ensure a stable order of the rows. Otherwise, the pagination rows may become unpredictable.
-- * The query needs to be MODIFIED, and the transformation is not that simple. The required changes are:
--   * Enclose the SQL in between two lines (trivial) as shown below.
--   * Identify the ORDER BY expression in the SQL.
--   * Add an extra column for the row number, and include the ORDER BY columns inside the OVER(...) section.
--   * Prefix all original columns with the table/view/alias names, as in: customer.* instead of simply *.
-- * An intermediate extra column for the row number is needed and should not have a conflicting name.
-- * First row is: 1
-- * The method shown here works on ALL versions of DB2, with or without compatibility modes enabled. Other possible strategies are (not explained here):
--   * a. Use the ROWNUM pseudo column. Very simple to use, but requires the Oracle compatibility mode turned on.
--   * b. Use the LIMIT & OFFSET keywords. Very simple to use, but requires the MYSQL compatiblity mode turned on.

-- If we want to retrieve a maximum of LIMIT rows with an OFFSET:

select * from  (
-- your MODIFIED query here (see above)
) where "pagination_$%&)(_rownum" > OFFSET and "pagination_$%&)(_rownum" <= OFFSET + LIMIT;

-- For example, if we want rows 41 to 50 (OFFSET 40, LIMIT 10), the original query:
select * from customer order by last_name, first_name, address_id
-- Becomes:
select * from  (
select row_number() over(order by last_name, first_name, address_id) as "pagination_$%&)(_rownum", customer.* from customer order by last_name, first_name, address_id
) where "pagination_$%&)(_rownum" > 40 and "pagination_$%&)(_rownum" <= 50;


-- In DB2 12:
-- ==========

-- OFFSET and LIMIT are available.



