package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.aggregations.AggregationFunction;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public abstract class WindowableAggregationFunction<T> extends AggregationFunction<T> implements WindowableFunction<T> {

  protected WindowableAggregationFunction(final int precedence) {
    super(precedence);
  }

  @Override
  public WindowFunctionOverStage<T> over() {
    return new WindowFunctionOverStage<T>(new WindowExpression<T>(this));
  }

  @Override
  public void renderBaseTo(final QueryWriter w) {
    this.renderTo(w);
  }

}
