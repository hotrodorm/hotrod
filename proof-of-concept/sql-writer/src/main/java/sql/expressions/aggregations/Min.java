package sql.expressions.aggregations;

import sql.expressions.Expression;

public class Min extends AggregationFunction {

  protected Min(final Expression expression) {
    super("min", null, Expression.toList(expression));
  }

}
