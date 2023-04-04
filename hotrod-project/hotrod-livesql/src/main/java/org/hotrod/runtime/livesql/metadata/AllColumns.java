package org.hotrod.runtime.livesql.metadata;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrodorm.hotrod.utils.SUtil;

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

  public static class Alias {
    private String alias;

    protected Alias(final String alias) {
      this.alias = alias;
    }

    protected Alias(final String... propertyParts) {
      StringBuilder sb = new StringBuilder();
      boolean first = true;
      for (String p : propertyParts) {
        if (first) {
          first = false;
          sb.append(p.toLowerCase());
        } else {
          sb.append(SUtil.upperFirst(p));
        }
      }
      this.alias = sb.toString();
    }

    public static Alias literal(final String alias) {
      return new Alias(alias);
    }

    public static Alias property(final String... alias) {
      return new Alias(alias);
    }

    public String getAlias() {
      return alias;
    }

  }

  public static interface ColumnRenamer {
    Alias rename(Column c);
  }

  public ColumnAliased as(final ColumnRenamer renamer) {
    return new ColumnAliased(Arrays.stream(this.columns) //
        .map(c -> {
          Alias a = renamer.rename(c);
          return new AliasedExpression((Expression) c, a.alias, true);
        }) //
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

    public ColumnAliased as(final ColumnRenamer renamer) {
      return new ColumnAliased(this.columns.stream() //
          .map(c -> {
            Alias a = renamer.rename(c);
            return new AliasedExpression((Expression) c, a.alias, true);
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
