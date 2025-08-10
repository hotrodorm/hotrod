package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.livesql.expressions.character.CharSyntaxExpression;

public class CharWindowFunctionOrderingStage {

  private CharWindowExpression function;

  public CharWindowFunctionOrderingStage(final CharWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public CharWindowFunctionFrameUnitStage rows() {
    this.function.setFrameUnit(FrameUnit.ROWS);
    return new CharWindowFunctionFrameUnitStage(this.function);
  }

  public CharWindowFunctionFrameUnitStage groups() {
    this.function.setFrameUnit(FrameUnit.GROUPS);
    return new CharWindowFunctionFrameUnitStage(this.function);
  }

  public CharSyntaxExpression end() {
    return this.function;
  }

}
