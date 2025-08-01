package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.character.GeneralCharExpression;
import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.util.BoxUtil;

public class CharCaseWhenStage {

  private CharCaseClause clause;

  public CharCaseWhenStage(final GeneralBooleanExpression predicate, final GeneralCharExpression value) {
    this.clause = new CharCaseClause(predicate, value);
  }

  // Same stage

  public CharCaseWhenStage when(final GeneralBooleanExpression predicate, final GeneralCharExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public CharCaseWhenStage when(final GeneralBooleanExpression predicate, final String value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public CharCaseElseStage elseValue(final GeneralCharExpression value) {
    this.clause.setElse(value);
    return new CharCaseElseStage(this.clause);
  }

  public CharCaseElseStage elseValue(final String value) {
    this.clause.setElse(BoxUtil.box(value));
    return new CharCaseElseStage(this.clause);
  }

  // Finishes the clause

  public CharExpression end() {
    return this.clause;
  }

}
