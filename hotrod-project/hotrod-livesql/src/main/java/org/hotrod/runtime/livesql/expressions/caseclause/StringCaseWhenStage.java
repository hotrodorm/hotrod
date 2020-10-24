package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class StringCaseWhenStage {

  private StringCaseClause clause;

  public StringCaseWhenStage(final Predicate predicate, final StringExpression value) {
    this.clause = new StringCaseClause(predicate, value);
  }

  // Same stage

  public StringCaseWhenStage when(final Predicate predicate, final StringExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public StringCaseWhenStage when(final Predicate predicate, final String value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public StringCaseElseStage elseValue(final StringExpression value) {
    this.clause.setElse(value);
    return new StringCaseElseStage(this.clause);
  }

  public StringCaseElseStage elseValue(final String value) {
    this.clause.setElse(BoxUtil.box(value));
    return new StringCaseElseStage(this.clause);
  }

  // Finishes the clause

  public StringExpression end() {
    return this.clause;
  }

}
