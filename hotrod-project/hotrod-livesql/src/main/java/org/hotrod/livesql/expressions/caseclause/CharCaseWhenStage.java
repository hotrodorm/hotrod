package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.character.CharSyntaxExpression;
import org.hotrod.livesql.util.BoxUtil;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class CharCaseWhenStage {

  private CharCaseClause clause;

  public CharCaseWhenStage(final Predicate predicate, final CharExpression value) {
    this.clause = new CharCaseClause(predicate, value);
  }

  // Same stage

  public CharCaseWhenStage when(final Predicate predicate, final CharExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public CharCaseWhenStage when(final Predicate predicate, final String value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public CharCaseElseStage elseValue(final CharExpression value) {
    this.clause.setElse(value);
    return new CharCaseElseStage(this.clause);
  }

  public CharCaseElseStage elseValue(final String value) {
    this.clause.setElse(BoxUtil.box(value));
    return new CharCaseElseStage(this.clause);
  }

  // Finishes the clause

  public CharSyntaxExpression end() {
    return this.clause;
  }

}
