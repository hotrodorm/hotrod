package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;

public class StringWindowFunctionOrderingStage {

  private StringWindowExpression function;

  public StringWindowFunctionOrderingStage(final StringWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public StringWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new StringWindowFunctionFrameUnitStage(this.function);
  }

  public StringWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new StringWindowFunctionFrameUnitStage(this.function);
  }

  public StringExpression end() {
    return this.function;
  }

}
