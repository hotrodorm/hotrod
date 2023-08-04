package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.Available;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.Const;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.ctes.CTE;

public class SelectCTEPhase<R> {

  // Properties

  private Select<R> select;

  // Constructor

  public SelectCTEPhase(final LiveSQLDialect sqlDialect, final SqlSession sqlSession, final LiveSQLMapper liveSQLMapper,
      final CTE... ctes) {
    Select<R> s = new Select<R>(sqlDialect, false, sqlSession, liveSQLMapper, false);
    s.setCTEs(Arrays.asList(ctes));
    this.select = s;
  }

  // Next stages

  public SelectColumnsPhase<R> select() {
    return new SelectColumnsPhase<R>(this.select, false);
  }

  public SelectColumnsPhase<R> selectDistinct() {
    return new SelectColumnsPhase<R>(this.select, true);
  }

  public SelectColumnsPhase<R> select(final ResultSetColumn... resultSetColumns) {
    return new SelectColumnsPhase<R>(this.select, false, resultSetColumns);
  }

  public SelectColumnsPhase<R> selectDistinct(final ResultSetColumn... resultSetColumns) {
    return new SelectColumnsPhase<R>(this.select, true, resultSetColumns);
  }

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public PGSelectColumnsPhase<R> selectDistinctOn(final ResultSetColumn... resultSetColumns) {
    return new PGSelectColumnsPhase<R>(this.select, true, resultSetColumns);
  }

}
