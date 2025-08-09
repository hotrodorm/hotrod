package org.hotrod.livesql.queries.select.tuples;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.expressions.ResultSetColumn;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.Join;
import org.hotrod.livesql.queries.select.TableExpression;

public class TuplesMetadata {

  private LiveSQLContext context;
  private List<CTE> ctes;
  private boolean distinct;
  private List<ResultSetColumn> resultSetColumns;

  private TableExpression from;
  private List<Join> joins;

  public TuplesMetadata(LiveSQLContext context, List<CTE> ctes, boolean distinct,
      List<ResultSetColumn> resultSetColumns) {
    this.context = context;
    this.ctes = ctes;
    this.distinct = distinct;
    this.resultSetColumns = resultSetColumns;
    this.from = null;
    this.joins = new ArrayList<>();
  }

  public void from(TableExpression t) {
    this.from = t;
  }

  public void join(Join j) {
    this.joins.add(j);
  }

  public final LiveSQLContext getContext() {
    return context;
  }

  public final List<CTE> getCtes() {
    return ctes;
  }

  public final boolean isDistinct() {
    return distinct;
  }

  public final List<ResultSetColumn> getResultSetColumns() {
    return resultSetColumns;
  }

  public final TableExpression getFrom() {
    return from;
  }

  public final List<Join> getJoins() {
    return joins;
  }

}
