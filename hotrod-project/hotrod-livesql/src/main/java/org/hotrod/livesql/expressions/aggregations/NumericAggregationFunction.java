package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.dialects.AggregationFuntionFilterRenderer;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.analytics.NumericWindowExpression;
import org.hotrod.livesql.expressions.analytics.NumericWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numeric.NumericFunction;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class NumericAggregationFunction extends NumericFunction implements WindowableAggregationFunction {

  private String name;
  private ComparableExpression expression;
  private boolean distinct = false;
  private Predicate filter = null;

  public NumericAggregationFunction(final String name, final ComparableExpression expression) {
    super(expression);
    this.name = name;
    this.expression = expression;
  }

  protected NumericAggregationFunction(final NumericAggregationFunction preparedAndRegistered, Predicate filter) {
    super(preparedAndRegistered);
    this.name = preparedAndRegistered.name;
    this.expression = preparedAndRegistered.expression;
    this.distinct = preparedAndRegistered.distinct;
    this.filter = filter;
  }

  protected void setDistinct() {
    this.distinct = true;
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    AggregationFuntionFilterRenderer r = w.getSQLDialect().getAggregationFuntionFilterRenderer();
    r.renderTo(w, this.name, this.expression, this.distinct, this.filter);
  }

}
