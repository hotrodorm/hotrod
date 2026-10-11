package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.dialects.AggregationFuntionFilterRenderer;
import org.hotrod.livesql.expressions.analytics.BinaryWindowExpression;
import org.hotrod.livesql.expressions.analytics.BinaryWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.binary.BinaryFunction;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BinaryAggregationFunction extends BinaryFunction implements WindowableAggregationFunction {

  protected String name;
  protected BinaryExpression expression;
  protected Predicate filter;

  protected BinaryAggregationFunction(String name, BinaryExpression expression, Predicate filter) {
    super(expression);
    this.name = name;
    this.expression = expression;
    this.filter = filter;
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    AggregationFuntionFilterRenderer r = w.getSQLDialect().getAggregationFuntionFilterRenderer();
    r.renderTo(w, this.name, this.expression, false, this.filter);
  }

}
