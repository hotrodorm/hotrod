package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.analytics.WindowExpression.FrameExclusion;
import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;

public class NumericWindowFunctionFrameBoundStage {

  private NumericWindowExpression function;

  public NumericWindowFunctionFrameBoundStage(final NumericWindowExpression function) {
    this.function = function;
  }

  // Next stages

  public NumericWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new NumericWindowFunctionFrameExcludeStage(this.function);
  }

  public NumericWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new NumericWindowFunctionFrameExcludeStage(this.function);
  }

  public NumericWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new NumericWindowFunctionFrameExcludeStage(this.function);
  }

  public NumericWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new NumericWindowFunctionFrameExcludeStage(this.function);
  }

  public NumericSyntaxExpression end() {
    return this.function;
  }

}
