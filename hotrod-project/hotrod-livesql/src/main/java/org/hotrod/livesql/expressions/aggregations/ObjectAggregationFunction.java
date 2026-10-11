package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.dialects.AggregationFuntionFilterRenderer;
import org.hotrod.livesql.expressions.analytics.ObjectWindowExpression;
import org.hotrod.livesql.expressions.analytics.ObjectWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class ObjectAggregationFunction extends ObjectFunction implements WindowableAggregationFunction {

  protected String name;
  protected ObjectExpression expression;
  protected Predicate filter;

  protected ObjectAggregationFunction(String name, ObjectExpression expression, Predicate filter) {
    super(expression);
    this.name = name;
    this.expression = expression;
    this.filter = filter;
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    AggregationFuntionFilterRenderer r = w.getSQLDialect().getAggregationFuntionFilterRenderer();
    r.renderTo(w, this.name, this.expression, false, this.filter);
  }

}
