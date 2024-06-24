package org.hotrod.runtime.livesql.metadata;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.Expression;

public class AllColumns extends WrappingColumn {

  private List<Expression> columns;

  public AllColumns(final Expression... columns) {
    this.columns = Arrays.asList(columns);
  }

  public ColumnsSubset filter(final Predicate<Expression> predicate) {
    return new ColumnsSubset(this.columns.stream().filter(predicate).collect(Collectors.toList()));
  }

  public ColumnsAliased as(final ColumnRenamer aliaser) {
    return new ColumnsAliased(this.columns.stream() //
        .map(c -> {
          return new AliasedExpression(c, aliaser.newName(c));
        }).collect(Collectors.toList()));
  }

  @Override
  protected List<Expression> unwrap() {
    return this.columns;
  }

}
