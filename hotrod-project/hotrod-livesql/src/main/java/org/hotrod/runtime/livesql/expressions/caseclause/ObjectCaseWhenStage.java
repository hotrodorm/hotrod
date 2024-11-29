package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.object.GeneralObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class ObjectCaseWhenStage {

  private ObjectCaseClause clause;

  public ObjectCaseWhenStage(final GeneralBooleanExpression predicate, final GeneralObjectExpression value) {
    this.clause = new ObjectCaseClause(predicate, value);
  }

  // Same stage

  public ObjectCaseWhenStage when(final GeneralBooleanExpression predicate, final GeneralObjectExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public ObjectCaseWhenStage when(final GeneralBooleanExpression predicate, final Object value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public ObjectCaseElseStage elseValue(final GeneralObjectExpression value) {
    this.clause.setElse(value);
    return new ObjectCaseElseStage(this.clause);
  }

  public ObjectCaseElseStage elseValue(final Object value) {
    this.clause.setElse(BoxUtil.box(value));
    return new ObjectCaseElseStage(this.clause);
  }

  // Finishes the clause

  public GeneralObjectExpression end() {
    return this.clause;
  }

}
