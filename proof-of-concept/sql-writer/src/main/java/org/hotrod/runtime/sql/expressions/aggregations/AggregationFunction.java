package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public abstract class AggregationFunction<T> extends Expression<T> {

  protected AggregationFunction(int precedence) {
    super(precedence);
  }

}
