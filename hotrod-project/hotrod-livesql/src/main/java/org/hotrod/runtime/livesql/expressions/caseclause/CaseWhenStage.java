package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class CaseWhenStage<T extends Expression> {

  private CaseClause<T> clause;

  public CaseWhenStage(final Predicate predicate, final T value) {
    this.clause = new CaseClause<T>(predicate, value);
  }

  // Same stage

  public CaseWhenStage<T> when(final Predicate predicate, final T value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  // TODO: validate non-boxed types such as int, string constants

//  public CaseWhenStage<T> when(final Predicate predicate, final T value) {
//    this.clause.addWhen(predicate, BoxUtil.boxTyped(value));
//    return this;
//  }

  // Next stages

  public CaseElseStage<T> elseValue(final T value) {
    this.clause.setElse(value);
    return new CaseElseStage<T>(this.clause);
  }

//  public CaseElseStage<T> elseValue(final T value) {
//    this.clause.setElse(BoxUtil.boxTyped(value));
//    return new CaseElseStage<T>(this.clause);
//  }

  // Finishes the clause

  public CaseClause<T> end() {
    return this.clause;
  }

}
