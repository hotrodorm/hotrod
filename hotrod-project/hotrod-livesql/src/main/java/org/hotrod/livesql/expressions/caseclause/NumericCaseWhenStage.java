package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.bool.BooleanExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;
import org.hotrod.livesql.util.BoxUtil;

public class NumericCaseWhenStage {

  private NumericCaseClause clause;

  public NumericCaseWhenStage(final BooleanExpression predicate, final NumericExpression value) {
    this.clause = new NumericCaseClause(predicate, value);
  }

  // Same stage

  public NumericCaseWhenStage when(final BooleanExpression predicate, final NumericExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public NumericCaseWhenStage when(final BooleanExpression predicate, final Number value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public NumericCaseElseStage elseValue(final NumericExpression value) {
    this.clause.setElse(value);
    return new NumericCaseElseStage(this.clause);
  }

  public NumericCaseElseStage elseValue(final Number value) {
    this.clause.setElse(BoxUtil.box(value));
    return new NumericCaseElseStage(this.clause);
  }

  // Finishes the clause

  public NumericSyntaxExpression end() {
    return this.clause;
  }

}
