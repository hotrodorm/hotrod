package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.SetOperationRenderer.SetOperation;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class EnclosedSelect<R> extends AbstractSelect<R> {

  private AbstractSelect<R> select;

  public EnclosedSelect(final LiveSQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
      final String mapperStatement, final AbstractSelect<R> select, final LiveSQLMapper liveSQLMapper) {
    super(sqlDialect, distinct, sqlSession, mapperStatement, liveSQLMapper);
    this.select = select;
  }

  @Override
  protected void writeColumns(final QueryWriter w, final TableOrView baseTable, final List<Join> joins) {
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
