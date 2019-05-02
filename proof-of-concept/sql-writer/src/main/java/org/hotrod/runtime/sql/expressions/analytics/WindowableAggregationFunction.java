package org.hotrod.runtime.sql.expressions.analytics;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.aggregations.AggregationFunction;

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
