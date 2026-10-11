package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.dialects.AggregationFuntionFilterRenderer;
import org.hotrod.livesql.expressions.analytics.BooleanWindowExpression;
import org.hotrod.livesql.expressions.analytics.BooleanWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.bool.BooleanFunction;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanAggregationFunction extends BooleanFunction implements WindowableAggregationFunction {

  protected String name;
  protected Predicate expression;
  protected Predicate filter;

  protected BooleanAggregationFunction(String name, Predicate expression, Predicate filter) {
    super(expression);
    this.name = name;
    this.expression = expression;
    this.filter = filter;
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    AggregationFuntionFilterRenderer r = w.getSQLDialect().getAggregationFuntionFilterRenderer();
    r.renderTo(w, this.name, this.expression, false, this.filter);
  }

}
