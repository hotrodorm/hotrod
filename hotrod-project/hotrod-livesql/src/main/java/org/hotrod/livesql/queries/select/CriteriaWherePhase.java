package org.hotrod.livesql.queries.select;

import java.util.Arrays;

import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class CriteriaWherePhase<T> extends CriteriaPhase<T> {

  public CriteriaWherePhase(final LiveSQLContext context, final TableOrView<?> baseTable,
      final Predicate whereCondition, RowReader<T> rowReader, LiveSQLLogging logger) {
    super(context, new FlatSelectObject<T>(null, false, true), rowReader, logger);
    super.select.setBaseTableExpression(baseTable);
    super.select.setWhereCondition(whereCondition);
  }

  // next phases

  public CriteriaOrderByPhase<T> orderBy(OrderingTerm... orderingTerms) {
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
    return new CriteriaOrderByPhase<T>(this);
  }

  public CriteriaOffsetPhase<T> offset(final int offset) {
    this.select.setOffset(offset);
    return new CriteriaOffsetPhase<T>(this);
  }

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.select.setLimit(limit);
    return new CriteriaLimitPhase<T>(this);
  }

  public CriteriaForUpdatePhase<T> forUpdate() {
    this.select.setForUpdate();
    return new CriteriaForUpdatePhase<T>(this);
  }

  public CriteriaForUpdatePhase<T> forShare() {
    this.select.setForShare();
    return new CriteriaForUpdatePhase<T>(this);
  }

}
