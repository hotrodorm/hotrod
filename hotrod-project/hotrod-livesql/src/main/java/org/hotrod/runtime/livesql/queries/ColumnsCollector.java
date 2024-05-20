package org.hotrod.runtime.livesql.queries;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

public class ColumnsCollector {

  private List<ResultSetColumn> queryColumns;

  public void register(final List<ResultSetColumn> queryColumns) {
    if (this.queryColumns != null) {
      throw new RuntimeException("Query columns already registered.");
    }
    this.queryColumns = queryColumns;
  }

}
