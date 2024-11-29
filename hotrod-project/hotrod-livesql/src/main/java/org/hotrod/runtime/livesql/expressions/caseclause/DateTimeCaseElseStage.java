package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.datetime.GeneralDateTimeExpression;

public class DateTimeCaseElseStage {

  private DateTimeCaseClause clause;

  public DateTimeCaseElseStage(final DateTimeCaseClause clause) {
    this.clause = clause;
  }

  public GeneralDateTimeExpression end() {
    return this.clause;
  }

}
