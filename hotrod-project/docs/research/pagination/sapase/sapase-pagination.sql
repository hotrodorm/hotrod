-- TOP-N Queries (decorated SQL):
-- ==============================

-- SAP ASE does not implement a TOP-N query with a simple decoration. The query must be modified -- see below.

-- TOP-N Queries (modified SQL):
-- =============================

-- you modified query here (adding TOP <N>)

-- Example: retrieve the first 20 rows:
select top 10 * from customer order by last_name, first_name, address_id

-- Standard Pagination Queries
-- ===========================
-- * VERY IMPORTANT: You must make sure you use an ORDER BY clause to ensure a stable order of the rows. Otherwise, the pagination may become unpredictable.
-- * Unfortunately Sybase does not implement standard pagination.
-- * There is a quite impractival workaround, really ugly. The drawbacks are:
--   * It's very inefficient for high values of OFFSET.
--   * It needs to materialize the whole range of rows (1 to OFFSET + LIMIT) into a temporary table.
--   * You must ensure to clean up (drop the temp table) after the query finishes.
--   * The second step (selecting from the temp table) includes the order by, but since there's no index on it, it will be slow.
--   * Creating an index on the temp table to make it fast is an extra step. This can be expensive.
--   * An extra column for the row number needs to be added, with a name that doesn't conflict with other columns.
-- * First row is: 1

-- If we want to retrieve a maximum of LIMIT rows with an OFFSET:
select -- your MODIFIED query here (including the extra column: top OFFSET + LIMIT)

select * from #tempa where "pagination_$%&)(_rownum" > OFFSET order by name

drop table #tempa


-- For example, if we want rows 41 to 50 (OFFSET 40, LIMIT 10):
select top 50 "pagination_$%&)(_rownum" = identity(9), * from customer order by last_name, first_name, address_id

select * from #tempa where "pagination_$%&)(_rownum" > 40 order order by last_name, first_name, address_id

drop table #tempa
