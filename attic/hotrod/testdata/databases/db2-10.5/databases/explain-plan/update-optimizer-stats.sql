-- Update table statistics for the SQL optimizer

call sysproc.admin_cmd('runstats on table payment with distribution on all columns and detailed indexes all');















