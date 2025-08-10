package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.character.CharSyntaxExpression;

public class CharCaseElseStage {

  private CharCaseClause clause;

  public CharCaseElseStage(final CharCaseClause clause) {
    this.clause = clause;
  }

  public CharSyntaxExpression end() {
    return this.clause;
  }

}
