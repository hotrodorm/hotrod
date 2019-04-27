package sql.expressions.aggregations;

import sql.expressions.Expression;

public class Sum extends AggregationFunction {

  protected Sum(final Expression expression) {
    super("sum", null, Expression.toList(expression));
  }

}
