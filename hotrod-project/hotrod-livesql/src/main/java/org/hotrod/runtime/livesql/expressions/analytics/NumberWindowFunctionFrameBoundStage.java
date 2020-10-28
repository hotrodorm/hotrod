package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameExclusion;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class NumberWindowFunctionFrameBoundStage {

  private NumberWindowExpression function;

  public NumberWindowFunctionFrameBoundStage(final NumberWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumberWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new NumberWindowFunctionFrameExcludeStage(this.function);
  }

  public NumberWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new NumberWindowFunctionFrameExcludeStage(this.function);
  }

  public NumberWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new NumberWindowFunctionFrameExcludeStage(this.function);
  }

  public NumberWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new NumberWindowFunctionFrameExcludeStage(this.function);
  }

  public NumberExpression end() {
    return this.function;
  }

}
