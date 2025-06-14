package org.hotrod.livesql.queries.select.tuples;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.expressions.ResultSetColumn;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;

public class TuplesMetadata {

  private LiveSQLContext context;
  private List<CTE> ctes;
  private boolean distinct;
  private List<ResultSetColumn> resultSetColumns;

  private List<Table<?>> tables;

  public TuplesMetadata(LiveSQLContext context, List<CTE> ctes, boolean distinct,
      List<ResultSetColumn> resultSetColumns) {
    this.context = context;
    this.ctes = ctes;
    this.distinct = distinct;
    this.resultSetColumns = resultSetColumns;
    this.tables = new ArrayList<>();
  }

  public void add(Table<?> t) {
    this.tables.add(t);
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

  public final List<Table<?>> getTables() {
    return tables;
  }

}
