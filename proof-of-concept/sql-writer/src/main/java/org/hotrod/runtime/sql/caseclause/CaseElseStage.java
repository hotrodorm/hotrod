package org.hotrod.runtime.sql.caseclause;

public class CaseElseStage<T> {

  private CaseClause<T> clause;

  public CaseElseStage(final CaseClause<T> clause) {
    this.clause = clause;
  }

  public CaseClause<T> end() {
    return this.clause;
  }

}
