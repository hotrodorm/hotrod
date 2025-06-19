package org.hotrod.livesql.queries.select.tuples;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.expressions.ResultSetColumn;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.Join;
import org.hotrod.livesql.queries.select.SShield;

public class TuplesMetadata {

  private LiveSQLContext context;
  private List<CTE> ctes;
  private boolean distinct;
  private List<ResultSetColumn> resultSetColumns;

  private Table<?> from;
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

  public <C> void from(Table<?> t) {
    this.from = t;
  }

  public void join(Join j) {
    this.joins.add(j);
    TableOrView<?> tv = (TableOrView<?>) SShield.getTableExpression(j);
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

  public final Table<?> getFrom() {
    return from;
  }

  public final List<Join> getJoins() {
    return joins;
  }

}
