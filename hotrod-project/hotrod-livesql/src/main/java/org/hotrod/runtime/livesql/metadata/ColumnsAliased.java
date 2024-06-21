package org.hotrod.runtime.livesql.metadata;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class ColumnsAliased implements ColumnList {

  private List<ResultSetColumn> columns;

  protected ColumnsAliased(final List<ResultSetColumn> columns) {
    this.columns = columns;
  }

  public boolean isEmpty() {
    return this.columns.isEmpty();
  }

  @Override
  public void renderTo(QueryWriter w) {
    boolean first = true;
    for (int i = 0; i < this.columns.size(); i++) {
      if (first) {
        first = false;
      } else {
        w.write(", ");
      }
      this.columns.get(i).renderTo(w);
    }

  }

}
