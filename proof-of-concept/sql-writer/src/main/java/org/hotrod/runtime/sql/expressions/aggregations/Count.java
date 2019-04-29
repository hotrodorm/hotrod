package org.hotrod.runtime.sql.expressions.aggregations;

public class Count extends NumericAggregationFunction {

  public Count() {
    super("count", null, NumericAggregationFunction.toList(new LiteralString("*")));
  }

}
