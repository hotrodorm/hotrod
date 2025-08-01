package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.character.CharExpression;

public class CharCaseElseStage {

  private CharCaseClause clause;

  public CharCaseElseStage(final CharCaseClause clause) {
    this.clause = clause;
  }

  public CharExpression end() {
    return this.clause;
  }

}
