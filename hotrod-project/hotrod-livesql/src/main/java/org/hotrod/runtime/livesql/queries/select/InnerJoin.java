package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.runtime.livesql.metadata.Column;

public class InnerJoin extends PredicatedJoin {

  public InnerJoin(final TableExpression tableExpression, final GeneralBooleanExpression predicate) {
    super(tableExpression, predicate);
  }

  public InnerJoin(final TableExpression tableExpression, final Column... using) {
    super(tableExpression, using);
  }

}
