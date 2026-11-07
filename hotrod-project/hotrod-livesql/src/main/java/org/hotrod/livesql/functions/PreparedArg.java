package org.hotrod.livesql.functions;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.QueryWriter;

public class PreparedArg extends PreparedSegment {

  private ComparableExpression arg;

  private PreparedArg(ComparableExpression ar) {
    this.arg = ar;
  }

  static PreparedArg of(ComparableExpression arg) {
    return new PreparedArg(arg);
  }

  @Override
  void renderTo(QueryWriter w) {
    Shield.renderTo(this.arg, w);
  }

}