package org.hotrod.runtime.livesql.queries.select;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.dialects.SetOperationRenderer.SetOperation;

public class EnclosedSelect<R> extends AbstractSelect<R> {

  private AbstractSelect<R> select;

  public EnclosedSelect(final SQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
      final String mapperStatement, final AbstractSelect<R> select, final LiveSQLMapper liveSQLMapper) {
    super(sqlDialect, distinct, sqlSession, mapperStatement, liveSQLMapper);
    this.select = select;
  }

  @Override
  protected void writeColumns(final QueryWriter w) {
    w.write("\n(\n");
    w.enterLevel();
    this.select.renderTo(w);
    w.exitLevel();
    w.write("\n)\n");
  }

  // Set operations

  public SelectHavingPhase<R> union(final CombinableSelect<R> select) {
    this.select.setCombinedSelect(SetOperation.UNION, select);
    return new SelectHavingPhase<R>(this.select, null);
  }

  public SelectHavingPhase<R> unionAll(final CombinableSelect<R> select) {
    this.select.setCombinedSelect(SetOperation.UNION_ALL, select);
    return new SelectHavingPhase<R>(this.select, null);
  }

  public SelectHavingPhase<R> intersect(final CombinableSelect<R> select) {
    this.select.setCombinedSelect(SetOperation.INTERSECT, select);
    return new SelectHavingPhase<R>(this.select, null);
  }

  public SelectHavingPhase<R> intersectAll(final CombinableSelect<R> select) {
    this.select.setCombinedSelect(SetOperation.INTERSECT_ALL, select);
    return new SelectHavingPhase<R>(this.select, null);
  }

  public SelectHavingPhase<R> except(final CombinableSelect<R> select) {
    this.select.setCombinedSelect(SetOperation.EXCEPT, select);
    return new SelectHavingPhase<R>(this.select, null);
  }

  public SelectHavingPhase<R> exceptAll(final CombinableSelect<R> select) {
    this.select.setCombinedSelect(SetOperation.EXCEPT_ALL, select);
    return new SelectHavingPhase<R>(this.select, null);
  }

}
