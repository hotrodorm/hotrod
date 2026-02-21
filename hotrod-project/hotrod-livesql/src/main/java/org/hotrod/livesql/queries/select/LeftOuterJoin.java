package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.metadata.EntityColumnMetadata;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class LeftOuterJoin extends PredicatedJoin {

  public LeftOuterJoin(final TableExpression tableExpression, final Predicate predicate) {
    super(tableExpression, predicate);
  }

  public LeftOuterJoin(final TableExpression tableExpression, final EntityColumnMetadata... using) {
    super(tableExpression, using);
  }

}
