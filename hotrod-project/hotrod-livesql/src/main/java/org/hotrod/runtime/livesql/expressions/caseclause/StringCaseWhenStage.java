package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.runtime.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class StringCaseWhenStage {

  private StringCaseClause clause;

  public StringCaseWhenStage(final GeneralBooleanExpression predicate, final GeneralStringExpression value) {
    this.clause = new StringCaseClause(predicate, value);
  }

  // Same stage

  public StringCaseWhenStage when(final GeneralBooleanExpression predicate, final GeneralStringExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public StringCaseWhenStage when(final GeneralBooleanExpression predicate, final String value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public StringCaseElseStage elseValue(final GeneralStringExpression value) {
    this.clause.setElse(value);
    return new StringCaseElseStage(this.clause);
  }

  public StringCaseElseStage elseValue(final String value) {
    this.clause.setElse(BoxUtil.box(value));
    return new StringCaseElseStage(this.clause);
  }

  // Finishes the clause

  public GeneralStringExpression end() {
    return this.clause;
  }

}
