package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Count extends AggregationFunction {

  public Count() {
    super("count", null, Expression.toList(new LiteralString("*")));
  }

}
