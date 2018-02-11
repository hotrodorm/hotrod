-- Update table statistics for the SQL optimizer

{call dbms_stats.gather_table_stats(USER, 'CODE')};
{call dbms_stats.gather_table_stats(USER, 'ADDRESS')};
{call dbms_stats.gather_table_stats(USER, 'SHIPMENT')};
{call dbms_stats.gather_table_stats(USER, 'PRODUCT')};
{call dbms_stats.gather_table_stats(USER, 'CUSTOMER')};
{call dbms_stats.gather_table_stats(USER, '"order"')};
{call dbms_stats.gather_table_stats(USER, 'ORDER_ITEM')};















