package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class AggregationFunction<T> extends Expression<T> {

  protected AggregationFunction(int precedence) {
    super(precedence);
  }

}
