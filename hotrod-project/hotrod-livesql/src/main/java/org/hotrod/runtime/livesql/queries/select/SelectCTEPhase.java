package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.Available;
import org.hotrod.runtime.livesql.dialects.Const;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;

public class SelectCTEPhase<R> {

  // Properties

  private LiveSQLContext context;
  private List<CTE> ctes;

  // Constructor

  public SelectCTEPhase(final LiveSQLContext context, final CTE... ctes) {
    this.context = context;
    this.ctes = Arrays.asList(ctes);
  }

  // Next stages

  public SelectColumnsPhase<R> select() {
    return new SelectColumnsPhase<R>(this.context, this.ctes, false);
  }

  public SelectColumnsPhase<R> selectDistinct() {
    return new SelectColumnsPhase<R>(this.context, this.ctes, true);
  }

  public SelectColumnsPhase<R> select(final ResultSetColumn... resultSetColumns) {
    return new SelectColumnsPhase<R>(this.context, this.ctes, false, resultSetColumns);
  }

  public SelectColumnsPhase<R> selectDistinct(final ResultSetColumn... resultSetColumns) {
    return new SelectColumnsPhase<R>(this.context, this.ctes, true, resultSetColumns);
  }

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public PGSelectColumnsPhase<R> selectDistinctOn(final ResultSetColumn... resultSetColumns) {
    return new PGSelectColumnsPhase<R>(this.context, this.ctes, true, resultSetColumns);
  }

}
