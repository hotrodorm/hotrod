# Analytical Functions

Analytical functions indirectly generated result values from the data they analize. 

They are divided in two groups, as shown below.


## Analytical Functions

These functions compute rank rows according to their values and produce a result in a different domain of values.

| ROW_NUMBER() OVER(...)
| RANK(<expression>) OVER(...)
| DENSE_RANK(<expression>) OVER(...)
| NTILE(<expression>) OVER(...)


## Positional Analytical Functions

Positional analytical functions peek at other rows of the table, view or result set to retrieve a value. They
can be useful to find changes in values over time or to differentiate different groups of data according to custom
logic.

| LEAD(<expression>) OVER(...)
| LAG(<expression>) OVER(...)


