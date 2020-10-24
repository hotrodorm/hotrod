package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.NumberWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class CountRows extends NumberFunction implements WindowableAggregationFunction {

  public CountRows() {
    super("count", (String) null, BoxUtil.box("*"));
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new WindowExpression(this));
  }

}
