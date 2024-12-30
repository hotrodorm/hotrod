package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaWherePhase<T> extends CriteriaPhase<T> {

  public CriteriaWherePhase(final LiveSQLContext context, final TableOrView baseTable,
      final GeneralBooleanExpression whereCondition) {
    super(context, new SelectObject<T>(null, false, true));
    super.select.setBaseTableExpression(baseTable);
    super.select.setWhereCondition(whereCondition);
  }

  // next phases

  public CriteriaOrderByPhase<T> orderBy(final OrderingTerm... orderingTerms) {
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
    return new CriteriaOrderByPhase<T>(this.context, this.select);
  }

  public CriteriaOffsetPhase<T> offset(final int offset) {
    this.select.setOffset(offset);
    return new CriteriaOffsetPhase<T>(this.context, this.select);
  }

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.select.setLimit(limit);
    return new CriteriaLimitPhase<T>(this.context, this.select);
  }

  public CriteriaForUpdatePhase<T> forUpdate() {
    this.select.setForUpdate();
    return new CriteriaForUpdatePhase<T>(this.context, this.select);
  }

  public CriteriaForUpdatePhase<T> forShare() {
    this.select.setForShare();
    return new CriteriaForUpdatePhase<T>(this.context, this.select);
  }

}
