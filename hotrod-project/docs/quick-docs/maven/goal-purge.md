# Purging Dangling Temporary Views

The `purge` goal cleans up temporary dangling database views that may have resulted from an earlier HotRod crash.

Current versions of HotRod include the new `result-set` SQL Processor that do not create temporary views anymore and makes the
functionality described in this section obsolete. There are no temporary views anymore, so there's no need to clean them up anymore.

Previous versions HotRod included the old SQL Processor `create-view` as described in [Select Generation](../config/select-generation.md).
If you are using this SQL Processor, under error conditions some temporary views may remain in the sandbox database. If that's
the case the `purge` command identifies them and removes them automatically.

To use the `purge` goal type:

```bash
mvn hotrod:purge
```


