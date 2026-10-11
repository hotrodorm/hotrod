package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.dialects.AggregationFuntionFilterRenderer;
import org.hotrod.livesql.expressions.analytics.CharWindowExpression;
import org.hotrod.livesql.expressions.analytics.CharWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class CharAggregationFunction extends CharFunction implements WindowableAggregationFunction {

  protected String name;
  protected CharExpression expression;
  protected Predicate filter;

  protected CharAggregationFunction(String name, CharExpression expression, Predicate filter) {
    super(expression);
    this.name = name;
    this.expression = expression;
    this.filter = filter;
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    AggregationFuntionFilterRenderer r = w.getSQLDialect().getAggregationFuntionFilterRenderer();
    r.renderTo(w, this.name, this.expression, false, this.filter);
  }

}
