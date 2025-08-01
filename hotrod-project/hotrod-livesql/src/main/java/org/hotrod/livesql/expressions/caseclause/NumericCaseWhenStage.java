package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.util.BoxUtil;

public class NumericCaseWhenStage {

  private NumericCaseClause clause;

  public NumericCaseWhenStage(final GeneralBooleanExpression predicate, final GeneralNumericExpression value) {
    this.clause = new NumericCaseClause(predicate, value);
  }

  // Same stage

  public NumericCaseWhenStage when(final GeneralBooleanExpression predicate, final GeneralNumericExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public NumericCaseWhenStage when(final GeneralBooleanExpression predicate, final Number value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public NumericCaseElseStage elseValue(final GeneralNumericExpression value) {
    this.clause.setElse(value);
    return new NumericCaseElseStage(this.clause);
  }

  public NumericCaseElseStage elseValue(final Number value) {
    this.clause.setElse(BoxUtil.box(value));
    return new NumericCaseElseStage(this.clause);
  }

  // Finishes the clause

  public NumericExpression end() {
    return this.clause;
  }

}
