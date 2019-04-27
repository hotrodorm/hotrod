package sql.expressions.aggregations;

import sql.expressions.Expression;

public class Avg extends AggregationFunction {

  protected Avg(final Expression expression) {
    super("avg", null, Expression.toList(expression));
  }

}
