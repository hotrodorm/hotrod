package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.datetime.DateTimeExpression;

public class DateTimeCaseElseStage {

  private DateTimeCaseClause clause;

  public DateTimeCaseElseStage(final DateTimeCaseClause clause) {
    this.clause = clause;
  }

  public DateTimeExpression end() {
    return this.clause;
  }

}
