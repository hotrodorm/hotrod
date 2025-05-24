package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.metadata.EntityColumn;

public class LeftOuterJoin extends PredicatedJoin {

  public LeftOuterJoin(final TableExpression tableExpression, final GeneralBooleanExpression predicate) {
    super(tableExpression, predicate);
  }

  public LeftOuterJoin(final TableExpression tableExpression, final EntityColumn... using) {
    super(tableExpression, using);
  }

}
