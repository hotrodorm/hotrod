package org.hotrod.livesql.queries.select;

import java.util.Arrays;

import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.LiveSQLContext;

public class CriteriaWherePhase<T> extends CriteriaPhase<T> {

  public CriteriaWherePhase(final LiveSQLContext context, final TableOrView baseTable,
      final GeneralBooleanExpression whereCondition, RowReader<T> rowReader) {
    super(context, new UnarySelectObject<T>(null, false, true), rowReader);
    super.select.setBaseTableExpression(baseTable);
    super.select.setWhereCondition(whereCondition);
  }

  // next phases

  public CriteriaOrderByPhase<T> orderBy(final OrderingTerm... orderingTerms) {
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
    return new CriteriaOrderByPhase<T>(this.context, this.select, this.rowReader);
  }

  public CriteriaOffsetPhase<T> offset(final int offset) {
    this.select.setOffset(offset);
    return new CriteriaOffsetPhase<T>(this.context, this.select, this.rowReader);
  }

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.select.setLimit(limit);
    return new CriteriaLimitPhase<T>(this.context, this.select, this.rowReader);
  }

  public CriteriaForUpdatePhase<T> forUpdate() {
    this.select.setForUpdate();
    return new CriteriaForUpdatePhase<T>(this.context, this.select, this.rowReader);
  }

  public CriteriaForUpdatePhase<T> forShare() {
    this.select.setForShare();
    return new CriteriaForUpdatePhase<T>(this.context, this.select, this.rowReader);
  }

}
