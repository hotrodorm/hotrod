package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class ObjectCaseWhenStage {

  private ObjectCaseClause clause;

  public ObjectCaseWhenStage(final Predicate predicate, final ObjectExpression value) {
    this.clause = new ObjectCaseClause(predicate, value);
  }

  // Same stage

  public ObjectCaseWhenStage when(final Predicate predicate, final ObjectExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public ObjectCaseWhenStage when(final Predicate predicate, final Object value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public ObjectCaseElseStage elseValue(final ObjectExpression value) {
    this.clause.setElse(value);
    return new ObjectCaseElseStage(this.clause);
  }

  public ObjectCaseElseStage elseValue(final Object value) {
    this.clause.setElse(BoxUtil.box(value));
    return new ObjectCaseElseStage(this.clause);
  }

  // Finishes the clause

  public ObjectExpression end() {
    return this.clause;
  }

}
