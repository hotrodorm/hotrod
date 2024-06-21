package org.hotrod.runtime.livesql.metadata;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.metadata.AllColumns.ColumnRenamer;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class ColumnsSubset implements ColumnList {

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
