package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaWherePhase<T> implements ExecutableCriteriaSelect<T> {

  private LiveSQLContext context;
  private AbstractSelectObject<T> select;
  private String mapperStatement;

  public CriteriaWherePhase(final LiveSQLContext context, final String mapperStatement, final TableOrView baseTable,
      final Predicate whereCondition) {
    this.context = context;
    this.select = new SelectObject<T>(null, false, true);
    this.select.setBaseTableExpression(baseTable);
    this.select.setWhereCondition(whereCondition);
    this.mapperStatement = mapperStatement;
  }

  // same phase

  // next phases

  public CriteriaOrderByPhase<T> orderBy(final OrderingTerm... orderingTerms) {
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
    return new CriteriaOrderByPhase<T>(this.context, this.select, this.mapperStatement);
  }

  public CriteriaOffsetPhase<T> offset(final int offset) {
    this.select.setOffset(offset);
    return new CriteriaOffsetPhase<T>(this.context, this.select, this.mapperStatement);
  }

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.select.setLimit(limit);
    return new CriteriaLimitPhase<T>(this.context, this.select, this.mapperStatement);
  }

  // execute

  public List<T> execute() {
    return this.select.execute(this.context, this.mapperStatement);
  }

  public Cursor<T> executeCursor() {
    return this.select.executeCursor(this.context, this.mapperStatement);
  }

  // rendering

  @Override
  public void renderTo(QueryWriter w) {
    this.select.renderTo(w);
  }

  @Override
  public String getPreview() {
    return this.select.getPreview(this.context);
  }

}
