package org.hotrod.runtime.livesql.queries.select;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.MDHelper;
import org.hotrod.runtime.livesql.metadata.WrappingColumn;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.subqueries.SubqueryColumn;

public class SelectObject<R> extends AbstractSelectObject<R> {

  private static final Logger log = Logger.getLogger(SelectObject.class.getName());

  private boolean doNotAliasColumns;
  private List<ResultSetColumn> resultSetColumns = new ArrayList<>();

  private List<Expression> expandedColumns;
//  protected LinkedHashMap<String, QueryColumn> queryColumns;

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
  // TODO: Nothing to do -- just a marker

  @Override
  protected List<Expression> assembleColumns() {

    // 1. Compute columns of subqueries in the FROM and JOIN clauses

    this.baseTableExpression.assembleColumns();
    for (Join j : this.joins) {
      j.assembleColumns();
    }

    // 2. Expand columns in the select list

    this.expandColumns();

    // 4. Return columns

    return this.expandedColumns;

  }

//  @Deprecated
//  protected void computeQueryColumns() {
//    log.info("SELECT[" + System.identityHashCode(this) + "].computeQueryColumns()");
//
//    // 1. Compute columns from subqueries in the FROM/JOIN clauses
//
//    this.baseTableExpression.assembleColumns();
//    for (Join j : this.joins) {
//      j.assembleColumns();
//    }
//
//    // 2. Expand columns in the select list
//
//    this.expandColumns();
//
//    // 3. Compute aliased columns and subquery columns
//
//    for (Expression expr : this.expandedColumns) {
////      log.info("............................ computing for " + expr.getClass().getName());
//      Helper.computeQueryColumns(expr);
//    }
//
//    // 4. Compute query columns
//
////    this.queryColumns = new LinkedHashMap<>();
////    for (Expression expr : this.expandedColumns) {
//////      log.info(" ");
//////      log.info("Will resolve alias. expr:" + expr.getClass().getName());
////      String alias = resolveAlias(expr);
////      TypeHandler typeHandler = Helper.getTypeHandler(expr);
////      log.info("alias=" + alias + " typeHandler=" + typeHandler);
////      this.queryColumns.put(alias, new QueryColumn(alias, typeHandler));
////    }
//
//  }

  private String resolveAlias(final Expression c) {
    try {
      Column col = (Column) c;
      return col.getProperty();
    } catch (ClassCastException e) {
      try {
        AliasedExpression ae = (AliasedExpression) c;
        return ae.getName();
      } catch (ClassCastException e2) {
        try {
          SubqueryColumn sc = (SubqueryColumn) c;
          String alias = sc.getReferencedColumnName();
          return alias;
        } catch (ClassCastException e3) {
          return null;
        }
      }
    }
  }

  private void expandColumns() {

    this.expandedColumns = new ArrayList<>();

    if (this.resultSetColumns == null || this.resultSetColumns.isEmpty()) {

      // 1. Expand unlisted columns

      try {
        this.expandedColumns.addAll(this.baseTableExpression.assembleColumns());
        for (Join j : this.joins) {
          this.expandedColumns.addAll(j.getTableExpression().assembleColumns());
        }
//      } catch (IllegalAccessException e) {
//        throw new LiveSQLException("Could not expand LiveSQL columns (all)", e);
      } catch (RuntimeException e) {
        throw new LiveSQLException("Could not expand LiveSQL columns (all)", e);
      }

    } else {

      // 2. Expand columns subsets (star(), filtered star(), etc.)

      for (ResultSetColumn rsc : this.resultSetColumns) {
        try {
          List<Expression> scols = unwrapColumn(rsc);
          if (scols == null) {
            Expression expr = (Expression) rsc;
            this.expandedColumns.add(expr);
          } else {
            for (ResultSetColumn sc : scols) {
              Expression expr = (Expression) sc;
              this.expandedColumns.add(expr);
            }
          }
        } catch (ClassCastException e) {
          throw new LiveSQLException("Could not expand subset of LiveSQL columns", e);
        } catch (RuntimeException e) {
          throw new LiveSQLException("Could not expand subset of LiveSQL columns", e);
        }
      }

    }
  }

  private List<Expression> unwrapColumn(final ResultSetColumn c) {
    try {
      WrappingColumn wc = (WrappingColumn) c;
      return MDHelper.unwrap(wc);
    } catch (ClassCastException e) {
      return null;
    }
  }

//  @Override
//  protected LinkedHashMap<String, QueryColumn> getQueryColumns() {
//    return null;
//  }

  @Override
  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
    super.writeExpandedColumns(w, this.expandedColumns, this.doNotAliasColumns);
  }

  private List<ResultSetColumn> listedColumns = null;

//  @Override
//  public List<ResultSetColumn> listColumns() {
//    log.info(
//        "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ listColumns()");
//    if (this.listedColumns == null) {
//      this.listedColumns = expandColumns(this.resultSetColumns);
//    }
//    return this.listedColumns;
//  }

//  // Do not use inheritance to avoid exposing internal methods to the end user
//  private List<ResultSetColumn> expandColumns(final List<ResultSetColumn> cols) {
//    List<ResultSetColumn> list = new ArrayList<>();
//    for (ResultSetColumn c : cols) {
//
//      try {
//        AllColumns ac = (AllColumns) c;
//
//        List<Column> columns = ReflectionUtil.getColumnsField(ac, "columns");
//        list.addAll(expandColumns(columns.stream().map(col -> (ResultSetColumn) col).collect(Collectors.toList())));
//
//      } catch (IllegalAccessException e) {
//        throw new LiveSQLException("Could not expand AllColumns", e);
//      } catch (ClassCastException e1) {
//
//        try {
//          AllSubqueryColumns asc = (AllSubqueryColumns) c;
//          list.addAll(this.expandSubqueryColumn(asc));
//        } catch (ClassCastException e2) {
//          try {
//            ColumnList cl = (ColumnList) c;
//            list.addAll(this.expandColumnList(cl));
//          } catch (ClassCastException e3) {
//            list.add(c);
//          }
//        }
//
//      }
//
//    }
//    return list;
//  }
//
//  // Do not use inheritance to avoid exposing internal methods to the end user
//  private List<ResultSetColumn> expandSubqueryColumn(final AllSubqueryColumns asc) {
//    return SubqueryUtil.listColumns(asc);
//  }
//
//  // Do not use inheritance to avoid exposing internal methods to the end user
//  private List<ResultSetColumn> expandColumnList(final ColumnList cl) {
//    List<ResultSetColumn> list = new ArrayList<>();
//    try {
//      ColumnsSubset cs = (ColumnsSubset) cl;
//      List<Column> columns = ReflectionUtil.getColumnsField(cs, "columns");
//      list.addAll(expandColumns(columns.stream().map(c -> (ResultSetColumn) c).collect(Collectors.toList())));
//    } catch (IllegalAccessException e) {
//      throw new LiveSQLException("Could not expand ColumnSubset", e);
//    } catch (ClassCastException e1) {
//
//      try {
//        ColumnsAliased ca = (ColumnsAliased) cl;
//        List<ResultSetColumn> columns;
//        try {
//          columns = ReflectionUtil.getResultSetColumnsField(ca, "columns");
//          list.addAll(expandColumns(columns));
//        } catch (IllegalAccessException e) {
//          throw new LiveSQLException("Could not expand ColumnAliased", e);
//        }
//      } catch (ClassCastException e2) {
//        throw new LiveSQLException("Invalid ColumnList type: " + (cl == null ? "null" : cl.getClass().getName()), e2);
//      }
//
//    }
//    return list;
//  }

  @Override
  public void flatten() {
    // Nothing to do. It's already a single level
  }

}
