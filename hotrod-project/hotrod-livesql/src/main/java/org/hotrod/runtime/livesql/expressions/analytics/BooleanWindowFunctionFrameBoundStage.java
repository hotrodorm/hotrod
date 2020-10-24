package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameExclusion;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanWindowFunctionFrameBoundStage {

  private BooleanWindowExpression function;

  public BooleanWindowFunctionFrameBoundStage(final BooleanWindowExpression function) {
    this.function = function;
  }

  // Next stages BooleanWindowFunction

  public BooleanWindowFunctionFrameExcludeStage excludeCurrentRow() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_CURRENT_ROW);
    return new BooleanWindowFunctionFrameExcludeStage(this.function);
  }

  public BooleanWindowFunctionFrameExcludeStage excludeGroup() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_GROUP);
    return new BooleanWindowFunctionFrameExcludeStage(this.function);
  }

  public BooleanWindowFunctionFrameExcludeStage excludeTies() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_TIES);
    return new BooleanWindowFunctionFrameExcludeStage(this.function);
  }

  public BooleanWindowFunctionFrameExcludeStage excludeNoOthers() {
    this.function.setFrameExclusion(FrameExclusion.EXCLUDE_NO_OTHERS);
    return new BooleanWindowFunctionFrameExcludeStage(this.function);
  }

  public Predicate end() {
    return this.function;
  }

}
