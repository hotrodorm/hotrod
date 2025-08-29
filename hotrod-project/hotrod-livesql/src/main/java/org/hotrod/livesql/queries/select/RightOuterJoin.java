package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class RightOuterJoin extends PredicatedJoin {

  public RightOuterJoin(final TableExpression tableExpression, final Predicate on) {
    super(tableExpression, on);
  }

  public RightOuterJoin(final TableExpression tableExpression, final EntityColumn... using) {
    super(tableExpression, using);
  }

}
