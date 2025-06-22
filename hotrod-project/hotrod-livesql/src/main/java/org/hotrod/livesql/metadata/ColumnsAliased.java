package org.hotrod.livesql.metadata;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;

public class ColumnsAliased extends ColumnList {

  private List<Expression> columns;

  protected ColumnsAliased(final List<Expression> columns) {
    this.columns = columns;
  }

  public boolean isEmpty() {
    return this.columns.isEmpty();
  }

  // ResultSetColumn

  @Override
  protected List<Expression> expand() {
    return this.columns;
  }

}
