package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class FullOuterJoin extends PredicatedJoin {

  public FullOuterJoin(final TableExpression tableExpression, final Predicate predicate) {
    super(tableExpression, predicate);
  }

  public FullOuterJoin(final TableExpression tableExpression, final EntityColumn... using) {
    super(tableExpression, using);
  }

}
