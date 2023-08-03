package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;

public class RightOuterJoin extends PredicatedJoin {

  RightOuterJoin(final TableExpression tableExpression, final Predicate on) {
    super(tableExpression, on);
  }

  RightOuterJoin(final TableExpression tableExpression, final Column... using) {
    super(tableExpression, using);
  }

}
