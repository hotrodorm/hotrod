package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class LeftOuterJoin extends PredicatedJoin {

  public LeftOuterJoin(final TableExpression tableExpression, final Predicate predicate) {
    super(tableExpression, predicate);
  }

  public LeftOuterJoin(final TableExpression tableExpression, final EntityColumn... using) {
    super(tableExpression, using);
  }

}
