package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.expressions.bool.BooleanExpression;
import org.hotrod.livesql.metadata.EntityColumn;

public class RightOuterJoin extends PredicatedJoin {

  public RightOuterJoin(final TableExpression tableExpression, final BooleanExpression on) {
    super(tableExpression, on);
  }

  public RightOuterJoin(final TableExpression tableExpression, final EntityColumn... using) {
    super(tableExpression, using);
  }

}
