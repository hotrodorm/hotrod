package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.datetime.DateTimeSyntaxExpression;

public class DateTimeCaseElseStage {

  private DateTimeCaseClause clause;

  public DateTimeCaseElseStage(final DateTimeCaseClause clause) {
    this.clause = clause;
  }

  public DateTimeSyntaxExpression end() {
    return this.clause;
  }

}
