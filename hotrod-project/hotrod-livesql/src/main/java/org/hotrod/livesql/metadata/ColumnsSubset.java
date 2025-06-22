package org.hotrod.livesql.metadata;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.livesql.expressions.AliasedExpression;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;

public class ColumnsSubset extends ColumnList {

  private List<EntityColumn> columns;

  protected ColumnsSubset(final List<EntityColumn> columns) {
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
  protected List<Expression> expand() {
    return this.columns.stream().map(c -> (Expression) c).collect(Collectors.toList());
  }

}
