package org.hotrod.runtime.livesql.metadata;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.Expression;

public class AllColumns extends WrappingColumn {

  private List<Column> columns;

  public AllColumns(final Column... columns) {
    this.columns = Arrays.asList(columns);
  }

  public ColumnsSubset filter(final Predicate<Column> predicate) {
    return new ColumnsSubset(this.columns.stream().filter(predicate).collect(Collectors.toList()));
  }

  public ColumnsAliased as(final ColumnRenamer aliaser) {
    return new ColumnsAliased(this.columns.stream() //
        .map(c -> {
          return new AliasedExpression((Expression) c, aliaser.newName(c));
        }).collect(Collectors.toList()));
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
