# The `<version-control-column>` Tag

This tag can be added to a table to enable Optimistic Locking in it. When activating it
this tag must indicate the column that will be used as a *versioning column*.


## Attributes

It includes the following attributes:

| Attribute | Description | Defaults to |
| -- | -- | -- |
| name | The column name to use for versionining. Must be an integer-like type | Required |


## Cycling Values

The version column values are considered cyclic. They have a starting value that falls within a range of
possible values. When reaching the maximum value the column falls back to the minimum value. The initial value and 
valid range are set by default by HotRod according to the default logic of each database
column type. It can also be customized with the `<column>` tag's attributes `initial-value`, `min-value`, and `max-value`.


## Optimistic Locking 

Optimistic Locking can be used to control concurrent updates on a table. These update operation
can take the form of an `UPDATE` or `DELETE` SQL statement that follows a `SELECT` SQL statement. 

When updating or deleting the row HotRod will craft the SQL statement to validate the row hasn't been changed since
reading it. If intact, the operation will succeed. If changed, a runtime exception will be thrown with 
the aim of aborting the transaction. The business logic may choose to catch this exeption and try again; it would need to 
read the row again while doing so, to make retrieve the updated values.


## Optimistic Locking Strategies

HotRod currently supports only one strategy to implement optimistic locking.

Optimistic Locking can be implemented at least in three different ways. HotRod currently supports only one of them:
- **Version Number**: (Supported by HotRod) A column of the table is dedicated to store a version number for the row. Every
time the row is updated the version number changes. `UPDATE` and `DELETE` operations can detect changes in 
this value and abort the ongoing transaction on such event.
- **Timestamp Column**: (Not supported by HotRod) If a timestamp column happens to be already included in the table 
it can be used for optimistic locking. Each time the row is changed this timestamp is updated. The upside 
of this strategy is that no extra column is required. The downsides of it is that the
column may have been intended for a different goal (not low level updates), and also the granularity of the timestamp
value could be insufficient on highly updated rows that can occur more than once a second (or millisecond).
- **Full Row Check**: (Not supported by HotRod) This strategy remembers the entire row and checks it while
updating or deleting it. The upside is that is doesn't require an extra column. The downside is that it needs to compare
all the columns of the table before updating, and this could place extra load in the database. This extra load could be 
significant when comparing LOB types or other heavy values that need to be sent back and forth over the network and then 
fully compared before performing update operations.




