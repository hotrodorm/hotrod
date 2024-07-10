package org.hotrod.runtime.livesql.metadata;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;

public class ColumnsSubset extends ColumnList {

  private List<Column> columns;

  protected ColumnsSubset(final List<Column> columns) {
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

  // ResultSetColumn

  @Override
  protected Expression getExpression() {
    return null;
  }

  @Override
  protected List<Expression> unwrap() {
    return this.columns.stream().map(c -> (Expression) c).collect(Collectors.toList());
  }

}
