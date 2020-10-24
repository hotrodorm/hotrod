package org.hotrod.runtime.livesql.expressions.caseclause;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class DateTimeCaseWhenStage {

  private DateTimeCaseClause clause;

  public DateTimeCaseWhenStage(final Predicate predicate, final DateTimeExpression value) {
    this.clause = new DateTimeCaseClause(predicate, value);
  }

  // Same stage

  public DateTimeCaseWhenStage when(final Predicate predicate, final DateTimeExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public DateTimeCaseWhenStage when(final Predicate predicate, final Date value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public DateTimeCaseElseStage elseValue(final DateTimeExpression value) {
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
