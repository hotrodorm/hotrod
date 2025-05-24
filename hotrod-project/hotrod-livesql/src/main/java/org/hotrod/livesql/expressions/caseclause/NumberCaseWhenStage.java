package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.numbers.NumberExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.util.BoxUtil;

public class NumberCaseWhenStage {

  private NumberCaseClause clause;

  public NumberCaseWhenStage(final GeneralBooleanExpression predicate, final GeneralNumberExpression value) {
    this.clause = new NumberCaseClause(predicate, value);
  }

  // Same stage

  public NumberCaseWhenStage when(final GeneralBooleanExpression predicate, final GeneralNumberExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public NumberCaseWhenStage when(final GeneralBooleanExpression predicate, final Number value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public NumberCaseElseStage elseValue(final GeneralNumberExpression value) {
    this.clause.setElse(value);
    return new NumberCaseElseStage(this.clause);
  }

  public NumberCaseElseStage elseValue(final Number value) {
    this.clause.setElse(BoxUtil.box(value));
    return new NumberCaseElseStage(this.clause);
  }

  // Finishes the clause

  public NumberExpression end() {
    return this.clause;
  }

}
