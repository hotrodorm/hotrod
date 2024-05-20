package org.hotrod.runtime.livesql.queries.select;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.AllColumns;
import org.hotrod.runtime.livesql.metadata.AllColumns.ColumnAliased;
import org.hotrod.runtime.livesql.metadata.AllColumns.ColumnList;
import org.hotrod.runtime.livesql.metadata.AllColumns.ColumnSubset;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.subqueries.AllSubqueryColumns;
import org.hotrod.runtime.livesql.util.ReflectionUtil;
import org.hotrod.runtime.livesql.util.SubqueryUtil;

public class SelectObject<R> extends AbstractSelectObject<R> {

  private boolean doNotAliasColumns;
  private List<ResultSetColumn> resultSetColumns = new ArrayList<>();

  public SelectObject(final List<CTE> ctes, final boolean distinct, final boolean doNotAliasColumns) {
    super(ctes, distinct);
    this.doNotAliasColumns = doNotAliasColumns;
  }

  public SelectObject(final List<CTE> ctes, final boolean distinct, final boolean doNotAliasColumns,
      final List<ResultSetColumn> resultSetColumns) {
    super(ctes, distinct);
    this.doNotAliasColumns = doNotAliasColumns;
    this.resultSetColumns = resultSetColumns;
  }

  public SelectObject(final List<CTE> ctes, final Expression[] distinctOn, final boolean doNotAliasColumns,
      final List<ResultSetColumn> resultSetColumns) {
    super(ctes, distinctOn);
    this.doNotAliasColumns = doNotAliasColumns;
    this.resultSetColumns = resultSetColumns;
  }

  // Setters

  public void setResultSetColumns(final List<ResultSetColumn> resultSetColumns) {
    this.resultSetColumns = resultSetColumns;
  }

  // Rendering

  @Override
  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
    super.writeExpandedColumns(w, baseTableExpression, joins, this.resultSetColumns, this.doNotAliasColumns);
  }

  private List<ResultSetColumn> columns = null;

  @Override
  public List<ResultSetColumn> listColumns() {
    if (this.columns == null) {
      this.columns = expandColumns(this.resultSetColumns);
    }
    return this.columns;
  }

  // Do not use inheritance to avoid exposing internal methods to the end user
  private List<ResultSetColumn> expandColumns(final List<ResultSetColumn> cols) {
    List<ResultSetColumn> list = new ArrayList<>();
    for (ResultSetColumn c : cols) {

      try {
        AllColumns ac = (AllColumns) c;

        List<Column> columns = ReflectionUtil.getColumnsField(ac, "columns");
        list.addAll(expandColumns(columns.stream().map(col -> (ResultSetColumn) col).collect(Collectors.toList())));

      } catch (IllegalAccessException e) {
        throw new LiveSQLException("Could not expand AllColumns", e);
      } catch (ClassCastException e1) {

        try {
          AllSubqueryColumns asc = (AllSubqueryColumns) c;
          list.addAll(this.expandSubqueryColumn(asc));
        } catch (ClassCastException e2) {
          try {
            ColumnList cl = (ColumnList) c;
            list.addAll(this.expandColumnList(cl));
          } catch (ClassCastException e3) {
            list.add(c);
          }
        }

      }

    }
    return list;
  }

  // Do not use inheritance to avoid exposing internal methods to the end user
  private List<ResultSetColumn> expandSubqueryColumn(final AllSubqueryColumns asc) {
    return SubqueryUtil.listColumns(asc);
  }

  // Do not use inheritance to avoid exposing internal methods to the end user
  private List<ResultSetColumn> expandColumnList(final ColumnList cl) {
    List<ResultSetColumn> list = new ArrayList<>();
    try {
      ColumnSubset cs = (ColumnSubset) cl;
      List<Column> columns = ReflectionUtil.getColumnsField(cs, "columns");
      list.addAll(expandColumns(columns.stream().map(c -> (ResultSetColumn) c).collect(Collectors.toList())));
    } catch (IllegalAccessException e) {
      throw new LiveSQLException("Could not expand ColumnSubset", e);
    } catch (ClassCastException e1) {

      try {
        ColumnAliased ca = (ColumnAliased) cl;
        List<ResultSetColumn> columns;
        try {
          columns = ReflectionUtil.getResultSetColumnsField(ca, "columns");
          list.addAll(expandColumns(columns));
        } catch (IllegalAccessException e) {
          throw new LiveSQLException("Could not expand ColumnAliased", e);
        }
      } catch (ClassCastException e2) {
        throw new LiveSQLException("Invalid ColumnList type: " + (cl == null ? "null" : cl.getClass().getName()), e2);
      }

    }
    return list;
  }

  @Override
  public void flatten() {
    // Nothing to do. It's already a single level
  }

}
