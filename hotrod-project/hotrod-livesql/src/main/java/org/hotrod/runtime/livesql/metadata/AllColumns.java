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

  private TableOrView tableOrView;
  private Column[] columns;

  public AllColumns(final TableOrView tableOrView, final Column... columns) {
    super();
    this.tableOrView = tableOrView;
    this.columns = columns;
  }

  public ColumnSubset filter(final Predicate<Column> predicate) {
    return new ColumnSubset(Arrays.stream(this.columns).filter(predicate).collect(Collectors.toList()));
  }

  public static interface ColumnRenamer {
    String rename(Column c);
  }

  public ColumnAliased as(final ColumnRenamer renamer) {
    return new ColumnAliased(Arrays.stream(this.columns) //
        .map(c -> new AliasedExpression((Expression) c, renamer.rename(c))) //
        .collect(Collectors.toList()));
  }

  @Override
  public void renderTo(QueryWriter w) {
    if (this.tableOrView.getAlias() != null) {
      w.write(this.tableOrView.getAlias());
      w.write(".");
    }
    w.write("*");
  }

  public static class ColumnSubset implements ResultSetColumn {

    private List<Column> columns;

    protected ColumnSubset(final List<Column> columns) {
      this.columns = columns;
    }

    public boolean isEmpty() {
      return this.columns.isEmpty();
    }

    public ColumnAliased as(final ColumnRenamer renamer) {
      return new ColumnAliased(this.columns.stream() //
          .map(c -> new AliasedExpression((Expression) c, renamer.rename(c))) //
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

  public static class ColumnAliased implements ResultSetColumn {

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
