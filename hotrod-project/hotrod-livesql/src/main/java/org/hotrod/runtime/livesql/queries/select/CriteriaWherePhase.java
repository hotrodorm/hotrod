package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaWherePhase<T> implements ExecutableCriteriaSelect<T> {

  private AbstractSelect<T> select;
  private String mapperStatement;

  public CriteriaWherePhase(final TableOrView baseTable, final LiveSQLContext context, final String mapperStatement,
      final Predicate whereCondition) {
    this.select = new Select<T>(context, null, false, true);
    this.select.setBaseTableExpression(baseTable);
    this.select.setWhereCondition(whereCondition);
    this.mapperStatement = mapperStatement;
  }

  // same phase

  // next phases

  public CriteriaOrderByPhase<T> orderBy(final OrderingTerm... orderingTerms) {
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
    return new CriteriaOrderByPhase<T>(this.select, this.mapperStatement);
  }

  public CriteriaOffsetPhase<T> offset(final int offset) {
    this.select.setOffset(offset);
    return new CriteriaOffsetPhase<T>(this.select, this.mapperStatement);
  }

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.select.setLimit(limit);
    return new CriteriaLimitPhase<T>(this.select, this.mapperStatement);
  }

  // execute

  public List<T> execute() {
    return this.select.execute(this.mapperStatement);
  }

  public Cursor<T> executeCursor() {
    return this.select.executeCursor(this.mapperStatement);
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
