package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.Expression;

public class CaseElseStage<T extends Expression> {

  private CaseClause<T> clause;

  public CaseElseStage(final CaseClause<T> clause) {
    this.clause = clause;
  }

  public CaseClause<T> end() {
    return this.clause;
  }

}
