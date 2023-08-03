package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;

public class FullOuterJoin extends PredicatedJoin {

  FullOuterJoin(final TableExpression tableExpression, final Predicate predicate) {
    super(tableExpression, predicate);
  }

  FullOuterJoin(final TableExpression tableExpression, final Column... using) {
    super(tableExpression, using);
  }

}
