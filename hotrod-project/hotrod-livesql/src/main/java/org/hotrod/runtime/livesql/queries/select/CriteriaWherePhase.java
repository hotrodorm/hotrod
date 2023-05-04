package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class CriteriaWherePhase<T> implements ExecutableCriteriaSelect<T> {

  private AbstractSelect<T> select;

  public CriteriaWherePhase(final TableOrView baseTable, final LiveSQLDialect sqlDialect, final SqlSession sqlSession,
      final Predicate whereCondition, final String mapperStatement) {
    this.select = new Select<T>(sqlDialect, false, sqlSession, mapperStatement, true);
    this.select.setBaseTable(baseTable);
    this.select.setWhereCondition(whereCondition);
  }

  // same phase

  // next phases

  public CriteriaOrderByPhase<T> orderBy(final OrderingTerm... orderingTerms) {
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
    return new CriteriaOrderByPhase<T>(this.select);
  }

  public CriteriaOffsetPhase<T> offset(final int offset) {
    this.select.setOffset(offset);
    return new CriteriaOffsetPhase<T>(this.select);
  }

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.select.setLimit(limit);
    return new CriteriaLimitPhase<T>(this.select);
  }

  // execute

  public List<T> execute() {
    return this.select.execute();
  }

  public Cursor<T> executeCursor() {
    return this.select.executeCursor();
  }
  
  // rendering

  @Override
  public void renderTo(QueryWriter w) {
    this.select.renderTo(w);
  }

  @Override
  public String getPreview() {
    return this.select.getPreview();
  }

}
