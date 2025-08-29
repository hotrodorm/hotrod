package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.util.BoxUtil;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanCaseWhenStage {

  private BooleanCaseClause clause;

  public BooleanCaseWhenStage(final Predicate predicate, final Predicate value) {
    this.clause = new BooleanCaseClause(predicate, value);
  }

  // Same stage

  public BooleanCaseWhenStage when(final Predicate predicate, final Predicate value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public BooleanCaseWhenStage when(final Predicate predicate, final Boolean value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public BooleanCaseElseStage elseValue(final Predicate value) {
    this.clause.setElse(value);
    return new BooleanCaseElseStage(this.clause);
  }

  public BooleanCaseElseStage elseValue(final Boolean value) {
    this.clause.setElse(BoxUtil.box(value));
    return new BooleanCaseElseStage(this.clause);
  }

  // Finishes the clause

  public BooleanSyntaxExpression end() {
    return this.clause;
  }

}
