package org.hotrod.runtime.livesql.expressions.caseclause;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.util.BoxUtil;

public class ByteArrayCaseWhenStage {

  private ByteArrayCaseClause clause;

  public ByteArrayCaseWhenStage(final Predicate predicate, final ByteArrayExpression value) {
    this.clause = new ByteArrayCaseClause(predicate, value);
  }

  // Same stage

  public ByteArrayCaseWhenStage when(final Predicate predicate, final ByteArrayExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public ByteArrayCaseWhenStage when(final Predicate predicate, final byte[] value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public ByteArrayCaseElseStage elseValue(final ByteArrayExpression value) {
    this.clause.setElse(value);
    return new ByteArrayCaseElseStage(this.clause);
  }

  public ByteArrayCaseElseStage elseValue(final byte[] value) {
    this.clause.setElse(BoxUtil.box(value));
    return new ByteArrayCaseElseStage(this.clause);
  }

  // Finishes the clause

  public ByteArrayExpression end() {
    return this.clause;
  }

}
