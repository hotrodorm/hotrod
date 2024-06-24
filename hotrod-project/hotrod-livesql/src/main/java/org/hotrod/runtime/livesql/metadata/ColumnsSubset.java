package org.hotrod.runtime.livesql.metadata;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;

public class ColumnsSubset extends ColumnList {

  private List<Expression> columns;

  protected ColumnsSubset(final List<Expression> columns) {
    this.columns = columns;
  }

  public boolean isEmpty() {
    return this.columns.isEmpty();
  }

  public ColumnsAliased as(final ColumnRenamer aliaser) {
    return new ColumnsAliased(this.columns.stream() //
        .map(c -> {
          return new AliasedExpression((ComparableExpression) c, aliaser.newName(c));
        }) //
        .collect(Collectors.toList()));
  }

  @Override
  protected List<Expression> unwrap() {
    return this.columns;
  }

}
