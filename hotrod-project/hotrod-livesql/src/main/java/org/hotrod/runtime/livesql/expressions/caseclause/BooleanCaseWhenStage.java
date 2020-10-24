package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.util.BoxUtil;

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

  public Predicate end() {
    return this.clause;
  }

}
