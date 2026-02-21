package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.metadata.EntityColumnMetadata;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class InnerJoin extends PredicatedJoin {

  public InnerJoin(final TableExpression tableExpression, final Predicate predicate) {
    super(tableExpression, predicate);
  }

  public InnerJoin(final TableExpression tableExpression, final EntityColumnMetadata... using) {
    super(tableExpression, using);
  }

}
