package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.predicates.BooleanExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.util.BoxUtil;

public class BooleanCaseWhenStage {

  private BooleanCaseClause clause;

  public BooleanCaseWhenStage(final GeneralBooleanExpression predicate, final GeneralBooleanExpression value) {
    this.clause = new BooleanCaseClause(predicate, value);
  }

  // Same stage

  public BooleanCaseWhenStage when(final GeneralBooleanExpression predicate, final GeneralBooleanExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public BooleanCaseWhenStage when(final GeneralBooleanExpression predicate, final Boolean value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public BooleanCaseElseStage elseValue(final GeneralBooleanExpression value) {
    this.clause.setElse(value);
    return new BooleanCaseElseStage(this.clause);
  }

  public BooleanCaseElseStage elseValue(final Boolean value) {
    this.clause.setElse(BoxUtil.box(value));
    return new BooleanCaseElseStage(this.clause);
  }

  // Finishes the clause

  public BooleanExpression end() {
    return this.clause;
  }

}
