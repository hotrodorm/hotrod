package org.hotrod.livesql.expressions.caseclause;

import java.util.Date;

import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.datetime.GeneralDateTimeExpression;
import org.hotrod.livesql.util.BoxUtil;

public class DateTimeCaseWhenStage {

  private DateTimeCaseClause clause;

  public DateTimeCaseWhenStage(final GeneralBooleanExpression predicate, final GeneralDateTimeExpression value) {
    this.clause = new DateTimeCaseClause(predicate, value);
  }

  // Same stage

  public DateTimeCaseWhenStage when(final GeneralBooleanExpression predicate, final GeneralDateTimeExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public DateTimeCaseWhenStage when(final GeneralBooleanExpression predicate, final Date value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public DateTimeCaseElseStage elseValue(final GeneralDateTimeExpression value) {
    this.clause.setElse(value);
    return new DateTimeCaseElseStage(this.clause);
  }

  public DateTimeCaseElseStage elseValue(final Date value) {
    this.clause.setElse(BoxUtil.box(value));
    return new DateTimeCaseElseStage(this.clause);
  }

  // Finishes the clause

  public DateTimeExpression end() {
    return this.clause;
  }

}
