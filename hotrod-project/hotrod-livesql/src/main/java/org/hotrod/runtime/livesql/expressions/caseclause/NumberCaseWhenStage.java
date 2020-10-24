package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class NumberCaseWhenStage {

  private NumberCaseClause clause;

  public NumberCaseWhenStage(final Predicate predicate, final NumberExpression value) {
    this.clause = new NumberCaseClause(predicate, value);
  }

  // Same stage

  public NumberCaseWhenStage when(final Predicate predicate, final NumberExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public NumberCaseWhenStage when(final Predicate predicate, final Number value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public NumberCaseElseStage elseValue(final NumberExpression value) {
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
