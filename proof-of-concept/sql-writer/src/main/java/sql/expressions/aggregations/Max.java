package sql.expressions.aggregations;

import sql.expressions.Expression;

public class Max extends AggregationFunction {

  protected Max(final Expression expression) {
    super("max", null, Expression.toList(expression));
  }

}
