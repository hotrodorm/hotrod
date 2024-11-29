package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.strings.StringExpression;

public class StringCaseElseStage {

  private StringCaseClause clause;

  public StringCaseElseStage(final StringCaseClause clause) {
    this.clause = clause;
  }

  public StringExpression end() {
    return this.clause;
  }

}
