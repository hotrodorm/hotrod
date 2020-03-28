-- Update table statistics for the SQL optimizer

call sysproc.admin_cmd('runstats on table code with distribution on all columns and detailed indexes all');
call sysproc.admin_cmd('runstats on table address with distribution on all columns and detailed indexes all');
call sysproc.admin_cmd('runstats on table shipment with distribution on all columns and detailed indexes all');
call sysproc.admin_cmd('runstats on table product with distribution on all columns and detailed indexes all');
call sysproc.admin_cmd('runstats on table customer with distribution on all columns and detailed indexes all');
call sysproc.admin_cmd('runstats on table "order" with distribution on all columns and detailed indexes all');
call sysproc.admin_cmd('runstats on table order_item with distribution on all columns and detailed indexes all');










