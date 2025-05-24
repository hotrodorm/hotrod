package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.metadata.EntityColumn;

public class InnerJoin extends PredicatedJoin {

  public InnerJoin(final TableExpression tableExpression, final GeneralBooleanExpression predicate) {
    super(tableExpression, predicate);
  }

  public InnerJoin(final TableExpression tableExpression, final EntityColumn... using) {
    super(tableExpression, using);
  }

}
