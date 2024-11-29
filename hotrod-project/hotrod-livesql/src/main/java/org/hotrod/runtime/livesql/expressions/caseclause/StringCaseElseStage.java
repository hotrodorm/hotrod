package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.strings.GeneralStringExpression;

public class StringCaseElseStage {

  private StringCaseClause clause;

  public StringCaseElseStage(final StringCaseClause clause) {
    this.clause = clause;
  }

  public GeneralStringExpression end() {
    return this.clause;
  }

}
