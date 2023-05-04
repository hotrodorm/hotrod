package org.hotrod.runtime.livesql.metadata;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class AllColumns implements ResultSetColumn {

  private Column[] columns;

  public AllColumns(final Column... columns) {
    this.columns = columns;
  }

  public ColumnSubset filter(final Predicate<Column> predicate) {
    return new ColumnSubset(Arrays.stream(this.columns).filter(predicate).collect(Collectors.toList()));
  }

  public static interface ColumnRenamer {
    String newName(Column c);
  }

  public ColumnAliased as(final ColumnRenamer aliaser) {
    return new ColumnAliased(Arrays.stream(this.columns) //
        .map(c -> {
          return new AliasedExpression((Expression) c, aliaser.newName(c));
        }) //
        .collect(Collectors.toList()));
  }

  @Override
  public void renderTo(QueryWriter w) {
    throw new UnsupportedOperationException();
  }

  public static interface ColumnList extends ResultSetColumn {
    boolean isEmpty();
  }

  public static class ColumnSubset implements ColumnList {

    private List<Column> columns;

    protected ColumnSubset(final List<Column> columns) {
      this.columns = columns;
    }

    public boolean isEmpty() {
      return this.columns.isEmpty();
    }

    public ColumnAliased as(final ColumnRenamer aliaser) {
      return new ColumnAliased(this.columns.stream() //
          .map(c -> {
            return new AliasedExpression((Expression) c, aliaser.newName(c));
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

  public static class ColumnAliased implements ColumnList {

    private List<ResultSetColumn> columns;

    protected ColumnAliased(final List<ResultSetColumn> columns) {
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

}
