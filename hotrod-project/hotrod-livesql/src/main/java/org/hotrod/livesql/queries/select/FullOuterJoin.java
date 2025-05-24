package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.metadata.EntityColumn;

public class FullOuterJoin extends PredicatedJoin {

  public FullOuterJoin(final TableExpression tableExpression, final GeneralBooleanExpression predicate) {
    super(tableExpression, predicate);
  }

  public FullOuterJoin(final TableExpression tableExpression, final EntityColumn... using) {
    super(tableExpression, using);
  }

}
