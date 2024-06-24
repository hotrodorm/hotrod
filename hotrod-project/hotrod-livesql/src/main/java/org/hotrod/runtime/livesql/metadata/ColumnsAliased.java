package org.hotrod.runtime.livesql.metadata;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;

public class ColumnsAliased extends ColumnList {

  private List<Expression> columns;

  protected ColumnsAliased(final List<Expression> columns) {
    this.columns = columns;
  }

  public boolean isEmpty() {
    return this.columns.isEmpty();
  }

  @Override
  protected List<Expression> unwrap() {
    return this.columns;
  }

}
