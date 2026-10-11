package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.dialects.AggregationFuntionFilterRenderer;
import org.hotrod.livesql.expressions.analytics.DateTimeWindowExpression;
import org.hotrod.livesql.expressions.analytics.DateTimeWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class DateTimeAggregationFunction extends DateTimeFunction implements WindowableAggregationFunction {

  protected String name;
  protected DateTimeExpression expression;
  protected Predicate filter;

  protected DateTimeAggregationFunction(String name, DateTimeExpression expression, Predicate filter) {
    super(expression);
    this.name = name;
    this.expression = expression;
    this.filter = filter;
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    AggregationFuntionFilterRenderer r = w.getSQLDialect().getAggregationFuntionFilterRenderer();
    r.renderTo(w, this.name, this.expression, false, this.filter);
  }

}
