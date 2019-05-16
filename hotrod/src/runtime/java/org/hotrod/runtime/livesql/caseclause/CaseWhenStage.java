package org.hotrod.runtime.livesql.caseclause;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class CaseWhenStage<T> {

  private CaseClause<T> clause;

  public CaseWhenStage(final Predicate predicate, final Expression<T> value) {
    this.clause = new CaseClause<T>(predicate, value);
  }

  // Same stage

  public CaseWhenStage<T> when(final Predicate predicate, final Expression<T> value) {
    this.clause.add(predicate, value);
    return this;
  }

  public CaseWhenStage<T> when(final Predicate predicate, final T value) {
    this.clause.add(predicate, BoxUtil.boxTyped(value));
    return this;
  }

  // Next stages

  public CaseElseStage<T> elseValue(final Expression<T> value) {
    this.clause.setElse(value);
    return new CaseElseStage<T>(this.clause);
  }

  public CaseElseStage<T> elseValue(final T value) {
    this.clause.setElse(BoxUtil.boxTyped(value));
    return new CaseElseStage<T>(this.clause);
  }

  // Finishes the clause

  public CaseClause<T> end() {
    return this.clause;
  }

}
