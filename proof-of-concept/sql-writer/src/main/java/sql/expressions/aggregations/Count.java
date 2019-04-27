package sql.expressions.aggregations;

import sql.expressions.Expression;

public class Count extends AggregationFunction {

  public Count() {
    super("count", null, Expression.toList(new LiteralString("*")));
  }

}
