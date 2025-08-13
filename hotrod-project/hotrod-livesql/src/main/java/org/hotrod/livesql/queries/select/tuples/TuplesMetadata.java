package org.hotrod.livesql.queries.select.tuples;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.Join;
import org.hotrod.livesql.queries.select.TableExpression;

public class TuplesMetadata {

  private LiveSQLContext context;
  private List<CTE> ctes;
  private boolean distinct;
  private List<SQLExpression> resultSetColumns;

  private TableExpression from;
  private List<TuplesJoin> joins;

  public static class TuplesJoin {

    private Join join;
    private boolean includeInResultSet;

    public TuplesJoin(Join join, boolean includeInResultSet) {
      this.join = join;
      this.includeInResultSet = includeInResultSet;
    }

    public final Join getJoin() {
      return join;
    }

    public final boolean includeInResultSet() {
      return includeInResultSet;
    }

  }

  public TuplesMetadata(LiveSQLContext context, List<CTE> ctes, boolean distinct,
      List<SQLExpression> resultSetColumns) {
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
    this.joins.add(new TuplesJoin(j, true));
  }

  public void join(Join j, boolean includeInResultSet) {
    this.joins.add(new TuplesJoin(j, includeInResultSet));
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

  public final List<SQLExpression> getResultSetColumns() {
    return resultSetColumns;
  }

  public final TableExpression getFrom() {
    return from;
  }

  public final List<TuplesJoin> getJoins() {
    return joins;
  }

}
